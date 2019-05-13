package com.fungo.games.service;




import com.game.common.util.exception.BusinessException;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *      游戏工具业务层
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public interface IGameToolsService {


    /**
     * 获取游戏工具资源数据
     *
     * @param platformType
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getGameTools(int platformType) throws BusinessException;


}
