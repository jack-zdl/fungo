package com.fungo.system.controller;

import com.fungo.system.service.IMemberIncentHonorService;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.util.annotation.MD5ParanCheck;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * <p>
 *      用户荣誉controller
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
@Api(value = "", description = "用户激励")
@RestController
public class MemberIncentHonorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberIncentHonorController.class);

    @Autowired
    private IMemberIncentHonorService iMemberIncentHonorService;

    @ApiOperation(value = "获取用户荣誉(v2.4.3)", notes = "")
    @RequestMapping(value = "/api/user/incents/spirit/honors", method = RequestMethod.GET)
    @ApiImplicitParams({})
    @MD5ParanCheck()
    public ResultDto<List<Map<String, Object>>> getUserHonors(MemberUserProfile memberUserPrefile) {
        ResultDto<List<Map<String, Object>>> resultDto = new ResultDto<List<Map<String, Object>>>();
        List<Map<String, Object>> honorList = iMemberIncentHonorService.getMemberIncentHonor(memberUserPrefile.getLoginId());
        if (null != honorList) {
            resultDto.setData(honorList);
        } else {
            return ResultDto.error("-1", "该用户暂无荣誉数据");
        }
        return resultDto;
    }

    @ApiOperation(value = "获取其它用户荣誉(v2.4.3)", notes = "")
    @RequestMapping(value = "/api/user/incents/spirit/userhonors/{userId}", method = RequestMethod.GET)
    @ApiImplicitParams({})
    @MD5ParanCheck()
    public ResultDto<List<Map<String, Object>>> getHonors(MemberUserProfile memberUserPrefile,@PathVariable("userId") String userId) {
        ResultDto<List<Map<String, Object>>> resultDto = new ResultDto<List<Map<String, Object>>>();
        List<Map<String, Object>> honorList = iMemberIncentHonorService.getMemberIncentHonor(userId);
        if (null != honorList) {
            resultDto.setData(honorList);
        } else {
            return ResultDto.error("-1", "该用户暂无荣誉数据");
        }
        return resultDto;
    }
}
