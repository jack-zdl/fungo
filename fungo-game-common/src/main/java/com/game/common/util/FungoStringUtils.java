package com.game.common.util;


/**
 * <p>
 *  字符串 生成工具类
 * </p>
 *
 * @author mxf
 * @since 2018-12-03
 */
public class FungoStringUtils {


    /**
     * 验证字符串是否是数字
     * @param s
     * @return
     */
    public final static boolean isNumeric(String s) {
        if (s != null && !"".equals(s.trim())) {
            return s.matches("^[0-9]*$");
        } else {
            return false;
        }
    }



    //---------
}
