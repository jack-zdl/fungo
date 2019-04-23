package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.dto.AccountCoinOutBean;
import com.fungo.system.dto.AccountRecordInput;
import com.fungo.system.dto.AccountScoreOutBean;
import com.fungo.system.dto.CoinBean;
import com.fungo.system.entity.IncentAccountCoin;
import com.fungo.system.entity.IncentAccountScore;
import com.fungo.system.entity.Member;
import com.fungo.system.entity.ScoreLog;
import com.fungo.system.service.*;
import com.game.common.consts.FunGoGameConsts;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.repo.cache.facade.FungoCacheTask;
import com.game.common.util.CommonUtils;
import com.game.common.util.PageTools;
import com.game.common.util.date.DateTools;
import com.game.common.util.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberIncentAccountServiceImpl implements IMemberIncentAccountService {


    private static final Logger LOGGER = LoggerFactory.getLogger(MemberIncentAccountServiceImpl.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private IncentAccountScoreService incentAccountScoreService;

    @Autowired
    private IncentAccountCoinDaoService incentAccountCoinService;

    @Autowired
    private ScoreLogService scoreLogService;

    @Autowired
    private FungoCacheTask fungoCacheTask;

    @Override
    public void transfMemberExp() throws BusinessException {

        try {

            LOGGER.info("------------迁移用户经验值数据开始......");


            List<Member> memberList = memberService.selectList(new EntityWrapper<Member>());
            if (null != memberList && !memberList.isEmpty()) {

                for (int i = 0; i < memberList.size(); i++) {

                    Member member = memberList.get(i);

                    //获取用户id
                    String mb_id = member.getId();

                    //用户手机号
                    String mbPhone = member.getMobilePhoneNum();

                    //用户经验值
                    Integer exp = member.getExp();

                    String user_name = member.getUserName();


                    IncentAccountScore accountScore = new IncentAccountScore();
                    accountScore.setId(Long.parseLong(CommonUtils.getRandomDigit(18)));
                    accountScore.setAccountGroupId(1L);
                    String account_ode = "";
                    if (StringUtils.isNotBlank(mbPhone)) {
                        account_ode = "E98" + mbPhone;
                    } else {
                        account_ode = "E98" + CommonUtils.getRandomDigit(18);
                    }
                    accountScore.setAccountCode(account_ode);
                    accountScore.setAccountName(user_name + "经验值账户");
                    accountScore.setMbId(mb_id);
                    accountScore.setMbUserName(user_name);


                    BigDecimal expBD = new BigDecimal(exp);
                    expBD.setScale(4, RoundingMode.HALF_UP);
                    accountScore.setScoreUsable(expBD);

                    accountScore.setScoreFreeze(BigDecimal.ZERO);
                    accountScore.setIsActivate(2);
                    accountScore.setPayPwd("");
                    accountScore.setResetPayPwdPhone("");

                    accountScore.setClearZeroPeriod(-1);
                    accountScore.setClearZeroTime(null);
                    accountScore.setDisabledTime(null);

                    accountScore.setScoreType(1);
                    accountScore.setExt1("");
                    accountScore.setExt2("");
                    accountScore.setExt3("");


                    incentAccountScoreService.insert(accountScore);
                }
            }

            LOGGER.info("------------迁移用户经验值数据结束......");

        } catch (BusinessException ex) {
            ex.printStackTrace();
        }


    }


    @Cacheable(cacheNames = {FunGoGameConsts.CACHE_EH_NAME}, key = "'" + FunGoGameConsts.CACHE_EH_KEY_PRE_MEMBER + "_ExpAccount' + #memberId")
    @Override
    public AccountScoreOutBean getExpAccountOfMember(String memberId) throws BusinessException {
        try {

            if (StringUtils.isBlank(memberId)) {
                return null;
            }
            LOGGER.info(" 获取用户的经验值账户数据-入参memberId：{}", memberId);

            //获取某个会员的最高身份成就数据
            //封装查询规则的条件
            Wrapper wrapperCriteria = new EntityWrapper<IncentAccountScore>();

            Map<String, Object> criteriaMap = new HashMap<String, Object>();
            criteriaMap.put("mb_id", memberId);
            wrapperCriteria.allEq(criteriaMap);

            IncentAccountScore accountScore = incentAccountScoreService.selectOne(wrapperCriteria);
            if (null != accountScore) {
                AccountScoreOutBean accountScoreOutBean = new AccountScoreOutBean();
                BeanUtils.copyProperties(accountScore, accountScoreOutBean);

                LOGGER.info(" 获取用户的经验值账户数据-出参：{}", accountScoreOutBean.toString());

                return accountScoreOutBean;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    @Cacheable(cacheNames = {FunGoGameConsts.CACHE_EH_NAME}, key = "'" + FunGoGameConsts.CACHE_EH_KEY_PRE_MEMBER + "_CoinAccount' + #memberId")
    @Override
    public AccountCoinOutBean getCoinAccountOfMember(String memberId) throws BusinessException {

        try {

            if (StringUtils.isBlank(memberId)) {
                return null;
            }

            LOGGER.info(" 获取用户的虚拟币账户数据-入参memberId：{}", memberId);

            //获取某个会员的最高身份成就数据
            //封装查询规则的条件
            Wrapper wrapperCriteria = new EntityWrapper<IncentAccountCoin>();

            Map<String, Object> criteriaMap = new HashMap<String, Object>();
            criteriaMap.put("mb_id", memberId);
            wrapperCriteria.allEq(criteriaMap);

            IncentAccountCoin accountScore = incentAccountCoinService.selectOne(wrapperCriteria);
            if (null != accountScore) {
                AccountCoinOutBean accountCoinOutBean = new AccountCoinOutBean();
                BeanUtils.copyProperties(accountScore, accountCoinOutBean);

                LOGGER.info(" 获取用户的虚拟币账户数据-出参：{}", accountCoinOutBean.toString());

                return accountCoinOutBean;
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    @Override
    public FungoPageResultDto<CoinBean> getCoinAccount(String loginId, AccountRecordInput input) {

        FungoPageResultDto<CoinBean> re = null;

        //from redis
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INCENTS_FORTUNE_COIN_POST + loginId;
        String keySuffix = JSON.toJSONString(input);

        re = (FungoPageResultDto<CoinBean>) fungoCacheTask.getIndexCache(keyPrefix, keySuffix);
        if (null != re) {
            return re;
        }

        /**
         * produce_type
         * 3 用户消费 fungo币
         4 用户消费经验值
         如果是消费记录 task_type = -1
         */

        re = new FungoPageResultDto<CoinBean>();
        List<CoinBean> clist = new ArrayList<>();
        Page<ScoreLog> logPage = null;

        //账户类型 1:获取 2:消耗
        if (input.getType() == 1) {
            logPage = scoreLogService.selectPage(new Page<>(input.getPage(), input.getLimit()), new EntityWrapper<ScoreLog>().eq("member_id", loginId).in("task_type", new Integer[]{21, 22, 220, 23}).orderBy("created_at", false));

        } else if (input.getType() == 2) {

            logPage = scoreLogService.selectPage(new Page<>(input.getPage(), input.getLimit()), new EntityWrapper<ScoreLog>().eq("member_id", loginId).eq("produce_type", 3).orderBy("created_at", false));
        }

        if (logPage != null) {
            List<ScoreLog> slist = logPage.getRecords();
            for (ScoreLog log : slist) {
                CoinBean b = new CoinBean();
                b.setAction(log.getRuleName());
                b.setChangeTime(DateTools.fmtDate(log.getCreatedAt()));
                b.setCoinChange(log.getScore());
                clist.add(b);
            }

            PageTools.pageToResultDto(re, logPage);
            re.setData(clist);
        }

        //redis cache
        fungoCacheTask.excIndexCache(true, keyPrefix, keySuffix, re, FungoCacheTask.REDIS_EXPIRE_12_HOUR);
        return re;
    }

    //-----
}
