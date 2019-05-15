package com.game.common.ts.mq.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description:分布式事务之消息状态<br/>
 * @Date: Create in 2018/5/8
 */
@Getter
@AllArgsConstructor
public enum DTPMessageStatusEnum {

    WAITING_CONFIRM(1,"待确认"),

    SENDING(2,"发送中");


    private int code;
    private String value;
}
