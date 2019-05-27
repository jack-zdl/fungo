package com.fungo.games.proxy.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.games.feign.CommunityFeignClient;
import com.fungo.games.feign.MQFeignClient;
import com.fungo.games.feign.SystemFeignClient;
import com.fungo.games.proxy.IEvaluateProxyService;
import com.game.common.bean.TagBean;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.community.CmmCmtReplyDto;
import com.game.common.dto.community.CmmCommunityDto;
import com.game.common.dto.community.MooMessageDto;
import com.game.common.dto.community.ReplyInputPageDto;
import com.game.common.dto.game.BasTagDto;
import com.game.common.dto.game.BasTagGroupDto;
import com.game.common.dto.game.ReplyDto;
import com.game.common.dto.system.TaskDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏评论业务调用区
 *
 * @Author lyc
 * @create 2019/5/11 10:26
 */
@Service
public class EvaluateProxyServiceImpl implements IEvaluateProxyService {

    private static final Logger logger = LoggerFactory.getLogger(EvaluateProxyServiceImpl.class);
    @Autowired
    private SystemFeignClient systemFeignClient;
    @Autowired
    private CommunityFeignClient communityFeignClient;
    @Autowired
    private MQFeignClient mqFeignClient;

    /**
     * 迁移微服务后 SystemFeignClient调用 用户成长
     *
     * @param memberId
     * @param task_group_flag
     * @param task_type
     * @param type_code_idt
     * @return
     */
    @HystrixCommand(fallbackMethod = "hystrixExTask", ignoreExceptions = {Exception.class},
            commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"))
    @Override
    public Map<String, Object> exTask(String memberId, int task_group_flag, int task_type, int type_code_idt, Long requestId) {
        TaskDto taskDto = new TaskDto();
        taskDto.setMbId(memberId);
        taskDto.setTaskGroupFlag(task_group_flag);
        taskDto.setTaskType(task_type);
        taskDto.setTypeCodeIdt(type_code_idt);
        taskDto.setRequestId(requestId + "");
        Map<String, Object> data = new HashMap<>();
        try {
            data = systemFeignClient.exTask(taskDto).getData();
        } catch (Exception e) {
            e.printStackTrace();
            TransactionMessageDto transactionMessageDto = new TransactionMessageDto();
            MQResultDto mqResultDto = new MQResultDto();
            mqResultDto.setType(MQResultDto.GameMQDataType.GAME_DATA_TYPE_EXTASK.getCode());
            mqResultDto.setBody(taskDto);
            transactionMessageDto.setMessageBody(JSON.toJSONString(mqResultDto));
            transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_SYSTEM_USER.getName());
            transactionMessageDto.setRoutingKey(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_SYSTEM_USER.getName());
            transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_GAME);
            ResultDto resultDto = mqFeignClient.saveAndSendMessage(transactionMessageDto);
        } finally {
            return data;
        }
    }

    public Map<String, Object> hystrixExTask(String memberId, int task_group_flag, int task_type, int type_code_idt, Long requestId) {
        return new HashMap<String, Object>();
    }

    /**
     * 根据用户id获取authorBean
     *
     * @param memberId
     * @return
     */
    /*@HystrixCommand(fallbackMethod = "hystrixGetAuthor",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )*/
    @Override
    public AuthorBean getAuthor(String memberId) {
        try{
            AuthorBean data = systemFeignClient.getAuthor(memberId).getData();
            return data;
        }catch (Exception e){
            logger.error("远程调用异常:"+e);
        }
        return new AuthorBean();
    }

    public AuthorBean hystrixGetAuthor(String memberId) {
        return new AuthorBean();
    }


    /**
     * 根据条件判断查询总数
     *
     * @param basActionDto
     * @return
     */
    /*@HystrixCommand(fallbackMethod = "hystrixGetBasActionSelectCount",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )*/
    @Override
    public int getBasActionSelectCount(BasActionDto basActionDto) {
        try{
            return systemFeignClient.getBasActionSelectCount(basActionDto).getData();
        }catch (Exception e){
            logger.error("远程调用异常:"+e);
        }
        return 0;
    }

    public int hystrixGetBasActionSelectCount(BasActionDto basActionDto) {
        return 0;
    }

    /**
     * 根据条件判断获取ReplyDtoList集合
     *
     * @param replyInputPageDto
     * @return
     */
    /*@HystrixCommand(fallbackMethod = "hystrixGetReplyDtoBysSelectPageOrderByCreatedAt",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )*/
    @Override
    public Page<CmmCmtReplyDto> getReplyDtoBysSelectPageOrderByCreatedAt(ReplyInputPageDto replyInputPageDto) {
        CmmCmtReplyDto ssrd = new CmmCmtReplyDto();
        ssrd.setPage(replyInputPageDto.getPage());
        ssrd.setLimit(replyInputPageDto.getLimit());
        ssrd.setTargetId(replyInputPageDto.getTarget_id());
        ssrd.setMemberId(replyInputPageDto.getUser_id());
        ssrd.setState(replyInputPageDto.getState());

        try{
            FungoPageResultDto<CmmCmtReplyDto> replyDtoBysSelectPageOrderByCreatedAt = communityFeignClient.getReplyDtoBysSelectPageOrderByCreatedAt(ssrd);
            List<CmmCmtReplyDto> data = replyDtoBysSelectPageOrderByCreatedAt.getData();
            Page<CmmCmtReplyDto> replyDtoPage = new Page<>();
            replyDtoPage.setRecords(data);
            return replyDtoPage;
        }catch (Exception e){
            logger.error("远程调用异常:"+e);
        }
       return new Page<CmmCmtReplyDto>();
    }

    public Page<CmmCmtReplyDto> hystrixGetReplyDtoBysSelectPageOrderByCreatedAt(ReplyInputPageDto replyInputPageDto) {
        return null;
    }

    /**
     * 根据条件判断获取memberDto对象
     *
     * @param md
     * @return
     */
    /*@HystrixCommand(fallbackMethod = "hystrixGetMemberDtoBySelectOne",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )*/
    @Override
    public MemberDto getMemberDtoBySelectOne(MemberDto md) {
        try{
            return systemFeignClient.getMembersByid(md.getId()).getData();
        }catch (Exception e){
            logger.error("远程调用异常:"+e);
        }
       return new MemberDto();
    }

    public MemberDto hystrixGetMemberDtoBySelectOne(MemberDto md) {
        return null;
    }

    /**
     * 根据用户id获取用户身份图标
     *
     * @param memberId
     * @return
     */
    /*@HystrixCommand(fallbackMethod = "hystrixGetStatusImageByMemberId",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )*/
    @Override
    public List<HashMap<String, Object>> getStatusImageByMemberId(String memberId) {
        try{
            return systemFeignClient.getStatusImageByMemberId(memberId).getData();
        }catch (Exception e){
            logger.error("远程调用异常:"+e);
        }
        return new ArrayList<>();
    }

    public List<HashMap<String, Object>> hystrixGetStatusImageByMemberId(String memberId) {
        return null;
    }

    /**
     * 根据判断集合id获取BasTagList集合
     *
     * @param collect
     * @return
     */
    /*@HystrixCommand(fallbackMethod = "hystrixGetBasTagBySelectListInId",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )*/
  /*  @Override
    public List<BasTagDto> getBasTagBySelectListInId(List<String> collect) {
        return systemFeignClient.getBasTagBySelectListInId(collect).getData();
    }*/

    public List<BasTagDto> hystrixGetBasTagBySelectListInId(List<String> collect) {
        return null;
    }

    /**
     * 根据group_id获取BasTag集合
     *
     * @param basTagDto
     * @return
     */
    /*@HystrixCommand(fallbackMethod = "hystrixGetBasTagBySelectListGroupId",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )*/
    @Override
    public List<BasTagDto> getBasTagBySelectListGroupId(BasTagDto basTagDto) {
        try{
            return systemFeignClient.getBasTagBySelectListGroupId(basTagDto.getGroupId()).getData();
        }catch (Exception e){
            logger.error("远程调用异常:"+e);
        }
        return new ArrayList<>();
    }

    public List<BasTagDto> hystrixGetBasTagBySelectListGroupId(BasTagDto basTagDto) {
        return null;
    }

    /**
     * 根据id获取cmmcomunity单个对象
     *
     * @param ccd
     * @return
     */
    /*@HystrixCommand(fallbackMethod = "hystrixGetCmmCommunitySelectOneById",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )*/
    @Override
    public CmmCommunityDto getCmmCommunitySelectOneById(CmmCommunityDto ccd) {
        CmmCommunityDto cmmCommunityDto = new CmmCommunityDto();
        try{
            FungoPageResultDto<CmmCommunityDto> cmmCommunitySelectOneById = communityFeignClient.getCmmCommunitySelectOneById(ccd);
            List<CmmCommunityDto> data = cmmCommunitySelectOneById.getData();
            if (data != null && data.size() > 0) {
                cmmCommunityDto = data.get(0);
            }
        }catch (Exception e){
            logger.error("远程调用出错:",e);
        }

        return cmmCommunityDto;
    }

    public Map<String, Integer> listCommunityFolloweeNum(List<String> ids) {
        try{
            ResultDto<Map<String, Integer>> resultDto = communityFeignClient.listCommunityFolloweeNum(ids);
            if(resultDto!=null&&resultDto.isSuccess()){
                return  resultDto.getData();
            }
        }catch (Exception e){
            logger.error("远程调用出错:",e);
        }

        return new HashMap<String, Integer>();
    }

    public CmmCommunityDto hystrixGetCmmCommunitySelectOneById(CmmCommunityDto ccd) {
        return new CmmCommunityDto();
    }

    /**
     * Mqfeign调用
     *
     * @param inviteMemberId
     * @param i
     * @param appVersion
     */
    /*@HystrixCommand(fallbackMethod = "hystrixPush",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )*/
    @Override
    public void push(String inviteMemberId, int i, String appVersion) {
        try{
            systemFeignClient.push(inviteMemberId, i, appVersion);
        }catch (Exception e){
            logger.error("远程调用出错:",e);
        }

    }

    public void hystrixPush(String inviteMemberId, int i, String appVersion) {
//        systemFeignClient.push(inviteMemberId,i,appVersion);
    }


    /**
     * 根据bastagid获取basTag对象
     *
     * @param basTagDto
     * @return
     */
    /*@HystrixCommand(fallbackMethod = "hystrixGetBasTagBySelectById",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )*/
   /* @Override
    public BasTagDto getBasTagBySelectById(BasTagDto basTagDto) {
        return systemFeignClient.getBasTagBySelectById(basTagDto.getId()).getData();
    }*/

    public BasTagDto hystrixGetBasTagBySelectById(BasTagDto basTagDto) {
        return new BasTagDto();
    }

    /**
     * 判断BasTagGroup属性值获取BasTagGroup集合
     *
     * @param basTagGroupDto
     * @return
     */
   /* @HystrixCommand(fallbackMethod = "hystrixGetBasTagGroupBySelectList",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )*/
   /* @Override
    public List<BasTagGroupDto> getBasTagGroupBySelectList(BasTagGroupDto basTagGroupDto) {
        return systemFeignClient.getBasTagGroupBySelectList(basTagGroupDto).getData();
    }*/

    public List<BasTagGroupDto> hystrixGetBasTagGroupBySelectList(BasTagGroupDto basTagGroupDto) {
        return new ArrayList<BasTagGroupDto>();
    }

    /**
     * 特殊 根据gameId获取TagBean集合
     *
     * @param tags
     * @return
     */
    /*@HystrixCommand(fallbackMethod = "hystrixGetSortTags",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )*/
 /*   @Override
    public List<TagBean> getSortTags(List<String> tags) {
        return systemFeignClient.listSortTags(tags).getData();
    }*/

    /**
     * 根据用户会员DTO对象分页查询用户会员
     * @param memberDto
     */
    @HystrixCommand(fallbackMethod = "hystrixGetSortTags",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public FungoPageResultDto<MemberDto> listMemberDtoPag(MemberDto memberDto) {

        return systemFeignClient.listMemberDtoPag(memberDto);
    }


    /****************************************降级区*************************************************************/


    public List<TagBean> hystrixGetSortTags(List<String> tags) {
        logger.warn("getSortTags 特殊 根据gameId获取TagBean集合");
        return new ArrayList<TagBean>();
    }

}
