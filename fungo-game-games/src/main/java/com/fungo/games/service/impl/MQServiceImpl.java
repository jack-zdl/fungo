package com.fungo.games.service.impl;

import com.fungo.games.dao.GameDao;
import com.fungo.games.entity.Game;
import com.fungo.games.entity.GameEvaluation;
import com.fungo.games.entity.GameReleaseLog;
import com.fungo.games.service.GameEvaluationService;
import com.fungo.games.service.IEvaluateService;
import com.fungo.games.service.IGameService;
import com.fungo.games.service.IMQService;
import com.game.common.dto.GameDto;
import com.game.common.dto.game.GameEvaluationDto;
import com.game.common.dto.game.GameReleaseLogDto;
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
@Transactional
public class MQServiceImpl implements IMQService {

    @Autowired
    private GameDao gameDao;
    @Autowired
    private IEvaluateService iEvaluateService;
    @Autowired
    private GameEvaluationService gameEvaluationService;

    /**
     * 游戏更新
     * @param gameDto1
     */
    @Override
    public boolean mqGameUpdate(GameDto gameDto1) {
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
}
