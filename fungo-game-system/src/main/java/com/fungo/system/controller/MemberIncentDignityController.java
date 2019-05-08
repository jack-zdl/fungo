package com.fungo.system.controller;

import com.fungo.system.service.IMemberIncentDignityService;
import com.game.common.dto.MemberUserProfile;
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
import java.util.Map;

/**
 * <p>
 * 用户身份controller
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */

@Api(value = "", description = "用户激励")
@RestController
public class MemberIncentDignityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberIncentDignityController.class);

    @Autowired
    private IMemberIncentDignityService iMemberIncentDignityService;


    @ApiOperation(value = "获取用户身份(v2.4.3)", notes = "")
    @RequestMapping(value = "/api/user/incents/spirit/dignity", method = RequestMethod.GET)
    @ApiImplicitParams({})
    public ResultDto<Map<String, Object>> UserDignity(MemberUserProfile memberUserPrefile) {

        LOGGER.info("call /api/user/incents/spirit/dignity");

        ResultDto<Map<String, Object>> resultDto = new ResultDto<Map<String, Object>>();

        Map<String, Object> dignityMap = iMemberIncentDignityService.getMemberIncentDignity(memberUserPrefile.getLoginId());
        if (null != dignityMap && !dignityMap.isEmpty()) {
            resultDto.setData(dignityMap);
        } else {
            return ResultDto.error("-1", "该用户暂无身份信息");
        }

        return resultDto;
    }

}
