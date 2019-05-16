package com.fungo.games.controller;



import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.games.dao.GameEvaluationDao;
import com.fungo.games.entity.Game;
import com.fungo.games.entity.GameEvaluation;
import com.fungo.games.entity.GameReleaseLog;
import com.fungo.games.entity.GameSurveyRel;
import com.fungo.games.helper.MQClient;
import com.fungo.games.service.GameReleaseLogService;
import com.fungo.games.service.GameService;
import com.fungo.games.service.GameSurveyRelService;
import com.fungo.games.service.IGameService;
import com.fungo.games.service.impl.GameEvaluationServiceImap;
import com.game.common.bean.MemberPulishFromCommunity;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.game.*;
import com.game.common.util.PageTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * feignService 调用中心
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
    private GameService gameService;

    @Autowired
    private GameEvaluationDao gameEvaluationDao;

    @Autowired
    private GameEvaluationServiceImap gameEvaluationServiceImap;

    @Autowired
    private GameReleaseLogService gameReleaseLogService;

/****************************************************ActionController**********************************************************************/

    @ApiOperation(value = "更新计数器", notes = "")
    @RequestMapping(value = "/api/update/counter", method = RequestMethod.POST)
    public Boolean updateCounter(@RequestBody Map<String,String> map) {
//        根据表名(动态)修改
        return iGameService.updateCountor(map);
    }

    @ApiOperation(value = "被点赞用户的id", notes = "")
    @RequestMapping(value = "/api/getMemberIdByTargetId", method = RequestMethod.POST)
    String getMemberIdByTargetId(@RequestBody Map<String, String> map){
        return iGameService.getMemberIdByTargetId(map);
    }
/****************************************************System**********************************************************************/

    /**
     * 查询游戏测试会员关联表总数
     * @param gameSurveyRelDto
     * @return
     */
    @ApiOperation(value = "查询游戏测试会员关联表总数", notes = "")
    @RequestMapping(value = "/api/selectCount", method = RequestMethod.POST)
    int gameSurveySelectCount(@RequestBody GameSurveyRelDto gameSurveyRelDto){
//        GameSurveyRel gameSurveyRel = new GameSurveyRel();
//        BeanUtils.copyProperties(gameSurveyRelDto, gameSurveyRel);
        HashMap<String, Object> param = new HashMap<String, Object>();
        gameSurveyFun(gameSurveyRelDto, param);
        return gameSurveyRelService.selectCount(new EntityWrapper<GameSurveyRel>().allEq(param));
    }

    /**
     * 查询游戏版本日志审批总数
     * @param gameReleaseLogDto
     * @return
     */
    @ApiOperation(value = "查询游戏版本日志审批总数", notes = "")
    @RequestMapping(value = "/api/gameReleaseLog/selectCount", method = RequestMethod.POST)
    int gameReleaseLogSelectCount(@RequestBody GameReleaseLogDto gameReleaseLogDto){
        HashMap<String, Object> param = new HashMap<String, Object>();
        gameReleaseLogFun(gameReleaseLogDto, param);
        return gameReleaseLogService.selectCount(new EntityWrapper<GameReleaseLog>().allEq(param));
    }

    /**
     * 查询游戏评价总数
     * @param gameEvaluationDto
     * @return
     */
    @ApiOperation(value = "查询游戏评价总数", notes = "")
    @RequestMapping(value = "/api/gameEvaluation/selectCount", method = RequestMethod.POST)
    int gameEvaluationSelectCount(@RequestBody GameEvaluationDto gameEvaluationDto){
        HashMap<String, Object> param = new HashMap<String, Object>();
        gameEvaluationFun(gameEvaluationDto, param);
        return gameEvaluationServiceImap.selectCount(new EntityWrapper<GameEvaluation>().allEq(param));
    }


    /**
     * 用户游戏评测精品数
     * @return
     */
    @ApiOperation(value = "用户游戏评测精品数", notes = "")
    @RequestMapping(value = "/api/game/getUserGameReviewBoutiqueNumber", method = RequestMethod.POST)
    ResultDto<Map<String,Object>> getUserGameReviewBoutiqueNumber(){
        Map<String, Object> userGameReviewBoutiqueNumber = gameEvaluationDao.getUserGameReviewBoutiqueNumber();
        return ResultDto.success(userGameReviewBoutiqueNumber);
    }

    /**
     * 游戏评价的分页查询
     * @param gameEvaluationDto
     * @return
     */
    @ApiOperation(value = "游戏评价的分页查询", notes = "")
    @RequestMapping(value = "/api/evaluation/getGameEvaluationPage", method = RequestMethod.POST)
    FungoPageResultDto<GameEvaluationDto> getGameEvaluationPage(@RequestBody GameEvaluationDto gameEvaluationDto){
        List<GameEvaluationDto> gameEvaluationList = null;
        FungoPageResultDto<GameEvaluationDto> fungoPageResultDto = new FungoPageResultDto<>();
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

                Page<GameEvaluation> cmmPostPageSelect = this.gameEvaluationServiceImap.selectPage(gameEvaluationPage, evaluationWrapper);

                if (null != cmmPostPageSelect) {
                    PageTools.pageToResultDto(fungoPageResultDto, cmmPostPageSelect);
                    selectRecords = cmmPostPageSelect.getRecords();
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
        Page<GameEvaluationDto> gameEvaluationDtoPage = new Page<>();
        gameEvaluationDtoPage.setRecords(gameEvaluationList);
        fungoPageResultDto.setData(gameEvaluationList);
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
     * @param gameSurveyDto
     * @return
     */
    @SuppressWarnings("all")
    @ApiOperation(value = "游戏测试会员关联表的分页查询", notes = "")
    @RequestMapping(value = "/api/evaluation/getGameSurveyRelPage", method = RequestMethod.POST)
    FungoPageResultDto<GameSurveyRelDto> getGameSurveyRelPage(@RequestBody GameSurveyRelDto gameSurveyDto){
        List<GameSurveyRelDto> gameSurveyList = null;
        FungoPageResultDto<GameSurveyRelDto> fungoPageResultDto = new FungoPageResultDto<>();
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

                Page<GameSurveyRel> gameSurveyRelPageSelect = this.gameSurveyRelService.selectPage(gameSurveyPage, surveyWrapper);

                if (null != gameSurveyRelPageSelect) {
                    PageTools.pageToResultDto(fungoPageResultDto, gameSurveyRelPageSelect);
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
        Page<GameSurveyRelDto> gameEvaluationDtoPage = new Page<>();
        gameEvaluationDtoPage.setRecords(gameSurveyList);
        fungoPageResultDto.setData(gameSurveyList);
        return fungoPageResultDto;
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
    FungoPageResultDto<GameReleaseLogDto> getGameReleaseLogPage(@RequestBody GameReleaseLogDto gameReleaseLogDto){
        List<GameReleaseLogDto> gameReleaseLogList = null;
        FungoPageResultDto<GameReleaseLogDto> fungoPageResultDto = new FungoPageResultDto<>();
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

                Page<GameReleaseLog> gameSurveyRelPageSelect = this.gameReleaseLogService.selectPage(gameReleaseLogPage, gameReleaseLogWrapper);

                if (null != gameSurveyRelPageSelect) {
                    PageTools.pageToResultDto(fungoPageResultDto, gameSurveyRelPageSelect);
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
        Page<GameReleaseLogDto> gameReleaseLogDtoPage = new Page<>();
        gameReleaseLogDtoPage.setRecords(gameReleaseLogList);
        fungoPageResultDto.setData(gameReleaseLogList);
        return fungoPageResultDto;
    }

    @ApiOperation(value = "根据游戏对象查询集合", notes = "")
    @RequestMapping(value = "/api/geme/getGamePage", method = RequestMethod.POST)
    FungoPageResultDto<GameDto> getGamePage(@RequestBody GameDto gameDto){
        List<GameDto> gameList = null;
        FungoPageResultDto<GameDto> fungoPageResultDto = new FungoPageResultDto<>();
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
            //根据修改时间倒叙
            gameWrapper.orderBy("updated_at", false);

            List<Game> selectRecords = null;

            if (null != gamePage) {

                Page<Game> gamePageSelect = this.gameService.selectPage(gamePage, gameWrapper);

                if (null != gamePageSelect) {
                    PageTools.pageToResultDto(fungoPageResultDto, gamePageSelect);
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
        Page<GameDto> gameDtoPage = new Page<>();
        gameDtoPage.setRecords(gameList);
        fungoPageResultDto.setData(gameList);
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
    FungoPageResultDto<GameOutBean> getGameList(@RequestBody GameItemInput input){
        return iGameService.getGameList1(input);
    }


/****************************************************comunity**********************************************************************/
    /**
     * 获取游戏平均分
     * @param gameId
     * @param state
     * @return
     */
    @ApiOperation(value = "获取游戏平均分", notes = "")
    @RequestMapping(value = "/api/game/average", method = RequestMethod.POST)
    double selectGameAverage(String gameId,Integer state){
        return iGameService.getGameRating(gameId);
    }

    /**
     * 根据游戏id和状态查询游戏详情
     * @param gameId
     * @param state
     * @return
     */
    @ApiOperation(value = "根据游戏id和状态查询游戏详情", notes = "")
    @RequestMapping(value = "/api/game/details", method = RequestMethod.POST)
    ResultDto<GameDto> selectGameDetails(String gameId,Integer state){
        Game game = gameService.selectOne(new EntityWrapper<Game>().eq("id", gameId).eq("state", state));
        GameDto gameDto = new GameDto();
        BeanUtils.copyProperties(game,gameDto);
        return ResultDto.success(gameDto);
    }

    /**
     * 查询游戏评论表中发表评论大于X条，前Y名的用户
     * @param x
     * @param y
     * @return
     */
    @ApiOperation(value = "查询游戏评论表中发表评论大于X条，前Y名的用户", notes = "")
    @RequestMapping(value = "/api/game/getRecommendMembersFromEvaluation", method = RequestMethod.POST)
    ResultDto<List<String>> getRecommendMembersFromEvaluation(Integer x,Integer y){
        List<String> wathMbsSet = new ArrayList<>();
        List<String> sendCommentMembers = gameEvaluationDao.getRecommendMembersFromEvaluation(x,
                y, wathMbsSet);
        return ResultDto.success(sendCommentMembers);
    }

    /**
     * 根据游戏id查询参与评论的用户
     * @param gameId
     * @param state
     * @return
     */
    @ApiOperation(value = "根据游戏id查询参与评论的用户", notes = "")
    @RequestMapping(value = "/api/game/getMemberOrder", method = RequestMethod.POST)
    ResultDto<List<MemberPulishFromCommunity>> getMemberOrder(String gameId,Integer state){
        Map<String, Object> map = new HashMap<>();
        Page page = new Page<>(1, 6);
        map.put("gameId", gameId);
        List<MemberPulishFromCommunity> list = gameEvaluationDao.getMemberOrder(page, map);
        return ResultDto.success(list);
    }








}
