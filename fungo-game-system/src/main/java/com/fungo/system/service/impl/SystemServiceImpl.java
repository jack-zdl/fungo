package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fungo.system.dao.BasActionDao;
import com.fungo.system.entity.*;
import com.fungo.system.service.*;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.system.TaskDto;
import com.game.common.dto.user.IncentRankedDto;
import com.game.common.dto.user.IncentRuleRankDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.dto.user.MemberFollowerDto;
import com.game.common.util.CommonUtils;
import com.game.common.util.PageTools;
import com.game.common.util.StringUtil;
import com.game.common.util.UniqueIdCkeckUtil;
import com.game.common.vo.MemberFollowerVo;
import com.sun.istack.NotNull;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
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
@Transactional
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

    @Resource(name = "memberIncentDoTaskFacadeServiceImpl")
    private IMemberIncentDoTaskFacadeService iMemberIncentDoTaskFacadeService;

    @Autowired
    private IUserService userService;


    @Autowired
    private ScoreLogService scoreLogService;

    @Autowired
    private IncentRuleRankService rankRuleService;


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
        if(vo == null){
            return FungoPageResultDto.error("-1","请求参数为空");
        }
        FungoPageResultDto<MemberFollowerDto> re = null;
        try {
            Page<MemberFollower> basNoticePage = new Page<>(vo.getPage(), vo.getLimit());
            re = new FungoPageResultDto<MemberFollowerDto>();
            MemberFollower memberFollower = new MemberFollower();
            BeanUtils.copyProperties(memberFollower, vo);
            EntityWrapper<MemberFollower> memberFollowerWrapper = new EntityWrapper<>(memberFollower);
            Page<MemberFollower> page = memberFollowerServiceImap.selectPage(basNoticePage, memberFollowerWrapper);
            List<MemberFollower> memberFollowers = page.getRecords();
            List<MemberFollowerDto> memberFollowerDtos = CommonUtils.deepCopy(memberFollowers, MemberFollowerDto.class);
            re.setData(memberFollowerDtos);
            PageTools.pageToResultDto(re, page);
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
        if(memberDto == null){
            return FungoPageResultDto.error("-1","请求参数为空");
        }
        FungoPageResultDto<MemberDto> re = null;
        try {
            Page<Member> basNoticePage = new Page<>(memberDto.getPage(), memberDto.getLimit());
            re = new FungoPageResultDto<MemberDto>();
            Member memberFollower = new Member();
            BeanUtils.copyProperties(memberFollower, memberDto);
            EntityWrapper<Member> memberFollowerWrapper = new EntityWrapper<>(memberFollower);
            Page<Member> page = memberServiceImap.selectPage(basNoticePage, memberFollowerWrapper);
            List<Member> memberFollowers = page.getRecords();
            List<MemberDto> memberFollowerDtos = CommonUtils.deepCopy(memberFollowers, MemberDto.class);
            re.setData(memberFollowerDtos);
            PageTools.pageToResultDto(re, page);

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
    public FungoPageResultDto<MemberDto> listMemberDtoPag(MemberDto memberDto) {
        if(memberDto == null){
            return FungoPageResultDto.error("-1","请求参数为空");
        }
        FungoPageResultDto<MemberDto> re = null;
        try {
            Page<Member> page = memberServiceImap.selectPage(new Page<Member>(memberDto.getPage(), memberDto.getLimit()), new EntityWrapper<Member>().ne("id", memberDto.getId()).orderBy("sort", false));
            re = new FungoPageResultDto<MemberDto>();
            List<Member> memberFollowers = page.getRecords();
            List<MemberDto> memberFollowerDtos = CommonUtils.deepCopy(memberFollowers, MemberDto.class);
            re.setData(memberFollowerDtos);
            PageTools.pageToResultDto(re, page);

        } catch (Exception e) {
            LOGGER.error("SystemServiceImpl.listMemberDtoPag", e);
            re = FungoPageResultDto.error("-1", "找不到目标");
        }
        return re;
    }

    @Override
    public FungoPageResultDto<IncentRankedDto> getIncentRankedList(IncentRankedDto incentRankedDto) {
        if(incentRankedDto == null){
            return FungoPageResultDto.error("-1","请求参数为空");
        }
        FungoPageResultDto<IncentRankedDto> re = null;
        try {
            Page<IncentRanked> basNoticePage = new Page<>(incentRankedDto.getPage(), incentRankedDto.getLimit());
            re = new FungoPageResultDto<>();
            IncentRanked incentRanked = new IncentRanked();
            BeanUtils.copyProperties(incentRanked, incentRankedDto);
            EntityWrapper<IncentRanked> wrapper = new EntityWrapper<>();
            if(incentRankedDto.getId() != null)
                wrapper.eq("id", incentRankedDto.getId());
            if(incentRankedDto.getRankType() != null)
                wrapper.eq("rank_type", incentRankedDto.getRankType());
            if(StringUtils.isNoneBlank(incentRankedDto.getMbId()))
                wrapper.eq("mb_id", incentRankedDto.getMbId());
//            EntityWrapper<IncentRanked> wrapper = new EntityWrapper<>(incentRanked);
            Page<IncentRanked> page = incentRankedServiceImap.selectPage(basNoticePage, wrapper);
            List<IncentRanked> memberFollowers = page.getRecords();
            List<IncentRankedDto> memberFollowerDtos = CommonUtils.deepCopy(memberFollowers, IncentRankedDto.class);
            re.setData(memberFollowerDtos);
            PageTools.pageToResultDto(re, page);

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("SystemServiceImpl.getMemberFollowerList", e);
            re = FungoPageResultDto.error("-1", "找不到目标");
        }
        return re;
    }

    /**
     * 处理用户经验变更服务
     *
     * @param userId      用户id
     * @param changeScore 要变更的经验/积分
     * @return 操作结果
     */
    public ResultDto<String> processUserScoreChange(String userId, Integer changeScore) {
        Member user = memberServiceImap.selectById(userId);
        if (user == null) {
            return ResultDto.error("126", "用户不存在");
        }
        IncentAccountScore scoreCount = accountScoreServiceImap.selectOne(new EntityWrapper<IncentAccountScore>().eq("mb_id", userId).eq("account_group_id", 1));
        if (scoreCount == null) {
            scoreCount = accountScoreDaoServiceImap.createAccountScore(user, 1);
        }
        scoreCount.setScoreUsable(scoreCount.getScoreUsable().subtract((new BigDecimal(changeScore))));
        scoreCount.setUpdatedAt(new Date());
        scoreCount.updateById();

        user.setExp(scoreCount.getScoreUsable().intValue());
        user = scoreLogService.updateLevel(user);
        //scoreLogService

        IncentRanked ranked = incentRankedServiceImap.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", userId).eq("rank_type", 1));
        Long prelevel = ranked.getCurrentRankId();//记录中的等级 可能需要修改
        int curLevel = scoreLogService.getLevel(scoreCount.getScoreUsable().intValue());//现在应有的等级
        //用户需不需要变更等级
        ObjectMapper mapper = new ObjectMapper();
        if (curLevel != prelevel) {
            ranked.setCurrentRankId((long) curLevel);
            ranked.setCurrentRankName("Lv" + curLevel);
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
                if (m.get("rankId").equals(curLevel + "")) {
                    add = false;
                }
            }
            if (add) {
                HashMap<String, String> newMap = new HashMap<String, String>();
                newMap.put("rankId", curLevel + "");
                newMap.put("rankName", "Lv" + curLevel);
                rankList.add(newMap);
                try {
                    ranked.setRankIdtIds(mapper.writeValueAsString(rankList));
                } catch (JsonProcessingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //等级变更记录 t_incent_ranked_log 更新
            }
            ranked.setUpdatedAt(new Date());
            ranked.updateById();
            IncentRuleRank rankRule = ruleRankServiceImap.selectOne(new EntityWrapper<IncentRuleRank>().eq("id", curLevel));
            scoreLogService.addRankLog(userId, rankRule);
        }
        return ResultDto.success();
    }

    @Override
    public ResultDto<IncentRuleRankDto> getIncentRuleRankById(String id) {
        IncentRuleRank rank = rankRuleService.selectById(id);
        IncentRuleRankDto ruleRankDto = new IncentRuleRankDto();
        if(rank == null){
            return ResultDto.error("-1","未查询到数据");
        }
        try {
            BeanUtils.copyProperties(ruleRankDto, rank);
        } catch (Exception e) {
            LOGGER.error("SystemServiceImpl.getIncentRuleRankById", e);
            return ResultDto.error("-1", "数据转化异常");
        }
        return ResultDto.success(ruleRankDto);
    }

    @Override
    public ResultDto<List<BasActionDto>> listActionByCondition(BasActionDto basActionDto) {
        //条件拼接
        EntityWrapper<BasAction> actionEntityWrapper = new EntityWrapper<>();
        if (StringUtils.isNotBlank(basActionDto.getMemberId())) {
            actionEntityWrapper.eq("member_id", basActionDto.getMemberId());
        }
        if (basActionDto.getType() != null) {
            actionEntityWrapper.eq("type", basActionDto.getType());
        }
        if (basActionDto.getTargetType() != null) {
            actionEntityWrapper.eq("target_type", basActionDto.getTargetType());
        }
        if (StringUtils.isNotBlank(basActionDto.getTargetId()) ) {
            actionEntityWrapper.eq("target_id", basActionDto.getTargetId());
        }
        if (basActionDto.getState() != null) {
            actionEntityWrapper.eq("state", basActionDto.getState());
        }
        //根据条件查询
        List<BasAction> basActions = actionServiceImap.selectList(actionEntityWrapper);
        List<BasActionDto> basActionsDtos = null;
        try {
            basActionsDtos = CommonUtils.deepCopy(basActions, BasActionDto.class);
        } catch (Exception e) {
            LOGGER.error("SystemServiceImpl.listActionByCondition", e);
            return ResultDto.error("-1", "数据转化异常");
        }
        return ResultDto.success(basActionsDtos);
    }

    @Override
    public ResultDto<MemberFollowerDto> getMemberFollower1(MemberFollowerDto memberFollowerDto) {
        MemberFollower one = memberFollowerServiceImap.selectOne(new EntityWrapper<MemberFollower>().eq("member_id", memberFollowerDto.getMemberId()).eq("follower_id", memberFollowerDto.getFollowerId()).andNew("state = {0}", 1).or("state = {0}", 2));
        if(one == null){
            return ResultDto.success();
        }
        MemberFollowerDto followerDto = new MemberFollowerDto();
        try {
            BeanUtils.copyProperties(followerDto, one);
        } catch (Exception e) {
            LOGGER.error("SystemServiceImpl.getMemberFollower1", e);
            return ResultDto.error("-1", "数据转化异常");
        }
        return ResultDto.success(followerDto);
    }

    @Override
    public ResultDto<Integer> countActionNumGameUse(BasActionDto basActionDto) {
        int liked = actionServiceImap.selectCount(new EntityWrapper<BasAction>().eq("type", 0).notIn("state", "-1").eq("target_id", basActionDto.getTargetId()).eq("member_id", basActionDto.getMemberId()));
        return ResultDto.success(liked);
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

    /**
     * 扣减指定用户经验值
     */
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

    /**
     * 获取用户关注的社区id列表
     */
    @Override
    public ResultDto<List<String>> listFollowerCommunityId(String memberId) {
        List<String> ids = basActionDao.getFollowerCommunityId(memberId);
        return ResultDto.success(ids);

    }

    /**
     * 获取动作数量(比如点赞)
     */
    @Override
    public ResultDto<Integer> countActionNum(BasActionDto basActionDto) {
        //条件拼接
        EntityWrapper<BasAction> actionEntityWrapper = new EntityWrapper<>();
        if (StringUtil.isNotNull(basActionDto.getMemberId()) ) {
            actionEntityWrapper.eq("member_id", basActionDto.getMemberId());
        }
        if (basActionDto.getType() != null) {
            actionEntityWrapper.eq("type", basActionDto.getType());
        }
        if (basActionDto.getTargetType() != null) {
            actionEntityWrapper.eq("target_type", basActionDto.getTargetType());
        }
        if ( StringUtil.isNotNull(basActionDto.getTargetId())) {
            actionEntityWrapper.eq("target_id", basActionDto.getTargetId());
        }
        if (basActionDto.getState() != null) {
            actionEntityWrapper.eq("state", basActionDto.getState());
        }
        //根据条件查询
        int count = actionServiceImap.selectCount(actionEntityWrapper);
        return ResultDto.success(count);
    }


    @Override
    public ResultDto<List<String>> listtargetId(BasActionDto basActionDto) {

        //Wrapper<BasAction> wrapper = Condition.create().setSqlSelect("target_id");
        // EntityWrapper<BasAction> wrapper = new EntityWrapper<>();
        // wrapper.setSqlSelect("target_id");

        Wrapper<BasAction> wrapper = Condition.create().setSqlSelect(" target_id as targetId");

        if (StringUtil.isNotNull(basActionDto.getMemberId() )) {
            wrapper.eq("member_id", basActionDto.getMemberId());
        }
        if (basActionDto.getType() != null) {
            wrapper.eq("type", basActionDto.getType());
        }
        if (basActionDto.getTargetType() != null) {
            wrapper.eq("target_type", basActionDto.getTargetType());
        }
        if (basActionDto.getState() != null) {
            wrapper.eq("state", basActionDto.getState());
        }
        List<BasAction> actionList = actionServiceImap.selectList(wrapper);
        LOGGER.info(actionList.size() + "");
        List<String> ids = actionList.stream().filter(li -> {
            return li != null && li.getTargetId() != null && !"".equals(li.getTargetId());
        }).map(BasAction::getTargetId).collect(Collectors.toList());
        return ResultDto.success(ids);

    }


    /**
     * 新增用户行为记录
     */
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

    @Override
    public ResultDto<List<MemberDto>> listMembersByids(List<String> ids, Integer state) {
        EntityWrapper<Member> wrapper = new EntityWrapper<>();
        if(ids==null||ids.size()==0){
            return ResultDto.success(new ArrayList<>());
        }
        wrapper.in("id", ids);
        if (state != null) {
            wrapper.eq("state", state);
        }
        List<Member> members = memberServiceImap.selectList(wrapper);
        List<MemberDto> memberFollowerDtos = null;
        try {
            memberFollowerDtos = CommonUtils.deepCopy(members, MemberDto.class);
        } catch (Exception e) {
            LOGGER.error("SystemService.listMembersByids error", e);
            return ResultDto.error("-1", "SystemService.listMembersByids error");
        }
        return ResultDto.success(memberFollowerDtos);
    }

    @Override
    public ResultDto<List<IncentRankedDto>> listIncentrankeByids(List<String> ids, Integer rankType) {
        EntityWrapper<IncentRanked> wrapper = new EntityWrapper<>();
        wrapper.in("mb_id", ids);
        wrapper.eq("rank_type", rankType);
        List<IncentRanked> incentRankeds = incentRankedServiceImap.selectList(wrapper);
        List<IncentRankedDto> rankDtos = null;
        try {
            rankDtos = CommonUtils.deepCopy(incentRankeds, IncentRankedDto.class);
        } catch (Exception e) {
            LOGGER.error("SystemService.listIncentrankeByids error", e);
            return ResultDto.error("-1", "SystemService.listIncentrankeByids error");
        }
        return ResultDto.success(rankDtos);
    }

    @Override
    public ResultDto<Map<String, Object>> exTask(TaskDto taskDto) {
        if (StringUtil.isNull(taskDto.getRequestId())) {
            return ResultDto.error("-1", "任务唯一请求码缺失");
        }
        ResultDto<Map<String, Object>> re = null;
        try{
        if(!UniqueIdCkeckUtil.checkUniqueIdAndSave(taskDto.getRequestId())){
            return ResultDto.error("-1", "业务已执行");
        }
            Map<String, Object> map = null;

            if (StringUtil.isNull(taskDto.getTargetId())) {
                    map = iMemberIncentDoTaskFacadeService.exTask(taskDto.getMbId(), taskDto.getTaskGroupFlag(), taskDto.getTaskType(), taskDto.getTypeCodeIdt());
                } else {
                    map = iMemberIncentDoTaskFacadeService.exTask(taskDto.getMbId(), taskDto.getTaskGroupFlag(), taskDto.getTaskType(), taskDto.getTypeCodeIdt(), taskDto.getTargetId());
                }
                re = ResultDto.success(map);
        }catch (Exception e){
            //手动开启事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            //删除唯一请求标志,使逻辑可以重试
            UniqueIdCkeckUtil.deleteUniqueId(taskDto.getRequestId());
            LOGGER.error("SystemServiceImpl.exTask", e);
            re = ResultDto.error("-1", "任务执行异常");
        }
        return re;
    }

    @Override
    public ResultDto<AuthorBean> getAuthor(String memberId) {
        AuthorBean author = userService.getAuthor(memberId);
        return ResultDto.success(author);
    }

    @Override
    public ResultDto<AuthorBean> getUserCard(String cardId, String memberId) {
        AuthorBean authorBean = userService.getUserCard(cardId, memberId);
        return ResultDto.success(authorBean);
    }

    public ResultDto<String> updateActionUpdatedAtByCondition(Map<String, Object> map) {
        BasAction action = actionServiceImap.selectOne(new EntityWrapper<BasAction>()
                .eq("member_id", map.get("memberId"))
                .eq("target_id", map.get("targetId"))
                .eq("target_type", map.get("targetType"))
                .eq("type", map.get("type"))
                .eq("state", map.get("state")));
        if (action != null) {
            action.setUpdatedAt(new Date());
            actionServiceImap.updateAllColumnById(action);
        }
        return ResultDto.success();
    }

    @Override
    public ResultDto<MemberDto> getMembersByid(String id) {
        Member member = memberServiceImap.selectById(id);
        MemberDto memberDto = new MemberDto();
        if(member == null){
            return ResultDto.error("-1","未查询到数据");
        }
        try {
            BeanUtils.copyProperties(memberDto, member);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("SystemServiceImpl.getMembersByid", e);
            return ResultDto.error("-1", "异常");
        }
        return ResultDto.success(memberDto);
    }

    @Override
    public ResultDto<List<HashMap<String, Object>>> getStatusImage(String memberId) {
        List<HashMap<String, Object>> image;
        try {
            image = userService.getStatusImage(memberId);
        } catch (Exception e) {
            LOGGER.error("SystemServiceImpl.getStatusImage", e);
            return ResultDto.error("-1", "异常");
        }
        return ResultDto.success(image);
    }

 /*   @Override
    public ResultDto<List<BasTagDto>> listBasTags(List<String> collect) {
        List<BasTag> tagList = bagTagService.selectList(new EntityWrapper<BasTag>().in("id", collect));
        List<BasTagDto> tagDtoList = null;
        try {
            tagDtoList = CommonUtils.deepCopy(tagList, BasTagDto.class);
        } catch (Exception e) {
            LOGGER.error("SystemService.listBasTags error", e);
            return ResultDto.error("-1", "SystemService.listBasTags error");
        }
        return ResultDto.success(tagDtoList);
    }*/

    /*@Override
    public ResultDto<List<BasTagDto>> listBasTagByGroup(String groupId) {
        List<BasTag> tagList = bagTagService.selectList(new EntityWrapper<BasTag>().in("group_id", groupId));
        List<BasTagDto> tagDtoList = null;
        try {
            tagDtoList = CommonUtils.deepCopy(tagList, BasTagDto.class);
        } catch (Exception e) {
            LOGGER.error("SystemService.listBasTagByGroup error", e);
            return ResultDto.error("-1", "SystemService.listBasTagByGroup error");
        }
        return ResultDto.success(tagDtoList);
    }

    @Override
    public ResultDto<List<BasTagGroupDto>> listBasTagGroupByCondition(BasTagGroupDto basTagGroupDto) {
        BasTag basTag = new BasTag();
        if(basTagGroupDto == null){
            return ResultDto.error("-1","参数为null");
        }
        List<BasTagGroupDto> basTagDtoList;
        try {
            BeanUtils.copyProperties(basTag, basTagGroupDto);
            //获得全部分类以及标签，在选中的打勾
            List<BasTagGroup> basTagGroupList = basTagGroupService.selectList(new EntityWrapper<BasTagGroup>());
            basTagDtoList = CommonUtils.deepCopy(basTagGroupList, BasTagGroupDto.class);
        } catch (Exception e) {
            LOGGER.error("SystemService.listBasTagGroupByCondition error", e);
            return ResultDto.error("-1", "SystemService.listBasTagGroupByCondition error");
        }
        return ResultDto.success(basTagDtoList);
    }*/

 /*   @Override
    public ResultDto<List<TagBean>> listSortTags(List<String> tags) {
        List<BasTag> tagList = bagTagService.selectList(new EntityWrapper<BasTag>().in("id", tags));
        ArrayList<TagBean> list = new ArrayList<>();
        for (BasTag basTag : tagList) {
            TagBean tagBean = new TagBean();
            tagBean.setName(basTag.getName());
            list.add(tagBean);
        }
        return ResultDto.success(list);
    }*/

  /*  @Override
    public ResultDto<BasTagDto> getBasTagById(String id) {
        BasTag tag = bagTagService.selectById(id);
        if(tag == null){
            return ResultDto.error("-1","未查询到数据");
        }
        BasTagDto tagDto = new BasTagDto();
        try {
            BeanUtils.copyProperties(tagDto, tag);
        } catch (Exception e) {
            LOGGER.error("SystemService.getBasTagById error", e);
            return ResultDto.error("-1", "SystemService.getBasTagById error");
        }
        return ResultDto.success(tagDto);
    }*/

    @Override
    public ResultDto<Integer> countSerchUserName(String keyword) {
        int count = memberServiceImap.selectCount(new EntityWrapper<Member>().where("state = {0}", 0).like("user_name", keyword));
        return ResultDto.success(count);
    }




    @Override
    public ResultDto<List<MemberDto>> listWatchMebmber(Integer limit, String currentMbId) {
        List<MemberDto> memberDtoList = new ArrayList<>();
        EntityWrapper actionEntityWrapper = new EntityWrapper<BasAction>();
        actionEntityWrapper.setSqlSelect("target_id as targetId");

        //target_type 0 关注用户
        actionEntityWrapper.eq("state", "0").eq("type", 5).eq("member_id", currentMbId).eq("target_type", 0);
        actionEntityWrapper.orderBy("created_at", false);
        if (limit > 0) {
            actionEntityWrapper.last("limit " + limit);
        }
        List<BasAction> watchMebmberIdsList = actionServiceImap.selectList(actionEntityWrapper);

        if (null != watchMebmberIdsList && !watchMebmberIdsList.isEmpty()) {
            StringBuffer mbIds = new StringBuffer();
            for (BasAction basAction : watchMebmberIdsList) {
                if (null != basAction) {
                    mbIds = mbIds.append(basAction.getTargetId());
                    mbIds = mbIds.append(",");
                }
            }
            mbIds.deleteCharAt(mbIds.length() - 1);

            //查询出符合推荐条件用户的详情数据
            if (mbIds.length() > 0) {
                List<Member> watchMebmberList = memberServiceImap.selectList(new EntityWrapper<Member>().in("id", mbIds.toString()).eq("state", 0));
                try {
                    memberDtoList = CommonUtils.deepCopy(watchMebmberList, MemberDto.class);
                } catch (Exception e) {
                    LOGGER.error("SystemService.listBasTags error", e);
                    return ResultDto.error("-1", "SystemService.listBasTags error");
                }
            }
        }
        return ResultDto.success(memberDtoList);
    }

    /**
     * 修改通知数据
     * @param id 通知id
     * @param data 通知数据
     * @return 结果
     */
    public ResultDto<String> updateNoticeDate(String id,String data){
        BasNotice notice = new BasNotice();
        notice = notice.selectById(id);
        notice.setData(data);
        notice.updateById();
        return ResultDto.success();
    }

    @Override
    public ResultDto<List<String>> listCommunityHisIds(BasActionDto basActionDto) {

        //条件拼接
        EntityWrapper<BasAction> actionEntityWrapper = new EntityWrapper<>();
        actionEntityWrapper.setSqlSelect(" DISTINCT(target_id) as targetId");
        if (StringUtils.isNotBlank(basActionDto.getMemberId())) {
            actionEntityWrapper.eq("member_id", basActionDto.getMemberId());
        }
        if (basActionDto.getType() != null) {
            actionEntityWrapper.eq("type", basActionDto.getType());
        }
        if (basActionDto.getTargetType() != null) {
            actionEntityWrapper.eq("target_type", basActionDto.getTargetType());
        }


        //根据条件查询
        List<BasAction> actions = actionServiceImap.selectList(actionEntityWrapper);
        ArrayList<String> list = new ArrayList<>();
        for (BasAction action : actions) {
            list.add(action.getTargetId());
        }

        return ResultDto.success(list);

    }

    public ResultDto<List<MemberDto>> listRecommendedMebmber(Integer limit, String currentMbId, List<String> wathMbsSet) {
        List<MemberDto> memberDtoList;
        EntityWrapper memberSqlWrapper = new EntityWrapper<Member>();
        memberSqlWrapper.eq("type", 2).eq("state", 0);
        //去除当前登录用户和已经关注用户
        if (StringUtils.isNotBlank(currentMbId) || !wathMbsSet.isEmpty()) {
            wathMbsSet.add(currentMbId);
            memberSqlWrapper.notIn("id", wathMbsSet);
        }
        memberSqlWrapper.orderBy("sort", false);
        memberSqlWrapper.last("limit " + limit);
        List<Member> ml1 = memberServiceImap.selectList(memberSqlWrapper);
        try {
            memberDtoList = CommonUtils.deepCopy(ml1, MemberDto.class);
        } catch (Exception e) {
            LOGGER.error("SystemService.listBasTags error", e);
            return ResultDto.error("-1", "SystemService.listBasTags error");
        }
        return ResultDto.success(memberDtoList);
    }
}