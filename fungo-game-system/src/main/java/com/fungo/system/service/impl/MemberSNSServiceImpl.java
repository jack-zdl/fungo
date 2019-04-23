
package com.fungo.system.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.dto.MemberSNSBindInput;
import com.fungo.system.dto.MemberSNSBindOutput;
import com.fungo.system.entity.Member;
import com.fungo.system.helper.ThirdPartyLoginHelper;
import com.fungo.system.service.IMemberIncentDoTaskFacadeService;
import com.fungo.system.service.IMemberSNSService;
import com.fungo.system.service.MemberService;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.consts.MemberIncentTaskConsts;
import com.game.common.dto.ResultDto;
import com.game.common.enums.AccountEnum;
import com.game.common.enums.FunGoIncentTaskV246Enum;
import com.game.common.repo.cache.facade.FungoCacheMember;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MemberSNSServiceImpl implements IMemberSNSService {

    private static final Logger logger = LoggerFactory.getLogger(MemberSNSServiceImpl.class);


    @Autowired
    private MemberService memberService;

    @Autowired
    private FungoCacheMember fungoCacheMember;

    //@Autowired
    //private IGameProxy gameProxy;

    //用户成长业务
    @Resource(name = "memberIncentDoTaskFacadeServiceImpl")
    private IMemberIncentDoTaskFacadeService iMemberIncentDoTaskFacadeService;


    @Transactional
    @Override
    public ResultDto<MemberSNSBindOutput> bindThirdSNSWithLogged(String mb_id, MemberSNSBindInput bindInput) throws Exception {

        logger.info("--bindThirdSNSWithLogged-mb_id{}:----bindInput:{}", mb_id, JSON.toJSONString(bindInput));

        ResultDto<MemberSNSBindOutput> resultDto = null;
        try {
            //获取用户数据
            Member memberDetail = getMemberWithMbId(mb_id);

            /**
             * bindAction
             * 用户SNS平台绑定操作类型：
             *  -1 正常绑定 (默认)
             *  1 注销原账号 ,把当前手机号与当前第三方账号绑定
             *  2 重新绑定，当前第三方账号与其他手机号取消绑定
             *
             */
            Integer bindAction = bindInput.getBindAction();
            //-1 正常绑定 (默认)
            if (-1 == bindAction.intValue()) {

                //1.先验证该用户是否绑定当前SNS平台账号
                boolean isBinded = isBinded(memberDetail, bindInput);
                if (isBinded) {
                    return ResultDto.error("-1", "已经绑定第三方信息,如需重新绑定请先解绑");
                } else {
                    resultDto = new ResultDto<MemberSNSBindOutput>();
                    //2.若当前fungo账号没有绑定当前SNS平台账号，则执行绑定
                    //  a.先验证 当前SNS平台账号，是否已经在fungo平台被创建账号,且未绑定手机号
                    boolean isReged = isRegistered(mb_id, bindInput);
                    //已经注册了其他账号
                    if (isReged) {
                        MemberSNSBindOutput bindOutput = new MemberSNSBindOutput();
                        bindOutput.setBindState(2);
                        bindOutput.setSnsType(bindInput.getSnsType());
                        resultDto.setData(bindOutput);
                        resultDto.setStatus(1);
                        resultDto.setMessage("检测到您的" + AccountEnum.getValue(bindInput.getSnsType()) + "已在 FunGo 有账号，为避免账号数据⽆法同步," +
                                "若您继续绑定该 " + AccountEnum.getValue(bindInput.getSnsType()) + "，则原 FunGo 账号将被注销");
                        return resultDto;
                    }

                    // b.再验证，当前SNS平台账号，是否已经在Fungo平台创建账号，且已绑定手机号
                    String isRegedAndHasPhone = isRegisteredAndHasPhone(mb_id, bindInput);
                    if (StringUtils.isNotBlank(isRegedAndHasPhone)) {

                        MemberSNSBindOutput bindOutput = new MemberSNSBindOutput();

                        bindOutput.setBindState(3);
                        bindOutput.setSnsType(bindInput.getSnsType());
                        bindOutput.setPhones(isRegedAndHasPhone);
                        resultDto.setStatus(1);

                        resultDto.setData(bindOutput);
                        resultDto.setMessage("检测到您的" + AccountEnum.getValue(bindInput.getSnsType()) + "已绑定" + isRegedAndHasPhone + "手机,若您继续绑定，则与原⼿机解绑。");
                        return resultDto;
                    }

                    //该SNS id 没有被注册为fungo账号，则继续执行绑定
                    //实施SNS绑定
                    resultDto = bindSNS(mb_id, memberDetail, bindInput);

                }
                //1 注销原账号 ,把当前账号与当前SNS账号绑定
            } else if (1 == bindAction.intValue()) {
                //执行逻辑删除原账号
                deleteOldMemberAccount(mb_id, bindInput);
                //继续SNS绑定
                resultDto = bindSNS(mb_id, memberDetail, bindInput);

                //2 重新绑定，当前第三方账号与其他账号取消绑定
            } else if (2 == bindAction.intValue()) {
                unBindOldMemberAccount(mb_id, bindInput);
                //继续SNS绑定
                resultDto = bindSNS(mb_id, memberDetail, bindInput);
            }

        } catch (Exception ex) {
            logger.error("绑第三方SNS平台账号出现异常", ex);
            resultDto = ResultDto.error("-1", "服务器非常繁忙，请耐心等一下");
            throw ex;
        }

        logger.info("--bindThirdSNSWithLogged-resultDto: {}", JSON.toJSONString(resultDto));
        return resultDto;
    }


    /**
     * 该SNS已被其他手机号认证成功的fungo账号绑定，解绑原fungo账号
     */
    private void unBindOldMemberAccount(String mb_id, MemberSNSBindInput bindInput) {
        //从 redis 缓存取
        Integer snsType = bindInput.getSnsType();
        List<Member> memberList = (List<Member>) fungoCacheMember.getIndexCache("isRegisteredAndHasPhone" + mb_id, bindInput.getUnionid());
        if (null == memberList || memberList.isEmpty()) {
            //从DB查

            String queryColumn = "";
            String snsId = bindInput.getUnionid();
            switch (snsType) {
                //微博
                case 0:
                    queryColumn = "weibo_open_id";
                    break;
                //微信
                case 1:
                    queryColumn = "weixin_open_id";
                    break;
                //QQ
                case 4:
                    queryColumn = "qq_open_id";
                    break;
            }

            EntityWrapper<Member> memberEntityWrapper = new EntityWrapper<Member>();
            memberEntityWrapper.eq(queryColumn, snsId).and("state != {0}", -1).isNotNull("mobile_phone_num");
            memberList = memberService.selectList(memberEntityWrapper);
        }
        //把原fungo账号关联的sns字段置为""
        if (null != memberList && !memberList.isEmpty()) {

            for (Member member : memberList) {

                //Member udpateMb = new Member();
                //udpateMb.setId(member.getId());
                switch (snsType) {
                    //微博
                    case 0:
                        member.setWeiboOpenId("");
                        break;
                    //微信
                    case 1:
                        member.setWeixinOpenId("");
                        break;
                    //QQ
                    case 4:
                        member.setQqOpenId("");
                        break;
                }
                //把账号状态修改为-1，删除状态
                //udpateMb.setState(-1);
                //memberService.updateById(udpateMb);
                member.updateAllColumnById();

                //删除该用户的redis cache
                fungoCacheMember.excIndexCache(false, "getMemberWithMbIdSNS" + member.getId(), "", null);
            }
        }
    }


    /**
     * 逻辑删除账号
     * @param mb_id
     * @param bindInput
     * @return
     */
    private void deleteOldMemberAccount(String mb_id, MemberSNSBindInput bindInput) {

        //同时把该SNS id的fungo账号数据进行redis 缓存
        List<Member> memberList = (List<Member>) fungoCacheMember.getIndexCache("isRegistered" + mb_id, bindInput.getUnionid());
        if (null == memberList || memberList.isEmpty()) {
            //从DB获取
            String queryColumn = "";
            String snsId = bindInput.getUnionid();
            Integer snsType = bindInput.getSnsType();
            switch (snsType) {
                //微博
                case 0:
                    queryColumn = "weibo_open_id";
                    break;
                //微信
                case 1:
                    queryColumn = "weixin_open_id";
                    break;
                //QQ
                case 4:
                    queryColumn = "qq_open_id";
                    break;
            }
            EntityWrapper<Member> memberEntityWrapper = new EntityWrapper<Member>();
            memberEntityWrapper.eq(queryColumn, snsId).and("state != {0}", -1).isNull("mobile_phone_num");
            memberList = memberService.selectList(memberEntityWrapper);
        }

        if (null != memberList && !memberList.isEmpty()) {
            for (Member member : memberList) {

                Member udpateMb = new Member();
                udpateMb.setId(member.getId());
                udpateMb.setState(-1);
                //把账号修改为-1，删除状态
                memberService.updateById(udpateMb);

                //删除该用户的redis cache
                fungoCacheMember.excIndexCache(false, "getMemberWithMbIdSNS" + member.getId(), "", null);
            }
        }

    }


    /**
     * 绑定SNS平台账号
     * @param mb_id
     * @param memberDetail
     * @param bindInput
     * @param resultDto
     * @throws Exception
     */
    private ResultDto<MemberSNSBindOutput> bindSNS(String mb_id, Member memberDetail, MemberSNSBindInput bindInput) throws Exception {
        //验证当前SNS id在Sns平台是否有效
        ResultDto<MemberSNSBindOutput> resultDto = new ResultDto<>();
        boolean isValid = isValidWithSNS(bindInput);
        if (isValid) {
            boolean isExcBind = excBindSNS(memberDetail, bindInput);
            if (isExcBind) {

                MemberSNSBindOutput bindOutput = new MemberSNSBindOutput();
                bindOutput.setBindState(1);
                bindOutput.setSnsType(bindInput.getSnsType());

                resultDto.setData(bindOutput);
                //绑定成功后，若是首次绑定,加经验值和fungo币
                //绑定SNS任务
                excAddTaskExpAndFungoCoin(mb_id, bindInput.getSnsType());

                //其他会员信息
                fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_CARD + mb_id, "", null);
                // 个人资料
                fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + mb_id, "", null);

            } else {
                return ResultDto.error("-1", "服务器非常繁忙，请耐心等一下");
            }
            //无效
        } else {
            return ResultDto.error("-1", "第三方用户验证失败");
        }

        return resultDto;
    }


    /**
     * 给首次绑定成功SNS账号的用户添加经验值和Fungo币
     * @param mb_id
     * @return
     * @throws Exception
     */
    private void excAddTaskExpAndFungoCoin(String mb_id, Integer snsType) throws Exception {

        Member memberDetail = memberService.selectById(mb_id);
        //V2.4.6之前版本
        // return gameProxy.addTaskCore(Setting.ACTION_TYPE_BIND_THIRDPARTY, mb_id, "", -1);

        //V2.4.6起版本的任务
        switch (snsType) {

            //微博
            case 0:

                //1 经验值
                iMemberIncentDoTaskFacadeService.exTask(mb_id, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                        MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_NS_WB_EXP.code());
                //2 fungo币
                iMemberIncentDoTaskFacadeService.exTask(mb_id, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                        MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_SNS_WB_COIN.code());

                break;
            //微信
            case 1:

                //1 经验值
                iMemberIncentDoTaskFacadeService.exTask(mb_id, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                        MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_NS_WX_EXP.code());
                //2 fungo币
                iMemberIncentDoTaskFacadeService.exTask(mb_id, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                        MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_SNS_WX_COIN.code());
                break;
            //QQ
            case 4:

                //1 经验值
                iMemberIncentDoTaskFacadeService.exTask(mb_id, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                        MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_NS_QQ_EXP.code());
                //2 fungo币
                iMemberIncentDoTaskFacadeService.exTask(mb_id, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                        MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_SNS_QQ_COIN.code());
                break;
        }

    }


    /**
     * 验证是否首次绑定SNS账号
     * @param memberDetail
     * @param bindInput
     * @return
     */
    private boolean isFirstBindSNS(Member memberDetail, MemberSNSBindInput bindInput) {
        boolean isFirstBindSNS = false;
        Integer snsType = bindInput.getSnsType();
        switch (snsType) {
            //微博
            case 0:
                //1
                if (null == memberDetail.getWeiboOpenId()) {
                    isFirstBindSNS = true;
                }
                break;
            //微信
            case 1:
                if (null == memberDetail.getWeixinOpenId()) {
                    isFirstBindSNS = true;
                }
                break;
            //QQ
            case 4:
                if (null == memberDetail.getQqOpenId()) {
                    isFirstBindSNS = true;
                }
                break;
        }

        return isFirstBindSNS;
    }


    /**
     * 1.先验证该用户是否绑定当前SNS平台账号
     * @param memberDetail
     * @param bindInput
     * @return
     */
    private boolean isBinded(Member memberDetail, MemberSNSBindInput bindInput) {

        Integer snsType = bindInput.getSnsType();
        boolean isBined = false;
        switch (snsType) {
            //微博
            case 0:
                //1
                if (StringUtils.isNotBlank(memberDetail.getWeiboOpenId())) {
                    isBined = true;
                }
                break;
            //微信
            case 1:
                if (StringUtils.isNotBlank(memberDetail.getWeixinOpenId())) {
                    isBined = true;
                }
                break;
            //QQ
            case 4:
                if (StringUtils.isNotBlank(memberDetail.getQqOpenId())) {
                    isBined = true;
                }
                break;
        }
        return isBined;
    }

    /**
     * 根据用户ID查询用户数据
     * @param mb_id
     * @return
     */
    private Member getMemberWithMbId(String mb_id) {
        //先从Redis获取
        Member member = (Member) fungoCacheMember.getIndexCache("getMemberWithMbIdSNS" + mb_id, "");
        if (null != member) {
            return member;
        }
        Member memberDetail = memberService.selectById(mb_id);

        //redis cache
        fungoCacheMember.excIndexCache(true, "getMemberWithMbIdSNS" + mb_id, "", memberDetail);
        return memberDetail;
    }


    /**
     * 查询SNS平台是否已经被注册为fungo账号 ，且未绑定手机号
     * @param mb_id 用户id
     * @param bindInput sns绑定入参
     * @return true 该SNS ID已经有有Fungo账号，反之 false
     */
    private boolean isRegistered(String mb_id, MemberSNSBindInput bindInput) {

        Integer snsType = bindInput.getSnsType();
        String queryColumn = "";
        String snsId = bindInput.getUnionid();
        switch (snsType) {
            //微博
            case 0:
                queryColumn = "weibo_open_id";
                break;
            //微信
            case 1:
                queryColumn = "weixin_open_id";
                break;
            //QQ
            case 4:
                queryColumn = "qq_open_id";
                break;
        }
        EntityWrapper<Member> memberEntityWrapper = new EntityWrapper<Member>();
        memberEntityWrapper.eq(queryColumn, snsId).and("state != {0}", -1).isNull("mobile_phone_num");
        List<Member> memberList = memberService.selectList(memberEntityWrapper);
        if (null != memberList && !memberList.isEmpty()) {
            //同时把该SNS id的fungo账号数据进行redis 缓存
            fungoCacheMember.excIndexCache(true, "isRegistered" + mb_id, snsId, memberList);
            return true;
        }
        return false;
    }


    /**
     * b. 再验证，当前SNS平台账号，是否已经在Fungo平台创建账号，且已绑定手机号
     * @param mb_id 用户id
     * @param bindInput sns绑定入参
     * @return true 该SNS ID已经有有Fungo账号，反之 false
     */
    private String isRegisteredAndHasPhone(String mb_id, MemberSNSBindInput bindInput) {

        Integer snsType = bindInput.getSnsType();
        String queryColumn = "";
        String snsId = bindInput.getUnionid();
        switch (snsType) {
            //微博
            case 0:
                queryColumn = "weibo_open_id";
                break;
            //微信
            case 1:
                queryColumn = "weixin_open_id";
                break;
            //QQ
            case 4:
                queryColumn = "qq_open_id";
                break;
        }

        EntityWrapper<Member> memberEntityWrapper = new EntityWrapper<Member>();
        memberEntityWrapper.eq(queryColumn, snsId).and("state != {0}", -1).isNotNull("mobile_phone_num");
        List<Member> memberList = memberService.selectList(memberEntityWrapper);
        if (null != memberList && !memberList.isEmpty()) {
            //同时把该SNS id的fungo账号数据进行redis 缓存
            fungoCacheMember.excIndexCache(true, "isRegisteredAndHasPhone" + mb_id, snsId, memberList);
            StringBuffer stringBuffer = new StringBuffer();
            for (Member member : memberList) {
                stringBuffer.append(member.getMobilePhoneNum()).append(",");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            return stringBuffer.toString();
        }
        return null;
    }


    /**
     * 执行绑定
     * @param memberDetail
     * @param bindInput
     * @return
     */
    private boolean excBindSNS(Member memberDetail, MemberSNSBindInput bindInput) {

        Member updateMember = new Member();
        Integer snsType = bindInput.getSnsType();
        switch (snsType) {
            //微博
            case 0:
                updateMember.setWeiboOpenId(bindInput.getUnionid());
                break;
            //微信
            case 1:
                updateMember.setWeixinOpenId(bindInput.getUnionid());
                break;
            //QQ
            case 4:
                updateMember.setQqOpenId(bindInput.getUnionid());
                break;
        }

        updateMember.setId(memberDetail.getId());
        boolean isUpdate = memberService.updateById(updateMember);

        //清除member 和  SNS id的 fungo账号数据进行redis 缓存
        fungoCacheMember.excIndexCache(false, "getMemberWithMbIdSNS" + memberDetail.getId(), "", null);
        fungoCacheMember.excIndexCache(false, "isRegistered" + memberDetail.getId(), bindInput.getUnionid(), null);
        fungoCacheMember.excIndexCache(false, "isRegisteredAndHasPhone" + memberDetail.getId(), bindInput.getUnionid(), null);
        return isUpdate;
    }


    /**
     * 验证当前绑定的SNS平台id，在SNS平台是否有效
     * @param bindInput sns绑定入参
     * @return true 有效，false 无效
     */
    private boolean isValidWithSNS(MemberSNSBindInput bindInput) throws Exception {
        boolean isValid = false;
        String accessToken = bindInput.getAccessToken();
        Integer snsType = bindInput.getSnsType();
        switch (snsType) {
            //微博
            case 0:
                isValid = ThirdPartyLoginHelper.isSinaUser(accessToken, bindInput.getUid());
                break;
            //微信
            case 1:
                isValid = ThirdPartyLoginHelper.isWxUser(accessToken, bindInput.getOpenid());
                break;
            //QQ
            case 4:
                isValid = ThirdPartyLoginHelper.isQQUser(accessToken, bindInput.getOpenid(), bindInput.getOs());
                break;
        }
        return isValid;
    }


    //---------
}

