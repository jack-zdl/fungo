package com.fungo.system.ts.mq.entity;


import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 分布式事务之数据消息业务领域实体
 * </p>
 *
 * @author mxf
 * @since 2019-05-14
 */
@TableName("t_sys_ts_msg")
public class TransactionMessageDomain extends Model<TransactionMessageDomain> {

    private static final long serialVersionUID = 1L;

    @TableId("message_id")
    private Long messageId;
    /**
     * 消息内容,数据格式:json
     */
    @TableField("message_body")
    private String messageBody;
    /**
     * 消息数据类型:
     1 任务
     2 商城
     */
    @TableField("message_dataType")
    private Integer messageDataType;
    /**
     * 消费队列
     */
    @TableField("consumer_queue")
    private String consumerQueue;

    /**
     * 消息路由key
     */
    @TableField("routing_key")
    private String routingKey;

    /**
     * 消息重发次数 最大5
     */
    @TableField("message_sendTimes")
    private Integer messageSendTimes;
    /**
     * 是否死亡: 1 是, 2 否
     */
    @TableField("areadly_dead")
    private Integer areadlyDead;
    /**
     * 消息状态 :
     1 待确认 , 2发送中
     */
    private Integer status;
    /**
     * 版本号
     */
    @TableField("cas_version")
    private Integer casVersion;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 最后修改时间
     */
    @TableField("edit_time")
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

    public Integer getCasVersion() {
        return casVersion;
    }

    public void setCasVersion(Integer casVersion) {
        this.casVersion = casVersion;
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

    @Override
    protected Serializable pkVal() {
        return this.messageId;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    /**
     * 消息发送次数
     */
    public void addSendTimes() {
        messageSendTimes++;
    }

}
