package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.system.dao.IncentAccountScoreDao;
import com.fungo.system.entity.IncentAccountCoin;
import com.fungo.system.entity.IncentAccountScore;
import com.fungo.system.entity.Member;
import com.fungo.system.service.IMemberAccountScoreDaoService;
import com.fungo.system.service.IncentAccountCoinDaoService;
import com.game.common.util.CommonUtils;
import com.game.common.util.PKUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;


@Service
public class MemberAccountDaoServiceImpl extends ServiceImpl<IncentAccountScoreDao, IncentAccountScore> implements IMemberAccountScoreDaoService {


    @Autowired
    private IMemberAccountScoreDaoService accountScoreService;

    @Value("${sys.config.fungo.cluster.index}")
    private String clusterIndex;


    @Override
    public IncentAccountCoin createAccountCoin(String mb_id) {

        int clusterIndex_i = Integer.parseInt(clusterIndex);

        IncentAccountCoin accountCoin = new IncentAccountCoin();

        accountCoin.setId(PKUtil.getInstance(clusterIndex_i).longPK());

        String phoneNum = "";

        if (StringUtils.isNotBlank(phoneNum)) {
            accountCoin.setAccountCode("C68" + phoneNum);
        } else {
            accountCoin.setAccountCode("C68" + CommonUtils.getRandomDigit(18));
        }

        accountCoin.setAccountGroupId((long) 3);
        accountCoin.setMbId(mb_id);
        accountCoin.setCoinUsable(BigDecimal.ZERO);
        accountCoin.setCoinFreeze(BigDecimal.ZERO);
        accountCoin.setClearZeroPeriod(-1);
        accountCoin.setIsActivate(1);
        accountCoin.setIsFreeze(2);
        accountCoin.insert();
        accountCoin.setCreatedAt(new Date());
        accountCoin.setUpdatedAt(new Date());

        return accountCoin;
    }


    @Override
    public IncentAccountScore createAccountScore(Member member, int accountType) {

        //获取用户id
        String mb_id = member.getId();

        //用户手机号
        String mbPhone = member.getMobilePhoneNum();

        //用户经验值
        Integer exp = member.getExp();

        String user_name = member.getUserName();

        int clusterIndex_i = Integer.parseInt(clusterIndex);
        IncentAccountScore accountScore = new IncentAccountScore();
        accountScore.setId(PKUtil.getInstance(clusterIndex_i).longPK());
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

        accountScore.setScoreType(accountType);
        accountScore.setExt1("");
        accountScore.setExt2("");
        accountScore.setExt3("");

        accountScore.setCreatedAt(new Date());
        accountScore.setUpdatedAt(new Date());

        accountScore.insert();

        return accountScore;
    }

    //-------------
}
