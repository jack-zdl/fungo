package com.fungo.system.ts.mq.service;

import com.fungo.system.ts.mq.entity.TransactionMessageDomain;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.util.exception.BusinessException;

import java.util.Map;

/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description:分布式事务之消息服务接口<br/>
 * @Date: Create in 2018/5/8
 */
public interface ITransactionMessageService {


    /**
     * 预存储消息
     */
    public void saveMessageWaitingConfirm(TransactionMessageDto transactionMessageDto) throws BusinessException;


    /**
     * 确认并发送消息
     */
    public void confirmAndSendMessage(Long messageId) throws BusinessException;


    /**
     * 存储并发送消息
     * 1 执行完成
     * -1 执行失败
     */
    public int saveAndSendMessage(TransactionMessageDto transactionMessageDto) throws BusinessException;


    /**
     * 直接发送消息
     */
    public void directSendMessage(TransactionMessageDto transactionMessageDto) throws BusinessException;


    /**
     * 根据消息ID删除消息
     */
    public void deleteMessageByMessageId(Long messageId) throws BusinessException;




    //------------------------------------------------------------------------------------------------------------------

    /**
     * 重发消息
     */
    public void reSendMessage(TransactionMessageDto transactionMessageDto) throws BusinessException;


    /**
     * 根据messageId重发某条消息
     */
    public void reSendMessageByMessageId(Long messageId) throws BusinessException;


    /**
     * 将消息标记为死亡消息
     */
    public void setMessageToAreadlyDead(Long messageId) throws BusinessException;


    /**
     * 根据消息ID获取消息
     */
    public TransactionMessageDomain getMessageByMessageId(Long messageId) throws BusinessException;




    /**
     * 重发某个消息队列中的全部已死亡的消息.
     */
    public void reSendAllDeadMessageByQueueName(String queueName, int batchSize) throws BusinessException;


    /**
     * 分页获取消息
     * @param transactionMessageDto
     * @return
     */
    public Map<String,Object>  listPage(int pageNum,int pageSize,TransactionMessageDto transactionMessageDto) throws BusinessException ;


    /**
     * 根据创建时间，分页获取消息
     * @param transactionMessageDto
     * @param param
     * @return
     * @throws BusinessException
     */
    public Map<String,Object>  listPageWithLessCreate(TransactionMessageDto transactionMessageDto ,  Map<String ,Object> param) throws BusinessException ;

}
