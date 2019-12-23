package com.fungo.community.aop;


import com.alibaba.fastjson.JSON;
import com.fungo.community.dao.mapper.CmmCircleMapper;
import com.fungo.community.dao.mapper.CmmPostDao;
import com.fungo.community.entity.CmmCircle;
import com.fungo.community.entity.CmmPost;
import com.fungo.community.feign.TSFeignClient;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 浏览圈子的行为切面
 */
@SuppressWarnings("all")
@Aspect
@Component
public class BrowseActionCircleAspect {

    private static Logger logger = LoggerFactory.getLogger(BrowseActionCircleAspect.class);
    @Autowired
    private TSFeignClient tsFeignClient;
    @Autowired
    private CmmPostDao cmmPostDao;
    @Autowired
    private CmmCircleMapper cmmCircleMapper;



    @Pointcut("execution(public * com.fungo.community.controller.CircleController.selectCircle(..)) || execution(public * com.fungo.community.controller.portal.PortalCommunityCircleController.selectCircle(..)) " )
    public void webLog() {
    }

    @Before("webLog()") //之前执行
    public void deBefore() {
        try {
//        获取request上下文
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
//        获取圈子ID
            String[] split = request.getRequestURI().split("/");
            String circleId = split[split.length-1];
            //            post
            if (split[split.length-2].equals("circle")){
                CmmCircle cmmCircle = new CmmCircle();
                cmmCircle.setId(circleId);
                circleId = null;
                CmmCircle cmmCircle1 = cmmCircleMapper.selectOne(cmmCircle);
                if (cmmCircle1 != null){
                    circleId = cmmCircle1.getId();
                }
            }
//        获取用户id
            MemberUserProfile member = (MemberUserProfile)request.getAttribute("member");
            if (member != null && StringUtils.isNotBlank(circleId)){
                //MQ 业务数据发送给系统用户业务处理
                BasActionDto basActionDtoAdd = new BasActionDto();
                basActionDtoAdd.setCreatedAt(new Date());
                basActionDtoAdd.setUpdatedAt(new Date());
                basActionDtoAdd.setMemberId(member.getLoginId());
                basActionDtoAdd.setType(12); // 浏览
                basActionDtoAdd.setTargetType(11);   //圈子
                basActionDtoAdd.setTargetId(circleId);
                basActionDtoAdd.setState(0);  //正常
                TransactionMessageDto transactionMessageDto = new TransactionMessageDto();
                //消息类型
                transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_POST);
                //发送的队列
                transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_SYSTEM_USER.getName());
                //路由key
                StringBuffer routinKey = new StringBuffer(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_SYSTEM_USER.getName());
                routinKey.deleteCharAt(routinKey.length() - 1);
                routinKey.append("ACTION_ADD");
                transactionMessageDto.setRoutingKey(routinKey.toString());
                MQResultDto mqResultDto = new MQResultDto();
                mqResultDto.setType(MQResultDto.CommunityEnum.CMT_ACTION_MQ_TYPE_ACTION_ADD.getCode());
                mqResultDto.setBody(basActionDtoAdd);
                transactionMessageDto.setMessageBody(JSON.toJSONString(mqResultDto));
                //执行MQ发送
                ResultDto<Long> messageResult = tsFeignClient.saveAndSendMessage(transactionMessageDto);
            }
        } catch (Exception e) {
            logger.error("浏览切面error______________________");
            e.printStackTrace();
        }

    }


}
