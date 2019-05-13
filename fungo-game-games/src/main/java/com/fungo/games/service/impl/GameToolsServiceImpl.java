package com.fungo.games.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import com.fungo.games.entity.GameTools;
import com.fungo.games.service.GameToolsService;
import com.fungo.games.service.IGameToolsService;
import com.game.common.consts.FunGoGameConsts;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.repo.cache.facade.FungoCacheSystem;
import com.game.common.util.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameToolsServiceImpl implements IGameToolsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameToolsServiceImpl.class);


    @Autowired
    private GameToolsService gameToolsService;

    @Autowired
    private FungoCacheSystem fungoCacheSystem;


    @Cacheable(cacheNames = {FunGoGameConsts.CACHE_EH_NAME}, key = "'" + FunGoGameConsts.CACHE_EH_KEY_PRE_SYSTEM + "_getGameTools' + #platformType")
    @Override
    public List<Map<String, Object>> getGameTools(int platformType) throws BusinessException {

        List<Map<String, Object>> toolsList = null;
        try {

            LOGGER.info("查询游戏工具业务-platformType：{}", platformType);

            //默认查询android平台
            if (platformType <= 0) {
                platformType = FunGoGameConsts.GAME_TOOL_TOOLS_PLATFORM_ANDROID;
            }

            toolsList = (List<Map<String, Object>>) fungoCacheSystem.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_SYSTEM_TOOLS_GAME + platformType, "");
            if (null != toolsList && !toolsList.isEmpty()) {
                return toolsList;
            }

            Wrapper wrapperCriteria = new EntityWrapper<GameTools>();

            Map<String, Object> criteriaMap = new HashMap<String, Object>();
            criteriaMap.put("tools_platform", platformType);
            wrapperCriteria.allEq(criteriaMap);

            List<GameTools> gameToolslist = gameToolsService.selectList(wrapperCriteria);

            if (null != gameToolslist && !gameToolslist.isEmpty()) {

                toolsList = new ArrayList<Map<String, Object>>();

                Set<Integer> toolsTypeSet = new HashSet<Integer>();
                for (GameTools gameTools : gameToolslist) {
                    toolsTypeSet.add(gameTools.getToolsType());
                }

                for (Integer toolsTypeFor : toolsTypeSet) {

                    Map<String, Object> toolsTypeMap = new HashMap<String, Object>();
                    toolsList.add(toolsTypeMap);

                    List<GameTools> toolsoFCateList = new ArrayList<GameTools>();
                    toolsTypeMap.put("tools", toolsoFCateList);

                    for (GameTools gameTools : gameToolslist) {
                        Integer toolsTypeData = gameTools.getToolsType();
                        if (toolsTypeFor.intValue() == toolsTypeData.intValue()) {

                            if (!toolsTypeMap.containsKey("toolsTypeId") && !toolsTypeMap.containsValue(gameTools.getToolsType())) {
                                toolsTypeMap.put("toolsTypeId", gameTools.getToolsType());
                            }

                            if (!toolsTypeMap.containsKey("typeTitle") && !toolsTypeMap.containsValue(gameTools.getTypeTitle())) {
                                toolsTypeMap.put("typeTitle", gameTools.getTypeTitle());
                            }
                            toolsoFCateList.add(gameTools);
                        }
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //redis cache
        fungoCacheSystem.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_SYSTEM_TOOLS_GAME + platformType,
                "", toolsList);

        return toolsList;
    }


    //---------
}
