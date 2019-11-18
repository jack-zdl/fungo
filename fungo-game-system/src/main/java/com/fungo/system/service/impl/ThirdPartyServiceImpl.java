package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.dto.BindInfo;
import com.fungo.system.dto.ThirdLoginInput;
import com.fungo.system.entity.LoginMemberBean;
import com.fungo.system.entity.Member;
import com.fungo.system.function.MemberLoginedStatisticsService;
import com.fungo.system.helper.ThirdPartyLoginHelper;
import com.fungo.system.service.*;
import com.game.common.consts.MemberIncentTaskConsts;
import com.game.common.dto.ResultDto;
import com.game.common.enums.FunGoIncentTaskV246Enum;
import com.game.common.util.CommonUtil;
import com.game.common.util.FungoStringUtils;
import com.game.common.util.RandomsAndId;
import com.game.common.util.UUIDUtils;
import com.game.common.util.date.DateTools;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ThirdPartyServiceImpl implements IThirdPartyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyServiceImpl.class);


    @Autowired
    private MemberService memberService;
    @Autowired
    ThirdLoginService thirdLoginService;
    @Autowired
    private MemberNoService memberNoService;
    @Autowired
    private IGameProxy gameProxy;
    @Autowired
    private ScoreLogService scoreLogService;
    @Autowired
    private IUserService iUserService;

    @Autowired
    private IPKNoService iPKNoService;

    @Autowired
    private MemberLoginedStatisticsService memberLoginedStatisticsService;

    //用户成长业务
    @Resource(name = "memberIncentDoTaskFacadeServiceImpl")
    private IMemberIncentDoTaskFacadeService iMemberIncentDoTaskFacadeService;


    /**
     * 第三方登录,判断是哪方登录 判断用户是否存在 判断有无绑定用户 如有,直接切换 如无,新建用户(第三方用户绑定功能) 用户登录,memberProfile
     */
    @Override
    public ResultDto<LoginMemberBean> thirdPartyLogin(ThirdLoginInput input, String channel, String appVersion,String deviceId) throws Exception {

        LOGGER.info("第三方登录入参-input:{}---channel:{}", JSON.toJSONString(input), channel);
        Integer platform = input.getPlatformType();
        String accessToken = input.getAccessToken();

        //QQ/微信/微博 识别平台下不同用户，统一使用 unionid
        String unionid = input.getUnionid();

        //***************************************************************************************************
        //fixbug: 兼容V2.4.6之前版本App，若没有 unionid字段按V2.4.6之前字段处理 [by mxf 2019-03-11]
        //微博
        if (0 == platform) {
            //user.setWeiboOpenId(input.getUid());
            if (StringUtils.isBlank(unionid)) {
                unionid = input.getUid();
            }
            //微信
        } else if (1 == platform) {
            //user.setWeixinOpenId(input.getOpenid());
            if (StringUtils.isBlank(unionid)) {
                unionid = input.getOpenid();
            }
            //QQ
        } else if (4 == platform) {
            //user.setQqOpenId(input.getOpenid());
            if (StringUtils.isBlank(unionid)) {
                unionid = input.getOpenid();
            }
        }

        if (CommonUtil.isNull(accessToken) || CommonUtil.isNull(unionid)) {
            return ResultDto.error("13", "非法的第三方信息");
        }

        //***************************************************************************************************
        Member member = null;
        //微博 [微博只有uid，没有unionid]
        if (platform == 0) {

            List<Member> memberList = memberService.selectList(new EntityWrapper<Member>().eq("weibo_open_id", unionid).and("state != {0}", -1));
            member = getMemberWithSNSLogin(memberList);

            //微信
        } else if (platform == 1) {

            //先用 unionid
            List<Member> memberList = memberService.selectList(new EntityWrapper<Member>().eq("weixin_open_id", unionid).and("state != {0}", -1));
            member = getMemberWithSNSLogin(memberList);
            if (null == member) {
                //获取openId ，把用户的历史openid更新为 unionid
                String wxOpenId = input.getOpenid();
                if (StringUtils.isNotBlank(wxOpenId)) {

                    List<Member> memberListWX = memberService.selectList(new EntityWrapper<Member>().eq("weixin_open_id", wxOpenId).and("state != {0}", -1));
                    member = getMemberWithSNSLogin(memberListWX);

                    if (null != member) {
                        //把数据库中已存在的openId更新为unionid
                        Member memberWXUpdate = new Member();
                        memberWXUpdate.setId(member.getId());
                        memberWXUpdate.setWeixinOpenId(unionid);
                        memberWXUpdate.updateById();
                    }
                }
            }
            //qq
        } else if (platform == 4) {

            List<Member> memberList = memberService.selectList(new EntityWrapper<Member>().eq("qq_open_id", unionid).and("state != {0}", -1));
            member = getMemberWithSNSLogin(memberList);
            if (null == member) {
                //获取openId 把用户的历史openid更新为 unionid
                String qqOpenId = input.getOpenid();
                if (StringUtils.isNotBlank(qqOpenId)) {

                    List<Member> memberListQQ = memberService.selectList(new EntityWrapper<Member>().eq("qq_open_id", qqOpenId).and("state != {0}", -1));
                    member = getMemberWithSNSLogin(memberListQQ);

                    if (null != member) {
                        //把数据库中已存在的openId更新为unionid
                        Member memberQQUpdate = new Member();
                        memberQQUpdate.setId(member.getId());
                        memberQQUpdate.setQqOpenId(unionid);
                        memberQQUpdate.updateById();
                    }
                }
            }
        }
        //记录member登录设备号和信息
        if(null!= member && StringUtils.isNotBlank(channel) && StringUtils.isNotBlank(deviceId)){
           Member chaAndDe = new Member();
            chaAndDe.setId(member.getId());
            chaAndDe.setDeviceId(deviceId);
            chaAndDe.setChannel(channel);
            chaAndDe.updateById();
        }
        //******************************************已注册*********************************************************
        ResultDto<LoginMemberBean> rest = new ResultDto<LoginMemberBean>();
        // 如有 切换为当前用户，登录
        if (member != null) {
            if (member.getMobilePhoneNum() != null && !"".equals(member.getMobilePhoneNum())) {
                LoginMemberBean bean = new LoginMemberBean();
                bean.setCreatedAt(DateTools.fmtDate(member.getCreatedAt()));
                bean.setUpdatedAt(DateTools.fmtDate(member.getUpdatedAt()));
                bean.setEmailVerified(false);
                bean.setMobilePhoneNumber(member.getMobilePhoneNum());
                bean.setUsername(member.getUserName());

                //fix bug: [by mxf 2019-03-06]
                //bean.setMobilePhoneVerified(member.getMobilePhoneVerified().equals("1") ? true : false);
                String mobilePhoneVerified = member.getMobilePhoneVerified();
                if (StringUtils.isNotBlank(mobilePhoneVerified) && StringUtils.equalsIgnoreCase("1", mobilePhoneVerified)) {
                    bean.setMobilePhoneVerified(true);
                } else {
                    bean.setMobilePhoneVerified(false);
                }
                //end
                LOGGER.info("mobilePhoneVerified:{}", mobilePhoneVerified);

                bean.setObjectId(member.getId());
                rest.setData(bean);

                if (!"pc".equals(channel)) {
                    //记录登录用户
                    memberLoginedStatisticsService.addLoginToBucket(member.getId(), appVersion);
                }

                return rest;
            } else {
                LoginMemberBean bean = new LoginMemberBean();
                bean.setCreatedAt(DateTools.fmtDate(member.getCreatedAt()));
                bean.setUpdatedAt(DateTools.fmtDate(member.getUpdatedAt()));
                bean.setEmailVerified(false);
                bean.setMobilePhoneVerified(false);
                bean.setUsername(member.getUserName());
                bean.setObjectId(member.getId());
                rest.setData(bean);
                rest.setMessage(member.getSesionToken());
                rest.setStatus(2);

                if (!"pc".equals(channel)) {
                    //记录登录用户
                    memberLoginedStatisticsService.addLoginToBucket(member.getId(), appVersion);
                }
                return rest;
            }

            //*********************************************未注册******************************************************
        } else {
            // 如无 注册并请求绑定手机号
            //非PC通道 验证用户在第三方平台是否有效
            if (!"pc".equals(channel)) {

                String uid = input.getUid();
                String openid = input.getOpenid();
                ResultDto<String> thre;
                if (platform == 0) {// 新浪微博
                    thre = this.thirdLoginService.checkThirdParty(platform, accessToken, uid, channel);
                } else {
                    thre = this.thirdLoginService.checkThirdParty(platform, accessToken, openid, channel);
                }
                if (!thre.isSuccess()) {
                    return ResultDto.error(thre.getCode(), thre.getMessage());
                }
            }
            Member user = new Member();

            //set id
            user.setId(UUIDUtils.getUUID());

            String name = input.getName().trim();
            if (CommonUtil.isNull(name)) {
                return ResultDto.error("26", "用户名不存在");
            }
            user.setUserName(name);
            user.setAvatar(input.getIconurl());
            Date date = new Date();
            user.setCreatedAt(date);
            user.setHasPassword("0");
            user.setUpdatedAt(date);
            //三个平台同一保存 unionid
            //微博
            if (platform == 0) {
                LOGGER.info("用户注册-新浪微博-初始化数据 memberId : {}", user.getId());
                //user.setWeiboOpenId(input.getUid());
                user.setWeiboOpenId(unionid);
                //微信
            } else if (platform == 1) {
                LOGGER.info("用户注册-微信-初始化数据  memberId : {}", user.getId());
                //user.setWeixinOpenId(input.getOpenid());
                user.setWeixinOpenId(unionid);
                //QQ
            } else if (platform == 4) {
                LOGGER.info("用户注册-QQ-初始化数据  memberId : {}", user.getId());
                //user.setQqOpenId(input.getOpenid());
                user.setQqOpenId(unionid);
            }

            //用户
            String token = RandomsAndId.getUUID();
            user.setSesionToken(token);

            //fix bug:修改会员编号出现的重复的情况 修改用户的member_no用户编号 [by mxf 2019-03-06]
            String mb_no = iPKNoService.genUniqueMbNo(user.getId());
            user.setMemberNo(mb_no);
            //end

            //添加用户数据
            user.insert();

            //fix bug:修改会员编号出现的重复的情况 修改用户的member_no用户编号 [by mxf 2019-03-06]
            //memberNoService.getMemberNoAndForUpdate(user);
            //end

            LoginMemberBean bean = new LoginMemberBean();
            bean.setCreatedAt(DateTools.fmtDate(user.getCreatedAt()));
            bean.setUpdatedAt(DateTools.fmtDate(user.getUpdatedAt()));
            bean.setEmailVerified(false);
            bean.setMobilePhoneVerified(false);
            bean.setUsername(user.getUserName());
            bean.setObjectId(user.getId());
            rest.setData(bean);
            rest.setMessage(token);
            rest.setStatus(2);
//            首次第三方登录埋点事件ID:login005

//			scoreLogService.updateRanked(user.getId(), new ObjectMapper(), 1);
//			scoreLogService.updateRanked(user.getId(), new ObjectMapper(), 24);
            //初始化等级身份
            LOGGER.info("第三方注册-初始化等级和fungo身份证开始....");
            iUserService.initUserRank(user.getId());
            LOGGER.info("第三方注册-初始化等级和fungo身份证结束");

            //V2.4.6版本任务之前任务 废弃
            //gameProxy.addTaskCore(Setting.ACTION_TYPE_BIND_THIRDPARTY, user.getId(), "", -1);
            //执行用户任务
            doTask(platform, user.getId());

            if (!"pc".equals(channel)) {
                //记录登录用户
                memberLoginedStatisticsService.addLoginToBucket(user.getId(), appVersion);
            }
            return rest;
        }
    }

    /**
     * 获取用户数据
     * 若用户多次进行第三方登录，存在多个SNS unoinid数据的情况，优先使用有手机号的
     * @param memberList
     * @return
     */
    private Member getMemberWithSNSLogin(List<Member> memberList) {
        Member member = null;
        if (null != memberList && !memberList.isEmpty()) {
            if (1 == memberList.size()) {
                return memberList.get(0);
            }
            for (Member memberDB : memberList) {
                String mobilePhoneNum = memberDB.getMobilePhoneNum();
                if (StringUtils.isNotBlank(mobilePhoneNum) && FungoStringUtils.isNumeric(mobilePhoneNum)) {
                    return memberDB;
                }
                member = memberDB;
            }
        }
        return member;
    }

    /**
     * 执行用户任务
     * @param platform
     */
    private void doTask(int platform, String mb_id) {
        try {
            //V2.4.6版本任务
            //微博
            if (platform == 0) {
                //1 经验值
                iMemberIncentDoTaskFacadeService.exTask(mb_id, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                        MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_NS_WB_EXP.code());

                //2 fungo币
                iMemberIncentDoTaskFacadeService.exTask(mb_id, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                        MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_SNS_WB_COIN.code());
                //微信
            } else if (platform == 1) {
                //1 经验值
                iMemberIncentDoTaskFacadeService.exTask(mb_id, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                        MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_NS_WX_EXP.code());
                //2 fungo币
                iMemberIncentDoTaskFacadeService.exTask(mb_id, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                        MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_SNS_WX_COIN.code());
                //QQ
            } else if (platform == 4) {
                //1 经验值
                iMemberIncentDoTaskFacadeService.exTask(mb_id, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                        MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_NS_QQ_EXP.code());

                //2 fungo币
                iMemberIncentDoTaskFacadeService.exTask(mb_id, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                        MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_SNS_QQ_COIN.code());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    @Transactional
    public ResultDto<String> thirdUserBind(String userId, ThirdLoginInput input, String channel) throws Exception {
        Member user = memberService.selectById(userId);

        Integer platform = input.getPlatformType();

        // 用户有无绑定  第三方账号存不存在
        boolean b = false;
        boolean t = false;
        String accessToken = input.getAccessToken();

        String uid = input.getUid();
        String openid = input.getOpenid();
        String unionid = input.getUnionid();

        //fixbug: 兼容V2.4.6之前版本App，若没有 unionid字段按V2.4.6之前字段处理 [by mxf 2019-03-11]
        //微博
        if (0 == platform) {
            //user.setWeiboOpenId(input.getUid());
            if (StringUtils.isBlank(unionid)) {
                unionid = uid;
            }
            //微信
        } else if (1 == platform) {
            //user.setWeixinOpenId(input.getOpenid());
            if (StringUtils.isBlank(unionid)) {
                unionid = openid;
            }
            //QQ
        } else if (4 == platform) {
            //user.setQqOpenId(input.getOpenid());
            if (StringUtils.isBlank(unionid)) {
                unionid = openid;
            }
        }

        if (CommonUtil.isNull(accessToken) || CommonUtil.isNull(unionid)) {
            return ResultDto.error("13", "非法的第三方信息");
        }

        // 新浪微博
        if (platform == 0) {
            if (!CommonUtil.isNull(user.getWeiboOpenId())) {
                return ResultDto.error("-1", "已经绑定第三方信息,如需重新绑定请先解绑");
            }
            if (user.getWeiboOpenId() == null) {
                t = true;
            }
            b = ThirdPartyLoginHelper.isSinaUser(accessToken, uid);
            if (b) {
                //保存unionid
                //user.setWeiboOpenId(uid);
                user.setWeiboOpenId(unionid);
            }
            //微信
        } else if (platform == 1 && findUserByThirdIdAndClean("weixin_open_id", openid)) {
            if (!CommonUtil.isNull(user.getWeixinOpenId())) {
                return ResultDto.error("-1", "已经绑定第三方信息,如需重新绑定请先解绑");
            }
            if (user.getWeixinOpenId() == null) {
                t = true;
            }
            b = ThirdPartyLoginHelper.isWxUser(accessToken, openid);
            if (b) {
                //保存unionid
                //user.setWeixinOpenId(openid);
                user.setWeixinOpenId(unionid);
            }
            //QQ
        } else if (platform == 4 && findUserByThirdIdAndClean("qq_open_id", openid)) {
            if (!CommonUtil.isNull(user.getQqOpenId())) {
                return ResultDto.error("-1", "已经绑定第三方信息,如需重新绑定请先解绑");
            }
            if (user.getQqOpenId() == null) {
                t = true;
            }
            b = ThirdPartyLoginHelper.isQQUser(accessToken, openid, channel);
            if (b && findUserByThirdIdAndClean("weibo_open_id", openid)) {
                //保存unionid
                //user.setQqOpenId(openid);
                user.setQqOpenId(unionid);
            }
        } else {
            return ResultDto.error("13", "该第三方账号已绑定其他账号");
        }
        if (!b) {
            return ResultDto.error("-1", "第三方用户验证错误");
        }

        memberService.updateById(user);
        int times = -1;
        if (t) {
            //V2.4.6版本之前任务 废弃
            //times = gameProxy.addTaskCore(Setting.ACTION_TYPE_BIND_THIRDPARTY, userId, "", -1);
            //V2.4.6版本任务
            doTask(platform, userId);
        }
        ResultDto<String> re = new ResultDto<String>();
        if (times > 0) {
            re.show("绑定成功！经验+2,Fun币+100！");
        } else {
            re.show("绑定成功！");
        }

        return re;

    }


    /**
     * 清楚第三方登录没有绑定手机号的会员信息
     * @param platForm
     * @param ThirdId
     * @return
     */
    private boolean findUserByThirdIdAndClean(String platForm, String ThirdId) {
        Member member = memberService.selectOne(new EntityWrapper<Member>().eq(platForm, ThirdId));
        if (member != null) {
            if (null == member.getMobilePhoneNum() || "".equals(member.getMobilePhoneNum())) {
                member.deleteById();
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public ResultDto<String> thirdUserUnbind(String userId, Integer platformType) {
        Member user = memberService.selectById(userId);
        if (platformType == 0) {// 新浪微博
            user.setWeiboOpenId("");
        } else if (platformType == 1) {//微信
            user.setWeixinOpenId("");
        } else if (platformType == 4) {//QQ
            user.setQqOpenId("");
        } else {
            return ResultDto.error("13", "请求参数错误");
        }
        user.updateAllColumnById();
        return ResultDto.success();
    }

    @Override
    public ResultDto<List<BindInfo>> thirdUserUnbind(String userId) {
        Member user = memberService.selectById(userId);
        List<BindInfo> bindInfo = new ArrayList<>();
        BindInfo b1 = new BindInfo();
        b1.setPlatformtype(0);
        b1.setName("weibo");
        BindInfo b2 = new BindInfo();
        b2.setPlatformtype(1);
        b2.setName("weixin");
        BindInfo b3 = new BindInfo();
        b3.setPlatformtype(4);
        b3.setName("qq");
        if (!CommonUtil.isNull(user.getWeiboOpenId())) {
            b1.setIs_bind(true);
        }
        if (!CommonUtil.isNull(user.getWeixinOpenId())) {
            b2.setIs_bind(true);
        }
        if (!CommonUtil.isNull(user.getQqOpenId())) {
            b3.setIs_bind(true);
        }
        bindInfo.add(b1);
        bindInfo.add(b2);
        bindInfo.add(b3);

        return ResultDto.success(bindInfo);
    }

}
