package com.fungo.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.dto.*;
import com.fungo.system.entity.*;
import com.fungo.system.function.MemberLoginedStatisticsService;
import com.fungo.system.service.*;
import com.game.common.buriedpoint.BuriedPointUtils;
import com.game.common.consts.GameConstant;
import com.game.common.dto.AbstractEventDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.user.MemberOutBean;
import com.game.common.enums.AbstractResultEnum;
import com.game.common.framework.file.IFileService;
import com.game.common.util.ValidateUtils;
import com.game.common.util.annotation.JsonView;
import com.game.common.util.annotation.LogicCheck;
import com.game.common.util.token.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 用户登录
 * @author sam
 *
 */
@RestController
@Api(value = "", description = "用户登录")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private MessageCodeService messageCodeService;
    @Autowired
    private IUserService userService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberAppleService memberAppleService;
    @Autowired
    private MemberLoginedStatisticsService memberLoginedStatisticsService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 功能描述: 这是提供给pc端使用
     * @date: 2019/10/15 15:45
     */
    @ApiOperation(value = "用户注册《pc用》", notes = "用户注册《pc用》")
    @PostMapping(value = "/api/user/register")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "code", value = "验证码", paramType = "form", dataType = "string")
    })
    public ResultDto<LoginMemberBean> register(@RequestBody MsgInput msg) throws  Exception {
        ValidateUtils.is(msg.getMobile()).notNull().maxLength(11).minLength(11);
        ResultDto<LoginMemberBean> re = userService.register(msg.getMobile(), msg.getPassword(), msg.getCode());
        if (re.isSuccess()) {
            LoginMemberBean bean = re.getData();
            MemberUserProfile userPrefile = new MemberUserProfile();
            userPrefile.setLoginId(bean.getObjectId());
            userPrefile.setName(bean.getUsername());
            bean.setToken(tokenService.createJWT("jwt", objectMapper.writeValueAsString(userPrefile), 1000 * 60 * 60 * 60 * 24 * 30));
        }
        return re;
    }

    @ApiOperation(value = "用户登录(pc)", notes = "用户登录")
    @PostMapping(value = "/api/user/loginpc")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "captcha", value = "验证码", paramType = "form", dataType = "string")
    })
    public ResultDto<LoginMemberBean> loginpc(HttpSession session, @RequestBody MsgInput msg) throws Exception {
        ValidateUtils.is(msg.getMobile()).notNull().maxLength(11).minLength(11);
        String imageCode = (String) session.getAttribute("imageCode");
        if (msg.getCaptcha() == null || imageCode == null || !imageCode.toLowerCase().equals(msg.getCaptcha().toLowerCase())) {
            return ResultDto.error("100", "验证码错误");
        }
        ResultDto<LoginMemberBean> re = userService.loginpc(msg.getMobile(), msg.getPassword(), msg.getCode());
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
     * 功能描述: app端直接手机号和验证码注册新用户
     * @date: 2019/10/15 15:45
     */
    @ApiOperation(value = "用户登录(2.4)", notes = "用户登录(2.4)")
    @PostMapping(value = "/api/user/login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "code", value = "验证码", paramType = "form", dataType = "string")
    })
    public ResultDto<LoginMemberBean> login(HttpServletRequest request, @RequestBody MsgInput msg) throws  Exception {
        String appversion = request.getHeader("appversion");
        String deviceId = request.getHeader("deviceId");
        String channel = request.getHeader("os");
        ResultDto<LoginMemberBean> re = userService.login(msg.getMobile(), msg.getPassword(), msg.getCode(), appversion,deviceId,channel);
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
     * 功能描述: v2.6 被邀请人直接手机号和验证码注册新用户
     * @date: 2019/10/15 15:45
     */
    @ApiOperation(value = "用户登录(2.4)", notes = "用户登录(2.4)")
    @PostMapping(value = "/api/user/login/recommend")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "recommendId", value = "邀请人id", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "code", value = "验证码", paramType = "form", dataType = "string")
    })

    public ResultDto<LoginMemberBean> recommendLogin(HttpServletRequest request, @RequestBody MsgInput msg) throws  Exception {
        String appversion = request.getHeader("appversion");
        if(StringUtils.isBlank(msg.getCode()) || StringUtils.isBlank(msg.getRecommendId()) ){
            return ResultDto.ResultDtoFactory.buildError(AbstractResultEnum.CODE_SYSTEM_FESTIVAL_NINE.getKey(),AbstractResultEnum.CODE_SYSTEM_FESTIVAL_NINE.getFailevalue());
        }
        ResultDto<LoginMemberBean> re = userService.recommendLogin(msg, appversion);
        if (re.isSuccess()) {
            LoginMemberBean bean = re.getData();
            MemberUserProfile userPrefile = new MemberUserProfile();
            userPrefile.setLoginId(bean.getObjectId());
            userPrefile.setName(bean.getUsername());
            bean.setToken(tokenService.createJWT("jwt", objectMapper.writeValueAsString(userPrefile), 1000 * 60 * 60 * 60 * 24 * 30));
        }
        return re;
    }

    @ApiOperation(value = "发送验证码", notes = "发送验证码")
    @PostMapping(value = "/api/user/smscode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "form", dataType = "string")
    })
    public ResultDto<String> smscode(@RequestBody MsgInput msg) throws Exception {
        ValidateUtils.is(msg.getMobile()).notNull().maxLength(11).minLength(11);
        return userService.smscode(msg.getMobile());
    }

    @ApiOperation(value = "校验验证码", notes = "校验验证码")
    @PostMapping(value = "/api/user/verifymobile")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "验证码", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "form", dataType = "string")
    })
    public ResultDto<String> verifymobile(@RequestBody MsgInput msg) {
        ValidateUtils.is(msg.getCode()).notNull().minLength(6).and(msg.getMobile()).notNull().maxLength(11).minLength(11);
        return userService.verifymobile(msg.getCode(), msg.getMobile());
    }

    @ApiOperation(value = "手机号校验（是否被注册）", notes = "手机号校验（是否被注册）")
    @PostMapping(value = "/api/user/mobileusable")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "form", dataType = "string")
    })
    public ResultDto<MobileusableBean> mobileusable(@RequestBody MsgInput msg) {
        ValidateUtils.is(msg.getMobile()).notNull().maxLength(11).minLength(11);
        return userService.mobileusable(msg.getMobile());
    }

    @ApiOperation(value = "忘记密码", notes = "忘记密码")
    @PostMapping(value = "/api/user/forgotpassword")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "验证码", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "form", dataType = "string")
    })
    public void forgotpassword(@RequestBody MsgInput msg) {
        ValidateUtils.is(msg.getCode()).notNull().minLength(6).and(msg.getMobile()).notNull().maxLength(11).minLength(11);
    }

    /*********************************************************************************/

    @ApiOperation(value = "修改密码", notes = "修改密码")
    @PostMapping(value = "/api/mine/updatepassword")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "old_password", value = "旧密码", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "new_password", value = "新密码", paramType = "form", dataType = "string")
    })
    public ResultDto<String> updatepassword(MemberUserProfile memberUserPrefile, @RequestBody MsgInput msg) {
        ValidateUtils.is(msg.getOld_password()).notNull().and(msg.getNew_password()).notNull();
        return this.userService.updatepassword(memberUserPrefile.getLoginId(), msg.getOld_password(), msg.getNew_password());
    }

    @ApiOperation(value = "退出登录", notes = "退出登录")
    @PostMapping(value = "/api/mine/logout")
    @ApiImplicitParams({
    })
    public ResultDto<String> logon(MemberUserProfile memberUserPrefile) {

        memberLoginedStatisticsService.removeLogoutFromBucket(memberUserPrefile.getLoginId());
        return ResultDto.success();
    }

    @ApiOperation(value = "修改绑定手机号", notes = "修改绑定手机号")
    @PostMapping(value = "/api/mine/mobile")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "验证码", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "form", dataType = "string")
    })
    public ResultDto<String> updateMobile(MemberUserProfile memberUserPrefile, @RequestBody MsgInput msg) {
        ValidateUtils.is(msg.getCode()).notNull().and(msg.getMobile()).notNull();
        return this.userService.updateMobile(memberUserPrefile.getLoginId(), msg.getCode(), msg.getMobile());
    }

    @ApiOperation(value = "用户身份校验(配合修改密码操作)", notes = "用户身份校验(配合修改密码操作)")
    @PostMapping(value = "/api/mine/verify")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "验证码", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "类型", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "form", dataType = "string")
    })
    public ResultDto<Map<String, String>> verifyMobileByUser(MemberUserProfile memberUserPrefile, @RequestBody MsgInput msg) {
        return this.userService.verifyMobile(memberUserPrefile.getLoginId(), msg.getType(), msg.getCode(), msg.getMobile(), msg.getPassword());
    }

    @ApiOperation(value = "设置新密码", notes = "设置新密码(v.2.4)")
    @PostMapping(value = "/api/mine/setnewpassword")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "密码", paramType = "form", dataType = "string"),
    })
    public ResultDto<String> updateNewpassword(MemberUserProfile memberUserPrefile, @RequestBody MsgInput msg) {
        ValidateUtils.is(msg.getPassword()).notNull();
        return this.userService.updateNewpassword(memberUserPrefile.getLoginId(), msg.getPassword());
    }

    @ApiOperation(value = "设置新密码", notes = "设置新密码")
    @PostMapping(value = "/api/mine/password")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "密码", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "token", value = "token验证", paramType = "form", dataType = "string")
    })
    public ResultDto<String> updateNewpasswordByToken(MemberUserProfile memberUserPrefile, @RequestBody MsgInput msg) {
        ValidateUtils.is(msg.getPassword()).notNull();
        return this.userService.updateNewpasswordByToken(memberUserPrefile.getLoginId(), msg.getToken(), msg.getPassword());
    }

    /*********************************************设置资料************************************/

    @ApiOperation(value = "个人资料", notes = "用户身份校验(配合修改密码操作)")
    @GetMapping(value = "/api/mine/info")
    @ApiImplicitParams({})
    public ResultDto<MemberOutBean> getUserInfo(MemberUserProfile memberUserPrefile) throws Exception {
        String memberId = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        return userService.getUserInfo(memberId);
    }

    @ApiOperation(value = "编辑个人资料", notes = "编辑个人资料")
    @PostMapping(value = "/api/mine/info")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gender", value = "性别", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "sign", value = "签名", paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "username", value = "名称", paramType = "form", dataType = "string")
    })
    @JsonView
    public ResultDto<String> editUser(MemberUserProfile memberUserPrefile, @Valid @RequestBody UserBean msg, BindingResult errors) throws Exception {
        if(errors.hasErrors()){
            return ResultDto.ResultDtoFactory.buildSuccess( AbstractResultEnum.CODE_SYSTEM_FIVE.getKey(),errors.getAllErrors().stream().map( ObjectError::getDefaultMessage).collect(Collectors.joining(",") ));
        }
        String memberId = memberUserPrefile.getLoginId();
        return userService.editUser(memberId, msg);
    }


    @ApiOperation(value = "编辑个人资料", notes = "编辑个人资料")
    @PostMapping(value = "/api/user/info")
    @JsonView
    @LogicCheck(loginc = {"BANNED_AUTH"})
    public ResultDto<String> editUserAuth(MemberUserProfile memberUserPrefile, @Valid @RequestBody UserBean msg, BindingResult errors) throws Exception {
        if(errors.hasErrors()){
            return ResultDto.ResultDtoFactory.buildSuccess( AbstractResultEnum.CODE_SYSTEM_FIVE.getKey(),errors.getAllErrors().stream().map( ObjectError::getDefaultMessage).collect(Collectors.joining(",") ));
        }
        return userService.editUser(msg.getId(), msg);
    }


    @ApiOperation(value = "验证手机验证码有效性", notes = "验证手机验证码有效性")
    @PostMapping(value = "/api/user/verifycode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "code", value = "验证码", paramType = "form", dataType = "String")

    })
    public ResultDto<Boolean> verifycode(@RequestBody MsgInput msgInput) {
        ResultDto<Boolean> res = new ResultDto<Boolean>();
        ResultDto<String> re = messageCodeService.checkCode(GameConstant.MSG_TYPE_USER_LOGIN, msgInput.getMobile(), msgInput.getCode());//验证短信
        res.setData(re.isSuccess());
        return res;
    }

    @ApiOperation(value = "用户绑定appleId(v2.3)", notes = "")
    @PostMapping(value = "/api/user/addAppleId")
    @ApiImplicitParams({})
    public ResultDto<String> addAppleId(MemberUserProfile memberUserPrefile, @RequestBody AppleInputBean msg) throws Exception {
        String memberId =  memberUserPrefile.getLoginId();
        return userService.addAppleId(memberId, msg);
    }

    @ApiOperation(value = "获取appleId(v2.3)", notes = "")
    @RequestMapping(value = "/api/user/getAppleId", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<AppleInputBean> getAppleId(MemberUserProfile memberUserPrefile) {
        ResultDto<AppleInputBean> re = new ResultDto<AppleInputBean>();
        MemberApple ma = this.memberAppleService.selectOne(new EntityWrapper<MemberApple>().eq("member_id", memberUserPrefile.getLoginId()));
        if (ma != null) {
            AppleInputBean a = new AppleInputBean();
            a.setAppleId(ma.getAppleId());
            a.setName(ma.getName());
            a.setSurname(ma.getSurname());
            re.setData(a);
        }
        return re;
    }

    @Autowired
    private IFileService fileService;
    private String allowSuffix = "jpg,png,gif,jpeg";//允许文件格式
    private long allowSize = 10L;//允许文件大小

    @ApiOperation(value = "上传图片", notes = "上传图片")
    @PostMapping(value = "/api/mine/avatar")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "avatarImage", value = "图片", paramType = "form", dataType = "string")
    })
    public ResultDto<Map<String, String>> uploadAvatar(MemberUserProfile memberUserPrefile, @RequestParam("avatarImage") MultipartFile appFile) throws Exception {
        String suffix = appFile.getOriginalFilename().substring(appFile.getOriginalFilename().lastIndexOf(".") + 1);
        suffix = suffix.toLowerCase();
        int length = getAllowSuffix().indexOf(suffix);
        if (length == -1) {
            return ResultDto.ResultDtoFactory.buildError( "请上传允许格式的文件" );
        }
//         if(file.getSize() > getAllowSize()){
//             throw new Exception("您上传的文件大小已经超出范围");
//         }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String datePath = sdf.format(new Date());
        String name = UUID.randomUUID().toString();
//         File f = new File(name+"."+suffix);
//         file.transferTo(f);
//         CommonsMultipartFile cf = (CommonsMultipartFile) appFile;
//         InputStream fileContent = cf.getInputStream();

        String imagePath =  fileService.saveFile(datePath + "/" + name + "." + suffix, suffix, appFile.getInputStream());
        Member member = this.memberService.selectById(memberUserPrefile.getLoginId());
        member.setAvatar(imagePath);
        member.updateById();

        // 2.7 任务
        AbstractEventDto abstractEventDto = new AbstractEventDto(this);
        abstractEventDto.setEventType( AbstractEventDto.AbstractEventEnum.EDIT_USER.getKey());
        abstractEventDto.setUserId(member.getId());
        applicationEventPublisher.publishEvent(abstractEventDto);
        ResultDto<Map<String, String>> re = new ResultDto<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("url", imagePath);
        re.setData(map);
        re.setMessage("编辑成功");
        return re;
    }

   @GetMapping("/api/user/getBuriedPointUserProperties")
   public ResultDto<MemberBuriedPointBean> getBuriedPointUserProperties(String userId){
        return userService.getBuriedPointUserProperties(userId);
    }

    /**
     *  更新 用户注册信息
     * @param userId 用户id
     * @param registerChannel 注册渠道
     * @return 结果
     */
    @GetMapping("/api/user/updateUserRegister")
    public ResultDto updateUserRegister(String userId,String registerChannel){
        String platForm = BuriedPointUtils.getPlatForm();
        return  userService.updateUserRegister(userId,registerChannel,platForm);
    }

    @ApiOperation(value="新增虚拟用户", notes="")
    @PostMapping(value="/api/system/user/mockuser")
    @ApiImplicitParams({})
    public ResultDto<Member> addVirtualUser(MemberUserProfile memberUserPrefile, @RequestBody AdminUserInputDTO input){
        String adminId = memberUserPrefile.getLoginId();
        try {
            return userService.addVirtualUser(input,adminId);
        } catch (Exception e) {
            return ResultDto.error( "-1","新增虚拟用户异常" );
        }
    }

    @ApiOperation(value="用户分享中秋礼品", notes="")
    @RequestMapping(value="/api/system/user/share", method= RequestMethod.GET)
    @ApiImplicitParams({})
    public ResultDto<String> userShareMall(MemberUserProfile memberUserPrefile){
        String adminId = memberUserPrefile.getLoginId();
        try {
            return userService.userShareMall(adminId);
        } catch (Exception e) {
            return ResultDto.error( "-1","新增虚拟用户异常" );
        }
    }

    public String getAllowSuffix() {
        return allowSuffix;
    }

    public void setAllowSuffix(String allowSuffix) {
        this.allowSuffix = allowSuffix;
    }

    public long getAllowSize() {
        return allowSize;
    }

    public void setAllowSize(long allowSize) {
        this.allowSize = allowSize;
    }

}
