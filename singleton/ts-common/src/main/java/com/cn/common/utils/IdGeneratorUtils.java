package com.cn.common.utils;

import cn.ipokerface.snowflake.SnowflakeIdGenerator;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@SuppressWarnings("all")
public class IdGeneratorUtils {

    private final SnowflakeIdGenerator idGenerator;

    public IdGeneratorUtils() {
        this.idGenerator = new SnowflakeIdGenerator(0, 0);
    }


    public long getSnowflakeId() {
        // 生成 用户主键ID
        return idGenerator.nextId();
    }

    public String getOrderNo() {
        // 生成订单号，格式为 "yyyyMMddHHmmss" + "SnowflakeId"
        return DateFormatUtils.format(new Date(), "yyyyMMddHH") + idGenerator.nextId();
    }
}
