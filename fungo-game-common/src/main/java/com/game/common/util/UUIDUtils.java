package com.game.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * <p>
 * UUID 生成工具类
 * </p>
 *
 * @author mxf
 * @since 2018-12-03
 */
public class UUIDUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(UUIDUtils.class);
    /**
     * 获得一个UUID
     *
     * @return String UUID
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-", "");
    }



    public static void main(String[] args) {
        for (int i = 0;i < 20;i++) {
            LOGGER.info(getUUID());
        }

       // LOGGER.info(getUUID());
    }


//---------
}
