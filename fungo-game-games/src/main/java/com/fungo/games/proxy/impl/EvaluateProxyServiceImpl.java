package com.fungo.games.proxy.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.games.feign.CommunityFeignClient;
import com.fungo.games.feign.SystemFeignClient;
import com.fungo.games.proxy.IEvaluateProxyService;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.community.CmmCommunityDto;
import com.game.common.dto.community.ReplyInputPageDto;
import com.game.common.dto.game.BasTagDto;
import com.game.common.dto.game.ReplyDto;
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
     * @param code
     * @param inectTaskVirtualCoinTaskCodeIdt
     * @param code1
     * @return
     */
    @HystrixCommand(fallbackMethod = "hystrixExTask",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public Map<String, Object> exTask(String memberId, int code, int inectTaskVirtualCoinTaskCodeIdt, int code1) {
        return systemFeignClient.exTask(memberId,code,inectTaskVirtualCoinTaskCodeIdt,code1);
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
        return systemFeignClient.getAuthor(memberId);
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
    public Page<ReplyDto> getReplyDtoBysSelectPageOrderByCreatedAt(ReplyInputPageDto replyInputPageDto) {
        return communityFeignClient.getReplyDtoBysSelectPageOrderByCreatedAt(replyInputPageDto);
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
        return communityFeignClient.getCmmCommunitySelectOneById(ccd);
    }

    /**
     * Mqfeign调用
     * @param inviteMemberId
     * @param i
     * @param appVersion
     */
    @Override
    public void push(String inviteMemberId, int i, String appVersion) {
        systemFeignClient.push(inviteMemberId,i,appVersion);
    }

    /**
     * 根据bastagid获取basTag对象
     * @param basTagDto
     * @return
     */
    @Override
    public BasTagDto getBasTagBySelectById(BasTagDto basTagDto) {
        return systemFeignClient.getBasTagBySelectById(basTagDto);
    }


    /****************************************降级区*************************************************************/



}
