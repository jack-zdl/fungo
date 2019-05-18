package com.fungo.community.feign;


import com.game.common.dto.ResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *      系统微服务接口
 * </p>
 *
 * @author mxf
 * @since 2018-11-08
 */
@FeignClient(name = "FUNGO-GAME-SYSTEM")
@RestController("/ms/service/dtp/mq")
public interface TSFeignClient {


    /**
     * 预存储消息
     *
     * @param transactionMessageDto
     * @return 返回 -1 失败，1 成功
     */
    @PostMapping(value = "/saveMsgWC", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    public ResultDto<Long> saveMessageWaitingConfirm(@RequestBody TransactionMessageDto transactionMessageDto);


    /**
     * 确认并发送消息
     *
     * @param transactionMessageDto
     * @return 返回 -1 失败，1 成功
     */
    @PostMapping(value = "/cSendMsg", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    public ResultDto confirmAndSendMessage(@RequestBody TransactionMessageDto transactionMessageDto);


    /**
     * 存储并发送消息
     *
     * @param transactionMessageDto
     * @return 返回 -1 失败，1 成功
     */
    @PostMapping(value = "/saveSendMsg", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    public ResultDto<Long> saveAndSendMessage(@RequestBody TransactionMessageDto transactionMessageDto);


    /**
     * 直接发送消息
     *
     * @param transactionMessageDto
     * @return 返回 -1 失败，1 成功
     */
    @PostMapping(value = "/dSendMsg", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    public ResultDto directSendMessage(@RequestBody TransactionMessageDto transactionMessageDto);


    /**
     * 从数据库删除消息
     * @param messageId 消息ID
     * @return 返回 -1 失败，1 成功
     */
    @GetMapping(value = "/deleteMsg", produces = "application/json;charset=UTF-8")
    public ResultDto deleteMessageByMessageId(@RequestParam("messageId") Long messageId);
    //----------
}
