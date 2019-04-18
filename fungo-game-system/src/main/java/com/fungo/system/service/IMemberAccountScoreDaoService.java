package com.fungo.system.service;


import com.baomidou.mybatisplus.service.IService;
import com.fungo.system.entity.IncentAccountCoin;
import com.fungo.system.entity.IncentAccountScore;
import com.fungo.system.entity.Member;

/**
 * <p>
 *
 *   用户账户类Dao接口
 *   账户号:
 *          经验值： E98 开头 +  用户手机号 | 18位随机数
 *          积分：  I88 开头 +  用户手机号  | 18位随机数
 *          虚拟币: C68 开头 +  用户手机号 | 18位随机数
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public interface IMemberAccountScoreDaoService extends IService<IncentAccountScore> {

    /**
     * 新建用户funbi账户
     * @param member
     * @return
     */
    public IncentAccountCoin createAccountCoin(Member member);


    /**
     * 新建用户fun积分账户
     * @param member
     * @param accountType
     * @return
     */
    public IncentAccountScore createAccountScore(Member member, int accountType);


}
