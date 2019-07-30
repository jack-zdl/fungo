package com.fungo.games.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.games.dao.GameDao;
import com.fungo.games.dao.GameSurveyRelDao;
import com.fungo.games.entity.Game;
import com.fungo.games.entity.GameEvaluation;
import com.fungo.games.entity.GameReleaseLog;
import com.fungo.games.service.*;
import com.fungo.games.utils.FungoLivelyCalculateUtils;
import com.game.common.dto.GameDto;
import com.game.common.dto.game.GameEvaluationDto;
import com.game.common.dto.game.GameReleaseLogDto;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  MQ业务层
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/5/17 14:27
 */
@Service
public class MQServiceImpl implements IMQService {

    private static final Logger logger = LoggerFactory.getLogger(MQServiceImpl.class);

    @Autowired
    private GameDao gameDao;
    @Autowired
    private IEvaluateService iEvaluateService;
    @Autowired
    private GameEvaluationService gameEvaluationService;
    @Autowired
    private GameService gameService;
    @Autowired
    private GameSurveyRelDao gameSurveyRelDao;

    /**
     * 游戏更新
     * @param gameDto1
     */
    @Override
    public boolean mqGameUpdate(GameDto gameDto1) {
        if (gameDto1 == null || StringUtils.isEmpty(gameDto1.getId())){
            return false;
        }
        Game game = new Game();
        BeanUtils.copyProperties(gameDto1, game);
        return game.updateById();
    }


    /**
     * mq游戏添加
     * @param gameDto1
     * @return
     */
    @Override
    public boolean mqGameInsert(GameDto gameDto1) {
        Game game = new Game();
        BeanUtils.copyProperties(gameDto1, game);
        return game.insert();
    }

    /**
     * mq插入游戏版本日志审批
     * @param gameReleaseLogDto
     * @return
     */
    @Override
    public boolean mqGameReleaseLogInsert(GameReleaseLogDto gameReleaseLogDto) {
        GameReleaseLog gameReleaseLog = new GameReleaseLog();
        BeanUtils.copyProperties(gameReleaseLogDto, gameReleaseLog);
        return gameReleaseLog.insert();
    }

    /**
     * mq计数器(动态表)
     * @param map
     * @return
     */
    @Override
    public boolean mqCounterUpdate(Map map) {
        return gameDao.updateCountor(map);
    }

    /**
     * mq根据后台标签id集合，分类标签，游戏id
     * @param map
     * @return
     */
    @Override
    public boolean mqAddGametag(Map map) {
        List<String> tags = (List<String>) map.get("tags");
        String categoryId = (String) map.get("categoryId");
        String gameId = (String) map.get("gameId");
        return iEvaluateService.feignAddGameTagInsert(tags,categoryId,gameId);
    }

    /**
     * mq添加游戏评论
     * @param gameEvaluationDto
     * @return
     */
    @Override
    public boolean mqGameEvaluationInsert(GameEvaluationDto gameEvaluationDto) {
        GameEvaluation gameEvaluation = new GameEvaluation();
        BeanUtils.copyProperties(gameEvaluationDto,gameEvaluation);
        return gameEvaluationService.insert(gameEvaluation);
    }

    /**
     * mq修改游戏评论
     * @param gameEvaluationDto
     * @return
     */
    @Override
    public boolean mqGameEvaluationUpdate(GameEvaluationDto gameEvaluationDto) {
        GameEvaluation gameEvaluation = new GameEvaluation();
        BeanUtils.copyProperties(gameEvaluationDto,gameEvaluation);
        return gameEvaluationService.updateById(gameEvaluation);
    }

    /**
     * 游戏下载量变化
     * @param map
     * @return
     */
    @Override
    public boolean mqUpdateGameDownLoadNum(Map map) {
        String game_id = (String)map.get("gameId");
        if (StringUtils.isNotBlank(game_id)) {
            EntityWrapper<Game> gameEntityWrapper = new EntityWrapper<Game>();
            gameEntityWrapper.setSqlSelect("id,boom_download_num as boomDownloadNum,download_num as downloadNum");
            gameEntityWrapper.eq("id", game_id);
            Game gameDB = gameService.selectOne(gameEntityWrapper);
            if (null != gameDB) {
                Long downNumNew = 0L;
                if (null == gameDB.getBoomDownloadNum() || 0 == gameDB.getBoomDownloadNum()) {
                    downNumNew = gameDB.getDownloadNum().longValue();
                } else {
                    downNumNew = gameDB.getBoomDownloadNum();
                }
                downNumNew = FungoLivelyCalculateUtils.calcViewAndDownloadCount(downNumNew);
                gameDB.setBoomDownloadNum(downNumNew);
                gameDB.setDownloadNum(gameDB.getDownloadNum() + 1);
                return gameDB.updateById();
            }
        }
        return false;
    }

    @Override
    public boolean mqBatchUpdateGameSurveyRel(List<String> ids) {
        try {
            if(ids != null && ids.size() != 0){
                int total = gameSurveyRelDao.updateMemberNoticeBatch( ids );
            }else {
                logger.error( "没有检查到参数ids:"+JSON.toJSON( ids) );
            }
            return true;
        }catch (Exception e){
            logger.error( "批量更新游戏测试关联表异常.ids:{}", JSON.toJSONString(ids,true),e );
            return false;
        }
    }
}
