package com.fungo.system.service;

import com.fungo.system.dto.MemberNoticeInput;
import com.game.common.dto.ResultDto;
import com.game.common.vo.DelObjectListVO;

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



    void updateSystemByGame() throws Exception;



    void insertSystemNotice(String mobileType, String memberId,String data) throws Exception;

    void insertSystemVersionNotice(String mobileType, String data) throws Exception;

    /**
     * 查询用户的消息数据
     * @param noticeInput 用户查询消息数据封装
     * @return
     */
    List<Map<String,Object>> insertMbNotices(MemberNoticeInput noticeInput);

    ResultDto<String> delMbNotices(DelObjectListVO noticeInput);

    ResultDto<String> addUserGiftcardNotice(DelObjectListVO delObjectListVO);


}
