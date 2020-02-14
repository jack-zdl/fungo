package com.fungo.games.controller.portal;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.games.dao.GameDao;
import com.fungo.games.entity.Game;
import com.fungo.games.entity.GameCollectionGroup;
import com.fungo.games.entity.GameCollectionItem;
import com.fungo.games.service.*;
import com.game.common.api.InputPageDto;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.game.GameInputPageDto;
import com.game.common.dto.game.GameOut;
import com.game.common.dto.game.GameOutPage;
import com.game.common.dto.game.TagOutPage;
import com.game.common.repo.cache.facade.FungoCacheIndex;
import com.game.common.util.StringUtil;
import com.game.common.util.annotation.Anonymous;
import com.game.common.util.annotation.MD5ParanCheck;
import com.game.common.util.date.DateTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  PC游戏
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/5/25 11:54
 */
@RestController
@Api(value = "", description = "PC游戏")
public class PortalGamesGameController {

    @Autowired
    private IGameService gameService;
    @Autowired
    GameService gameServiceImpl;
    @Autowired
    private IIndexService indexService;
    @Autowired
    private FungoCacheIndex fungoCacheIndex;
    @Autowired
    private GameCollectionGroupService gameCollectionGroupService;
    @Autowired
    private GameCollectionItemService gameCollectionItemService;
    @Autowired
    private GameDao gameDao;


    @ApiOperation(value = "游戏详情", notes = "")
    @RequestMapping(value = "/api/portal/games/content/game/number/{gameId}", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameId", value = "游戏id", paramType = "path", dataType = "string"),
    })
    public ResultDto<GameOut> getGameDetailByNumber(@Anonymous MemberUserProfile memberUserPrefile, HttpServletRequest request, @PathVariable("gameId") String gameId) {
        String memberId = "";
        String os = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        os = (String) request.getAttribute("os");
        if (StringUtils.isBlank(os)) {
            os = request.getHeader("os");
        }
        try {
            return gameService.getGameDetailByNumber(gameId, memberId, os);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDto.error("-1", "操作失败");
        }
    }

    @ApiOperation(value = "PC2.0游戏详情(2.4修改/api/content/evaluations|)", notes = "")
    @RequestMapping(value = "/api/portal/games/content/game/{gameId}", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameId", value = "游戏id", paramType = "path", dataType = "string"),
    })
    @MD5ParanCheck()
    public ResultDto<GameOut> getGameDetail(@Anonymous MemberUserProfile memberUserPrefile, HttpServletRequest request, @PathVariable("gameId") String gameId) {
        String memberId = "";
        String os = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        os = (String) request.getAttribute("os");
        try {
            return gameService.getGameDetail(gameId, memberId, os);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDto.error("-1", "操作失败");
        }
    }

    @ApiOperation(value = "PC2.0官方游戏分类", notes = "")
    @RequestMapping(value = "/api/portal/games/recommend/tag/game", method = RequestMethod.GET)
    @ApiImplicitParams({
    })
    public ResultDto<List<TagOutPage>> getGameTags(@Anonymous MemberUserProfile memberUserPrefile) {
        return gameService.getGameTags();
    }


    @ApiOperation(value = "获取最近评论的游戏(2.4.3)", notes = "")
    @RequestMapping(value = "/api/portal/games/content/game/recenteva", method = RequestMethod.POST)
    @ApiImplicitParams({
    })
    public FungoPageResultDto<GameOutPage> recentEvaluatedGamesByMember(MemberUserProfile memberUserPrefile, @RequestBody InputPageDto input) {

        String userId = memberUserPrefile.getLoginId();
        return gameService.recentEvaluatedGamesByMember(userId, input);
    }

    @ApiOperation(value = "PC2.0游戏列表", notes = "")
    @RequestMapping(value = "/api/portal/games/content/games", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "当前页数", paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "sort", value = "排序字段", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "tag", value = "游戏分类", paramType = "path", dataType = "string")
    })
    public FungoPageResultDto<GameOutPage> getGameList(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody GameInputPageDto gameInputDto, HttpServletRequest request) {

        String memberId = "";
        String os = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        os = (String) request.getAttribute("os");

        return gameService.getGameList(gameInputDto, memberId, os);
    }


    @ApiOperation(value = "pc端发现页游戏合集", notes = "")
    @RequestMapping(value = "/api/recommend/pc/gamegroup", method = RequestMethod.POST)
    @ApiImplicitParams({
    })
    @MD5ParanCheck(param = {"page","limit"})
    public FungoPageResultDto<Map<String, Object>> pcGameGroup(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody InputPageDto input) {
        return indexService.pcGameGroup(input);
    }


    @ApiOperation(value = "pc端游戏合集详情", notes = "")
    @GetMapping(value = "/api/recommend/pc/groupdetail/{groupId}")
    @ApiImplicitParams({
    })
    public ResultDto<Map<String, Object>> groupDetail(@Anonymous MemberUserProfile memberUserPrefile, @PathVariable("groupId") String groupId) {


        Map<String, Object> map = null;
        ResultDto<Map<String, Object>> resultMap = null;

        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_INDEX_RECOMMEND_PC_GROUPDETAIL;
        map = (Map<String, Object>) fungoCacheIndex.getIndexCache(keyPrefix, groupId);

        if (null != map) {
            resultMap = ResultDto.success(map);
            return resultMap;
        }

        GameCollectionGroup group = gameCollectionGroupService.selectById(groupId);
        map = new HashMap<>();

        if (group != null) {
            map.put("topic_name", group.getName());
            List<GameCollectionItem> ilist = this.gameCollectionItemService.selectList(new EntityWrapper<GameCollectionItem>().eq("group_id", group.getId()).eq("show_state", "1")
                    .orderBy("sort", false));
//			if(ilist == null || ilist.size() == 0) {
//				continue;
//			}
            List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
            for (GameCollectionItem gameCollectionItem : ilist) {
                Game game = this.gameServiceImpl.selectById(gameCollectionItem.getGameId());
                HashMap<String, BigDecimal> rateData = gameDao.getRateData(game.getId());
                Map<String, Object> map1 = new HashMap<String, Object>();
                if (rateData != null) {
                    if (rateData.get("avgRating") != null) {
                        map1.put("rating", rateData.get("avgRating"));
                    } else {
                        map1.put("rating", 0.0);
                    }
                } else {
                    map1.put("rating", 0.0);
                }
                map1.put("name", game.getName());
                map1.put("tag", game.getTags());
                map1.put("cover_image", game.getCoverImage());
                map1.put("icon", game.getIcon());
                map1.put("objectId", game.getId());
                map1.put("createdAt", DateTools.fmtDate(game.getCreatedAt()));
                map1.put("updatedAt", DateTools.fmtDate(game.getUpdatedAt()));
                map1.put("gameIdtSn", game.getGameIdtSn());
                map1.put("hot_value", 100);
                lists.add(map1);
            }
            map.put("game_list", lists);

        }

        resultMap = ResultDto.success(map);
        //redis cacahe
        fungoCacheIndex.excIndexCache(true, keyPrefix, groupId, map);

        return resultMap;
    }


    @ApiOperation(value = "最近浏览游戏列表", notes = "")
    @RequestMapping(value = "/api/portal/games/content/viewGames", method = RequestMethod.GET)
    public ResultDto<List<GameOutPage>> viewGames(@Anonymous MemberUserProfile memberUserPrefile) {
        String memberId = "";
        String os = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        if(StringUtil.isNull(memberId)){
            return ResultDto.error("-1","请登录");
        }
        //"cec9c9dfe70b4ba9b684f81e617f4833"
        return gameService.viewGames(memberId);
    }

}
