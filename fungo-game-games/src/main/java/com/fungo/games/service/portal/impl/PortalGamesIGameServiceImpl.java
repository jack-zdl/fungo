package com.fungo.games.service.portal.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.games.dao.GameDao;
import com.fungo.games.service.portal.PortalGamesIGameService;
import com.game.common.api.InputPageDto;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.game.GameOutPage;
import com.game.common.util.PageTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/27
 */
@Service
public class PortalGamesIGameServiceImpl implements PortalGamesIGameService {

    @Autowired
    private GameDao gameDao;

    @Override
    public FungoPageResultDto<GameOutPage> searchGamesByDownload(String userId, InputPageDto input) {
        String keySuffix = JSON.toJSONString(input);
        // @todo 缓存
        FungoPageResultDto<GameOutPage> re = null; //(FungoPageResultDto<GameOutPage>) fungoCacheGame.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_GAME_RECENTEVA + userId, keySuffix);
        if (null != re && null != re.getData() && re.getData().size() > 0) {
            return re;
        }

        Page page = new Page<>(input.getPage(), input.getLimit());
        List<HashMap<String, Object>> list = gameDao.getRecentCommentedGames(page, userId);
        List<GameOutPage> olist = new ArrayList<>();

        for (HashMap<String, Object> map : list) {
            GameOutPage out = new GameOutPage();
            out.setIcon((String) map.get("icon"));
            out.setName((String) map.get("name"));
            out.setObjectId((String) map.get("game_id"));
            HashMap<String, BigDecimal> rateData = gameDao.getRateData((String) map.get("game_id"));
            if (rateData != null) {
                if (rateData.get("avgRating") != null) {
                    out.setRating(Double.parseDouble(rateData.get("avgRating").toString()));
                } else {
                    out.setRating(0.0);
                }
            } else {
                out.setRating(0.0);
            }
            out.setComment_num((int) map.get("comment_num"));
            out.setLink_community((String) map.get("community_id"));
            out.setCategory((String) map.get("tags"));
            olist.add(out);
        }
        re = new FungoPageResultDto<GameOutPage>();
        PageTools.pageToResultDto(re, page);
        re.setData(olist);

        //redis cache
//        fungoCacheGame.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_GAME_RECENTEVA + userId, keySuffix, re);
        return re;
    }
}
