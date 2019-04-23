package com.game.common.util.exception;


/**
 * 参数校验异常
 * Created by ZhangGang on 2016/9/5.
 */
public class ParamsException extends RuntimeException {

    private static final long serialVersionUID = 276486514583932180L;

    public ParamsException(String msg) {
        super(msg);
    }

}
