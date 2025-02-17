package com.cn.drawing.scheduled;


import com.cn.common.constant.DayDataConstant;
import com.cn.common.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/10/16 10:55
 */
@Service
@RequiredArgsConstructor
public class ScheduledTodayDataService {

    private final RedisUtils redisUtils;

    @Scheduled(cron = "0 0 0 * * ?")
    public void executeTaskAtThreeAm() {
        redisUtils.delKey(DayDataConstant.NEW_USERS_TODAY);
        redisUtils.delKey(DayDataConstant.NEW_VISITS_TODAY);
        redisUtils.delKey(DayDataConstant.NEW_AMOUNT_TODAY);
    }

}
