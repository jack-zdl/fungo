package com.fungo.system.controller;

import com.fungo.system.dto.MemberSNSBindInput;
import com.fungo.system.dto.MemberSNSBindOutput;
import com.fungo.system.service.IMemberSNSService;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.util.annotation.JsonView;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>
 *      用户SNS操作controller
 *      绑定
 *      解绑
 * </p>
 * since:V2.4.6
 * @author mxf
 * @since 2018-12-04
 */
@RestController
public class MemberSNSController {

    private static final Logger logger = LoggerFactory.getLogger(MemberSNSController.class);

    @Autowired
    private IMemberSNSService iMemberSNSService;

    /**
     * 绑定第三方SNS平台账号
     * 有效版本范围：V2.4.6 ~
     * @throws Exception
     */
    @ApiOperation(value = "登录用户绑定第三方账号", httpMethod = "POST")
    @ApiImplicitParams({})
    @RequestMapping(value = "/api/user/sns/bind", method = RequestMethod.POST)
    @JsonView
    public ResultDto<MemberSNSBindOutput> bindThirdSNSWithLogged(MemberUserProfile memberprofile, HttpServletRequest request,
                                                                 @Valid @RequestBody MemberSNSBindInput bindInput) throws Exception {
        String mb_id = memberprofile.getLoginId();
        String os = request.getHeader("os");
        bindInput.setOs(os);
        ResultDto<MemberSNSBindOutput> outputResultDto = iMemberSNSService.bindThirdSNSWithLogged(mb_id, bindInput);
        return outputResultDto;
    }
}
