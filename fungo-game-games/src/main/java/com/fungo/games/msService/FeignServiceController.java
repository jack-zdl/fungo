package com.fungo.games.msService;


import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.games.dao.GameDao;
import com.fungo.games.dao.GameEvaluationDao;
import com.fungo.games.entity.*;
import com.fungo.games.facede.IEvaluateProxyService;
import com.fungo.games.service.*;
import com.fungo.games.service.impl.GameEvaluationServiceImap;
import com.game.common.bean.MemberPulishFromCommunity;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.evaluation.EvaluationInputPageDto;
import com.game.common.dto.game.*;
import com.game.common.dto.index.CardDataBean;
import com.game.common.dto.index.CardIndexBean;
import com.game.common.util.PageTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * feignService 调用中心
 *
 * @Author lyc
 * @create 2019/5/7 11:48
 */
@SuppressWarnings("all")
@RestController
@Api(value = "", description = "feignService 调用中心")
@RequestMapping("/ms/service/game")
public class FeignServiceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeignServiceController.class);

    @Autowired
    private IGameService iGameService;

    @Autowired
    private GameSurveyRelService gameSurveyRelService;

    @Autowired
    private GameInviteService gameInviteService;

    @Autowired
    private GameService gameService;

    @Autowired
    private GameEvaluationDao gameEvaluationDao;

    @Autowired
    private GameEvaluationServiceImap gameEvaluationServiceImap;

    @Autowired
    private GameReleaseLogService gameReleaseLogService;

    @Autowired
    private GameCollectionGroupService gameCollectionGroupService;

    @Autowired
    private GameCollectionItemService gameCollectionItemService;

    @Autowired
    private IEvaluateProxyService iEvaluateProxyService;

    @Autowired
    private GameDao gameDao;

    @Autowired
    private GameTagService gameTagService;

    @Autowired
    private BasTagService basTagService;

    @Autowired
    private GameEvaluationService gameEvaluationService;

    /****************************************************ActionController**********************************************************************/

    @ApiOperation(value = "更新计数器", notes = "")
    @RequestMapping(value = "/api/update/counter", method = RequestMethod.POST)
    public Boolean updateCounter(@RequestBody Map<String, String> map) {
//        根据表名(动态)修改
        return iGameService.updateCountor(map);
    }

    @ApiOperation(value = "被点赞用户的id", notes = "")
    @RequestMapping(value = "/api/getMemberIdByTargetId", method = RequestMethod.POST)
    String getMemberIdByTargetId(@RequestBody Map<String, String> map) {
        return iGameService.getMemberIdByTargetId(map);
    }
/****************************************************System**********************************************************************/

    /**
     * 查询游戏测试会员关联表总数
     *
     * @param gameSurveyRelDto
     * @return
     */
    @ApiOperation(value = "查询游戏测试会员关联表总数", notes = "")
    @RequestMapping(value = "/api/selectCount", method = RequestMethod.POST)
    int gameSurveySelectCount(@RequestBody GameSurveyRelDto gameSurveyRelDto) {
//        GameSurveyRel gameSurveyRel = new GameSurveyRel();
//        BeanUtils.copyProperties(gameSurveyRelDto, gameSurveyRel);
        HashMap<String, Object> param = new HashMap<String, Object>();
        gameSurveyFun(gameSurveyRelDto, param);
        return gameSurveyRelService.selectCount(new EntityWrapper<GameSurveyRel>().allEq(param));
    }

    /**
     * 查询游戏版本日志审批总数
     *
     * @param gameReleaseLogDto
     * @return
     */
    @ApiOperation(value = "查询游戏版本日志审批总数", notes = "")
    @RequestMapping(value = "/api/gameReleaseLog/selectCount", method = RequestMethod.POST)
    int gameReleaseLogSelectCount(@RequestBody GameReleaseLogDto gameReleaseLogDto) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        gameReleaseLogFun(gameReleaseLogDto, param);
        return gameReleaseLogService.selectCount(new EntityWrapper<GameReleaseLog>().allEq(param));
    }

    /**
     * 查询游戏评价总数
     *
     * @param gameEvaluationDto
     * @return
     */
    @ApiOperation(value = "查询游戏评价总数", notes = "")
    @RequestMapping(value = "/api/gameEvaluation/selectCount", method = RequestMethod.POST)
    int gameEvaluationSelectCount(@RequestBody GameEvaluationDto gameEvaluationDto) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        gameEvaluationFun(gameEvaluationDto, param);
        return gameEvaluationServiceImap.selectCount(new EntityWrapper<GameEvaluation>().allEq(param));
    }


    /**
     * 用户游戏评测精品数
     *
     * @return
     */
    @ApiOperation(value = "用户游戏评测精品数", notes = "")
    @RequestMapping(value = "/api/game/getUserGameReviewBoutiqueNumber", method = RequestMethod.POST)
    ResultDto<List<Map>> getUserGameReviewBoutiqueNumber() {
        List<Map> userGameReviewBoutiqueNumber = gameEvaluationDao.getUserGameReviewBoutiqueNumber();
        return ResultDto.success(userGameReviewBoutiqueNumber);
    }

    /**
     * 游戏评价的分页查询
     *
     * @param gameEvaluationDto
     * @return
     */
    @ApiOperation(value = "游戏评价的分页查询", notes = "")
    @RequestMapping(value = "/api/evaluation/getGameEvaluationPage", method = RequestMethod.POST)
    FungoPageResultDto<GameEvaluationDto> getGameEvaluationPage(@RequestBody GameEvaluationDto gameEvaluationDto) {
        List<GameEvaluationDto> gameEvaluationList = null;
        FungoPageResultDto<GameEvaluationDto> fungoPageResultDto = new FungoPageResultDto<>();
        Page<GameEvaluation> cmmPostPageSelect = null;
        try {
            int page = gameEvaluationDto.getPage();
            int limit = gameEvaluationDto.getLimit();
            EntityWrapper<GameEvaluation> evaluationWrapper = new EntityWrapper<GameEvaluation>();
            HashMap<String, Object> param = new HashMap<String, Object>();
            Page<GameEvaluation> gameEvaluationPage = null;
            if (page > 0 && limit > 0) {
                gameEvaluationPage = new Page<>(page, limit);
            }
            gameEvaluationFun(gameEvaluationDto, param);

            evaluationWrapper.allEq(param);

            //帖子内容
            String content = gameEvaluationDto.getContent();
            if (StringUtils.isNotBlank(content)) {
                evaluationWrapper.orNew("content like '%" + content + "%'");
            }
            //游戏名字
            String gameName = gameEvaluationDto.getGameName();
            if (StringUtils.isNotBlank(gameName)) {
                evaluationWrapper.orNew("game_name like '%" + gameName + "%'");
            }

            //根据修改时间倒叙
            evaluationWrapper.orderBy("updated_at", false);

            List<GameEvaluation> selectRecords = null;

            if (null != gameEvaluationPage) {

                cmmPostPageSelect = this.gameEvaluationServiceImap.selectPage(gameEvaluationPage, evaluationWrapper);

                if (null != cmmPostPageSelect) {
//                    PageTools.pageToResultDto(fungoPageResultDto, cmmPostPageSelect);
                    selectRecords = cmmPostPageSelect.getRecords();

                    //设置分页数据
                    fungoPageResultDto.setCount(cmmPostPageSelect.getTotal());
                }

            } else {
                selectRecords = this.gameEvaluationServiceImap.selectList(evaluationWrapper);
            }

            if (null != selectRecords) {

                gameEvaluationList = new ArrayList<GameEvaluationDto>();

                for (GameEvaluation gameEvaluation : selectRecords) {

                    GameEvaluationDto gEvaluationDto = new GameEvaluationDto();

                    BeanUtils.copyProperties(gameEvaluation, gEvaluationDto);

                    gameEvaluationList.add(gEvaluationDto);
                }
            }

        } catch (Exception ex) {
            LOGGER.error("/ms/service/game/api/evaluation/getGameEvaluationPage--getGameEvaluationPage-出现异常:", ex);
        }
//        Page<GameEvaluationDto> gameEvaluationDtoPage = new Page<>();
//        gameEvaluationDtoPage.setRecords(gameEvaluationList);

        fungoPageResultDto.setData(gameEvaluationList);
        PageTools.pageToResultDto(fungoPageResultDto, cmmPostPageSelect);
        return fungoPageResultDto;
    }

    private void gameEvaluationFun(GameEvaluationDto gameEvaluationDto, HashMap<String, Object> param) {
        //            主键ID
        String gameEvaluationId = gameEvaluationDto.getId();
        if (StringUtils.isNotBlank(gameEvaluationId)) {
            param.put("id", gameEvaluationId);
        }
        //游戏ID
        String gameId = gameEvaluationDto.getGameId();
        if (StringUtils.isNotBlank(gameId)) {
            param.put("game_id", gameId);
        }
        //会员ID
        String memberId = gameEvaluationDto.getMemberId();
        if (StringUtils.isNotBlank(memberId)) {
            param.put("member_id", memberId);
        }
        //图片
        String images = gameEvaluationDto.getImages();
        if (StringUtils.isNotBlank(images)) {
            param.put("images", images);
        }
        //手机型号
        String phoneModel = gameEvaluationDto.getPhoneModel();
        if (StringUtils.isNotBlank(phoneModel)) {
            param.put("phone_model", phoneModel);
        }
        //状态
        Integer state = gameEvaluationDto.getState();
        if (null != state) {
            param.put("state", state);
        }
        //是否推荐
        String isRecommend = gameEvaluationDto.getIsRecommend();
        if (StringUtils.isNotBlank(isRecommend)) {
            param.put("is_recommend", isRecommend);
        }
        //属性
        Integer type = gameEvaluationDto.getType();
        if (null != type) {
            param.put("type", type);
        }
    }


    /**
     * 游戏测试会员关联表的分页查询
     *
     * @param gameSurveyDto
     * @return
     */
    @SuppressWarnings("all")
    @ApiOperation(value = "游戏测试会员关联表的分页查询", notes = "")
    @RequestMapping(value = "/api/evaluation/getGameSurveyRelPage", method = RequestMethod.POST)
    FungoPageResultDto<GameSurveyRelDto> getGameSurveyRelPage(@RequestBody GameSurveyRelDto gameSurveyDto) {
        List<GameSurveyRelDto> gameSurveyList = null;
        FungoPageResultDto<GameSurveyRelDto> fungoPageResultDto = new FungoPageResultDto<>();
        Page<GameSurveyRel> gameSurveyRelPageSelect = null;
        try {
            int page = gameSurveyDto.getPage();
            int limit = gameSurveyDto.getLimit();
            EntityWrapper<GameSurveyRel> surveyWrapper = new EntityWrapper<GameSurveyRel>();
            HashMap<String, Object> param = new HashMap<String, Object>();
            Page<GameSurveyRel> gameSurveyPage = null;
            if (page > 0 && limit > 0) {
                gameSurveyPage = new Page<>(page, limit);
            }
            gameSurveyFun(gameSurveyDto, param);
            surveyWrapper.allEq(param);


            //根据修改时间倒叙
            surveyWrapper.orderBy("updated_at", false);

            List<GameSurveyRel> selectRecords = null;

            if (null != gameSurveyPage) {

                gameSurveyRelPageSelect = this.gameSurveyRelService.selectPage(gameSurveyPage, surveyWrapper);

                if (null != gameSurveyRelPageSelect) {
//                    PageTools.pageToResultDto(fungoPageResultDto, gameSurveyRelPageSelect);
                    selectRecords = gameSurveyRelPageSelect.getRecords();
                }
            } else {
                selectRecords = this.gameSurveyRelService.selectList(surveyWrapper);
            }
            if (null != selectRecords) {

                gameSurveyList = new ArrayList<GameSurveyRelDto>();

                for (GameSurveyRel gameSurveyRel : selectRecords) {

                    GameSurveyRelDto gameSurveyRelDto = new GameSurveyRelDto();

                    BeanUtils.copyProperties(gameSurveyRel, gameSurveyRelDto);

                    gameSurveyList.add(gameSurveyRelDto);
                }
            }

        } catch (Exception ex) {
            LOGGER.error("/ms/service/game/api/evaluation/getGameSurveyRelPage--getGameSurveyRelPage-出现异常:", ex);
        }
//        Page<GameSurveyRelDto> gameEvaluationDtoPage = new Page<>();
//        gameEvaluationDtoPage.setRecords(gameSurveyList);
        fungoPageResultDto.setData(gameSurveyList);
        PageTools.pageToResultDto(fungoPageResultDto, gameSurveyRelPageSelect);
        return fungoPageResultDto;
    }

    @ApiOperation(value = "游戏评价邀请的分页查询", notes = "")
    @RequestMapping(value = "/api/evaluation/getGameInvitePage", method = RequestMethod.POST)
    FungoPageResultDto<GameInviteDto> getGameInvitePage(@RequestBody GameInviteDto gameInviteDto) {
        List<GameInviteDto> gameInviteList = null;
        FungoPageResultDto<GameInviteDto> fungoPageResultDto = new FungoPageResultDto<>();
        Page<GameInvite> gameInvitePage1 = new Page<>();
        try {
            int page = gameInviteDto.getPage();
            int limit = gameInviteDto.getLimit();
            EntityWrapper<GameInvite> gameInviteWrapper = new EntityWrapper<GameInvite>();
            HashMap<String, Object> param = new HashMap<String, Object>();
            Page<GameInvite> gameInvitePage = null;
            if (page > 0 && limit > 0) {
                gameInvitePage = new Page<>(page, limit);
            }
            gameInviteFun(gameInviteDto, param);
            gameInviteWrapper.allEq(param);


            //根据修改时间倒叙
//            surveyWrapper.orderBy("updated_at", false);

            List<GameInvite> selectRecords = null;

            if (null != gameInvitePage) {

                gameInvitePage1 = this.gameInviteService.selectPage(gameInvitePage, gameInviteWrapper);

                if (null != gameInvitePage1) {
//                    PageTools.pageToResultDto(fungoPageResultDto, gameInvitePage1);
                    selectRecords = gameInvitePage1.getRecords();
                }
            } else {
                selectRecords = this.gameInviteService.selectList(gameInviteWrapper);
            }
            if (null != selectRecords) {

                gameInviteList = new ArrayList<GameInviteDto>();

                for (GameInvite gameInvite : selectRecords) {

                    GameInviteDto gameInviteDto1 = new GameInviteDto();

                    BeanUtils.copyProperties(gameInvite, gameInviteDto1);

                    gameInviteList.add(gameInviteDto1);
                }
            }

        } catch (Exception ex) {
            LOGGER.error("/ms/service/game/api/evaluation/getGameInvitePage--getGameInvitePage-出现异常:", ex);
        }
//        Page<GameInviteDto> gameEvaluationDtoPage = new Page<>();
//        gameEvaluationDtoPage.setRecords(gameInviteList);
        fungoPageResultDto.setData(gameInviteList);
        PageTools.pageToResultDto(fungoPageResultDto, gameInvitePage1);
        return fungoPageResultDto;
    }

    private void gameInviteFun(GameInviteDto gameInviteDto, HashMap<String, Object> param) {
        //            主键ID
        String gameSurveyId = gameInviteDto.getId();
        if (StringUtils.isNotBlank(gameSurveyId)) {
            param.put("id", gameSurveyId);
        }
        //游戏ID
        String gameId = gameInviteDto.getGameId();
        if (StringUtils.isNotBlank(gameId)) {
            param.put("game_id", gameId);
        }
        //会员ID
        String memberId = gameInviteDto.getMemberId();
        if (StringUtils.isNotBlank(memberId)) {
            param.put("member_id", memberId);
        }
        //被邀请人
        String inviteMemberId = gameInviteDto.getInviteMemberId();
        if (StringUtils.isNotBlank(inviteMemberId)) {
            param.put("invite_member_id", inviteMemberId);
        }
        //状态
        Integer state = gameInviteDto.getState();
        if (null != state) {
            param.put("state", state);
        }
        //notice_id
        String noticeId = gameInviteDto.getNoticeId();
        if (StringUtils.isNotBlank(noticeId)) {
            param.put("notice_id", noticeId);
        }
    }


    private void gameSurveyFun(GameSurveyRelDto gameSurveyDto, HashMap<String, Object> param) {
        //            主键ID
        String gameSurveyId = gameSurveyDto.getId();
        if (StringUtils.isNotBlank(gameSurveyId)) {
            param.put("id", gameSurveyId);
        }
        //游戏ID
        String gameId = gameSurveyDto.getGameId();
        if (StringUtils.isNotBlank(gameId)) {
            param.put("game_id", gameId);
        }
        //会员ID
        String memberId = gameSurveyDto.getMemberId();
        if (StringUtils.isNotBlank(memberId)) {
            param.put("member_id", memberId);
        }
        //是否同意协议
        Integer agree = gameSurveyDto.getAgree();
        if (null != agree) {
            param.put("agree", agree);
        }
        //通知状态
        Integer notice = gameSurveyDto.getNotice();
        if (null != notice) {
            param.put("notice", notice);
        }
        //状态
        Integer state = gameSurveyDto.getState();
        if (null != state) {
            param.put("state", state);
        }
        //批次号，打批
        String batchNo = gameSurveyDto.getBatchNo();
        if (StringUtils.isNotBlank(batchNo)) {
            param.put("batch_no", batchNo);
        }
        //手机型号
        String phoneModel = gameSurveyDto.getPhoneModel();
        if (StringUtils.isNotBlank(phoneModel)) {
            param.put("phone_model", phoneModel);
        }
        //姓名
        String surname = gameSurveyDto.getSurname();
        if (StringUtils.isNotBlank(phoneModel)) {
            param.put("surname", surname);
        }
        //名称
        String name = gameSurveyDto.getName();
        if (StringUtils.isNotBlank(phoneModel)) {
            param.put("name", name);
        }
    }

    //    根据游戏版本日志审批对象查询集合
    @SuppressWarnings("all")
    @ApiOperation(value = "根据游戏版本日志审批对象查询集合", notes = "")
    @RequestMapping(value = "/api/evaluation/getGameReleaseLogPage", method = RequestMethod.POST)
    FungoPageResultDto<GameReleaseLogDto> getGameReleaseLogPage(@RequestBody GameReleaseLogDto gameReleaseLogDto) {
        List<GameReleaseLogDto> gameReleaseLogList = null;
        FungoPageResultDto<GameReleaseLogDto> fungoPageResultDto = new FungoPageResultDto<>();
        Page<GameReleaseLog> gameSurveyRelPageSelect = new Page<>();

        try {
            int page = gameReleaseLogDto.getPage();
            int limit = gameReleaseLogDto.getLimit();
            EntityWrapper<GameReleaseLog> gameReleaseLogWrapper = new EntityWrapper<GameReleaseLog>();
            HashMap<String, Object> param = new HashMap<String, Object>();
            Page<GameReleaseLog> gameReleaseLogPage = null;
            if (page > 0 && limit > 0) {
                gameReleaseLogPage = new Page<>(page, limit);
            }
            gameReleaseLogFun(gameReleaseLogDto, param);
            gameReleaseLogWrapper.allEq(param);
            //根据修改时间倒叙
            gameReleaseLogWrapper.orderBy("updated_at", false);

            List<GameReleaseLog> selectRecords = null;

            if (null != gameReleaseLogPage) {

                gameSurveyRelPageSelect = this.gameReleaseLogService.selectPage(gameReleaseLogPage, gameReleaseLogWrapper);

                if (null != gameSurveyRelPageSelect) {
//                    PageTools.pageToResultDto(fungoPageResultDto, gameSurveyRelPageSelect);
                    selectRecords = gameSurveyRelPageSelect.getRecords();
                }
            } else {
                selectRecords = this.gameReleaseLogService.selectList(gameReleaseLogWrapper);
            }
            if (null != selectRecords) {

                gameReleaseLogList = new ArrayList<GameReleaseLogDto>();

                for (GameReleaseLog gameReleaseLog : selectRecords) {

                    GameReleaseLogDto gameReleaseLDto = new GameReleaseLogDto();

                    BeanUtils.copyProperties(gameReleaseLog, gameReleaseLDto);

                    gameReleaseLogList.add(gameReleaseLDto);
                }
            }
        } catch (Exception ex) {
            LOGGER.error("/ms/service/game/api/evaluation/getGameReleaseLogPage--getGameReleaseLogPage-出现异常:", ex);
        }
//        Page<GameReleaseLogDto> gameReleaseLogDtoPage = new Page<>();
//        gameReleaseLogDtoPage.setRecords(gameReleaseLogList);
        fungoPageResultDto.setData(gameReleaseLogList);
        PageTools.pageToResultDto(fungoPageResultDto, gameSurveyRelPageSelect);
        return fungoPageResultDto;
    }

    @ApiOperation(value = "根据游戏对象查询集合", notes = "")
    @RequestMapping(value = "/api/geme/getGamePage", method = RequestMethod.POST)
    FungoPageResultDto<GameDto> getGamePage(@RequestBody GameDto gameDto) {

        List<GameDto> gameList = null;

        FungoPageResultDto<GameDto> fungoPageResultDto = new FungoPageResultDto<>();

        Page<Game> gamePageSelect = new Page<>();

        try {

            int page = gameDto.getPage();
            int limit = gameDto.getLimit();

            EntityWrapper<Game> gameWrapper = new EntityWrapper<Game>();

            HashMap<String, Object> param = new HashMap<String, Object>();
            Page<Game> gamePage = null;
            if (page > 0 && limit > 0) {
                gamePage = new Page<>(page, limit);
            }

            gameFun(gameDto, param);
            gameWrapper.allEq(param);

            Integer state = gameDto.getState();
            if (null != state) {
                gameWrapper.eq("state", state);
            }

            //根据修改时间倒叙
            gameWrapper.orderBy("updated_at", false);

            List<Game> selectRecords = null;

            if (null != gamePage) {

                gamePageSelect = this.gameService.selectPage(gamePage, gameWrapper);

                if (null != gamePageSelect) {
//                    PageTools.pageToResultDto(fungoPageResultDto, gamePageSelect);
                    selectRecords = gamePageSelect.getRecords();
                }
            } else {
                selectRecords = this.gameService.selectList(gameWrapper);
            }
            if (null != selectRecords) {

                gameList = new ArrayList<GameDto>();

                for (Game game : selectRecords) {

                    GameDto gameDto1 = new GameDto();

                    BeanUtils.copyProperties(game, gameDto1);

                    gameList.add(gameDto1);
                }
            }
        } catch (Exception ex) {
            LOGGER.error("/ms/service/game/api/geme/getGamePage--getGamePage-出现异常:", ex);
        }
//        Page<GameDto> gameDtoPage = new Page<>();
//        gameDtoPage.setRecords(gameList);
        fungoPageResultDto.setData(gameList);
        PageTools.pageToResultDto(fungoPageResultDto, gamePageSelect);
        return fungoPageResultDto;
    }

    private void gameFun(GameDto gameDto, HashMap<String, Object> param) {
        //            主键ID
        String gameId = gameDto.getId();
        if (StringUtils.isNotBlank(gameId)) {
            param.put("id", gameId);
        }
        //苹果商店
        String itunesId1 = gameDto.getItunesId();
        if (StringUtils.isNotBlank(itunesId1)) {
            param.put("itunes_id", itunesId1);
        }
        //标签
        String tags = gameDto.getTags();
        if (StringUtils.isNotBlank(tags)) {
            param.put("tags", tags);
        }
        //苹果商店
        String itunesId = gameDto.getItunesId();
        if (StringUtils.isNotBlank(itunesId)) {
            param.put("itunes_id", itunesId);
        }
        //游戏版本
        String versionChild = gameDto.getVersionChild();
        if (StringUtils.isNotBlank(versionChild)) {
            param.put("version_child", versionChild);
        }
        //状态
        Integer state = gameDto.getState();
        if (null != state) {
            param.put("state", state);
        }
        Integer imageRatio = gameDto.getImageRatio();
        if (null != imageRatio) {
            param.put("image_ratio", imageRatio);
        }
        //社区ID
        String communityId = gameDto.getCommunityId();
        if (StringUtils.isNotBlank(communityId)) {
            param.put("community_id", communityId);
        }
        //名称
        String name = gameDto.getName();
        if (StringUtils.isNotBlank(name)) {
            param.put("name", name);
        }
        //国家
        String origin = gameDto.getOrigin();
        if (StringUtils.isNotBlank(origin)) {
            param.put("origin", origin);
        }
        //主版本号
        String versionMain = gameDto.getVersionMain();
        if (StringUtils.isNotBlank(versionMain)) {
            param.put("version_main", versionMain);
        }
        String isbnId = gameDto.getIsbnId();
        if (StringUtils.isNotBlank(isbnId)) {
            param.put("isbn_id", isbnId);
        }
        //开发者ID
        String developerId = gameDto.getDeveloperId();
        if (StringUtils.isNotBlank(developerId)) {
            param.put("developer_id", developerId);
        }
        //软件著作权登记号
        String copyrightId = gameDto.getCopyrightId();
        if (StringUtils.isNotBlank(copyrightId)) {
            param.put("copyright_id", copyrightId);
        }
        //游戏备案通知单号
        String issueId = gameDto.getIssueId();
        if (StringUtils.isNotBlank(issueId)) {
            param.put("issue_id", issueId);
        }
        //安卓状态
        Integer androidState = gameDto.getAndroidState();
        if (null != androidState) {
            param.put("android_state", androidState);
        }
        //iOS状态
        Integer iosState = gameDto.getIosState();
        if (null != iosState) {
            param.put("ios_state", iosState);
        }
    }

    private void gameReleaseLogFun(GameReleaseLogDto gameReleaseLogDto, HashMap<String, Object> param) {
        //            主键ID
        String gameReleaseLogId = gameReleaseLogDto.getId();
        if (StringUtils.isNotBlank(gameReleaseLogId)) {
            param.put("id", gameReleaseLogId);
        }
        //游戏ID
        String gameId = gameReleaseLogDto.getGameId();
        if (StringUtils.isNotBlank(gameId)) {
            param.put("game_id", gameId);
        }
        //会员ID
        String memberId = gameReleaseLogDto.getMemberId();
        if (StringUtils.isNotBlank(memberId)) {
            param.put("member_id", memberId);
        }
        //标签
        String tags = gameReleaseLogDto.getTags();
        if (StringUtils.isNotBlank(tags)) {
            param.put("tags", tags);
        }
        //苹果商店
        String itunesId = gameReleaseLogDto.getItunesId();
        if (StringUtils.isNotBlank(itunesId)) {
            param.put("itunes_id", itunesId);
        }
        //游戏版本
        String versionChild = gameReleaseLogDto.getVersionChild();
        if (StringUtils.isNotBlank(versionChild)) {
            param.put("version_child", versionChild);
        }
        //状态
        Integer state = gameReleaseLogDto.getState();
        if (null != state) {
            param.put("state", state);
        }
        Integer imageRatio = gameReleaseLogDto.getImageRatio();
        if (null != imageRatio) {
            param.put("image_ratio", imageRatio);
        }
        //社区ID
        String communityId = gameReleaseLogDto.getCommunityId();
        if (StringUtils.isNotBlank(communityId)) {
            param.put("community_id", communityId);
        }
        //名称
        String name = gameReleaseLogDto.getName();
        if (StringUtils.isNotBlank(name)) {
            param.put("name", name);
        }
        //国家
        String origin = gameReleaseLogDto.getOrigin();
        if (StringUtils.isNotBlank(origin)) {
            param.put("origin", origin);
        }
        //主版本号
        String versionMain = gameReleaseLogDto.getVersionMain();
        if (StringUtils.isNotBlank(versionMain)) {
            param.put("version_main", versionMain);
        }
        String isbnId = gameReleaseLogDto.getIsbnId();
        if (StringUtils.isNotBlank(isbnId)) {
            param.put("isbn_id", isbnId);
        }
        //开发者ID
        String developerId = gameReleaseLogDto.getDeveloperId();
        if (StringUtils.isNotBlank(developerId)) {
            param.put("developer_id", developerId);
        }
        //审批状态
        Integer approveState = gameReleaseLogDto.getApproveState();
        if (null != approveState) {
            param.put("approve_state", approveState);
        }
        //软件著作权登记号
        String copyrightId = gameReleaseLogDto.getCopyrightId();
        if (StringUtils.isNotBlank(copyrightId)) {
            param.put("copyright_id", copyrightId);
        }
        //游戏备案通知单号
        String issueId = gameReleaseLogDto.getIssueId();
        if (StringUtils.isNotBlank(issueId)) {
            param.put("issue_id", issueId);
        }
        //安卓状态
        String androidState = gameReleaseLogDto.getAndroidState();
        if (StringUtils.isNotBlank(androidState)) {
            param.put("android_state", androidState);
        }
        //iOS状态
        String iosState = gameReleaseLogDto.getIosState();
        if (StringUtils.isNotBlank(iosState)) {
            param.put("ios_state", iosState);
        }
    }

    @ApiOperation(value = "根据游戏id集合获取FungoPageResultDto<GameOutBean>", notes = "")
    @RequestMapping(value = "/api/content/gameList", method = RequestMethod.POST)
    FungoPageResultDto<GameOutBean> getGameList(@RequestBody GameItemInput input) {
        return iGameService.getGameList1(input);
    }

    /**************************************************2019-05-18系統*****************************************************************************/
    @ApiOperation(value = "getGameSelectCountByLikeNameAndState", notes = "")
    @RequestMapping(value = "/api/game/getGameSelectCountByLikeNameAndState", method = RequestMethod.POST)
    int getGameSelectCountByLikeNameAndState(@RequestBody GameDto gameDto) {
        return gameService.selectCount(new EntityWrapper<Game>().where("state = {0}", 0).like("name", gameDto.getName()));
    }

    @ApiOperation(value = "getGameEvaluationSelectPageByTypeAndStateOrderByRAND", notes = "")
    @RequestMapping(value = "/api/game/getGameEvaluationSelectPageByTypeAndStateOrderByRAND", method = RequestMethod.GET)
    FungoPageResultDto<GameEvaluationDto> getGameEvaluationSelectPageByTypeAndStateOrderByRAND() {
        Page<GameEvaluation> type = gameEvaluationServiceImap.selectPage(new Page<GameEvaluation>(1, 6), new EntityWrapper<GameEvaluation>().eq("type", 2).and("state != {0}", -1).orderBy("RAND()"));
        FungoPageResultDto<GameEvaluationDto> re = new FungoPageResultDto<GameEvaluationDto>();
//        PageTools.pageToResultDto(re, type);
        List<GameEvaluation> data = type.getRecords();
        List<GameEvaluationDto> gameEvaluationDtos = new ArrayList<>();
        for (GameEvaluation gameEvaluation : data) {
            GameEvaluationDto gameEvaluationDto = new GameEvaluationDto();
            BeanUtils.copyProperties(gameEvaluation, gameEvaluationDto);
            gameEvaluationDtos.add(gameEvaluationDto);
        }
        re.setData(gameEvaluationDtos);
        PageTools.pageToResultDto(re, type);
        return re;
    }

    @ApiOperation(value = "getSelectedGames", notes = "")
    @RequestMapping(value = "/api/game/getSelectedGames", method = RequestMethod.GET)
    ResultDto<CardIndexBean> getSelectedGames() {
        CardIndexBean indexBean = new CardIndexBean();
        GameCollectionGroup co = gameCollectionGroupService.selectOne(new EntityWrapper<GameCollectionGroup>().eq("state", "0").orderBy("RAND()").last("limit 1"));
        List<GameCollectionItem> ilist = new ArrayList<>();
        if (co != null) {
            ilist = this.gameCollectionItemService.selectList(new EntityWrapper<GameCollectionItem>().eq("group_id", co.getId()).eq("show_state", "1").last("limit 3").orderBy("sort", false));
        }
        if (ilist.size() == 0) {
            return null;
        }

        ArrayList<CardDataBean> gameDateList = new ArrayList<>();
        for (GameCollectionItem gameCollectionItem : ilist) {
            CardDataBean bean = new CardDataBean();
            Game game = this.gameService.selectById(gameCollectionItem.getGameId());
            bean.setMainTitle(game.getName());
            String intro = game.getIntro();
            bean.setSubtitle(intro.length() > 25 ? intro.substring(0, 25) : intro);
            bean.setImageUrl(game.getIcon());
//            迁移微服务
//            2019-05-18
//            lyc
//            List<TagBean> tags = tagDao.getSortTags(game.getId());
            List<GameTag> gameTags = gameTagService.selectList(new EntityWrapper<GameTag>().setSqlSelect("tag_id as tagId").eq("game_id", game.getId()));
            List<String> strings = new ArrayList<>();
            if (gameTags != null && gameTags.size() > 0) {
                for (GameTag gameTag : gameTags) {
                    strings.add(gameTag.getTagId());
                }
            }
            List<BasTag> tags = basTagService.selectList(new EntityWrapper<BasTag>().in("id", strings));
            //List<TagBean> tags = iEvaluateProxyService.getSortTags(strings);
            String tag = "";
            if (tags.size() > 0) {
                for (BasTag tagBean : tags) {
                    tag += tagBean.getName() + " ";
                }
            }
            bean.setLowerLeftCorner(tag);
            bean.setActionType("1");
            bean.setTargetId(game.getId());
            bean.setTargetType(3);
            gameDateList.add(bean);
        }
        indexBean.setCardName("大家都在玩");
        indexBean.setCardType(7);
        indexBean.setOrder(8);
        indexBean.setDataList(gameDateList);
        indexBean.setSize(gameDateList.size());
        indexBean.setUprightFlag(true);

        return ResultDto.success(indexBean);
    }

    @ApiOperation(value = "getRateData", notes = "")
    @RequestMapping(value = "/api/game/getRateData", method = RequestMethod.GET)
    ResultDto<HashMap<String, BigDecimal>> getRateData(@RequestParam("gameId") String gameId) {
        HashMap<String, BigDecimal> rateData = gameDao.getRateData1(gameId);
        return ResultDto.success(rateData);
    }


/****************************************************comunity**********************************************************************/
    /**
     * 获取游戏平均分
     *
     * @param gameId
     * @param state
     * @return
     */
    @ApiOperation(value = "获取游戏平均分", notes = "")
    @RequestMapping(value = "/api/game/average", method = RequestMethod.POST)
    double selectGameAverage(String gameId, Integer state) {
        return iGameService.getGameRating(gameId);
    }

    /**
     * 根据游戏id和状态查询游戏详情
     *
     * @param gameId
     * @param state
     * @return
     */
    @ApiOperation(value = "根据游戏id和状态查询游戏详情", notes = "")
    @RequestMapping(value = "/api/game/details", method = RequestMethod.POST)
    ResultDto<GameDto> selectGameDetails(String gameId, Integer state) {
        Game game = gameService.selectOne(new EntityWrapper<Game>().eq("id", gameId).eq("state", state));
        GameDto gameDto = new GameDto();
        BeanUtils.copyProperties(game, gameDto);
        return ResultDto.success(gameDto);
    }


    /**
     * 根据游戏ids查询游戏详情集合
     *
     * @param gameId 1,2,3,4
     * @return
     */
    @ApiOperation(value = "根据游戏ids查询游戏详情集合", notes = "")
    @RequestMapping(value = "/api/game/detailsByIds", method = RequestMethod.POST)
    ResultDto<List<GameDto>> selectGameDetailsByIds(String gameIds) {
        if (org.apache.commons.lang3.StringUtils.isBlank(gameIds)) {
            return ResultDto.success();
        }

        List<GameDto> gameDtoList = new ArrayList<>();

        List<Game> gameList = gameService.selectList(new EntityWrapper<Game>().in("id", gameIds));

        if (null != gameList && !gameList.isEmpty()) {

            for (Game game : gameList) {
                GameDto gameDto = new GameDto();
                BeanUtils.copyProperties(game, gameDto);
                gameDtoList.add(gameDto);
            }

        }
        return ResultDto.success(gameDtoList);
    }


    /**
     * 查询游戏评论表中发表评论大于X条，前Y名的用户
     *
     * @param x
     * @param y
     * @param wathMbsSet
     * @return
     */
    @ApiOperation(value = "查询游戏评论表中发表评论大于X条，前Y名的用户", notes = "")
    @RequestMapping(value = "/api/game/getRecommendMembersFromEvaluation", method = RequestMethod.POST)
    ResultDto<List<String>> getRecommendMembersFromEvaluation(@RequestParam("x") Integer x, @RequestParam("y") Integer y, @RequestParam("wathMbsSet") List<String> wathMbsSet) {
        List<String> sendCommentMembers = gameEvaluationDao.getRecommendMembersFromEvaluation(x,
                y, wathMbsSet);
        return ResultDto.success(sendCommentMembers);
    }

    /**
     * 根据游戏id查询参与评论的用户
     *
     * @param gameId
     * @param state
     * @return
     */
    @ApiOperation(value = "根据游戏id查询参与评论的用户", notes = "")
    @RequestMapping(value = "/api/game/getMemberOrder", method = RequestMethod.POST)
    ResultDto<List<MemberPulishFromCommunity>> getMemberOrder(String gameId, Integer state) {
        Map<String, Object> map = new HashMap<>();
        Page page = new Page<>(1, 6);
        map.put("gameId", gameId);
        List<MemberPulishFromCommunity> list = gameEvaluationDao.getMemberOrder(page, map);
        return ResultDto.success(list);
    }

    /*******************************************2019-05-18**************************************************************/
    @ApiOperation(value = "gameEvaluationService.selectOne", notes = "")
    @RequestMapping(value = "/api/game/getGameEvaluationSelectOne", method = RequestMethod.POST)
    ResultDto<GameEvaluationDto> getGameEvaluationSelectOne(@RequestParam("memberId") String memberId, @RequestParam("targetId") String targetId) {
        GameEvaluation gameEvaluation = gameEvaluationServiceImap.selectOne(new EntityWrapper<GameEvaluation>().eq("member_id", memberId).eq("game_id", targetId).eq("state", 0));
        GameEvaluationDto gameEvaluationDto = new GameEvaluationDto();
        BeanUtils.copyProperties(gameEvaluation, gameEvaluationDto);
        return ResultDto.success(gameEvaluationDto);
    }

    @ApiOperation(value = "gameEvaluationService.selectById", notes = "")
    @RequestMapping(value = "/api/game/getGameEvaluationSelectById", method = RequestMethod.POST)
    ResultDto<GameEvaluationDto> getGameEvaluationSelectById(@RequestParam("commentId") String commentId) {
        GameEvaluation gameEvaluation = gameEvaluationServiceImap.selectById(commentId);
        GameEvaluationDto gameEvaluationDto = new GameEvaluationDto();
        BeanUtils.copyProperties(gameEvaluation, gameEvaluationDto);
        return ResultDto.success(gameEvaluationDto);
    }

    @ApiOperation(value = "getPreGameEvaluation上一评论", notes = "")
    @RequestMapping(value = "/api/game/getPreGameEvaluation", method = RequestMethod.POST)
    ResultDto<GameEvaluationDto> getPreGameEvaluation(@RequestParam("createdAt") String createdAt, @RequestParam("id") String id) {
        GameEvaluation pre = gameEvaluationServiceImap.selectOne(Condition.create().setSqlSelect("id").eq("type", 2).and("state != {0}", -1).gt("created_at", createdAt).ne("id", id).orderBy("concat(sort,created_at)").last("limit 1"));
        GameEvaluationDto gameEvaluationDto = new GameEvaluationDto();
        BeanUtils.copyProperties(pre, gameEvaluationDto);
        return ResultDto.success(gameEvaluationDto);
    }

    @ApiOperation(value = "getNextGameEvaluation下一评论", notes = "")
    @RequestMapping(value = "/api/game/getNextGameEvaluation", method = RequestMethod.POST)
    ResultDto<GameEvaluationDto> getNextGameEvaluation(@RequestParam("createdAt") String createdAt, @RequestParam("id") String id) {
        GameEvaluation next = gameEvaluationServiceImap.selectOne(Condition.create().setSqlSelect("id").eq("type", 2).and("state != {0}", -1).le("created_at", createdAt).ne("id", id).orderBy("concat(sort,created_at)", false).last("limit 1"));
        GameEvaluationDto gameEvaluationDto = new GameEvaluationDto();
        BeanUtils.copyProperties(next, gameEvaluationDto);
        return ResultDto.success(gameEvaluationDto);
    }


    @ApiOperation(value = "getEvaluationEntityWrapper", notes = "")
    @RequestMapping(value = "/api/game/getEvaluationEntityWrapper", method = RequestMethod.POST)
    ResultDto<List<GameEvaluationDto>> getEvaluationEntityWrapper(@RequestParam("memberId") String memberId, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        EntityWrapper<GameEvaluation> evaluationEntityWrapper = new EntityWrapper<>();
        evaluationEntityWrapper.eq("member_id", memberId);
        evaluationEntityWrapper.between("updated_at", startDate, endDate);
        evaluationEntityWrapper.eq("state", 0);
        //type  0:普通 1:热门 2:精华
        evaluationEntityWrapper.in("type", new Integer[]{1, 2});
        List<GameEvaluation> gameEvaluationsList = gameEvaluationServiceImap.selectList(evaluationEntityWrapper);
        List<GameEvaluationDto> gameEvaluationDtos = new ArrayList<>();
        if (gameEvaluationsList != null && gameEvaluationsList.size() > 0) {
            for (GameEvaluation gameEvaluation : gameEvaluationsList) {
                GameEvaluationDto gameEvaluationDto = new GameEvaluationDto();
                BeanUtils.copyProperties(gameEvaluation, gameEvaluationDto);
                gameEvaluationDtos.add(gameEvaluationDto);
            }
        }
        return ResultDto.success(gameEvaluationDtos);
    }


    @ApiOperation(value = "getEvaluationEntityWrapperByPageDtoAndMemberId", notes = "")
    @RequestMapping(value = "/api/game/getEvaluationEntityWrapperByPageDtoAndMemberId", method = RequestMethod.POST)
    FungoPageResultDto<GameEvaluationDto> getEvaluationEntityWrapperByPageDtoAndMemberId(@RequestBody EvaluationInputPageDto pageDto, @RequestParam("memberId") String memberId) {
        Wrapper<GameEvaluation> commentWrapper = new EntityWrapper<GameEvaluation>();
        commentWrapper.eq("game_id", pageDto.getGame_id());
        commentWrapper.and("state !={0}", -1);

        if ("mine".equals(pageDto.getFilter())) {//社区主
            commentWrapper.eq("member_id", memberId);
        }
        //pageDto.getSort()==0||
        if (pageDto.getSort() == 1) {//排序
            commentWrapper.orderBy("created_at", true);
        } else if (pageDto.getSort() == 0 || pageDto.getSort() == 2) {
            commentWrapper.orderBy("created_at", false);
        } else if (pageDto.getSort() == 3) {
            commentWrapper.groupBy("id").orderBy("sum(like_num+reply_num)", true);//按照点赞数和回复数排序
        } else if (pageDto.getSort() == 4) {
            commentWrapper.groupBy("id").orderBy("sum(like_num+reply_num)", false);
        }

        Page<GameEvaluation> page = gameEvaluationServiceImap.selectPage(new Page<>(pageDto.getPage(), pageDto.getLimit()), commentWrapper);

        FungoPageResultDto<GameEvaluationDto> re = new FungoPageResultDto<GameEvaluationDto>();
//        PageTools.pageToResultDto(re, page);

        List<GameEvaluation> list = page.getRecords();

        List<GameEvaluationDto> gameEvaluationDtos = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (GameEvaluation gameEvaluation : list) {
                GameEvaluationDto gameEvaluationDto = new GameEvaluationDto();
                BeanUtils.copyProperties(gameEvaluation, gameEvaluationDto);
                gameEvaluationDtos.add(gameEvaluationDto);
            }
        }
        re.setData(gameEvaluationDtos);
        PageTools.pageToResultDto(re, page);
        return re;
    }


    @GetMapping("/api/game/selectedGames")
    public CardIndexBean selectedGames() {
        CardIndexBean indexBean = gameCollectionItemService.selectedGames();
        return indexBean;
    }

    @GetMapping("/api/game/selectGameEvaluationPage")
    public FungoPageResultDto<GameEvaluationDto> selectGameEvaluationPage() {
        Page<GameEvaluation> page = gameEvaluationService.selectPage(new Page<GameEvaluation>(1, 6), new EntityWrapper<GameEvaluation>().eq("type", 2).and("state != {0}", -1).orderBy("RAND()"));
        FungoPageResultDto<GameEvaluationDto> re = new FungoPageResultDto<GameEvaluationDto>();
//        PageTools.pageToResultDto(re, page);
        List<GameEvaluation> list = page.getRecords();
        List<GameEvaluationDto> gameEvaluationDtos = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (GameEvaluation gameEvaluation : list) {
                GameEvaluationDto gameEvaluationDto = new GameEvaluationDto();
                BeanUtils.copyProperties(gameEvaluation, gameEvaluationDto);
                gameEvaluationDtos.add(gameEvaluationDto);
            }
        }
        re.setData(gameEvaluationDtos);
        PageTools.pageToResultDto(re, page);
        return re;
    }

    //---------

}
