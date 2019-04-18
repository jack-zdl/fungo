package com.game.common.utils.convert;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : dl.zhang
 * @date : 2018/4/2
 * @description : 注解翻译接口
 */
@Service
public interface ConvertApi {
    /***
     * @description 注解翻译，获取配置注解的属性，并调用翻译方法
     *
     * @author Cheese
     * @date 2018/4/4
     * @param
     * @return com.alibaba.fastjson.JSONObject
     */
    JSONObject convertByAnnotation(List<?> list);

    String getDicOptionToJsonByKeynoByEnum(String keyno, String keyvalue) throws ClassNotFoundException;
}
