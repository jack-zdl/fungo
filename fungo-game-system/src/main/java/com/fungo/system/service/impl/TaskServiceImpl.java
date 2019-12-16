package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.dao.BannerDao;
import com.fungo.system.entity.*;
import com.fungo.system.service.*;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.ResultDto;
import com.game.common.enums.FunGoTaskV243Enum;
import com.game.common.repo.cache.facade.FungoCacheTask;
import com.game.common.util.PKUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Transactional
public class TaskServiceImpl implements ITaskService {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ScoreLogService scoreLogService;

    @Autowired
    private ScoreRuleService scoreRuleService;

    @Autowired
    private ScoreGroupService scoreGroupService;

    @Autowired
    private IncentAccountCoinDaoService accountCoinService;

    @Autowired
    private IncentAccountScoreService accountScoreService;

    @Autowired
    private IncentRankedService rankedService;

    @Autowired
    private IMemberAccountScoreDaoService IAccountDaoService;

    @Autowired
    private IncentRuleRankService ruleRankService;

    @Autowired
    private IncentRankedLogService rankedLogService;

    @Autowired
    private BannerDao bannerDao;

    @Autowired
    private FungoCacheTask fungoCacheTask;

    @SuppressWarnings("rawtypes")
    @Override
    /*
     * (non-Javadoc)
     * @see com.fungo.game.api.ITaskService#getMineTaskInfo(java.lang.String)
     * 旧版接口
     */
    public ResultDto<Map> getMineTaskInfo(String userId) throws IOException, ParseException {
        if (memberService.selectById(userId) == null) {
            return ResultDto.error("126", "用户不存在!");
        }
        ScoreLog logData = scoreLogService.selectOne(new EntityWrapper<ScoreLog>().eq("member_id", userId)
                .eq("type", "LOGIN").orderBy("created_at", false).last("LIMIT 1"));
        // 可以放缓存中
        String extra = scoreRuleService.selectOne(new EntityWrapper<ScoreRule>().eq("code", "LOGIN")).getExtra();
        ObjectMapper mapper = new ObjectMapper();

        Integer exp = 0;
        boolean isChecked = false;
        if (logData == null) {// 没有记录
            String first = mapper.readTree(extra).get("first").toString();
            exp = Integer.parseInt(first);
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date recordData = format.parse(format.format(logData.getCreatedAt()));
            Date nowData = format.parse(format.format(new Date()));
            Long time = (long) (24 * 60 * 60 * 1000); // 一天
            Long interval = (nowData.getTime() - recordData.getTime()) / time;
            if (interval == 0) { // 今日已经登陆过,则
                isChecked = true;
                exp = logData.getScore();
            } else if (interval == 1) { // 连续登陆
                String again = mapper.readTree(extra).get("again").toString();
                exp = Integer.parseInt(again.toString());
            } else {
                String refresh = mapper.readTree(extra).get("refresh").toString();
                exp = Integer.parseInt(refresh.toString());
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("daily_check_exp", exp);
        data.put("is_check", isChecked);
        return ResultDto.success(data);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public ResultDto<List> getTaskCategory() {
        List<ScoreGroup> taskList = scoreGroupService.selectList(new EntityWrapper<ScoreGroup>());
        if (taskList == null || taskList.size() == 0) {
            return ResultDto.error("281", "找不到分类数据");
        }
        List<Object> data = new ArrayList<>();
        for (ScoreGroup scoreGroup : taskList) {
            Map<String, String> map = new HashMap<>();
            map.put("objectId", scoreGroup.getId());
            map.put("name", scoreGroup.getName());
            data.add(map);
        }
        return ResultDto.success(data);
    }

    /**
     * 旧版接口,已弃用
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public ResultDto<List> getTaskList(String userId, String categoryId) throws Exception {
        // 获取分类id
        // 用户
        if (memberService.selectById(userId) == null) {
            return ResultDto.error("126", "找不到当前用户");
        }
        Map<String, Object> scoreMap = new HashMap<>();
        // promise.resolve
        //
        // 查询日志
        List<ScoreLog> logList = scoreLogService
                .selectList(new EntityWrapper<ScoreLog>().eq("member_id", userId).gt("created_at", new Date()));
        // 遍历，map操作
        for (ScoreLog log : logList) {
            String code = log.getType();
            Integer score = log.getScore();
            if (scoreMap.get(code) != null) {
                scoreMap.put(code, (int) scoreMap.get(code) + score);
            } else {
                scoreMap.put(code, score);
            }
        }
        // 查询最近登陆任务日志
        List<ScoreLog> lastLogList = scoreLogService.selectList(new EntityWrapper<ScoreLog>().eq("member_id", userId)
                .eq("type", "LOGIN").orderBy("created_at", false).last("LIMIT 1"));

        ScoreRule logData = scoreRuleService.selectOne(new EntityWrapper<ScoreRule>().eq("code", "LOGIN"));
        String extra = logData.getExtra();
        ObjectMapper mapper = new ObjectMapper();
        int exp = 0;
        boolean isChecked = false;
        int days = 0;
        Long interval = (long) -1;
        if (lastLogList == null || lastLogList.size() == 0) {// 没有登陆记录
            String first = mapper.readTree(extra).get("first").toString();
            exp = Integer.parseInt(first);
        } else {
            ScoreLog lastLog = lastLogList.get(0);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date recordData = format.parse(format.format(lastLog.getCreatedAt()));
            Date nowData = format.parse(format.format(new Date()));
            Long time = (long) (24 * 60 * 60 * 1000); // 一天
            interval = (nowData.getTime() - recordData.getTime()) / time;
            int logDay = Integer.parseInt(mapper.readTree(lastLog.getInfo()).get("days").toString());
            if (interval == 0) { // 今日已经登陆过,则
                isChecked = true;
                days = logDay;
                exp = lastLog.getScore();
            } else if (interval == 1) { // 连续登陆
                String again = mapper.readTree(extra).get("again").toString();
                exp = Integer.parseInt(again.toString());
                days = logDay;
            } else {
                String refresh = mapper.readTree(extra).get("refresh").toString();
                exp = Integer.parseInt(refresh.toString());
            }
        }
        Map<String, Object> loginMap = new HashMap<>();
        loginMap.put("score", exp);
        loginMap.put("days", days);
        loginMap.put("is_check", isChecked);
        scoreMap.put("LOGIN", loginMap);

        // 分类id存在 isactive true
        Wrapper<ScoreRule> ruleWrapper = new EntityWrapper<ScoreRule>();
        if (categoryId != null) {
            ruleWrapper = ruleWrapper.eq("group_id", categoryId);
        }
        List<ScoreRule> taskList = scoreRuleService.selectList(ruleWrapper.eq("is_active", true));

        // 遍历，封装
        List resultList = new ArrayList<>();
        for (ScoreRule scoreRule : taskList) {
            Map<String, Object> taskInfo = new HashMap<>();
            String code = scoreRule.getCode();
            Integer codeScore = 0;
            if (!"LOGIN".equals(code)) {
                //codeScore = (Integer) scoreMap.get(scoreRule.getScore());
                codeScore = scoreRule.getScore();
            }
            taskInfo.put("ObjectId", scoreRule.getId());
            taskInfo.put("name", scoreRule.getName());
            taskInfo.put("intro", scoreRule.getIntro());
            taskInfo.put("exp_max", scoreRule.getMax());
            //taskInfo.put("code", code.equals("LOGIN") ? code : "undefined");
            taskInfo.put("code", code);
            //设置时间 今日零点
            //查询今天完成了几次任务
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date todayDate = format.parse(format.format(new Date()));
            //查出今天的目标任务日志个数
            int count = scoreLogService.selectCount(new EntityWrapper<ScoreLog>().eq("member_id", userId).eq("type", code).ge("created_at", todayDate));
            taskInfo.put("exp_got", codeScore != null ? codeScore * count : 0);
            if (code.equals("LOGIN")) {
                Map login = (Map) scoreMap.get("LOGIN");
                StringBuffer buffer = new StringBuffer();
                taskInfo.put("intro", buffer.append("经验+").append(login.get("score")).append("已连续")
                        .append(login.get("days")).append("天"));
                taskInfo.put("is_done", login.get("is_check"));
                taskInfo.put("exp_got", interval == 0 ? login.get("score") : 0);
                taskInfo.put("exp_max", login.get("score"));
            }
            resultList.add(taskInfo);
        }
        return ResultDto.success(resultList);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    @Transactional
    public ResultDto checkIn(String userId) throws Exception {

        Member user = memberService.selectById(userId);
        if (user == null) {
            return ResultDto.error("126", "用户不存在");
        }
        ResultDto<Map> loginResult = doLoginTask(userId, user);
        if (!loginResult.isSuccess()) {
            return loginResult;
        }
        Map data = loginResult.getData();

        data.put("level", user.getLevel());

        return ResultDto.success(data);
    }

    /**
     * 登录规则
     * @param userId
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    @Transactional
    public ResultDto<Map> doLoginTask(String userId, Member user) throws Exception {
        if (user == null) {
            return ResultDto.error("126", "用户不存在");
        }
        ScoreRule sr = scoreRuleService.selectOne(new EntityWrapper<ScoreRule>().eq("code", "LOGIN"));

        //fun币
        int funCoin = 1;
        int days = 1;
        List<ScoreLog> scoreLogList = scoreLogService.selectList(new EntityWrapper<ScoreLog>().eq("member_id", userId)
                .eq("type", "LOGIN").orderBy("created_at", false).last("LIMIT 1"));

        ObjectMapper mapper = new ObjectMapper();

        if (scoreLogList == null || scoreLogList.size() == 0) {// 没有记录

        } else {
            ScoreLog scoreLog = scoreLogList.get(0);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date recordData = format.parse(format.format(scoreLog.getCreatedAt()));
            Date nowData = format.parse(format.format(new Date()));
            Long time = (long) (24 * 60 * 60 * 1000); // 一天
            Long interval = (nowData.getTime() - recordData.getTime()) / time;

            //System.out.println(interval);

            if (interval == 0) { // 今日已经登陆过,则
                return ResultDto.error("99", "今日已签到");
            } else if (interval == 1) { // 连续登陆

                days = Integer.parseInt(mapper.readTree(scoreLog.getInfo()).get("days").toString()) + 1;
                int effectiveDays = days % 30;
                if (effectiveDays > 0) {
                    funCoin = addFunCoinNumber(effectiveDays);
                } else {
                    funCoin = addFunCoinNumber(30);
                }
            } else if (interval > 1) {//断签

                int effectiveDays = 1;
                funCoin = addFunCoinNumber(effectiveDays);
            } else {
                return ResultDto.error("-1", "日期错误");
            }
        }


        //fun币 .eq("account_group_id", 3)
        IncentAccountCoin accountCoin = accountCoinService.selectOne(new EntityWrapper<IncentAccountCoin>().eq("mb_id", userId));
        if (accountCoin == null) {
            accountCoin = IAccountDaoService.createAccountCoin(userId);
        }
        accountCoin.setCoinUsable(accountCoin.getCoinUsable().add(new BigDecimal(funCoin)));
        accountCoin.setUpdatedAt(new Date());
        accountCoin.updateById();

        // 日志记录
        ScoreLog newLog = new ScoreLog();
        newLog.setMemberId(userId);
        newLog.setType("LOGIN");
        newLog.setScore(funCoin);
        newLog.setInfo("{\"days\":" + days + "}");
        newLog.setCreatedAt(new Date());
        newLog.setUpdatedAt(new Date());
        newLog.setTaskType(sr.getTaskType());
        newLog.setCodeIdt(sr.getCodeIdt());
        newLog.setRuleId(sr.getId());
        newLog.setGroupId(sr.getGroupId());
        newLog.setRuleName(sr.getName());
        scoreLogService.insert(newLog);

        int times = scoreLogService.updateTasked(userId, sr, mapper);
        //获取荣誉
        if (times == 7) {
            //汇总 日志
            updateRanked(userId, mapper, 34);
        } else if (times == 30) {
            updateRanked(userId, mapper, 35);
        } else if (times == 100) {
            updateRanked(userId, mapper, 36);
        }

        Map<String, Integer> result = new HashMap<String, Integer>();
        result.put("days", days);
        result.put("score", 0);
        result.put("funCoin", funCoin);

        return ResultDto.success(result);

    }

    //更新荣誉汇总
    private void updateRanked(String userId, ObjectMapper mapper, int idt)
            throws JsonProcessingException, IOException, JsonParseException, JsonMappingException {
        IncentRuleRank rankRule = ruleRankService.selectOne(new EntityWrapper<IncentRuleRank>().eq("rank_idt", idt));
        IncentRanked incentRanked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", userId).eq("rank_type", rankRule.getRankType()));


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String datos = format.format(new Date());
        if (incentRanked == null) {
            incentRanked = new IncentRanked();
            incentRanked.setMbId(userId);
            List<Map<String, Object>> mapList = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("1", rankRule.getId());
            map.put("2", rankRule.getRankName());
            map.put("3", rankRule.getRankType());
            map.put("4", datos);
            mapList.add(map);
            incentRanked.setRankIdtIds(mapper.writeValueAsString(mapList));
            incentRanked.setCurrentRankId(rankRule.getId());
            incentRanked.setCurrentRankName(rankRule.getRankName());

            incentRanked.setRankType(rankRule.getRankType());

            incentRanked.setCreatedAt(new Date());
            incentRanked.setUpdatedAt(new Date());
            incentRanked.setId(PKUtil.getInstance().longPK());
            incentRanked.insert();
            addRankLog(userId, rankRule);
        } else {
            String rankIdtIds = incentRanked.getRankIdtIds();
            if (rankIdtIds != null) {
                ArrayList<Map<String, Object>> mapList = mapper.readValue(rankIdtIds, ArrayList.class);
                boolean add = true;
                for (Map<String, Object> m : mapList) {
                    if (Long.parseLong(m.get("1") + "") == rankRule.getId()) {
                        add = false;
                        break;
                    }
                }
                if (add) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("1", rankRule.getId());
                    m.put("2", rankRule.getRankName());
                    m.put("3", rankRule.getRankType());
                    m.put("4", datos);
                    mapList.add(m);
                    incentRanked.setRankIdtIds(mapper.writeValueAsString(mapList));
                    incentRanked.setCurrentRankId(rankRule.getId());
                    incentRanked.setCurrentRankName(rankRule.getRankName());

                    //fix bug:在具体荣誉规则数据不维护荣誉的flag数据，通过荣誉分类表维护 [by mxf 2018-12-22]
                    //incentRanked.setRankType(rankRule.getRankFlag());

                    incentRanked.setRankType(rankRule.getRankType());
                    incentRanked.setUpdatedAt(new Date());
                    incentRanked.updateById();
                    addRankLog(userId, rankRule);
                }
            }

            //日志
        }
    }

    private void addRankLog(String userId, IncentRuleRank rankRule) {
        IncentRankedLog rankLog = new IncentRankedLog();
        rankLog.setMbId(userId);
        rankLog.setGainTime(new Date());
        rankLog.setProduceType(1);
        rankLog.setRankCode(rankRule.getRankCode());
        rankLog.setRankGroupId(Long.parseLong(rankRule.getRankGroupId()));
        rankLog.setRankIdt(rankRule.getRankIdt());
        rankLog.setRankRuleId(rankRule.getId());
        rankLog.setId(PKUtil.getInstance().longPK());
        rankLog.insert();
    }

    /**
     * 2.4.3签到信息
     */
    @Override
    public ResultDto<Map<String, Object>> checkInfo(String userId) throws Exception {
        //连续签到天数  追加文案
        ScoreLog logData = scoreLogService.selectOne(new EntityWrapper<ScoreLog>().eq("member_id", userId)
                .eq("type", "LOGIN").orderBy("created_at", false).last("LIMIT 1"));

        ObjectMapper mapper = new ObjectMapper();

        Integer exp = 0;
        boolean isChecked = false;
        int days = 0;
        if (logData == null) {// 没有记录

        } else {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date recordData = format.parse(format.format(logData.getCreatedAt()));
            Date nowData = format.parse(format.format(new Date()));
            Long time = (long) (24 * 60 * 60 * 1000); // 一天
            Long interval = (nowData.getTime() - recordData.getTime()) / time;

            if (interval == 0) { // 今日已经登陆过,则
                isChecked = true;
                days = Integer.parseInt(mapper.readTree(logData.getInfo()).get("days").toString()) % 30;
            } else if (interval == 1) { // 连续登陆
                days = Integer.parseInt(mapper.readTree(logData.getInfo()).get("days").toString()) % 30;
            } else {//断开连续登陆
            }
        }

        Map<String, Object> data = new HashMap<>();
        //7 14 30
        if (days < 7) {
            data.put("tip", "再签" + (7 - days) + "天可得到10Fun的币奖励哦!");
        } else if (days < 14) {
            data.put("tip", "再签" + (14 - days) + "天可得到20Fun的币奖励哦!");
        } else if (days < 30) {
            data.put("tip", "再签" + (30 - days) + "天可得到50Fun的币奖励哦!");
        }


//		data.put("daily_check_exp", exp);
        data.put("is_check", isChecked);
        data.put("continue_days", days);

        return ResultDto.success(data);

    }

    public int addFunCoinNumber(int addDays) {
        ScoreRule rule = scoreRuleService.selectOne(new EntityWrapper<ScoreRule>().eq("code_idt", getTaskIdt(addDays)).eq("task_type", 22));
        if (rule != null) {
            return rule.getScore();
        } else {
            return 0;
        }

    }

    private int getTaskIdt(int addDays) {

        if (1 <= addDays && addDays <= 6) {
            return FunGoTaskV243Enum.CHECK_IN_ONE_TO_SEX_COIN.code();

        } else if (addDays == 7) {
            return FunGoTaskV243Enum.CHECK_IN_SEVEN_COIN.code();

        } else if (8 <= addDays && addDays <= 13) {
            return FunGoTaskV243Enum.CHECK_IN_EIGHT_TO_THIRTEEN_COIN.code();

        } else if (addDays == 14) {
            return FunGoTaskV243Enum.CHECK_IN_FOURTEEN_COIN.code();

        } else if (15 <= addDays && addDays <= 29) {
            return FunGoTaskV243Enum.CHECK_IN_FIFTEEN_TO_TWENTYNINE_COIN.code();

        } else if (addDays == 30) {
            return FunGoTaskV243Enum.CHECK_IN_THIRTY_COIN.code();

        }

        return 0;
    }


    @Override
    public ResultDto<String> DailyMotto(String userId) throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String relDate = formatter.format(new Date());

        ResultDto<String> re = new ResultDto<String>();
        Banner banner = null;

        //from redis cache
        banner = (Banner) fungoCacheTask.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_TASK_RANK_SIGNINMOTTO, relDate);
        if (null != banner) {
            re.setData(banner.getIntro());
            re.setMessage(banner.getIntro());
            return re;
        }

        //获取当日格言
        banner = bannerDao.getDailyMotto(relDate);

        if (banner != null) {

            re.setData(banner.getIntro());
            re.setMessage(banner.getIntro());

            //redis cache
            fungoCacheTask.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_TASK_RANK_SIGNINMOTTO,
                    relDate, banner, FungoCacheTask.REDIS_EXPIRE_12_HOUR);
        } else {
            re.setData("真理惟一可靠的标准就是永远自相符合。 —— 欧文");
            re.setMessage("真理惟一可靠的标准就是永远自相符合。 —— 欧文");
        }

        return re;
    }


    private StringBuffer getNearDateByUserSign(int userSignDate){
        List<Integer>  twoDaysList = Arrays.asList( 0,1 );
        List<Integer>  sevenDaysList = Arrays.asList( 2,3,4,5,6 );
        List<Integer>  fourthDaysList = Arrays.asList( 7,8,9,10,11,12,13 );
        List<Integer>  twentyOneDaysList = Arrays.asList( 14,15,16,17,18,19,20 );
        List<Integer>  twenthSevenDaysList = Arrays.asList( 21,22,23,24,25,26,27 );
        List<Integer>  twenthEightDaysList = Arrays.asList( 28,29 );
//        List<Integer>  thirtyDaysList = Arrays.asList( 30 );
        int nextSignDays = 0;
        StringBuffer stringBuffer = new StringBuffer( "再签到天可额外获得Fun币奖励" );
        if( twoDaysList.contains( userSignDate ) ){
            nextSignDays = twoDaysList.size() - userSignDate;
            stringBuffer = stringBuffer.insert(3,nextSignDays);
            stringBuffer = stringBuffer.insert(stringBuffer.length()-8,15);
        }else if(sevenDaysList.contains( userSignDate )){
            nextSignDays = sevenDaysList.size() - userSignDate;
            stringBuffer = stringBuffer.insert(3,nextSignDays);
            stringBuffer = stringBuffer.insert(stringBuffer.length()-8,20);
        }else if(fourthDaysList.contains( userSignDate )){
            nextSignDays = fourthDaysList.size() - userSignDate;
            stringBuffer = stringBuffer.insert(3,nextSignDays);
            stringBuffer = stringBuffer.insert(stringBuffer.length()-8,30);
        }else if(twentyOneDaysList.contains( userSignDate )){
            nextSignDays = twentyOneDaysList.size() - userSignDate;
            stringBuffer = stringBuffer.insert(3,nextSignDays);
            stringBuffer = stringBuffer.insert(stringBuffer.length()-8,35);
        }else if(twenthSevenDaysList.contains( userSignDate )){
            nextSignDays = twenthSevenDaysList.size() - userSignDate;
            stringBuffer = stringBuffer.insert(3,nextSignDays);
            stringBuffer = stringBuffer.insert(stringBuffer.length()-8,50);
        }else if(twenthEightDaysList.contains( userSignDate )){
            nextSignDays = twenthEightDaysList.size() - userSignDate;
            stringBuffer = stringBuffer.insert(3,nextSignDays);
            stringBuffer = stringBuffer.insert(stringBuffer.length()-8,150);
        }else{
            nextSignDays =  userSignDate - 30;
            stringBuffer = getNearDateByUserSign(nextSignDays);
        }
        return stringBuffer;
    }

    //-------
}

