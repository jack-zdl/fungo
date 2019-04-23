package com.fungo.system.controller;

import com.fungo.system.dto.DignityPutInput;
import com.fungo.system.dto.HonorInfo;
import com.fungo.system.dto.IncentPutInput;
import com.fungo.system.dto.ScoreBean;
import com.fungo.system.service.IMemberIncentAccountService;
import com.fungo.system.service.IMemberIncentRiskService;
import com.fungo.system.service.IMemberIncentRuleRankService;
import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.util.date.DateTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(value = "", description = "用户激励")
@RestController
public class MemberIncentController {


    @Autowired
    private IMemberIncentAccountService incentAccountService;


    @Autowired
    private IMemberIncentRuleRankService iMemberIncentRuleRankService;


    @Autowired
    private IMemberIncentRiskService iMemberIncentRiskService;




    @ApiOperation(value = "增加身份规则(v2.4.3)", notes = "")
    @RequestMapping(value = "/api/user/incents/rule/dignity", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<String> addDignityRuler(MemberUserProfile memberUserPrefile, @RequestBody DignityPutInput input) {
        return ResultDto.success();
    }

    @ApiOperation(value = "修改身份规则(v2.4.3)", notes = "")
    @RequestMapping(value = "/api/user/incents/rule/dignity", method = RequestMethod.PUT)
    @ApiImplicitParams({})
    public ResultDto<String> updateDignityRuler(MemberUserProfile memberUserPrefile, @RequestBody DignityPutInput input) {
        return ResultDto.success();

    }

    @ApiOperation(value = "删除身份规则(v2.4.3)", notes = "")
    @RequestMapping(value = "/api/user/incents/rule/dignity/{dignityId}", method = RequestMethod.PUT)
    @ApiImplicitParams({})
    public ResultDto<String> delDignityRuler(MemberUserProfile memberUserPrefile, @PathVariable("dignityId") String dignityId) {
        return ResultDto.success();

    }





    @ApiOperation(value = "用户荣誉详情(v2.4.3)", notes = "")
    @RequestMapping(value = "/api/user/incents/spirit/honor/{honorId}", method = RequestMethod.GET)
    @ApiImplicitParams({})
    public FungoPageResultDto<HonorInfo> getUserHonorDetail(MemberUserProfile memberUserPrefile, @PathVariable("honorId") String honorId) {
        List<HonorInfo> sl = new ArrayList<>();
        HonorInfo honorInfo = new HonorInfo();
        honorInfo.setGainTime(DateTools.fmtDate(new Date()));
        honorInfo.setHonorId("dsfsfg");
        honorInfo.setHonorImg("sdfsgfg");
        honorInfo.setHonorIntro("介绍");
        honorInfo.setHonorName("娃哈哈");
        honorInfo.setHonorNum(333);
        honorInfo.setHonorState(1);
        honorInfo.setHonorType(1);
        sl.add(honorInfo);
        FungoPageResultDto<HonorInfo> re = new FungoPageResultDto<HonorInfo>();
        re.setData(sl);
        return re;

    }



//    @ApiOperation(value = "获取用户虚拟币账户(v2.4.3)", notes = "")
//    @RequestMapping(value = "/api/user/incents/fortune/coin", method = RequestMethod.POST)
//    @ApiImplicitParams({})
//    public FungoPageResultDto<CoinBean> getCoinAccount(MemberUserProfile memberUserPrefile, @RequestBody InputPageDto input) {
//        List<CoinBean> sl = new ArrayList<>();
//        CoinBean coinBean = new CoinBean();
//        coinBean.setAction("aaa");
//        coinBean.setChangeTime("ssss");
//        coinBean.setCoinChange("wahahaha");
//        sl.add(coinBean);
//        FungoPageResultDto<CoinBean> re = new FungoPageResultDto<CoinBean>();
//        re.setData(sl);
//        return re;
//
//    }

    @ApiOperation(value = "查询用户积分(v2.4.3)", notes = "")
    @RequestMapping(value = "/api/user/incents/fortune/bps", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<ScoreBean> getUserScoreAccount(MemberUserProfile memberUserPrefile, @RequestBody InputPageDto input) {
        List<ScoreBean> sl = new ArrayList<>();
        ScoreBean scoreBean = new ScoreBean();
        scoreBean.setAction("vbb");
        scoreBean.setChangeTime(DateTools.fmtDate(new Date()));
        scoreBean.setScoreChange("加积分");
        sl.add(scoreBean);
        FungoPageResultDto<ScoreBean> re = new FungoPageResultDto<ScoreBean>();
        re.setData(sl);
        return re;

    }

    @ApiOperation(value = "设置用户任务(v2.4.3)", notes = "")
    @RequestMapping(value = "/api/user/incents/spirit/task", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<String> setUserTask(MemberUserProfile memberUserPrefile, @RequestBody IncentPutInput input) {
        return ResultDto.success();

    }

    @ApiOperation(value = "修改用户等级(2.4.3)", notes = "")
    @RequestMapping(value = "/api/user/incents/spirit/rank", method = RequestMethod.PUT)
    @ApiImplicitParams({})
    public ResultDto<String> updateMemberLevel(MemberUserProfile memberUserPrefile, @RequestBody IncentPutInput input) {
        return ResultDto.success();

    }

    @ApiOperation(value = "修改用户荣誉(2.4.3)", notes = "")
    @RequestMapping(value = "/api/user/incents/spirit/honor", method = RequestMethod.PUT)
    @ApiImplicitParams({})
    public ResultDto<String> updateMemberHonor(MemberUserProfile memberUserPrefile, @RequestBody IncentPutInput input) {
        return ResultDto.success();

    }

    @ApiOperation(value = "修改用户身份(2.4.3)", notes = "")
    @RequestMapping(value = "/api/user/incents/spirit/dignity", method = RequestMethod.PUT)
    @ApiImplicitParams({})
    public ResultDto<String> updateMemberDignity(MemberUserProfile memberUserPrefile, @RequestBody IncentPutInput input) {
        return ResultDto.success();

    }

    @ApiOperation(value = "修改用户积分(2.4.3)", notes = "")
    @RequestMapping(value = "/api/user/incents/fortune/bps", method = RequestMethod.PUT)
    @ApiImplicitParams({})
    public ResultDto<String> updateMemberScore(MemberUserProfile memberUserPrefile, @RequestBody IncentPutInput input) {
        return ResultDto.success();

    }

    @ApiOperation(value = "修改用户虚拟币账户(2.4.3)", notes = "")
    @RequestMapping(value = "/api/user/incents/fortune/coin", method = RequestMethod.PUT)
    @ApiImplicitParams({})
    public ResultDto<String> updateMemberCoin(MemberUserProfile memberUserPrefile, @RequestBody IncentPutInput input) {
        return ResultDto.success();
    }



//----
}
