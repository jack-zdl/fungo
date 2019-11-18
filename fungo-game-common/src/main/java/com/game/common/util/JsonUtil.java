package com.game.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>JSON工具类</p>
 * @Date: 2019/10/28
 */
public class JsonUtil {

//解析xml
    public static Map<String,Object> getPrepayMapInfo(String str){
        String notityXml = str.replaceAll("</?xml>", "");
        Pattern pattern = Pattern.compile("<.*?/.*?>");
        Matcher matcher = pattern.matcher(notityXml);
        Pattern pattern2 = Pattern.compile("!.*]");
        Map<String, Object> mapInfo = new HashMap<>();
        while(matcher.find()) {
            String key = matcher.group().replaceAll(".*/", "");
            key = key.substring(0, key.length() - 1);
            Matcher matcher2 = pattern2.matcher(matcher.group());
            String value = matcher.group().replaceAll("</?.*?>", "");
            if(matcher2.find() && !value.equals("DATA")) {
                value = matcher2.group().replaceAll("!.*\\[", "");
                value = value.substring(0, value.length() - 2);
            }
            mapInfo.put(key, value);
        }
        return mapInfo;
    }
}
