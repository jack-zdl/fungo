package com.game.common.dto.market;

import java.io.Serializable;


/**
 * <p>
 *      获取游戏与Fungo app Apk捆绑包下载地址入参数据封装
 * </p>
 *
 * @author mxf
 * @since 2018-12-03
 */
public class GameFungoAppRefInput implements Serializable {

    /**
     * 游戏ID
     */
    private String game_id;


    /**
     * fungo apk渠道id
     */
    private String fungo_channel_id;

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getFungo_channel_id() {
        return fungo_channel_id;
    }

    public void setFungo_channel_id(String fungo_channel_id) {
        this.fungo_channel_id = fungo_channel_id;
    }

    @Override
    public String toString() {
        return "GameFungoAppRefInput{" +
                "game_id='" + game_id + '\'' +
                ", fungo_channel_id='" + fungo_channel_id + '\'' +
                '}';
    }
}
