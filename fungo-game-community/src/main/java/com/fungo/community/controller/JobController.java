package com.fungo.community.controller;

import com.fungo.community.service.IPostService;
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
@RequestMapping("/api/community/job")
public class JobController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private IPostService postService;

    /**
     * 功能描述: 定时检查社区模块系统管控台系统消息
     * @return: com.game.common.dto.ResultDto<java.lang.String>
     * @auther: dl.zhang
     * @date: 2019/7/29 16:42
     */
    @GetMapping("/admin/systemNotice")
    public ResultDto<String> checkSystemNotice(  ){
        ResultDto<String> re = null;
        try {
            re = ResultDto.success("  定时检查社区模块系统管控台系统消息定期任务执行成功   ");
        }catch (Exception e){
            LOGGER.error("定时检查社区模块系统管控台系统消息定期任务执行异常",e);
            re = ResultDto.error("-1","定时检查社区模块系统管控台系统消息定期任务执行异常");
        }
        return re;
    }

    /**
     * 功能描述:  定时任务检查vedio和文章关联
     * @auther: dl.zhang
     * @date: 2019/10/18 16:32
     */
    @GetMapping("/video/check")
    public ResultDto<String> checkAndUpdateVideoPost(){
        ResultDto<String> re = null;
        try {
            postService.checkAndUpdateVideoPost();
            re = ResultDto.success("  定时任务检查vedio和文章关联执行成功   ");
        }catch (Exception e){
            LOGGER.error("定时任务检查vedio和文章关联执行异常",e);
            re = ResultDto.error("-1","定时任务检查vedio和文章关联执行异常");
        }
        return re;
    }
}
