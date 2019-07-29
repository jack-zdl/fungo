package com.fungo.games.service.impl;

import com.fungo.games.dao.GameSurveyRelDao;
import com.fungo.games.entity.GameSurveyRel;
import com.fungo.games.service.GamesJobService;
import com.fungo.games.utils.dto.GameSurveyRelDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/7/29
 */
public class GamesJobServiceImpl implements GamesJobService {

    private static final Logger logger = LoggerFactory.getLogger(GamesJobServiceImpl.class);

    @Autowired
    private GameSurveyRelDao gameSurveyRelDao;
    
    /**
     * 功能描述:  检查游戏模块下game已经进入测试阶段
     * @auther: dl.zhang
     * @date: 2019/7/29 18:20
     */
    @Override
    public void checkGamesNotice() {
        try {
            List<GameSurveyRelDTO> gameSurveyRelList = gameSurveyRelDao.getMemberNoticeByGame();
            gameSurveyRelList.stream().forEach( s ->{

            } );


        }catch (Exception e){
            logger.error("",e);
        }

    }
}
