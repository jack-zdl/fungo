package com.fungo.games.service.portal.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.games.dao.GameDao;
import com.fungo.games.entity.Game;
import com.fungo.games.entity.GameSurveyRel;
import com.fungo.games.feign.SystemFeignClient;
import com.fungo.games.service.GameService;
import com.fungo.games.service.GameSurveyRelService;
import com.fungo.games.service.portal.PortalGamesIGameService;
import com.game.common.api.InputPageDto;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.game.GameOutPage;
import com.game.common.dto.game.MyGameBean;
import com.game.common.dto.game.MyGameInputPageDto;
import com.game.common.dto.system.CircleFollowVo;
import com.game.common.enums.ActionTypeEnum;
import com.game.common.enums.CommonEnum;
import com.game.common.repo.cache.facade.FungoCacheGame;
import com.game.common.util.PageTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(PortalGamesIGameServiceImpl.class);

    @Autowired
    private GameDao gameDao;
    @Autowired
    private FungoCacheGame fungoCacheGame;
    @Autowired
    private SystemFeignClient systemFeignClient;
    @Autowired
    private GameSurveyRelService gameSurveyRelService;
    @Autowired
    private GameService gameService;

    @Override
    public FungoPageResultDto<GameOutPage> searchGamesByDownload(String userId, InputPageDto input) {
        String keySuffix = JSON.toJSONString(input);
        // @todo 缓存
        FungoPageResultDto<GameOutPage> re = (FungoPageResultDto<GameOutPage>) fungoCacheGame.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_GAME_RECENTEVA + userId, keySuffix);
        if (null != re && null != re.getData() && re.getData().size() > 0) {
            return re;
        }

        Page page = new Page<>(input.getPage(), input.getLimit());
        List<HashMap<String, Object>> list = gameDao.getGamesByDownload(page, userId);
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
        fungoCacheGame.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_GAME_RECENTEVA + userId, keySuffix, re);
        return re;
    }

    /**
     * 功能描述:
     * <p>1 因为时间原因pc这个没有添加redis缓存</p>
     * <p>查询下载的游戏</p>
     * @param:
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.game.MyGameBean>
     * @auther: dl.zhang
     * @date: 2019/8/20 11:34
     */
    @Override
    public FungoPageResultDto<MyGameBean> getMyDownloadGameList(String memberId, MyGameInputPageDto inputPage, String os) {
        FungoPageResultDto<MyGameBean> re = null;
        List<MyGameBean> list = new ArrayList<MyGameBean>();
        try {
            CircleFollowVo param = new CircleFollowVo();
            param.setActionType(ActionTypeEnum.DOWNLOAD.getKey());
            param.setMemberId(memberId);
            param.setPage( inputPage.getPage() );
            param.setLimit( inputPage.getLimit());
            FungoPageResultDto<String>  resultDto = systemFeignClient.gameListMineDownload(param);
            if(CommonEnum.SUCCESS.code().equals(String.valueOf(resultDto.getStatus()))){
                List<String> gameIds = resultDto.getData() != null ?  resultDto.getData() : new ArrayList<>();
                List<Game>  gamesList =  gameDao.getGameList(gameIds);
                gamesList.stream().forEach(game ->{
                    MyGameBean bean = new MyGameBean();
                    bean.setAndroidState(game.getAndroidState());
                    bean.setGameContent(game.getDetail());
                    bean.setGameIcon(game.getIcon());
                    bean.setGameId(game.getId());
                    bean.setGameName(game.getName());
                    bean.setIosState(game.getIosState());
                    bean.setMsgCount(0);
//                    bean.setPhoneModel(gameSurveyRel.getPhoneModel());
//                if (os.equalsIgnoreCase(bean.getPhoneModel())) {
                    list.add(bean);
                });
                re.setData(list);
                PageTools.newPageToResultDto(re, resultDto.getCount(),inputPage.getLimit(),inputPage.getPage());
            }
        }catch (Exception e){
            LOGGER.error( "game模块查询用户下载的游戏",e);
        }
        return re;
    }

    @Override
    public FungoPageResultDto<MyGameBean> getMyGameList(String memberId, MyGameInputPageDto inputPage, String os) {
        FungoPageResultDto<MyGameBean> re = null;

        re = new FungoPageResultDto<MyGameBean>();
        List<MyGameBean> list = new ArrayList<MyGameBean>();

        if (2 == inputPage.getType()) {
            Page<GameSurveyRel> page = gameSurveyRelService.selectPage(new Page<GameSurveyRel>(inputPage.getPage(),
                    inputPage.getLimit()), new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("state", 0));
            List<GameSurveyRel> plist = page.getRecords();
            for (GameSurveyRel gameSurveyRel : plist) {
                Game game = gameService.selectById(gameSurveyRel.getGameId());
                MyGameBean bean = new MyGameBean();
                bean.setAndroidState(game.getAndroidState());
                bean.setGameContent(game.getDetail());
                bean.setGameIcon(game.getIcon());
                bean.setGameId(gameSurveyRel.getGameId());
                bean.setGameName(game.getName());
                bean.setIosState(game.getIosState());
                bean.setMsgCount(0);
                bean.setPhoneModel(gameSurveyRel.getPhoneModel());
//                if (os.equalsIgnoreCase(bean.getPhoneModel())) {
                list.add(bean);
//                }
            }
            re.setData(list);
            PageTools.pageToResultDto(re, page);
        }else {
            CircleFollowVo param = new CircleFollowVo();
            param.setActionType(ActionTypeEnum.DOWNLOAD.getKey());
            param.setMemberId(memberId);
            param.setPage( inputPage.getPage() );
            param.setLimit( inputPage.getLimit());
            FungoPageResultDto<String>  resultDto = systemFeignClient.gameListMineDownload(param);
            if(resultDto != null && CommonEnum.SUCCESS.code().equals(String.valueOf(resultDto.getStatus()))){
                List<String> gameIds = resultDto.getData() != null ?  resultDto.getData() : new ArrayList<>();
                List<Game>  gamesList =  gameDao.getGameList(gameIds);
                gamesList.stream().forEach(game ->{
                    MyGameBean bean = new MyGameBean();
                    bean.setAndroidState(game.getAndroidState() == null ? 0 : game.getAndroidState()  );
                    bean.setGameContent(game.getDetail());
                    bean.setGameIcon(game.getIcon());
                    bean.setGameId(game.getId());
                    bean.setGameName(game.getName());
                    bean.setIosState(game.getIosState()== null ? 0 : game.getIosState());
                    bean.setMsgCount(0);
//                    bean.setPhoneModel(gameSurveyRel.getPhoneModel());
//                if (os.equalsIgnoreCase(bean.getPhoneModel())) {
                    list.add(bean);
                });
                re.setData(list);
                PageTools.newPageToResultDto(re, resultDto.getCount(),inputPage.getLimit(),inputPage.getPage());
            }
        }
        //redis cache
        return re;
    }
}
