package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.controller.MemberIncentHonorController;
import com.fungo.system.dao.BasActionDao;
import com.fungo.system.dao.MemberFollowerDao;
import com.fungo.system.entity.*;
import com.fungo.system.service.*;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.user.IncentRankedDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.dto.user.MemberFollowerDto;
import com.game.common.util.CommonUtils;
import com.game.common.util.PageTools;
import com.game.common.vo.MemberFollowerVo;
import com.sun.istack.NotNull;
import org.apache.commons.beanutils.BeanUtils;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Size;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>系统微服务对外服务实现类</p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/10
 */
@Service
public class SystemServiceImpl implements SystemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemServiceImpl.class);

    @Autowired
    private BasActionDao basActionDao;


    @Autowired
    private MemberFollowerService memberFollowerServiceImap;

    @Autowired
    private MemberService memberServiceImap;

    @Autowired
    private IncentRankedService incentRankedServiceImap;

    @Autowired
    private IncentRuleRankService ruleRankServiceImap;

    @Autowired
    private ScoreLogService scoreLogServiceImap;

    @Autowired
    private IncentAccountScoreService accountScoreServiceImap;

    @Autowired
    private IMemberAccountScoreDaoService accountScoreDaoServiceImap;

    @Autowired
    private BasActionService actionServiceImap;
    /**
     * 功能描述: 根据用户id查询被关注人的id集合
     *
     * @param: [memberId]
     * @return: com.game.common.dto.FungoPageResultDto<java.lang.String>
     * @auther: dl.zhang
     * @date: 2019/5/10 14:22
     */
    @Override
    public FungoPageResultDto<String> getFollowerUserId(@NotNull String memberId) {
        FungoPageResultDto<String> re = null;
        List<String> followerIdList = new ArrayList<>();
        try {
            followerIdList = basActionDao.getFollowerUserId(memberId);
            re = new FungoPageResultDto<String>();
            re.setData(followerIdList);
//            PageTools.pageToResultDto(re, plist);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("SystemServiceImpl.getFollowerUserId", e);
            re = FungoPageResultDto.error("-1", "找不到目标");
        } finally {
            return re;
        }
    }

    /**
     * 功能描述:
     *
     * @param: [vo] 根据对象获取对象集合
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.user.MemberFollowerDto>
     * @auther: dl.zhang
     * @date: 2019/5/10 16:48
     */
    @Override
    public FungoPageResultDto<MemberFollowerDto> getMemberFollowerList(MemberFollowerVo vo) {
        FungoPageResultDto<MemberFollowerDto> re = null;
        try {
            Page<MemberFollower> basNoticePage = new Page<>(vo.getPage(), vo.getLimit());
            re = new FungoPageResultDto<MemberFollowerDto>();
            MemberFollower memberFollower = new MemberFollower();
            BeanUtils.copyProperties(memberFollower, vo);
            EntityWrapper<MemberFollower> memberFollowerWrapper = new EntityWrapper<>(memberFollower);
            Page<MemberFollower> page = memberFollowerServiceImap.selectPage(basNoticePage, memberFollowerWrapper);
            PageTools.pageToResultDto(re, page);
            List<MemberFollower> memberFollowers = page.getRecords();
            List<MemberFollowerDto> memberFollowerDtos = CommonUtils.deepCopy(memberFollowers, MemberFollowerDto.class);
            re.setData(memberFollowerDtos);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("SystemServiceImpl.getMemberFollowerList", e);
            re = FungoPageResultDto.error("-1", "找不到目标");
        }
        return re;
    }

    /**
     * 功能描述: 根据用户会员DTO对象分页查询用户会员
     *
     * @param: [memberDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.user.MemberDto>
     * @auther: dl.zhang
     * @date: 2019/5/10 17:40
     */
    @Override
    public FungoPageResultDto<MemberDto> getMemberDtoList(MemberDto memberDto) {
        FungoPageResultDto<MemberDto> re = null;
        try {
            Page<Member> basNoticePage = new Page<>(memberDto.getPage(), memberDto.getLimit());
            re = new FungoPageResultDto<MemberDto>();
            Member memberFollower = new Member();
            BeanUtils.copyProperties(memberFollower, memberDto);
            EntityWrapper<Member> memberFollowerWrapper = new EntityWrapper<>(memberFollower);
            Page<Member> page = memberServiceImap.selectPage(basNoticePage, memberFollowerWrapper);
            PageTools.pageToResultDto(re, page);
            List<Member> memberFollowers = page.getRecords();
            List<MemberDto> memberFollowerDtos = CommonUtils.deepCopy(memberFollowers, MemberDto.class);
            re.setData(memberFollowerDtos);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("SystemServiceImpl.getMemberFollowerList", e);
            re = FungoPageResultDto.error("-1", "找不到目标");
        }
        return re;
    }

    @Override
    public FungoPageResultDto<IncentRankedDto> getIncentRankedList(IncentRankedDto incentRankedDto) {
        FungoPageResultDto<IncentRankedDto> re = null;
        try {
            Page<IncentRanked> basNoticePage = new Page<>(incentRankedDto.getPage(), incentRankedDto.getLimit());
            re = new FungoPageResultDto<>();
            IncentRanked incentRanked = new IncentRanked();
            BeanUtils.copyProperties(incentRanked, incentRankedDto);
            EntityWrapper<IncentRanked> wrapper = new EntityWrapper<>(incentRanked);
            Page<IncentRanked> page = incentRankedServiceImap.selectPage(basNoticePage, wrapper);
            PageTools.pageToResultDto(re, page);
            List<IncentRanked> memberFollowers = page.getRecords();
            List<IncentRankedDto> memberFollowerDtos = CommonUtils.deepCopy(memberFollowers, IncentRankedDto.class);
            re.setData(memberFollowerDtos);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("SystemServiceImpl.getMemberFollowerList", e);
            re = FungoPageResultDto.error("-1", "找不到目标");
        }
        return re;
    }

    /**
     * 功能描述: 根据指定用户id变更用户到指定等级
     *
     * @param memberDto
     * @return
     */
    @Override
    public ResultDto<String> changeMemberLevel(MemberDto memberDto) {
        Member user = memberServiceImap.selectById(memberDto.getId());
        if (user == null) {
            return ResultDto.error("126", "用户不存在");
        }
        // TODO 向调用方确认是否需要做变更校验避免可能的校验不通过问题 ，确认t_incent_ranked是否变更
        //当前外部调用期望变更的等级
        int expectLevel = memberDto.getLevel();
        if (user.getLevel() != expectLevel) {
            user.setLevel(memberDto.getLevel());
            memberServiceImap.updateById(user);
        }

        IncentRanked ranked = incentRankedServiceImap.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", memberDto.getId()).eq("rank_type", 1));
        Long prelevel = ranked.getCurrentRankId();//记录中的等级 可能需要修改

        //用户需不需要变更等级
        ObjectMapper mapper = new ObjectMapper();
        if (expectLevel != prelevel) {
            ranked.setCurrentRankId((long) expectLevel);
            ranked.setCurrentRankName("Lv" + expectLevel);
            String rankIdtIds = ranked.getRankIdtIds();
            List<HashMap<String, String>> rankList = new ArrayList<>();
            try {
                rankList = mapper.readValue(rankIdtIds, ArrayList.class);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            boolean add = true;
            for (HashMap m : rankList) {
                if (m.get("rankId").equals(expectLevel + "")) {
                    add = false;
                }
            }
            if (add) {
                HashMap<String, String> newMap = new HashMap<String, String>();
                newMap.put("rankId", expectLevel + "");
                newMap.put("rankName", "Lv" + expectLevel);
                rankList.add(newMap);
                try {
                    ranked.setRankIdtIds(mapper.writeValueAsString(rankList));
                } catch (JsonProcessingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            //等级变更记录 t_incent_ranked_log 更新
            ranked.setUpdatedAt(new Date());
            ranked.updateById();
            IncentRuleRank rankRule = ruleRankServiceImap.selectOne(new EntityWrapper<IncentRuleRank>().eq("id", expectLevel));
            scoreLogServiceImap.addRankLog(memberDto.getId(), rankRule);
        }
        return ResultDto.success();
    }

    @Override
    public ResultDto<String> decMemberExp(MemberDto memberDto) {
        Member user = memberServiceImap.selectById(memberDto.getId());
        if (user == null) {
            return ResultDto.error("126", "用户不存在");
        }

        IncentAccountScore scoreCount = accountScoreServiceImap.selectOne(new EntityWrapper<IncentAccountScore>().eq("mb_id", memberDto.getId()).eq("account_group_id", 1));
        if (scoreCount == null) {
            scoreCount = accountScoreDaoServiceImap.createAccountScore(user, 1);
        }
        scoreCount.setScoreUsable(scoreCount.getScoreUsable().subtract((new BigDecimal(memberDto.getExp()))));
        scoreCount.setUpdatedAt(new Date());
        scoreCount.updateById();
        user.setExp(scoreCount.getScoreUsable().intValue());
        memberServiceImap.updateById(user);
        return ResultDto.success();
    }

    @Override
    public ResultDto<List<String>> listFollowerCommunityId(String memberId) {
        List<String>  ids = basActionDao.getFollowerCommunityId(memberId);
        return ResultDto.success(ids);

    }

    @Override
    public ResultDto<Integer> countActionNum(BasActionDto basActionDto) {
        //条件拼接
        EntityWrapper<BasAction> actionEntityWrapper = new EntityWrapper<>();
        if(basActionDto.getMemberId()!=null){
            actionEntityWrapper.eq("member_id", basActionDto.getMemberId());
        }
        if(basActionDto.getType()!=null){
            actionEntityWrapper.eq("type", basActionDto.getType());
        }
        if(basActionDto.getTargetType()!=null){
            actionEntityWrapper.eq("target_type", basActionDto.getTargetType());
        }
        if(basActionDto.getTargetId()!=null){
            actionEntityWrapper.eq("target_id", basActionDto.getTargetId());
        }
        if(basActionDto.getState()!=null){
            actionEntityWrapper.eq("state", basActionDto.getState());
        }
        //根据条件查询
        int count = actionServiceImap.selectCount(actionEntityWrapper);
        return ResultDto.success(count);
    }

    @Override
    public ResultDto<List<String>> listtargetId(BasActionDto basActionDto) {
        Wrapper<BasAction> wrapper = Condition.create().setSqlSelect(" target_id as targetId");
        if(basActionDto.getMemberId()!=null){
            wrapper.eq("member_id", basActionDto.getMemberId());
        }
        if(basActionDto.getType()!=null){
            wrapper.eq("type", basActionDto.getType());
        }
        if(basActionDto.getTargetType()!=null){
            wrapper.eq("target_type", basActionDto.getTargetType());
        }
        if(basActionDto.getState()!=null){
            wrapper.eq("state", basActionDto.getState());
        }
        List<BasAction> actionList = actionServiceImap.selectList(wrapper);
        LOGGER.info(actionList.size()+"");
        List<String> ids = actionList.stream().filter(li -> {return li != null && li.getTargetId() != null && li.getTargetId() != "";}).map(BasAction::getTargetId).collect(Collectors.toList());
      /*  ArrayList<String> list = new ArrayList<>();
        for (BasAction basAction : actionList) {
            list.add(basAction.getTargetId());
        }*/
        return ResultDto.success(ids);
    }


    @Override
    public ResultDto<String> addAction(BasActionDto basActionDto) {
        BasAction action = new BasAction();
        action.setCreatedAt(new Date());
        action.setInformation(basActionDto.getInformation());
        action.setMemberId(basActionDto.getMemberId());
        action.setState(0);
        action.setTargetId(basActionDto.getTargetId());
        action.setTargetType(basActionDto.getTargetType());
        action.setType(basActionDto.getType());
        action.setUpdatedAt(new Date());
        action.insert();
        return ResultDto.success("");
    }

}
