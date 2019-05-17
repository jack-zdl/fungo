package com.game.common.ts.mq.dto;


import com.game.common.api.InputPageDto;


import java.util.Date;


/**
 * <p>
 * 分布式事务之数据消息业务领域数据封装
 * </p>
 *
 * @author mxf
 * @since 2019-05-14
 */
public class TransactionMessageDto extends InputPageDto {


    //消息业务领域类型常量定义
    /**
     * 1 系统
     */
    public static final int MESSAGE_DATA_TYPE_SYSTEM = 1;

    /**
     * 2 用户
     */
    public static final int MESSAGE_DATA_TYPE_USER = 2;

    /**
     * 7 社区
     */
    public static final int MESSAGE_DATA_TYPE_COMMUNITY = 7;

    /**
     * 3 社区-文章
     */
    public static final int MESSAGE_DATA_TYPE_POST = 3;


    /**
     * 4 社区-心情
     */
    public static final int MESSAGE_DATA_TYPE_MOOD = 4;


    /**
     * 5 游戏
     */
    public static final int MESSAGE_DATA_TYPE_GAME = 5;


    /**
     * 6 首页
     */
    public static final int MESSAGE_DATA_TYPE_PORTAL = 6;



    private static final long serialVersionUID = 1L;

    private Long messageId;
    /**
     * 消息内容,数据格式:json
     */
    private String messageBody;
    /**
     * 消息业务领域类型:
     * 1 系统
     * 2 用户
     * 7 社区
     * 3 社区-文章
     * 4 社区-心情
     * 5 游戏
     * 6 首页
     */
    private Integer messageDataType;
    /**
     * 消费队列
     */
    private String consumerQueue;
    /**
     * 消息重发次数 最大5
     */
    private Integer messageSendTimes;
    /**
     * 是否死亡: 1 是, 2 否
     */
    private Integer areadlyDead;
    /**
     * 消息状态 :
     1 待确认 , 2发送中
     */
    private Integer status;
    /**
     * 版本号
     */
    private Integer casVersion;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最后修改时间
     */
    private Date editTime;
    /**
     * 扩展字段1
     */
    private String ext1;
    /**
     * 扩展字段2
     */
    private String ext2;
    /**
     * 扩展字段3
     */
    private String ext3;

    /**
     * mq 路由key
     */
    private String routingKey;


    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Integer getMessageDataType() {
        return messageDataType;
    }

    public void setMessageDataType(Integer messageDataType) {
        this.messageDataType = messageDataType;
    }

    public String getConsumerQueue() {
        return consumerQueue;
    }

    public void setConsumerQueue(String consumerQueue) {
        this.consumerQueue = consumerQueue;
    }

    public Integer getMessageSendTimes() {
        return messageSendTimes;
    }

    public void setMessageSendTimes(Integer messageSendTimes) {
        this.messageSendTimes = messageSendTimes;
    }

    public Integer getAreadlyDead() {
        return areadlyDead;
    }

    public void setAreadlyDead(Integer areadlyDead) {
        this.areadlyDead = areadlyDead;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public Integer getCasVersion() {
        return casVersion;
    }

    public void setCasVersion(Integer casVersion) {
        this.casVersion = casVersion;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }
}
