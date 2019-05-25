package com.fungo.games.controller.portal;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.games.entity.Game;
import com.fungo.games.entity.GameEvaluation;
import com.fungo.games.proxy.IEvaluateProxyService;
import com.fungo.games.service.GameEvaluationService;
import com.fungo.games.service.GameService;
import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.index.AmwayWallBean;
import com.game.common.repo.cache.facade.FungoCacheIndex;
import com.game.common.util.CommonUtils;
import com.game.common.util.PageTools;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *  PC端门户-首页
 * @author mxf
 * @update 2019/5/5 16:26
 */
@RestController
public class PortalSystemIndexController {


    @Autowired
    private GameEvaluationService gameEvaluationService;

    @Autowired
    private GameService gameService;

    @Autowired
    private IEvaluateProxyService iEvaluateProxyService;

    @Autowired
    private FungoCacheIndex fungoCacheIndex;


    @ApiOperation(value = "安利墙游戏评价列表(v2.3)", notes = "")
    @RequestMapping(value = "/api/portal/index/games/amwaywall/list", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<AmwayWallBean> getAmwayWallList(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody InputPageDto inputPageDto) {
        //从redis获取
        String keyPrefix = "/api/portal/index/games/amwaywall/list";
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







    //--------
}
