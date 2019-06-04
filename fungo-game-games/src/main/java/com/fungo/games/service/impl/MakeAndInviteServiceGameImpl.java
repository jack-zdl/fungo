package com.fungo.games.service.impl;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.games.entity.GameSurveyRel;
import com.fungo.games.facede.IEvaluateProxyService;
import com.fungo.games.service.GameInviteService;
import com.fungo.games.service.GameSurveyRelService;
import com.fungo.games.service.IMakeAndInviteGameService;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.consts.MemberIncentTaskConsts;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.FollowUserOutBean;
import com.game.common.dto.mark.BindingAppleInputBean;
import com.game.common.dto.mark.MakeCheckOut;
import com.game.common.dto.mark.MakeInputPageDto;
import com.game.common.enums.FunGoIncentTaskV246Enum;
import com.game.common.repo.cache.facade.FungoCacheGame;
import com.game.common.repo.cache.facade.FungoCacheMember;
import com.game.common.util.CommonUtil;
import com.game.common.util.PKUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MakeAndInviteServiceGameImpl implements IMakeAndInviteGameService {


    @Autowired
    private GameSurveyRelService surveyRelService;


//    @Autowired
//    private MemberService memberService;


//    @Autowired
//    private GameInviteService gameInviteService;
//
//
//    @Autowired
//    private IGameProxy gameProxy;
//
//
//    @Autowired
//    private IMemberIncentRuleRankService IRuleRankService;
//
//
//    @Autowired
//    private IMemberService iMemberService;
//
    @Autowired
    private FungoCacheMember fungoCacheMember;

    @Autowired
    private FungoCacheGame fungoCacheGame;


    //用户成长业务
//    @Resource(name = "memberIncentDoTaskFacadeServiceImpl")
//    private IMemberIncentDoTaskFacadeService iMemberIncentDoTaskFacadeService;

    @Autowired
    private IEvaluateProxyService iEvaluateProxyService;

    @Autowired
    private GameInviteService gameInviteService;

    @Value("${sys.config.fungo.cluster.index}")
    private String clusterIndex;


    @Override
    @Transactional
    public ResultDto<String> makGame(String memberId, String gameId, String phoneModel) throws Exception {
        GameSurveyRel sur = surveyRelService.selectOne(new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("phone_model", phoneModel).eq("game_id", gameId));

        // int times = -1;
        String tips = "";

        if (sur == null) {
            GameSurveyRel survey = new GameSurveyRel();
            survey.setAgree(0);
            survey.setCreatedAt(new Date());
            survey.setGameId(gameId);
            survey.setMemberId(memberId);
            survey.setNotice(0);
            survey.setState(0);
            survey.setUpdatedAt(new Date());
            survey.setPhoneModel(phoneModel);
            surveyRelService.insertOrUpdate(survey);

            //V2.4.6版本任务之前任务 废弃
            //times = gameProxy.addTaskCore(Setting.ACTION_TYPE_BOOK, memberId, gameId, Setting.RES_TYPE_GAME);

            //V2.4.6版本任务
            //每周任务
            //1 fungo币 微服务迁移 feign调用执行
//            2019-05-13
//            lyc
//            Map<String, Object> resMapCoin = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_WEEKLY.code(),
//                    MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_WEEKLY_FIRST_SUBSCRIBE_GAME_COIN.code());
            Integer clusterIndex_i = Integer.parseInt(clusterIndex);
            Map<String, Object> resMapCoin = iEvaluateProxyService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_WEEKLY.code(),
                    MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_WEEKLY_FIRST_SUBSCRIBE_GAME_COIN.code(), PKUtil.getInstance(clusterIndex_i).longPK());

            //2 经验值 微服务迁移 feign调用执行
//            2019-05-13
//            lyc
//            Map<String, Object> resMapExp = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_WEEKLY.code(),
//                    MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_WEEKLY_FIRST_SUBSCRIBEGAME_EXP.code());
            Map<String, Object> resMapExp = iEvaluateProxyService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_WEEKLY.code(),
                    MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_WEEKLY_FIRST_SUBSCRIBEGAME_EXP.code(), PKUtil.getInstance(clusterIndex_i).longPK());


            if (null != resMapCoin && !resMapCoin.isEmpty()) {
                if (null != resMapExp && !resMapExp.isEmpty()) {
                    boolean coinsSuccess = (boolean) resMapCoin.get("success");
                    boolean expSuccess = (boolean) resMapExp.get("success");
                    if (coinsSuccess && expSuccess) {
                        tips = (String) resMapCoin.get("msg");
                        tips += (String) resMapExp.get("msg");
                    } else {
                        tips = (String) resMapCoin.get("msg");
                    }
                }
            }
            //end

        } else {
            sur.setState(0);
            sur.updateById();
        }
        ResultDto<String> re = new ResultDto<String>();
        if (StringUtils.isNotBlank(tips)) {
            re.show(tips);
            re.setData(tips);
        } else {
            re.show("预约成功");
            re.setData("预约成功");
        }

        //clear redis cache
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_GAMELIST + memberId;
        //我的游戏列表
        fungoCacheMember.excIndexCache(false, keyPrefix, "", null);
        //游戏详情
        fungoCacheMember.excIndexCache(false,  FungoCoreApiConstant.FUNGO_CORE_API_GAME_DETAIL + gameId, "", null);
        //游戏合集项列表,
        fungoCacheMember.excIndexCache(false,  FungoCoreApiConstant.FUNGO_CORE_API_GAME_ITEMS , "", null);
        //游戏列表
        fungoCacheGame.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_GAME_LIST + memberId, "", null);
        return re;
    }

    public ResultDto<String> unmakeGame(String memberId, String gameId, String phoneModel) {

        GameSurveyRel rel = surveyRelService.selectOne(new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("phone_model", phoneModel).eq("game_id", gameId));

        if (rel == null){
            return ResultDto.error("-1", "您还未预约成功!!!");
        }
        rel.setState(-1);

        rel.updateById();

        //clear redis cache
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_GAMELIST + memberId;
        fungoCacheMember.excIndexCache(false, keyPrefix, "", null);
        //游戏详情
        fungoCacheMember.excIndexCache(false,  FungoCoreApiConstant.FUNGO_CORE_API_GAME_DETAIL + gameId, "", null);
        //游戏合集项列表
        fungoCacheMember.excIndexCache(false,  FungoCoreApiConstant.FUNGO_CORE_API_GAME_ITEMS, "", null);
        //游戏列表
        fungoCacheGame.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_GAME_LIST + memberId, "", null);
        return ResultDto.success("取消成功");
    }

    @Override
    public ResultDto<String> bindingAppleID(String memberId, BindingAppleInputBean makeInput) {
        //修改
        GameSurveyRel sur = surveyRelService.selectOne(new EntityWrapper<GameSurveyRel>().eq("member_id", memberId)
                .eq("phone_model", makeInput.getPhoneModel()).eq("game_id", makeInput.getGameId()));
        if (sur == null) {
            return ResultDto.error("-1", "绑定失败");
        } else {
            sur.setAppleId(makeInput.getAppleId());
            sur.setSurname(makeInput.getSurname());
            sur.setName(makeInput.getName());
            sur.setUpdatedAt(new Date());
            sur.updateById();
            return ResultDto.success("绑定成功");
        }
    }

    @Override
    public ResultDto<MakeCheckOut> getgameCheck(String memberId, String gameId, String phoneModel) {
        //修改
        GameSurveyRel sur = surveyRelService.selectOne(new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("phone_model", phoneModel).eq("game_id", gameId));
        ResultDto<MakeCheckOut> re = new ResultDto<MakeCheckOut>();
        MakeCheckOut mcheck = new MakeCheckOut();
        if (sur != null) {
            mcheck.setMake(true);
            mcheck.setBinding(CommonUtil.isNull(sur.getAppleId()) ? false : true);
            mcheck.setClause(sur.getAgree() == 1 ? true : false);
        } else {
            mcheck.setBinding(false);
            mcheck.setClause(false);
        }
        re.setData(mcheck);
        return re;
    }

    @Override
    public ResultDto<String> getgameAgree(String memberId, String gameId, String phoneModel) {
        //修改
        GameSurveyRel sur = surveyRelService.selectOne(new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("phone_model", phoneModel).eq("game_id", gameId));
        if (sur != null) {
            sur.setAgree(1);
            sur.updateById();
        }
        return ResultDto.success("同意成功");
    }

//    @Autowired
//    private BasActionService actionService;
    @Override
    public FungoPageResultDto<FollowUserOutBean> getInviteUserList(String memberId, MakeInputPageDto inputPageDto) throws Exception {
        FungoPageResultDto<FollowUserOutBean> re = new FungoPageResultDto<FollowUserOutBean>();
        List<FollowUserOutBean> list = new ArrayList<FollowUserOutBean>();
        re.setData(list);
//        迁移微服务 根据用户会员DTO对象分页查询用户会员
//        2019-05-22
//        lyc
//        Page<Member> page = this.memberService.selectPage(new Page<Member>(inputPageDto.getPage(), inputPageDto.getLimit()), new EntityWrapper<Member>().ne("id", memberId).orderBy("sort", false));
        /*MemberDto memberDto = new MemberDto();
        memberDto.setPage(inputPageDto.getPage());
        memberDto.setLimit(inputPageDto.getLimit());
        memberDto.setId(memberId);
        FungoPageResultDto<MemberDto> memberDtoFungoPageResultDto = iEvaluateProxyService.listMemberDtoPag(memberDto);
        List<MemberDto> plist = memberDtoFungoPageResultDto.getData();
        List<IncentRuleRank> levelRankList = iEvaluateProxyService.getLevelRankList();
        ObjectMapper mapper = new ObjectMapper();
        for (MemberDto member : plist) {
            FollowUserOutBean bean = new FollowUserOutBean();
            bean.setAvatar(member.getAvatar());
            bean.setCreatedAt(DateTools.fmtDate(member.getCreatedAt()));
            bean.setFollowed(false);
            GameInvite one = gameInviteService.selectOne(new EntityWrapper<GameInvite>().eq("member_id", memberId).eq("game_id", inputPageDto.getGameId()).eq("invite_member_id", member.getId()).eq("state", 0));
            if (one == null) {
                bean.setFollowed(false);
            } else {
                bean.setFollowed(true);
            }
    //			int actionCount = actionService.selectCount(new EntityWrapper<BasAction>().eq("target_id", memberId).eq("type", 5).eq("member_id", memberId).and("state != 1"));
    //			if (actionCount > 0) {
    //				bean.setFollowed(true);
    //			} else {
    //				bean.setFollowed(false);
    //			}
            bean.setLevel(member.getLevel());
            bean.setMemberNo(member.getMemberNo());
            bean.setObjectId(member.getId());
            bean.setUpdatedAt(DateTools.fmtDate(member.getUpdatedAt()));
            bean.setUsername(member.getUserName());
            bean.setSign(member.getSign());
            String rankImgs = iMemberService.getLevelRankUrl(member.getLevel(), levelRankList);
            ArrayList<HashMap<String, Object>> urlList = mapper.readValue(rankImgs, ArrayList.class);
            bean.setDignityImg((String) urlList.get(0).get("url"));
            list.add(bean);
        }
        PageTools.pageToResultDto(re, page);*/
        return re;
    }



}
