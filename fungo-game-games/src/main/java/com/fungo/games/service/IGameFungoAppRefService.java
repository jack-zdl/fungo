package com.fungo.games.service;


import com.fungo.games.entity.GameFungoappRef;

/**
 * <p>
 *          获取游戏与Fungo app Apk捆绑包下载地址接口业务层
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public interface IGameFungoAppRefService {


    /**
     * 获取游戏与Fungo app Apk捆绑包下载地址
     * @param game_id 游戏ID
     * @param fungo_channel_id fungo apk渠道id
     * @return
     */
    public GameFungoappRef queryGameFungoappRef(String game_id, String fungo_channel_id);


}
