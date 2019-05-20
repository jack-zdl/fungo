package com.fungo.games.proxy.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.games.feign.CommunityFeignClient;
import com.fungo.games.feign.SystemFeignClient;
import com.fungo.games.proxy.IEvaluateProxyService;
import com.game.common.bean.TagBean;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.community.CmmCmtReplyDto;
import com.game.common.dto.community.CmmCommunityDto;
import com.game.common.dto.community.ReplyInputPageDto;
import com.game.common.dto.game.BasTagDto;
import com.game.common.dto.game.BasTagGroupDto;
import com.game.common.dto.game.ReplyDto;
import com.game.common.dto.system.TaskDto;
import com.game.common.dto.user.MemberDto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏评论业务调用区
 * @Author lyc
 * @create 2019/5/11 10:26
 */
@Service
public class EvaluateProxyServiceImpl implements IEvaluateProxyService {
    @Autowired
    private SystemFeignClient systemFeignClient;
    @Autowired
    private CommunityFeignClient communityFeignClient;

    /**
     * 迁移微服务后 SystemFeignClient调用 用户成长
     * @param memberId
     * @param task_group_flag
     * @param task_type
     * @param type_code_idt
     * @return
     */
    @HystrixCommand(fallbackMethod = "hystrixExTask",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public Map<String, Object> exTask(String memberId, int task_group_flag, int task_type, int type_code_idt) {
        TaskDto taskDto = new TaskDto();
        taskDto.setMbId(memberId);
        taskDto.setTaskGroupFlag(task_group_flag);
        taskDto.setTaskType(task_type);
        taskDto.setTypeCodeIdt(type_code_idt);
        return systemFeignClient.exTask(taskDto).getData();
    }
    /**
     * 根据用户id获取authorBean
     * @param memberId
     * @return
     */
    @HystrixCommand(fallbackMethod = "hystrixGetAuthor",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public AuthorBean getAuthor(String memberId) {
        return systemFeignClient.getAuthor(memberId).getData();
    }
    /**
     * 根据条件判断查询总数
     * @param basActionDto
     * @return
     */
    @HystrixCommand(fallbackMethod = "hystrixGetBasActionSelectCount",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public int getBasActionSelectCount(BasActionDto basActionDto) {
        return systemFeignClient.getBasActionSelectCount(basActionDto);
    }

    /**
     * 根据条件判断获取ReplyDtoList集合
     * @param replyInputPageDto
     * @return
     */
    @HystrixCommand(fallbackMethod = "hystrixGetReplyDtoBysSelectPageOrderByCreatedAt",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public Page<CmmCmtReplyDto> getReplyDtoBysSelectPageOrderByCreatedAt(ReplyInputPageDto replyInputPageDto) {
        CmmCmtReplyDto ssrd = new CmmCmtReplyDto();
        ssrd.setPage(replyInputPageDto.getPage());
        ssrd.setLimit(replyInputPageDto.getLimit());
        ssrd.setTargetId(replyInputPageDto.getTarget_id());
        ssrd.setMemberId(replyInputPageDto.getUser_id());
        ssrd.setState(replyInputPageDto.getState());
        FungoPageResultDto<CmmCmtReplyDto> replyDtoBysSelectPageOrderByCreatedAt = communityFeignClient.getReplyDtoBysSelectPageOrderByCreatedAt(ssrd);
        List<CmmCmtReplyDto> data = replyDtoBysSelectPageOrderByCreatedAt.getData();
        Page<CmmCmtReplyDto> replyDtoPage = new Page<>();
        replyDtoPage.setRecords(data);
        return replyDtoPage;
    }

    /**
     * 根据条件判断获取memberDto对象
     * @param md
     * @return
     */
    @HystrixCommand(fallbackMethod = "hystrixGetMemberDtoBySelectOne",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public MemberDto getMemberDtoBySelectOne(MemberDto md) {
        return systemFeignClient.getMemberDtoBySelectOne(md);
    }

    /**
     * 根据用户id获取用户身份图标
     * @param memberId
     * @return
     */
    @HystrixCommand(fallbackMethod = "hystrixGetStatusImageByMemberId",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public List<HashMap<String, Object>> getStatusImageByMemberId(String memberId) {
        return systemFeignClient.getStatusImageByMemberId(memberId);
    }

    /**
     * 根据判断集合id获取BasTagList集合
     * @param collect
     * @return
     */
    @HystrixCommand(fallbackMethod = "hystrixGetBasTagBySelectListInId",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public List<BasTagDto> getBasTagBySelectListInId(List<String> collect) {
        return systemFeignClient.getBasTagBySelectListInId(collect);
    }

    /**
     * 根据group_id获取BasTag集合
     * @param basTagDto
     * @return
     */
    @HystrixCommand(fallbackMethod = "hystrixGetBasTagBySelectListGroupId",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public List<BasTagDto> getBasTagBySelectListGroupId(BasTagDto basTagDto) {
        return systemFeignClient.getBasTagBySelectListGroupId(basTagDto);
    }

    /**
     * 根据id获取cmmcomunity单个对象
     * @param ccd
     * @return
     */
    @HystrixCommand(fallbackMethod = "hystrixGetCmmCommunitySelectOneById",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public CmmCommunityDto getCmmCommunitySelectOneById(CmmCommunityDto ccd) {
        FungoPageResultDto<CmmCommunityDto> cmmCommunitySelectOneById = communityFeignClient.getCmmCommunitySelectOneById(ccd);
        List<CmmCommunityDto> data = cmmCommunitySelectOneById.getData();
        CmmCommunityDto cmmCommunityDto = new CmmCommunityDto();
        if (data != null && data.size() > 0){
            cmmCommunityDto = data.get(0);
        }
        return cmmCommunityDto;
    }

    /**
     * Mqfeign调用
     * @param inviteMemberId
     * @param i
     * @param appVersion
     */
    @HystrixCommand(fallbackMethod = "hystrixPush",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public void push(String inviteMemberId, int i, String appVersion) {
        systemFeignClient.push(inviteMemberId,i,appVersion);
    }

    /**
     * 根据bastagid获取basTag对象
     * @param basTagDto
     * @return
     */
    @HystrixCommand(fallbackMethod = "hystrixGetBasTagBySelectById",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public BasTagDto getBasTagBySelectById(BasTagDto basTagDto) {
        return systemFeignClient.getBasTagBySelectById(basTagDto);
    }

    /**
     * 判断BasTagGroup属性值获取BasTagGroup集合
     * @param basTagGroupDto
     * @return
     */
    @HystrixCommand(fallbackMethod = "hystrixGetBasTagGroupBySelectList",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public List<BasTagGroupDto> getBasTagGroupBySelectList(BasTagGroupDto basTagGroupDto) {
        return systemFeignClient.getBasTagGroupBySelectList(basTagGroupDto);
    }

    /**
     * 特殊 根据gameId获取TagBean集合
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "hystrixGetSortTags",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public List<TagBean> getSortTags(String id) {
        return systemFeignClient.getSortTags(id);
    }


    /****************************************降级区*************************************************************/



}
