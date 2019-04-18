package com.game.common.service.advice.impl;


import com.game.common.bean.advice.BasNoticeDto;
import com.game.common.entity.BasNotice;
import com.game.common.service.BasNoticeService;
import com.game.common.service.advice.IFungoAdvicePushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FungoAdvicePushServiceImpl implements IFungoAdvicePushService {


    private static final Logger logger = LoggerFactory.getLogger(FungoAdvicePushServiceImpl.class);

    @Autowired
    private BasNoticeService noticeService;

    @Override
    public boolean addNotice(BasNoticeDto noticeDto) throws Exception {

        logger.info("FungoAdvicePushServiceImpl-addNotice:{}", noticeDto.toString());

        BasNotice notice = new BasNotice();

        notice.setMemberId(noticeDto.getMemberId());
        notice.setData(noticeDto.getData());
        notice.setIsRead(0);
        notice.setType(noticeDto.getType());

        notice.setCreatedAt(new Date());
        notice.setUpdatedAt(new Date());
        return noticeService.insert(notice);
    }
}
