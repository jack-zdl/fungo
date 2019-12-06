package com.game.common.util.validate;

import com.game.common.util.CommonUtil;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/12/5
 */
public class ValidateUtil {

    public static boolean checkNullAndLength(String param){
        return (CommonUtil.isNull( param ) || 32 != param.length());
    }
}
