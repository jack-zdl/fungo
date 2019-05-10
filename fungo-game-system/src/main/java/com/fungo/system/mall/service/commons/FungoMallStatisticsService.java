package com.fungo.system.mall.service.commons;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.mall.daoService.*;
import com.fungo.system.mall.entity.MallGoods;
import com.fungo.system.mall.entity.MallLogs;
import com.fungo.system.mall.entity.MallSeckill;
import com.fungo.system.mall.mapper.MallOrderGoodsDao;
import com.fungo.system.mall.service.consts.FungoMallSeckillConsts;
import com.game.common.util.CSVUtils;
import com.game.common.util.FungoAESUtil;
import com.game.common.util.date.DateTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *    fungo商城-秒杀业务统计分析业务类
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
@Component
public class FungoMallStatisticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FungoMallStatisticsService.class);


    @Autowired
    private MallSeckillDaoService mallSeckillDaoService;

    @Autowired
    private MallOrderDaoService mallOrderDaoService;

    @Autowired
    private MallOrderGoodsDaoService mallOrderGoodsDaoService;

    @Autowired
    private MallLogsDaoService mallLogsDaoService;

    @Autowired
    private MallOrderGoodsDao mallOrderGoodsDao;

    @Autowired
    private MallGoodsDaoService mallGoodsDaoService;

    @Value("${fungo.mall.seckill.aesSecretKey}")
    private String aESSecretKey;


    /**
     * 3.每日每个礼品的兑换次数；
     *
     *    指每个礼品点击立即兑换的次数（无论是否兑换成功，需要统计点击立即兑换的次数）
     */
    public void exportSeckillGoogsClickCount () {

        EntityWrapper<MallSeckill> mallSeckillEntityWrapper = new EntityWrapper<MallSeckill>();
        mallSeckillEntityWrapper.orderBy("start_time", false);
        List<MallSeckill> mallSeckillList = mallSeckillDaoService.selectList(mallSeckillEntityWrapper);


        List<MallSeckill> goodsClickCountList = new ArrayList<MallSeckill>();

        for (MallSeckill mallSeckill : mallSeckillList) {

            //秒杀日期
            Date startTime = mallSeckill.getStartTime();
            String seckillDate = DateTools.fmtSimpleDateToString(startTime);

            //商品id
            Long goodsId = mallSeckill.getGoodsId();

            //商品名称
            String goodsName = mallSeckill.getGoodsName();

            //查询该商品的点击次数
            EntityWrapper<MallLogs> mallLogsEntityWrapper = new EntityWrapper<MallLogs>();
            //当天秒杀时间
            mallLogsEntityWrapper.like("created_at", seckillDate);
            //日志类型: 2 点击商品
            mallLogsEntityWrapper.eq("action_type", 2);
            //当天秒杀的商品id
            mallLogsEntityWrapper.eq("goods_id", goodsId);

            //查询该商品点击次数
            int clickCount = mallLogsDaoService.selectCount(mallLogsEntityWrapper);

            //记录每天的秒杀商品，被点击的次数
            MallSeckill mallSeckillClick  = new MallSeckill();
            mallSeckillClick.setExt1(seckillDate);
            mallSeckillClick.setExt2(goodsName);
            mallSeckillClick.setExt3( String.valueOf(clickCount));
            goodsClickCountList.add(mallSeckillClick);

        }

        File exportFile = new File("E:/temps/SeckillGoogsClickCount.csv");
        List<String> exclDataList=new ArrayList<>();
        for (MallSeckill mallSeckill : goodsClickCountList) {
            String excelData =  new StringBuffer().append(mallSeckill.getExt1()).append(",")
                    .append(mallSeckill.getExt2()).append(",")
                    .append(mallSeckill.getExt3()).toString();
            exclDataList.add(excelData);
        }
        CSVUtils.exportCsv(exportFile,exclDataList);
    }



    /**
     * 4.每天每种礼品兑换成功的数量；整个活动期间兑换成功的数量；
     *
     *   需记录，时间、礼品名称、成功数
     */
    public void exportSeckillSuccessGoogsCount() {

        EntityWrapper<MallSeckill> mallSeckillEntityWrapper = new EntityWrapper<MallSeckill>();
        mallSeckillEntityWrapper.orderBy("start_time", false);
        List<MallSeckill> mallSeckillList = mallSeckillDaoService.selectList(mallSeckillEntityWrapper);


        List<MallSeckill> goodsClickCountList = new ArrayList<MallSeckill>();

        for (MallSeckill mallSeckill : mallSeckillList) {

            //秒杀日期
            Date startTime = mallSeckill.getStartTime();
            String seckillDate = DateTools.fmtSimpleDateToString(startTime);

            //商品id
            Long goodsId = mallSeckill.getGoodsId();

            //商品名称
            String goodsName = mallSeckill.getGoodsName();


            //查询订单商品表，秒杀日、某个商品被秒杀成功的数量

            Map map = mallOrderGoodsDao.querySeckillGoodsTradeCount("%" + seckillDate + "%", String.valueOf(goodsId));

            Long tradeCount = (Long) map.get("ct");

            //记录每天的秒杀商品，交易成功的数量
            MallSeckill mallSeckillClick  = new MallSeckill();
            mallSeckillClick.setExt1(seckillDate);
            mallSeckillClick.setExt2(goodsName);
            mallSeckillClick.setExt3( String.valueOf(tradeCount));
            goodsClickCountList.add(mallSeckillClick);
        }

        File exportFile = new File("E:/temps/SeckillGoogsTradeCount.csv");
        List<String> exclDataList=new ArrayList<>();
        for (MallSeckill mallSeckill : goodsClickCountList) {
            String excelData =  new StringBuffer().append(mallSeckill.getExt1()).append(",")
                    .append(mallSeckill.getExt2()).append(",")
                    .append(mallSeckill.getExt3()).toString();
            exclDataList.add(excelData);
        }
        CSVUtils.exportCsv(exportFile,exclDataList);

    }


    /**
     * 5.每天兑换每种礼品消耗的Fun币数量；整个活动期间兑消耗Fun币数量；
     *
     *   记录，时间、礼品名称、单价、数量、合计消耗Fun币数量
     */
    public void exportGoodsTradedFungoCoinSum () {

        EntityWrapper<MallSeckill> mallSeckillEntityWrapper = new EntityWrapper<MallSeckill>();
        mallSeckillEntityWrapper.orderBy("start_time", false);
        List<MallSeckill> mallSeckillList = mallSeckillDaoService.selectList(mallSeckillEntityWrapper);


        List<MallSeckill> goodsClickCountList = new ArrayList<MallSeckill>();

        for (MallSeckill mallSeckill : mallSeckillList) {

            //秒杀日期
            Date startTime = mallSeckill.getStartTime();
            String seckillDate = DateTools.fmtSimpleDateToString(startTime);

            //商品id
            Long goodsId = mallSeckill.getGoodsId();

            //商品名称
            String goodsName = mallSeckill.getGoodsName();

            //单价
            String seckillPriceVcy = mallSeckill.getSeckillPriceVcy();
            //解密
            seckillPriceVcy = FungoAESUtil.decrypt(seckillPriceVcy,
                    aESSecretKey + FungoMallSeckillConsts.AES_SALT);


            //交易成功 数量
            //查询订单商品表，秒杀日、某个商品被秒杀成功的数量
            Map map = mallOrderGoodsDao.querySeckillGoodsTradeCount("%" + seckillDate + "%", String.valueOf(goodsId));
            Long tradeCount = (Long) map.get("ct");

            //合计消耗Fun币数量
            Map fungoCoinSumMap = mallOrderGoodsDao.querySeckillGoodsTradeFungoCoinSum("%" + seckillDate + "%", String.valueOf(goodsId));

            //记录每天的秒杀商品，交易成功的数量
            MallSeckill mallSeckillClick  = new MallSeckill();
            mallSeckillClick.setExt1(seckillDate);
            mallSeckillClick.setExt2(goodsName);
            mallSeckillClick.setExt3(seckillPriceVcy);
            mallSeckillClick.setExt4( String.valueOf(tradeCount));

            if (null !=fungoCoinSumMap && !fungoCoinSumMap.isEmpty()) {
                BigDecimal fungoCoinSum = (BigDecimal) fungoCoinSumMap.get("amount");
                if (null != fungoCoinSum){
                    mallSeckillClick.setExt5(fungoCoinSum.toString());
                }else {
                    mallSeckillClick.setExt5("0");
                }
            }else {
                mallSeckillClick.setExt5("0");
            }

            goodsClickCountList.add(mallSeckillClick);
        }

        File exportFile = new File("E:/temps/SeckillGoogsTradeFungoCoinCount.csv");
        List<String> exclDataList =  new ArrayList<String>();
        for (MallSeckill mallSeckill : goodsClickCountList) {
            String excelData =  new StringBuffer()
                    .append(mallSeckill.getExt1()).append(",")
                    .append(mallSeckill.getExt2()).append(",")
                    .append(mallSeckill.getExt3()).append(",")
                    .append(mallSeckill.getExt4()).append(",")
                    .append(mallSeckill.getExt5()) .toString();
            exclDataList.add(excelData);
        }
        CSVUtils.exportCsv(exportFile,exclDataList);

    }


    /**
     * 8.虚拟奖品剩余的奖品信息
     *
     *   需记录，礼品名称、剩余数量、单个礼品名称、礼品兑换码
     */
    public void exportVirtualGoods() {


        //先查出虚拟商品
        EntityWrapper<MallGoods> goodsEntityWrapper = new EntityWrapper<MallGoods>();
        goodsEntityWrapper.in("goods_type", "21,22,23");

        List<MallGoods> goodsList = mallGoodsDaoService.selectList(goodsEntityWrapper);

        List<MallSeckill> goodsResudeList = new ArrayList<MallSeckill>();

        for (MallGoods goods : goodsList) {

            //商品id
            Long goodsId = goods.getId();

            //商品名称
            String goodsName = goods.getGoodsName();

            //从秒杀表中查询出 虚拟商品的 商品id，商品名称，秒杀总库存 ，剩余库存
            EntityWrapper<MallSeckill> mallSeckillEntityWrapper = new EntityWrapper<MallSeckill>();
            mallSeckillEntityWrapper.eq("goods_id", goodsId);

            List<MallSeckill> mallSeckills = mallSeckillDaoService.selectList(mallSeckillEntityWrapper);
            //计算该虚拟商品秒杀活动期间内总库存  总剩余库存
            long tatalStock = 0;
            long resideuStock = 0;
            for (MallSeckill seckillGoods : mallSeckills) {

                //秒杀总库存
                String totalStockEncy = seckillGoods.getTotalStock();
                String  total_stock_str = FungoAESUtil.decrypt(totalStockEncy,
                        aESSecretKey + FungoMallSeckillConsts.AES_SALT);
                tatalStock += Long.parseLong(total_stock_str);

                //剩余库存
                String residueStockDency = seckillGoods.getResidueStock();
                String  residue_stock_str = FungoAESUtil.decrypt(residueStockDency,
                        aESSecretKey + FungoMallSeckillConsts.AES_SALT);

                resideuStock +=  Long.parseLong(residue_stock_str);

            }


            MallSeckill  mallSeckill  = new MallSeckill();
            mallSeckill.setExt1(String.valueOf(goodsId));
            mallSeckill.setExt2(goodsName);
            mallSeckill.setExt3(String.valueOf(tatalStock));
            mallSeckill.setExt4(String.valueOf(resideuStock));

            goodsResudeList.add(mallSeckill);
        }


        File exportFile = new File("E:/temps/seckillGoodsresideuStock.csv");
        List<String> exclDataList =  new ArrayList<String>();
        for (MallSeckill mallSeckill : goodsResudeList) {
            String excelData =  new StringBuffer()
                    .append(mallSeckill.getExt1()).append(",")
                    .append(mallSeckill.getExt2()).append(",")
                    .append(mallSeckill.getExt3()).append(",")
                    .append(mallSeckill.getExt4()) .toString();
            exclDataList.add(excelData);
        }
        CSVUtils.exportCsv(exportFile,exclDataList);

    }



    //----------
}
