package com.game.common.buriedpoint.model;

import com.game.common.buriedpoint.util.map.annotation.MapKeyMapping;

/**
 *  埋点 - 消耗fun币埋点
 */
public class BuriedPointGameScoreModel extends BuriedPointModel{

    @MapKeyMapping("game_id")
    private String gameId;
    @MapKeyMapping("game_name")
    private String gameName;


    /**
     *  游戏综合评分
     */
    private Double rank;
    /**
     *  画面评分
     */
    @MapKeyMapping("game_image")
    private  Double gameImage;
    /**
     * 音乐评分
     */
    @MapKeyMapping("game_bgm")
    private  Double gameBgm;
    /**
     * 氪金评分
     */
    @MapKeyMapping("game_cost")
    private  Double gameCost;
    /**
     * 剧情评分
     */
    @MapKeyMapping("game_plot")
    private  Double gamePlot;
    /**
     * 玩法评分
     */
    @MapKeyMapping("game_rules")
    private  Double gameRules;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }


    public Double getRank() {
        return rank;
    }

    public void setRank(Double rank) {
        this.rank = rank;
    }

    public Double getGameImage() {
        return gameImage;
    }

    public void setGameImage(Double gameImage) {
        this.gameImage = gameImage;
    }

    public Double getGameBgm() {
        return gameBgm;
    }

    public void setGameBgm(Double gameBgm) {
        this.gameBgm = gameBgm;
    }

    public Double getGameCost() {
        return gameCost;
    }

    public void setGameCost(Double gameCost) {
        this.gameCost = gameCost;
    }

    public Double getGamePlot() {
        return gamePlot;
    }

    public void setGamePlot(Double gamePlot) {
        this.gamePlot = gamePlot;
    }

    public Double getGameRules() {
        return gameRules;
    }

    public void setGameRules(Double gameRules) {
        this.gameRules = gameRules;
    }
}
