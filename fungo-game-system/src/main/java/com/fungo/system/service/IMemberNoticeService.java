package com.fungo.system.service;

import com.fungo.system.dto.MemberNoticeInput;
import com.fungo.system.entity.MemberNotice;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *      用户消息业务层
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public interface IMemberNoticeService {

    /**
     * 查询用户的消息数据
     * @param noticeInput 用户查询消息数据封装
     * @return
     */
    List<Map<String,Object>> queryMbNotices(MemberNoticeInput noticeInput);


    /**
     * 删除用户的消息数据
     * @param mb_id 用户id
     * @param ntc_type 消息类型
     * @return
     */
    void deleteMbNotices(String mb_id, Integer ntc_type, Long noticeId);


    /**
     * 添加用户的消息数据
     * @param mb_id 用户id
     * @param ntc_type 消息类型
     * @param ntcData 消息数据
     * @return
     */
    void addMbNotice(String mb_id, Integer ntc_type, Object ntcData);


    /**修改用户的消息数据
     *
     * @param memberNotice 用户消息数据实体
     */
    void updateMbNotice(MemberNotice memberNotice);


    void updateSystemByGame() throws Exception;


    void insertSystemNotice(String memberId,String data) throws Exception;


}
