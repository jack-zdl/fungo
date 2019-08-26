package com.fungo.games.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.games.dao.GameSurveyRelDao;
import com.fungo.games.entity.GameSurveyRel;
import com.fungo.games.service.GameSurveyRelService;
import com.game.common.dto.game.GameSurveyRelDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 游戏测试会员关联表 服务实现类
 * </p>
 *
 * @author lzh
 * @since 2018-06-08
 */
@Service
public class GameSurveyRelServiceImap extends ServiceImpl<GameSurveyRelDao, GameSurveyRel> implements GameSurveyRelService {

    private static final Logger logger = LoggerFactory.getLogger(GameSurveyRelServiceImap.class);

    @Autowired
    private GameSurveyRelDao gameSurveyRelDao;

    @Override
    public List<GameSurveyRelDto> getMemberNoticeByGame(){
        List<GameSurveyRelDto> gameSurveyRelList = new ArrayList<>(  );
        try {
           gameSurveyRelList = gameSurveyRelDao.getMemberNoticeByGame();
        }catch (Exception e){
            logger.error("查询当前游戏模块游戏",e);
        }
        return gameSurveyRelList;

    }
}
