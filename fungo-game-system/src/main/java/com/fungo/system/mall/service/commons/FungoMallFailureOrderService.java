package com.fungo.system.mall.service.commons;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.entity.IncentAccountCoin;
import com.fungo.system.mall.daoService.MallOrderDaoService;
import com.fungo.system.mall.entity.MallOrder;
import com.fungo.system.mall.service.consts.FungoMallSeckillConsts;
import com.fungo.system.service.IncentAccountCoinDaoService;
import com.game.common.util.date.DateTools;
import com.game.common.util.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *    fungo商城-失败订单处理
 *    产生订单后，超过4个小时未支付的订单视为交易失败订单
 *    处理：
 *         1.把订单状态置为:  3 无效
 *         2.把冻结的用户货款，解冻
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
@Component
public class FungoMallFailureOrderService {

    private static final Logger logger = LoggerFactory.getLogger(FungoMallFailureOrderService.class);


    @Autowired
    private MallOrderDaoService mallOrderDaoService;

    @Autowired
    private IncentAccountCoinDaoService incentAccountCoinService;


    @Transactional(rollbackFor = Exception.class)
    public void scanOrderWithSeckillFailure() {

        logger.info("扫描订单表,处理产生订单后，超过4个小时未支付的订单视为交易失败订单....");

        try {

            //开始扫描订单，设置处理订单状态为 2 正在扫描订单处理中
            FungoMallSeckillTaskStateCommand.getInstance().setScanOrderIsOkWithFailureOrder(2);


            //每次处理条数
            int pageSize = 40;
            //分页查询订单
            //1 先查询订单总条数
            EntityWrapper<MallOrder> orderEntityWrapper = new EntityWrapper<MallOrder>();
            //1 已确认  3 已冻结物品价格可用余额
            orderEntityWrapper.eq("order_status", 1).eq("pay_status", 3);
            //订单总数
            int orderCount = mallOrderDaoService.selectCount(orderEntityWrapper);
            if (orderCount > 0) {
                long totalPage = (orderCount - 1) / pageSize + 1;

                //订单按创建时间正序顺序排列
                orderEntityWrapper.orderBy("create_time", true);
                //按页处理订单 当前页下标从1开始
                for (int i = 1; i <= totalPage; i++) {

                    Page<MallOrder> mallOrderPage = mallOrderDaoService.selectPage(new Page<>(i, pageSize), orderEntityWrapper);
                    List<MallOrder> mallOrderList = mallOrderPage.getRecords();

                    if (null != mallOrderList && !mallOrderList.isEmpty()) {
                        for (MallOrder mallOrder : mallOrderList) {

                            //获取订单创建时间戳
                            Date orderCreateTime = mallOrder.getCreateTime();

                            //验证该订单是否超过4小时
                            boolean isExpire = isExpireWithOrder(orderCreateTime);
                            if (isExpire) {

                                //解冻用户账户被冻结的订单货款
                                unfreezeAccountCoinWithMember(mallOrder.getMbId(), mallOrder.getGoodsAmountVcy());

                                //若订单的支付状态是 3 已冻结余额，则把支付状态置为 4 被冻结额已解冻
                                //否则置为-1 未付款
                                int pay_status = -1;
                                if (3 == mallOrder.getPayStatus()) {
                                    pay_status = 4;
                                }
                                //修改订单状态为: 3 无效   4 被冻结额已解冻   -1 未发货
                                updateOrderStatusWithScan(mallOrder.getMbId(), mallOrder.getId(), 3, pay_status, -1);
                            }
                        }
                    }

                }
            }
        } catch (Exception ex) {
            logger.error("扫描订单表,处理产生订单后，超过4个小时未支付的订单视为交易失败订单出现异常", ex);
            ex.printStackTrace();
            throw new BusinessException("-1", "扫描订单表,处理产生订单后，超过4个小时未支付的订单视为交易失败订单出现异常");
        } finally {
            //处理完设置扫描订单为 3 完成扫描订单
            FungoMallSeckillTaskStateCommand.getInstance().setScanOrderIsOkWithFailureOrder(3);
        }
    }


    /**
     * 验证两个时间的差值
     *  当前时间 - 订单创建时间
     * @param orderCreateTime
     * @return true 订单超过4小时  false 订单未超过4小时
     */
    private boolean isExpireWithOrder(Date orderCreateTime) {
        try {

            if (null == orderCreateTime) {
                return false;
            }

            String currentTime = DateTools.fmtDate(new Date());
            String orderTime = DateTools.fmtDate(orderCreateTime);

            logger.info("验证两个时间的差值-currentTime:{}--orderCreateTime:{}", currentTime, orderTime);

            SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            long from = simpleFormat.parse(currentTime).getTime();
            long to = simpleFormat.parse(orderTime).getTime();
            int hours = (int) ((from - to) / (1000 * 60 * 60));

            if (hours >= FungoMallSeckillConsts.ORDER_EXPIRE_HOUR) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }


    /**
     *  解冻用户fungo币账户被冻结商品价格
     * @param mb_id
     * @param goodsPriceVcy
     */
    private void unfreezeAccountCoinWithMember(String mb_id, Long goodsPriceVcy) {

        EntityWrapper<IncentAccountCoin> mbAccountCoinEntityWrapper = new EntityWrapper<IncentAccountCoin>();
        Map<String, Object> criteriaMap = new HashMap<String, Object>();
        criteriaMap.put("mb_id", mb_id);
        mbAccountCoinEntityWrapper.allEq(criteriaMap);

        IncentAccountCoin incentAccountCoinDB = incentAccountCoinService.selectOne(mbAccountCoinEntityWrapper);

        logger.info("解冻用户fungo币账户被冻结商品价格-解冻前账户详情：{}", JSON.toJSONString(incentAccountCoinDB));

        //账户冻结币量
        BigDecimal coinFreeze = incentAccountCoinDB.getCoinFreeze();
        //商品价格
        BigDecimal goodsPriceVcyDB = new BigDecimal(goodsPriceVcy);

        int freezCompResult = coinFreeze.compareTo(goodsPriceVcyDB);
        //账户冻结余额 大于等于 商品价格
        if (0 == freezCompResult || 1 == freezCompResult) {

            //账户冻结额 - 被冻结的商品价格 = 新的账户冻结余额
            BigDecimal coinFreezeNew = BigDecimal.ZERO;
            coinFreezeNew = coinFreezeNew.add(coinFreeze.subtract(goodsPriceVcyDB));

            //账户余额 + 被冻结的商品价格 = 新的账户可用余额
            BigDecimal coinUsableNew = BigDecimal.ZERO;
            coinUsableNew = coinUsableNew.add(incentAccountCoinDB.getCoinUsable());
            coinUsableNew = coinUsableNew.add(goodsPriceVcyDB);

            IncentAccountCoin incentAccountCoinNew = new IncentAccountCoin();
            incentAccountCoinNew.setCoinFreeze(coinFreezeNew);
            incentAccountCoinNew.setCoinUsable(coinUsableNew);
            incentAccountCoinNew.setUpdatedAt(new Date());

            logger.info("解冻用户fungo币账户被冻结商品价格-执行解冻计算后账户详情：{}", JSON.toJSONString(incentAccountCoinNew));
            //执行账户更新
            boolean updateOk = incentAccountCoinService.update(incentAccountCoinNew, mbAccountCoinEntityWrapper);
            logger.info("更新解冻用户-用户Id:{}--fungo币账户被冻结商品价格-商品价格:{}--执行结果:{}", mb_id, goodsPriceVcy, updateOk);
        }
    }


    /**
     * 更新订单状态
     * @param mb_id 用户id
     * @param orderId 订单id
     * @param orderStatus 订单状态
     * @param payStatus 支付状态
     * @param payStatus 发货状态
     */
    public boolean updateOrderStatusWithScan(String mb_id, Long orderId, Integer orderStatus, Integer payStatus, Integer shipping_status) {

        MallOrder order = new MallOrder();
        order.setOrderStatus(orderStatus);
        order.setPayStatus(payStatus);
        order.setShippingStatus(shipping_status);

        order.setUpdatedTime(new Date());

        EntityWrapper<MallOrder> orderEntityWrapper = new EntityWrapper<MallOrder>();
        orderEntityWrapper.eq("mb_id", mb_id).eq("id", orderId);

        boolean updateOk = mallOrderDaoService.update(order, orderEntityWrapper);
        logger.info(" 更新订单状态--订单id:{}--用户Id:{}--执行结果:{}", orderId, mb_id, updateOk);

        return updateOk;
    }

    //---------
}
