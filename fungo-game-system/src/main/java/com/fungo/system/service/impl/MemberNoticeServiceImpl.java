package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.dto.MemberNoticeInput;
import com.fungo.system.entity.MemberNotice;
import com.fungo.system.service.IMemberNoticeService;
import com.fungo.system.service.MemberNoticeDaoService;
import com.game.common.repo.cache.facade.FungoCacheNotice;
import com.game.common.util.PKUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MemberNoticeServiceImpl implements IMemberNoticeService {

    private static final Logger logger = LoggerFactory.getLogger(MemberNoticeServiceImpl.class);


    @Autowired
    private MemberNoticeDaoService memberNoticeDaoService;


    @Autowired
    private FungoCacheNotice fungoCacheNotice;

    @Value("${sys.config.fungo.cluster.index}")
    private String clusterIndex;


    @Override
    public List<Map<String, Object>> queryMbNotices(MemberNoticeInput noticeInput) {

        List<Map<String, Object>> noticesList = null;
        //从redis缓存，获取
        String mb_id = noticeInput.getMb_id();
        Integer ntc_type = noticeInput.getNtc_type();
        String keyPrefix = FungoCacheNotice.REDIS_MEMBER_NOTICE + mb_id + String.valueOf(ntc_type.intValue());

        try {

            logger.info("queryMbNotices-input:{}", JSON.toJSONString(noticeInput));

            //从redis中查询出 某个用户某种类型的消息 key集合
            Set<Object> keySet = fungoCacheNotice.findKeysWithOutSecurity(keyPrefix, "");

            List<String> noticeIdList = new ArrayList<String>();

            if (null != keySet) {

                noticesList = new ArrayList<Map<String, Object>>();

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

                noticesList = new ArrayList<Map<String, Object>>();

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
            String keyPrefix = FungoCacheNotice.REDIS_MEMBER_NOTICE + mb_id + String.valueOf(ntc_type.intValue());
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

    //--------
}
