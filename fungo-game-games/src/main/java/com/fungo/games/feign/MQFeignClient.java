package com.fungo.games.feign;


import com.game.common.dto.AuthorBean;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.game.BasTagDto;
import com.game.common.dto.game.BasTagGroupDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.dto.user.MemberOutBean;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@FeignClient(name = "fungo-game-system")
@RestController("/ms/service/dtp/mq")
public interface MQFeignClient {

    /**
     * 存储并发送消息
     *
     * @param transactionMessageDto
     * @return 返回 -1 失败，1 成功
     */
    @PostMapping(value = "/saveSendMsg", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    ResultDto saveAndSendMessage(@RequestBody TransactionMessageDto transactionMessageDto);

    /**
     * 从数据库删除消息
     * @param messageId 消息ID
     * @return 返回 -1 失败，1 成功
     */
    @GetMapping(value = "/deleteMsg", produces = "application/json;charset=UTF-8")
    ResultDto deleteMessageByMessageId(@RequestParam("messageId") Long messageId);
}
