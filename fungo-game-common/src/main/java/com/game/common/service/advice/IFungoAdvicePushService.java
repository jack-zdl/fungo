package com.game.common.service.advice;


import com.game.common.bean.advice.BasNoticeDto;

/**
 * <p>
 *      消息推送业务
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public interface IFungoAdvicePushService {


    /**
     * 添加通知消息数据
     * @param noticeDto
     * @return
     */
    public boolean addNotice(BasNoticeDto noticeDto) throws Exception;


}
