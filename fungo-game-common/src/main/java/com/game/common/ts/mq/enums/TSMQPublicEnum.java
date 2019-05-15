package com.game.common.ts.mq.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description:公共状态之是，否<br/>
 * @Date: Create in 2018/5/8
 */
@Getter
@AllArgsConstructor
public enum TSMQPublicEnum {

    YES(1,"是"),

    NO(2 ,"否");


    private int code;
    private String value;

}
