package com.fungo.community.facede;


import com.fungo.community.feign.TSFeignClient;
import com.game.common.dto.ResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TSMQFacedeHystrixService  implements FallbackFactory<TSFeignClient> {

    private static final Logger logger = LoggerFactory.getLogger(TSMQFacedeHystrixService.class);

    @Override
    public TSFeignClient create(Throwable throwable) {
        return new TSFeignClient(){

            @Override
            public ResultDto<Long> saveMessageWaitingConfirm(TransactionMessageDto transactionMessageDto) {

                logger.error("--------------------TSFeignClient--启动熔断:{}" , "saveMessageWaitingConfirm");
                return null;
            }

            @Override
            public ResultDto confirmAndSendMessage(TransactionMessageDto transactionMessageDto) {

                logger.error("--------------------TSFeignClient--启动熔断:{}" , "confirmAndSendMessage");
                return null;
            }

            @Override
            public ResultDto<Long> saveAndSendMessage(TransactionMessageDto transactionMessageDto) {
                logger.error("--------------------TSFeignClient--启动熔断:{}" , "saveAndSendMessage");
                return null;
            }

            @Override
            public ResultDto directSendMessage(TransactionMessageDto transactionMessageDto) {
                logger.error("--------------------TSFeignClient--启动熔断:{}" , "directSendMessage");
                return null;
            }

            @Override
            public ResultDto deleteMessageByMessageId(Long messageId) {
                logger.error("--------------------TSFeignClient--启动熔断:{}" , "deleteMessageByMessageId");
                return null;
            }
        };
    }
}
