package com.fungo.system.service;

import com.fungo.system.dto.*;
import com.fungo.system.entity.*;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.ResultDto;
import com.game.common.dto.user.MemberOutBean;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IUserService {

    /**
     * 第三方用户绑定手机号
     * thirdparty
     * 请求地址：http://{{host}}/api/user/mobile/thirdparty
     * 返回会员信息
     */
    ResultDto<LoginMemberBean> bindingPhoneNo(String code, String mobile, String token,String channel,String deviceId);

    /**
     * 用户登录
     * http://{{host}}/api/user/login
     * @param
     * @throws Exception
     */
    ResultDto<LoginMemberBean> login(String mobile, String password, String code, String appVersion,String deviceId,String channel) throws Exception;

    /**
     * 用户登录
     * http://{{host}}/api/user/login
     * @param
     * @throws Exception
     */
    ResultDto<LoginMemberBean> recommendLogin(MsgInput msgInput, String appVersion) throws Exception;

    /**
     * pc用户登录
     * @param mobile
     * @param password
     * @param code
     * @return
     */
    ResultDto<LoginMemberBean> loginpc(String mobile, String password, String code);

    /**
     * 用户注册（pc用）
     * @param mobile
     * @param password
     * @param code
     * @return
     * @throws Exception
     */
    ResultDto<LoginMemberBean> register(String mobile, String password, String code) throws Exception;


    /**
     * http://{{host}}/api/user/smscode
     */
    ResultDto<String> smscode(String mobile) throws Exception;

    /**
     * 校验验证码
     * http://{{host}}/api/user/verifymobile
     */
    ResultDto<String> verifymobile(String code, String mobile);

    /**
     * 手机号校验（是否被注册）
     * http://{{host}}/api/user/mobileusable
     *
     */
    ResultDto<MobileusableBean> mobileusable(String mobile);

    /**
     * 忘记密码
     * http://{{host}}/api/user/forgotpassword
     */
    void forgotpassword(String code, String mobile);

    /**************************************************************************/
    /**
     * 修改密码
     * http://{{host}}/api/mine/updatepassword
     */
    ResultDto<String> updatepassword(String memberId, String oldPassword, String newPassword);

    /**
     * 退出登录
     * @param memberId
     * @return
     */
    ResultDto<String> logon(String memberId);

    /**
     * 修改绑定手机号
     * http://{{host}}/api/mine/mobile
     * @param memberId
     * @param code
     * @param mobile
     * @return
     */
    ResultDto<String> updateMobile(String memberId, String code, String mobile);

    /**
     * 用户身份校验(配合修改密码操作)
     * http://{{host}}/api/mine/verify
     */
    ResultDto<Map<String, String>> verifyMobile(String memberId, String type, String code, String mobile, String password);


    /**
     * 设置新密码
     * http://{{host}}/api/mine/password
     */
    ResultDto<String> updateNewpasswordByToken(String memberId, String token, String password);

    ResultDto<String> updateNewpassword(String memberId, String password);

    /**
     * 用户信息
     * @param memberId
     * @return
     */
    AuthorBean getAuthor(String memberId);

    /**
     * 用户绑定appleId
     * @param loginId
     * @param msg
     * @return
     * @throws Exception
     */
    ResultDto<String> addAppleId(String loginId, AppleInputBean msg) throws Exception;

    //编辑用户信息
    ResultDto<String> editUser(String memberId, UserBean msg) throws Exception;

    //用户信息
    ResultDto<MemberOutBean> getUserInfo(String memberId) throws Exception;

    //更新用户头像
    ResultDto<Map<String, String>> uploadAvatar(String loginId, MultipartFile appFile);

    //用户信息
    AuthorBean getUserCard(String cardId, String memberId);

    //获得用户身份图标
    List<HashMap<String, Object>> getStatusImage(String memberId) throws Exception;

    //用户注册 初始化数据(Fungo身份证、等级)
    void initUserRank(String memberId);


    ResultDto updateUserRegister(String userId, String registerChannel, String registerPlatform);

    ResultDto<MemberBuriedPointBean> getBuriedPointUserProperties(String loginId);


    //添加管理员关联的虚拟用户
    ResultDto<Member> addVirtualUser(AdminUserInputDTO input, String adminId) throws Exception;


    //添加管理员关联的虚拟用户
    ResultDto<String> userShareMall(String adminId) throws Exception;

}
