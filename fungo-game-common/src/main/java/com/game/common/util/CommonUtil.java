package com.game.common.util;

import org.apache.commons.lang.StringUtils;
import java.util.concurrent.Executor;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Created by ZhangGang on 2017/8/31.
 */
public class CommonUtil {
    public static boolean isNull(String str) {
        return StringUtils.isBlank(str);
    }

    public static boolean isNull(Collection<?> collection) {
        return CollectionUtils.isEmpty(collection);
    }

    public static boolean isNull(Map<?, ?> paramMap) {
        return null == paramMap || paramMap.isEmpty();
    }

    public static boolean isNull(Object[] array) {
        return null == array || array.length == 0;
    }

    public static Executor executor ;

    public static Executor createThread(int num){
        if(executor == null){
            executor = (Executor) Executors.newFixedThreadPool(num);
        }
        return  executor;

    }
}
