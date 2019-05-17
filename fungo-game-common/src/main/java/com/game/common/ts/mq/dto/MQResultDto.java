package com.game.common.ts.mq.dto;

/**
 * <p>提供给mq封装类</p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/17
 */
public class MQResultDto {

    private String type;

    private Object body;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
