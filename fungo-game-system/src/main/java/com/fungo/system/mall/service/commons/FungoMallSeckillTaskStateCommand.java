package com.fungo.system.mall.service.commons;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 *    fungo商城-秒杀业务任务执行状态控制
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public class FungoMallSeckillTaskStateCommand {


    private static FungoMallSeckillTaskStateCommand fungoMallSeckillTaskStateCommand = null;


    /**
     * 是否继续扫描订单表
     * 1 继续 （默认）
     * 2 停止
     */
    private int isScanOrder = 1;

    /**
     *  本次扫描订单表是否结束
     * 1  未开始扫描 (默认)
     * 2  正在扫描订单处理中
     * 3  完成扫描订单
     */
    private int scanOrderIsOk = 1;


    /**
     *   产生订单后，超过4个小时未支付的订单视为交易失败订单
     * 1  未开始扫描 (默认)
     * 2  正在扫描订单处理中
     * 3  完成扫描订单
     */
    private int scanOrderIsOkWithFailureOrder = 1;


    /**
     * 秒杀下单授权码
     */
    private final Map<String, String> seckillCodeMap = new HashMap<String, String>();


    private FungoMallSeckillTaskStateCommand() {

    }


    /**
     * 初始化单例对象和阿里云vod客户端对象
     *
     */
    private static synchronized void syncInit() {

        if (null == fungoMallSeckillTaskStateCommand) {

            fungoMallSeckillTaskStateCommand = new FungoMallSeckillTaskStateCommand();

        }
    }


    /**
     * 获取单例对象
     *
     * @return
     */
    public static FungoMallSeckillTaskStateCommand getInstance() {

        if (null == fungoMallSeckillTaskStateCommand) {
            syncInit();
        }
        return fungoMallSeckillTaskStateCommand;
    }


    public int getIsScanOrder() {
        return isScanOrder;
    }

    /**
     * 设置是否继续扫描订单表
     * @param isScanOrder
     */
    public void setIsScanOrder(int isScanOrder) {
        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            this.isScanOrder = isScanOrder;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public int getScanOrderIsOk() {
        return scanOrderIsOk;
    }

    /**
     * 设置扫描表是否完成的状态
     * @param scanOrderIsOk
     */
    public void setScanOrderIsOk(int scanOrderIsOk) {
        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            this.scanOrderIsOk = scanOrderIsOk;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public Map<String, String> getSeckillCodeMap() {
        return seckillCodeMap;
    }

    public int getScanOrderIsOkWithFailureOrder() {
        return scanOrderIsOkWithFailureOrder;
    }

    public void setScanOrderIsOkWithFailureOrder(int scanOrderIsOkWithFailureOrder) {
        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            this.scanOrderIsOkWithFailureOrder = scanOrderIsOkWithFailureOrder;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


//------
}
