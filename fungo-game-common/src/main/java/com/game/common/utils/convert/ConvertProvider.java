package com.game.common.utils.convert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.game.common.enums.BaseEnum;
import com.game.common.utils.annotation.EnumConver;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author : Cheese
 * @date : 2018/4/2
 * @description : 注解翻译接口的实现类
 */
@Service
public class ConvertProvider<E extends BaseEnum> implements ConvertApi{

    private Logger logger = LoggerFactory.getLogger(getClass());

    /***
     * @description 注解翻译，获取配置注解的属性，并调用翻译方法
     *
     * @author Cheese
     * @date 2018/4/4
     * @return com.alibaba.fastjson.JSONObject
     */
    @Override
    public JSONObject convertByAnnotation(List<?> list){
        if(list.size()==0){
            JSONObject jsonObject =new JSONObject();
            jsonObject.put("rows",new JSONArray());
            return jsonObject;
        }
        //获取list中的泛型的所有属性信息
        Field[] fs = list.get(0).getClass().getDeclaredFields();
        Map<String,Object> enumMap =new HashMap<>();
        Map<String,String> cacheMap =new HashMap<>();
        for(Field f:fs){
            //获取类中使用了@EnumConver注解的属性域并将枚举类型放入map
            if(f.isAnnotationPresent(EnumConver.class)){
                EnumConver annotation = f.getAnnotation(EnumConver.class);
                //获取注解中的属性值
                Class enumType = annotation.enumType();
                enumMap.put(f.getName(),enumType);
            }
        }

        //调用翻译方法
        return convert(list,enumMap,cacheMap);
    }
    /***
     * @description 注解翻译方法
     *
     * @author Cheese
     * @date 2018/4/4
     * @param [list, enumMap, cacheMap]
     * @return com.alibaba.fastjson.JSONObject
     */
    public JSONObject convert(List<?> list, Map<String,Object> enumMap, Map<String,String> cacheMap){
        //将PageView转换成JSON对象并进行翻译
        try {
            JSONObject jsonObject = new JSONObject();
            //获取列表内容
            JSONArray jsonArray = (JSONArray)JSONObject.toJSON(list);
            //获取列表内容迭代器
            Iterator<Object> it = jsonArray.iterator();
            //开始迭代
            while(it.hasNext()){
                JSONObject ob = (JSONObject)it.next();
                //遍历enumMap，并根据枚举类型翻译
                for (Map.Entry<String, Object> entry : enumMap.entrySet()) {
                    Class map =(Class)entry.getValue();
                    E[] enums = (E[])map.getEnumConstants();
                    if(ob.get(entry.getKey())==null){
                        ob.put(entry.getKey()+"Value",entry.getKey());
                    }else{
                        for(E e : enums) {
                            if(e.getKey().equals(ob.get(entry.getKey())+"")) {
                                ob.put(entry.getKey()+"Value",e.getValue());
                                ob.put(entry.getKey(), ob.get(entry.getKey()));
                            }
                        }
                    }
                }
            }
            jsonObject.put("rows",jsonArray);
            return jsonObject;
        } catch (Exception e) {
            logger.error("结果转换异常， Exception{}",e.getMessage(), e);
            return null;
        }
    }
    @Override
    public  String getDicOptionToJsonByKeynoByEnum(String keyno, String keyvalue) throws ClassNotFoundException {
        List<String> allowKeyValues = null;
        if(StringUtils.isNotBlank(keyvalue)){
            allowKeyValues = Arrays.asList(keyvalue.split(","));
        }
        Map<Object, String> map=new HashMap<>();
        E[] enums = (E[]) Class.forName("com.ceying.common.enums."+keyno).getEnumConstants();
        for(E e : enums) {
            map.put(e.getKey(),e.getValue());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (map!=null) {
            Set set = map.keySet();
            Iterator it = set.iterator();
            //判断是否需要过滤字典项
            if(allowKeyValues == null){
                while(it.hasNext()){
                    String key= it.next().toString();
                    String value = map.get(key);
                    sb.append("{\"text\":\"").append(value).append("\",\"value\":\"").append(key).append("\"},");
                }
            }else{
                while(it.hasNext()){
                    String key= it.next().toString();
                    //若key包含在allowKeyValues则加入返回的结果中
                    if(allowKeyValues.contains(key)){
                        String value = map.get(key);
                        sb.append("{\"text\":\"").append(value).append("\",\"value\":\"").append(key).append("\"},");
                    }
                }
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        return sb.toString();
    }
}
