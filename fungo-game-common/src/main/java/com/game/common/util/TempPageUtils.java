package com.game.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/5/31 17:19
 */
public class TempPageUtils {
    public static Map<String,Integer> getPageLimiter(Integer page,Integer pageSize){
        Map<String, Integer> map = new HashMap<>();
        if (page == null || page < 0){
            page = 0;
        }
        if (pageSize == null || pageSize < 0){
            pageSize = 10;
        }
        map.put("beginRow",(page-1)*pageSize);
        map.put("pageSize",pageSize);
        return map;
    }
}
