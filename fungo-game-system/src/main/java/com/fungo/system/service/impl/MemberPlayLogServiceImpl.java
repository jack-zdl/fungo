package com.fungo.system.service.impl;

import com.fungo.system.dao.MemberPlayLogDao;
import com.fungo.system.entity.MemberPlayLog;
import com.fungo.system.service.MemberPlayLogService;
import com.game.common.dto.ResultDto;
import com.game.common.vo.MemberPlayLogVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/10/22
 */
@Service
public class MemberPlayLogServiceImpl implements MemberPlayLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger( MemberPlayLogServiceImpl.class);

    @Autowired
    private MemberPlayLogDao memberPlayLogDao;

    /**
     * 功能描述: 保存用户支付日志
     * @date: 2019/10/22 10:44
     */
    @Override
    public ResultDto<String> saveMemberPalyLog(MemberPlayLogVO memberPlayLogVO) {
        ResultDto<String> resultDto = null;
        try {
            MemberPlayLog param = new MemberPlayLog();
            param.setPlayId( memberPlayLogVO.getPlayId());
            MemberPlayLog memberPlay = memberPlayLogDao.selectOne( param);
            if(memberPlay != null && StringUtils.isNotBlank(memberPlay.getId())){
                resultDto = ResultDto.ResultDtoFactory.buildSuccess( "用户支付日志重复记录" );
                return resultDto;
            }
            MemberPlayLog memberPlayLog = new MemberPlayLog();
            memberPlayLog.setMemberPhone(memberPlayLogVO.getMemberPhone() );
            memberPlayLog.setPlayId(memberPlayLogVO.getPlayId()  );
            memberPlayLog.setBusinessId(memberPlayLogVO.getBusinessId() );
            memberPlayLog.setCouponType(memberPlayLogVO.getCouponType()  );
            memberPlayLog.setCouponId( memberPlayLogVO.getCouponId() );
            memberPlayLog.setPayType(memberPlayLogVO.getPayType()  );
            memberPlayLog.setIsactive("1");
            memberPlayLog.setCreatedAt( new Date());
            memberPlayLog.setUpdatedAt(new Date());
            memberPlayLog.setRversion(1);
            memberPlayLog.setDescription(memberPlayLogVO.getDescription());
            memberPlayLog.insert();
            resultDto = ResultDto.ResultDtoFactory.buildSuccess( "保存用户支付日志" );
        }catch (Exception e){
            LOGGER.error( "保存用户支付日志",e );
            resultDto = ResultDto.ResultDtoFactory.buildError( "保存用户支付日志异常" );
        }
        return resultDto;
    }
}
