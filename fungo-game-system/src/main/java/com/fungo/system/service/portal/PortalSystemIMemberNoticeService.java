package com.fungo.system.service.portal;

import com.fungo.system.dto.MemberNoticeInput;

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
public interface PortalSystemIMemberNoticeService {

    /**
     * 查询用户的消息数据
     * @param noticeInput 用户查询消息数据封装
     * @return
     */
    public List<Map<String,Object>> queryMbNotices(MemberNoticeInput noticeInput);



}
