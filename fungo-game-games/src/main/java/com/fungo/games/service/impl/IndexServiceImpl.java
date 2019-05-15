package com.fungo.games.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.games.dao.GameDao;
import com.fungo.games.entity.Game;
import com.fungo.games.entity.GameCollectionGroup;
import com.fungo.games.entity.GameCollectionItem;
import com.fungo.games.service.GameCollectionGroupService;
import com.fungo.games.service.GameCollectionItemService;
import com.fungo.games.service.GameService;
import com.fungo.games.service.IIndexService;
import com.game.common.api.InputPageDto;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.repo.cache.facade.FungoCacheIndex;
import com.game.common.util.PageTools;
import com.game.common.util.date.DateTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class IndexServiceImpl implements IIndexService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexServiceImpl.class);

//    @Autowired
//    private BannerService bannerService;
//
//    @Autowired
//    private CmmPostService postService;
//
    @Autowired
    private GameService gameService;
//
//    @Autowired
//    private IUserService userService;
//
//    @Autowired
//    private CmmCommunityService communityService;
//
//    @Autowired
//    private GameEvaluationService gameEvaluationService;
//
    @Autowired
    private GameCollectionGroupService gameCollectionGroupService;

    @Autowired
    private GameCollectionItemService gameCollectionItemService;
//
//    @Autowired
//    private BasTagDao tagDao;
//
    @Autowired
    private GameDao gameDao;
//
    @Autowired
    private FungoCacheIndex fungoCacheIndex;
//
//    @Autowired
//    private SysVersionService sysVersionService;





    @Override
    public FungoPageResultDto<Map<String, Object>> pcGameGroup(InputPageDto input) {

        String keySuffix = JSON.toJSONString(input);
        FungoPageResultDto<Map<String, Object>> re = (FungoPageResultDto<Map<String, Object>>) fungoCacheIndex.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_INDEX_RECOMMEND_PC_GAMEGROUP,
                keySuffix);
        if (null != re && null != re.getData() && re.getData().size() > 0) {
            return re;
        }

//		ResultDto<List<Map<String,Object>>> re=new ResultDto<List<Map<String,Object>>>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Page<GameCollectionGroup> gpage = gameCollectionGroupService.selectPage(new Page<>(input.getPage(), input.getLimit()), new EntityWrapper<GameCollectionGroup>().eq("state", "0").orderBy("sort", false));

//		List<GameCollectionGroup> clist =gameCollectionGroupService.selectList(new EntityWrapper<GameCollectionGroup>().eq("state", "0").orderBy("sort", false));
        List<GameCollectionGroup> clist = gpage.getRecords();
        for (Iterator iterator = clist.iterator(); iterator.hasNext(); ) {
            GameCollectionGroup gameCollectionGroup = (GameCollectionGroup) iterator.next();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("topic_name", gameCollectionGroup.getName());
            map.put("groupId", gameCollectionGroup.getId());
            map.put("is_more", false);
            List<GameCollectionItem> ilist = this.gameCollectionItemService.selectList(new EntityWrapper<GameCollectionItem>().eq("group_id", gameCollectionGroup.getId()).eq("show_state", "1").orderBy("sort", false));
            if (ilist == null || ilist.size() == 0) {
                continue;
            }
            List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
            for (GameCollectionItem gameCollectionItem : ilist) {
                if (lists.size() == 4) {
                    map.put("is_more", true);
                    break;
                }
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
            list.add(map);
//			if(list.size() == 4) {
//				break;
//			}

        }
        re = new FungoPageResultDto<Map<String, Object>>();
        PageTools.pageToResultDto(re, gpage);
        re.setData(list);

        //redis cache
        fungoCacheIndex.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_INDEX_RECOMMEND_PC_GAMEGROUP,
                keySuffix, re);
        return re;
    }

}
