package com.fungo.system.mall.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.dto.FungoMallDto;
import com.fungo.system.feign.GamesFeignClient;
import com.fungo.system.helper.zookeeper.DistributedLockByCurator;
import com.fungo.system.mall.daoService.MallGoodsCatesDaoService;
import com.fungo.system.mall.daoService.MallGoodsDaoService;
import com.fungo.system.mall.daoService.MallSeckillDaoService;
import com.fungo.system.mall.entity.MallGoods;
import com.fungo.system.mall.entity.MallGoodsCates;
import com.fungo.system.mall.entity.MallSeckill;
import com.fungo.system.mall.mapper.MallSeckillDao;
import com.fungo.system.mall.service.IFungoMallGoodsService;
import com.fungo.system.mall.service.consts.FungoMallSeckillConsts;
import com.game.common.dto.ResultDto;
import com.game.common.dto.mall.MallGoodsInput;
import com.game.common.util.FungoAESUtil;
import com.game.common.util.PKUtil;
import com.game.common.util.date.DateTools;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private MallSeckillDao mallSeckillDao;



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
    @Transactional
    @Override
    public boolean addGoodsAndSeckill(FungoMallDto fungoMallDto) {

        try {

//            //虚拟物品类型id: 2019011415405395215L
//            //实物物品类型id: 2019011415434340219
//            //Long cateId = (Long) param.get("cateId");
//
//            Long goodsId = (Long) param.get("goodsId");
//            //商品名称
//            String goodsName = (String) param.get("goodsName");
//
//            Integer price = (Integer) param.get("price");
//            //库存
//            String stock = (String) param.get("stock");
//
//            //序号
//            //int sort = (Integer) param.get("sort");
//            //商品类型
//            // int goodsType = (Integer) param.get("goodsType");
//
//            //开始时间
//            String startDate = (String) param.get("startDate");
//            //结束时间
//            String endDate = (String) param.get("endDate");
//            //商品图片
//            //String imgs = (String) param.get("imgs");
//
//
//            addGoods(cateId,goodsName, imgs, price, goodsType, sort);
//            addGoodsSeckill(goodsId,goodsName,price,stock,startDate,endDate);
            //MallGoods mallGoods =  addGoods(fungoMallDto);
            //addGoodsSeckill(fungoMallDto,mallGoods.getId());
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    //添加秒杀的商品
    public void addGoodsSeckill(String goodId,String stocks) {

        try {

            Long goodsId = Long.valueOf(goodId);
            MallGoods mallGoods = mallGoodsDaoService.selectById(goodId);

            String goodsName = mallGoods.getGoodsName();  //fungoMallDto.getGoodsName();

            String MarketPriceVcy = mallGoods.getMarketPriceVcy().toString();  //String.valueOf(fungoMallDto.getPrice());

            String stock = stocks; //fungoMallDto.getStock();

            String startDate = DateTools.getCurrentDate( "-"); // fungoMallDto.getStartDate();

            String endDate =  DateTools.getCurrentDate( "-"); //;


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
            MallSeckill mallSeckill = mallSeckillDao.querySeckillGoodsByGoodId(goodId, queryStartDate,queryEndDate );
            if(mallSeckill == null){
                mallSeckillDaoServicel.insert(seckill);
            }else {
                logger.error( "新增每日库存失败,商品id="+ goodId);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    //添加商品
    public Object addGoods(FungoMallDto fungoMallDto) {

        //实物物品类型id: 2019011415434340219
        try{
            Map<String, Object> imgMapBig = new HashMap<>();
            Long cateId = 0l;
            switch (fungoMallDto.getType()){
                case 2:
                    cateId = 2019011415405395215L;
                    break;
                case 1:
                    cateId = 2019011415434340219L;
                    break;
                default:
                    break;
            }
            String goodsName = fungoMallDto.getGoodsName();
            Long price = fungoMallDto.getPrice();

            List<Map<String, Object>> imgsList = new ArrayList<>();

                imgMapBig.put("url", fungoMallDto.getBigUrl());
                imgMapBig.put("status", 1);
                imgMapBig.put("size", 1);
                imgMapBig.put("style", 1);

                Map<String, Object> imgMapSmall = new HashMap<>();
                imgMapSmall.put("url",fungoMallDto.getSmallUrl());
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

            mallGoods.setGoodsStatus(fungoMallDto.getGoodsStatus());
            mallGoods.setGoodsType(fungoMallDto.getGoodsType());
            mallGoods.setSort(fungoMallDto.getSort());
            mallGoods.setGoodsIntro( "由手动添加商品");
            mallGoods.setCreatedAt(new Date());
            mallGoods.setUpdatedAt(new Date());
            mallGoodsDaoService.insert(mallGoods);
            return JSON.toJSON( mallGoods ); // mallGoods.toString();
        }catch (Exception e){
            e.printStackTrace();
            logger.error("增加商品异常,参数="+fungoMallDto.toString(),e);
        }

//        String goodsName = "三只松鼠巨型零食礼盒2217g";
//        Long price = 19800L;
//
//        List<Map<String, Object>> imgsList = new ArrayList<>();
//        Map<String, Object> imgMapBig = new HashMap<>();
//        imgMapBig.put("url", "http://output-mingbo.oss-cn-beijing.aliyuncs.com/mall/goods/imgs/lbd.png");
//        imgMapBig.put("status", 1);
//        imgMapBig.put("size", 1);
//        imgMapBig.put("style", 1);
//
//        Map<String, Object> imgMapSmall = new HashMap<>();
//        imgMapSmall.put("url", "http://output-mingbo.oss-cn-beijing.aliyuncs.com/mall/goods/imgs/lbs.png");
//        imgMapSmall.put("status", 1);
//        imgMapSmall.put("size", 3);
//        imgMapSmall.put("style", 1);
//
//        imgsList.add(imgMapBig);
//        imgsList.add(imgMapSmall);
//
//
//        //----------
//        MallGoods mallGoods = new MallGoods();
//
//        Long goodsId = PKUtil.getInstance().longPK();
//
//        mallGoods.setId(goodsId);
//
//        mallGoods.setCid(cateId);
//        mallGoods.setGoodsName(goodsName);
//        mallGoods.setMainImg(JSON.toJSONString(imgsList));
//
//
//        mallGoods.setMarketPriceVcy(price);
//
//        mallGoods.setGoodsStatus(2);
//        mallGoods.setGoodsType(1);
//        mallGoods.setSort(4);
//
//        mallGoods.setCreatedAt(new Date());
//        mallGoods.setUpdatedAt(new Date());

        return null;

    }

    @Override
    public ResultDto<String> addSeckill(FungoMallDto fungoMallDto) {
        try {

        }catch (Exception e){

        }
        return null;
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

            //查询已上架的商品
            mallGoodsEntityWrapper.eq("goods_status", 2);

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