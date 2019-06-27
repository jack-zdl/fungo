package com.fungo.system.mall.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.mall.daoService.MallGoodsCatesDaoService;
import com.fungo.system.mall.daoService.MallGoodsDaoService;
import com.fungo.system.mall.daoService.MallSeckillDaoService;
import com.fungo.system.mall.entity.MallGoods;
import com.fungo.system.mall.entity.MallGoodsCates;
import com.fungo.system.mall.entity.MallSeckill;
import com.fungo.system.mall.service.IFungoMallGoodsService;
import com.fungo.system.mall.service.consts.FungoMallSeckillConsts;
import com.game.common.dto.ResultDto;
import com.game.common.dto.mall.MallGoodsInput;
import com.game.common.util.FungoAESUtil;
import com.game.common.util.PKUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FungoMallGoodsServiceImpl implements IFungoMallGoodsService {

    private static final Logger logger = LoggerFactory.getLogger(FungoMallGoodsServiceImpl.class);


    @Autowired
    private MallGoodsCatesDaoService mallGoodsCatesDaoService;

    @Autowired
    private MallGoodsDaoService mallGoodsDaoService;

    @Autowired
    private MallSeckillDaoService mallSeckillDaoServicel;

    @Value("${fungo.mall.seckill.aesSecretKey}")
    private String aESSecretKey;


    private void addGoodsCates() {
        //分类
        MallGoodsCates goodsCates = new MallGoodsCates();

        goodsCates.setId(PKUtil.getInstance().longPK());
        goodsCates.setPid(-1L);
        goodsCates.setCatName("实物礼品");
        goodsCates.setStatus(1);
        goodsCates.setSort(0);

        goodsCates.setCreatedAt(new Date());
        goodsCates.setUpdatedAt(new Date());

        mallGoodsCatesDaoService.insert(goodsCates);
    }


    /**
     * 1 实物
     * 2 虚拟物品
     *    21 零卡
     *    22 京东卡
     *    23 QB卡
     */
    @Override
    public boolean addGoodsAndSeckill(Map<String, Object> param) {

        try {

            //虚拟物品类型id: 2019011415405395215L
            //实物物品类型id: 2019011415434340219
            //Long cateId = (Long) param.get("cateId");

            Long goodsId = (Long) param.get("goodsId");
            //商品名称
            String goodsName = (String) param.get("goodsName");

            Integer price = (Integer) param.get("price");
            //库存
            String stock = (String) param.get("stock");

            //序号
            //int sort = (Integer) param.get("sort");
            //商品类型
            // int goodsType = (Integer) param.get("goodsType");

            //开始时间
            String startDate = (String) param.get("startDate");
            //结束时间
            String endDate = (String) param.get("endDate");
            //商品图片
            //String imgs = (String) param.get("imgs");


            //addGoods(cateId,goodsName, imgs, price, goodsType, sort);
            //addGoodsSeckill(goodsId,goodsName,price,stock,startDate,endDate);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }


    //添加秒杀的商品
    public void addGoodsSeckill(MallSeckill seckillParm) {

        try {

            Long goodsId = seckillParm.getGoodsId();

            String goodsName = seckillParm.getGoodsName();

            String MarketPriceVcy = seckillParm.getSeckillPriceVcy();

            String stock = seckillParm.getTotalStock();

            String startDate = seckillParm.getExt1();

            String endDate = seckillParm.getExt2();


            //秒杀表
            MallSeckill seckill = new MallSeckill();
            seckill.setId(PKUtil.getInstance().longPK());
            seckill.setGoodsId(goodsId);

            seckill.setGoodsName(goodsName);

            //价格加密保存
            String seckillPriceVcyEncrypt = FungoAESUtil.encrypt(String.valueOf(MarketPriceVcy),
                    aESSecretKey + FungoMallSeckillConsts.AES_SALT);
            seckill.setSeckillPriceVcy(seckillPriceVcyEncrypt);

            //总库存加密保存
            String totalStockEncrypt = FungoAESUtil.encrypt(stock, aESSecretKey + FungoMallSeckillConsts.AES_SALT);
            seckill.setTotalStock(totalStockEncrypt);


            //剩余库存加密保存
            String residueStockEncrypt = FungoAESUtil.encrypt(stock, aESSecretKey + FungoMallSeckillConsts.AES_SALT);
            seckill.setResidueStock(residueStockEncrypt);


            String queryStartDate = startDate + " 00:00:00";
            String queryEndDate = endDate + " 23:59:59";

            seckill.setStartTime(DateUtils.parseDate(queryStartDate, "yyyy-MM-dd HH:mm:ss"));
            seckill.setEndTime(DateUtils.parseDate(queryEndDate, "yyyy-MM-dd HH:mm:ss"));

            seckill.setCreatedAt(new Date());
            seckill.setUpdatedAt(new Date());

            mallSeckillDaoServicel.insert(seckill);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //添加商品
    public void addGoods() {

        //实物物品类型id: 2019011415434340219

        Long cateId = 2019011415434340219L;
        String goodsName = "三只松鼠巨型零食礼盒2217g";
        Long price = 19800L;

        List<Map<String, Object>> imgsList = new ArrayList<>();
        Map<String, Object> imgMapBig = new HashMap<>();
        imgMapBig.put("url", "http://output-mingbo.oss-cn-beijing.aliyuncs.com/mall/goods/imgs/lbd.png");
        imgMapBig.put("status", 1);
        imgMapBig.put("size", 1);
        imgMapBig.put("style", 1);

        Map<String, Object> imgMapSmall = new HashMap<>();
        imgMapSmall.put("url", "http://output-mingbo.oss-cn-beijing.aliyuncs.com/mall/goods/imgs/lbs.png");
        imgMapSmall.put("status", 1);
        imgMapSmall.put("size", 3);
        imgMapSmall.put("style", 1);

        imgsList.add(imgMapBig);
        imgsList.add(imgMapSmall);


        //----------
        MallGoods mallGoods = new MallGoods();

        Long goodsId = PKUtil.getInstance().longPK();

        mallGoods.setId(goodsId);

        mallGoods.setCid(cateId);
        mallGoods.setGoodsName(goodsName);
        mallGoods.setMainImg(JSON.toJSONString(imgsList));


        mallGoods.setMarketPriceVcy(price);

        mallGoods.setGoodsStatus(2);
        mallGoods.setGoodsType(1);
        mallGoods.setSort(4);

        mallGoods.setCreatedAt(new Date());
        mallGoods.setUpdatedAt(new Date());

        mallGoodsDaoService.insert(mallGoods);

    }


    @Override
    public ResultDto<Map<String, Object>> queryGoodsCountWithGame(MallGoodsInput mallGoodsInput) {

        if (null == mallGoodsInput) {
            return null;
        }

        String gameId = mallGoodsInput.getGameId();
        if (StringUtils.isBlank(gameId)) {
            return null;
        }

        try {

            EntityWrapper<MallGoods> mallGoodsEntityWrapper = new EntityWrapper<MallGoods>();
            mallGoodsEntityWrapper.eq("game_id", gameId);
            /**
             * 商品类型
             * 1 实物
             * 2 虚拟物品
             *    21 零卡
             *    22 京东卡
             *    23 QB卡
             * 3 游戏礼包
             */
            mallGoodsEntityWrapper.eq("goods_type", 3);

            int count = mallGoodsDaoService.selectCount(mallGoodsEntityWrapper);

            ResultDto<Map<String, Object>> resultDto = new ResultDto<Map<String, Object>>();
            Map<String, Object> dataMap = new HashMap<String, Object>();
            resultDto.setData(dataMap);

            dataMap.put("goodsCount", count);

            return resultDto;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    //---------
}