package com.fungo.system.service;


import com.fungo.system.dto.AccountCoinOutBean;
import com.fungo.system.dto.AccountRecordInput;
import com.fungo.system.dto.AccountScoreOutBean;
import com.fungo.system.dto.CoinBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.util.exception.BusinessException;


/**
 * <p>
 *          用户账户类业务层接口定义
 *          账户号:
 *                   经验值： E98 开头 +  用户手机号 | 18位随机数
 *                   积分：  I88 开头 +  用户手机号  | 18位随机数
 *                   虚拟币: C68 开头 +  用户手机号 |  18位随机数
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public interface IMemberIncentAccountService {


    /**
     * V2.4.2版本及其之前版本用户经验值迁移到经验值账户
     *
     * @throws BusinessException
     */
    public void transfMemberExp() throws BusinessException;




    /**
     * 获取用户的经验值账户数据
     * @param memberId
     * @return
     * @throws BusinessException
     */
    public AccountScoreOutBean getExpAccountOfMember(String memberId) throws  BusinessException;




    /**
     * 获取用户的虚拟币账户数据
     * @param memberId
     * @return
     * @throws BusinessException
     */
    public AccountCoinOutBean getCoinAccountOfMember(String memberId) throws  BusinessException;


    /**
     * 获取用户fungo比账号获取|消费明细
     * @param loginId
     * @param input
     * @return
     */
	public FungoPageResultDto<CoinBean> getCoinAccount(String loginId, AccountRecordInput input);
    

}
