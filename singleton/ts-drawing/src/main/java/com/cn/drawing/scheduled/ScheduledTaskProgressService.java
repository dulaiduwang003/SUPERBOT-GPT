package com.cn.drawing.scheduled;


import com.cn.common.utils.RedisUtils;
import com.cn.drawing.constant.DrawingTaskConstant;
import com.cn.drawing.structure.DrawingTaskResultStructure;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/10/16 10:55
 */
@Service
@RequiredArgsConstructor
public class ScheduledTaskProgressService {

    private final RedisUtils redisUtils;


    @Scheduled(cron ="0 0/10 * * * ?")
    public void cleanupOldTasks() {
        execution();
    }


    private void execution() {
        Set<String> userIds = redisUtils.keys(DrawingTaskConstant.DRAWING_TASK_LIST + "*");
        for (String userIdKey : userIds) {
            Map<Object, Object> objectObjectMap = redisUtils.hashGetAll(userIdKey);
            objectObjectMap.keySet().forEach(key -> {
               DrawingTaskResultStructure task = (DrawingTaskResultStructure) objectObjectMap.get(key);
               LocalDateTime createTime = task.getCreateTime();
                    if (createTime != null && LocalDateTime.now().minus(Duration.ofHours(24)).isAfter(createTime)) {
                        // 删除超过24小时的任务
                        redisUtils.hashDelete(userIdKey, task.getTaskId());
                    }
           });

        }
    }


}
