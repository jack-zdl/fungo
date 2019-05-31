package com.fungo.community.facede;

import com.fungo.community.feign.SystemFeignClient;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.system.TaskDto;
import com.game.common.dto.user.IncentRankedDto;
import com.game.common.dto.user.IncentRuleRankDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.dto.user.MemberFollowerDto;
import com.game.common.vo.MemberFollowerVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SystemFacedeService {

    private static final Logger logger = LoggerFactory.getLogger(SystemFacedeService.class);


    @Autowired(required = false)
    private SystemFeignClient systemFeignClient;


    /**
     * 功能描述: 根据用户id查询被关注人的id集合
     * @param:
     * @return: java.util.List<java.lang.String>
     * @auther: mxf
     * @date: 2019/5/10 11:34
     */
    public FungoPageResultDto<String> getFollowerUserId(MemberFollowerVo memberFollowerVo){
        try {
            return systemFeignClient.getFollowerUserId(memberFollowerVo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  new FungoPageResultDto<String>();
    }



    /**
     * 功能描述: 根据用户id集合查询用户详情 state为null就不根据状态查询
     */

    public ResultDto<List<MemberDto>> listMembersByids(List<String> ids, Integer state){
        try {
            return systemFeignClient.listMembersByids(ids,state);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<List<MemberDto>>();
    }



    /**
     * 功能描述: 根据会员关注粉丝表对象来分页查询集合
     * @param: [memberUserPrefile, memberFollowerVo]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.user.MemberFollowerDto>
     * @auther: dl.zhang
     * @date: 2019/5/10 17:15
     */

    public FungoPageResultDto<MemberFollowerDto> getMemberFollowerList(MemberFollowerVo memberFollowerVo){
        try {
            return systemFeignClient.getMemberFollowerList(memberFollowerVo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new FungoPageResultDto<MemberFollowerDto>();
    }


    /**
     * 功能描述: 根据用户会员DTO对象分页查询用户会员
     * @param: [memberUserPrefile, memberDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.user.MemberDto>
     * @auther: dl.zhang
     * @date: 2019/5/10 17:41
     */
    public FungoPageResultDto<MemberDto> getMemberDtoList(MemberDto memberDto){
        try {
            return systemFeignClient.getMemberDtoList(memberDto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new  FungoPageResultDto<MemberDto>();
    }



    /**
     * 根据用户id和用户权益(等级、身份、荣誉)类型，获取用户权益数据
     * @param incentRankedDto
     * @return
     */
    public FungoPageResultDto<IncentRankedDto> getIncentRankedList(IncentRankedDto incentRankedDto){
        try {
            return systemFeignClient.getIncentRankedList(incentRankedDto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  new FungoPageResultDto<IncentRankedDto>();
    }



    /**
     * 获取关注社区id集合
     * @param memberId
     * @return
     */
    public ResultDto<List<String>> listFollowerCommunityId(String memberId){
        try {
            return systemFeignClient.listFollowerCommunityId(memberId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<List<String>>();
    }



    /**
     * 获取动作数量(比如点赞
     */
    public ResultDto<Integer> countActionNum(BasActionDto basActionDto){
        try {
            return systemFeignClient.countActionNum(basActionDto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  new ResultDto<Integer>();
    }



    /**
     * 根据用户id，动作类型，目前类型，状态获取目前id集合
     */
    public ResultDto<List<String>> listtargetId(BasActionDto basActionDto){
        try {
            return systemFeignClient.listtargetId(basActionDto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<List<String>>();
    }



    /**
     * 根据用户id，动作类型，目前类型，状态获取目前id集合
     */
    public ResultDto<AuthorBean> getAuthor(String memberId){
        try {
            return systemFeignClient.getAuthor(memberId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<AuthorBean>();
    }



    /**
     * 执行任务
     */
    public ResultDto<Map<String, Object>> exTask(TaskDto taskDto){
        try {
            return systemFeignClient.exTask(taskDto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  new ResultDto<Map<String, Object>>();
    }



    /**
     * 获取会员信息
     * @param cardId
     * @param memberId
     * @return
     */
    public ResultDto<AuthorBean> getUserCard(String cardId, String memberId){
        try {
            return systemFeignClient.getUserCard(cardId,memberId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<AuthorBean>();
    }



    /**
     * 根据用户id获取用户身份图标
     * @param memberId
     * @return
     */
    public ResultDto<List<HashMap<String, Object>>> getStatusImage(String memberId){
        try {
            return systemFeignClient.getStatusImage(memberId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  new ResultDto<List<HashMap<String, Object>>>();
    }



    /**
     * 查询指定用户所关注的其他用户列表
     */
    public ResultDto<List<MemberDto>> listWatchMebmber(Integer limit, String currentMbId){
        try {
            return systemFeignClient.listWatchMebmber(limit,currentMbId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<List<MemberDto>>();
    }


    /**
     * 功能描述: .找出官方推荐玩家
     */
    public ResultDto<List<MemberDto>> listRecommendedMebmber(Integer limit, String currentMbId, List<String> wathMbsSet){
        try {
            return systemFeignClient.listRecommendedMebmber(limit,currentMbId,wathMbsSet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new  ResultDto<List<MemberDto>>();
    }




    /**
     * 获取用户级别、身份、荣誉规则
     * @param id
     * @return
     */
    public ResultDto<IncentRuleRankDto> getIncentRuleRankById(String id){
        try {
            return systemFeignClient.getIncentRuleRankById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<IncentRuleRankDto>();
    }



    /**
     * 根据条件获取动作
     * @param basActionDto
     * @return
     */
    public ResultDto<List<BasActionDto>> listActionByCondition(BasActionDto basActionDto){
        try {
            return systemFeignClient.listActionByCondition(basActionDto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<List<BasActionDto>>();
    }




    /**
     * 社区使用
     */
    public ResultDto<MemberFollowerDto> getMemberFollower1(MemberFollowerDto memberFollowerDto){
        try {
            return systemFeignClient.getMemberFollower1(memberFollowerDto);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResultDto<MemberFollowerDto>();
    }


    //-------
}
