package com.fungo.community.facede;

import com.fungo.community.feign.TSFeignClient;
import com.game.common.dto.ResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TSMQFacedeService {

    private static final Logger logger = LoggerFactory.getLogger(TSMQFacedeService.class);

    @Autowired(required = false)
    private TSFeignClient tsFeignClient;

    /**
     * 预存储消息
     *
     * @param transactionMessageDto
     * @return 返回 -1 失败，1 成功
     */
    @HystrixCommand(fallbackMethod = "hystrixSaveMessageWaitingConfirm", ignoreExceptions = {Exception.class},
            commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
    public ResultDto<Long> saveMessageWaitingConfirm(TransactionMessageDto transactionMessageDto) {
        try {
            return tsFeignClient.saveMessageWaitingConfirm(transactionMessageDto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<Long>();
    }

    public ResultDto<Long> hystrixSaveMessageWaitingConfirm(TransactionMessageDto transactionMessageDto) {
        return new ResultDto<Long>();
    }


    /**
     * 确认并发送消息
     *
     * @param transactionMessageDto
     * @return 返回 -1 失败，1 成功
     */
    @HystrixCommand(fallbackMethod = "hystrixConfirmAndSendMessage", ignoreExceptions = {Exception.class},
            commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
    public ResultDto confirmAndSendMessage(TransactionMessageDto transactionMessageDto) {
        try {
            return tsFeignClient.confirmAndSendMessage(transactionMessageDto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto();
    }

    public ResultDto hystrixConfirmAndSendMessage(TransactionMessageDto transactionMessageDto) {
        return new ResultDto();
    }


    /**
     * 存储并发送消息
     *
     * @param transactionMessageDto
     * @return 返回 -1 失败，1 成功
     */
    @HystrixCommand(fallbackMethod = "hystrixSaveAndSendMessage", ignoreExceptions = {Exception.class},
            commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
    public ResultDto<Long> saveAndSendMessage(TransactionMessageDto transactionMessageDto) {
        try {
            return tsFeignClient.saveAndSendMessage(transactionMessageDto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<Long>();
    }

    public ResultDto<Long> hystrixSaveAndSendMessage(TransactionMessageDto transactionMessageDto) {
        return new ResultDto<Long>();
    }


    /**
     * 直接发送消息
     *
     * @param transactionMessageDto
     * @return 返回 -1 失败，1 成功
     */
    @HystrixCommand(fallbackMethod = "hystrixDirectSendMessage", ignoreExceptions = {Exception.class},
            commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
    public ResultDto directSendMessage(TransactionMessageDto transactionMessageDto) {
        try {
            return tsFeignClient.directSendMessage(transactionMessageDto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto();
    }

    public ResultDto hystrixDirectSendMessage(TransactionMessageDto transactionMessageDto) {
        return new ResultDto();
    }


    /**
     * 从数据库删除消息
     * @param messageId 消息ID
     * @return 返回 -1 失败，1 成功
     */
    @HystrixCommand(fallbackMethod = "hystrixDeleteMessageByMessageId", ignoreExceptions = {Exception.class},
            commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
    public ResultDto deleteMessageByMessageId(Long messageId) {
        try {
            return tsFeignClient.deleteMessageByMessageId(messageId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto();
    }

    public ResultDto hystrixDeleteMessageByMessageId(Long messageId) {
        return new ResultDto();
    }


    //----------
}
