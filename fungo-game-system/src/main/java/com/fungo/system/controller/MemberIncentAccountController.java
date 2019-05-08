package com.fungo.system.controller;

import com.fungo.system.dto.AccountCoinOutBean;
import com.fungo.system.dto.AccountRecordInput;
import com.fungo.system.dto.AccountScoreOutBean;
import com.fungo.system.dto.CoinBean;
import com.fungo.system.service.IMemberIncentAccountService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户账户controller
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
@Api(value = "", description = "用户账户")
@RestController
public class MemberIncentAccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberIncentAccountController.class);

    @Autowired
    private IMemberIncentAccountService iMemberIncentAccountService;


    @ApiOperation(value = "获取用户经验值(v2.4.3)", notes = "")
    @RequestMapping(value = "/api/user/incents/fortune/exp", method = RequestMethod.GET)
    @ApiImplicitParams({})
    public ResultDto<AccountScoreOutBean> getExpAccountOfMember(MemberUserProfile memberUserPrefile) {

        LOGGER.info("call /api/user/incents/fortune/exp");
        ResultDto<AccountScoreOutBean> resultDto = new ResultDto<AccountScoreOutBean>();
        AccountScoreOutBean expAccountOfMember = iMemberIncentAccountService.getExpAccountOfMember(memberUserPrefile.getLoginId());
        if (null != expAccountOfMember) {
            resultDto.setData(expAccountOfMember);
        } else {
            return ResultDto.error("-1", "该用户暂无经验值数据");
        }
        return resultDto;
    }


    @ApiOperation(value = "获取用户虚拟币值(v2.4.3)", notes = "")
    @RequestMapping(value = "/api/user/incents/fortune/coin", method = RequestMethod.GET)
    @ApiImplicitParams({})
    public ResultDto<AccountCoinOutBean> getCoinAccountOfMember(MemberUserProfile memberUserPrefile) {

        LOGGER.info("call /api/user/incents/fortune/coin");

        ResultDto<AccountCoinOutBean> resultDto = new ResultDto<AccountCoinOutBean>();
        AccountCoinOutBean accountCoinOutBean = iMemberIncentAccountService.getCoinAccountOfMember(memberUserPrefile.getLoginId());
        if (null != accountCoinOutBean) {
            resultDto.setData(accountCoinOutBean);
        } else {
            return ResultDto.error("-1", "该用户暂无虚拟币数据");
        }
        return resultDto;
    }
    
    @ApiOperation(value = "获取用户虚拟币账户(v2.4.3)-获取用户fungo比账号获取|消费明细", notes = "")
    @RequestMapping(value = "/api/user/incents/fortune/coin", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<CoinBean> getCoinAccount(MemberUserProfile memberUserPrefile, @RequestBody AccountRecordInput input) {
        return iMemberIncentAccountService.getCoinAccount(memberUserPrefile.getLoginId(),input);

    }


}
