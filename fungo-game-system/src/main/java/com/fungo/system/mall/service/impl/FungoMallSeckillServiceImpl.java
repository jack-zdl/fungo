package com.fungo.system.mall.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.entity.IncentAccountCoin;
import com.fungo.system.entity.Member;
import com.fungo.system.mall.daoService.*;
import com.fungo.system.mall.entity.*;
import com.fungo.system.mall.service.IFungoMallSeckillService;
import com.fungo.system.mall.service.IMallLogsService;
import com.fungo.system.mall.service.commons.FungoMallSeckillTaskStateCommand;
import com.fungo.system.mall.service.consts.FungoMallSeckillConsts;
import com.fungo.system.service.IncentAccountCoinDaoService;
import com.fungo.system.service.MemberService;
import com.game.common.consts.FunGoGameConsts;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.mall.*;
import com.game.common.repo.cache.facade.FungoCacheSystem;
import com.game.common.repo.cache.facade.FungoCacheTask;
import com.game.common.util.FunGoEHCacheUtils;
import com.game.common.util.FungoAESUtil;
import com.game.common.util.PKUtil;
import com.game.common.util.UUIDUtils;
import com.game.common.util.date.DateTools;
import com.game.common.util.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public class FungoMallSeckillServiceImpl implements IFungoMallSeckillService {


    private static final Logger logger = LoggerFactory.getLogger(FungoMallSeckillServiceImpl.class);


    @Autowired
    private MallGoodsDaoService mallGoodsDaoService;

    @Autowired
    private MallSeckillDaoService mallSeckillDaoService;

    @Autowired
    private MallVirtualCardDaoService mallVirtualCardDaoService;

    @Autowired
    private IncentAccountCoinDaoService incentAccountCoinService;

    @Autowired
    private MallOrderDaoService mallOrderDaoService;

    @Autowired
    private MallOrderGoodsDaoService mallOrderGoodsDaoService;

    @Autowired
    private IMallLogsService iMallLogsService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private FungoCacheTask fungoCacheTask;

    @Autowired
    private FungoCacheSystem fungoCacheSystem;


    @Value("${fungo.mall.seckill.aesSecretKey}")
    private String aESSecretKey;

    @Value("${sys.config.fungo.cluster.index}")
    private String clusterIndex;


    @Override
    public String getGoodsListForSeckillJson(String mb_id, String realIp) {


        logger.info("查询秒杀活动的商品....");
        //查询可以秒杀的商品从2019-02-03~2019-02-10
        //获取当前日期 yyyyMMdd
        String currentDate = DateTools.getCurrentDate(null);
        List<MallGoodsOutBean> goodsOutBeanList = null;
        //缓存key
        String cacheKey = FungoMallSeckillConsts.CACHE_KEY_MALL_SECKILL_GOODS + currentDate;

        try {

            //记录日志
            addMallLogs(mb_id, "", 0L, realIp, 1);

            //已指定秒杀活动开始和结束日期
            int startDate = FungoMallSeckillConsts.SECKILL_START_DATE;
            int endDate = FungoMallSeckillConsts.SECKILL_END_DATE;

            int currentDate_i = Integer.parseInt(currentDate);
            int endDate_i = Integer.parseInt(currentDate);

            //若当前日期大于2020-12-31，则不查询商品
            if (endDate_i > endDate) {
                return "";
            }

            //------从缓存中获取商品数据
           /* String goodsCacheJson = (String) FunGoEHCacheUtils.get(FunGoGameConsts.CACHE_EH_NAME, cacheKey);
            if (StringUtils.isNoneBlank(goodsCacheJson)) {
                logger.info("查询秒杀活动的商品，从ehcache缓存中获取:{}", goodsCacheJson);
                return goodsCacheJson;
            }*/
            //------end------

            String queryStartDate = "";
            String queryEndDate = "";
            //若当前日期小于2月3日，则显示2月3日的商品
            if (startDate > currentDate_i) {
                queryStartDate = FungoMallSeckillConsts.SECKILL_START_DATE_FORMAT + " " + FungoMallSeckillConsts.SECKILL_START_TIME;
                queryEndDate = FungoMallSeckillConsts.SECKILL_START_DATE_FORMAT + " " + FungoMallSeckillConsts.SECKILL_END_TIME;
            } else {
                queryStartDate = DateTools.getCurrentDate("-") + " " + FungoMallSeckillConsts.SECKILL_START_TIME;
                queryEndDate = DateTools.getCurrentDate("-") + " " + FungoMallSeckillConsts.SECKILL_END_TIME;
            }


            /*
                 获取秒杀当天的商品:
                 goods_type 商品类型
                                    1 实物
                                    2 虚拟物品
                                       21 零卡
                                       22 京东卡
                                       23 QB卡
                                    3 游戏礼包
             * goods_status 产品状态 :
             *                       -1 已删除 ，1 已 下架  ，  2 已 上架
             */
            List<Map<String, Object>> goodsMapList = mallSeckillDaoService.querySeckillGoods(queryStartDate, queryEndDate, "1,2,21,22,23", 2);
            if (null != goodsMapList && !goodsMapList.isEmpty()) {
                goodsOutBeanList = new ArrayList<MallGoodsOutBean>();

                for (Map<String, Object> objectMap : goodsMapList) {

                    MallGoodsOutBean goodsOutBean = new MallGoodsOutBean();

                    Long goodsId = (Long) objectMap.get("goodsId");
                    String goods_name = (String) objectMap.get("goods_name");
                    String main_img = (String) objectMap.get("main_img");
                    Integer sort = (Integer) objectMap.get("sort");
                    Integer goodsType = (Integer) objectMap.get("goods_type");
                    String goods_intro = (String) objectMap.get("goods_intro");

                    //价格解密
                    String seckill_price_vcy = (String) objectMap.get("seckill_price_vcy");
                    seckill_price_vcy = FungoAESUtil.decrypt(seckill_price_vcy,
                            aESSecretKey + FungoMallSeckillConsts.AES_SALT);


                    //剩余库存解析
                    String residue_stock = (String) objectMap.get("residue_stock");
                    residue_stock = FungoAESUtil.decrypt(residue_stock,
                            aESSecretKey + FungoMallSeckillConsts.AES_SALT);


                    Date start_time = (Date) objectMap.get("start_time");
                    if (null != start_time) {
                        String startDate_s = DateTools.fmtSimpleDateToString(start_time);
                        goodsOutBean.setStartTime(startDate_s);
                    }
                    Date end_time = (Date) objectMap.get("end_time");
                    if (null != end_time) {
                        String endDate_s = DateTools.fmtSimpleDateToString(end_time);
                        goodsOutBean.setEndTime(endDate_s);
                    }

                    goodsOutBean.setId(String.valueOf(goodsId));
                    goodsOutBean.setGoodsName(goods_name);
                    goodsOutBean.setSeckillPriceVcy(seckill_price_vcy);
                    goodsOutBean.setResidueStock(residue_stock);
                    goodsOutBean.setMainImg(main_img);
                    goodsOutBean.setSort(sort);
                    goodsOutBean.setGoodsIntro(goods_intro);
                    goodsOutBean.setGoodsType(goodsType);

                    goodsOutBeanList.add(goodsOutBean);

                }
            }

        } catch (Exception ex) {
            logger.error("获取秒杀商品列表出现异常", ex);
            ex.printStackTrace();
        }

        String goodsResultJSON = "";
        if (null != goodsOutBeanList && !goodsOutBeanList.isEmpty()) {
            goodsResultJSON = JSON.toJSONString(goodsOutBeanList);
            //把商品数据保存到缓存中
            //FunGoEHCacheUtils.put(FunGoGameConsts.CACHE_EH_NAME, cacheKey, goodsResultJSON);
        }

        return goodsResultJSON;

    }

    @Override
    public List<MallGoodsOutBean> getGoodsListForGame(String mb_id, String realIp, MallGoodsInput mallGoodsInput) {


        List<MallGoodsOutBean> goodsOutBeanList = null;

        try {

            //从Redis获取
            String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_GAME_GOODS_LIST;
            String keySuffix = mb_id + "_" + JSON.toJSONString(mallGoodsInput);

            goodsOutBeanList = (List<MallGoodsOutBean>) fungoCacheSystem.getIndexCache(keyPrefix, keySuffix);
            if (null != goodsOutBeanList && !goodsOutBeanList.isEmpty()) {
                return goodsOutBeanList;
            }


            EntityWrapper<MallGoods> mallGoodsEntityWrapper = new EntityWrapper<MallGoods>();

            mallGoodsEntityWrapper.eq("game_id", mallGoodsInput.getGameId());
            mallGoodsEntityWrapper.eq("goods_type", mallGoodsInput.getGoodsType());
            //查询已上架的商品
            mallGoodsEntityWrapper.eq("goods_status", 2);

            mallGoodsEntityWrapper.orderBy("created_at", false);

            List<MallGoods> goodsMapList = mallGoodsDaoService.selectList(mallGoodsEntityWrapper);


            if (null != goodsMapList && !goodsMapList.isEmpty()) {
                goodsOutBeanList = new ArrayList<MallGoodsOutBean>();

                for (MallGoods mallGoods : goodsMapList) {

                    MallGoodsOutBean goodsOutBean = new MallGoodsOutBean();

                    Long goodsId = mallGoods.getId();
                    String goods_name = mallGoods.getGoodsName();
                    Integer sort = mallGoods.getSort();
                    String goods_intro = mallGoods.getGoodsIntro();

                    Long seckill_price_vcy = mallGoods.getMarketPriceVcy();

                    //有效期描述信息
                    String validPeriodIntro = mallGoods.getExt1();

                    //使用方法说明
                    String usageDesc = mallGoods.getUsageDesc();

                    goodsOutBean.setId(String.valueOf(goodsId));
                    goodsOutBean.setGoodsName(goods_name);
                    goodsOutBean.setSeckillPriceVcy(String.valueOf(seckill_price_vcy));

                    goodsOutBean.setSort(sort);
                    goodsOutBean.setGoodsIntro(goods_intro);

                    goodsOutBean.setValidPeriodIntro(validPeriodIntro);
                    goodsOutBean.setUsageDesc(usageDesc);

                    //当前礼包是否过期
                    if (StringUtils.isNoneBlank(validPeriodIntro)) {
                        String[] startAndEndDate = validPeriodIntro.split("~");
                        if (null != startAndEndDate && startAndEndDate.length >= 2) {
                            String endDateStr = startAndEndDate[1];

                            String beginDateStr = DateTools.fmtDate(new Date());

                            long interval = DateTools.getDaySub(beginDateStr, endDateStr);

                            if (interval < 0) {
                                goodsOutBean.setIs_expire(true);
                            }
                        }

                    }

                    goodsOutBeanList.add(goodsOutBean);


                    //验证用户是否购买过
                    boolean buyedValid = this.isBuyedVMCardValidWithGame(mb_id, goodsId);
                    goodsOutBean.setIs_buy(buyedValid);

                    //查询该商品剩余的卡号数量
                    int unSaledVMCardCount = this.getUnSaledVMCardWithGame(mb_id, goodsId);
                    goodsOutBean.setResidueStock(String.valueOf(unSaledVMCardCount));


                }
            }
            //保存到Redis中
            fungoCacheSystem.excIndexCache(true, keyPrefix, keySuffix, goodsOutBeanList, 3);

        } catch (Exception ex) {
            logger.error("获取秒杀商品列表出现异常", ex);
            ex.printStackTrace();
        }
        return goodsOutBeanList;


    }


    @Override
    public List<MallGoodsOutBean> getGoodsListForSeckill(String mb_id, String realIp) {

        List<MallGoodsOutBean> goodsOutBeanList = null;

        try {
            //记录日志
            addMallLogs(mb_id, "", 0L, realIp, 1);

            //查询可以秒杀的商品从2019-02-03~2019-02-10
            //获取当前日期 yyyyMMdd
            String currentDate = DateTools.getCurrentDate(null);

            //已指定秒杀活动开始和结束日期
            int startDate = FungoMallSeckillConsts.SECKILL_START_DATE;
            int endDate = FungoMallSeckillConsts.SECKILL_END_DATE;

            int currentDate_i = Integer.parseInt(currentDate);
            int endDate_i = Integer.parseInt(currentDate);

            //若当前日期大于2月10日，则不查询商品
            if (endDate_i > endDate) {
                return goodsOutBeanList;
            }

            String queryStartDate = "";
            String queryEndDate = "";
            //若当前日期小于2月3日，则显示2月3日的商品
            if (startDate > currentDate_i) {
                queryStartDate = FungoMallSeckillConsts.SECKILL_START_DATE_FORMAT + " " + FungoMallSeckillConsts.SECKILL_START_TIME;
                queryEndDate = FungoMallSeckillConsts.SECKILL_START_DATE_FORMAT + " " + FungoMallSeckillConsts.SECKILL_END_TIME;
            } else {
                queryStartDate = DateTools.getCurrentDate("-") + " " + FungoMallSeckillConsts.SECKILL_START_TIME;
                queryEndDate = DateTools.getCurrentDate("-") + " " + FungoMallSeckillConsts.SECKILL_END_TIME;
            }

            //获取秒杀当天的商品
            List<Map<String, Object>> goodsMapList = mallSeckillDaoService.querySeckillGoods(queryStartDate, queryEndDate, "1,2,21,22,23", 2);
            if (null != goodsMapList && !goodsMapList.isEmpty()) {
                goodsOutBeanList = new ArrayList<MallGoodsOutBean>();

                for (Map<String, Object> objectMap : goodsMapList) {

                    MallGoodsOutBean goodsOutBean = new MallGoodsOutBean();

                    Long goodsId = (Long) objectMap.get("goodsId");
                    String goods_name = (String) objectMap.get("goods_name");
                    String main_img = (String) objectMap.get("main_img");
                    Integer sort = (Integer) objectMap.get("sort");
                    String goods_intro = (String) objectMap.get("goods_intro");

                    //价格解密
                    String seckill_price_vcy = (String) objectMap.get("seckill_price_vcy");
                    seckill_price_vcy = FungoAESUtil.decrypt(seckill_price_vcy,
                            aESSecretKey + FungoMallSeckillConsts.AES_SALT);


                    //剩余库存解析
                    String residue_stock = (String) objectMap.get("residue_stock");
                    residue_stock = FungoAESUtil.decrypt(residue_stock,
                            aESSecretKey + FungoMallSeckillConsts.AES_SALT);


                    Date start_time = (Date) objectMap.get("start_time");
                    if (null != start_time) {
                        String startDate_s = DateTools.fmtSimpleDateToString(start_time);
                        goodsOutBean.setStartTime(startDate_s);
                    }
                    Date end_time = (Date) objectMap.get("end_time");
                    if (null != end_time) {
                        String endDate_s = DateTools.fmtSimpleDateToString(end_time);
                        goodsOutBean.setEndTime(endDate_s);
                    }

                    goodsOutBean.setId(String.valueOf(goodsId));
                    goodsOutBean.setGoodsName(goods_name);
                    goodsOutBean.setSeckillPriceVcy(seckill_price_vcy);
                    goodsOutBean.setResidueStock(residue_stock);
                    goodsOutBean.setMainImg(main_img);
                    goodsOutBean.setSort(sort);
                    goodsOutBean.setGoodsIntro(goods_intro);

                    goodsOutBeanList.add(goodsOutBean);

                }
            }

        } catch (Exception ex) {
            logger.error("获取秒杀商品列表出现异常", ex);
            ex.printStackTrace();
        }
        return goodsOutBeanList;
    }


    @Override
    public String getSeckillGrantCode(String mb_id) {

        String code = null;

        try {
            //获取当前日期 yyyy-MM-dd
            String currentDate = DateTools.getCurrentDate("-");
            //验证码必须在每天的  00:00:00 后发放
            /**
             //关闭秒杀时段限制 [by mxf 2019-04-03]
             if (!isSeckillDate(currentDate)) {
             return code;
             }*/

            Map<String, String> seckillCodeMap = FungoMallSeckillTaskStateCommand.getInstance().getSeckillCodeMap();

            if (!seckillCodeMap.containsKey(currentDate)) {

                code = UUIDUtils.getUUID();
                code = StringUtils.substring(code, 0, code.length() / 2);

                seckillCodeMap.put(currentDate, code);

            } else {
                code = seckillCodeMap.get(currentDate);
            }
        } catch (Exception ex) {
            logger.error("获取秒杀授权码出现异常", ex);
            ex.printStackTrace();
        }
        logger.info("秒杀授权码:{}", code);
        return code;
    }


    /**
     响应数据：
     mbId           用户ID
     orderSn        订单号
     seckillStatus  订单状态
     1 秒杀成功
     2 未到秒杀时间
     3 fungo币不足
     4 商品已被秒光
     5同件商品重复秒杀
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> createOrderWithSeckill(MallOrderInput orderInput, String realIp) {

        //记录日志
        addMallLogs(orderInput.getMbId(), "", Long.parseLong(orderInput.getGoodsId()), realIp, 2);

        logger.info("秒杀商品--用户id:{}--商品id:{}--msg:{}", orderInput.getMbId(), orderInput.getGoodsId(), "开始了...");

        Map<String, Object> resultMap = null;


        try {

            resultMap = new HashMap<String, Object>();
            resultMap.put("mbId", orderInput.getMbId());

            //1.验证是否到秒杀时间段
            boolean isStart = true;
            // isStartSeckill(orderInput.getCode());
            if (!isStart) {
                resultMap.put("seckillStatus", 2);

                logger.info("秒杀商品失败--用户id:{}--商品id:{}--msg:{}", orderInput.getMbId(), orderInput.getGoodsId(), "未到秒杀时间段");
                return resultMap;
            }

            //2.验证秒杀的商品库存是否充足
            boolean isFullGoodsStock = false;
            MallSeckill mallSeckill = isFullGoodsStock(orderInput.getGoodsId());

            //剩余库存 加密保存
            if (null != mallSeckill) {
                String residueStock = mallSeckill.getResidueStock();
                //解密
                String residueStockDecrypt = FungoAESUtil.decrypt(residueStock, aESSecretKey + FungoMallSeckillConsts.AES_SALT);
                logger.info("验证秒杀的商品库存是否充足---商品id:{}--剩余库存:{}", orderInput.getGoodsId(), residueStockDecrypt);

                if (StringUtils.isNoneBlank(residueStockDecrypt)) {
                    int residueStock_i = Integer.parseInt(residueStockDecrypt);
                    if (residueStock_i > 0) {
                        isFullGoodsStock = true;
                    }
                }
            }


            if (!isFullGoodsStock) {
                resultMap.put("seckillStatus", 4);
                logger.info("秒杀商品失败--用户id:{}--商品id:{}--msg:{}", orderInput.getMbId(), orderInput.getGoodsId(), "秒杀的商品库存不足");
                return resultMap;
            }


            //3.当天不能重复秒杀同一个商品
            // 验证当前用户在当天，是否重复秒杀了同一件商品
            boolean isReBuy = isReBuy(orderInput.getMbId(), orderInput.getGoodsId());
            if (isReBuy) {
                resultMap.put("seckillStatus", 5);
                logger.info("秒杀商品失败--用户id:{}--商品id:{}--msg:{}", orderInput.getMbId(), orderInput.getGoodsId(), "当天不能重复秒杀同一个商品");
                return resultMap;
            }

            //4.有足够的fungo币可用余额，且下单后要冻结与订单金额对等的可用余额
            // 验证当前用户的fungo币可用余额是否大于等于当前商品的价格
            //  4.1 若fungo币可用余额 足够秒杀当前商品，则下单
            //  4.2 同时直接扣除可用余额，到冻结余额
            boolean isFullMbFungo = false;

            IncentAccountCoin incentAccountCoin = isFullMbFungo(orderInput.getMbId());
            if (null == incentAccountCoin) {
                resultMap.put("seckillStatus", 3);
                logger.info("秒杀商品失败--用户id:{}--商品id:{}--msg:{}", orderInput.getMbId(), orderInput.getGoodsId(), "用户fungo币不足");
                return resultMap;
            }


            BigDecimal coinUsable = incentAccountCoin.getCoinUsable();
            if (null == coinUsable) {
                resultMap.put("seckillStatus", 3);
                logger.info("秒杀商品失败--用户id:{}--商品id:{}--msg:{}", orderInput.getMbId(), orderInput.getGoodsId(), "用户fungo币不足");
                return resultMap;
            }

            int isHaveCoin = coinUsable.compareTo(BigDecimal.ZERO);
            if (0 == isHaveCoin || -1 == isHaveCoin) {
                resultMap.put("seckillStatus", 3);
                logger.info("秒杀商品失败--用户id:{}--商品id:{}--msg:{}", orderInput.getMbId(), orderInput.getGoodsId(), "用户fungo币不足");
                return resultMap;
            }

            coinUsable = coinUsable.setScale(2);

            //获取商品的价格fungo币 加密串
            String seckillPriceVcy = mallSeckill.getSeckillPriceVcy();

            //解密
            String seckillPriceVcyDecrypt = FungoAESUtil.decrypt(seckillPriceVcy, aESSecretKey + FungoMallSeckillConsts.AES_SALT);

            BigDecimal goodsPriceVcyBD = new BigDecimal(seckillPriceVcyDecrypt);
            goodsPriceVcyBD = goodsPriceVcyBD.setScale(2);

            //可用余额是否大于等于商品价格
            int cmpResult = coinUsable.compareTo(goodsPriceVcyBD);
            if (0 == cmpResult || 1 == cmpResult) {
                isFullMbFungo = true;
            }

            if (!isFullMbFungo) {
                resultMap.put("seckillStatus", 3);
                logger.info("秒杀商品失败--用户id:{}--商品id:{}--msg:{}", orderInput.getMbId(), orderInput.getGoodsId(), "用户fungo币不足");
                return resultMap;
            }


            //5.执行下单
            //5.1 先冻结用户的可用余额
            int isFreezeMbCoinAccount = freezeMemberFungo(orderInput.getMbId(), incentAccountCoin, goodsPriceVcyBD);
            //5.2 再创建订单
            //冻结用户可用币量后生成订单
            if (1 == isFreezeMbCoinAccount) {
                logger.info("秒杀商品--用户id:{}--商品id:{}--msg:{}", orderInput.getMbId(), orderInput.getGoodsId(), "冻结用户的可用fungo币量成功，开始创建订单");

                //创建订单
                MallOrder mallOrder = addSeckillOrder(orderInput.getMbId(), orderInput.getGoodsId(), mallSeckill);

                if (null != mallOrder && StringUtils.isNoneBlank(mallOrder.getOrderSn())) {

                    logger.info("秒杀商品--用户id:{}--商品id:{}--订单号:{}--msg:{}", orderInput.getMbId(), orderInput.getGoodsId(), mallOrder.getOrderSn(),
                            "订单创建成功，正在排队秒杀商品...");
                    resultMap.put("orderId", String.valueOf(mallOrder.getId()));
                    resultMap.put("seckillStatus", 1);
                }
            } else {
                throw new BusinessException("-1", "冻结用户可用币量失败");
            }

        } catch (Exception ex) {
            logger.error("秒杀商品--用户id:{}--商品id:{}--出现异常:{}", orderInput.getMbId(), orderInput.getGoodsId(), ex);
            ex.printStackTrace();
            throw new BusinessException("-1", "秒杀商品出现异常");
        } finally {
            //清除缓存的订单数据
            logger.info("用户创建订单成功，删除该用户订单缓存,mb_id:{}", orderInput.getMbId());
            removeCacheWithOrders(orderInput.getMbId(), String.valueOf(resultMap.get("orderId")));
            removeCacheWithOrders(orderInput.getMbId(), "");
            //FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INCENTS_FORTUNE_COIN_POST + loginId;
            // 清除用户fun消耗缓存  FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INCENTS_FORTUNE_COIN_POST
            String detailFunCoinCacheKey = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INCENTS_FORTUNE_COIN_POST + orderInput.getMbId();
            ;
            fungoCacheTask.excIndexCache(false, detailFunCoinCacheKey, "", null);
            String detailFunInfoCacheKey = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + orderInput.getMbId();
            ;
            fungoCacheTask.excIndexCache(false, detailFunInfoCacheKey, "", null);
            //清除个人订单缓存
            String orderInfoCacheKey = FungoMallSeckillConsts.CACHE_KEY_MALL_SECKILL_ORDER + orderInput.getMbId();
            ;
            fungoCacheTask.excIndexCache(false, orderInfoCacheKey, "", null);
        }

        return resultMap;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> createOrderWithSeckillWithGame(MallOrderInput orderInput, String realIp) {

        //记录日志
        addMallLogs(orderInput.getMbId(), "", Long.parseLong(orderInput.getGoodsId()), realIp, 2);

        logger.info("秒杀游戏礼包商品--用户id:{}--游戏礼包商品id:{}--msg:{}", orderInput.getMbId(), orderInput.getGoodsId(), "开始了...");

        Map<String, Object> resultMap = null;


        try {

            //查询出游戏礼包商品详情
            EntityWrapper<MallGoods> goodsEntityWrapper = new EntityWrapper<MallGoods>();
            goodsEntityWrapper.eq("id", orderInput.getGoodsId());
            MallGoods goods = mallGoodsDaoService.selectOne(goodsEntityWrapper);

            if (null == goods) {
                resultMap.put("seckillStatus", 4);
                return resultMap;
            }

            resultMap = new HashMap<String, Object>();
            resultMap.put("mbId", orderInput.getMbId());


            //2.验证秒杀的游戏礼包商品库存是否充足
            //2.1  若是游戏礼包
            int unSaledVMCardCount = this.getUnSaledVMCardWithGame(null, Long.parseLong(orderInput.getGoodsId()));

            if (unSaledVMCardCount <= 0) {
                resultMap.put("seckillStatus", 4);
                logger.info("秒杀游戏礼包商品失败--用户id:{}--游戏礼包商品id:{}--msg:{}", orderInput.getMbId(), orderInput.getGoodsId(), "秒杀的游戏礼包商品库存不足");
                return resultMap;
            }


            //4.有足够的fungo币可用余额，且下单后要冻结与订单金额对等的可用余额
            // 验证当前用户的fungo币可用余额是否大于等于当前游戏礼包商品的价格
            //  4.1 若fungo币可用余额 足够秒杀当前游戏礼包商品，则下单
            //  4.2 同时直接扣除可用余额，到冻结余额
            boolean isFullMbFungo = false;

            IncentAccountCoin incentAccountCoin = isFullMbFungo(orderInput.getMbId());
            if (null == incentAccountCoin) {
                resultMap.put("seckillStatus", 3);
                logger.info("秒杀游戏礼包商品失败--用户id:{}--游戏礼包商品id:{}--msg:{}", orderInput.getMbId(), orderInput.getGoodsId(), "用户fungo币不足");
                return resultMap;
            }


            BigDecimal coinUsable = incentAccountCoin.getCoinUsable();
            if (null == coinUsable) {
                resultMap.put("seckillStatus", 3);
                logger.info("秒杀游戏礼包商品失败--用户id:{}--游戏礼包商品id:{}--msg:{}", orderInput.getMbId(), orderInput.getGoodsId(), "用户fungo币不足");
                return resultMap;
            }

            int isHaveCoin = coinUsable.compareTo(BigDecimal.ZERO);
            if (0 == isHaveCoin || -1 == isHaveCoin) {
                resultMap.put("seckillStatus", 3);
                logger.info("秒杀游戏礼包商品失败--用户id:{}--游戏礼包商品id:{}--msg:{}", orderInput.getMbId(), orderInput.getGoodsId(), "用户fungo币不足");
                return resultMap;
            }

            coinUsable = coinUsable.setScale(2);


            BigDecimal goodsPriceVcyBD = new BigDecimal(goods.getMarketPriceVcy());
            goodsPriceVcyBD = goodsPriceVcyBD.setScale(2);

            //可用余额是否大于等于游戏礼包商品价格
            int cmpResult = coinUsable.compareTo(goodsPriceVcyBD);
            if (0 == cmpResult || 1 == cmpResult) {
                isFullMbFungo = true;
            }

            if (!isFullMbFungo) {
                resultMap.put("seckillStatus", 3);
                logger.info("秒杀游戏礼包商品失败--用户id:{}--游戏礼包商品id:{}--msg:{}", orderInput.getMbId(), orderInput.getGoodsId(), "用户fungo币不足");
                return resultMap;
            }


            //5.执行下单
            //5.1 先冻结用户的可用余额
            int isFreezeMbCoinAccount = freezeMemberFungo(orderInput.getMbId(), incentAccountCoin, goodsPriceVcyBD);

            //5.2 再创建订单
            //冻结用户可用币量后生成订单
            if (1 == isFreezeMbCoinAccount) {
                logger.info("秒杀游戏礼包商品--用户id:{}--游戏礼包商品id:{}--msg:{}", orderInput.getMbId(), orderInput.getGoodsId(), "冻结用户的可用fungo币量成功，开始创建订单");

                //创建订单
                MallOrder mallOrder = addSeckillOrderWithGame(orderInput.getMbId(), goods);

                if (null != mallOrder && StringUtils.isNoneBlank(mallOrder.getOrderSn())) {

                    logger.info("秒杀游戏礼包商品--用户id:{}--游戏礼包商品id:{}--订单号:{}--msg:{}", orderInput.getMbId(), orderInput.getGoodsId(), mallOrder.getOrderSn(),
                            "订单创建成功，正在排队秒杀游戏礼包商品...");
                    resultMap.put("orderId", String.valueOf(mallOrder.getId()));
                    resultMap.put("seckillStatus", 1);
                }
            } else {
                throw new BusinessException("-1", "冻结用户可用币量失败");
            }

        } catch (Exception ex) {
            logger.error("秒杀游戏礼包商品--用户id:{}--游戏礼包商品id:{}--出现异常:{}", orderInput.getMbId(), orderInput.getGoodsId(), ex);
            ex.printStackTrace();
            throw new BusinessException("-1", "秒杀游戏礼包商品出现异常");
        } finally {

            //清除缓存的订单数据
            logger.info("用户创建订单成功，删除该用户订单缓存,mb_id:{}", orderInput.getMbId());

            // 清除用户fun消耗缓存  FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INCENTS_FORTUNE_COIN_POST
            String detailFunCoinCacheKey = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INCENTS_FORTUNE_COIN_POST + orderInput.getMbId();

        }

        return resultMap;
    }


    @Override
    public boolean updateOrderWithSeckill(MallOrderInput mallOrderInput) {

        try {
            logger.info("用户修改订单--用户Id:{}--订单Id:{}", mallOrderInput.getMbId(), mallOrderInput.getOrderId());

            MallOrder order = new MallOrder();
            order.setConsigneeName(mallOrderInput.getConsigneeName());
            order.setCsgAddress(mallOrderInput.getCsgAddress());
            order.setCsgMobile(mallOrderInput.getCsgMobile());
            //收货信息完整
            order.setShippingStatus(5);

            order.setUpdatedTime(new Date());

            EntityWrapper<MallOrder> orderEntityWrapper = new EntityWrapper<MallOrder>();
            orderEntityWrapper.eq("mb_id", mallOrderInput.getMbId()).eq("id", mallOrderInput.getOrderId());

            boolean updateOk = mallOrderDaoService.update(order, orderEntityWrapper);
            return updateOk;

        } catch (Exception ex) {
            logger.error("用户修改订单失败", ex);
            ex.printStackTrace();
        } finally {
            //清除缓存的订单数据
            logger.info("用户更新订单成功，删除该用户订单缓存,mb_id:{}", mallOrderInput.getMbId());
            removeCacheWithOrders(mallOrderInput.getMbId(), mallOrderInput.getOrderId());
            removeCacheWithOrders(mallOrderInput.getMbId(), "");

        }
        return false;
    }


    @Override
    public String getOrdersWithSeckillWithJsonStr(String mb_id, String orderId, String orderSn) {

        List<MallOrderOutBean> orderOutBeanList = null;

        //缓存key
        String cacheKey = FungoMallSeckillConsts.CACHE_KEY_MALL_SECKILL_ORDER + mb_id + orderId;
        logger.info("getOrdersWithSeckillWithJsonStr-获取订单数据-缓存key:{}", cacheKey);
        try {

            //从缓存中获取订单数据
           /* String ordersCacheJson = (String) FunGoEHCacheUtils.get(FunGoGameConsts.CACHE_EH_NAME, cacheKey);
            if (StringUtils.isNoneBlank(ordersCacheJson)) {

                logger.info("用户查询订单--用户id:{}--订单ID:{}--订单编号:{}，从ehCache缓存中获取...", mb_id, orderId, orderSn);
                return ordersCacheJson;
            }*/

            logger.info("用户查询订单--用户id:{}--订单ID:{}--订单编号:{}，从数据库中获取...", mb_id, orderId, orderSn);

            /*
              orderSn 若为空，则查询该用户的所有订单
              否则查询，orderSn订单详情
             */

            orderOutBeanList = new ArrayList<MallOrderOutBean>();

            //查询某个订单
            if (StringUtils.isNoneBlank(orderId)) {
                MallOrder mallOrder = mallOrderDaoService.selectById(orderId);
                if (null != mallOrder) {
                    //查询该订单关联的商品
                    List<MallOrderGoods> mallOrderGoodsList = queryOrderGoodsWithMember(mb_id, orderId);
                    MallOrderOutBean orderOutBean = createMemberOrderOut(mallOrder, mallOrderGoodsList);
                    orderOutBeanList.add(orderOutBean);
                }

                //查询该用户所有交易成功的订单
            } else {
                List<MallOrder> mallOrders = queryTradeSuccessOrderrWithMember(mb_id);
                if (null != mallOrders && !mallOrders.isEmpty()) {

                    for (MallOrder mallOrder : mallOrders) {
                        //查询该订单关联的商品
                        List<MallOrderGoods> mallOrderGoodsList = queryOrderGoodsWithMember(mb_id, String.valueOf(mallOrder.getId()));
                        MallOrderOutBean orderOutBean = createMemberOrderOut(mallOrder, mallOrderGoodsList);
                        orderOutBeanList.add(orderOutBean);

                    }
                }
            }

        } catch (Exception ex) {
            logger.error("查询用户秒杀订单失败", ex);
            ex.printStackTrace();
        }

        String orderDataJson = "";
        if (null != orderOutBeanList && !orderOutBeanList.isEmpty()) {
            orderDataJson = JSON.toJSONString(orderOutBeanList);
            FunGoEHCacheUtils.put(FunGoGameConsts.CACHE_EH_NAME, cacheKey, orderDataJson);
        }

        return orderDataJson;

    }


    @Override
    public List<MallOrderOutBean> getOrdersWithSeckillGame(String mb_id, String orderId, String orderSn, String orderType) {

        List<MallOrderOutBean> orderOutBeanList = null;
        try {

            logger.info("用户查询订单--用户id:{}--订单ID:{}--订单编号:{}---orderType:{}", mb_id, orderId, orderSn, orderType);

            orderOutBeanList = new ArrayList<MallOrderOutBean>();

            //查询某个订单
            if (StringUtils.isNoneBlank(orderId)) {
                MallOrder mallOrder = mallOrderDaoService.selectById(orderId);
                if (null != mallOrder) {
                    //查询该订单关联的商品
                    List<MallOrderGoods> mallOrderGoodsList = queryOrderGoodsWithMember(mb_id, orderId);
                    MallOrderOutBean orderOutBean = createMemberOrderOut(mallOrder, mallOrderGoodsList);
                    orderOutBeanList.add(orderOutBean);
                }

                //查询该用户所有交易成功的订单
            } else {
                List<MallOrder> mallOrders = queryTradeSuccessOrderrWithMember(mb_id);
                if (null != mallOrders && !mallOrders.isEmpty()) {

                    for (MallOrder mallOrder : mallOrders) {
                        //查询该订单关联的商品
                        List<MallOrderGoods> mallOrderGoodsList = queryOrderGoodsWithMember(mb_id, String.valueOf(mallOrder.getId()), orderType);

                        if (null != mallOrderGoodsList && !mallOrderGoodsList.isEmpty()) {
                            MallOrderOutBean orderOutBean = createMemberOrderOut(mallOrder, mallOrderGoodsList);
                            orderOutBeanList.add(orderOutBean);
                        }

                    }
                }
            }

        } catch (Exception ex) {
            logger.error("查询用户秒杀订单失败", ex);
            ex.printStackTrace();
        }
        return orderOutBeanList;
    }


    /**
     * 组建用户订单和订单关联的商品数据
     * @param mallOrder
     * @param orderGoodsList
     * @return
     */
    private MallOrderOutBean createMemberOrderOut(MallOrder mallOrder, List<MallOrderGoods> orderGoodsList) {

        MallOrderOutBean orderOutBean = new MallOrderOutBean();
        orderOutBean.setOrderId(String.valueOf(mallOrder.getId()));
        orderOutBean.setOrderSn(mallOrder.getOrderSn());
        orderOutBean.setMbId(mallOrder.getMbId());
        orderOutBean.setMbName(mallOrder.getMbName());
        orderOutBean.setMbMobile(mallOrder.getMbMobile());

        orderOutBean.setOrderStatus(mallOrder.getOrderStatus());
        orderOutBean.setPayStatus(mallOrder.getPayStatus());
        orderOutBean.setShippingStatus(mallOrder.getShippingStatus());

        orderOutBean.setGoodsAmountVcy(mallOrder.getGoodsAmountVcy());


        //收货人信息
        //若收货人信息都为空 则从已经交易成功订单里获取收货人数据
        String consigneeName = mallOrder.getConsigneeName();
        String csgAddress = mallOrder.getCsgAddress();
        String csgMobile = mallOrder.getCsgMobile();


        Integer orderStatus = mallOrder.getOrderStatus();
        Integer payStatus = mallOrder.getPayStatus();
        //若支付状态是 2已付款，订单状态是 5 交易成功 ，且收货人信息都为空，则表示秒杀成功后，需要填收货人信息
        //则从该用户已经成交的订单中获取历史上已填写的收货人信息
        if (5 == orderStatus && 2 == payStatus) {
            if (StringUtils.isBlank(consigneeName) && StringUtils.isBlank(csgAddress) && StringUtils.isBlank(csgMobile)) {

                MallOrder tradeSuccessOrder = queryTradeSuccessSingleOrderrWithMember(mallOrder.getMbId());
                if (null != tradeSuccessOrder) {
                    consigneeName = tradeSuccessOrder.getConsigneeName();
                    csgAddress = tradeSuccessOrder.getCsgAddress();
                    csgMobile = tradeSuccessOrder.getCsgMobile();
                }
            }
        }

        if (null != mallOrder.getCreateTime()) {
            orderOutBean.setCreateTime(DateTools.fmtDate(mallOrder.getCreateTime()));
        }

        if (null != mallOrder.getPayTime()) {
            orderOutBean.setPayTime(DateTools.fmtDate(mallOrder.getPayTime()));
        }

        if (null != mallOrder.getShippingTime()) {
            orderOutBean.setShippingTime(DateTools.fmtDate(mallOrder.getShippingTime()));
        }


        orderOutBean.setConsigneeName(consigneeName);
        orderOutBean.setCsgAddress(csgAddress);
        orderOutBean.setCsgMobile(csgMobile);


        if (null != orderGoodsList && !orderGoodsList.isEmpty()) {

            List<MallGoodsOutBean> goodsOutBeanList = new ArrayList<MallGoodsOutBean>();

            //订单下商品
            for (MallOrderGoods orderGoods : orderGoodsList) {

                MallGoodsOutBean goodsOutBean = new MallGoodsOutBean();
                goodsOutBean.setId(String.valueOf(orderGoods.getId()));
                goodsOutBean.setCid(String.valueOf(orderGoods.getGoodsCid()));
                goodsOutBean.setGoodsName(orderGoods.getGoodsName());
                //已经生成订单的商品，价格不加密
                goodsOutBean.setSeckillPriceVcy(String.valueOf(orderGoods.getGoodsPriceVcy().intValue()));

                //商品图片
                String goodsAttJson = orderGoods.getGoodsAtt();
                if (StringUtils.isNoneBlank(goodsAttJson)) {
                    JSONObject jsonObject = JSONObject.parseObject(goodsAttJson);
                    if (null != jsonObject) {
                        JSONObject goodsInfoJson = jsonObject.getJSONObject("goodsInfo");
                        if (null != goodsInfoJson) {
                            String mainImg = goodsInfoJson.getString("mainImg");
                            String goodsIntro = goodsInfoJson.getString("goodsIntro");
                            if (StringUtils.isBlank(goodsIntro)) {
                                goodsIntro = "";
                            }

                            Integer goodsType = (Integer) goodsInfoJson.get("goodsType");
                            goodsOutBean.setGoodsType(goodsType);
                            goodsOutBean.setMainImg(mainImg);
                            goodsOutBean.setGoodsIntro(goodsIntro);
                        }

                        //获取cardInfo
                        JSONObject cardInfoJson = jsonObject.getJSONObject("cardInfo");
                        if (null != cardInfoJson) {
                            orderOutBean.setCardInfo(cardInfoJson);
                        }
                    }
                }

                goodsOutBeanList.add(goodsOutBean);

            }
            //把订单商品数据 添加到订单对象中
            orderOutBean.setGoodsList(goodsOutBeanList);
        }

        return orderOutBean;
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
     * 查询用户订单关联的商品数据
     * @param mb_id
     * @param orderId
     * @return
     */
    private List<MallOrderGoods> queryOrderGoodsWithMember(String mb_id, String orderId, String orderType) {

        EntityWrapper<MallOrderGoods> orderGoodsEntityWrapper = new EntityWrapper<MallOrderGoods>();
        orderGoodsEntityWrapper.eq("mb_id", mb_id).eq("order_id", orderId);
        orderGoodsEntityWrapper.eq("goods_type", orderType);
        return mallOrderGoodsDaoService.selectList(orderGoodsEntityWrapper);
    }


    /**
     * 查询用户已经成功交易的订单数据
     *  订单状态：5 成功交易
     *  支付状态：2 已经付款
     * @param mb_id
     * @return
     */
    private List<MallOrder> queryTradeSuccessOrderrWithMember(String mb_id) {
        EntityWrapper<MallOrder> orderEntityWrapper = new EntityWrapper<MallOrder>();
        orderEntityWrapper.eq("mb_id", mb_id).eq("order_status", 5).eq("pay_status", 2).orderBy("create_time", false);
        return mallOrderDaoService.selectList(orderEntityWrapper);
    }

    /**
     * 查询用户已经成功交易的一条订单数据
     *  订单状态：5 成功交易
     *  支付状态：2 已经付款
     *  最后一个成功的订单数据
     * @param mb_id
     * @return
     */
    private MallOrder queryTradeSuccessSingleOrderrWithMember(String mb_id) {
        EntityWrapper<MallOrder> orderEntityWrapper = new EntityWrapper<MallOrder>();
        orderEntityWrapper.eq("mb_id", mb_id).eq("order_status", 5).eq("pay_status", 2).eq("shipping_status", 5).
                orderBy("create_time", false).last("limit 1");
        return mallOrderDaoService.selectOne(orderEntityWrapper);
    }


    /**
     * 验证当前日期 是否是秒杀活动的日期
     * @param currentDate
     * @return
     */
    private boolean isSeckillDate(String currentDate) {

        if (StringUtils.isBlank(currentDate)) {
            return false;
        }

        for (String date : FungoMallSeckillConsts.SECKILL_DATE) {
            if (StringUtils.equalsIgnoreCase(currentDate, date)) {
                return true;
            }
        }
        return false;
    }


    /**
     *  验证是否到秒杀时间段
     * @param grantCode 秒杀授权码
     * @return
     */
    private boolean isStartSeckill(String grantCode) {
        boolean isStartSeckill = false;
        String currengDate = DateTools.getCurrentDate("-");

        //获取当前日期的秒杀授权码
        Map<String, String> seckillCodeMap = FungoMallSeckillTaskStateCommand.getInstance().getSeckillCodeMap();
        String existCode = seckillCodeMap.get(currengDate);

        for (String date : FungoMallSeckillConsts.SECKILL_DATE) {
            if (StringUtils.equalsIgnoreCase(currengDate, date) &&
                    StringUtils.equalsIgnoreCase(grantCode, existCode)) {
                isStartSeckill = true;
                break;
            }
        }
        return isStartSeckill;
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

        return mallSeckill;
    }


    /**
     * 验证用户是否有足够的fungo币可用余额
     * @param mb_id
     * @return
     */
    private IncentAccountCoin isFullMbFungo(String mb_id) {

        EntityWrapper<IncentAccountCoin> mbAccountCoinEntityWrapper = new EntityWrapper<IncentAccountCoin>();

        Map<String, Object> criteriaMap = new HashMap<String, Object>();
        criteriaMap.put("mb_id", mb_id);
        mbAccountCoinEntityWrapper.allEq(criteriaMap);

        IncentAccountCoin incentAccountCoinDB = incentAccountCoinService.selectOne(mbAccountCoinEntityWrapper);

        return incentAccountCoinDB;
    }


    /**
     * 冻结用户账户对应的商品价格额
     * -1 可用fungo币不足
     * 1 冻结成功
     * @param mb_id
     * @param incentAccountCoin
     * @param goodsPriceVcyBD
     * @return
     */
    private int freezeMemberFungo(String mb_id, IncentAccountCoin incentAccountCoin, BigDecimal goodsPriceVcyBD) {

        logger.info("冻结用户账户对应的商品价格额-冻结前账户详情:{}", JSON.toJSONString(incentAccountCoin));
        //可用币量
        BigDecimal lastCoinUsable = incentAccountCoin.getCoinUsable();

        //可用fungo币 减去 商品价格fungo币 且大于等于0
        BigDecimal residueUsableNew = lastCoinUsable.subtract(goodsPriceVcyBD);

        //减去商品价格fungo币后 可用币量不能小于0
        int isMoreZero = residueUsableNew.compareTo(BigDecimal.ZERO);
        if (-1 == isMoreZero) {
            return -1;
        }

        Long lastCasVersion = incentAccountCoin.getCasVersion();
        //初始化
        if (null == lastCasVersion) {
            lastCasVersion = 0L;
        }

        //账户已冻结fungo币
        BigDecimal lastCoinFreeze = incentAccountCoin.getCoinFreeze();
        if (null == lastCoinFreeze) {
            lastCoinFreeze = BigDecimal.ZERO;
        }

        //已经冻结funbo币  + 商品价格fungo币 = 冻结总量
        BigDecimal newCoinFreeze = lastCoinFreeze.add(goodsPriceVcyBD);


        IncentAccountCoin incentAccountCoinNew = new IncentAccountCoin();
        //设置新的cas版本
        incentAccountCoinNew.setCasVersion(lastCasVersion + 1L);
        //设置新的可用 fungo币账户
        incentAccountCoinNew.setCoinUsable(residueUsableNew);
        //设置新的冻结fungo币账户
        incentAccountCoinNew.setCoinFreeze(newCoinFreeze);
        incentAccountCoinNew.setUpdatedAt(new Date());

        logger.info("冻结用户账户对应的商品价格额-冻结计算后账户详情:{}", JSON.toJSONString(incentAccountCoinNew));

        Map<String, Object> criteriaMap = new HashMap<String, Object>();
        criteriaMap.put("mb_id", mb_id);
        criteriaMap.put("cas_version", lastCasVersion);

        EntityWrapper<IncentAccountCoin> mbAccountCoinEntityWrapper = new EntityWrapper<IncentAccountCoin>();
        //再次验证 账户可用币量 大于等于 商品价格fungo币
        mbAccountCoinEntityWrapper.allEq(criteriaMap).ge("coin_usable", goodsPriceVcyBD);

        boolean update = incentAccountCoinService.update(incentAccountCoinNew, mbAccountCoinEntityWrapper);

        logger.info("冻结用户可用币量,mb_id:{}--执行结果：{}", mb_id, update);
        if (update) {
            return 1;
        }
        return -1;
    }


    /**
     * 验证当前用户在当天，是否重复秒杀了同一件商品
     * @param mb_id
     * @param goods_id
     * @return 未购买 false ，已购买true
     */
    private boolean isReBuy(String mb_id, String goods_id) {
        boolean isReBuy = false;

        Map<String, Object> criteriaMap = new HashMap<String, Object>();
        criteriaMap.put("mb_id", mb_id);
        criteriaMap.put("goods_id", goods_id);


        String currengDate = DateTools.fmtSimpleDateToString(new Date());
        String queryStartDate = currengDate + " " + FungoMallSeckillConsts.SECKILL_START_TIME;
        String queryEndDate = currengDate + " " + FungoMallSeckillConsts.SECKILL_END_TIME;

        EntityWrapper<MallOrderGoods> orderGoodsEntityWrapper = new EntityWrapper<MallOrderGoods>();

        orderGoodsEntityWrapper.allEq(criteriaMap);
        //orderGoodsEntityWrapper.like("created_at", currengDate);

        orderGoodsEntityWrapper.ge("created_at", queryStartDate);
        orderGoodsEntityWrapper.le("created_at", queryEndDate);

        List<MallOrderGoods> orderGoodsList = mallOrderGoodsDaoService.selectList(orderGoodsEntityWrapper);

        logger.info("验证当前用户在当天，是否重复秒杀了同一件商品--orderGoodsList:{}", orderGoodsList);

        if (null != orderGoodsList && !orderGoodsList.isEmpty()) {

            Set<Long> orderIds = new HashSet<>();

            for (MallOrderGoods orderGoods : orderGoodsList) {
                Long orderId = orderGoods.getOrderId();
                orderIds.add(orderId);
            }

            EntityWrapper<MallOrder> orderEntityWrapper = new EntityWrapper<MallOrder>();
            orderEntityWrapper.eq("mb_id", mb_id);
            orderEntityWrapper.in("id", orderIds);

            List<MallOrder> orderList = mallOrderDaoService.selectList(orderEntityWrapper);

            logger.info("验证当前用户在当天，是否重复秒杀了同一件商品--orderIds:{}", orderIds.toArray().toString());


            if (null != orderList && !orderList.isEmpty()) {
                for (MallOrder order : orderList) {
                    Integer payStatus = order.getPayStatus();
                    Integer orderStatus = order.getOrderStatus();
                    if ((2 == payStatus.intValue() && 5 == orderStatus.intValue()) || 1 == orderStatus.intValue()) {
                        isReBuy = true;
                        break;
                    }
                }

                logger.info("验证当前用户在当天，是否重复秒杀了同一件商品---日期:{}----用户id:{}---商品id:{}---已购买数量:{}",
                        currengDate, mb_id, goods_id, orderList.size());
            }
        }

        return isReBuy;
    }


    /**
     *  添加订单和订单商品表
     * @param mb_id
     * @param goods_id
     * @param mallSeckill
     */
    public MallOrder addSeckillOrder(String mb_id, String goods_id, MallSeckill mallSeckill) {

        //1 查询出商品详情
        EntityWrapper<MallGoods> goodsEntityWrapper = new EntityWrapper<MallGoods>();
        goodsEntityWrapper.eq("id", goods_id);
        MallGoods goods = mallGoodsDaoService.selectOne(goodsEntityWrapper);

        if (null == goods) {
            throw new BusinessException("-1", "秒杀生成订单过程中，查询商品和秒杀商品数据出现异常");
        }


        //2 从秒杀商品信息中 解析商品秒杀价格
        String seckillPriceVcy = mallSeckill.getSeckillPriceVcy();
        //解密 商品秒杀价格
        String seckillPriceVcyDecrypt = FungoAESUtil.decrypt(seckillPriceVcy, aESSecretKey + FungoMallSeckillConsts.AES_SALT);

        long seckillPriceVcy_i = 0;
        if (StringUtils.isNoneBlank(seckillPriceVcyDecrypt)) {
            seckillPriceVcy_i = Integer.parseInt(seckillPriceVcyDecrypt);
        }

        //商品价格设置为秒杀价格
        goods.setMarketPriceVcy(seckillPriceVcy_i);

        //查询出当前登录会员详情
        EntityWrapper<Member> memberEntityWrapper = new EntityWrapper<Member>();
        memberEntityWrapper.eq("id", mb_id);
        Member member = memberService.selectOne(memberEntityWrapper);

        //创建订单
        MallOrder order = new MallOrder();

        int clusterIndex_i = Integer.parseInt(clusterIndex);
        String orderSN = "";
        /*
        订单编号
            实物商品 以 R开头
            虚拟物品 以 V开头
                1 实物
                2 虚拟物品
                           21 零卡
                           22 京东卡
                           23 QB卡
        */
        Integer goodsType = goods.getGoodsType();
        switch (goodsType.intValue()) {
            case 1:
                orderSN = "R" + String.valueOf(PKUtil.getInstance(clusterIndex_i).longPK());
                break;
            case 21:
                orderSN = "V" + String.valueOf(PKUtil.getInstance(clusterIndex_i).longPK());
                break;

            case 22:
                orderSN = "V" + String.valueOf(PKUtil.getInstance(clusterIndex_i).longPK());
                break;

            case 23:
                orderSN = "V" + String.valueOf(PKUtil.getInstance(clusterIndex_i).longPK());
                break;
            default:
                break;
        }

        order.setId(PKUtil.getInstance(clusterIndex_i).longPK());
        order.setOrderSn(orderSN);
        order.setMbId(mb_id);

        if (null != member) {
            order.setMbName(member.getUserName());
            order.setMbMobile(member.getMobilePhoneNum());
        }

        //订单状态 1 已确认
        order.setOrderStatus(1);
        //支付状态 3 已冻结余额
        order.setPayStatus(3);
        //发货状态 -1 未发货
        order.setShippingStatus(-1);
        order.setGoodsAmountVcy(seckillPriceVcy_i);

        Date currentDateTime = new Date();
        order.setCreateTime(currentDateTime);

        boolean orderInsertOk = mallOrderDaoService.insert(order);
        logger.info("秒杀下单，订单添加结果状态:{}--orderDetail:{}", orderInsertOk, JSON.toJSONString(order));

        //添加订单商品关系表
        MallOrderGoods orderGoods = new MallOrderGoods();
        orderGoods.setId(PKUtil.getInstance(clusterIndex_i).longPK());
        orderGoods.setMbId(mb_id);
        orderGoods.setOrderId(order.getId());
        orderGoods.setGoodsId(goods.getId());
        orderGoods.setGoodsName(goods.getGoodsName());
        //购买 商品数量
        orderGoods.setGoodsNumber(1L);
        orderGoods.setGoodsPriceVcy(seckillPriceVcy_i);

        //若商品有虚拟卡信息，保存商品本身信息和兑换卡信息 {"goodsInfo":"" , "cardInfo:"}
        Map<String, Object> goodsAttMap = new HashMap<String, Object>();
        goodsAttMap.put("goodsInfo", JSON.toJSONString(goods));
        goodsAttMap.put("cardInfo", "");

        orderGoods.setGoodsAtt(JSON.toJSONString(goodsAttMap));

        orderGoods.setGoodsType(goods.getGoodsType());
        orderGoods.setCreatedAt(currentDateTime);
        orderGoods.setUpdatedAt(currentDateTime);

        boolean orderGoodsInsertOK = mallOrderGoodsDaoService.insert(orderGoods);
        logger.info("秒杀下单，订单商品关系表添加结果状态:{}--orderDetail:{}", orderGoodsInsertOK, JSON.toJSONString(orderGoods));

        return order;
    }


    /**
     *  添加订单和订单商品表
     * @param mb_id
     * @param goods
     */
    public MallOrder addSeckillOrderWithGame(String mb_id, MallGoods goods) {


        //查询出当前登录会员详情
        EntityWrapper<Member> memberEntityWrapper = new EntityWrapper<Member>();
        memberEntityWrapper.eq("id", mb_id);
        Member member = memberService.selectOne(memberEntityWrapper);

        //创建订单
        MallOrder order = new MallOrder();

        int clusterIndex_i = Integer.parseInt(clusterIndex);
        String orderSN = "";
        /*
        订单编号
            实物商品 以 R开头
            虚拟物品 以 V开头
                1 实物
                2 虚拟物品
                           21 零卡
                           22 京东卡
                           23 QB卡
                3 游戏礼包

        */
        Integer goodsType = goods.getGoodsType();
        switch (goodsType.intValue()) {
            case 1:
                orderSN = "R" + String.valueOf(PKUtil.getInstance(clusterIndex_i).longPK());
                break;
            case 3:
                orderSN = "G" + String.valueOf(PKUtil.getInstance(clusterIndex_i).longPK());
                break;
            case 21:
                orderSN = "V" + String.valueOf(PKUtil.getInstance(clusterIndex_i).longPK());
                break;

            case 22:
                orderSN = "V" + String.valueOf(PKUtil.getInstance(clusterIndex_i).longPK());
                break;

            case 23:
                orderSN = "V" + String.valueOf(PKUtil.getInstance(clusterIndex_i).longPK());
                break;
            default:
                break;
        }

        order.setId(PKUtil.getInstance(clusterIndex_i).longPK());
        order.setOrderSn(orderSN);
        order.setMbId(mb_id);

        if (null != member) {
            order.setMbName(member.getUserName());
            order.setMbMobile(member.getMobilePhoneNum());
        }

        //订单状态 1 已确认
        order.setOrderStatus(1);
        //支付状态 3 已冻结余额
        order.setPayStatus(3);
        //发货状态 -1 未发货
        order.setShippingStatus(-1);
        order.setGoodsAmountVcy(goods.getMarketPriceVcy());

        Date currentDateTime = new Date();
        order.setCreateTime(currentDateTime);

        boolean orderInsertOk = mallOrderDaoService.insert(order);
        logger.info("秒杀下单，订单添加结果状态:{}--orderDetail:{}", orderInsertOk, JSON.toJSONString(order));

        //添加订单商品关系表
        MallOrderGoods orderGoods = new MallOrderGoods();
        orderGoods.setId(PKUtil.getInstance(clusterIndex_i).longPK());
        orderGoods.setMbId(mb_id);
        orderGoods.setOrderId(order.getId());
        orderGoods.setGoodsId(goods.getId());
        orderGoods.setGoodsName(goods.getGoodsName());
        //购买 商品数量
        orderGoods.setGoodsNumber(1L);
        orderGoods.setGoodsPriceVcy(goods.getMarketPriceVcy());

        //若商品有虚拟卡信息，保存商品本身信息和兑换卡信息 {"goodsInfo":"" , "cardInfo:"}
        Map<String, Object> goodsAttMap = new HashMap<String, Object>();
        goodsAttMap.put("goodsInfo", JSON.toJSONString(goods));
        goodsAttMap.put("cardInfo", "");

        orderGoods.setGoodsAtt(JSON.toJSONString(goodsAttMap));

        orderGoods.setGoodsType(goods.getGoodsType());
        orderGoods.setCreatedAt(currentDateTime);
        orderGoods.setUpdatedAt(currentDateTime);

        //扩展字段1   保存用户名称
        orderGoods.setExt1(member.getUserName());
        //扩展字段2  保存用户手机号
        orderGoods.setExt2(member.getMobilePhoneNum());

        boolean orderGoodsInsertOK = mallOrderGoodsDaoService.insert(orderGoods);
        logger.info("秒杀下单，订单商品关系表添加结果状态:{}--orderDetail:{}", orderGoodsInsertOK, JSON.toJSONString(orderGoods));

        return order;
    }


    //线程池
    private ExecutorService fixedThreadPoolMallLog = Executors.newFixedThreadPool(8);


    /**
     * 记录浏览商品页面日志
     */
    private void addMallLogs(String mb_id, String userName, Long goodsId, String realIp, Integer actionType) {
        fixedThreadPoolMallLog.execute(new Runnable() {
            @Override
            public void run() {

                MallLogsDto logsDto = new MallLogsDto();

                int clusterIndex_i = Integer.parseInt(clusterIndex);
                logsDto.setId(PKUtil.getInstance(clusterIndex_i).longPK());
                logsDto.setMbId(mb_id);
                logsDto.setPageUrl("/shopmall.html");
                logsDto.setIUrl("/api/mall/goods/seckill/list");
                logsDto.setGoodsId(goodsId);
                logsDto.setVisitIp(realIp);
                logsDto.setActionType(actionType);
                logsDto.setCreatorName(userName);

                Date currentDatte = new Date();
                logsDto.setCreatedAt(currentDatte);
                logsDto.setUpdatedAt(currentDatte);

                iMallLogsService.addMallLog(logsDto);
            }
        });
    }


    /**
     * 清除缓存的用户订单数据
     * //"'" + FunGoGameConsts.CACHE_EH_KEY_PRE_MEMBER + "_CoinAccount' + #memberId"
     */
    private void removeCacheWithOrders(String mb_id, String orderId) {
        String cacheKey = FungoMallSeckillConsts.CACHE_KEY_MALL_SECKILL_ORDER + mb_id + orderId;
        logger.info("清除缓存的用户订单数据-缓存key:{}", cacheKey);
        FunGoEHCacheUtils.remove(FunGoGameConsts.CACHE_EH_NAME, cacheKey);
        //清除总数  //  "'" + FunGoGameConsts.CACHE_EH_KEY_PRE_MEMBER + "_CoinAccount' + #memberId"
        String memberIdCacheKey = FunGoGameConsts.CACHE_EH_KEY_PRE_MEMBER + "_CoinAccount" + mb_id;
        FunGoEHCacheUtils.remove(FunGoGameConsts.CACHE_EH_NAME, memberIdCacheKey);

    }


    /**
     * 验证用户是否购买过游戏礼包
     * @param mb_id
     * @param goods_id
     * @return
     */
    private boolean isBuyedVMCardValidWithGame(String mb_id, Long goods_id) {

        //查询虚拟卡验证用户是否购买过
        EntityWrapper<MallVirtualCard> virtualCardEntityWrapper = new EntityWrapper<MallVirtualCard>();
        virtualCardEntityWrapper.eq("goods_id", goods_id);
        virtualCardEntityWrapper.eq("mb_id", mb_id);
        virtualCardEntityWrapper.eq("card_type", 3);
        virtualCardEntityWrapper.eq("is_saled", 1);

        int count = mallVirtualCardDaoService.selectCount(virtualCardEntityWrapper);
        return count > 0 ? true : false;
    }


    /**
     * 查询某个游戏礼包未卖出剩余的虚拟卡数量
     * @param mb_id
     * @param goods_id
     * @return
     */
    private int getUnSaledVMCardWithGame(String mb_id, Long goods_id) {

        //查询虚拟卡验证用户是否购买过
        EntityWrapper<MallVirtualCard> virtualCardEntityWrapper = new EntityWrapper<MallVirtualCard>();
        virtualCardEntityWrapper.eq("goods_id", goods_id);
        virtualCardEntityWrapper.eq("card_type", 31);
        virtualCardEntityWrapper.eq("is_saled", -1);

        int count = mallVirtualCardDaoService.selectCount(virtualCardEntityWrapper);
        return count;
    }


    //---------

}
