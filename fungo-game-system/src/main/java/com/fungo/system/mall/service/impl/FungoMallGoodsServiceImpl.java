package com.fungo.system.mall.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.config.NacosFungoCircleConfig;
import com.fungo.system.dao.MallSeckillOrderDao;
import com.fungo.system.dao.MemberInfoDao;
import com.fungo.system.dto.FungoMallDto;
import com.fungo.system.entity.*;
import com.fungo.system.feign.GamesFeignClient;
import com.fungo.system.helper.zookeeper.DistributedLockByCurator;
import com.fungo.system.mall.daoService.*;
import com.fungo.system.mall.entity.*;
import com.fungo.system.mall.mapper.MallLogsDao;
import com.fungo.system.mall.mapper.MallSeckillDao;
import com.fungo.system.mall.service.IFungoMallGoodsService;
import com.fungo.system.mall.service.commons.FungoMallScanOrderWithSeckillService;
import com.fungo.system.mall.service.commons.FungoMallSeckillTaskStateCommand;
import com.fungo.system.mall.service.consts.FungoMallSeckillConsts;
import com.fungo.system.service.MemberInfoService;
import com.fungo.system.service.MemberService;
import com.fungo.system.service.ScoreLogService;
import com.fungo.system.service.impl.MemberServiceImpl;
import com.game.common.api.InputPageDto;
import com.game.common.buriedpoint.BuriedPointUtils;
import com.game.common.buriedpoint.constants.BuriedPointEventConstant;
import com.game.common.buriedpoint.model.BuriedPointConsumeModel;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.mall.MallGoodsInput;
import com.game.common.dto.mall.MallGoodsOutBean;
import com.game.common.dto.mall.MallSeckillOrderDto;
import com.game.common.enums.AbstractResultEnum;
import com.game.common.repo.cache.facade.FungoCacheTask;
import com.game.common.util.FungoAESUtil;
import com.game.common.util.PKUtil;
import com.game.common.util.PageTools;
import com.game.common.util.StringUtil;
import com.game.common.util.date.DateTools;
import com.game.common.util.exception.BusinessException;
import io.netty.handler.codec.json.JsonObjectDecoder;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

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
    @Autowired
    private MallVirtualCardDaoService mallVirtualCardDaoService;
    @Autowired
    private MallSeckillDaoService mallSeckillDaoService;
    @Autowired
    private MemberServiceImpl memberServiceImpl;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberInfoService memberInfoService;
    @Autowired
    private MallLogsDao mallLogsDao;
    @Autowired
    private MallSeckillOrderDao mallSeckillOrderDao;
    @Autowired
    private FungoMallSeckillServiceImpl fungoMallSeckillServiceImpl;
    @Autowired
    private NacosFungoCircleConfig nacosFungoCircleConfig;
    @Autowired
    private MallOrderDaoService mallOrderDaoService;
    @Autowired
    private MallOrderGoodsDaoService mallOrderGoodsDaoService;
    @Autowired
    private IFungoMallGoodsService iFungoMallGoodsService;
    @Autowired
    private FungoMallScanOrderWithSeckillService fungoMallScanOrderWithSeckillServiceImpl;
    @Autowired
    private FungoCacheTask fungoCacheTask;
    @Autowired
    private ScoreLogService scoreLogService;
    @Autowired
    private MemberInfoDao memberInfoDao;

    @Value("${fungo.mall.seckill.aesSecretKey}")
    private String aESSecretKey;

    @Value("${sys.config.fungo.cluster.index}")
    private String clusterIndex;



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
    public ResultDto<String> addSeckill(String goodId,String stocks,String startDate,String endDate) {
        ResultDto<String>  resultDto = null;
        try {

            Long goodsId = Long.valueOf(goodId);
            MallGoods mallGoods = mallGoodsDaoService.selectById(goodId);

            String goodsName = mallGoods.getGoodsName();  //fungoMallDto.getGoodsName();

            String MarketPriceVcy = mallGoods.getMarketPriceVcy().toString();  //String.valueOf(fungoMallDto.getPrice());

            String stock = stocks; //fungoMallDto.getStock();

//            String startDate = DateTools.getCurrentDate( "-"); // fungoMallDto.getStartDate();

//            String endDate =  DateTools.getCurrentDate( "-"); //;


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
            seckill.setExt1("1");
            MallSeckill mallSeckill = mallSeckillDao.querySeckillGoodsByGoodId(goodId, queryStartDate,queryEndDate );
            if(mallSeckill == null){
                mallSeckillDaoServicel.insert(seckill);
            }else {
                logger.error( "新增每日库存失败,商品id="+ goodId);
            }
            resultDto = ResultDto.success();
        } catch (Exception ex) {
            logger.error("中秋抽奖增加秒杀异常goodId="+goodId,ex);
            resultDto = ResultDto.ResultDtoFactory.buildError("中秋抽奖增加秒杀异常");
        }
        return resultDto;
    }

    /**
     * 查询出当前中秋佳节的礼品
     * @param inputPageDto
     * @return
     */
    @Override
    public FungoPageResultDto<MallGoodsOutBean> getFestivalMall(InputPageDto inputPageDto) {
        FungoPageResultDto<MallGoodsOutBean> resultDto = new FungoPageResultDto<>();
        logger.info("查询秒杀活动的商品....");
        //查询可以秒杀的商品从2019-02-03~2019-02-10
        //获取当前日期 yyyyMMdd
        String currentDate = DateTools.getCurrentDate(null);
        List<MallGoodsOutBean> goodsOutBeanList = null;
        Page page = new Page(inputPageDto.getPage(),inputPageDto.getLimit());
        try {
            //记录日志
//            addMallLogs(mb_id, "", 0L, realIp, 1);

            //已指定秒杀活动开始和结束日期
            int startDate = FungoMallSeckillConsts.SECKILL_START_DATE;
            int endDate = FungoMallSeckillConsts.SECKILL_END_DATE;

            int currentDate_i = Integer.parseInt(currentDate);
            int endDate_i = Integer.parseInt(currentDate);

            //若当前日期大于2020-12-31，则不查询商品
            if (endDate_i > endDate) {
                return FungoPageResultDto.FungoPageResultDtoFactory.buildError("时间不对");
            }


            String queryStartDate = "";
            String queryEndDate = "";
            //若当前日期小于2月3日，则显示2月3日的商品
            if (startDate > currentDate_i) {
                queryStartDate = FungoMallSeckillConsts.SECKILL_START_DATE_FORMAT + " " + FungoMallSeckillConsts.SECKILL_START_TIME;
                queryEndDate = FungoMallSeckillConsts.SECKILL_START_DATE_FORMAT + " " + FungoMallSeckillConsts.SECKILL_END_TIME;
            } else {
                queryStartDate = DateTools.getCurrentDate("-"); // + " " + FungoMallSeckillConsts.SECKILL_START_TIME;
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
            List<String> goods_types = new ArrayList<>();
            goods_types.add("1");goods_types.add("2");goods_types.add("21");goods_types.add("22");goods_types.add("23");
            List<Map<String, Object>> goodsMapList = mallSeckillDao.queryFestivalSeckillGoods(page,queryStartDate, queryEndDate, goods_types, 2);
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
                    String ext2 = (String) objectMap.get("ext2");
                    goodsOutBean.setColorType(MallGoodsOutBean.GameColorTypeEnum.getValueByKey(ext2));

                    goodsOutBeanList.add(goodsOutBean);

                }
            }
            List<MallGoodsOutBean> list = goodsOutBeanList.stream().sorted((e1, e2) -> Long.valueOf(e1.getSeckillPriceVcy()).compareTo(Long.valueOf(e2.getSeckillPriceVcy())) ).collect( Collectors.toList());
            resultDto.setData(list);
            PageTools.pageToResultDto(resultDto,page);
        }catch (Exception e){
            logger.error("查询出所有的礼品",e);
            resultDto = FungoPageResultDto.FungoPageResultDtoFactory.buildError("查询出所有的礼品异常");
        }
        return resultDto;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FungoPageResultDto<MallGoodsOutBean> drawFestivalMall(String memberId, InputPageDto inputPageDto,String realIp) throws Exception {
        FungoPageResultDto<MallGoodsOutBean> resultDto = new FungoPageResultDto<>();
        try {
            Member member = memberService.selectById(memberId);
            boolean istrue = memberInfoService.shareFestival(memberId);
            List<MallLogs> mallLogs = new ArrayList<>();
            Wrapper<MallLogs> wrapper =  new EntityWrapper<MallLogs>().eq( "mb_id",memberId);
            int userType = 0 ;
            if(istrue && inputPageDto.getLimit() == 1){
                userType = 3;
                MemberInfo memberInfo = new MemberInfo();
                memberInfoService.delFestival(memberId);
            } else if(memberServiceImpl.getNewMember(member.getCreatedAt(),member.getLevel()) && inputPageDto.getLimit() == 1 && (mallLogsDao.selectList(wrapper.eq( "user_type",1)).size() < 2)){
                //查询是否已使用过
                    userType = 1;
            }else if(memberServiceImpl.getActiveMemeber(member.getId()) && inputPageDto.getLimit() == 1 && ( mallLogsDao.selectList(wrapper.eq( "user_type",2  )).size() < 2) ){
                //查询是否已使用过
                    userType = 2;
            }else {
                // 用户使用自己fun币购买
                boolean isEnough = false;
                if(1 == inputPageDto.getLimit()){
                    String money = String.valueOf(200 * inputPageDto.getLimit());
                    isEnough = fungoMallSeckillServiceImpl.checkUserMoney(memberId,money);
                    userType = 4;
                }else {
                    String money = String.valueOf(200 * inputPageDto.getLimit() - 50);
                    isEnough = fungoMallSeckillServiceImpl.checkUserMoney(memberId,money);
                    userType = 5;
                }
                if(!isEnough) {
                    return FungoPageResultDto.FungoPageResultDtoFactory.buildWarning( AbstractResultEnum.CODE_SYSTEM_SIX.getKey(),AbstractResultEnum.CODE_SYSTEM_SIX.getFailevalue());
                }
            }
            // 查询顺序表
            Page<MallSeckillOrder> page = new Page<>(inputPageDto.getPage(),inputPageDto.getLimit());
            List<MallSeckillOrder> mallSeckillOrders = mallSeckillOrderDao.getMallSeckillOrderByActive(page);
            if(mallSeckillOrders == null || mallSeckillOrders.size() == 0){
                return  FungoPageResultDto.FungoPageResultDtoFactory.buildWarning(AbstractResultEnum.CODE_SYSTEM_SEVEN.getKey(),AbstractResultEnum.CODE_SYSTEM_SEVEN.getFailevalue());
            }
            // 是否发送预警信息
            if(nacosFungoCircleConfig.isWarnningSwitch()){
                MallSeckillOrder mallSeckillOrder = mallSeckillOrders.get(0);
                if(mallSeckillOrder.getId() > nacosFungoCircleConfig.getWarningNumber()){
                    BasNotice basNotice = new BasNotice();
                    basNotice.setType( 6 );
                    basNotice.setIsRead( 0 );
                    basNotice.setIsPush( 0 );
                    basNotice.setMemberId( nacosFungoCircleConfig.getOperatorId());
                    basNotice.setCreatedAt( new Date() );
                    Map<String,Object> dataMap = new HashMap<>();
                    dataMap.put( "actionType","1");
                    dataMap.put( "userAvatar","http://output-mingbo.oss-cn-beijing.aliyuncs.com/official.png" );
                    dataMap.put( "targetType","3" );
                    dataMap.put( "userType","1" );
                    dataMap.put( "userName" ,"FunGo大助手");
                    dataMap.put( "userId","0b8aba833f934452992a972b60e9ad10" );
                    dataMap.put("content", "发送奖品数量已经超过1000，请尽快填充奖品。");
                    dataMap.put("msgTime", DateTools.fmtDate(new Date()));
                    basNotice.setData(JSON.toJSONString(dataMap));
                    basNotice.insert();
                }
            }
            int finalUserType = userType;
            List<MallSeckillOrderDto> mallSeckillOrderDtos = new ArrayList<>();

            mallSeckillOrders.stream().forEach( s -> {
                s.setIsactive("0");
                s.setUpdatedAt(new Date());
                s.setUpdatedBy(memberId);
                s.setRversion( s.getRversion()+1);

//                s.updateById();

                MallSeckillOrderDto mallSeckillOrderDto = new MallSeckillOrderDto();
                try {
                    BeanUtils.copyProperties(mallSeckillOrderDto,s);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                MallSeckill mallSeckill = fungoMallSeckillServiceImpl.getFestivalGoods(s.getMallGoodsId());
                if(finalUserType == 1 ){
                    //创建订单
                    MallOrder mallOrder = addSeckillOrder(memberId, s.getMallGoodsId(), mallSeckill, 11,0);
                    mallSeckillOrderDto.setOrderId( String.valueOf(mallOrder.getId()));
                }else if(finalUserType == 2){
                    //创建订单
                    MallOrder mallOrder = addSeckillOrder(memberId, s.getMallGoodsId(),mallSeckill, 11,0);
                    mallSeckillOrderDto.setOrderId( String.valueOf(mallOrder.getId()));
                }else if(finalUserType == 3){
                    //创建订单
                    MallOrder mallOrder = addSeckillOrder(memberId, s.getMallGoodsId(), mallSeckill, 11,0);
                    mallSeckillOrderDto.setOrderId( String.valueOf(mallOrder.getId()));
                }else if(finalUserType == 4){
                    //5.执行下单
                    //5.1 先冻结用户的可用余额
                    IncentAccountCoin incentAccountCoin = fungoMallSeckillServiceImpl.isFullMbFungo(memberId);
                    BigDecimal goodsPriceVcyBD = new BigDecimal(200);
                    goodsPriceVcyBD = goodsPriceVcyBD.setScale(2);
                    int isFreezeMbCoinAccount = fungoMallSeckillServiceImpl.freezeMemberFungo(memberId, incentAccountCoin, goodsPriceVcyBD);
                    if (1 == isFreezeMbCoinAccount) {
                        //创建订单
                        MallOrder mallOrder = addSeckillOrder(memberId, s.getMallGoodsId(), mallSeckill, 11,200);
                        mallSeckillOrderDto.setOrderId( String.valueOf(mallOrder.getId()));
                    }
                }else if(finalUserType == 5){
                    //5.执行下单
                    //5.1 先冻结用户的可用余额
                    IncentAccountCoin incentAccountCoin = fungoMallSeckillServiceImpl.isFullMbFungo(memberId);
                    BigDecimal goodsPriceVcyBD = new BigDecimal(190);
                    goodsPriceVcyBD = goodsPriceVcyBD.setScale(2);
                    int isFreezeMbCoinAccount = fungoMallSeckillServiceImpl.freezeMemberFungo(memberId, incentAccountCoin, goodsPriceVcyBD);
                    if (1 == isFreezeMbCoinAccount) {
                        //创建订单
                        MallOrder mallOrder = addSeckillOrder(memberId, s.getMallGoodsId(), mallSeckill, 11,190);
                        mallSeckillOrderDto.setOrderId( String.valueOf(mallOrder.getId()));
                    }
                }
                mallSeckillOrderDtos.add(mallSeckillOrderDto);
                fungoMallSeckillServiceImpl.addMallLogs(memberId, "", Long.parseLong(s.getMallGoodsId()), realIp, 2,"/api/system/mall/draw", finalUserType,Integer.valueOf(inputPageDto.getFilter()));
            });
            mallSeckillOrderDao.updateBatch(mallSeckillOrders);
//            orderMap = iFungoMallSeckillService.createOrderWithSeckill(mallOrderInput, realIp);
            List<String> mallOrderIds = mallSeckillOrderDtos.stream().map(MallSeckillOrderDto::getOrderId).collect(Collectors.toList());
            // 那个商品id去下订单  不能和原来的秒杀
            boolean isOk = dealFestivalOrder(mallOrderIds,finalUserType);
            if(!isOk){
                throw new Exception("处理订单异常");
            }
            List<MallGoodsOutBean> goodsOutBeanList = new ArrayList<>();
            mallSeckillOrderDtos.stream().forEach( x ->{
                List<MallGoodsOutBean> finalGoodsOutBeanList = goodsOutBeanList;
                List<Map<String, Object>> goodsMapList = mallSeckillDao.queryFestivalSeckillGoodsById(Arrays.asList(x.getMallGoodsId()));
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
                    String ext2 = (String) objectMap.get("ext2");
                    goodsOutBean.setColorType(MallGoodsOutBean.GameColorTypeEnum.getValueByKey(ext2));
                    goodsOutBean.setOrderId(x.getOrderId());
                    finalGoodsOutBeanList.add(goodsOutBean);
                }
            });
            List<MallGoodsOutBean> list = goodsOutBeanList.stream().sorted((e1, e2) -> Long.valueOf(e1.getSeckillPriceVcy()).compareTo(Long.valueOf(e2.getSeckillPriceVcy())) ).collect( Collectors.toList());
            resultDto.setData(list);
//            PageTools.pageToResultDto(resultDto,page);
        }catch (Exception e){
            logger.error( "中秋礼品抽奖异常用户id="+memberId,e);
            throw new Exception("处理订单异常");

        }
        return resultDto;
    }

    @Override
    public ResultDto<JSON> drawnFestivalMall(String memberId) {
        JSONObject json = new JSONObject();
        try {
            List<MallLogs>  mallLogs = mallLogsDao.selectMallLogsByUserId( memberId);
            Map<Integer, List<MallLogs>> mallLogsMap =  mallLogs.stream().collect(groupingBy(MallLogs::getUserType));
            Member member = memberService.selectById(memberId);
            boolean isNew = memberServiceImpl.getNewMember(member.getCreatedAt(),member.getLevel());
            boolean isOld = memberServiceImpl.getActiveMemeber(member.getId());
            //查询log日志 获取用户签到累计天数
            EntityWrapper<ScoreLog> scoreLogEntityWrapper = new EntityWrapper<ScoreLog>();

            String startDate = nacosFungoCircleConfig.getStartDate();
            String endDate = nacosFungoCircleConfig.getEndDate();
            int days = nacosFungoCircleConfig.getFestivalDays();

            startDate = startDate+ " " + FungoMallSeckillConsts.SECKILL_START_TIME;
            endDate = endDate + " " + FungoMallSeckillConsts.SECKILL_END_TIME;
            scoreLogEntityWrapper.eq("member_id", memberId);
            scoreLogEntityWrapper.in("task_type", "22,220");
            scoreLogEntityWrapper.ge("created_at", startDate);
            int signInCount = scoreLogService.selectCount(scoreLogEntityWrapper);
            MemberInfo memberInfo = new MemberInfo();
            memberInfo.setMdId( memberId);
            memberInfo = memberInfoDao.selectOne( memberInfo);
            if(mallLogsMap != null){
                List<MallLogs> mallLogs1 = mallLogsMap.get(1);
                json.put( "newMember",mallLogs1 != null ? mallLogs1.size() : 0 );
                List<MallLogs> mallLogs2 = mallLogsMap.get(2);
                json.put( "oldMember", mallLogs2 != null ? mallLogs2.size() : 0);
                List<MallLogs> mallLogs3 = mallLogsMap.get(3);
                json.put( "share", mallLogs3 != null ? mallLogs3.size() : 0);
                json.put( "count",signInCount > 4 ? 4 : signInCount );
                json.put( "isNew",isNew ? 1: 0 );
                json.put( "isOld",isOld ? 1 : 0 );
                json.put( "isShare",memberInfo != null ? 1 : 0 );
                json.put( "surplus", ((Integer)json.get("isNew")* 2) +((Integer)json.get("isOld")* 2)+(Integer)json.get("isShare")-(Integer)json.get("newMember")-(Integer)json.get("oldMember")-(Integer)json.get("share"));
            }
        }catch (Exception e){
            logger.error("",e);
        }
        return ResultDto.ResultDtoFactory.buildSuccess(json);
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

    /**
     * 查询某个游戏礼包未卖出剩余的虚拟卡数量
     * @param goods_id
     * @return
     */
    private int getUnSaledVMCardWithGame( Long goods_id) {
        //查询虚拟卡验证用户是否购买过
        EntityWrapper<MallVirtualCard> virtualCardEntityWrapper = new EntityWrapper<MallVirtualCard>();
        virtualCardEntityWrapper.eq("goods_id", goods_id);
        virtualCardEntityWrapper.eq("card_type", 31);
        virtualCardEntityWrapper.eq("is_saled", -1);
        int count = mallVirtualCardDaoService.selectCount(virtualCardEntityWrapper);
        return count;
    }

    /**
     * 中秋添加订单和订单商品表
     *
     * @param mb_id
     * @param goods_id
     * @param mallSeckill
     */
    public MallOrder addSeckillOrder(String mb_id, String goods_id, MallSeckill mallSeckill, int order_type,long seckillPriceVcy_i) {

        //1 查询出商品详情
        EntityWrapper<MallGoods> goodsEntityWrapper = new EntityWrapper<MallGoods>();
        goodsEntityWrapper.eq("id", goods_id);
        MallGoods goods = mallGoodsDaoService.selectOne(goodsEntityWrapper);

        if (null == goods) {
            throw new BusinessException("-1", "秒杀生成订单过程中，查询商品和秒杀商品数据出现异常");
        }


        //2 从秒杀商品信息中 解析商品秒杀价格
//        String seckillPriceVcy = mallSeckill.getSeckillPriceVcy();
        //解密 商品秒杀价格
//        String seckillPriceVcyDecrypt = FungoAESUtil.decrypt(seckillPriceVcy, aESSecretKey + FungoMallSeckillConsts.AES_SALT);

//        long seckillPriceVcy_i = 0;
//        if (StringUtils.isNoneBlank(seckillPriceVcyDecrypt)) {
//            seckillPriceVcy_i = Integer.parseInt(seckillPriceVcyDecrypt);
//        }

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
            case 24:
                orderSN = "V" + String.valueOf(PKUtil.getInstance(clusterIndex_i).longPK());
                break;
            case 25:
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

        order.setOrderType(order_type);

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

    private ReentrantLock lock = new ReentrantLock();

    /**
     * 功能描述: 处理中秋订单
     * @param: []
     * @return: boolean
     * @auther: dl.zhang
     * @date: 2019/8/28 16:19
     */
    public boolean dealFestivalOrder(List<String> orderIds,int finalUserType){
        boolean isOk = false;
        try {
            logger.info("扫描订单表,处理已确定和已冻结余额的订单....");

//            //开始扫描订单，设置处理订单状态为 2 正在扫描订单处理中
            FungoMallSeckillTaskStateCommand.getInstance().setScanOrderIsOk(2);
//            //每次处理条数
//            int pageSize = 40;
//
//            //分页查询订单
//            //1 先查询订单总条数
            EntityWrapper<MallOrder> orderEntityWrapper = new EntityWrapper<MallOrder>();
            orderEntityWrapper.in( "id", orderIds);
//            //1 已确认     3 已冻结物品价格可用余额
//            orderEntityWrapper.eq("order_status", 1).eq("pay_status", 3).eq( "order_type",11);
//
//            int orderCount = mallOrderDaoService.selectCount(orderEntityWrapper);
//            if (orderCount > 0) {

//                long totalPage = (orderCount - 1) / pageSize + 1;

                //订单按创建时间正序顺序排列
//                orderEntityWrapper.orderBy("create_time", true);

                //按页处理订单 当前页下标从1开始
//                for (int i = 1; i <= totalPage; i++) {
//                    Page<MallOrder> mallOrderPage = mallOrderDaoService.selectList(orderEntityWrapper);

                    List<MallOrder> mallOrderList = mallOrderDaoService.selectList(orderEntityWrapper);
                    if (null != mallOrderList && !mallOrderList.isEmpty()) {

                        for (MallOrder mallOrder : mallOrderList) {

                            //处理交易
                            //1.把该订单关联的商品查询出来
                            String mb_id = mallOrder.getMbId();
                            Long orderId = mallOrder.getId();

                            //游戏id
                            String gameId = mallOrder.getExt1();

                            boolean isPushMsg = false;

                            List<MallOrderGoods> orderGoodsList = fungoMallSeckillServiceImpl.queryOrderGoodsWithMember(mb_id, String.valueOf(orderId));
                            if (null != orderGoodsList && !orderGoodsList.isEmpty()) {
                                for (MallOrderGoods goods : orderGoodsList) {

                                    //2.获取购买的商品数量
                                    Long goodsNumber = goods.getGoodsNumber();
                                    //商品价格fungo币
                                    Long goodsPriceVcy = mallOrder.getGoodsAmountVcy(); //goods.getGoodsPriceVcy();

                                    //3. 验证该种商品库存是否充足
                                    MallSeckill mallSeckill = null;
                                    boolean isFullGoodsStock = false;
                                    //游戏礼包订单
                                    if (3 == goods.getGoodsType().intValue()) {

                                        MallGoodsInput mallGoodsInput = new MallGoodsInput();
                                        mallGoodsInput.setGameId(gameId);
                                        ResultDto<Map<String, Object>> mapResultDto =iFungoMallGoodsService.queryGoodsCountWithGame(mallGoodsInput);
                                        if (null != mapResultDto) {
                                            Map<String, Object> dataMap = mapResultDto.getData();
                                            Integer goodsCount = (Integer) dataMap.get("goodsCount");
                                            if (null != goodsCount && goodsCount.intValue() > 0) {
                                                isFullGoodsStock = true;
                                            }
                                        }

                                    } else {
                                        mallSeckill = fungoMallSeckillServiceImpl.getFestivalGoods(String.valueOf(goods.getGoodsId()));
                                        isFullGoodsStock = fungoMallScanOrderWithSeckillServiceImpl.isFullGoodsStockForScanOrder(mallSeckill, goodsNumber, goods.getGoodsId());
                                    }
                                    //3.1 若库存不足，把冻结余额 加回到 可用余额中，同时标记订单为 8 商品库存不足，继续处理下一个商品
//                                    if (!isFullGoodsStock ) {
//                                        logger.info("mb_id:{}--order_id:{}--goods_id:{}--商品库存不足，解冻用户fungo币账户,修改订单状态: 8 商品库存不足,4 被冻结额已解冻, -1 未发货",
//                                                mb_id, orderId, goods.getGoodsId());
//                                        //解冻用户fungo币账户
//                                        fungoMallScanOrderWithSeckillServiceImpl.unfreezeAccountCoinWithMember(mb_id, goodsPriceVcy);
//                                        //修改订单状态 8 商品库存不足  4 被冻结额已解冻   -1 未发货
//                                        fungoMallScanOrderWithSeckillServiceImpl.updateOrderStatusWithScan(mb_id, orderId, 8, 4, -1);
//                                        continue;
//                                    }

                                    //若库存足，先扣钱，再减库存
                                    //4.扣减冻结商品价格额
                                    boolean dedCoinAccoutResult = true;
                                    if(finalUserType == 4 || finalUserType == 5 ){
                                        dedCoinAccoutResult = fungoMallScanOrderWithSeckillServiceImpl.deductionAccountCoinWithMember(mb_id, goodsPriceVcy);
                                    }

                                    if (!dedCoinAccoutResult) {
                                        //若扣减冻结账户失败 同时标记订单为 3  无效   5 扣减冻结失败 ，继续处理下一个商品
                                        logger.info("mb_id:{}--order_id:{}--goods_id:{}--扣减冻结用户账户的商品价格额失败,修改订单状态: 3 无效, 5 扣减冻结失败 ,-1 未发货",
                                                mb_id, orderId, goods.getGoodsId());
                                        //修改订单状态  3 无效   5 扣减冻结失败   -1 未发货
                                        fungoMallScanOrderWithSeckillServiceImpl.updateOrderStatusWithScan(mb_id, orderId, 3, 5, -1);
                                        continue;
                                    }

                                    //5.扣减库存
                                    //不是游戏礼包订单
                                    if (3 != goods.getGoodsType().intValue()) {
                                        boolean goodsStockUpdateResult = fungoMallScanOrderWithSeckillServiceImpl.deductionGoodsStock(mallSeckill, goodsNumber);
//                                        if (!goodsStockUpdateResult ) {
//                                            logger.info("mb_id:{}--order_id:{}--goods_id:{}--扣减库存失败，解冻用户fungo币账户,修改订单状态: 8 商品库存不足,4 被冻结额已解冻 ,-1 未发货",
//                                                    mb_id, orderId, goods.getGoodsId());
//                                            //扣减库存失败
//                                            //解冻用户fungo币账户
//                                            fungoMallScanOrderWithSeckillServiceImpl.unfreezeAccountCoinWithMember(mb_id, goodsPriceVcy);
//                                            //修改订单状态  8 商品库存不足  4 被冻结额已解冻  -1 未发货
//                                            fungoMallScanOrderWithSeckillServiceImpl.updateOrderStatusWithScan(mb_id, orderId, 8, 4, -1);
//                                            continue;
//                                        }
                                    }

                                    lock.lock();
                                    try {
                                        //6. 交易成功修改订单状态  5 交易成功 & 2 已付款   &  4 无收货信息
                                        boolean isUpdateSucc = fungoMallScanOrderWithSeckillServiceImpl.updateOrderStatusWithScan(mb_id, orderId, 5, 2, 4);

                                        logger.info("mb_id:{}--order_id:{}--goods_id:{}--交易成功修改订单状态:5 交易成功,2 已付款,4 无收货信息",
                                                mb_id, orderId, goods.getGoodsId());

                                        //6.1 若是虚拟商品，把虚拟卡号，转移给用户对应的虚拟商品 ,
                                        // 修改虚拟卡表为已经卖出状态，同时把虚拟卡信息保存到订单商品表中
                                        // 若虚拟商品不存在，则由客户手动处理，程序不做处理
                                        MallVirtualCard vCardNewWithOrderGoods = null;
                                        switch (goods.getGoodsType().intValue()) {
                                            case 21:
                                                vCardNewWithOrderGoods = fungoMallScanOrderWithSeckillServiceImpl.updateOrderGoodsInfoWithVCard(mb_id, orderId, goods);
                                                break;
                                            case 22:
                                                vCardNewWithOrderGoods = fungoMallScanOrderWithSeckillServiceImpl.updateOrderGoodsInfoWithVCard(mb_id, orderId, goods);
                                                break;
                                            case 23:
                                                vCardNewWithOrderGoods = fungoMallScanOrderWithSeckillServiceImpl.updateOrderGoodsInfoWithVCard(mb_id, orderId, goods);
                                                break;
                                            case 24:
                                                vCardNewWithOrderGoods = fungoMallScanOrderWithSeckillServiceImpl.updateOrderGoodsInfoWithVCard(mb_id, orderId, goods);
                                                break;
                                            case 3:
                                                vCardNewWithOrderGoods = fungoMallScanOrderWithSeckillServiceImpl.updateOrderGoodsInfoWithVCard(mb_id, orderId, goods,31);
                                                break;
                                            default:
                                                break;
                                        }

                                        //7. 记录用户fungo币消费明细
                                        if (isUpdateSucc) {
                                            if(finalUserType == 4 || finalUserType == 5 ){
                                                fungoMallScanOrderWithSeckillServiceImpl.addFungoPayLogs(mb_id, mallOrder.getMbMobile(), mallOrder.getMbName(), String.valueOf(goodsPriceVcy),
                                                        goods.getGoodsName());
                                                // fun币消耗埋点
                                                Long goodsPriceVcy1 = goods.getGoodsPriceVcy();
                                                if(goodsPriceVcy1!=null&&goodsPriceVcy1>0){
                                                    BuriedPointConsumeModel model = new BuriedPointConsumeModel();
                                                    model.setDistinctId(mb_id);
                                                    model.setEventName( BuriedPointEventConstant.EVENT_KEY_GOLD_CONSUMED);
                                                    model.setPlatForm( BuriedPointUtils.getPlatForm());
                                                    model.setMethod("消费");
                                                    model.setAmount(goodsPriceVcy);
                                                    BuriedPointUtils.publishBuriedPointEvent(model);
                                                }
                                            }
                                        }
                                        //8. 推送系统消息通知用户商品秒杀成功和虚拟卡商品卡号密码等信息
                                        if (isUpdateSucc && 3 != goods.getGoodsType().intValue()) {
                                            fungoMallScanOrderWithSeckillServiceImpl.pushMsgToMember(mb_id, goods.getGoodsName(), goods.getGoodsType(), vCardNewWithOrderGoods);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        logger.error("lock锁执行失败");
                                    } finally {
                                        lock.unlock();
                                    }
                                }
                            }
                            // 9. 清除缓存的订单数据
                            logger.info("用户订单交易成功，删除该用户订单缓存,mb_id:{}", mb_id);
                            fungoMallScanOrderWithSeckillServiceImpl.removeCacheWithOrders(mb_id, String.valueOf(orderId));
                            fungoMallScanOrderWithSeckillServiceImpl.removeCacheWithOrders(mb_id, "");

                            String cacheKey = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INCENTS_FORTUNE_COIN_POST + mb_id;
                            logger.info("扫描处理订单-清除缓存的用户fun币消耗", cacheKey);
                            fungoCacheTask.excIndexCache(false, cacheKey, "", null);
                        }
                    }
//                }
//            }
            isOk = true;
        } catch (Exception ex) {
            logger.error("扫描订单表,处理已确定和已冻结余额的订单出现异常", ex);
            ex.printStackTrace();
            throw new BusinessException("-1", "扫描订单表,处理已确定和已冻结余额的订单出现异常");
        } finally {
            //处理完设置扫描订单为 3 完成扫描订单
            FungoMallSeckillTaskStateCommand.getInstance().setScanOrderIsOk(3);
            //清除缓存的商品数据
            fungoMallScanOrderWithSeckillServiceImpl.removeCacheWithGoods();
        }
        return isOk;
    }

    //---------
}