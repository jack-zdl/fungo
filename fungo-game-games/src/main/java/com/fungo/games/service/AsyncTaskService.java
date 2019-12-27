package com.fungo.games.service;

public interface AsyncTaskService {

    /**
     *  记录用户浏览游戏记录
     */
    void recordGameView(String userId,String gameId);


}
