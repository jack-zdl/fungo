package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.dao.MemberInfoDao;
import com.fungo.system.dao.SysMockuserDao;
import com.fungo.system.dto.*;
import com.fungo.system.entity.*;
import com.fungo.system.facede.ICommunityProxyService;
import com.fungo.system.function.MemberLoginedStatisticsService;
import com.fungo.system.service.*;
import com.game.common.consts.GameConstant;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.consts.MemberIncentTaskConsts;
import com.game.common.consts.MemberLoginConsts;
import com.game.common.consts.Setting;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.ResultDto;
import com.game.common.dto.user.MemberOutBean;
import com.game.common.enums.AbstractResultEnum;
import com.game.common.enums.FunGoIncentTaskV246Enum;
import com.game.common.repo.cache.facade.FungoCacheMember;
import com.game.common.util.*;
import com.game.common.util.date.DateTools;
import com.game.common.util.exception.BusinessException;

import com.game.common.util.CommonUtil;
import com.game.common.util.SecurityMD5;

import com.game.common.validate.an.Max;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private MessageCodeService messageCodeService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberNoService memberNoService;
    @Autowired
    private IPushService pushService;
    @Autowired
    private IncentRuleRankService rankRuleService;
    @Autowired
    private IncentRankedService rankedService;
    @Autowired
    private MemberAppleService memberAppleService;
    @Autowired
    private IGameProxy gameProxy;
    @Autowired
    private BasActionService actionService;
    @Autowired
    private IncentAccountCoinDaoService accountCoinService;
    @Autowired
    private ScoreLogService scoreLogService;
    @Autowired
    private MemberFollowerService followService;
    @Autowired
    private IPKNoService iPKNoService;
    @Autowired
    private MemberLoginedStatisticsService memberLoginedStatisticsService;
    @Autowired
    private FungoCacheMember fungoCacheMember;
    @Autowired
    private IMemberIncentRiskService iMemberIncentRiskService;
    @Autowired
    private MemberInfoDao memberInfoDao;

    //用户成长业务
    @Resource(name = "memberIncentDoTaskFacadeServiceImpl")
    private IMemberIncentDoTaskFacadeService iMemberIncentDoTaskFacadeService;

    @Autowired

    private IncentAccountCoinDaoService incentAccountCoinDaoService;

    @Autowired
    private ICommunityProxyService communityProxyService;

    @Autowired
    private SysMockuserDao sysMockuserDao;


    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public ResultDto<LoginMemberBean> bindingPhoneNo(String code, String mobile, String token) {
        ResultDto<LoginMemberBean> re1 = new ResultDto<LoginMemberBean>();
        Member member = memberService.selectOne(new EntityWrapper<Member>().eq("sesion_token", token));
        if (member == null) {
            return ResultDto.error("13", "用户不存在");
        }
        ResultDto<String> re = messageCodeService.checkCode(GameConstant.MSG_TYPE_USER_LOGIN, mobile, code);
        if (re.isSuccess()) {
            Member tem = memberService.selectOne(new EntityWrapper<Member>().eq("mobile_phone_num", mobile));
            if (tem == null) {
                member.setSesionToken("");
                member.setMobilePhoneNum(mobile);
                member.setMobilePhoneVerified("1");
                member.updateById();
                LoginMemberBean bean = new LoginMemberBean();
                bean.setCreatedAt(DateTools.fmtDate(member.getCreatedAt()));
                bean.setUpdatedAt(DateTools.fmtDate(member.getUpdatedAt()));
                bean.setEmailVerified(false);
                bean.setMobilePhoneNumber(member.getMobilePhoneNum());
                bean.setUsername(member.getUserName());
                bean.setMobilePhoneVerified(member.getMobilePhoneVerified().equals("1") ? true : false);
                bean.setObjectId(member.getId());
                re1.setData(bean);
                return re1;
            } else {
                return ResultDto.error("13", "手机号已经存在");
            }
        } else {
            return ResultDto.error("13", re.getMessage());
        }
    }

    public ResultDto<LoginMemberBean> register(String mobile, String password, String code) throws Exception {
        ResultDto<LoginMemberBean> rest = new ResultDto<LoginMemberBean>();
        Member member = null;
        //校对验证码
        ResultDto<String> re = messageCodeService.checkCode(GameConstant.MSG_TYPE_USER_LOGIN, mobile, code);//验证短信
        if (re.isSuccess()) {
            member = memberService.selectOne(new EntityWrapper<Member>().eq("mobile_phone_num", mobile));
            if (member == null) {//如果第一次进行会员注册
                member = new Member();
                //set id
                member.setId(UUIDUtils.getUUID());
                member.setUserName(geUserName(mobile));
                member.setMobilePhoneNum(mobile);
                member.setMobilePhoneVerified("1");
                member.setPassword(SecurityMD5.MD5(password));
                member.setHasPassword("1");//没设置密码
                member.setLastVisit(new Date());
                member.setCreatedAt(new Date());
                member.setUpdatedAt(new Date());
                member.setComplete("0");//未完成
                member.setLevel(1);
                member.setState(0);

                //fix bug:修改会员编号出现的重复的情况 修改用户的member_no用户编号 [by mxf 2019-03-06]
                String mb_no = iPKNoService.genUniqueMbNo(member.getId());
                member.setMemberNo(mb_no);
                //end

                //保存用户数据
                memberService.insert(member);
                //fix bug:修改会员编号出现的重复的情况 修改用户的member_no用户编号 [by mxf 2019-03-06]
                //memberNoService.getMemberNoAndForUpdate(member);
                //end

                messageCodeService.updateCheckCodeSuccess(re.getData());//更新验证成功

                LoginMemberBean bean = new LoginMemberBean();
                bean.setCreatedAt(DateTools.fmtDate(member.getCreatedAt()));
                bean.setUpdatedAt(DateTools.fmtDate(member.getUpdatedAt()));
                bean.setEmailVerified(false);
                bean.setMobilePhoneNumber(member.getMobilePhoneNum());
                bean.setUsername(member.getUserName());
                bean.setMobilePhoneVerified(member.getMobilePhoneVerified().equals("1") ? true : false);
                bean.setObjectId(member.getId());
                rest.setData(bean);

                LOGGER.info("用户注册-PC-初始化数据  memberId : {}", member.getId());
                initUserRank(member.getId());
            } else {
                return ResultDto.error("211", "会员已存在");
            }
        } else {
            return ResultDto.error(re.getCode(), re.getMessage());
        }
        return rest;
    }

    public ResultDto<LoginMemberBean> loginpc(String mobile, String password, String code) {
        ResultDto<LoginMemberBean> rest = new ResultDto<LoginMemberBean>();
        Member member = null;
        member = memberService.selectOne(new EntityWrapper<Member>().eq("password", SecurityMD5.MD5(password)).eq("mobile_phone_num", mobile));
        if (member == null) {
            return ResultDto.error("210", "密码错误,请重新输入");
        }
        LoginMemberBean bean = new LoginMemberBean();
        bean.setCreatedAt(DateTools.fmtDate(member.getCreatedAt()));
        bean.setUpdatedAt(DateTools.fmtDate(member.getUpdatedAt()));
        bean.setEmailVerified(false);
        bean.setMobilePhoneNumber(member.getMobilePhoneNum());
        bean.setUsername(member.getUserName());
        bean.setMobilePhoneVerified(member.getMobilePhoneVerified().equals("1") ? true : false);
        bean.setObjectId(member.getId());
        rest.setData(bean);
        return rest;
    }

    @Override
    public ResultDto<LoginMemberBean> login(String mobile, String password, String code, String appVersion) throws Exception {
        ResultDto<LoginMemberBean> rest = new ResultDto<LoginMemberBean>();
        Member member = null;
        if (!CommonUtil.isNull(password)) {//密码登录
            member = memberService.selectOne(new EntityWrapper<Member>().eq("password", SecurityMD5.MD5(password)).eq("mobile_phone_num", mobile));
            if (member == null) {
                return ResultDto.error("210", "密码错误,请重新输入");
            }
        } else if (!CommonUtil.isNull(code)) {//手机验证码
            if (CommonUtil.isNull(mobile)) {
                return ResultDto.error("210", "手机号不能为空");
            }
            ResultDto<String> re = messageCodeService.checkCode(GameConstant.MSG_TYPE_USER_LOGIN, mobile, code);//验证短信
            if (re.isSuccess()) {
                member = memberService.selectOne(new EntityWrapper<Member>().eq("mobile_phone_num", mobile));
                if (member == null) {//如果第一次进行会员注册
                    member = new Member();

                    member.setId(UUIDUtils.getUUID());

                    member.setUserName(geUserName(mobile));
                    member.setMobilePhoneNum(mobile);
                    member.setMobilePhoneVerified("1");
                    member.setHasPassword("0");//没设置密码
                    member.setLastVisit(new Date());
                    member.setCreatedAt(new Date());
                    member.setUpdatedAt(new Date());
                    member.setComplete("0");//未完成
                    member.setLevel(1);
                    member.setState(0);

                    //fix bug:修改会员编号出现的重复的情况 修改用户的member_no用户编号 [by mxf 2019-03-06]
                    String mb_no = iPKNoService.genUniqueMbNo(member.getId());
                    member.setMemberNo(mb_no);
                    //end

                    //添加用户数据
                    memberService.insert(member);

                    //fix bug:修改会员编号出现的重复的情况 修改用户的member_no用户编号 [by mxf 2019-03-06]
                    //memberNoService.getMemberNoAndForUpdate(member);
                    //end

                    LOGGER.info("用户注册-手机验证-初始化数据  memberId : {}, phoneNumber:{}", member.getId(), mobile);
                    this.initUserRank(member.getId());
                }

                messageCodeService.updateCheckCodeSuccess(re.getData());//更新验证成功
            } else {
                return ResultDto.error(re.getCode(), re.getMessage());
            }
        } else {
            return ResultDto.error("13", "请求参数错误");
        }
        //推送设备号
//		DeviceInput dein = new DeviceInput();
//		dein.setDeviceId(deviceId);
//		dein.setPhoneModel(os);
//		pushService.bindDevice(member.getId(),dein);

        LoginMemberBean bean = new LoginMemberBean();
        bean.setCreatedAt(DateTools.fmtDate(member.getCreatedAt()));
        bean.setUpdatedAt(DateTools.fmtDate(member.getUpdatedAt()));
        bean.setEmailVerified(false);
        bean.setMobilePhoneNumber(member.getMobilePhoneNum());
        bean.setUsername(member.getUserName());
        bean.setAvatar(member.getAvatar());
        bean.setMobilePhoneVerified(member.getMobilePhoneVerified().equals("1") ? true : false);
        bean.setHas_password(member.getHasPassword().equals("1") ? true : false);
        bean.setObjectId(member.getId());
        rest.setData(bean);


        //记录登录用户
        memberLoginedStatisticsService.addLoginToBucket(member.getId(), appVersion);
        return rest;
    }

   /* private void buriedPointFun(Member member, String s) {
        Map<String, String> buriedpointmap = new HashMap<>();
        buriedpointmap.put("distinctId", member.getId());
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        buriedpointmap.put("platForm", request.getHeader("os"));
        buriedpointmap.put("login001", "手机号");
        buriedpointmap.put("login002", s);
//                  手机登录埋点事件ID:login005
        BuriedPointUtils.login05(buriedpointmap, analysysJavaSdk);
    }*/

    @Override
    public ResultDto<String> smscode(String mobile) throws Exception {
        String code = RandomsAndId.getRandomNum(6);
        ResultDto<String> re = messageCodeService.sendCode(GameConstant.MSG_TYPE_USER_LOGIN, mobile, code, GameConstant.MSG_VALIDITY_MINUTE);
        return re;
    }

    @Override
    public ResultDto<String> verifymobile(String code, String mobile) {
        return messageCodeService.checkCodeAndSuccess(GameConstant.MSG_TYPE_USER_LOGIN, mobile, code);
    }

    @Override
    public ResultDto<MobileusableBean> mobileusable(String mobile) {
        Member member = memberService.selectOne(new EntityWrapper<Member>().eq("mobile_phone_num", mobile));
        ResultDto<MobileusableBean> re = new ResultDto<MobileusableBean>();
        MobileusableBean bean = new MobileusableBean();
        if (member == null) {
            bean.setIs_register(false);
            bean.setHas_password(false);
        } else {

            //验证该手机号的账户是否被冻结
            //状态 :0正常，1:锁定，2：禁止 -1:删除
            Integer memberState = member.getState();
            if (1 == memberState || 2 == memberState || -1 == memberState) {
                re.setStatus(-1);
                re.setMessage(MemberLoginConsts.MEMBER_REG_MOBILEUSABLE_TIPS);
                re.setData(bean);
                return re;
            } else {
                bean.setIs_register(true);
                member.getHasPassword();
                bean.setHas_password(member.getHasPassword().equals("1") ? true : false);
            }
        }
        re.setData(bean);
        return re;
    }

    /**作废*/
    @Override
    public void forgotpassword(String code, String mobile) {
        messageCodeService.checkCode(GameConstant.MSG_TYPE_USER_LOGIN, mobile, code);

    }

    /******************************************************************************/
    @Override
    public ResultDto<String> updatepassword(String memberId, String oldPassword, String newPassword) {
        Member member = this.memberService.selectById(memberId);
        if (member == null) {
            return ResultDto.error("211", "找不到用户");
        }
        if (SecurityMD5.MD5(oldPassword).equals(member.getPassword())) {
            member.setPassword(SecurityMD5.MD5(newPassword));
            member.updateById();
        } else {
            return ResultDto.error("211", "找不到用户");
        }
        return ResultDto.success();
    }

    @Override
    public ResultDto<String> logon(String memberId) {
        Member member = this.memberService.selectById(memberId);
        if (member == null) {
            return ResultDto.error("", "");
        }
        member.setSesionToken("");
        member.updateById();
        return null;
    }

    @Override
    public ResultDto<String> updateMobile(String memberId, String code, String mobile) {
        Member member = this.memberService.selectById(memberId);
        if (member == null) {
            return ResultDto.error("211", "用户不存在");
        }

        ResultDto<String> re = this.messageCodeService.checkCodeAndSuccess(GameConstant.MSG_TYPE_USER_LOGIN, mobile, code);
        if (re.isSuccess()) {
            member.setMobilePhoneNum(mobile);
            member.updateById();

            //clear cache
            // 个人资料
            fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + memberId, "", null);
            return ResultDto.success();
        } else {
            return re;
        }
    }

    public ResultDto<String> updateNewpassword(String memberId, String password) {
        Member member = this.memberService.selectById(memberId);
        if (member == null) {
            return ResultDto.error("211", "用户不存在");
        }
        member.setPassword(SecurityMD5.MD5(password));
        member.setHasPassword("1");
        member.updateById();

        //clear cache
        // 个人资料
        fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + memberId, "", null);
        return ResultDto.success();
    }


    @Override
    public ResultDto<Map<String, String>> verifyMobile(String memberId, String type, String code, String mobile, String password) {
        ResultDto<Map<String, String>> res = new ResultDto<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        res.setData(map);

        Member member = this.memberService.selectById(memberId);
        if (member == null) {
            return ResultDto.error("211", "用户不存在");
        }
        if ("1".equals(type)) {
            ResultDto<String> re = this.messageCodeService.checkCodeAndSuccess(GameConstant.MSG_TYPE_USER_LOGIN, member.getMobilePhoneNum(), code);
            if (re.isSuccess()) {
                String token = RandomsAndId.getUUID();
                member.setSesionToken(token);
                member.updateById();
                map.put("token", token);
                return res;
            } else {
                return ResultDto.error(re.getCode(), re.getMessage());
            }
        } else if ("0".equals(type)) {
            if (SecurityMD5.MD5(password).equals(member.getPassword())) {
                String token = RandomsAndId.getUUID();
                member.setSesionToken(token);
                member.updateById();
                map.put("token", token);
                return res;
            } else {
                return ResultDto.error("211", "用户名密码错误");
            }
        }
        return ResultDto.error("211", "用户不存在");
    }

    @Override
    public ResultDto<String> updateNewpasswordByToken(String memberId, String token, String password) {
        Member member = this.memberService.selectById(memberId);
        if (member == null) {
            return ResultDto.error("211", "用户不存在");
        }
        if ("".equals(token) || token == null) {
            member.setPassword(SecurityMD5.MD5(password));
            member.setHasPassword("1");
            member.updateById();
            return ResultDto.success();
        } else if (member.getSesionToken().equals(token)) {
            member.setPassword(SecurityMD5.MD5(password));
            member.setHasPassword("1");
            member.updateById();

            //clear cache
            // 个人资料
            fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + memberId, "", null);

            return ResultDto.success();
        } else {
            return ResultDto.error("211", "用户不存在");
        }
    }

    private String geUserName(String mobile) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String dateString = formatter.format(currentTime);
        String tem = mobile.substring(0, 4);
        return dateString + tem;
    }

    @Override
    public AuthorBean getAuthor(String memberId) {
        Member member = memberService.selectById(memberId);
        if (member == null) {
            return null;
        }
        AuthorBean author = null;
        if (member != null) {
            author = new AuthorBean();

            author.setAvatar(member.getAvatar());
            author.setCreatedAt(DateTools.fmtDate(member.getCreatedAt()));

            author.setUpdatedAt(DateTools.fmtDate(member.getUpdatedAt()));
            author.setUsername(member.getUserName());
            author.setObjectId(member.getId());
//			author.setFolloweeNum(member.getFolloweeNum());
//			author.setFollowerNum(member.getFollowerNum());
            int followed = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 5).ne("state", "-1").eq("target_id", memberId));
            author.setFolloweeNum(followed);//粉丝数
            int follower = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 5).ne("state", "-1").eq("target_type", 0).eq("member_id", memberId));
            author.setFollowerNum(follower);//关注数

            author.setMemberNo(member.getMemberNo());
            author.setSign(member.getSign());
//			author.setFunImg(Setting.FUM_IMAGE);
            author.setLastestHonorName("FunGo身份证");

            author.setGender(member.getGender());
            ObjectMapper mapper = new ObjectMapper();

            //荣誉,身份图片
            List<IncentRanked> list = rankedService.selectList(new EntityWrapper<IncentRanked>().eq("mb_id", member.getId()));
            for (IncentRanked ranked : list) {
                try {
                    if (ranked.getRankType() == 1) {//等级
                        IncentRuleRank rank = rankRuleService.selectById(ranked.getCurrentRankId());//最近获得
                        author.setLevel(ranked.getCurrentRankId().intValue());
                        String rankImgs = rank.getRankImgs();
                        ArrayList<HashMap<String, Object>> urlList = mapper.readValue(rankImgs, ArrayList.class);
                        author.setDignityImg((String) urlList.get(0).get("url"));
                    } else if (ranked.getRankType() == 2) {//身份
                        IncentRuleRank rank = rankRuleService.selectById(ranked.getCurrentRankId());//最近获得
                        String rankImgs = rank.getRankImgs();
                        ArrayList<HashMap<String, Object>> urlList = mapper.readValue(rankImgs, ArrayList.class);
                        author.setStatusImg(urlList);
                    } else if (ranked.getRankType() == 3) {//成就

                        //找出获得的荣誉合集
                        String rankIdtIds = ranked.getRankIdtIds();
                        ArrayList<HashMap<String, Object>> rankList = mapper.readValue(rankIdtIds, ArrayList.class);
                        Collections.reverse(rankList);
                        List<String> honorImgList = new ArrayList<>();
                        int i = 0;

                        ArrayList<String> groupIdList = new ArrayList<>();
                        //取前三位
                        for (HashMap<String, Object> map : rankList) {
                            IncentRuleRank rank = rankRuleService.selectById(Long.parseLong(map.get("1") + ""));

                            //取同一勋章中等级最高的那一个
                            if (!groupIdList.contains(rank.getRankGroupId())) {
                                groupIdList.add(rank.getRankGroupId());

                                if (rank != null) {
//								String honorName = getHonorName(rank.getRankGroupId()) + rank.getRankName();
                                    ArrayList<HashMap<String, Object>> urlkList = mapper.readValue(rank.getRankImgs(), ArrayList.class);
                                    honorImgList.add((String) urlkList.get(0).get("url"));
                                }
                                if (i == 0) {
//								author.setLastestHonorName(getHonorName(rank.getRankGroupId()) +"-"+rank.getRankName());
                                    author.setLastestHonorName(getHonorName(rank.getRankGroupId()) + rank.getRankName());
                                }
                                i++;
                                if (i > 2) {
                                    break;
                                }
                            }

                        }
//						if(honorImgList.size() < 3) {
//							honorImgList.add(Setting.FUM_IMAGE);
//						}

                        author.setHonorImgList(honorImgList);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
//			if(CommonUtil.isNull(author.getDignityImg())) {
//				author.setDignityImg(Setting.LEVEL_ONE_IMAGE);
//			}

        }
        return author;
    }

    @Override
    public ResultDto<String> addAppleId(String loginId, AppleInputBean msg) throws Exception {
        MemberApple ma = this.memberAppleService.selectOne(new EntityWrapper<MemberApple>().eq("member_id", loginId));
        if (ma == null) {
            MemberApple apple = new MemberApple();
            apple.setAppleId(msg.getAppleId());
            apple.setCreatedAt(new Date());
            apple.setMemberId(loginId);
            apple.setName(msg.getName());
            apple.setSurname(msg.getSurname());
            apple.setUpdatedAt(new Date());
            apple.insert();

            //V2.4.6版本之前任务
            //gameProxy.addTaskCore(Setting.ACTION_TYPE_BIND_APPLE, loginId, "", -1);

        } else {
            ma.setAppleId(msg.getAppleId());
            ma.setMemberId(loginId);
            ma.setName(msg.getName());
            ma.setSurname(msg.getSurname());
            ma.setUpdatedAt(new Date());
            ma.updateById();
        }
        return ResultDto.success();
    }

    @Override
    public ResultDto<String> editUser(String memberId, UserBean msg) throws Exception {

        Member member = this.memberService.selectById(memberId);

        Member newMemeber = new Member();
        newMemeber.setId(member.getId());
        newMemeber.setGender(msg.getGender());

        if (!CommonUtil.isNull(msg.getUser_name()) && !member.getUserName().equals(msg.getUser_name())) {//昵称

            //转码表情符号
            String usereName = msg.getUser_name();
            newMemeber.setUserName(usereName);

            //V2.4.6版本之前任务
            //gameProxy.addTaskCore(Setting.ACTION_TYPE_NICKNAME, memberId, "", -1);

            //V2.4.6版本任务
            //1 经验值
            iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                    MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_UPATE_NICKNAME_EXP.code());

            //2 fungo币
            iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                    MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_UPATE_NICKNAME_COIN.code());

        }
//		if(!CommonUtil.isNull(msg.getSign()) && !member.getSign().equals(msg.getSign())) {//简介
//			member.setSign(msg.getSign());
//			gameProxy.addTaskCore(Setting.ACTION_TYPE_INTRO, memberId, "", -1);
//		}
        if (null != msg.getSign() &&!"".equals(msg.getSign().trim())&& !member.getSign().equals(msg.getSign())) {//简介
            newMemeber.setSign(msg.getSign());

            //V2.4.6版本之前任务
            //gameProxy.addTaskCore(Setting.ACTION_TYPE_INTRO, memberId, "", -1);

            //V2.4.6版本任务
            //1 经验值
            iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                    MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_UPATE_RESUME_EXP.code());

            //2 fungo币
            iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                    MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_UPATE_RESUME_COIN.code());

        }

        newMemeber.updateById();

        //clear redis
        //其他会员信息接口 清除
        String keyPrefix_userCard = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_CARD + memberId;
        fungoCacheMember.excIndexCache(false, keyPrefix_userCard, "", null);

        //清除 会员信息（web端）
        fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_WEBIINFO + memberId, "", null);
        // 个人资料
        fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + memberId, "", null);
        // 个人等级
        fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_RANKS_LEVEL + memberId, "", null);
        return ResultDto.success("  修改成功  ");
    }

    @Override
    public ResultDto<MemberOutBean> getUserInfo(String memberId) throws Exception {

        ResultDto<MemberOutBean> re = new ResultDto<MemberOutBean>();

        MemberOutBean bean = null;
        //from redis
        bean = (MemberOutBean) fungoCacheMember.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + memberId, "");
        if (null != bean) {
            re.setData(bean);
            return re;
        }

        Member member = this.memberService.selectById(memberId);
        if (member == null) {
            return ResultDto.error("-1", "找不到用户");
        }
        bean = new MemberOutBean();


        bean.setAuthData(new String());
        bean.setAvatar(member.getAvatar());
        bean.setCreatedAt(DateTools.fmtDate(member.getCreatedAt()));
        bean.setEmailVerified(member.getEmailVerified().equals("1") ? true : false);
        bean.setExp(Float.valueOf(member.getExp()));

        int followed = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 5).eq("state", "0").eq("target_id", memberId));
        bean.setFollowee_num(followed);//粉丝
        int follower = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 5).eq("state", "0").eq("target_type", 0).eq("member_id", memberId));
        bean.setFollower_num(follower);//关注

        bean.setGender(member.getGender());
        bean.setHas_password(member.getHasPassword().equals("1") ? true : false);
        bean.setImportFromParse(member.getImportFromParse().equals("1") ? true : false);
        bean.setLevel(member.getLevel());
        bean.setMobilePhoneNumber(member.getMobilePhoneNum());
        bean.setMobilePhoneVerified(member.getMobilePhoneVerified().equals("1") ? true : false);
        bean.setObjectId(member.getId());
        bean.setPublish_num(member.getPublishNum());
        bean.setReport_num(member.getReportNum());
        bean.setSign(member.getSign());
        bean.setState(member.getState());
        bean.setUpdatedAt(DateTools.fmtDate(member.getUpdatedAt()));
        bean.setUsername(member.getUserName());
        bean.setMemberNo(member.getMemberNo());
        bean.setMb_id(member.getId());

        IncentAccountCoin accountCoin = accountCoinService.selectOne(new EntityWrapper<IncentAccountCoin>().eq("mb_id", memberId));
        //.eq("account_group_id", 3));
        if (accountCoin == null) {
            bean.setFunCoin(0);
        } else {
            bean.setFunCoin(accountCoin.getCoinUsable().longValue());
        }

        bean.setFunImg(Setting.FUM_IMAGE);

        ObjectMapper mapper = new ObjectMapper();

        //荣誉,身份图片
        List<IncentRanked> list = rankedService.selectList(new EntityWrapper<IncentRanked>().eq("mb_id", member.getId()));
        for (IncentRanked ranked : list) {
            if (ranked.getRankType() == 1) {
                IncentRuleRank rank = rankRuleService.selectById(ranked.getCurrentRankId());//最近获得
                bean.setLevel(ranked.getCurrentRankId().intValue());
                String rankImgs = rank.getRankImgs();
                ArrayList<HashMap<String, Object>> urlList = mapper.readValue(rankImgs, ArrayList.class);
                bean.setDignityImg((String) urlList.get(0).get("url"));
            } else if (ranked.getRankType() == 2) {
                IncentRuleRank rank = rankRuleService.selectById(ranked.getCurrentRankId());//最近获得
                String rankImgs = rank.getRankImgs();
                ArrayList<HashMap<String, Object>> urlList = mapper.readValue(rankImgs, ArrayList.class);
                bean.setStatusImg(urlList);
            } else if (ranked.getRankType() == 3) {

                //找出获得的荣誉合集
                String rankIdtIds = ranked.getRankIdtIds();
                ArrayList<HashMap<String, Object>> rankList = mapper.readValue(rankIdtIds, ArrayList.class);
                Collections.reverse(rankList);

                List<String> honorImgList = new ArrayList<>();
                int i = 0;
                ArrayList<String> groupIdList = new ArrayList<>();

                //取前三位
                for (HashMap<String, Object> map : rankList) {
                    IncentRuleRank rank = rankRuleService.selectById(Long.parseLong(map.get("1") + ""));

                    //同一荣誉取等级最高的一个
                    if (!groupIdList.contains(rank.getRankGroupId())) {
                        groupIdList.add(rank.getRankGroupId());

                        if (rank != null) {
                            ArrayList<HashMap<String, Object>> urlkList = mapper.readValue(rank.getRankImgs(), ArrayList.class);
                            honorImgList.add((String) urlkList.get(0).get("url"));
                        }
                        i++;
                        if (i > 2) {
                            break;
                        }
                    }
                }
                bean.setHonorImgList(honorImgList);
            }
        }
        if (CommonUtil.isNull(bean.getDignityImg())) {
            bean.setDignityImg(Setting.LEVEL_ONE_IMAGE);
        }
//		bean.getHonorImgList().add(Setting.FUM_IMAGE)
        //bean.setHonorImgList(honorImgList);

        re.setData(bean);

        //redis cache
        fungoCacheMember.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + memberId, "", bean);

        return re;
    }

    @Override
    public ResultDto<Map<String, String>> uploadAvatar(String loginId, MultipartFile appFile) {
        return null;

    }

    /**
     * 根据荣誉分组id获得荣誉分组名称
     * @param rank_group_id
     * @return
     */
    public String getHonorName(String rank_group_id) {
        int id = Integer.parseInt(rank_group_id);
        if (id == 7) {
            return "";
        } else if (id == 8) {
            return "会心一击";
        } else if (id == 9) {
            return "拓荒者";
        } else if (id == 10) {
            return "神之手";
        } else if (id == 11) {
            return "Fun之意志";
        } else if (id == 12) {
            return "";
//			return "专属活动";
        }
        return "";
    }

    @Override
    public AuthorBean getUserCard(String cardId, String memberId) {

        AuthorBean author = null;
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_CARD + cardId;
        author = (AuthorBean) fungoCacheMember.getIndexCache(keyPrefix, memberId);
        if (null != author) {
            return author;
        }
        author = this.getAuthor(cardId);
        if (!CommonUtil.isNull(memberId)) {
//			BasAction action=actionService.selectOne(new EntityWrapper<BasAction>().eq("type", "5").eq("member_id",memberId).eq("target_id", cardId).notIn("state", "-1"));
            MemberFollower one = followService.selectOne(new EntityWrapper<MemberFollower>().eq("member_id", memberId).eq("follower_id", cardId).andNew("state = {0}", 1).or("state = {0}", 2));
            if (one != null) {
                author.setIs_followed(true);
                if(one.getState().equals(2)){
                    author.setMutualFollowed("1");
                }
            }
        }

        //redis cache
        fungoCacheMember.excIndexCache(true, keyPrefix, memberId, author,30);

        return author;
    }

    @Override
    public List<HashMap<String, Object>> getStatusImage(String memberId) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        IncentRanked ranked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", memberId).eq("rank_type", 2));
        if (ranked != null) {
            IncentRuleRank rank = rankRuleService.selectById(ranked.getCurrentRankId());//最近获得
            if (rank != null) {
                String rankinf = rank.getRankImgs();
                ArrayList<HashMap<String, Object>> infolist = mapper.readValue(rankinf, ArrayList.class);
                return infolist;
            } else {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }

    }


    //用户注册 初始化数据(Fungo身份证、等级)
    @Override
    public void initUserRank(String memberId) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            scoreLogService.updateRanked(memberId, objectMapper, 1);
            scoreLogService.updateRanked(memberId, objectMapper, 24);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("用户注册-初始化数据失败 memberId: {}", memberId, e);
            throw new BusinessException("-1", "初始化注册数据失败!");
        }
    }


    @Override
    public ResultDto updateUserRegister(String userId, String registerChannel, String registerPlatform) {
        EntityWrapper<Member> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("id",userId);
        Member member = new Member();
        member.setRegisterChannel(registerChannel);
        member.setRegisterPlatform(registerPlatform);
        boolean update = memberService.update(member, entityWrapper);
        if(update){
            return ResultDto.success();
        }
       return ResultDto.error("-1","更新用户注册信息失败");
    }


    @Override
    public ResultDto<MemberBuriedPointBean> getBuriedPointUserProperties(String loginId) {
        Member member = memberService.selectById(loginId);
        if(member == null){
            return ResultDto.error("-1","未查询到用户信息");
        }
        MemberBuriedPointBean memberBuriedPointBean = new MemberBuriedPointBean();
        // 用户基本属性封装
        memberBuriedPointBean.setAge(null);
        memberBuriedPointBean.setFansCount(member.getFolloweeNum());
        memberBuriedPointBean.setFirstPlatform(member.getRegisterPlatform());
        memberBuriedPointBean.setFirstSource(member.getRegisterChannel());
        memberBuriedPointBean.setGender(member.getGender());
        memberBuriedPointBean.setRegisterDate(member.getCreatedAt().getTime());
        memberBuriedPointBean.setFollowCount(member.getFollowerNum());
        memberBuriedPointBean.setExp(member.getExp());
        memberBuriedPointBean.setUserLevel(member.getLevel());

        // 非用户基本属性封装
        // 账户余额
        EntityWrapper<IncentAccountCoin> incentAccountCoinEntityWrapper = new EntityWrapper<>();
        incentAccountCoinEntityWrapper.eq("mb_id",loginId);
        IncentAccountCoin incentAccountCoin = incentAccountCoinDaoService.selectOne(incentAccountCoinEntityWrapper);
        if(incentAccountCoin == null){
            memberBuriedPointBean.setBalance(0);
        }else{
            memberBuriedPointBean.setBalance(incentAccountCoin.getCoinUsable().intValue());
        }

        //下载记录
        int downCount = actionService.selectCount(new EntityWrapper<BasAction>()
                .eq("member_id", loginId)
                .eq("type", Setting.ACTION_TYPE_DOWNLOAD));
        memberBuriedPointBean.setGameDownloadCount(downCount);

        // 用户是否完成新手任务
        ResultDto<Map<String, Object>> mapResultDto = iMemberIncentRiskService.checkeUnfinshedNoviceTask(loginId, null);
        if(mapResultDto.isSuccess()&&mapResultDto.getData()!=null){
            Map<String, Object> data = mapResultDto.getData();
            Object hasNotiveTask = data.get("hasNotiveTask");
            if(hasNotiveTask instanceof Boolean){
                memberBuriedPointBean.setHasCompletedCourse(!(Boolean)hasNotiveTask);
            }else{
                memberBuriedPointBean.setHasCompletedCourse(false);
            }
        }else{
            // 容错 当未完成处理
            memberBuriedPointBean.setHasCompletedCourse(false);
        }

        // 用户类型 - 有身份标识，且标识是鉴游师身份的
        memberBuriedPointBean.setUserType("普通用户");
        IncentRanked incentRanked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", member.getId()).eq("rank_type",2));
        if(incentRanked!=null){
            IncentRuleRank rank = rankRuleService.selectById(incentRanked.getCurrentRankId());
            if(rank.getRankGroupId().equals("4")){
                memberBuriedPointBean.setUserType("鉴游师");
            }
        }

        // 文章和心情数量 - 微服务
        Map<String, Integer> countMap = communityProxyService.countMoodAndPost(loginId);
        memberBuriedPointBean.setArticlePublished(countMap.get("articlePublished"));
        memberBuriedPointBean.setMoodPublished(countMap.get("moodPublished"));
        return ResultDto.success(memberBuriedPointBean);

    }

    @Transactional()
    @Override
    public ResultDto<Member> addVirtualUser(AdminUserInputDTO input, String adminId) throws Exception {
        if (input == null) {
            return ResultDto.error("211", "没有添加的内容");
        }
        if (memberService.selectOne(new EntityWrapper<Member>().eq("user_name", input.getUser_name())) != null) {
            return ResultDto.error("211", "用户名已存在");
        }
        Member user = new Member();
        try {
            user.setUserName(input.getUser_name());
            user.setGender(input.getGender());
            user.setStatus(2);
            user.setSign(input.getSign());
            user.setAvatar(input.getAvatar());
            user.setPassword(SecurityMD5.MD5(input.getPassword()));
            Date date = new Date();
            user.setCreatedAt(date);
            user.setUpdatedAt(date);

            //设置用户编号
            //fix bug:修改会员编号出现的重复的情况 修改用户的member_no用户编号 [by mxf 2019-03-06]
            String mb_no = getMbNo();
            user.setMemberNo(mb_no);
            //
            Random ran = new Random();
            user.setMobilePhoneNum(input.getMobilePhoneNum());
            user.setLevel( input.getLevel());
            user.setHasPassword( input.getHasPassword());
            //添加用户数据
            boolean flag = user.insert();
            //fix bug:修改会员编号出现的重复的情况 修改用户的member_no用户编号 [by mxf 2019-03-06]
            //memberNoService.getMemberNoAndForUpdate(user);
            //end

            if (flag == false) {
                throw new BusinessException("-1", "操作失败");
            }

            SysMockuser mock = new SysMockuser();
            mock.setId(UUIDUtils.getUUID());
            mock.setMemberId(user.getId());
            mock.setAdminId(adminId);
            mock.setCreatedAt(date);
            mock.setUpdatedAt(date);
            Integer insert =  sysMockuserDao.insert( mock );
//            boolean insert = mock.insert();
            if (insert != 1) {
                throw new BusinessException("-1", "操作失败");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            scoreLogService.updateRanked(user.getId(), objectMapper, 1);
            scoreLogService.updateRanked(user.getId(), objectMapper, 24);
        } catch (RuntimeException e) {
			LOGGER.error("用户注册-初始化数据失败 memberId : {}",e);
//            throw new BusinessException("-1", "初始化注册数据失败!");
            throw new RuntimeException("初始化注册数据失败");
        }
        return ResultDto.success(user);
    }

    /**
     * 功能描述:  用户分享中秋活动
     * @param: [adminId]
     * @return: com.game.common.dto.ResultDto<com.fungo.system.entity.Member>
     * @auther: dl.zhang
     * @date: 2019/8/26 15:53
     */
    @Transactional
    @Override
    public ResultDto<String> userShareMall(String adminId) throws Exception {
        ResultDto<String> resultDto = null;
        try {
            MemberInfo memberInfo = new MemberInfo();
            memberInfo.setMdId(adminId);
            memberInfo.setShareType(1);
            memberInfo = memberInfoDao.selectOne( memberInfo);
            if(memberInfo != null){
                resultDto = ResultDto.ResultDtoFactory.buildSuccess( AbstractResultEnum.CODE_SYSTEM_FESTIVAL_EIGHT.getKey(),AbstractResultEnum.CODE_SYSTEM_FESTIVAL_EIGHT.getSuccessValue());
                resultDto.setShowState(-1);
                return resultDto;

            }
            memberInfo = new MemberInfo();
            memberInfo.setMdId(adminId);
            memberInfo.setShareType(1);
            memberInfo.setIsactive("1");
            memberInfo.setRversion(1);
            memberInfo.setCreatedBy(adminId);
            memberInfo.setCreatedAt(new Date());
            memberInfo.setUpdatedBy(adminId);
            memberInfo.setUpdatedAt(new Date());
            memberInfo.setDescription("用户分享中秋活动成功");
            memberInfoDao.insert(memberInfo);
            resultDto = ResultDto.ResultDtoFactory.buildSuccess(  "分享成功，已获得1次免费抽奖次数，快去抽奖吧!");
        }catch (Exception e){
            LOGGER.error("用户分享中秋活动",e);
            resultDto = ResultDto.ResultDtoFactory.buildError(  "用户分享中秋活动失败,请联系管理员");
        }
        return resultDto;
    }

    /**
     *  获取mb_no
     * @return
     */
    private String getMbNo() {
        long longPK = PKUtil.getInstance().longPK();
        String longPKStr = String.valueOf(longPK);
        //截取2位年2位月
        String idYearMonth = StringUtils.substring(longPKStr, 2, 6);
//        PC2.0需求变更 member_no改为8位
//        2019-06-10
//        lyc
        idYearMonth = idYearMonth.substring(1, 2) + idYearMonth.substring(3);
        //默认是2位年2位月 + 后6位

        String grapNo = StringUtils.substring(longPKStr, longPKStr.length() - 6);
        return idYearMonth + grapNo;
    }

}
