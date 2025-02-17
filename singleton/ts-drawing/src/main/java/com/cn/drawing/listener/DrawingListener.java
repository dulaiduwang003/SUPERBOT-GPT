package com.cn.drawing.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.cn.common.configuration.DrawingConfiguration;
import com.cn.common.entity.WorkflowsWorks;
import com.cn.common.enums.FilePathEnum;
import com.cn.common.mapper.WorkflowsWorksMapper;
import com.cn.common.utils.EnergyUtils;
import com.cn.common.utils.RedisUtils;
import com.cn.common.utils.UploadUtil;
import com.cn.common.utils.WeChatUtils;
import com.cn.drawing.constant.DrawingTaskConstant;
import com.cn.drawing.enums.DrawingResultTypeEnum;
import com.cn.drawing.enums.DrawingStatusEnum;
import com.cn.drawing.exceptions.DrawingException;
import com.cn.drawing.structure.DrawingTaskResultStructure;
import com.cn.drawing.structure.DrawingTaskStructure;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.cn.drawing.constant.DrawingTaskConstant.DRAWING_QUEUE_INDEX;
import static com.cn.drawing.utils.DrawingErrorParsingUtil.parsingError;


/**
 * 处理绘画
 *
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/23 14:43
 */
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("all")
@Configuration
public class DrawingListener {

    private final AtomicInteger anomalyCounters = new AtomicInteger(0);

    private final UploadUtil uploadUtil;

    private final RedisUtils redisUtils;

    private final WeChatUtils weChatUtils;

    private final EnergyUtils energyUtils;

    private final DrawingConfiguration drawingConfiguration;

    private final WorkflowsWorksMapper workflowsWorksMapper;

    private static final Random random = new Random();


    @EventListener(ApplicationReadyEvent.class)
    public void listening() {
        try {
            Semaphore semaphore = new Semaphore(Math.toIntExact(drawingConfiguration.getConcurrent()));
            new Thread(() -> {
                while (true) {
                    try {
                        semaphore.acquire();
                        //获取任务
                        final DrawingTaskStructure o = (DrawingTaskStructure) redisUtils.listPop(DrawingTaskConstant.DRAWING_QUEUE, 1, TimeUnit.SECONDS);
                        if (o != null) {
                            log.info("正在处理任务:{}", o.getTaskId());
                            execution(o);
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    } finally {
                        semaphore.release();
                    }
                }
            }).start();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void execution(final DrawingTaskStructure obj) {
        Long l = redisUtils.hashDelete(DRAWING_QUEUE_INDEX, obj.getTaskId().toString());

        try {
            Map<Object, Object> tasks = redisUtils.hashGetAll(DRAWING_QUEUE_INDEX);

            for (Map.Entry<Object, Object> entry : tasks.entrySet()) {
                Object taskId = entry.getKey();
                Integer currentValue = (Integer) entry.getValue();
                if (currentValue != null) {
                    redisUtils.hashPut(DRAWING_QUEUE_INDEX, taskId.toString(), currentValue - 1);
                }
            }


            final String block = WebClient.builder()
                    .baseUrl(drawingConfiguration.getServer())
                    .build()
                    .post()
                    .uri("/prompt")
                    .body(BodyInserters.fromValue(obj.getJson()))
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (clientResponse -> clientResponse.bodyToMono(String.class).flatMap(errorBody -> Mono.error(new DrawingException(parsingError(errorBody))))))
                    .bodyToMono(String.class).block();

            JSONObject result = parseObject(block);
            if (result != null) {
                String key = DrawingTaskConstant.DRAWING_TASK_LIST + obj.getUserId();
                final DrawingTaskResultStructure o = (DrawingTaskResultStructure) redisUtils.hashGet(key, obj.getTaskId());

                redisUtils.hashPut(key, obj.getTaskId(), o.setProgress(0L)
                        .setStatus(DrawingStatusEnum.BUILD.getDec()));
                //构建中

                obtainTaskExecutionResult(new TaskExecution()
                        .setTaskId(obj.getTaskId())
                        .setPromptId( result.getString("prompt_id"))
                        .setUserId(obj.getUserId())
                        .setOpenId(obj.getOpenId())
                        .setWorkflowsId(obj.getWorkflowsId())
                        .setResultType(obj.getResultType())
                        .setEnergy(obj.getEnergy())
                        .setInputNode(obj.getInputNode())
                );
            } else {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            energyUtils.increase(obj.getUserId(), obj.getEnergy());
            weChatUtils.sendNotice(obj.getOpenId(), false, "绘图服务貌似出了点问题");
            taskErrorStatus(obj.getUserId(), obj.getTaskId());

        }

    }


    private void obtainTaskExecutionResult(final TaskExecution taskExecution) {
        AtomicBoolean shouldStop = new AtomicBoolean(false);

        while (!shouldStop.get()) {
            try {
                handleRandomVariables(taskExecution.getUserId(), taskExecution.getTaskId());
                Thread.sleep(3000);
                final String block = WebClient.builder()
                        .baseUrl(drawingConfiguration.getServer())
                        .build()
                        .get()
                        .uri("/history/" + taskExecution.getPromptId())
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
                JSONObject jsonObject = parseObject(block);
                if (jsonObject.containsKey(taskExecution.getPromptId())) {
                    JSONObject resultJson = jsonObject.getJSONObject(taskExecution.getPromptId());
                    String fileUrl = drawingConfiguration.getServer() + "/view?filename=";
                    JSONObject outputs = resultJson.getJSONObject("outputs");
                    //如果是音频文件 需要做特殊格式处理
                    if (taskExecution.getResultType().equals(DrawingResultTypeEnum.AUDIO.getDec())) {
                        //音频处理
                        List<String> audioFromOutputs = getAudioFromOutputs(outputs, taskExecution.getInputNode());
                        fileUrl += audioFromOutputs.get(0) + "&type=" + audioFromOutputs.get(1);
                    } else if (taskExecution.getResultType().equals(DrawingResultTypeEnum.IMAGE.getDec()) || taskExecution.getResultType().equals(DrawingResultTypeEnum.VIDEO.getDec())) {
                        //图片和视频
                        JSONObject obj = getFirstImageFromSpecifiedNode(outputs, taskExecution.getInputNode());
                        fileUrl += obj.getString("filename") + "&type=" + obj.getString("type");
                    } else {
                        //3D模型
                        JSONObject obj = getModelNodeFromNode(outputs, taskExecution.getInputNode());
                        fileUrl += obj.getString("filename") + "&type=" + obj.getString("type");
                    }
                    //上传阿里OSS
                    final String uploadUrl = uploadUtil.uploadDrawingUrl(fileUrl, FilePathEnum.DRAWING.getDec());
                    final WorkflowsWorks workflowsWorks = new WorkflowsWorks()
                            .setUrl(uploadUrl)
                            .setResultType(taskExecution.getResultType())
                            .setWorkflowsId(taskExecution.getWorkflowsId())
                            .setUserId(taskExecution.getUserId());
                    workflowsWorksMapper.insert(workflowsWorks);
                    //设置redis构建消息
                    final String key = DrawingTaskConstant.DRAWING_TASK_LIST + taskExecution.getUserId();
                    DrawingTaskResultStructure o = (DrawingTaskResultStructure) redisUtils.hashGet(key, taskExecution.getTaskId());
                    redisUtils.hashPut(key, taskExecution.getTaskId(), o
                            //构建成功
                            .setUrl(uploadUrl)
                            .setStatus(DrawingStatusEnum.SUCCEED.getDec())
                            .setProgress(100L)
                            .setWorkflowsWorksId(workflowsWorks.getWorkflowsWorksId()));
                    weChatUtils.sendNotice(taskExecution.getOpenId(), true, "作品创作成功!前往个人中心查看吧!");
                    shouldStop.set(true);
                }
            } catch (Exception e) {
                energyUtils.increase(taskExecution.getUserId(), taskExecution.getEnergy());
                weChatUtils.sendNotice(taskExecution.getOpenId(), false, "抱歉!作品绘制失败!");
                taskErrorStatus(taskExecution.getUserId(), taskExecution.getTaskId());
                break;
            }
        }

    }

    private void taskErrorStatus(final Long userId, final String taskId) {
        String key = DrawingTaskConstant.DRAWING_TASK_LIST + userId;
        DrawingTaskResultStructure o = (DrawingTaskResultStructure) redisUtils.hashGet(key, taskId);
        redisUtils.hashPut(key, taskId, o
                .setStatus(DrawingStatusEnum.FAILED.getDec()));

    }


    private void handleRandomVariables(final Long userId, final String taskId) {
        final String key = DrawingTaskConstant.DRAWING_TASK_LIST + userId;
        DrawingTaskResultStructure value = (DrawingTaskResultStructure) redisUtils.hashGet(key, taskId);
        int progress = Math.toIntExact(value.getProgress());
        int maxIncrease = Math.min(4, 99 - progress);
        if (maxIncrease <= 0) {
            return;
        }
        int increase = random.nextInt(maxIncrease) + 1;
        progress += increase;
        redisUtils.hashPut(key, taskId, value.setProgress((long) progress));


    }

    public List<String> getAudioFromOutputs(final JSONObject outputsJsonObject, final String inputNode) {
        if (outputsJsonObject == null) {
            return null;
        }
        List<String> audioList = new LinkedList<>();
        for (String key : outputsJsonObject.keySet()) {
            JSONArray audioArray = outputsJsonObject.getJSONObject(key).getJSONArray("audio");
            for (int i = 0; i < audioArray.size(); i++) {
                audioList.add(audioArray.getString(i));
            }
        }

        return audioList;
    }

    public JSONObject getFirstImageFromSpecifiedNode(final JSONObject outputsJsonObject, final String inputNode) {
        if (outputsJsonObject == null || inputNode == null || inputNode.isEmpty()) {
            return null;
        }
        Object nodeValue = outputsJsonObject.get(inputNode);
        if (nodeValue instanceof JSONObject) {
            JSONObject nodeJsonObject = (JSONObject) nodeValue;
            JSONArray images = nodeJsonObject.getJSONArray("images");
            if (images != null && !images.isEmpty()) {
                return (JSONObject) images.get(0);
            } else {
                JSONArray gifs = nodeJsonObject.getJSONArray("gifs");
                if (gifs != null && !gifs.isEmpty()) {
                    return (JSONObject) gifs.get(0);
                }
            }
        }
        return null;
    }


    public JSONObject getModelNodeFromNode(final JSONObject outputsJsonObject, final String inputNode) {
        if (outputsJsonObject == null || inputNode == null || inputNode.isEmpty()) {
            return null;
        }

        // 尝试从outputsJsonObject中获取指定节点的值
        Object nodeValue = outputsJsonObject.get(inputNode);
        if (nodeValue instanceof JSONObject) {
            JSONObject nodeJsonObject = (JSONObject) nodeValue;
            return nodeJsonObject.getJSONArray("mesh").getJSONObject(0);
        }
        return null;
    }

    @Data
    @Accessors(chain = true)
    public static class TaskExecution {

        private String taskId;

        private String promptId;

        private Long userId;

        private String openId;

        private Long workflowsId;

        private String resultType;

        private Long energy;

        private String inputNode;


    }


}
