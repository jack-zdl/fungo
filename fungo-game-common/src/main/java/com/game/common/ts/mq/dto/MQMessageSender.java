package com.game.common.ts.mq.dto;


import java.io.Serializable;

/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description:rabbitMQ 消息封装
 * @Date: Create in 2018/5/8
 */
public class MQMessageSender implements Serializable {


    /**
     * 路由关键字，exchange根据这个关键字进行消息投递
     */
    private String routingKey;

    /**
     * 消息交换机，它指定消息按什么规则，路由到哪个队列
     */
    private String exchange;

    /**
     * 消息
     */
    private Object content;


    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "MQMessageSender{" +
                ", routingKey='" + routingKey + '\'' +
                ", exchange='" + exchange + '\'' +
                ", content=" + content +
                '}';
    }
}
