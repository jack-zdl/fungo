package com.fungo.system.mall.service.commons;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.entity.IncentAccountCoin;
import com.fungo.system.mall.daoService.MallOrderDaoService;
import com.fungo.system.mall.daoService.MallOrderGoodsDaoService;
import com.fungo.system.mall.daoService.MallSeckillDaoService;
import com.fungo.system.mall.daoService.MallVirtualCardDaoService;
import com.fungo.system.mall.entity.MallOrder;
import com.fungo.system.mall.entity.MallOrderGoods;
import com.fungo.system.mall.entity.MallSeckill;
import com.fungo.system.mall.entity.MallVirtualCard;
import com.fungo.system.mall.service.consts.FungoMallSeckillConsts;
import com.fungo.system.service.IncentAccountCoinDaoService;
import com.game.common.consts.FunGoGameConsts;
import com.game.common.util.FunGoEHCacheUtils;
import com.game.common.util.FungoAESUtil;
import com.game.common.util.FungoCRC32Util;
import com.game.common.util.date.DateTools;
import com.game.common.util.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *    fungo商城-秒杀订单扫描
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */

@Service
public class FungoMallScanOrderWithSeckillService {

    private static final Logger logger = LoggerFactory.getLogger(FungoMallScanOrderWithSeckillService.class);


    @Autowired
    private MallSeckillDaoService mallSeckillDaoService;

    @Autowired
    private IncentAccountCoinDaoService incentAccountCoinService;

    @Autowired
    private MallOrderDaoService mallOrderDaoService;

    @Autowired
    private MallOrderGoodsDaoService mallOrderGoodsDaoService;

    @Autowired
    private MallVirtualCardDaoService mallVirtualCardDaoService;

    @Autowired
    private FungoMallSeckillSuccessAdviceService fungoMallSeckillSuccessAdviceService;

    @Autowired
    private FungoMallSeckillLogService fungoMallSeckillLogService;

    @Value("${fungo.mall.seckill.aesSecretKey}")
    private String aESSecretKey;


    /**
     * 扫描订单表
     * 把 订单状态 order_status 为1 已确认
     * 且 支付状态 pay_status 为 3 已冻结余额的订单 查询出来
     * 执行商品交易，扣减库存，分配虚拟商品卡号、密码或者兑换码
     */
    @Transactional(rollbackFor = Exception.class)
    public void scanOrderWithSeckill() {

        try {

            logger.info("扫描订单表,处理已确定和已冻结余额的订单....");

            //开始扫描订单，设置处理订单状态为 2 正在扫描订单处理中
            FungoMallSeckillTaskStateCommand.getInstance().setScanOrderIsOk(2);
            //每次处理条数
            int pageSize = 40;

            //分页查询订单
            //1 先查询订单总条数
            EntityWrapper<MallOrder> orderEntityWrapper = new EntityWrapper<MallOrder>();
            //1 已确认     3 已冻结物品价格可用余额
            orderEntityWrapper.eq("order_status", 1).eq("pay_status", 3);

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

                            //处理交易
                            //1.把该订单关联的商品查询出来
                            String mb_id = mallOrder.getMbId();
                            Long orderId = mallOrder.getId();

                            boolean isPushMsg = false;

                            List<MallOrderGoods> orderGoodsList = queryOrderGoodsWithMember(mb_id, String.valueOf(orderId));
                            if (null != orderGoodsList && !orderGoodsList.isEmpty()) {
                                for (MallOrderGoods goods : orderGoodsList) {

                                    //2.获取购买的商品数量
                                    Long goodsNumber = goods.getGoodsNumber();
                                    //商品价格fungo币
                                    Long goodsPriceVcy = goods.getGoodsPriceVcy();

                                    //3. 验证该种商品库存是否充足
                                    boolean isFullGoodsStock = false;
                                    MallSeckill mallSeckill = isFullGoodsStock(String.valueOf(goods.getGoodsId()));
                                    isFullGoodsStock = isFullGoodsStockForScanOrder(mallSeckill, goodsNumber, goods.getGoodsId());

                                    //3.1 若库存不足，把冻结余额 加回到 可用余额中，同时标记订单为 8 商品库存不足，继续处理下一个商品
                                    if (!isFullGoodsStock) {
                                        logger.info("mb_id:{}--order_id:{}--goods_id:{}--商品库存不足，解冻用户fungo币账户,修改订单状态: 8 商品库存不足,4 被冻结额已解冻, -1 未发货",
                                                mb_id, orderId, goods.getGoodsId());
                                        //解冻用户fungo币账户
                                        this.unfreezeAccountCoinWithMember(mb_id, goodsPriceVcy);
                                        //修改订单状态 8 商品库存不足  4 被冻结额已解冻   -1 未发货
                                        updateOrderStatusWithScan(mb_id, orderId, 8, 4, -1);
                                        continue;
                                    }

                                    //若库存足，先扣钱，再减库存
                                    //4.扣减冻结商品价格额
                                    boolean dedCoinAccoutResult = this.deductionAccountCoinWithMember(mb_id, goodsPriceVcy);
                                    if (!dedCoinAccoutResult) {
                                        //若扣减冻结账户失败 同时标记订单为 3  无效   5 扣减冻结失败 ，继续处理下一个商品
                                        logger.info("mb_id:{}--order_id:{}--goods_id:{}--扣减冻结用户账户的商品价格额失败,修改订单状态: 3 无效, 5 扣减冻结失败 ,-1 未发货",
                                                mb_id, orderId, goods.getGoodsId());
                                        //修改订单状态  3 无效   5 扣减冻结失败   -1 未发货
                                        updateOrderStatusWithScan(mb_id, orderId, 3, 5, -1);
                                        continue;
                                    }

                                    //5.扣减库存
                                    boolean goodsStockUpdateResult = deductionGoodsStock(mallSeckill, goodsNumber);
                                    if (!goodsStockUpdateResult) {
                                        logger.info("mb_id:{}--order_id:{}--goods_id:{}--扣减库存失败，解冻用户fungo币账户,修改订单状态: 8 商品库存不足,4 被冻结额已解冻 ,-1 未发货",
                                                mb_id, orderId, goods.getGoodsId());
                                        //扣减库存失败
                                        //解冻用户fungo币账户
                                        this.unfreezeAccountCoinWithMember(mb_id, goodsPriceVcy);
                                        //修改订单状态  8 商品库存不足  4 被冻结额已解冻  -1 未发货
                                        updateOrderStatusWithScan(mb_id, orderId, 8, 4, -1);
                                        continue;
                                    }

                                    //6. 交易成功修改订单状态  5 交易成功 & 2 已付款   &  4 无收货信息
                                    boolean isUpdateSucc = updateOrderStatusWithScan(mb_id, orderId, 5, 2, 4);

                                    logger.info("mb_id:{}--order_id:{}--goods_id:{}--交易成功修改订单状态:5 交易成功,2 已付款,4 无收货信息",
                                            mb_id, orderId, goods.getGoodsId());


                                    //6.1 若是虚拟商品，把虚拟卡号，转移给用户对应的虚拟商品 ,
                                    // 修改虚拟卡表为已经卖出状态，同时把虚拟卡信息保存到订单商品表中
                                    // 若虚拟商品不存在，则由客户手动处理，程序不做处理
                                    MallVirtualCard vCardNewWithOrderGoods = null;
                                    switch (goods.getGoodsType().intValue()) {
                                        case 21:
                                            vCardNewWithOrderGoods = updateOrderGoodsInfoWithVCard(mb_id, orderId, goods);
                                            break;
                                        case 22:
                                            vCardNewWithOrderGoods = updateOrderGoodsInfoWithVCard(mb_id, orderId, goods);
                                            break;
                                        case 23:
                                            vCardNewWithOrderGoods = updateOrderGoodsInfoWithVCard(mb_id, orderId, goods);
                                            break;
                                        default:
                                            break;
                                    }

                                    //7. 记录用户fungo币消费明细
                                    addFungoPayLogs(mb_id, mallOrder.getMbMobile(), mallOrder.getMbName(), String.valueOf(goods.getGoodsPriceVcy()),
                                            goods.getGoodsName());
                                    //8. 推送系统消息通知用户商品秒杀成功和虚拟卡商品卡号密码等信息
                                    if (isUpdateSucc) {
                                        pushMsgToMember(mb_id, goods.getGoodsName(), goods.getGoodsType(), vCardNewWithOrderGoods);
                                    }

                                }
                            }
                            // 9. 清除缓存的订单数据
                            logger.info("用户订单交易成功，删除该用户订单缓存,mb_id:{}", mb_id);
                            removeCacheWithOrders(mb_id, String.valueOf(orderId));
                            removeCacheWithOrders(mb_id, "");
                        }
                    }

                }
            }

        } catch (Exception ex) {
            logger.error("扫描订单表,处理已确定和已冻结余额的订单出现异常", ex);
            ex.printStackTrace();
            throw new BusinessException("-1", "扫描订单表,处理已确定和已冻结余额的订单出现异常");
        } finally {
            //处理完设置扫描订单为 3 完成扫描订单
            FungoMallSeckillTaskStateCommand.getInstance().setScanOrderIsOk(3);
            //清除缓存的商品数据
            removeCacheWithGoods();
        }
    }


    /**
     * 清除缓存的商品数据
     */
    private void removeCacheWithGoods() {
        //获取当前日期 yyyyMMdd
        String currentDate = DateTools.getCurrentDate(null);
        String cacheKey = FungoMallSeckillConsts.CACHE_KEY_MALL_SECKILL_GOODS + currentDate;
        FunGoEHCacheUtils.remove(FunGoGameConsts.CACHE_EH_NAME, cacheKey);
    }


    /**
     * 清除缓存的用户订单数据
     */
    private void removeCacheWithOrders(String mb_id, String orderId) {
        String cacheKey = FungoMallSeckillConsts.CACHE_KEY_MALL_SECKILL_ORDER + mb_id + orderId;
        logger.info("扫描处理订单-清除缓存的用户订单数据-缓存key:{}", cacheKey);
        FunGoEHCacheUtils.remove(FunGoGameConsts.CACHE_EH_NAME, cacheKey);
    }


    /**
     * 记录用户fungo 币消费日志
     * @param userId
     * @param phone
     * @param nickName
     * @param fungoCoin
     * @param goodsName
     */
    private void addFungoPayLogs(String userId, String phone, String nickName, String fungoCoin, String goodsName) {
        fungoMallSeckillLogService.addCoinMbToLog(userId, phone, nickName, fungoCoin, goodsName);
    }

    /**
     * 推送消息-异步
     * @param mb_Id
     * @param goodsName
     * @param goodsType
     * @param vCard
     */
    private void pushMsgToMember(final String mb_Id, final String goodsName, final Integer goodsType, final MallVirtualCard vCard) {

        String cardSn = "";
        String cardPwd = "";
        String validPeriodIntro = "";
        if (null != vCard) {
            cardSn = vCard.getCardSn();
            cardPwd = vCard.getCardPwd();
            validPeriodIntro = vCard.getValidPeriodIntro();
        }
        fungoMallSeckillSuccessAdviceService.pushMsgToMember(mb_Id, goodsName,
                goodsType, cardSn, cardPwd, validPeriodIntro);

    }


    /**
     * 更新用户订单商品信息
     * 把虚拟卡号，转移给用户对应的虚拟商品
     * @param mb_Id 用户ID
     * @param orderId 订单ID
     * @param orderGoods 订单商品关系表实体
     * @return
     */
    private MallVirtualCard updateOrderGoodsInfoWithVCard(String mb_Id, Long orderId, MallOrderGoods orderGoods) {

        MallVirtualCard virtualCard = this.queryMallVirtualCard(orderGoods.getGoodsId(), orderGoods.getGoodsType());

        logger.info("更新用户订单商品信息--把虚拟卡号，转移给用户对应的虚拟商品---orderGoods:{}---virtualCard:{}",
                orderGoods, virtualCard);

        if (null == orderGoods || null == virtualCard) {
            return null;
        }


        //卡号-加密的
        String cardSnEncrypt = virtualCard.getCardSn();
        String cardSnDecrypt = "";
        if (StringUtils.isNoneBlank(cardSnEncrypt)) {
            //解密
            cardSnDecrypt = FungoAESUtil.decrypt(cardSnEncrypt, aESSecretKey + FungoMallSeckillConsts.AES_SALT);
        }

        //卡密-加密的
        String cardPwdEncrypt = virtualCard.getCardPwd();
        String cardPwdDecrypt = "";
        if (StringUtils.isNoneBlank(cardPwdEncrypt)) {
            cardPwdDecrypt = FungoAESUtil.decrypt(cardPwdEncrypt, aESSecretKey + FungoMallSeckillConsts.AES_SALT);
        }
        //获取解密之后的crc32值
        long cardCrc32Validate = FungoCRC32Util.getInstance().encrypt(cardSnDecrypt + cardPwdDecrypt);

        //数据库中保存的数据有效性验证 卡号明码 + 密码明码的 crc32取值
        long cardCrc32 = virtualCard.getCardCrc32();

        //若卡号和密码，验证通过，继续处理
        if (cardCrc32Validate != cardCrc32) {
            logger.info("更新用户订单商品信息,把虚拟卡号，转移给用户对应的虚拟商品失败,卡号和密码有效性验证失败。mb_Id:{}--orderId:{}---goodsId:{}----DBCardCrc32:{}---cardCrc32Validate:{}",
                    mb_Id, orderId, orderGoods.getGoodsId(), cardCrc32, cardCrc32Validate);
            return null;
        }

        //截至使用日期
        Date cardEndDate = virtualCard.getCardEndDate();
        //有效期描述
        String validPeriodIntro = virtualCard.getValidPeriodIntro();
        //价值RMB面额
        Integer valueRmb = virtualCard.getValueRmb();

        //卡的类型
        Integer cardType = virtualCard.getCardType();


        //更新订单商品表
        MallVirtualCard vCardNewWithOrderGoods = new MallVirtualCard();
        vCardNewWithOrderGoods.setId(virtualCard.getId());
        vCardNewWithOrderGoods.setCardSn(cardSnDecrypt);
        vCardNewWithOrderGoods.setCardPwd(cardPwdDecrypt);
        vCardNewWithOrderGoods.setCardEndDate(cardEndDate);
        vCardNewWithOrderGoods.setValidPeriodIntro(validPeriodIntro);
        vCardNewWithOrderGoods.setValueRmb(valueRmb);
        vCardNewWithOrderGoods.setCardType(cardType);

        boolean updateOrderGoodsResult = updateOrderGoodsInfoWithScan(orderGoods, vCardNewWithOrderGoods);
        logger.info("更新用户订单商品信息--结果:{}", updateOrderGoodsResult);

        //更新虚拟卡号信息表
        boolean updateVCardResult = updateVCardInfoWithScan(mb_Id, orderId, virtualCard);
        logger.info("更新虚拟卡号信息表--结果:{}", updateVCardResult);

        return vCardNewWithOrderGoods;
    }

    /**
     * 更新虚拟卡号信息表
     * @param orderGoods
     * @param virtualCard
     * @return
     */
    private boolean updateOrderGoodsInfoWithScan(MallOrderGoods orderGoods, MallVirtualCard virtualCard) {

        String goodsAttJson = orderGoods.getGoodsAtt();

        //保存商品本身信息和兑换卡信息 {"goodsInfo":"" , "cardInfo:"}
        Map<String, Object> goodsAttMap = new HashMap<String, Object>();
        String goodsInfo = "";
        if (StringUtils.isNoneBlank(goodsAttJson)) {
            JSONObject jsonObject = JSONObject.parseObject(goodsAttJson);
            goodsInfo = jsonObject.getString("goodsInfo");
        }

        goodsAttMap.put("goodsInfo", goodsInfo);
        goodsAttMap.put("cardInfo", JSON.toJSONString(virtualCard));


        MallOrderGoods orderGoodsNew = new MallOrderGoods();
        orderGoodsNew.setGoodsAtt(JSON.toJSONString(goodsAttMap));
        orderGoodsNew.setUpdatedAt(new Date());

        EntityWrapper<MallOrderGoods> orderGoodsEntityWrapper = new EntityWrapper<MallOrderGoods>();
        orderGoodsEntityWrapper.eq("id", orderGoods.getId());

        boolean orderGoodsUpdateOK = mallOrderGoodsDaoService.update(orderGoodsNew, orderGoodsEntityWrapper);

        return orderGoodsUpdateOK;
    }


    /**
     * 更新虚拟卡信息表
     * 修改虚拟卡表为已经卖出、属于某个订单、用户等状态状态
     * @param mb_Id
     * @param orderId
     * @param virtualCard
     * @return
     */
    private boolean updateVCardInfoWithScan(String mb_Id, Long orderId, MallVirtualCard virtualCard) {
        EntityWrapper<MallVirtualCard> vCardEntityWrapper = new EntityWrapper<MallVirtualCard>();
        vCardEntityWrapper.eq("id", virtualCard.getId());


        MallVirtualCard virtualCardNew = new MallVirtualCard();
        virtualCardNew.setIsSaled(1);
        virtualCardNew.setMbId(mb_Id);
        virtualCardNew.setOrderId(orderId);

        virtualCardNew.setUpdatedAt(new Date());

        return mallVirtualCardDaoService.update(virtualCardNew, vCardEntityWrapper);
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

            logger.info("解冻用户fungo币账户被冻结商品价格-执行解冻计算后账户详情：{}", JSON.toJSONString(incentAccountCoinNew));

            //执行账户更新
            boolean updateOk = incentAccountCoinService.update(incentAccountCoinNew, mbAccountCoinEntityWrapper);
            logger.info("更新解冻用户-用户Id:{}--fungo币账户被冻结商品价格-商品价格:{}--执行结果:{}", mb_id, goodsPriceVcy, updateOk);
        }
    }


    /**
     * 查询用户订单关联的商品数据
     * @param mb_id
     * @param orderId
     * @return
     */
    private List<MallOrderGoods> queryOrderGoodsWithMember(String mb_id, String orderId) {

        EntityWrapper<MallOrderGoods> orderGoodsEntityWrapper = new EntityWrapper<MallOrderGoods>();
        orderGoodsEntityWrapper.eq("mb_id", mb_id).eq("order_id", orderId);
        return mallOrderGoodsDaoService.selectList(orderGoodsEntityWrapper);
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


    /**
     * 在扫描订单时，再次验证商品库存是否足够
     * @param mallSeckill 秒杀商品库存
     * @param goodsNumber 商品购买数量
     * @return
     */
    public boolean isFullGoodsStockForScanOrder(MallSeckill mallSeckill, Long goodsNumber, Long goodsId) {

        boolean isFullGoodsStock = false;

        //剩余库存 加密保存
        if (null != mallSeckill) {

            String residueStock = mallSeckill.getResidueStock();
            //解密
            String residueStockDecrypt = FungoAESUtil.decrypt(residueStock, aESSecretKey + FungoMallSeckillConsts.AES_SALT);
            logger.info("处理订单验证秒杀的商品库存是否充足---商品id:{}--剩余库存:{}", goodsId, residueStockDecrypt);

            if (StringUtils.isNoneBlank(residueStockDecrypt)) {
                long residueStock_i = Long.parseLong(residueStockDecrypt);
                if (residueStock_i >= goodsNumber.longValue()) {
                    isFullGoodsStock = true;
                }
            }
        }

        return isFullGoodsStock;
    }


    /**
     * 扣减库存
     * @param mallSeckill
     * @param goodsNumber
     */
    private boolean deductionGoodsStock(MallSeckill mallSeckill, Long goodsNumber) {

        String residueStock = mallSeckill.getResidueStock();
        //解密
        String residueStockDecrypt = FungoAESUtil.decrypt(residueStock, aESSecretKey + FungoMallSeckillConsts.AES_SALT);

        if (StringUtils.isNoneBlank(residueStockDecrypt)) {
            long residueStock_l = Long.parseLong(residueStockDecrypt);
            residueStock_l -= goodsNumber;
            //库存小于0 不再扣减
            if (residueStock_l < 0) {
                return false;
            }

            //更新商品库存
            Integer oldCasVersion = mallSeckill.getCasVersion();

            MallSeckill mallSeckillNew = new MallSeckill();
            //剩余库存加密保存
            String residueStockEncrypt = FungoAESUtil.encrypt(String.valueOf(residueStock_l), aESSecretKey + FungoMallSeckillConsts.AES_SALT);
            mallSeckillNew.setResidueStock(residueStockEncrypt);
            mallSeckillNew.setCasVersion(oldCasVersion + 1);


            EntityWrapper<MallSeckill> mallSeckillEntityWrapper = new EntityWrapper<MallSeckill>();
            Map<String, Object> criteriaMap = new HashMap<String, Object>();
            criteriaMap.put("goods_id", mallSeckill.getGoodsId());
            criteriaMap.put("cas_version", oldCasVersion);
            criteriaMap.put("id", mallSeckill.getId());
            mallSeckillEntityWrapper.allEq(criteriaMap);

            return mallSeckillDaoService.update(mallSeckillNew, mallSeckillEntityWrapper);
        }
        return false;
    }


    /**
     *  扣减用户fungo币账户被冻结商品价格
     * @param mb_id
     * @param goodsPriceVcy
     */
    private boolean deductionAccountCoinWithMember(String mb_id, Long goodsPriceVcy) {

        EntityWrapper<IncentAccountCoin> mbAccountCoinEntityWrapper = new EntityWrapper<IncentAccountCoin>();
        Map<String, Object> criteriaMap = new HashMap<String, Object>();
        criteriaMap.put("mb_id", mb_id);
        mbAccountCoinEntityWrapper.allEq(criteriaMap);

        IncentAccountCoin incentAccountCoinDB = incentAccountCoinService.selectOne(mbAccountCoinEntityWrapper);

        logger.info("扣减用户fungo币账户被冻结商品价格-扣减前账户详情：{}", JSON.toJSONString(incentAccountCoinDB));
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

            IncentAccountCoin incentAccountCoinNew = new IncentAccountCoin();
            incentAccountCoinNew.setCoinFreeze(coinFreezeNew);

            logger.info("扣减用户fungo币账户被冻结商品价格-扣减计算后，账户详情：{}", JSON.toJSONString(incentAccountCoinNew));
            //执行账户更新
            boolean updateOk = incentAccountCoinService.update(incentAccountCoinNew, mbAccountCoinEntityWrapper);
            logger.info("扣减用户fungo币账户被冻结商品价格-用户Id:{}--fungo币账户被冻结商品价格-商品价格:{}--账户冻结币量:{}--执行结果:{}", mb_id, goodsPriceVcy,
                    coinFreezeNew.toString(), updateOk);

            return updateOk;
        }
        return false;
    }


    /**
     * 查询虚拟商品对用的卡号、密码等信息
     * @param goodsId
     * @param card_type
     * @return
     */
    private MallVirtualCard queryMallVirtualCard(Long goodsId, Integer card_type) {

        EntityWrapper<MallVirtualCard> vCardEntityWrapper = new EntityWrapper<MallVirtualCard>();
        vCardEntityWrapper.eq("goods_id", goodsId);
        vCardEntityWrapper.eq("card_type", card_type);

        //是否卖出: -1否 ; 1 是
        vCardEntityWrapper.eq("is_saled", -1).last("limit 1");

        MallVirtualCard virtualCard = mallVirtualCardDaoService.selectOne(vCardEntityWrapper);

        String vCardJson = "";
        if (null != virtualCard) {
            vCardJson = JSON.toJSONString(virtualCard);
        }
        logger.info("获取用户秒杀的虚拟商品卡信息:{}", vCardJson);


        return virtualCard;
    }


    /**
     * 验证秒杀的商品库存是否充足
     * @param goodsId
     * @return
     */
    private MallSeckill isFullGoodsStock(String goodsId) {

        String currengDate = DateTools.getCurrentDate("-");

        EntityWrapper<MallSeckill> mallSeckillEntityWrapper = new EntityWrapper<MallSeckill>();

        Map<String, Object> criteriaMap = new HashMap<String, Object>();
        criteriaMap.put("goods_id", goodsId);
        criteriaMap.put("start_time", currengDate + " " + FungoMallSeckillConsts.SECKILL_START_TIME);
        criteriaMap.put("end_time", currengDate + " " + FungoMallSeckillConsts.SECKILL_END_TIME);

        mallSeckillEntityWrapper.allEq(criteriaMap);

        MallSeckill mallSeckill = mallSeckillDaoService.selectOne(mallSeckillEntityWrapper);
        logger.info("验证秒杀的商品库存是否充足，获取库存数据:{}", JSON.toJSONString(mallSeckill));

        return mallSeckill;
    }


    /**
     * 查询用户的所有订单
     * 秒杀数据有限，不做分页
     * @param mb_id
     * @return
     */
    private List<MallOrder> queryAllOrderWithMember(String mb_id) {

        EntityWrapper<MallOrder> orderEntityWrapper = new EntityWrapper<MallOrder>();
        orderEntityWrapper.eq("mb_id", mb_id);
        return mallOrderDaoService.selectList(orderEntityWrapper);
    }


    //------------
}
