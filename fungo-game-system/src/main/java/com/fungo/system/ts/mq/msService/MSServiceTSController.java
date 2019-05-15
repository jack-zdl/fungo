package com.fungo.system.ts.mq.msService;


import com.fungo.system.ts.mq.service.ITransactionMessageService;
import com.game.common.dto.ResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *      分布式事务之数据消息业务微服务接口
 * </p>
 * since: V3.0.0
 * @author mxf
 * @since 2019-05-10
 */
@RestController("/ms/service/dtp/mq")
public class MSServiceTSController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MSServiceTSController.class);


    @Autowired
    private ITransactionMessageService iTransactionMessageService;

    /**
     * 预存储消息
     *
     * @param transactionMessageDto
     * @return 返回 -1 失败，1 成功
     */
    @PostMapping(value = "/saveMsgWC", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    public ResultDto saveMessageWaitingConfirm(@RequestBody TransactionMessageDto transactionMessageDto) {
        ResultDto<Object> resultDto = ResultDto.error("-1", "预存储消息失败");
        ;
        if (null == transactionMessageDto) {
            resultDto = ResultDto.error("-1", "预存储消息失败-【消息不能为空】");
            return resultDto;
        }
        try {

            iTransactionMessageService.saveMessageWaitingConfirm(transactionMessageDto);
            resultDto = ResultDto.success("预存储消息成功");

        } catch (Exception ex) {
            LOGGER.error("DTP-MQ 预存储消息发生异常", ex);
        }
        return resultDto;
    }


    /**
     * 确认并发送消息
     *
     * @param transactionMessageDto
     * @return 返回 -1 失败，1 成功
     */
    @PostMapping(value = "/cSendMsg", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    public ResultDto confirmAndSendMessage(@RequestBody TransactionMessageDto transactionMessageDto) {
        ResultDto<Object> resultDto = ResultDto.error("-1", "确认并发送消息失败");
        if (null == transactionMessageDto) {
            resultDto = ResultDto.error("-1", "确认并发送消息失败-【消息不能为空】");
            return resultDto;
        }
        try {

            iTransactionMessageService.confirmAndSendMessage(transactionMessageDto.getMessageId());
            resultDto = ResultDto.success("确认并发送消息成功");

        } catch (Exception ex) {
            LOGGER.error("DTP-MQ 确认并发送消息发生异常", ex);
        }
        return resultDto;
    }


    /**
     * 存储并发送消息
     *
     * @param transactionMessageDto
     * @return 返回 -1 失败，1 成功
     */
    @PostMapping(value = "/saveSendMsg", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    public ResultDto saveAndSendMessage(@RequestBody TransactionMessageDto transactionMessageDto) {
        ResultDto<Object> resultDto = ResultDto.error("-1", "存储并发送消息失败");
        if (null == transactionMessageDto) {
            resultDto = ResultDto.error("-1", "存储并发送消息失败-【消息不能为空】");
            return resultDto;
        }
        try {

            iTransactionMessageService.saveAndSendMessage(transactionMessageDto);
            resultDto = ResultDto.success("存储并发送消息成功");

        } catch (Exception ex) {
            LOGGER.error("DTP-MQ 存储并发送消息发生异常", ex);
        }
        return resultDto;
    }


    /**
     * 直接发送消息
     *
     * @param transactionMessageDto
     * @return 返回 -1 失败，1 成功
     */
    @PostMapping(value = "/dSendMsg", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    public ResultDto directSendMessage(@RequestBody TransactionMessageDto transactionMessageDto) {
        ResultDto<Object> resultDto = ResultDto.error("-1", "直接发送消息失败");
        if (null == transactionMessageDto) {
            resultDto = ResultDto.error("-1", "直接发送消息失败-【消息不能为空】");
            return resultDto;
        }
        try {

            iTransactionMessageService.directSendMessage(transactionMessageDto);
            resultDto = ResultDto.success("直接发送消息成功");

        } catch (Exception ex) {
            LOGGER.error("DTP-MQ 直接发送消息发生异常", ex);
        }
        return resultDto;
    }


    /**
     * 从数据库删除消息
     * @param messageId 消息ID
     * @return 返回 -1 失败，1 成功
     */
    @GetMapping(value = "/deleteMsg", produces = "application/json;charset=UTF-8")
    public ResultDto deleteMessageByMessageId(@RequestParam("messageId") Long messageId) {
        ResultDto<Object> resultDto = ResultDto.error("-1", "从数据库删除消息失败");
        if (messageId <= 0) {
            resultDto = ResultDto.error("-1", "从数据库删除消息失败-【消息Id 不能为空】");
            return resultDto;
        }
        try {

            iTransactionMessageService.deleteMessageByMessageId(messageId);
            resultDto = ResultDto.success("从数据库删除消息成功");

        } catch (Exception ex) {
            LOGGER.error("DTP-MQ 从数据库删除消息发生异常", ex);
        }
        return resultDto;
    }


    //--------
}
