package com.fungo.system.controller;

import com.fungo.system.job.DTPTransactionMessageScheduledJob;
import com.fungo.system.job.FungoMallSeckillTaskService;
import com.fungo.system.job.PushFunction;
import com.fungo.system.service.ISeacherService;
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
@RequestMapping("/api/system/job")
public class JobController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private DTPTransactionMessageScheduledJob dtpTransactionMessageScheduledJob;

    @Autowired
    private FungoMallSeckillTaskService fungoMallSeckillTaskService;

    @Autowired
    private PushFunction pushFunction;

    @Autowired
    private ISeacherService iSeacherService;

    @GetMapping("/dtpTransactionMessageScheduledJob")
    public ResultDto<String> dtpTransactionMessageScheduledJob(){
        ResultDto<String> re = null;
        try {
            dtpTransactionMessageScheduledJob.execute();
            re = ResultDto.success("分布式事务之消息状态监控定时任务执行成功");
        }catch (Exception e){
            LOGGER.error("分布式事务之消息状态监控定时任务执行异常",e);
            re = ResultDto.error("-1","分布式事务之消息状态监控定时任务异常");
        }
        return re;
    }

    @GetMapping("/excuteSeckillOrderScan")
    public ResultDto<String> excuteSeckillOrderScan(){
        ResultDto<String> re = null;
        try {
            fungoMallSeckillTaskService.excuteSeckillOrderScan();
            re = ResultDto.success("扫描订单表定时器执行成功");
        }catch (Exception e){
            LOGGER.error("扫描订单表定时器启动执行异常",e);
            re = ResultDto.error("-1","扫描订单表定时器执行异常");
        }
        return re;
    }

    @GetMapping("/excuteSeckillOrderScanWithFailure")
    public ResultDto<String> excuteSeckillOrderScanWithFailure(){
        ResultDto<String> re = null;
        try {
            fungoMallSeckillTaskService.excuteSeckillOrderScanWithFailure();
            re = ResultDto.success("处理秒杀失败的订单定时器执行成功");
        }catch (Exception e){
            LOGGER.error("处理秒杀失败的订单定时器执行异常",e);
            re = ResultDto.error("-1","处理秒杀失败的订单定时器执行异常");
        }
        return re;
    }
    @GetMapping("/pushControllerNotice")
    public ResultDto<String> pushControllerNotice(){
        ResultDto<String> re = null;
        try {
            pushFunction.pushControllerNotice();
            re = ResultDto.success("管理台消息推送定期任务执行成功");
        }catch (Exception e){
            LOGGER.error("管理台消息推送定期任务执行异常",e);
            re = ResultDto.error("-1","管理台消息推送定期任务执行异常");
        }
        return re;
    }


    @GetMapping("/updateGameKeywords")
    public ResultDto<String> updateGameKeywords(){
        ResultDto<String> re = null;
        try {
            iSeacherService.updateGameKeywords();
            re = ResultDto.success("更新热度游戏关键字定期任务执行成功");
        }catch (Exception e){
            LOGGER.error("更新热度游戏关键字定期任务执行异常",e);
            re = ResultDto.error("-1","更新热度游戏关键字定期任务执行异常");
        }
        return re;
    }
}
