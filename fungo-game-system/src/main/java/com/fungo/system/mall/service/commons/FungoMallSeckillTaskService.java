package com.fungo.system.mall.service.commons;


import com.game.common.util.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * <p>
 *    fungo商城-秒杀业务定时任务
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
//@Component
public class FungoMallSeckillTaskService {


    private static final Logger LOGGER = LoggerFactory.getLogger(FungoMallSeckillTaskService.class);

    @Autowired
    private FungoMallScanOrderWithSeckillService fungoMallScanOrderWithSeckillService;

    @Autowired
    private FungoMallFailureOrderService fungoMallFailureOrderService;


    /**
     * 处理秒杀开始后，已经下的单子
     * 每2秒执行扫描一次订单表
     */
    @Scheduled(cron = "0/2 * * * * ? ")
    public void excuteSeckillOrderScan() throws Exception {
        try {

            LOGGER.info("扫描订单表定时器启动...");
            //验证是否继续扫描订单表
            FungoMallSeckillTaskStateCommand seckillTaskStateCommand = FungoMallSeckillTaskStateCommand.getInstance();
            if (1 == seckillTaskStateCommand.getIsScanOrder()) {
                //处理订单
                //若开始处理，则等当前处理周期完成后才继续下一次扫描
                if (1 == FungoMallSeckillTaskStateCommand.getInstance().getScanOrderIsOk()
                        || 3 == FungoMallSeckillTaskStateCommand.getInstance().getScanOrderIsOk()
                ) {
                    fungoMallScanOrderWithSeckillService.scanOrderWithSeckill();
                }
                LOGGER.info("扫描订单表定时器执行完成。");
            }
        } catch (Exception ex) {
            LOGGER.error("扫描订单表，处理订单,出现异常", ex);
            ex.printStackTrace();
            throw new BusinessException("-1", "扫描订单表，处理订单,出现异常");
        }
    }


    /**
     * 定时 每4小时启动
     * 处理 秒杀失败的订单(创建订单超过4小时)，把用户已冻结的fungo币账户解冻，同时设置订单为 3 无效订单
     *
     */
    @Scheduled(cron = "0 0 0/4 * * ? ")
    public void excuteSeckillOrderScanWithFailure() {
        try {
            LOGGER.info("处理秒杀失败的订单定时器启动...");
            FungoMallSeckillTaskStateCommand seckillTaskStateCommand = FungoMallSeckillTaskStateCommand.getInstance();

            if (1 == seckillTaskStateCommand.getIsScanOrder()) {

                //处理订单
                //若开始处理，则等当前处理周期完成后才继续下一次扫描
                if (1 == FungoMallSeckillTaskStateCommand.getInstance().getScanOrderIsOkWithFailureOrder()
                        || 3 == FungoMallSeckillTaskStateCommand.getInstance().getScanOrderIsOkWithFailureOrder()
                ) {
                    fungoMallFailureOrderService.scanOrderWithSeckillFailure();
                }

            }
            LOGGER.info("处理秒杀失败的订单定时器执行完成。");
        } catch (Exception ex) {
            LOGGER.error("处理秒杀失败的订单,出现异常", ex);
            ex.printStackTrace();
        }
    }


    //---------------
}
