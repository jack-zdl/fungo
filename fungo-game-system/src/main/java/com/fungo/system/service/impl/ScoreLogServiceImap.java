package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fungo.system.dao.ScoreLogDao;
import com.fungo.system.entity.*;
import com.fungo.system.service.*;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.consts.FunGoGameConsts;
import com.game.common.dto.ResultDto;
import com.game.common.repo.cache.facade.FungoCacheMember;
import com.game.common.util.exception.BusinessException;
import com.game.common.util.CommonUtil;
import com.game.common.util.FunGoEHCacheUtils;
import com.game.common.util.PKUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 评分日志 服务实现类
 * </p>
 *
 * @author lzh
 * @since 2018-05-07
 */
@Service
public class ScoreLogServiceImap extends ServiceImpl<ScoreLogDao, ScoreLog> implements ScoreLogService {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ScoreRuleServiceImap scoreRuleService;

    @Autowired
    private IncentAccountCoinDaoService accountCoinService;

    @Autowired
    private IncentAccountScoreService accountScoreService;

    @Autowired
    private IncentRankedService rankedService;

    @Autowired
    private IncentTaskedService incentTaskService;

    @Autowired
    private IMemberAccountScoreDaoService IAccountDaoService;

    @Autowired
    private IncentRuleRankService ruleRankService;

    @Autowired
    private FungoCacheMember fungoCacheMember;


    @Value("${sys.config.fungo.cluster.index}")
    private String clusterIndex;

    private static ObjectMapper mapper = new ObjectMapper();


    @Override
    public ResultDto<String> finishTask(String userId, String expcode, Object object) throws IOException, ParseException {

        Member user = memberService.selectById(userId);
        if (user == null) {
            throw new BusinessException("-1", "用户不存在");
        }
        if (expcode == null) {
            throw new BusinessException("-1", "任务类型码不存在");
        }

        ScoreRule targetTask = scoreRuleService.selectOne(new EntityWrapper<ScoreRule>().eq("code", expcode).eq("is_active", 1));
        if (targetTask == null) {
            throw new BusinessException("-1", "找不到目标任务");
        }

        //设置时间 今日零点
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date todayDate = format.parse(format.format(new Date()));
        //查出今天的目标任务日志个数
        int count = selectCount(new EntityWrapper<ScoreLog>().eq("member_id", userId).eq("type", expcode).ge("created_at", todayDate));
        //今日完成上限
        Integer dayLimit = targetTask.getMax() / targetTask.getScore();
        if (count < dayLimit) {
            //加积分，添加日志,更新等级
            Integer score = targetTask.getScore();

            user.setExp(user.getExp() + score);
            user = updateLevel(user);

            ScoreLog newLog = new ScoreLog();
            newLog.setMemberId(userId);
            newLog.setType(expcode);
            newLog.setScore(score);

            if (object != null && !"".equals(object)) {
                ObjectMapper mapper = new ObjectMapper();
                String info = mapper.writeValueAsString(object);
                newLog.setInfo(info);
            }

            newLog.setCreatedAt(new Date());
            newLog.setUpdatedAt(new Date());
            insert(newLog);
            return ResultDto.success();
        }

        return ResultDto.error("-1", "任务次数已达每日上限");

    }

    /**
     * 经验值任务
     */
    @Override
    public ResultDto<Integer> expTask(String userId, int icode, Object object, String target_id, int target_type) throws Exception {
        ScoreRule expRule = scoreRuleService.selectOne(new EntityWrapper<ScoreRule>().eq("code_idt", icode).eq("is_active", 1));
        if (expRule == null) {
            return ResultDto.error("-1", "找不到目标任务");
        }
        Member user = memberService.selectById(userId);
        if (user == null) {
            throw new BusinessException("-1", "用户不存在");
        }

        //任务生命周期判定 是否能够进行
        boolean flag = false;
        IncentTasked tasked = incentTaskService.selectOne(new EntityWrapper<IncentTasked>().eq("mb_id", userId));
        if (tasked == null) {
            flag = true;
        } else {
            flag = doTask(tasked, icode, target_id, target_type, expRule);
        }

        if (flag) {
            return finishExpTask(userId, object, target_id, target_type, expRule, user);
        }

        return ResultDto.error("-1", "未完成");

    }

    //@CacheEvict(cacheNames = {FunGoGameConsts.CACHE_EH_NAME } ,key = "'" + FunGoGameConsts.CACHE_EH_KEY_PRE_MEMBER +"_TaskProgress' + #userId")
//	@FunGoCacheRemove(value = FunGoGameConsts.CACHE_EH_NAME, key = { FunGoGameConsts.CACHE_EH_KEY_PRE_MEMBER + "_TaskProgress"  })
    public ResultDto<Integer> finishExpTask(String userId, Object object, String target_id, int target_type,
                                            ScoreRule expRule, Member user)
            throws IOException, JsonParseException, JsonMappingException, JsonProcessingException, Exception {
        //加积分，添加日志,更新等级
        Integer score = expRule.getScore();

        //更新账户
        IncentAccountScore scoreCount = accountScoreService.selectOne(new EntityWrapper<IncentAccountScore>().eq("mb_id", userId).eq("account_group_id", 1));
        if (scoreCount == null) {
            scoreCount = IAccountDaoService.createAccountScore(user, 1);
        }
        scoreCount.setScoreUsable(scoreCount.getScoreUsable().add(new BigDecimal(score)));
        scoreCount.setUpdatedAt(new Date());
        scoreCount.updateById();

        user.setExp(scoreCount.getScoreUsable().intValue());
        user = updateLevel(user);

        IncentRanked ranked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", userId).eq("rank_type", 1));
        if (ranked == null) {
            return ResultDto.error("-1", "异常用户,无法操作");
        }
        Long prelevel = ranked.getCurrentRankId();//记录中的等级 可能需要修改
        int curLevel = getLevel(scoreCount.getScoreUsable().intValue());//现在应有的等级
        //用户需不需要升级
        ObjectMapper mapper = new ObjectMapper();
        if (curLevel != prelevel) {
            ranked.setCurrentRankId((long) curLevel);
            ranked.setCurrentRankName("Lv" + curLevel);

            String rankIdtIds = ranked.getRankIdtIds();
            List<HashMap<String, String>> rankList = mapper.readValue(rankIdtIds, ArrayList.class);
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
                ranked.setRankIdtIds(mapper.writeValueAsString(rankList));
                //等级变更记录 t_incent_ranked_log 更新
            }
            ranked.setUpdatedAt(new Date());
            ranked.updateById();
            IncentRuleRank rankRule = ruleRankService.selectOne(new EntityWrapper<IncentRuleRank>().eq("id", curLevel));
            addRankLog(userId, rankRule);

        }

        //任务日志
        addTaskLog(userId, object, expRule, score, mapper, target_id, target_type);

        //任务汇总
        int finishTimes = updateTasked(userId, expRule, mapper);

        FunGoEHCacheUtils.remove(FunGoGameConsts.CACHE_EH_NAME, FunGoGameConsts.CACHE_EH_KEY_PRE_MEMBER + "_TaskProgress" + userId);

        return ResultDto.success(finishTimes);
    }


    //任务生命周期判定 是否能够进行
    private boolean doTask(IncentTasked tasked, int icode, String target_id, int target_type, ScoreRule expRule)
            throws Exception {
//		boolean f = false;
//		if(expRule.getIncomieFregType() == 1) {//一天一次
//			SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
//			Date todayDate = format.parse(format.format(new Date()));
//	        //查出今天的目标任务日志个数
//			int count = selectCount(new EntityWrapper<ScoreLog>().eq("member_id", tasked).eq("code_idt", icode).ge("created_at", todayDate));
//			//今日完成上限
//			Integer dayLimit = expRule.getMax();
//
//			if(count < dayLimit) {
//				f = true;
//			}
//
//		}else if(expRule.getIncomieFregType() == 2) {//生命周期一次
//			if(this.selectCount(new EntityWrapper<ScoreLog>().eq("code_idt", icode).eq("member_id",tasked)) == 0) {
//				f = true;
//			}
//		}else if(expRule.getIncomieFregType() == 4) {//一条数据一次
//			if(this.selectCount(new EntityWrapper<ScoreLog>().eq("code_idt", icode).eq("target_type", target_type).eq("target_id", target_id)) == 0) {
//				f = true;
//			}
//		}
        if (expRule.getIncomieFregType() == 1) {//一天周期
            ArrayList<Map<String, Object>> taskList = mapper.readValue(tasked.getTaskIdtIds(), ArrayList.class);
            for (Map<String, Object> map : taskList) {
                if (map.get("1").equals(expRule.getId())) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date todayDate = format.parse(format.format(new Date()));
                    //是否今天
                    Date taskDate = format.parse((String) map.get("6"));
                    if (taskDate.before(todayDate)) {
                        return true;
                    } else {
                        int count = (int) map.get("5");
                        if (count < expRule.getMax()) {
                            return true;
                        } else {
                            return false;
                        }
                    }

                }
            }
            return true;

        } else if (expRule.getIncomieFregType() == 2) {//整个生命周期
            ArrayList<Map<String, Object>> taskList = mapper.readValue(tasked.getTaskIdtIds(), ArrayList.class);
            for (Map<String, Object> map : taskList) {
                if (map.get("1").equals(expRule.getId())) {
                    if (expRule.getMax() <= (int) map.get("5")) {
                        return false;
                    }
                }
            }
            return true;

        } else if (expRule.getIncomieFregType() == 4) {//一条数据一次
            if (this.selectCount(new EntityWrapper<ScoreLog>().eq("code_idt", icode).eq("target_type", target_type).eq("target_id", target_id)) == 0) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void addTaskLog(String userId, Object object, ScoreRule expRule, Integer score, ObjectMapper mapper, String target_id, int target_type)
            throws JsonProcessingException {
        ScoreLog newLog = new ScoreLog();
        newLog.setMemberId(userId);
        newLog.setType(expRule.getCode());
        newLog.setTaskType(expRule.getTaskType());
        newLog.setCodeIdt(expRule.getCodeIdt());
        newLog.setScore(score);
        newLog.setGroupId(expRule.getGroupId());
        newLog.setRuleId(expRule.getId());
        newLog.setRuleName(expRule.getName());
//		target
        if (!CommonUtil.isNull(target_id)) {
            newLog.setTargetId(target_id);
        }
        if (target_type != -1) {
            newLog.setTargetType(target_type);
        }
        if (object != null && !"".equals(object)) {
            String info = mapper.writeValueAsString(object);
            newLog.setInfo(info);
        }
        newLog.setCreatedAt(new Date());
        newLog.setUpdatedAt(new Date());
        insert(newLog);
    }

    @Override
    public int updateTasked(String userId, ScoreRule expRule, ObjectMapper mapper)
            throws Exception {
        //用户任务记录表
        IncentTasked tasked = incentTaskService.selectOne(new EntityWrapper<IncentTasked>().eq("mb_id", userId));
        int count = 1;
        if (tasked != null) {//记录已存在

            //{1:taskid,2:taskname,3:score,4:type,5:count}
            tasked.setCurrentTaskId(expRule.getId());
            tasked.setCurrentTaskName(expRule.getName());
            tasked.setTaskType(expRule.getTaskType());

            ArrayList<Map<String, Object>> taskList = mapper.readValue(tasked.getTaskIdtIds(), ArrayList.class);
            boolean contain = false;
            for (Map<String, Object> map : taskList) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date todayDate = format.parse(format.format(new Date()));
                //是否今天
                Date taskDate = format.parse((String) map.get("6"));
                if (map.get("1").equals(expRule.getId())) {
                    if (taskDate.before(todayDate)) {//最后一次完成时间在今天之前
                        map.put("6", format.format(todayDate));
                        if (expRule.getIncomieFregType() == 1 && 999 != expRule.getCodeIdt()) {//任务周期为1天或者是每日签到任务
                            map.put("5", 1);
                        } else {
                            map.put("5", (int) map.get("5") + 1);
                        }
                    } else {//今天已完成过
                        map.put("5", (int) map.get("5") + 1);
                    }
                    count = (int) map.get("5");
                    contain = true;

                    //添加收益频率类型
                    if (map.get("7") == null) {
                        map.put("7", expRule.getIncomieFregType());
                    }
                }
            }
            //列表中没有此任务 添加
            if (!contain) {
                Map<String, Object> map = new HashMap<>();
                map.put("1", expRule.getId());
                map.put("2", expRule.getName());
                map.put("3", expRule.getScore());
                map.put("4", expRule.getTaskType());
                map.put("5", 1);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                map.put("6", format.format(new Date()));
                map.put("7", expRule.getIncomieFregType());
                taskList.add(map);
            }
            tasked.setTaskIdtIds(mapper.writeValueAsString(taskList));
            tasked.setUpdatedAt(new Date());
            tasked.updateById();

        } else {
            tasked = new IncentTasked();
            tasked.setCurrentTaskId(expRule.getId());
            tasked.setCurrentTaskName(expRule.getName());
            tasked.setTaskType(expRule.getTaskType());
            tasked.setMbId(userId);
            ArrayList<Map<String, Object>> taskList = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("1", expRule.getId());
            map.put("2", expRule.getName());
            map.put("3", expRule.getScore());
            map.put("4", expRule.getTaskType());
            map.put("5", 1);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            map.put("6", format.format(new Date()));
            map.put("7", expRule.getIncomieFregType());
            taskList.add(map);
            tasked.setTaskIdtIds(mapper.writeValueAsString(taskList));
            tasked.setCreatedAt(new Date());
            tasked.setUpdatedAt(new Date());

            Integer clusterIndex_i = Integer.parseInt(clusterIndex);
            tasked.setId(PKUtil.getInstance(clusterIndex_i).longPK());
            tasked.insert();
        }
        //完成任务的次数 如果任务收益频率为一天就是当天完成的次数
        return count;
    }

    @Override
    public ResultDto<Integer> funCoinTask(String userId, int icode, Object object, String target_id, int target_type) throws Exception {
        ScoreRule coinRule = scoreRuleService.selectOne(new EntityWrapper<ScoreRule>().eq("code_idt", icode).eq("is_active", 1));
        if (coinRule == null) {
            return ResultDto.error("-1", "找不到目标任务");
        }
        Member user = memberService.selectById(userId);
        if (user == null) {
            throw new BusinessException("-1", "用户不存在");
        }
//		//任务奖励经验 更新  更新等级 更新日志 更新统计
//
//		//设置时间 今日零点
//		SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
//		Date todayDate = format.parse(format.format(new Date()));
//        //查出今天的目标任务日志个数
//		int count = selectCount(new EntityWrapper<ScoreLog>().eq("member_id", userId).eq("code_idt", icode).ge("created_at", todayDate));
//		//今日完成上限
//		Integer dayLimit = coinRule.getMax();
        boolean flag = false;
        IncentTasked tasked = incentTaskService.selectOne(new EntityWrapper<IncentTasked>().eq("mb_id", userId));
        if (tasked == null) {
            flag = true;
        } else {
            flag = doTask(tasked, icode, target_id, target_type, coinRule);
        }
        if (flag) {
            return finishFoinTask(userId, object, target_id, target_type, coinRule, user);
        }
        return null;

    }

    //	@CacheEvict(cacheNames = {FunGoGameConsts.CACHE_EH_NAME } ,key = "'" + FunGoGameConsts.CACHE_EH_KEY_PRE_MEMBER +"_TaskProgress' + #userId")
//	@FunGoCacheRemove(value = FunGoGameConsts.CACHE_EH_NAME, key = { FunGoGameConsts.CACHE_EH_KEY_PRE_MEMBER + "_TaskProgress"  })
    public ResultDto<Integer> finishFoinTask(String userId, Object object, String target_id, int target_type,
                                             ScoreRule coinRule, Member user) throws JsonProcessingException, Exception {
        //加积分，添加日志,更新等级
        Integer score = coinRule.getScore();
        //更新账户
        IncentAccountCoin coinAccount = accountCoinService.selectOne(new EntityWrapper<IncentAccountCoin>().eq("mb_id", userId).eq("account_group_id", 3));
        if (coinAccount == null) {
            coinAccount = IAccountDaoService.createAccountCoin(user);
        }

        coinAccount.setCoinUsable(coinAccount.getCoinUsable().add(new BigDecimal(score)));
        coinAccount.setUpdatedAt(new Date());
        coinAccount.updateById();

//			ObjectMapper mapper = new ObjectMapper();

        //更新日志、记录
        addTaskLog(userId, object, coinRule, score, mapper, target_id, target_type);

        int finishTimes = updateTasked(userId, coinRule, mapper);

        FunGoEHCacheUtils.remove(FunGoGameConsts.CACHE_EH_NAME, FunGoGameConsts.CACHE_EH_KEY_PRE_MEMBER + "_TaskProgress" + userId);

        // 个人资料
        fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + userId, "", null);
        //web端会员信息
        fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_WEBIINFO + userId, "", null);
        return ResultDto.success(finishTimes);
    }


    @Override
    public Member updateLevel(Member user) {
        int exp = user.getExp();
        int level = 0;
        if (0 < exp && exp <= 10) {
            level = 1;
        } else if (10 < exp && exp <= 50) {
            level = 2;
        } else if (50 < exp && exp <= 100) {
            level = 3;
        } else if (100 < exp && exp <= 200) {
            level = 4;
        } else if (200 < exp && exp <= 400) {
            level = 5;
        } else if (400 < exp && exp <= 600) {
            level = 6;
        } else if (600 < exp && exp <= 1000) {
            level = 7;
        } else if (1000 < exp && exp <= 2000) {
            level = 8;
        } else if (2000 < exp && exp <= 3000) {
            level = 9;
        } else if (3000 < exp && exp <= 5000) {
            level = 10;
        } else if (5000 < exp && exp <= 10000) {
            level = 11;
        } else if (10000 < exp && exp <= 20000) {
            level = 12;
        } else if (exp > 20000) {
            level = 12;
        }

        if (level != user.getLevel()) {
            user.setLevel(level);
        }
        memberService.updateById(user);

        //清除用户信息缓存
        //clear redis
        //其他会员信息接口 清除
        String keyPrefix_userCard = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_CARD + user.getId();
        fungoCacheMember.excIndexCache(false, keyPrefix_userCard, "", null);
        //清除 会员信息（web端）
        fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_WEBIINFO + user.getId(), "", null);
        //我的等级信息(2.4.3)
        fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_RANKS_LEVEL + user.getId(), "", null);
        // 个人资料
        fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + user.getId(), "", null);
        return user;

    }

    public int getLevel(int exp) {
        int level = 0;
        if (0 < exp && exp <= 10) {
            level = 1;
        } else if (10 < exp && exp <= 50) {
            level = 2;
        } else if (50 < exp && exp <= 100) {
            level = 3;
        } else if (100 < exp && exp <= 200) {
            level = 4;
        } else if (200 < exp && exp <= 400) {
            level = 5;
        } else if (400 < exp && exp <= 600) {
            level = 6;
        } else if (600 < exp && exp <= 1000) {
            level = 7;
        } else if (1000 < exp && exp <= 2000) {
            level = 8;
        } else if (2000 < exp && exp <= 3000) {
            level = 9;
        } else if (3000 < exp && exp <= 5000) {
            level = 10;
        } else if (5000 < exp && exp <= 10000) {
            level = 11;
        } else if (10000 < exp && exp <= 20000) {
            level = 12;
        } else if (exp > 20000) {
            level = 12;
        }

        return level;

    }

    @Override
    public void updateRanked(String userId, ObjectMapper mapper, int rankidt, Date createdAt) throws Exception {

        IncentRuleRank rankRule = ruleRankService.selectOne(new EntityWrapper<IncentRuleRank>().eq("rank_idt", rankidt));
        //荣誉汇总 类型
        IncentRanked incentRanked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", userId).eq("rank_type", rankRule.getRankType()));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String datos = "";
        //------fungo身份证 获取时间，是用户注册时间
        if (null != createdAt) {
            datos = format.format(createdAt);
        } else {
            datos = format.format(new Date());
        }

        boolean levelRank = rankRule.getRankType() == 1;
        if (incentRanked == null) {
            incentRanked = new IncentRanked();
            incentRanked.setMbId(userId);
            List<Map<String, Object>> mapList = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            if (levelRank) {
                map.put("rankId", rankRule.getId());
                map.put("rankName", rankRule.getRankName());
            } else {
                map.put("1", rankRule.getId());
                map.put("2", rankRule.getRankName());
                map.put("3", rankRule.getRankType());
                map.put("4", datos);
            }

            mapList.add(map);
            incentRanked.setRankIdtIds(mapper.writeValueAsString(mapList));
            incentRanked.setCurrentRankId(rankRule.getId());
            incentRanked.setCurrentRankName(rankRule.getRankName());
            incentRanked.setRankType(rankRule.getRankType());

            //？
            //incentRanked.setRankType(rankRule.getRankFlag());


            incentRanked.setCreatedAt(new Date());
            incentRanked.setUpdatedAt(new Date());

            Integer clusterIndex_i = Integer.parseInt(clusterIndex);
            incentRanked.setId(PKUtil.getInstance(clusterIndex_i).longPK());
            incentRanked.insert();
            addRankLog(userId, rankRule);
        } else {
            String rankIdtIds = incentRanked.getRankIdtIds();
            String key = "1";
            if (levelRank) {
                key = "rankId";
            }
            if (rankIdtIds != null) {
                ArrayList<Map<String, Object>> mapList = mapper.readValue(rankIdtIds, ArrayList.class);
                boolean add = true;
                for (Map<String, Object> m : mapList) {
                    if (Long.parseLong(m.get(key) + "") == rankRule.getId()) {
                        add = false;
                        break;
                    }
                }
                if (add) {
                    Map<String, Object> m = new HashMap<>();
                    if (levelRank) {
                        m.put("rankId", rankRule.getId());
                        m.put("rankName", rankRule.getRankName());
                    } else {
                        m.put("1", rankRule.getId());
                        m.put("2", rankRule.getRankName());
                        m.put("3", rankRule.getRankType());
                        m.put("4", datos);
                    }
                    mapList.add(m);
                    incentRanked.setRankIdtIds(mapper.writeValueAsString(mapList));
                    incentRanked.setCurrentRankId(rankRule.getId());
                    incentRanked.setCurrentRankName(rankRule.getRankName());
                    incentRanked.setRankType(rankRule.getRankType());

                    //?
                    //incentRanked.setRankType(rankRule.getRankFlag());

                    incentRanked.setUpdatedAt(new Date());
                    incentRanked.updateById();
                    addRankLog(userId, rankRule);
                }
            }

            //日志
        }
        //clear redis
        //清除 会员信息（web端）
        fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_WEBIINFO + userId, "", null);
        //其他会员信息
        fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_CARD + userId, "", null);
        // 个人资料
        fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + userId, "", null);
    }


    //更新荣誉-fungo身份证 汇总
    @Override
    public void updateRanked(String userId, ObjectMapper mapper, int rankidt)
            throws JsonProcessingException, IOException, JsonParseException, JsonMappingException {
        IncentRuleRank rankRule = ruleRankService.selectOne(new EntityWrapper<IncentRuleRank>().eq("rank_idt", rankidt));

        //荣誉汇总 类型
        IncentRanked incentRanked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", userId).eq("rank_type", rankRule.getRankType()));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String datos = "";
        //------fungo身份证 获取时间，是用户注册时间

        datos = format.format(new Date());


        boolean levelRank = rankRule.getRankType() == 1;
        if (incentRanked == null) {//用户没有该类型荣誉 新建记录

            incentRanked = new IncentRanked();
            incentRanked.setMbId(userId);
            List<Map<String, Object>> mapList = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();

            if (levelRank) {//等级荣誉
                map.put("rankId", rankRule.getId());
                map.put("rankName", rankRule.getRankName());
            } else {//非等级荣誉
                map.put("1", rankRule.getId());
                map.put("2", rankRule.getRankName());
                map.put("3", rankRule.getRankType());
                map.put("4", datos);
            }

            mapList.add(map);
            incentRanked.setRankIdtIds(mapper.writeValueAsString(mapList));
            incentRanked.setCurrentRankId(rankRule.getId());
            incentRanked.setCurrentRankName(rankRule.getRankName());
            incentRanked.setRankType(rankRule.getRankType());

            //？
            //incentRanked.setRankType(rankRule.getRankFlag());


            incentRanked.setCreatedAt(new Date());
            incentRanked.setUpdatedAt(new Date());

            Integer clusterIndex_i = Integer.parseInt(clusterIndex);
            incentRanked.setId(PKUtil.getInstance(clusterIndex_i).longPK());

            incentRanked.insert();
            addRankLog(userId, rankRule);

        } else {//用户已有该类型荣誉

            String rankIdtIds = incentRanked.getRankIdtIds();
            String key = "1";

            if (levelRank) {//等级荣誉
                key = "rankId";
            }

            if (rankIdtIds != null) {

                ArrayList<Map<String, Object>> mapList = mapper.readValue(rankIdtIds, ArrayList.class);
                boolean add = true;

                for (Map<String, Object> m : mapList) {
                    if (Long.parseLong(m.get(key) + "") == rankRule.getId()) {//汇总已包含当前荣誉
                        add = false;
                        break;
                    }
                }

                if (add) {//汇总未包含当前荣誉 添加
                    Map<String, Object> m = new HashMap<>();
                    if (levelRank) {
                        m.put("rankId", rankRule.getId());
                        m.put("rankName", rankRule.getRankName());
                    } else {
                        m.put("1", rankRule.getId());
                        m.put("2", rankRule.getRankName());
                        m.put("3", rankRule.getRankType());
                        m.put("4", datos);
                    }
                    mapList.add(m);
                    incentRanked.setRankIdtIds(mapper.writeValueAsString(mapList));
                    incentRanked.setCurrentRankId(rankRule.getId());
                    incentRanked.setCurrentRankName(rankRule.getRankName());
                    incentRanked.setRankType(rankRule.getRankType());

                    //?
                    //incentRanked.setRankType(rankRule.getRankFlag());

                    incentRanked.setUpdatedAt(new Date());

                    incentRanked.updateById();
                    addRankLog(userId, rankRule);
                }
            }

            //日志
        }
        //clear redis
        //清除 会员信息（web端）
        fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_WEBIINFO + userId, "", null);
        //其他会员信息
        fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_CARD + userId, "", null);
        // 个人资料
        fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + userId, "", null);
    }

    @Override
    public void addRankLog(String userId, IncentRuleRank rankRule) {
        IncentRankedLog rankLog = new IncentRankedLog();
        rankLog.setMbId(userId);
        rankLog.setGainTime(new Date());
        rankLog.setProduceType(1);
        rankLog.setRankCode(rankRule.getRankCode());
        rankLog.setRankGroupId(Long.parseLong(rankRule.getRankGroupId()));
        rankLog.setRankIdt(rankRule.getRankIdt());
        rankLog.setRankRuleId(rankRule.getId());
        rankLog.setUpdatedAt(new Date());
        rankLog.setCreatedAt(new Date());

        Integer clusterIndex_i = Integer.parseInt(clusterIndex);
        rankLog.setId(PKUtil.getInstance(clusterIndex_i).longPK());
        rankLog.insert();
    }


    @Override
    public List<ScoreLog> getScoreLogWithMbAndTask(String mb_id, int task_type, int task_code_idt) throws Exception {

        EntityWrapper<ScoreLog> logEntityWrapper = new EntityWrapper<>();

        logEntityWrapper.eq("member_id", mb_id);
        logEntityWrapper.eq("task_type", task_type);
        logEntityWrapper.eq("code_idt", task_code_idt);
        logEntityWrapper.orderBy("created_at", false);

        List<ScoreLog> scoreLogList = this.selectList(logEntityWrapper);
        return scoreLogList;
    }





    @Override
    public List<ScoreLog> getScoreLogWithMbAndTask(String mb_id, int task_type, int task_code_idt,String startDate,String endDate) throws Exception {

        EntityWrapper<ScoreLog> logEntityWrapper = new EntityWrapper<>();

        logEntityWrapper.eq("member_id", mb_id);
        logEntityWrapper.eq("task_type", task_type);
        logEntityWrapper.eq("code_idt", task_code_idt);

        logEntityWrapper.between("created_at", startDate, endDate);

        logEntityWrapper.orderBy("created_at", false);

        List<ScoreLog> scoreLogList = this.selectList(logEntityWrapper);
        return scoreLogList;
    }

    //----------
}
