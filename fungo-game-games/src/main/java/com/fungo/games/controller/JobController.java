package com.fungo.games.controller;

import com.fungo.games.service.GamesJobService;
import com.game.common.dto.ResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/7/12
 */
@RestController
@RequestMapping("/api/games/job")
public class JobController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private GamesJobService gamesJobService;
    /**
     * 功能描述: 定时检查游戏模块系统管控台系统消息
     * @return: com.game.common.dto.ResultDto<java.lang.String>
     * @auther: dl.zhang
     * @date: 2019/7/29 16:42
     */
    @GetMapping("/admin/systemNotice")
    public ResultDto<String> checkSystemNotice(  ){
        ResultDto<String> re = null;
        try {
//            gamesJobService.checkGamesNotice();
            re = ResultDto.success("定时检查游戏模块系统管控台系统消息定期任务执行成功");
        }catch (Exception e){
            LOGGER.error("定时检查游戏模块系统管控台系统消息定期任务执行异常",e);
            re = ResultDto.error("-1","定时检查游戏模块系统管控台系统消息定期任务执行异常");
        }
        return re;
    }



}
