package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.dto.MemberNoticeInput;
import com.fungo.system.entity.BasNotice;
import com.fungo.system.entity.Member;
import com.fungo.system.entity.MemberNotice;
import com.fungo.system.feign.GamesFeignClient;
import com.fungo.system.helper.mq.MQProduct;
import com.fungo.system.helper.zookeeper.DistributedLockByCurator;
import com.fungo.system.service.BasNoticeService;
import com.fungo.system.service.IMemberNoticeService;
import com.fungo.system.service.MemberNoticeDaoService;
import com.fungo.system.service.MemberNoticeDaoServiceImpl;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.game.GameSurveyRelDto;
import com.game.common.repo.cache.facade.FungoCacheNotice;
import com.game.common.util.PKUtil;
import com.game.common.util.StringUtil;
import com.game.common.util.UUIDUtils;
import com.game.common.util.date.DateTools;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    @Override
    public List<Map<String, Object>> queryMbNotices(MemberNoticeInput noticeInput) {

        List<Map<String, Object>> noticesList = null;
        //从redis缓存，获取
        String mb_id = noticeInput.getMb_id();
        Integer ntc_type = noticeInput.getNtc_type();
        String keyPrefix = FungoCoreApiConstant.REDIS_MEMBER_NOTICE + mb_id + String.valueOf(ntc_type.intValue());

        try {

            logger.info("queryMbNotices-input:{}", JSON.toJSONString(noticeInput));

            //从redis中查询出 某个用户某种类型的消息 key集合
            Set<Object> keySet = fungoCacheNotice.findKeysWithOutSecurity(keyPrefix, "");

            List<String> noticeIdList = new ArrayList<>();

            if (null != keySet) {

                noticesList = new ArrayList<>();

                for (Object o : keySet) {
                    String key = (String) o;

                    Map<String, Object> msgMap = (Map<String, Object>) fungoCacheNotice.getIndexCache(key);

                    noticesList.add(msgMap);

                    //同时把DB中的消息状态置为已读
                    String[] keys = key.split("_");
                    if (keys.length >= 2) {
                        String noticeId = keys[keys.length-1];
                        noticeIdList.add(noticeId);
                    }
                }
                logger.info("queryMbNotices-redis-cache-keys:{}--value:{}", JSON.toJSONString(keySet), JSON.toJSONString(noticesList));
            }

            if (null != noticesList && !noticesList.isEmpty()) {
                //修改消息状态
                updateNotes(mb_id, noticeIdList);
                return noticesList;
            }
            //从DB查
            EntityWrapper<MemberNotice> noticeEntityWrapper = new EntityWrapper<>();
            noticeEntityWrapper.eq("mb_id", mb_id);
            noticeEntityWrapper.eq("ntc_type", ntc_type);
            noticeEntityWrapper.eq("is_read", 2);

            List<MemberNotice> noticeListDB = memberNoticeDaoService.selectList(noticeEntityWrapper);
            if (null != noticeListDB && !noticeListDB.isEmpty()) {

                noticesList = new ArrayList<>();

                for (MemberNotice memberNotice : noticeListDB) {

                    String ntcDataJsonStr = memberNotice.getNtcData();
                    Map<String, Object> msgMap = JSON.parseObject(ntcDataJsonStr);
                    noticesList.add(msgMap);
                    noticeIdList.add(String.valueOf(memberNotice.getId()));
                }
                logger.info("queryMbNotices-DB:{}", JSON.toJSONString(noticesList));
                //修改消息状态
                updateNotes(mb_id, noticeIdList);
            }

        } catch (Exception ex) {
            logger.error("查询用户的消息数据出现异常:", ex);
            ex.printStackTrace();
        } finally {
            //清除redis缓存
            fungoCacheNotice.excIndexCacheWithOutSecurity(false, keyPrefix, "", null);
        }

        return noticesList;
    }


    /**
     * 修改消息状态为已读
     */
    private void updateNotes(String mb_id, List<String> noticeIdList) {
        for (String noticeId : noticeIdList) {
            MemberNotice memberNotice = new MemberNotice();
            memberNotice.setMbId(mb_id);
            memberNotice.setId(Long.parseLong(noticeId));
            updateMbNotice(memberNotice);
        }
    }


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


    @Override
    public void addMbNotice(String mb_id, Integer ntc_type, Object ntcData) {

        try {
            if (StringUtils.isBlank(mb_id)) {
                return;
            }
            if (null == ntcData) {
                return;
            }

            int clusterIndex_i = Integer.parseInt(clusterIndex);

            MemberNotice memberNotice = new MemberNotice();
            memberNotice.setId(PKUtil.getInstance(clusterIndex_i).longPK());
            memberNotice.setIsRead(2);
            memberNotice.setNtcType(ntc_type);
            memberNotice.setMbId(mb_id);
            memberNotice.setCreatedAt(new Date());
            memberNotice.setUpdatedAt(new Date());

            memberNotice.setNtcData(JSON.toJSONString(ntcData));

            memberNoticeDaoService.insert(memberNotice);

            //同时保存到Redis缓存
            String keyPrefix = FungoCoreApiConstant.REDIS_MEMBER_NOTICE + mb_id + String.valueOf(ntc_type.intValue());
            fungoCacheNotice.excIndexCacheWithOutSecurity(true, keyPrefix, String.valueOf(memberNotice.getId()), ntcData);

        } catch (Exception ex) {
            logger.error("添加用户的消息数据出现异常:", ex);
            ex.printStackTrace();
        }
    }

    @Override
    public void updateMbNotice(MemberNotice memberNotice) {
        try {

            if (null == memberNotice) {
                return;
            }
            if (StringUtils.isBlank(memberNotice.getMbId())) {
                return;
            }

            if (null == memberNotice.getId() || memberNotice.getId().longValue() <= 0) {
                return;
            }
            MemberNotice updateMemberNotice = new MemberNotice();
            updateMemberNotice.setIsRead(1);
            updateMemberNotice.setId(memberNotice.getId());
            updateMemberNotice.updateById();

        } catch (Exception ex) {
            logger.error("修改用户的消息数据出现异常:", ex);
            ex.printStackTrace();
        }
    }

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
                        distributedLockByCurator.acquireDistributedLock( s.getMemberId() );
                        //从DB查
                        EntityWrapper<MemberNotice> noticeEntityWrapper = new EntityWrapper<>();
                        noticeEntityWrapper.eq( "mb_id", s.getMemberId() );
                        noticeEntityWrapper.eq( "ntc_type", 7 );
                        noticeEntityWrapper.eq( "is_read", 2 );
                        List<MemberNotice> noticeListDB = memberNoticeDaoService.selectList( noticeEntityWrapper );
                        if (noticeListDB != null) {
                            noticeListDB.parallelStream().forEach( x -> {
                                String jsonString = x.getNtcData();
                                JSONObject jsonObject = JSON.parseObject( jsonString );
                                jsonObject.put( "notice_count", (int) jsonObject.get( "notice_count" ) + 1 );
                                jsonObject.put( "count", (int) jsonObject.get( "count" ) + 1 );
                                x.updateById();
                            } );
                        } else {
                            MemberNotice memberNotice = new MemberNotice();
                            int clusterIndex_i = Integer.parseInt( clusterIndex );
                            memberNotice.setId( PKUtil.getInstance( clusterIndex_i ).longPK() );
                            memberNotice.setIsRead( 2 );
                            memberNotice.setNtcType( 7 );
                            memberNotice.setMbId( s.getMemberId() );
                            memberNotice.setCreatedAt( new Date() );
                            memberNotice.setUpdatedAt( new Date() );
                            Map map = new ConcurrentHashMap( 4 );
                            map.put( "count", 1 );
                            map.put( "like_count", 0 );
                            map.put( "comment_count", 0 );
                            map.put( "notice_count", 1 );
                            memberNotice.setNtcData( JSON.toJSONString( map ) );
                            memberNoticeDaoService.insert( memberNotice );
                        }
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
                    } finally {
                        distributedLockByCurator.releaseDistributedLock( s.getMemberId() );
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
    public void insertSystemNotice (String memberId,String data) throws Exception {
            try {
                distributedLockByCurator.acquireDistributedLock( memberId );
                //从DB查
                EntityWrapper<MemberNotice> noticeEntityWrapper = new EntityWrapper<>();
                noticeEntityWrapper.eq( "mb_id", memberId );
                noticeEntityWrapper.eq( "ntc_type", 7 );
                noticeEntityWrapper.eq( "is_read", 2 );
                List<MemberNotice> noticeListDB = memberNoticeDaoService.selectList( noticeEntityWrapper );
                if (noticeListDB != null && noticeListDB.size() > 0) {
                    noticeListDB.parallelStream().forEach( x -> {
                        String jsonString = x.getNtcData();
                        JSONObject jsonObject = JSON.parseObject( jsonString );
                        jsonObject.put( "notice_count", (int) jsonObject.get( "notice_count" ) + 1 );
                        jsonObject.put( "count", (int) jsonObject.get( "count" ) + 1 );
                        x.setNtcData( jsonObject.toJSONString());
                        x.updateById();
                    } );
                } else {
                    MemberNotice memberNotice = new MemberNotice();
                    int clusterIndex_i = Integer.parseInt( clusterIndex );
                    memberNotice.setId( PKUtil.getInstance( clusterIndex_i ).longPK() );
                    memberNotice.setIsRead( 2 );
                    memberNotice.setNtcType( 7 );
                    memberNotice.setMbId( memberId );
                    memberNotice.setCreatedAt( new Date() );
                    memberNotice.setUpdatedAt( new Date() );
                    Map map = new ConcurrentHashMap( 4 );
                    map.put( "count", 1 );
                    map.put( "like_count", 0 );
                    map.put( "comment_count", 0 );
                    map.put( "notice_count", 1 );
                    memberNotice.setNtcData( JSON.toJSONString( map ) );
                    memberNoticeDaoService.insert( memberNotice );
                }
                BasNotice basNotice = new BasNotice();
                basNotice.setType( 6 );
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
                distributedLockByCurator.releaseDistributedLock(memberId);
            }
        }

    /**
     * 功能描述: 给某篇文章置顶或者推荐发送消息
     * @return: void
     * @auther: dl.zhang
     * @date: 2019/7/31 9:34
     */
    @Transactional
    public void insertSystemVersionNotice (String data) throws Exception {
        try {
            Member member = new Member();
            EntityWrapper<Member> memberEntity = new EntityWrapper<>();
            memberEntity.eq( "state","0" );
            List<Member> memberList = memberServiceImap.selectList( memberEntity);
            memberList.stream().forEach( o ->{
                try {
                    distributedLockByCurator.acquireDistributedLock( o.getId() );
                    //从DB查
                    EntityWrapper<MemberNotice> noticeEntityWrapper = new EntityWrapper<>();
                    noticeEntityWrapper.eq( "mb_id", o.getId() );
                    noticeEntityWrapper.eq( "ntc_type", 7 );
                    noticeEntityWrapper.eq( "is_read", 2 );
                    List<MemberNotice> noticeListDB = memberNoticeDaoService.selectList( noticeEntityWrapper );
                    if (noticeListDB != null && noticeListDB.size() > 0) {
                        noticeListDB.parallelStream().forEach( x -> {
                            String jsonString = x.getNtcData();
                            JSONObject jsonObject = JSON.parseObject( jsonString );
                            jsonObject.put( "notice_count", (int) jsonObject.get( "notice_count" ) + 1 );
                            jsonObject.put( "count", (int) jsonObject.get( "count" ) + 1 );
                            x.setNtcData( jsonObject.toJSONString());
                            x.updateById();
                        } );
                    } else {
                        MemberNotice memberNotice = new MemberNotice();
                        int clusterIndex_i = Integer.parseInt( clusterIndex );
                        memberNotice.setId( PKUtil.getInstance( clusterIndex_i ).longPK() );
                        memberNotice.setIsRead( 2 );
                        memberNotice.setNtcType( 7 );
                        memberNotice.setMbId( o.getId() );
                        memberNotice.setCreatedAt( new Date() );
                        memberNotice.setUpdatedAt( new Date() );
                        Map map = new ConcurrentHashMap( 4 );
                        map.put( "count", 1 );
                        map.put( "like_count", 0 );
                        map.put( "comment_count", 0 );
                        map.put( "notice_count", 1 );
                        memberNotice.setNtcData( JSON.toJSONString( map ) );
                        memberNoticeDaoService.insert( memberNotice );
                    }
                    BasNotice basNotice = new BasNotice();
                    basNotice.setType( 6 );
                    basNotice.setIsRead( 0 );
                    basNotice.setIsPush( 0 );
                    basNotice.setMemberId( o.getId() );
                    basNotice.setCreatedAt( new Date() );
                    basNotice.setData( data );
                    basNotice.insert();
                }catch (Exception e){
                    logger.error( "根据游戏模块版本更新系统消息异常用户id:"+o.getId(), e );
                }finally {
                    distributedLockByCurator.releaseDistributedLock(o.getId());
                }
            } );
        } catch (Exception e) {
            logger.error( "根据游戏模块更新系统消息异常", e );
            throw new Exception( "根据游戏模块更新系统消息异常" );
        }
    }


    //--------
}
