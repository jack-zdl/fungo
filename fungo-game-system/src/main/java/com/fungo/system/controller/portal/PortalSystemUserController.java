package com.fungo.system.controller.portal;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.dto.AppleInputBean;
import com.fungo.system.dto.MobileusableBean;
import com.fungo.system.dto.MsgInput;
import com.fungo.system.dto.UserBean;
import com.fungo.system.entity.LoginMemberBean;
import com.fungo.system.entity.Member;
import com.fungo.system.entity.MemberApple;
import com.fungo.system.function.MemberLoginedStatisticsService;
import com.fungo.system.service.*;
import com.game.common.consts.GameConstant;
import com.game.common.consts.MemberIncentTaskConsts;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.user.MemberOutBean;
import com.game.common.enums.FunGoIncentTaskV246Enum;
import com.game.common.framework.file.IFileService;
import com.game.common.util.ValidateUtils;
import com.game.common.util.token.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * pc端用户登录
 * @author sam
 *
 */
@RestController
@Api(value = "", description = "用户登录")
public class PortalSystemUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PortalSystemUserController.class);
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
    private IncentRuleRankService rankRuleService;


    @Autowired
    private IncentRankedService rankedService;


    @Autowired
    private IGameProxy gameProxy;

    @Autowired
    private MemberLoginedStatisticsService memberLoginedStatisticsService;

    @Autowired
    private BasActionService actionService;

    //用户成长业务
    @Resource(name = "memberIncentDoTaskFacadeServiceImpl")
    private IMemberIncentDoTaskFacadeService iMemberIncentDoTaskFacadeService;


    @ApiOperation(value = "用户注册《pc用》", notes = "用户注册《pc用》")
    @RequestMapping(value = "/api/portal/system/user/register", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "code", value = "验证码", paramType = "form", dataType = "string")
    })
    public ResultDto<LoginMemberBean> register(@RequestBody MsgInput msg) throws JsonProcessingException, Exception {
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
    @RequestMapping(value = "/api/portal/system/user/loginpc", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "captcha", value = "验证码", paramType = "form", dataType = "string")
    })
    public ResultDto<LoginMemberBean> loginpc(HttpSession session, @RequestBody MsgInput msg) throws JsonProcessingException, Exception {

        ValidateUtils.is(msg.getMobile()).notNull().maxLength(11).minLength(11);
        String imageCode = (String) session.getAttribute("imageCode");
        System.out.println("HttpSession session" + imageCode);

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

    @ApiOperation(value = "用户登录(2.4)", notes = "用户登录(2.4)")
    @RequestMapping(value = "/api/portal/system/user/login", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "code", value = "验证码", paramType = "form", dataType = "string")
    })
    public ResultDto<LoginMemberBean> login(HttpServletRequest request, @RequestBody MsgInput msg) throws JsonProcessingException, Exception {
//		ValidateUtils.is(msg.getMobile()).notNull().maxLength(11).minLength(11);
//		String os = "";
//		os = (String)request.getAttribute("os");
        String appversion = request.getHeader("appversion");
        ResultDto<LoginMemberBean> re = userService.login(msg.getMobile(), msg.getPassword(), msg.getCode(), appversion);
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
    @RequestMapping(value = "/api/portal/system/user/smscode", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "form", dataType = "string")
    })
    public ResultDto<String> smscode(@RequestBody MsgInput msg) throws Exception {
        ValidateUtils.is(msg.getMobile()).notNull().maxLength(11).minLength(11);
        return userService.smscode(msg.getMobile());
    }

    @ApiOperation(value = "校验验证码", notes = "校验验证码")
    @RequestMapping(value = "/api/portal/system/user/verifymobile", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "验证码", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "form", dataType = "string")
    })
    public ResultDto<String> verifymobile(@RequestBody MsgInput msg) {
        ValidateUtils.is(msg.getCode()).notNull().minLength(6).and(msg.getMobile()).notNull().maxLength(11).minLength(11);
        return userService.verifymobile(msg.getCode(), msg.getMobile());
    }

    @ApiOperation(value = "手机号校验（是否被注册）", notes = "手机号校验（是否被注册）")
    @RequestMapping(value = "/api/portal/system/user/mobileusable", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "form", dataType = "string")
    })
    public ResultDto<MobileusableBean> mobileusable(@RequestBody MsgInput msg) {
        ValidateUtils.is(msg.getMobile()).notNull().maxLength(11).minLength(11);
        return userService.mobileusable(msg.getMobile());
    }

    @ApiOperation(value = "忘记密码", notes = "忘记密码")
    @RequestMapping(value = "/api/portal/system/user/forgotpassword", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "验证码", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "form", dataType = "string")
    })
    public void forgotpassword(@RequestBody MsgInput msg) {
        ValidateUtils.is(msg.getCode()).notNull().minLength(6).and(msg.getMobile()).notNull().maxLength(11).minLength(11);
    }

    /*********************************************************************************/

    @ApiOperation(value = "修改密码", notes = "修改密码")
    @RequestMapping(value = "/api/portal/system/mine/updatepassword", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "old_password", value = "旧密码", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "new_password", value = "新密码", paramType = "form", dataType = "string")
    })
    public ResultDto<String> updatepassword(MemberUserProfile memberUserPrefile, @RequestBody MsgInput msg) {
        ValidateUtils.is(msg.getOld_password()).notNull().and(msg.getNew_password()).notNull();
        return this.userService.updatepassword(memberUserPrefile.getLoginId(), msg.getOld_password(), msg.getNew_password());
    }


    @ApiOperation(value = "退出登录", notes = "退出登录")
    @RequestMapping(value = "/api/portal/system/mine/logout", method = RequestMethod.POST)
    @ApiImplicitParams({
    })
    public ResultDto<String> logon(MemberUserProfile memberUserPrefile) {

        memberLoginedStatisticsService.removeLogoutFromBucket(memberUserPrefile.getLoginId());
        return ResultDto.success();
    }

    @ApiOperation(value = "修改绑定手机号", notes = "修改绑定手机号")
    @RequestMapping(value = "/api/portal/system/mine/mobile", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "验证码", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "form", dataType = "string")
    })
    public ResultDto<String> updateMobile(MemberUserProfile memberUserPrefile, @RequestBody MsgInput msg) {
        ValidateUtils.is(msg.getCode()).notNull().and(msg.getMobile()).notNull();
        return this.userService.updateMobile(memberUserPrefile.getLoginId(), msg.getCode(), msg.getMobile());
    }


    @ApiOperation(value = "用户身份校验(配合修改密码操作)", notes = "用户身份校验(配合修改密码操作)")
    @RequestMapping(value = "/api/portal/system/mine/verify", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "验证码", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "类型", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "form", dataType = "string")
    })
    public ResultDto<Map<String, String>> verifyMobile(MemberUserProfile memberUserPrefile, @RequestBody MsgInput msg) {
        return this.userService.verifyMobile(memberUserPrefile.getLoginId(), msg.getType(), msg.getCode(), msg.getMobile(), msg.getPassword());
    }

//    @ApiOperation(value = "设置新密码", notes = "设置新密码(v.2.4)")
//    @RequestMapping(value = "/api/portal/system/mine/setnewpassword", method = RequestMethod.POST)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "password", value = "密码", paramType = "form", dataType = "string"),
//    })
//    public ResultDto<String> updateNewpassword(MemberUserProfile memberUserPrefile, @RequestBody MsgInput msg) {
//        ValidateUtils.is(msg.getPassword()).notNull();
//        return this.userService.updateNewpassword(memberUserPrefile.getLoginId(), msg.getPassword());
//    }
//
//    @ApiOperation(value = "设置新密码", notes = "设置新密码")
//    @RequestMapping(value = "/api/portal/system/mine/password", method = RequestMethod.POST)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "password", value = "密码", paramType = "form", dataType = "string"),
//            @ApiImplicitParam(name = "token", value = "token验证", paramType = "form", dataType = "string")
//    })
//    public ResultDto<String> updateNewpasswordByToken(MemberUserProfile memberUserPrefile, @RequestBody MsgInput msg) {
//        ValidateUtils.is(msg.getPassword()).notNull();
//        return this.userService.updateNewpasswordByToken(memberUserPrefile.getLoginId(), msg.getToken(), msg.getPassword());
//    }

    /*********************************************设置资料************************************/


    @ApiOperation(value = "个人资料", notes = "用户身份校验(配合修改密码操作)")
    @RequestMapping(value = "/api/portal/system/mine/info", method = RequestMethod.GET)
    @ApiImplicitParams({})
    public ResultDto<MemberOutBean> getUserInfo(MemberUserProfile memberUserPrefile) throws Exception {
        String memberId = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        return userService.getUserInfo(memberId);
    }


    @ApiOperation(value = "编辑个人资料", notes = "编辑个人资料")
    @RequestMapping(value = "/api/portal/system/mine/info", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gender", value = "性别", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "sign", value = "签名", paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "username", value = "名称", paramType = "form", dataType = "string")
    })
    public ResultDto<String> editUser(MemberUserProfile memberUserPrefile, @RequestBody UserBean msg) throws Exception {
        String memberId = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        return userService.editUser(memberId, msg);
    }


    @ApiOperation(value = "验证手机验证码有效性", notes = "验证手机验证码有效性")
    @RequestMapping(value = "/api/portal/system/user/verifycode", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "code", value = "验证码", paramType = "form", dataType = "String")

    })
    public ResultDto<Boolean> verifycode(@RequestBody MsgInput msgInput) {
        ResultDto<Boolean> res = new ResultDto<Boolean>();
        ResultDto<String> re = messageCodeService.checkCode(GameConstant.MSG_TYPE_USER_LOGIN, msgInput.getMobile(), msgInput.getCode());//验证短信
        if (re.isSuccess()) {
            res.setData(true);
        } else {
            res.setData(false);
        }
        return res;
    }


    @Autowired
    private IFileService fileService;
    private String allowSuffix = "jpg,png,gif,jpeg";//允许文件格式
    private long allowSize = 10L;//允许文件大小

    @ApiOperation(value = "上传图片", notes = "上传图片")
    @RequestMapping(value = "/api/portal/system/mine/avatar", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "avatarImage", value = "图片", paramType = "form", dataType = "string")
    })
    public ResultDto<Map<String, String>> uploadAvatar(MemberUserProfile memberUserPrefile, @RequestParam("avatarImage") MultipartFile appFile) throws Exception {
        String suffix = appFile.getOriginalFilename().substring(appFile.getOriginalFilename().lastIndexOf(".") + 1);
        suffix = suffix.toLowerCase();
        int length = getAllowSuffix().indexOf(suffix);
        if (length == -1) {
            throw new Exception("请上传允许格式的文件");
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

        //V2.4.6版本之前任务
        //gameProxy.addTaskCore(Setting.ACTION_TYPE_AVATAR, memberUserPrefile.getLoginId(), "", -1);


        //V2.4.6版本任务
        //1 经验值
        iMemberIncentDoTaskFacadeService.exTask(memberUserPrefile.getLoginId(), FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_UPATE_AVATAR_EXP.code());
        //2 fungo币
        iMemberIncentDoTaskFacadeService.exTask(memberUserPrefile.getLoginId(), FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_UPATE_AVATAR_COIN.code());

        ResultDto<Map<String, String>> re = new ResultDto<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("url", imagePath);
        re.setData(map);

        re.setMessage("编辑成功");

        return re;
    }


    public String getAllowSuffix() {
        return allowSuffix;
    }


}
