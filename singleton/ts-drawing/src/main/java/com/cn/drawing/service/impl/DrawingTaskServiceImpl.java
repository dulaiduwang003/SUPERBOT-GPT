package com.cn.drawing.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.common.configuration.DrawingConfiguration;
import com.cn.common.entity.Workflows;
import com.cn.common.mapper.WorkflowsMapper;
import com.cn.common.structure.UserInfoStructure;
import com.cn.common.utils.EnergyUtils;
import com.cn.common.utils.RedisUtils;
import com.cn.common.utils.UserUtils;
import com.cn.drawing.constant.DrawingTaskConstant;
import com.cn.drawing.constant.DrawingWorkflowsConstant;
import com.cn.drawing.dto.SubmitTaskDto;
import com.cn.drawing.enums.DrawingStatusEnum;
import com.cn.drawing.exceptions.DrawingException;
import com.cn.drawing.service.DrawingTaskService;
import com.cn.drawing.structure.DrawingTaskResultStructure;
import com.cn.drawing.structure.DrawingTaskStructure;
import com.cn.drawing.vo.DrawingProgressVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.cn.drawing.constant.DrawingTaskConstant.DRAWING_QUEUE_INDEX;
import static com.cn.drawing.constant.DrawingTaskConstant.DRAWING_TASK_LIST;


/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/12 下午5:31
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DrawingTaskServiceImpl implements DrawingTaskService {

    private final RedisUtils redisUtils;

    private final RedissonClient redissonClient;

    private final EnergyUtils energyUtils;

    private final WorkflowsMapper workflowsMapper;

    private final DrawingConfiguration drawingConfiguration;


    private static final String SUBMIT_DRAWING_TASK_LOCK = "SUBMIT_DRAWING_TASK_LOCK:USER_ID:";


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submitTask(final SubmitTaskDto dto) {
        if (redisUtils.keySize(DrawingTaskConstant.DRAWING_QUEUE) > drawingConfiguration.getSubmitTaskMax()) {
            throw new DrawingException("使用人数过多 请稍后再试试");
        }

        //生产任务ID
        final String taskId = UUID.randomUUID().toString();
        final Long workflowsId = dto.getWorkflowsId();
        //获取工作流json
        Workflows workflows = workflowsMapper.selectOne(new QueryWrapper<Workflows>().lambda().eq(Workflows::getWorkflowsId, workflowsId));
        if (workflows == null) {
            throw new DrawingException("工作流已下架或不存在");
        }
        final UserInfoStructure currentUserInfo = UserUtils.getCurrentUserInfo();
        energyUtils.inspection(currentUserInfo.getUserId(), workflows.getEnergy());

        RLock lock = redissonClient.getLock(SUBMIT_DRAWING_TASK_LOCK + currentUserInfo.getUserId());
        try {
            if (lock.tryLock(100, 10, TimeUnit.SECONDS)) {
                try {
                    //工作流
                    JSONObject workflowsJson = JSONObject.parseObject(workflows.getJson());
                    for (SubmitTaskDto.Container update : dto.getContainers()) {
                        //重载
                        JSONObject node = workflowsJson.getJSONObject(update.getNodeKey());
                        if (node != null) {
                            JSONObject inputs = node.getJSONObject("inputs");
                            if (inputs != null) {
                                inputs.put(update.getNodeDigital(), update.getNodeValue());
                            }
                        }
                    }
                    //统计功能此工作流使用次数
                    redisUtils.setSet(DrawingWorkflowsConstant.VISITED_WORKFLOWS + workflowsId, currentUserInfo.getUserId());
                    JSONObject body = new JSONObject();
                    body.put("client_id", taskId);
                    body.put("prompt", workflowsJson);
                    final String key = DRAWING_TASK_LIST + currentUserInfo.getUserId();
                    //构建绘图任务
                    redisUtils.listPush(DrawingTaskConstant.DRAWING_QUEUE,
                            new DrawingTaskStructure()
                                    .setTaskId(taskId)
                                    .setOpenId(currentUserInfo.getOpenId())
                                    .setInputNode(workflows.getInputNode())
                                    //生成类型
                                    .setResultType(workflows.getResultType())
                                    .setJson(body)
                                    .setEnergy(workflows.getEnergy())
                                    .setWorkflowsId(dto.getWorkflowsId())
                                    .setUserId(currentUserInfo.getUserId()));
                    //扣除
                    energyUtils.deduct(currentUserInfo.getUserId(), workflows.getEnergy());
                    //构建辅助查询索引
                    redisUtils.hashPut(DRAWING_QUEUE_INDEX, taskId, redisUtils.keySize(DrawingTaskConstant.DRAWING_QUEUE));
                    final Long location = redisUtils.keySize(DrawingTaskConstant.DRAWING_QUEUE);
                    //构建查询任务
                    redisUtils.hashPut(key, taskId, new DrawingTaskResultStructure()
                            .setStatus(DrawingStatusEnum.WAIT.getDec())
                            .setResultType(workflows.getResultType())
                            .setWorkflowsName(workflows.getTitle())
                            .setWorkflowsId(dto.getWorkflowsId())
                            .setCreateTime(LocalDateTime.now())
                            .setTaskId(taskId)
                            .setLocation(location));
                    return taskId;

                } finally {
                    lock.unlock();
                }
            } else {
                throw new DrawingException("您点的太快啦,请等等再试");
            }
        } catch (InterruptedException e) {
            throw new DrawingException("系统异常,请稍后再试");
        }
    }


    @Override
    public DrawingProgressVo getTaskProgress(final String taskId) {
        DrawingTaskResultStructure value = (DrawingTaskResultStructure) redisUtils.hashGet(DRAWING_TASK_LIST + UserUtils.getCurrentLoginId(), taskId);
        if (value == null) {
            throw new DrawingException("绘图任务不存在");
        }
        DrawingProgressVo vo = new DrawingProgressVo()
                .setProgress(value.getProgress())
                .setUrl(value.getUrl())
                .setResultType(value.getResultType())
                .setCreateTime(value.getCreateTime())
                .setWorkflowsName(value.getWorkflowsName())
                .setTaskId(taskId)
                .setStatus(value.getStatus()).setWorkflowsWorksId(value.getWorkflowsWorksId());

        if (value.getStatus().equals(DrawingStatusEnum.WAIT.getDec())) {
            final Object location = redisUtils.hashGet(DRAWING_QUEUE_INDEX, taskId);
            vo.setLocation(Long.parseLong(location.toString()));
        }
        return vo;
    }

    @Override
    public List<DrawingProgressVo> getTaskProgressList() {

        Map<Object, Object> objectObjectMap = redisUtils.hashGetAll(DRAWING_TASK_LIST + UserUtils.getCurrentLoginId());
        return objectObjectMap.keySet().stream().map(c -> {
            DrawingTaskResultStructure o = (DrawingTaskResultStructure) objectObjectMap.get(c);
            final DrawingProgressVo vo = new DrawingProgressVo()
                    .setTaskId(c.toString())
                    .setUrl(o.getUrl())
                    .setWorkflowsName(o.getWorkflowsName())
                    .setLocation(o.getLocation())
                    .setStatus(o.getStatus())
                    .setResultType(o.getResultType())
                    .setProgress(o.getProgress())
                    .setWorkflowsWorksId(o.getWorkflowsWorksId())
                    .setCreateTime(o.getCreateTime());
            if (o.getStatus().equals(DrawingStatusEnum.WAIT.getDec())) {
                Object location = redisUtils.hashGet(DRAWING_QUEUE_INDEX, o.getTaskId());
                vo.setLocation(location != null ? Long.parseLong(location.toString()) : 0);
            }
            return vo;
        }).sorted(Comparator.comparing(DrawingProgressVo::getCreateTime).reversed()).toList();
    }

}
