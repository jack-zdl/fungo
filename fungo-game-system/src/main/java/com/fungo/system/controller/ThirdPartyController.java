package com.fungo.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.dto.*;
import com.fungo.system.entity.LoginMemberBean;
import com.fungo.system.service.IThirdPartyService;
import com.fungo.system.service.IUserService;
import com.fungo.system.service.ThirdLoginService;
import com.fungo.system.service.impl.MemberIncentSignInTaskServiceImpl;
import com.fungo.system.service.impl.MemberSNSServiceImpl;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.util.annotation.Anonymous;
import com.game.common.util.token.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 第三方登录验证处理接口
 * currentVersion: V2.4.6
 * @author sam
 *
 */
@RestController
@Api(value = "", description = "第三方登录验证处理接口")
public class ThirdPartyController {

    private static final Logger logger = LoggerFactory.getLogger(MemberIncentSignInTaskServiceImpl.class);

    @Autowired
    IThirdPartyService thirdLoginService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ThirdLoginService tlService;
    @Autowired
    private MemberSNSServiceImpl memberSNSService;
    /**
     * 用户绑定第三方账号
     * @throws Exception
     */
    @ApiOperation(value = " 第三方登录", httpMethod = "POST")
    @ApiImplicitParams({})
    @RequestMapping(value = "/api/user/login/thirdparty", method = RequestMethod.POST)
    public ResultDto<LoginMemberBean> thirdUserBind(HttpServletRequest request, @RequestBody ThirdLoginInput input) throws Exception {
        String channel = "";
        channel = (String) request.getHeader("os");
        String appversion = request.getHeader("appversion");
        String deviceId = request.getHeader("deviceId");
        ResultDto<LoginMemberBean> re = thirdLoginService.thirdPartyLogin(input, channel, appversion,deviceId);
        if (re.isSuccess()) {
            LoginMemberBean bean = re.getData();
            MemberUserProfile userPrefile = new MemberUserProfile();
            userPrefile.setLoginId(bean.getObjectId());
            userPrefile.setName(bean.getUsername());
            bean.setToken(tokenService.createJWT("jwt", objectMapper.writeValueAsString(userPrefile), 1000 * 60 * 60 * 60 * 24 * 30));
        }
        return re;
    }

    /**
     * 绑定第三方绑定
     * 有效版本范围：V0.0.1 - V2.4.5
     * @throws Exception
     */
    @ApiOperation(value = "绑定第三方绑定", httpMethod = "POST")
    @ApiImplicitParams({})
    @RequestMapping(value = "/api/mine/thirdparty", method = RequestMethod.POST)
    public ResultDto<String> thirdUserUnbind(MemberUserProfile memberprofile, HttpServletRequest request, @RequestBody ThirdLoginInput thirdLoginInput) throws Exception {
        String os = "";
        ResultDto<String> re = ResultDto.success();
        try {
            os = (String) request.getAttribute("os");
            String userId = memberprofile.getLoginId();
            MemberSNSBindInput bindInput = new MemberSNSBindInput();
            String mb_id = memberprofile.getLoginId();
            if (StringUtils.isBlank(mb_id)) {
                return ResultDto.error("-1", "请先登录");
            }
            BeanUtils.copyProperties( bindInput,thirdLoginInput);
            bindInput.setOs(os);
            bindInput.setSnsType(thirdLoginInput.getPlatformType());
            ResultDto<MemberSNSBindOutput> outputResultDto = memberSNSService.bindThirdSNSWithLogged(userId, bindInput);
            if(outputResultDto.getData() != null && outputResultDto.getData().getBindState() != 1 ){
                re = ResultDto.error("0",outputResultDto.getMessage());
            }
        }catch (Exception e){
            logger.error("绑定第三方绑定异常",e);
            re = ResultDto.error("-1","绑定第三方绑定异常");
        }
        return re;
    }

    /**
     * 解除第三方绑定
     */
    @ApiOperation(value = "解除第三方绑定", httpMethod = "DELETE")
    @ApiImplicitParams({})
    @RequestMapping(value = "/api/mine/thirdparty", method = RequestMethod.DELETE)
    public ResultDto<String> thirdUserUnbind(MemberUserProfile memberprofile, @RequestBody ThirdLoginInput thirdLoginInput) {
        String userId = memberprofile.getLoginId();
        return thirdLoginService.thirdUserUnbind(userId, thirdLoginInput.getPlatformType());
    }

    /**
     * 第三方绑定手机号
     * @throws Exception
     * @throws
     */
    @ApiOperation(value = "第三方绑定手机号", httpMethod = "POST")
    @ApiImplicitParams({})
    @RequestMapping(value = "/api/user/mobile/thirdparty", method = RequestMethod.POST)
    public ResultDto<LoginMemberBean> thirdUserBindPhoneNo(HttpServletRequest request,@Anonymous MemberUserProfile memberUserPrefile, @RequestBody MsgInput msg) throws Exception {
        String mobile = msg.getMobile();
        String code = msg.getCode();
        String channel =  request.getHeader("os");
        String deviceId = request.getHeader("deviceId");
        ResultDto<LoginMemberBean> re = userService.bindingPhoneNo(code, mobile, msg.getToken(),channel,deviceId);
        if (re.isSuccess()) {
            LoginMemberBean bean = re.getData();
            MemberUserProfile userPrefile = new MemberUserProfile();
            userPrefile.setLoginId(bean.getObjectId());
            userPrefile.setName(bean.getUsername());
            bean.setToken(tokenService.createJWT("jwt", objectMapper.writeValueAsString(userPrefile), 1000 * 60 * 60 * 60 * 24 * 30));
        }
        return re;
    }

    /**
     * 第三方绑定信息
     */
    @ApiOperation(value = "第三方绑定信息", httpMethod = "GET")
    @ApiImplicitParams({})
    @RequestMapping(value = "/api/mine/thirdparty", method = RequestMethod.GET)
    public ResultDto<List<BindInfo>> thirdUserUnbind(MemberUserProfile memberprofile) {
        String userId = memberprofile.getLoginId();
        return thirdLoginService.thirdUserUnbind(userId);
    }

    @ApiOperation(value = "第三方登录(pc)", httpMethod = "POST")
    @ApiImplicitParams({})
    @RequestMapping(value = "/api/user/login/thirdpartypc", method = RequestMethod.POST)
    public ResultDto<LoginMemberBean> thirdUserLoginPc(HttpServletRequest request, @RequestBody ThirdLoginPcInput input) throws Exception {

        String host = request.getHeader("host");
        String appversion = request.getHeader("appversion");

        String code = input.getCode();
        int type = input.getPlatformType();
        ResultDto<ThirdLoginInput> thirdRe = null;
        if (type == 0) {
            thirdRe = tlService.sinaVerify(host, code);
        } else if (type == 1) {
            thirdRe = tlService.wxVerify(host, code);
        } else if (type == 4) {
            thirdRe = tlService.qqVerify(host, code);
        } else {
            return ResultDto.error("-1", "请求参数错误");
        }

        if (!thirdRe.isSuccess()) {
            return ResultDto.error("-1", thirdRe.getMessage());
        }

        ResultDto<LoginMemberBean> re = thirdLoginService.thirdPartyLogin(thirdRe.getData(), "pc", appversion,null);
        if (re.isSuccess()) {
            LoginMemberBean bean = re.getData();
            MemberUserProfile userPrefile = new MemberUserProfile();
            userPrefile.setLoginId(bean.getObjectId());
            userPrefile.setName(bean.getUsername());
            bean.setToken(tokenService.createJWT("jwt", objectMapper.writeValueAsString(userPrefile), 1000 * 60 * 60 * 60 * 24 * 30));
        }
        return re;
    }


    //-------
}
