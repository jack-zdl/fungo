package com.fungo.system.controller;


import com.fungo.system.service.IMemberIncentRuleRankService;
import com.game.common.consts.FunGoGameConsts;
import com.game.common.dto.ResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * <p>
 *      用户荣誉规则controller
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
@Api(value = "", description = "用户激励")
@RestController
public class MemberIncentRuleRankController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberIncentRuleRankController.class);

    @Autowired
    private IMemberIncentRuleRankService iMemberIncentRuleRankService;


    @ApiOperation(value = "身份规则(v2.4.3)", notes = "")
    @RequestMapping(value = "/api/user/incents/rule/dignities", method = RequestMethod.GET)
    @ApiImplicitParams({})
    public ResultDto<List<Map<String, Object>>> dignityRules() {

        LOGGER.info("call /api/user/incents/rule/dignities");

        //查看评测身份
        Long rankGroupId = 0L;
        int rankType = FunGoGameConsts.INCENT_RULE_RANK_FLAG_HONORS_EVALUATIONER;
        int rankFlag = FunGoGameConsts.INCENT_RULE_RANK_FLAG_HONORS_NULL;


        ResultDto<List<Map<String, Object>>> resultDto = new ResultDto<List<Map<String, Object>>>();
        List<Map<String, Object>> resutList = iMemberIncentRuleRankService.getIncentRule(rankGroupId, rankType, rankFlag, true);
        if (null != resutList) {
            resultDto.setData(resutList);
        } else {
            return ResultDto.error("-1", "获取权益规则出现异常");
        }
        return resultDto;
    }


    @ApiOperation(value = "荣誉规则(v2.4.3)", notes = "")
    @RequestMapping(value = "/api/user/incents/rule/honors", method = RequestMethod.GET)
    @ApiImplicitParams({})
    public ResultDto<List<Map<String, Object>>> getHonorRules() {

        LOGGER.info("call /api/user/incents/rule/honors");

        //查看荣誉规则
        Long rankGroupId = 0L;
        int rankType = FunGoGameConsts.INCENT_RULE_RANK_TYPE_HONORS;
        int rankFlag = FunGoGameConsts.INCENT_RULE_RANK_FLAG_HONORS_NULL;


        ResultDto<List<Map<String, Object>>> resultDto = new ResultDto<List<Map<String, Object>>>();
        List<Map<String, Object>> resutList = iMemberIncentRuleRankService.getIncentRule(rankGroupId, rankType, rankFlag, true);
        if (null != resutList) {
            resultDto.setData(resutList);
        } else {
            return ResultDto.error("-1", "获取荣誉规则出现异常");
        }
        return resultDto;
    }


    //----------
}
