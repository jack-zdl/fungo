package com.fungo.community.function.cache;

import com.game.common.consts.FunGoGameConsts;
import com.game.common.util.CommonUtil;
import com.game.common.util.FunGoEHCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.logging.Handler;

/**
 * <p>清除Ehcache缓存</p>
 * @Date: 2019/12/5
 */
@Component
public class EhcacheActionFunction {

    private static final Logger logger = LoggerFactory.getLogger( EhcacheActionFunction.class);

    // String ehcacheName, String ehcacheKey
    public boolean deletePostEhcacheHandler(Map<String,String> ehcacheMap){
        try {
            for(Map.Entry<String, String> ehcache : ehcacheMap.entrySet()){
                String ehcacheName = ehcache.getKey();
                String ehcacheKey = ehcache.getValue();
                if(CommonUtil.isNull(ehcacheKey)){
                    FunGoEHCacheUtils.removeAll(ehcacheName);
                }else {
                    FunGoEHCacheUtils.remove(ehcacheName,ehcacheKey);
                }
            }
        }catch (Exception e){
            logger.error( "EhcacheActionFunction.deletePostEhcacheHandler异常",e);
        }
        return true;
    }
}
