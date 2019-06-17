package systemmq;

import com.alibaba.fastjson.JSON;
import com.fungo.games.FungoGamesApplication;
import com.fungo.games.entity.BasTag;
import com.fungo.games.feign.MQFeignClient;
import com.fungo.games.service.BasTagService;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *  ysx
 *  用户mq相关测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FungoGamesApplication.class)
public class SystemMqTest {
    @Autowired
    private MQFeignClient mqFeignClient;

    @Autowired
    private BasTagService basTagService;

    @Test
    public void addTag(){
        List<BasTag> tags = getTags();
        basTagService.insertBatch(tags);
    }

    private List<BasTag> getTags(){
        List<BasTag> list = new ArrayList<>();
        BasTag tag = new BasTag();
        initTag(tag);
        tag.setSort(0);
        tag.setName("评测试玩");
        list.add(tag);

        BasTag tag1 = new BasTag();
        initTag(tag1);
        tag1.setSort(1);
        tag1.setName("攻略心得");
        list.add(tag1);

        BasTag tag2 = new BasTag();
        initTag(tag2);
        tag2.setSort(2);
        tag2.setName("同人/杂谈");
        list.add(tag2);

        BasTag tag3 = new BasTag();
        initTag(tag3);
        tag3.setSort(3);
        tag3.setName("资讯/八卦");
        list.add(tag3);

        BasTag tag4 = new BasTag();
        initTag(tag4);
        tag4.setSort(4);
        tag4.setName("其他");
        list.add(tag4);

        return list;
    }

    private void initTag(BasTag tag) {
        tag.setGroupId("5b04e545a22b9d003239d4sa");
        tag.setCreatedAt(new Date());
        tag.setGameNum(0);
        tag.setUpdatedAt(new Date());
    }


    /**
     *  添加行为记录
     */
    @Test
    public void processAddAction() {
        //参数
        BasActionDto basActionDto = new BasActionDto();
        basActionDto.setCreatedAt(new Date());
        basActionDto.setTargetId("11111");
        basActionDto.setTargetType(6);
        basActionDto.setType(8);
        basActionDto.setMemberId("xxxxx");
        basActionDto.setState(0);
        basActionDto.setUpdatedAt(new Date());

        //消息发送
        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.GameMQDataType.GAME_DATA_TYPE_BASACTIONINSERT.getCode());
        mqResultDto.setBody(basActionDto);
        transactionMessageDto.setMessageBody(JSON.toJSONString(mqResultDto));
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_SYSTEM_USER.getName());
        transactionMessageDto.setRoutingKey(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_SYSTEM_USER.getName());
        transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_USER);
        ResultDto resultDto = mqFeignClient.saveAndSendMessage(transactionMessageDto);

    }
}
