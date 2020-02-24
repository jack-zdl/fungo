package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.dao.MemberDao;
import com.fungo.system.dto.MemberNoticeInput;
import com.fungo.system.entity.BasNotice;
import com.fungo.system.entity.Member;
import com.fungo.system.entity.MemberApple;
import com.fungo.system.feign.CommunityFeignClient;
import com.fungo.system.feign.GamesFeignClient;
import com.fungo.system.helper.mq.MQProduct;
import com.fungo.system.helper.zookeeper.DistributedLockByCurator;
import com.fungo.system.service.BasNoticeService;
import com.fungo.system.service.IMemberNoticeService;
import com.fungo.system.service.MemberNoticeDaoService;
import com.game.common.consts.MessageConstants;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmCmtReplyDto;
import com.game.common.dto.community.CmmCommentDto;
import com.game.common.dto.community.MooMessageDto;
import com.game.common.dto.game.GameEvaluationDto;
import com.game.common.dto.game.GameSurveyRelDto;
import com.game.common.enums.SystemTypeEnum;
import com.game.common.repo.cache.facade.FungoCacheNotice;
import com.game.common.util.CommonUtil;
import com.game.common.util.StringUtil;
import com.game.common.util.date.DateTools;
import com.game.common.vo.DelObjectListVO;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import java.util.concurrent.Executor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

@Service
public class MemberNoticeServiceImpl implements IMemberNoticeService {

    private static final Logger logger = LoggerFactory.getLogger(MemberNoticeServiceImpl.class);

    @Autowired
    private MemberNoticeDaoService memberNoticeDaoService;
    @Autowired
    private FungoCacheNotice fungoCacheNotice;
    @Autowired
    private GamesFeignClient gamesFeignClient;
    @Value("${sys.config.fungo.cluster.index}")
    private String clusterIndex;
    @Autowired
    private DistributedLockByCurator distributedLockByCurator;
    @Autowired
    private BasNoticeService basNoticeService;
    @Autowired
    private MQProduct mqProduct;
    @Autowired
    private MemberServiceImap memberServiceImap;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private MemberAppleServiceImap memberAppleServiceImap;
    @Autowired(required = false)
    private CommunityFeignClient communityFeignClient;


    /**
     * 修改消息状态为已读
     */
//    private void updateNotes(String mb_id, List<String> noticeIdList) {
//        for (String noticeId : noticeIdList) {
//            MemberNotice memberNotice = new MemberNotice();
//            memberNotice.setMbId(mb_id);
//            memberNotice.setId(Long.parseLong(noticeId));
//            updateMbNotice(memberNotice);
//        }
//    }


    @Override
    public void deleteMbNotices(String mb_id, Integer ntc_type, Long noticeId) {
        try {

            if (StringUtils.isBlank(mb_id)) {
                return;
            }
            if (null == ntc_type) {
                return;
            }

            Map<String, Object> paramMap = new HashMap<String, Object>();

            if (StringUtils.isNotBlank(mb_id) && ntc_type.intValue() > 0) {
                paramMap.put("mb_id", mb_id);
                paramMap.put("ntc_type", ntc_type);
            } else {
                paramMap.put("id", noticeId);
            }
            memberNoticeDaoService.deleteByMap(paramMap);
        } catch (Exception ex) {
            logger.error("删除用户的消息数据出现异常:", ex);
            ex.printStackTrace();
        }
    }


//
//    @Override
//    public void addMbNotice(String mb_id, Integer ntc_type, Object ntcData) {
//
//        try {
//            if (StringUtils.isBlank(mb_id)) {
//                return;
//            }
//            if (null == ntcData) {
//                return;
//            }
//
//            int clusterIndex_i = Integer.parseInt(clusterIndex);
//
//            MemberNotice memberNotice = new MemberNotice();
//            memberNotice.setId(PKUtil.getInstance(clusterIndex_i).longPK());
//            memberNotice.setIsRead(2);
//            memberNotice.setNtcType(ntc_type);
//            memberNotice.setMbId(mb_id);
//            memberNotice.setCreatedAt(new Date());
//            memberNotice.setUpdatedAt(new Date());
//
//            memberNotice.setNtcData(JSON.toJSONString(ntcData));
//
//            memberNoticeDaoService.insert(memberNotice);
//
//            //同时保存到Redis缓存
//            String keyPrefix = FungoCoreApiConstant.REDIS_MEMBER_NOTICE + mb_id + String.valueOf(ntc_type.intValue());
//            fungoCacheNotice.excIndexCacheWithOutSecurity(true, keyPrefix, String.valueOf(memberNotice.getId()), ntcData);
//
//        } catch (Exception ex) {
//            logger.error("添加用户的消息数据出现异常:", ex);
//            ex.printStackTrace();
//        }
//    }

//    @Override
//    public void updateMbNotice(MemberNotice memberNotice) {
//        try {
//
//            if (null == memberNotice) {
//                return;
//            }
//            if (StringUtils.isBlank(memberNotice.getMbId())) {
//                return;
//            }
//
//            if (null == memberNotice.getId() || memberNotice.getId().longValue() <= 0) {
//                return;
//            }
//            MemberNotice updateMemberNotice = new MemberNotice();
//            updateMemberNotice.setIsRead(1);
//            updateMemberNotice.setId(memberNotice.getId());
//            updateMemberNotice.updateById();
//
//        } catch (Exception ex) {
//            logger.error("修改用户的消息数据出现异常:", ex);
//            ex.printStackTrace();
//        }
//    }

    /**
     * 功能描述: 根据游戏模块更新系统消息
     * @auther: dl.zhang
     * @date: 2019/7/30 10:49
     */
    @Transactional
    @Override
    public void updateSystemByGame() throws Exception {
        try {
            FungoPageResultDto<GameSurveyRelDto> gameSurveyRelDtoFungoPageResultDto = gamesFeignClient.getMemberNoticeByGame();
            if (gameSurveyRelDtoFungoPageResultDto != null) {
                List<GameSurveyRelDto> gameSurveyRelList = gameSurveyRelDtoFungoPageResultDto.getData();
                List<String> ids = new Vector( gameSurveyRelList.size() );
                List<BasNotice> basNotices = new Vector( gameSurveyRelList.size() );
                StopWatch watch = new StopWatch( "Stream效率测试" );
                watch.start( "parallelStream start" );
                gameSurveyRelList.parallelStream().forEach( s -> {
                    try {
                        BasNotice basNotice = new BasNotice();
                        basNotice.setType( 6 );
                        basNotice.setIsRead( 0 );
                        basNotice.setIsPush( 0 );
                        basNotice.setMemberId( s.getMemberId() );
                        basNotice.setCreatedAt( new Date() );
                        Map map = new ConcurrentHashMap();
                        map.put( "actionType", "3" );
                        map.put( "content", StringUtil.getGameNotice( s.getGameName(), DateTools.getCurrentDate( "-" ), s.getPhoneModel() ) );
                        basNotice.setData( JSON.toJSONString( map ) );
                        basNotices.add( basNotice );
                        basNotice.insert();
                        ids.add( s.getId() );
                    } catch (Exception e) {
                        logger.error( "更新系统消息失败,用户id:{},游戏id:{}", s.getMemberId(), s.getGameId(), e );
                    }
                } );
                watch.stop();
                System.out.println( watch.prettyPrint() );
                //需要发送数据到game模块，更新t_game_survey_rel表，更改为已通知
                // @todo
                List idList = new ArrayList( ids );
//                mqProduct.gameSurveyRelUpdate( idList );
            }
        } catch (Exception e) {
            logger.error( "根据游戏模块更新系统消息异常", e );
            throw new Exception( "根据游戏模块更新系统消息异常" );
        }
    }

    /**
     * 功能描述: 给某篇文章置顶或者推荐发送消息
     * @return: void
     * @auther: dl.zhang
     * @date: 2019/7/31 9:34
     */
    @Transactional
    public void insertSystemNotice (String mobileType,String memberId,String data) throws Exception {
            try {
//                distributedLockByCurator.acquireDistributedLock( memberId );
                //从DB查
//                EntityWrapper<MemberNotice> noticeEntityWrapper = new EntityWrapper<>();
//                noticeEntityWrapper.eq( "mb_id", memberId );
//                noticeEntityWrapper.eq( "ntc_type", 7 );
//                noticeEntityWrapper.eq( "is_read", 2 );
//                List<MemberNotice> noticeListDB = memberNoticeDaoService.selectList( noticeEntityWrapper );
//                if (noticeListDB != null && noticeListDB.size() > 0) {
//                    noticeListDB.parallelStream().forEach( x -> {
//                        String jsonString = x.getNtcData();
//                        JSONObject jsonObject = JSON.parseObject( jsonString );
//                        jsonObject.put( "notice_count", (int) jsonObject.get( "notice_count" ) + 1 );
//                        jsonObject.put( "count", (int) jsonObject.get( "count" ) + 1 );
////                        jsonObject.put( "mobileType", mobileType );
//                        x.setNtcData( jsonObject.toJSONString());
//                        x.updateById();
//                    } );
//                } else {
//                    MemberNotice memberNotice = new MemberNotice();
//                    int clusterIndex_i = Integer.parseInt( clusterIndex );
//                    memberNotice.setId( PKUtil.getInstance( clusterIndex_i ).longPK() );
//                    memberNotice.setIsRead( 2 );
//                    memberNotice.setNtcType( 7 );
//                    memberNotice.setMbId( memberId );
//                    memberNotice.setCreatedAt( new Date() );
//                    memberNotice.setUpdatedAt( new Date() );
//                    Map map = new ConcurrentHashMap( 4 );
//                    map.put( "count", 1 );
////                    map.put( "mobileType", mobileType );
//                    map.put( "like_count", 0 );
//                    map.put( "comment_count", 0 );
//                    map.put( "notice_count", 1 );
//                    memberNotice.setNtcData( JSON.toJSONString( map ) );
//                    memberNoticeDaoService.insert( memberNotice );
//                }
                BasNotice basNotice = new BasNotice();
                basNotice.setType( 6 );
                basNotice.setChannel(mobileType);
                basNotice.setIsRead( 0 );
                basNotice.setIsPush( 0 );
                basNotice.setMemberId( memberId );
                basNotice.setCreatedAt( new Date() );
                basNotice.setData( data );
                basNotice.insert();
            } catch (Exception e) {
                logger.error( "根据游戏模块更新系统消息异常", e );
                throw new Exception( "根据游戏模块更新系统消息异常" );
            }finally {
//                distributedLockByCurator.releaseDistributedLock(memberId);
            }
        }

    /**
     * 功能描述: 给某篇文章置顶或者推荐发送消息
     * @return: void
     * @auther: dl.zhang
     * @date: 2019/7/31 9:34
     */
    @Transactional
    public void insertSystemVersionNotice (String mobileType,String data) throws Exception {
        try {
            EntityWrapper<Member> memberEntity = new EntityWrapper<>();
            memberEntity.eq( "state","0" );
            StopWatch watch = new StopWatch( "Stream效率测试" );
            watch.start( "总时间" );
            List<String> memberList = memberDao.getMemberList();
            String channel = "";
            if(SystemTypeEnum.ANDROID.getKey().equals(mobileType)){  // 安卓用户
                channel = SystemTypeEnum.ANDROID.getKey();
            }else if (SystemTypeEnum.IOS.getKey().equals(mobileType)){ // ios用户
                channel = SystemTypeEnum.IOS.getKey();
            }else {  //pc 用户

            }

            List<List<String>> memberIdsList = Lists.partition(memberList, 10);
            CountDownLatch countDownLatch = new CountDownLatch(10);
            Executor executorService = CommonUtil.createThread(10);
            String finalChannel = channel;
            memberIdsList.parallelStream().forEach( e ->{
                executorService.execute( new Runnable() {
                    @Override
                    public void run() {
                        updateNotice( finalChannel,e,data);
                    }
                });
                countDownLatch.countDown();
            });
            countDownLatch.await();
            watch.stop();
            logger.error("系统版本通知"+watch.prettyPrint() );
            logger.error("系统版本通知完成");
        } catch (Exception e) {
            logger.error( "根据游戏模块更新系统消息异常", e );
            throw new Exception( "根据游戏模块更新系统消息异常" );
        }
    }

    @Transactional
    @Override
    public List<Map<String, Object>> insertMbNotices(MemberNoticeInput noticeInput) {

        try {
            String id = noticeInput.getMb_id();
            List<String> listString = Arrays.asList( id.split( "," ) );
            GameDto gameDto = gamesFeignClient.selectOne(noticeInput.getGameId());
            if(gameDto == null){
                return  null;
            }
            listString.parallelStream().forEach( s -> {
                try {
                    EntityWrapper<MemberApple> memberAppleEntityWrapper = new EntityWrapper<>();
                    memberAppleEntityWrapper.eq( "member_id",s );
                    List<MemberApple> memberApples = memberAppleServiceImap.selectList(memberAppleEntityWrapper);
                    String appId= "";
                    if(memberApples != null && memberApples.size() >0  ){
                        appId = memberApples.get(0).getAppleId( );
                    }
                    Map<String,String> dataMap = new HashMap<>();
                    dataMap.put("actionType","1");
                    dataMap.put("targetType","1");
                    dataMap.put("userId", "0b8aba833f934452992a972b60e9ad10");
                    dataMap.put("userType", "1");
                    dataMap.put("userAvatar", "http://output-mingbo.oss-cn-beijing.aliyuncs.com/official.png");
                    dataMap.put("userName", "FunGo大助手");
                    dataMap.put("content", StringUtil.getGameIOSNotice(gameDto.getName(), appId));
                    dataMap.put("targetId",gameDto.getId());
                    dataMap.put("msgTime", DateTools.fmtDate(new Date()));
//                    distributedLockByCurator.acquireDistributedLock( s );
                    //从DB查
//                    EntityWrapper<MemberNotice> noticeEntityWrapper = new EntityWrapper<>();
//                    noticeEntityWrapper.eq( "mb_id", s );
//                    noticeEntityWrapper.eq( "ntc_type", 7 );
//                    noticeEntityWrapper.eq( "is_read", 2 );
//                    List<MemberNotice> noticeListDB = memberNoticeDaoService.selectList( noticeEntityWrapper );
//                    if (noticeListDB != null && noticeListDB.size() > 0) {
//                        noticeListDB.parallelStream().forEach( x -> {
//                            String jsonString = x.getNtcData();
//                            JSONObject jsonObject = JSON.parseObject( jsonString );
//                            jsonObject.put( "notice_count", (int) jsonObject.get( "notice_count" ) + 1 );
//                            jsonObject.put( "count", (int) jsonObject.get( "count" ) + 1 );
//                            x.setNtcData( jsonObject.toJSONString() );
//                            x.updateById();
//                        } );
//                    } else {
//                        MemberNotice memberNotice = new MemberNotice();
//                        int clusterIndex_i = Integer.parseInt( clusterIndex );
//                        memberNotice.setId( PKUtil.getInstance( clusterIndex_i ).longPK() );
//                        memberNotice.setIsRead( 2 );
//                        memberNotice.setNtcType( 7 );
//                        memberNotice.setMbId( s );
//                        memberNotice.setCreatedAt( new Date() );
//                        memberNotice.setUpdatedAt( new Date() );
//                        Map map = new ConcurrentHashMap( 4 );
//                        map.put( "count", 1 );
//                        map.put( "like_count", 0 );
//                        map.put( "comment_count", 0 );
//                        map.put( "notice_count", 1 );
//                        memberNotice.setNtcData( JSON.toJSONString( map ) );
//                        memberNoticeDaoService.insert( memberNotice );
//                    }
                    BasNotice basNotice = new BasNotice();
                    basNotice.setType( 6 );
                    basNotice.setIsRead( 0 );
                    basNotice.setIsPush( 0 );
                    basNotice.setMemberId( s );
                    basNotice.setCreatedAt( new Date() );
                    basNotice.setData( JSON.toJSONString(dataMap));
                    basNotice.insert();
                } catch (Exception e) {
                    logger.error( "根据游戏模块更新系统消息异常", e );
                } finally {
//                    distributedLockByCurator.releaseDistributedLock( s);
                }
            } );
        }catch (Exception e){
            logger.error("",e);
        }
        return null;
    }

    @Transactional
    @Override
    public ResultDto<String> delMbNotices(DelObjectListVO noticeInput) {
        ResultDto<String> resultDto = null;
        try {
            List<String> noticeIds = noticeInput.getCommentIds();
            noticeIds.stream().forEach(s ->{
                try {
//                    BasNotice basNotice = new BasNotice();
//                    basNotice.setId(s);
//                    basNotice.setState(-1);
//                    basNoticeService.updateById(basNotice);
                    basNoticeService.deleteById(s);
                }catch (Exception e){
                    logger.error( "id:"+s+"删除信息异常" );
                }
            });
            resultDto = ResultDto.success();
        }catch (Exception e){
            logger.error( "删除个人消息delMbNotices方法异常id="+JSON.toJSONString( noticeInput) ,e);
            resultDto = ResultDto.error( "-1","删除个人消息失败" );
        }
        return resultDto;
    }

    /**
     * 功能描述: 新增个人的礼品卡消息
     * @auther: dl.zhang
     * @date: 2019/10/23 14:36
     */
    @Override
    public ResultDto<String> addUserGiftcardNotice(DelObjectListVO delObjectListVO) {
        ResultDto<String> resultDto = null;
        try {
            String message = "";
            if(DelObjectListVO.TypeEnum.GAMEVIP.getKey() == delObjectListVO.getType()){
                message = delObjectListVO.getDays() == 0 ? MessageConstants.SYSTEM_NOTICE_GAMEVIP_ZERO : delObjectListVO.getDays() == 2 ? MessageConstants.SYSTEM_NOTICE_GAMEVIP_TWO : "";
            }else if(DelObjectListVO.TypeEnum.BAIJINVIP.getKey() == delObjectListVO.getType()){
                message = delObjectListVO.getDays() == 0 ? MessageConstants.SYSTEM_NOTICE_BAIJINVIP_ZERO : delObjectListVO.getDays() == 2 ? MessageConstants.SYSTEM_NOTICE_BAIJINVIP_TWO : "";
            }
            List<String> memberPhoneList = delObjectListVO.getCommentIds();
            List<String> listString = memberDao.getMemberIdList(memberPhoneList);
            String finalMessage = message;
            listString.parallelStream().forEach( s -> {
//                try {
                    Map<String,String> dataMap = new HashMap<>();
                    dataMap.put("actionType","1");
                    dataMap.put("targetType","1");
                    dataMap.put("userId", "0b8aba833f934452992a972b60e9ad10");
                    dataMap.put("userType", "1");
                    dataMap.put("userAvatar", "http://output-mingbo.oss-cn-beijing.aliyuncs.com/official.png");
                    dataMap.put("userName", "FunGo大助手");
                    dataMap.put("content", finalMessage );
                    dataMap.put("targetId","");
                    if(DelObjectListVO.TypeEnum.GAMEVIP.getKey() == delObjectListVO.getType()){
                        dataMap.put("targetType","13"); // 表明跳转位置 游戏VIP
                    }else if(DelObjectListVO.TypeEnum.BAIJINVIP.getKey() == delObjectListVO.getType()){
                        dataMap.put("targetType","14"); // 表明跳转位置 白金VIP
                    }
                    dataMap.put("msgTime", DateTools.fmtDate(new Date()));
                    //从DB查
//                    EntityWrapper<MemberNotice> noticeEntityWrapper = new EntityWrapper<>();
//                    noticeEntityWrapper.eq( "mb_id", s );
//                    noticeEntityWrapper.eq( "ntc_type", 7 );
//                    noticeEntityWrapper.eq( "is_read", 2 );
//                    List<MemberNotice> noticeListDB = memberNoticeDaoService.selectList( noticeEntityWrapper );
//                    if (noticeListDB != null && noticeListDB.size() > 0) {
//                        noticeListDB.stream().forEach( x -> {
//                            String jsonString = x.getNtcData();
//                            JSONObject jsonObject = JSON.parseObject( jsonString );
//                            jsonObject.put( "notice_count", (int) jsonObject.get( "notice_count" ) + 1 );
//                            jsonObject.put( "count", (int) jsonObject.get( "count" ) + 1 );
//                            x.setNtcData( jsonObject.toJSONString() );
//                            x.updateById();
//                        } );
//                    } else {
//                        MemberNotice memberNotice = new MemberNotice();
//                        int clusterIndex_i = Integer.parseInt( clusterIndex );
//                        memberNotice.setId( PKUtil.getInstance( clusterIndex_i ).longPK() );
//                        memberNotice.setIsRead( 2 );
//                        memberNotice.setNtcType( 7 );
//                        memberNotice.setMbId( s );
//                        memberNotice.setCreatedAt( new Date() );
//                        memberNotice.setUpdatedAt( new Date() );
//                        Map map = new ConcurrentHashMap( 4 );
//                        map.put( "count", 1 );
//                        map.put( "like_count", 0 );
//                        map.put( "comment_count", 0 );
//                        map.put( "notice_count", 1 );
//                        memberNotice.setNtcData( JSON.toJSONString( map ) );
//                        memberNoticeDaoService.insert( memberNotice );
//                    }
                    BasNotice basNotice = new BasNotice();
                    basNotice.setType( 6 );
                    basNotice.setIsRead( 0 );
                    basNotice.setIsPush( 0 );
                    basNotice.setMemberId( s );
                    basNotice.setCreatedAt( new Date() );
                    basNotice.setData( JSON.toJSONString(dataMap));
                    basNotice.insert();
            } );
            resultDto = ResultDto.ResultDtoFactory.buildSuccess( "新增个人的礼品卡消息成功" );
        }catch (Exception e){
            logger.error( "新增个人的礼品卡消息异常",e);
            resultDto = ResultDto.ResultDtoFactory.buildError( "新增个人的礼品卡消息异常" );
        }
        return resultDto;
    }

    public void updateNotice( String channel, List<String> memberList,String data){
        StopWatch watch = new StopWatch( "Stream效率测试" );
        watch.start( "parallelStream start" );
        memberList.parallelStream().forEach( o ->{
            try {
//                distributedLockByCurator.acquireDistributedLock( o );
                //从DB查
//                EntityWrapper<MemberNotice> noticeEntityWrapper = new EntityWrapper<>();
//                noticeEntityWrapper.eq( "mb_id", o );
//                noticeEntityWrapper.eq( "ntc_type", 7 );
//                noticeEntityWrapper.eq( "is_read", 2 );
//                List<MemberNotice> noticeListDB = memberNoticeDaoService.selectList( noticeEntityWrapper );
//                if (noticeListDB != null && noticeListDB.size() > 0) {
//                    noticeListDB.parallelStream().forEach( x -> {
//                        String jsonString = x.getNtcData();
//                        JSONObject jsonObject = JSON.parseObject( jsonString );
//                        jsonObject.put( "notice_count", (int) jsonObject.get( "notice_count" ) + 1 );
//                        jsonObject.put( "count", (int) jsonObject.get( "count" ) + 1 );
////                        jsonObject.put( "mobileType", channel );
//                        x.setNtcData( jsonObject.toJSONString());
//                        x.updateById();
//                    } );
//                } else {
//                    MemberNotice memberNotice = new MemberNotice();
//                    int clusterIndex_i = Integer.parseInt( clusterIndex );
//                    memberNotice.setId( PKUtil.getInstance( clusterIndex_i ).longPK() );
//                    memberNotice.setIsRead( 2 );
//                    memberNotice.setNtcType( 7 );
//                    memberNotice.setMbId( o );
//                    memberNotice.setCreatedAt( new Date() );
//                    memberNotice.setUpdatedAt( new Date() );
//                    Map map = new ConcurrentHashMap( 4 );
//                    map.put( "count", 1 );
//                    map.put( "like_count", 0 );
//                    map.put( "comment_count", 0 );
////                    map.put( "mobileType",channel );
//                    map.put( "notice_count", 1 );
//                    memberNotice.setNtcData( JSON.toJSONString( map ) );
//                    memberNoticeDaoService.insert( memberNotice );
//                }
                BasNotice basNotice = new BasNotice();
                basNotice.setType( 6 );
                basNotice.setChannel( channel);
                basNotice.setIsRead( 0 );
                basNotice.setIsPush( 0 );
                basNotice.setMemberId( o );
                basNotice.setCreatedAt( new Date() );
                basNotice.setData( data );
                basNotice.insert();
            }catch (Exception e){
                logger.error( "根据游戏模块版本更新系统消息异常用户id:"+o, e );
            }finally {
//                distributedLockByCurator.releaseDistributedLock(o);
            }
        } );
        watch.stop();
        logger.error("系统版本通知"+watch.prettyPrint() );
    }

    public void  updateNotice(CmmCmtReplyDto cmmCmtReplyDto1,  Map<String, Object>  map){
        CmmCmtReplyDto cmmCmtReplyDto = new CmmCmtReplyDto();
        FungoPageResultDto<CmmCmtReplyDto>  replyDtoFungoPageResultDto = null;
        if (cmmCmtReplyDto1 != null) {
            map.put( "one_level_deltype",cmmCmtReplyDto1.getState()  == -1 ? -1 : 0 );
            if(StringUtils.isNotBlank(cmmCmtReplyDto1.getReplyToContentId())){
                cmmCmtReplyDto.setId(cmmCmtReplyDto1.getReplyToContentId());
                replyDtoFungoPageResultDto = communityFeignClient.querySecondLevelCmtList(cmmCmtReplyDto);
                cmmCmtReplyDto1 =    (replyDtoFungoPageResultDto.getData() != null && replyDtoFungoPageResultDto.getData().size() >0 ) ? replyDtoFungoPageResultDto.getData().get(0) : null ;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                if (cmmCmtReplyDto1 != null && StringUtils.isNotBlank(cmmCmtReplyDto1.getId()) ) {
                    updateNotice( cmmCmtReplyDto1,map);
                    map.put("two_level_deltype", cmmCmtReplyDto1.getState() == -1 ? -1 : 0);
                }
            }else if(cmmCmtReplyDto1.getTargetType() == 5){ //社区一级评论t_cmm_message 5
                CmmCommentDto cmmCommentDto = new CmmCommentDto();
                cmmCommentDto.setId(cmmCmtReplyDto1.getTargetId());
                cmmCommentDto.setState(null);
                FungoPageResultDto<CmmCommentDto> resultDto = communityFeignClient.queryFirstLevelCmtList(cmmCommentDto);
                CmmCommentDto message =    (resultDto.getData() != null && resultDto.getData().size() >0 ) ? resultDto.getData().get(0) : null ;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                if (message != null) {
                    map.put( "parentType",cmmCmtReplyDto1.getTargetType()  );
                    map.put( "parentId",message.getPostId());
                    map.put("two_level_deltype", message.getState() == -1 ? -1 : 0);
                }
            }else if(cmmCmtReplyDto1.getTargetType() == 6){  //游戏评测 t_game_evation 6
                GameEvaluationDto param = new GameEvaluationDto();
                param.setId(cmmCmtReplyDto1.getTargetId());
                FungoPageResultDto<GameEvaluationDto>  resultDto = gamesFeignClient.getGameEvaluationPage(param);
                GameEvaluationDto gameEvaluationDto = (resultDto.getData() != null && resultDto.getData().size() > 0 ) ? resultDto.getData().get(0) : null;
                if(gameEvaluationDto != null){
                    map.put( "parentType",cmmCmtReplyDto1.getTargetType()  );
                    map.put( "parentId",gameEvaluationDto.getGameId() );
                    map.put("two_level_deltype", gameEvaluationDto.getState() == -1 ? -1 : 0);
                }
            }else if(cmmCmtReplyDto1.getTargetType() == 8){ //心情评论  t_moo_message 8
                MooMessageDto mooMessageDto = new MooMessageDto();
                mooMessageDto.setId(cmmCmtReplyDto1.getTargetId());
                mooMessageDto.setState(null);
                FungoPageResultDto<MooMessageDto> resultDto = communityFeignClient.queryCmmMoodCommentList(mooMessageDto);
                MooMessageDto message =    (resultDto.getData() != null && resultDto.getData().size() >0 ) ? resultDto.getData().get(0) : null ;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                if (message != null) {
                    map.put( "parentType",cmmCmtReplyDto1.getTargetType()  );
                    map.put( "parentId",message.getMoodId()  );
                    map.put("two_level_deltype", message.getState() == -1 ? -1 : 0);
                }
            }
        }
    }


    //--------
}
