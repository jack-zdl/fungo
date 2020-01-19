package com.fungo.system.function;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.dao.IncentRuleRankGroupDao;
import com.fungo.system.entity.IncentRanked;
import com.fungo.system.entity.IncentRuleRank;
import com.fungo.system.entity.IncentRuleRankGroup;
import com.fungo.system.feign.GamesFeignClient;
import com.fungo.system.service.IScoreRuleService;
import com.fungo.system.service.IncentRankedService;
import com.fungo.system.service.IncentRuleRankService;
import com.game.common.consts.FunGoGameConsts;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.GameDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.terracotta.modules.ehcache.ToolkitInstanceFactoryImpl.LOGGER;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/12/30
 */
@Service
public class GameFilterService {

    @Autowired
    private GamesFeignClient gamesFeignClient;

    public Long getGameDto(String gameId){
        Long gameNumber = null;
        try {
            GameDto gameDto = gamesFeignClient.selectOne( gameId);
            if(gameDto != null && gameDto.getId() != null){
                gameNumber = gameDto.getGameIdtSn();
            }
        }catch (Exception e){

        }
        return gameNumber;

    }
}
