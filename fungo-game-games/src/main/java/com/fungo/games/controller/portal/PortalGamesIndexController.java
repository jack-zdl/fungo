package com.fungo.games.controller.portal;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.games.dao.GameDao;
import com.fungo.games.entity.Game;
import com.fungo.games.entity.GameCollectionGroup;
import com.fungo.games.entity.GameCollectionItem;
import com.fungo.games.entity.GameEvaluation;
import com.fungo.games.proxy.IEvaluateProxyService;
import com.fungo.games.service.*;
import com.game.common.api.InputPageDto;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.index.AmwayWallBean;
import com.game.common.repo.cache.facade.FungoCacheIndex;
import com.game.common.util.CommonUtils;
import com.game.common.util.PageTools;
import com.game.common.util.annotation.Anonymous;
import com.game.common.util.date.DateTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * PC首页
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/5/25 12:42
 */
@SuppressWarnings("all")
@RestController
@Api(value = "", description = "PC首页")
public class PortalGamesIndexController {

    @Autowired
    private IIndexService indexService;

    @Autowired
    private FungoCacheIndex fungoCacheIndex;

    @Autowired
    private IEvaluateProxyService iEvaluateProxyService;

    @Autowired
    private GameCollectionGroupService gameCollectionGroupService;

    @Autowired
    private GameCollectionItemService gameCollectionItemService;

    @Autowired
    private GameService gameService;

    @Autowired
    private GameDao gameDao;

    @Autowired
    private GameEvaluationService gameEvaluationService;

    @ApiOperation(value = "PC2.0端发现页游戏合集", notes = "")
    @RequestMapping(value = "/api/portal/games/recommend/pc/gamegroup", method = RequestMethod.POST)
    @ApiImplicitParams({
    })
    public FungoPageResultDto<Map<String, Object>> pcGameGroup(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody InputPageDto input) {
        return indexService.pcGameGroup(input);
    }


    @ApiOperation(value = "PC2.0端游戏合集详情", notes = "")
    @RequestMapping(value = "/api/portal/games/recommend/pc/groupdetail/{groupId}", method = RequestMethod.GET)
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
            List<GameCollectionItem> ilist = this.gameCollectionItemService.selectList(new EntityWrapper<GameCollectionItem>().eq("group_id", group.getId()).eq("show_state", "1").orderBy("sort", false));
//			if(ilist == null || ilist.size() == 0) {
//				continue;
//			}
            List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
            for (GameCollectionItem gameCollectionItem : ilist) {
                Game game = this.gameService.selectById(gameCollectionItem.getGameId());
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


    @ApiOperation(value = "PC2.0安利墙游戏评价列表(v2.3)", notes = "")
    @RequestMapping(value = "/api/portal/games/amwaywall/list", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<AmwayWallBean> getAmwayWallList(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody InputPageDto inputPageDto) {
        //从redis获取
        String keyPrefix = "/api/amwaywall/list";
        String keySuffix = JSON.toJSONString(inputPageDto);
        FungoPageResultDto<AmwayWallBean> re = (FungoPageResultDto<AmwayWallBean>) fungoCacheIndex.getIndexCache(keyPrefix, keySuffix);
        if (null != re && null != re.getData() && re.getData().size() > 0) {
            //return re;
        }
        re = new FungoPageResultDto<AmwayWallBean>();
        List<AmwayWallBean> list = new ArrayList<AmwayWallBean>();
        re.setData(list);
        //精选游戏评测 按照标记和发布日期排序
        Page<GameEvaluation> page = gameEvaluationService.selectPage(new Page<GameEvaluation>(inputPageDto.getPage(), inputPageDto.getLimit()), new EntityWrapper<GameEvaluation>().eq("type", 2).and("state != {0}", -1).orderBy("concat(sort,created_at)", false));
        List<GameEvaluation> plist = page.getRecords();
        for (GameEvaluation gameEvaluation : plist) {
            AmwayWallBean bean = new AmwayWallBean();

            bean.setAuthor(iEvaluateProxyService.getAuthor(gameEvaluation.getMemberId()));
            Game game = this.gameService.selectById(gameEvaluation.getGameId());



            bean.setEvaluation(CommonUtils.filterWord(gameEvaluation.getContent()));
            bean.setEvaluationId(gameEvaluation.getId());

            bean.setGameImage(game.getCoverImage());
            bean.setGameIcon(game.getIcon());
            bean.setGameId(gameEvaluation.getGameId());
            bean.setGameName(game.getName());
            bean.setRecommend(gameEvaluation.getIsRecommend().equals("1") ? true : false);
            if (gameEvaluation.getRating() != null) {
                bean.setRating(new BigDecimal(gameEvaluation.getRating()));
            }
            list.add(bean);
        }
        PageTools.pageToResultDto(re, page);

        //Redis cache
        fungoCacheIndex.excIndexCache(true, keyPrefix, keySuffix, re);
        return re;

    }

    @ApiOperation(value = "安利墙首页接口(v2.3)", notes = "")
    @RequestMapping(value = "/api/portal/games/amwaywall", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<AmwayWallBean> getAmwayWall(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody InputPageDto inputPageDto) {

        //从redis获取
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_INDEX_AMWAYWALL;
        String keySuffix = JSON.toJSONString(inputPageDto);
        FungoPageResultDto<AmwayWallBean> re = (FungoPageResultDto<AmwayWallBean>) fungoCacheIndex.getIndexCache(keyPrefix, keySuffix);
        if (null != re && null != re.getData() && re.getData().size() > 0) {
            return re;
        }

        re = new FungoPageResultDto<AmwayWallBean>();

        List<AmwayWallBean> list = new ArrayList<AmwayWallBean>();
        re.setData(list);
        //精选游戏评测
        Page<GameEvaluation> page = gameEvaluationService.selectPage(new Page<GameEvaluation>(inputPageDto.getPage(), inputPageDto.getLimit()), new EntityWrapper<GameEvaluation>().eq("type", 2).and("state != {0}", -1).orderBy("RAND()"));
        List<GameEvaluation> plist = page.getRecords();
        for (GameEvaluation gameEvaluation : plist) {
            AmwayWallBean bean = new AmwayWallBean();
            //            迁移微服务 根据用户id获取authorbean对象
//            2019-05-13
//            lyc
//            bean.setAuthor(this.userService.getAuthor(gameEvaluation.getMemberId()));
            bean.setAuthor(iEvaluateProxyService.getAuthor(gameEvaluation.getMemberId()));
            Game game = this.gameService.selectById(gameEvaluation.getGameId());
            bean.setEvaluation(CommonUtils.filterWord(gameEvaluation.getContent()));
            bean.setEvaluationId(gameEvaluation.getId());
//			ObjectMapper objectMapper = new ObjectMapper();
//			ArrayList<String> imgs=null;
//	        try {
//		    	if(gameEvaluation.getImages()!=null) {
//		    		imgs = (ArrayList<String>)objectMapper.readValue(gameEvaluation.getImages(), ArrayList.class);
//		    	}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//	        if(imgs!=null&&imgs.size()!=0) {
//				bean.setGameImage(imgs.get(0));
//	        }
            HashMap<String, BigDecimal> rateData = gameDao.getRateData(game.getId());
            if (rateData != null) {
                if (rateData.get("avgRating") != null) {
                    bean.setRating(rateData.get("avgRating"));
                } else {
                    bean.setRating(new BigDecimal(0.0));
                }
            } else {
                bean.setRating(new BigDecimal(0.0));
            }
            bean.setGameImage(game.getCoverImage());
            bean.setGameIcon(game.getIcon());
            bean.setGameId(gameEvaluation.getGameId());
            bean.setGameName(game.getName());
            bean.setRecommend(gameEvaluation.getIsRecommend().equals("1") ? true : false);
            list.add(bean);
        }

        //save redis
        fungoCacheIndex.excIndexCache(true, keyPrefix, keySuffix, re);
        return re;
    }

}
