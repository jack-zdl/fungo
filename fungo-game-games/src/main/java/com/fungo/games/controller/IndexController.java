package com.fungo.games.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import com.fungo.games.dao.GameDao;
import com.fungo.games.entity.Game;
import com.fungo.games.entity.GameCollectionGroup;
import com.fungo.games.entity.GameCollectionItem;
import com.fungo.games.entity.GameEvaluation;
import com.fungo.games.facede.IEvaluateProxyService;
import com.fungo.games.service.*;
import com.game.common.api.InputPageDto;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.AuthorBean;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 首页
 * @author sam
 *
 */
@RestController
@Api(value = "", description = "首页")
public class IndexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    GameEvaluationService gameEvaluationService;

    @Autowired
    private GameCollectionGroupService gameCollectionGroupService;

    @Autowired
    private GameCollectionItemService gameCollectionItemService;

    @Autowired
    GameService gameService;

    @Autowired
    private IIndexService indexService;

    @Autowired
    private GameDao gameDao;

    @Autowired
    private FungoCacheIndex fungoCacheIndex;

    @Autowired
    private IEvaluateProxyService iEvaluateProxyService;

    @ApiOperation(value = "发现页游戏合集数据列表(2.4.3)", notes = "")
    @RequestMapping(value = "/api/recommend/topic", method = RequestMethod.POST)
    @ApiImplicitParams({
    })
    public FungoPageResultDto<Map<String, Object>> topic(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody InputPageDto input) {

        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_INDEX_RECM_TOPIC + "POST";
        String keySuffix = JSON.toJSONString(input);
        FungoPageResultDto<Map<String, Object>> re = (FungoPageResultDto<Map<String, Object>>) fungoCacheIndex.getIndexCache(keyPrefix, keySuffix);

        if (null != re && null != re.getData() && re.getData().size() > 0) {
            return re;
        }

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //游戏合集
        Page<GameCollectionGroup> gpage = gameCollectionGroupService.selectPage(new Page<>(input.getPage(), input.getLimit()), new EntityWrapper<GameCollectionGroup>().eq("state", "0").orderBy("sort", false));

        List<GameCollectionGroup> clist = gpage.getRecords();
        for (Iterator iterator = clist.iterator(); iterator.hasNext(); ) {
            GameCollectionGroup gameCollectionGroup = (GameCollectionGroup) iterator.next();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("topic_name", gameCollectionGroup.getName());
            map.put("group_id", gameCollectionGroup.getId());

            //游戏合集项
            List<GameCollectionItem> ilist = this.gameCollectionItemService.selectList(new EntityWrapper<GameCollectionItem>().eq("group_id", gameCollectionGroup.getId()).eq("show_state", "1").orderBy("sort", false));

            if (ilist == null || ilist.size() == 0) {//如果合集为空，跳过
                continue;
            }
            //获取游戏id集合
            List<String> gameIds = getgameIds(ilist);
            Map<String, Game> gameMap = gameService.listGame(gameIds);
            //获取社区id集合
            List<String> communityIds = getCommunity(gameMap);
            //获取社区推荐度
            Map<String, Integer> followeeNum = iEvaluateProxyService.listCommunityFolloweeNum(communityIds);
            List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();

            for (GameCollectionItem gameCollectionItem : ilist) {
                Game game = gameMap.get(gameCollectionItem.getGameId());
                Map<String, Object> map1 = new HashMap<String, Object>();
//				HashMap<String, BigDecimal> rateData = gameDao.getRateData(game.getId());
//				if(rateData != null) {
//					if(rateData.get("avgRating") != null) {
//						map1.put("rating",rateData.get("avgRating"));
//					}else {
//						map1.put("rating",0.0);
//					}
//				}else {
//					map1.put("rating",0.0);
//				}
//                迁移微服务 根据id获取cmmcomunity单个对象
//                2019-05-13
//                lyc
//                Wrapper wrapper = Condition.create().setSqlSelect("id,followee_num,post_num").eq("id", game.getCommunityId());
//                CmmCommunity community = communityService.selectOne(wrapper);
                Integer followeeNumValue = null;
                if (followeeNum != null){
                    followeeNumValue = followeeNum.get(game.getCommunityId());
                }


                map1.put("name", game.getName());
                map1.put("androidState", game.getAndroidState());
                map1.put("iosState", game.getIosState());
                map1.put("tag", game.getTags());
                map1.put("cover_image", game.getCoverImage());
                map1.put("icon", game.getIcon());
                map1.put("objectId", game.getId());
                map1.put("createdAt", DateTools.fmtDate(game.getCreatedAt()));
                map1.put("updatedAt", DateTools.fmtDate(game.getUpdatedAt()));

                int hot_value = 0;
                if (null != followeeNumValue) {
                    hot_value = followeeNumValue*2;
                }
                map1.put("hot_value", hot_value);

                map1.put("androidPackageName", game.getAndroidPackageName() == null ? "" : game.getAndroidPackageName());
                map1.put("game_size", game.getGameSize());
                map1.put("itunesId", game.getItunesId());
                map1.put("apkUrl", game.getApk()==null?"":game.getApk());
                lists.add(map1);
            }
            map.put("game_list", lists);
            list.add(map);
        }

        re = new FungoPageResultDto<Map<String, Object>>();
        re.setData(list);
        PageTools.pageToResultDto(re, gpage);


        //reids cache
        fungoCacheIndex.excIndexCache(true, keyPrefix, keySuffix, re);

        return re;
    }

    private List<String> getCommunity(Map<String, Game> gameMap) {
        ArrayList<String> list = new ArrayList<>();
        if(gameMap==null||gameMap.isEmpty()){
            return list;
        }
        Set<Map.Entry<String, Game>> entries = gameMap.entrySet();
        for (Map.Entry<String, Game> entry : entries) {
            Game game = entry.getValue();
            list.add(game.getCommunityId());
        }
        return list;
    }

    private List<String> getgameIds(List<GameCollectionItem> ilist) {
        ArrayList<String> list = new ArrayList<>();
        if(ilist==null||ilist.isEmpty()){
            return  list;
        }
        for (GameCollectionItem gameCollectionItem : ilist) {
            list.add(gameCollectionItem.getGameId());
        }
        return list;
    }

    /**
     *发现页游戏合集数据列表-已经不用
     * @param memberUserPrefile
     * @return
     */
    @ApiOperation(value = "发现页游戏合集数据列表", notes = "")
    @RequestMapping(value = "/api/recommend/topic", method = RequestMethod.GET)
    @ApiImplicitParams({
    })
    public ResultDto<List<Map<String, Object>>> topic(@Anonymous MemberUserProfile memberUserPrefile) {

        ResultDto<List<Map<String, Object>>> re = (ResultDto<List<Map<String, Object>>>) fungoCacheIndex.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_INDEX_RECM_TOPIC + "GET", "");
        if (null != re && null != re.getData() && re.getData().size() > 0) {
            return re;
        }
        re = new ResultDto<List<Map<String, Object>>>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<GameCollectionGroup> clist = gameCollectionGroupService.selectList(new EntityWrapper<GameCollectionGroup>().eq("state", "0").orderBy("sort", false));
        for (Iterator iterator = clist.iterator(); iterator.hasNext(); ) {
            GameCollectionGroup gameCollectionGroup = (GameCollectionGroup) iterator.next();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("topic_name", gameCollectionGroup.getName());
            List<GameCollectionItem> ilist = this.gameCollectionItemService.selectList(new EntityWrapper<GameCollectionItem>().eq("group_id", gameCollectionGroup.getId()).eq("show_state", "1").orderBy("sort", false));
            if (ilist == null || ilist.size() == 0) {
                continue;
            }
            List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
            for (GameCollectionItem gameCollectionItem : ilist) {
                Game game = this.gameService.selectById(gameCollectionItem.getGameId());
                Map<String, Object> map1 = new HashMap<String, Object>();
//				HashMap<String, BigDecimal> rateData = gameDao.getRateData(game.getId());
//				if(rateData != null) {
//					if(rateData.get("avgRating") != null) {
//						map1.put("rating",rateData.get("avgRating"));
//					}else {
//						map1.put("rating",0);
//					}
//				}else {
//					map1.put("rating",0);
//				}
//				map1.put("rating",0);
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
            list.add(map);
        }
        re.setData(list);

        //redis cache
        fungoCacheIndex.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_INDEX_RECM_TOPIC + "GET", "", re);

        return re;
    }

    @ApiOperation(value = "安利墙首页接口(v2.3)", notes = "")
    @RequestMapping(value = "/api/amwaywall", method = RequestMethod.POST)
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


    @ApiOperation(value = "安利墙游戏评价列表(v2.3)", notes = "")
    @RequestMapping(value = "/api/amwaywall/list", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<AmwayWallBean> getAmwayWallList(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody InputPageDto inputPageDto) {
        //从redis获取
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_INDEX_AMWAYWALL_LIST;
        String keySuffix = JSON.toJSONString(inputPageDto);
        FungoPageResultDto<AmwayWallBean> re = (FungoPageResultDto<AmwayWallBean>) fungoCacheIndex.getIndexCache(keyPrefix, keySuffix);
        if (null != re && null != re.getData() && re.getData().size() > 0) {
            return re;
        }
        re = new FungoPageResultDto<AmwayWallBean>();
        List<AmwayWallBean> list = new ArrayList<AmwayWallBean>();
        re.setData(list);
        //精选游戏评测 按照标记和发布日期排序
        Page<GameEvaluation> page = gameEvaluationService.selectPage(new Page<GameEvaluation>(inputPageDto.getPage(), inputPageDto.getLimit()), new EntityWrapper<GameEvaluation>().eq("type", 2).and("state != {0}", -1).orderBy("concat(sort,created_at)", false));
        List<GameEvaluation> plist = page.getRecords();
        for (GameEvaluation gameEvaluation : plist) {
            AmwayWallBean bean = new AmwayWallBean();
//            迁移微服务 根据用户id获取authorbean对象
//            2019-05-13
//            lyc
//            bean.setAuthor(this.userService.getAuthor(gameEvaluation.getMemberId()));
            AuthorBean author = iEvaluateProxyService.getAuthor(gameEvaluation.getMemberId());
            if(author==null){
                continue;
            }
            bean.setAuthor(author);
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

    @ApiOperation(value = "pc端发现页游戏合集", notes = "")
    @RequestMapping(value = "/api/recommend/pc/gamegroup", method = RequestMethod.POST)
    @ApiImplicitParams({
    })
    public FungoPageResultDto<Map<String, Object>> pcGameGroup(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody InputPageDto input) {
        return indexService.pcGameGroup(input);
    }


    @ApiOperation(value = "pc端游戏合集详情", notes = "")
    @RequestMapping(value = "/api/recommend/pc/groupdetail/{groupId}", method = RequestMethod.GET)
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

//-------
}
