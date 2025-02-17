package com.cn.drawing.utils;

import com.alibaba.fastjson.JSONObject;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/23 15:30
 */
public class DrawingErrorParsingUtil {


    public static String parsingError(String errorBody) {
        final JSONObject error = parseObject(errorBody).getJSONObject("error");
        return error.getString("message");
    }
}
