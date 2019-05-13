package com.fungo.games.service;


import com.fungo.games.entity.GameMarketSpy;
import com.game.common.dto.market.GameMarketSpyInput;
import com.game.common.util.exception.BusinessException;

/**
 * <p>
 *          运营市场推广下载游戏数据识别业务层接口定义
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public interface IGameMarketSpyService {

    /**
     * 记录wap页面下载游戏，app端上报游戏安装数据接口
     * @param gameMarketSpyInput
     * @return
     */
    public boolean addMarketSpyAction(GameMarketSpyInput gameMarketSpyInput) throws BusinessException;



    /**
     * 跳转游戏详情接口
     * 获取游戏id
     * @param gameMarketSpyInput
     * @return
     */
    public GameMarketSpy queryMarketSpyAction(GameMarketSpyInput gameMarketSpyInput) throws BusinessException;
}
