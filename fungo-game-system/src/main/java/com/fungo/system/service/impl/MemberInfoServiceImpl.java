package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.system.dao.MemberInfoDao;
import com.fungo.system.dao.MemberNoDao;
import com.fungo.system.entity.MemberInfo;
import com.fungo.system.entity.MemberNo;
import com.fungo.system.mall.service.impl.FungoMallGoodsServiceImpl;
import com.fungo.system.service.MemberInfoService;
import com.fungo.system.service.MemberNoService;
import com.game.common.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/8/26
 */
@Service
public class MemberInfoServiceImpl extends ServiceImpl<MemberInfoDao, MemberInfo> implements MemberInfoService {

    private static final Logger logger = LoggerFactory.getLogger( MemberInfoServiceImpl.class);

    @Autowired
    private MemberInfoDao memberInfoDao;

    /**
     * 功能描述:  查询用户是否分享中秋
     * @param: [memberId] 用户id
     * @return: boolean
     * @auther: dl.zhang
     * @date: 2019/8/26 17:30
     */
    @Override
    public boolean shareFestival(String memberId) {
        try {
            MemberInfo memberInfo = new MemberInfo();
            memberInfo.setMdId( memberId);
            memberInfo.setIsactive("1");
            memberInfo = memberInfoDao.selectOne( memberInfo);
            return memberInfo != null;
        }catch (Exception e){
            logger.error("查询用户是否分享中秋异常",e);
        }
        return false;
    }




    @Override
    public boolean delFestival(String memberId) {
        try {
            MemberInfo memberInfo = new MemberInfo();
            memberInfo.setMdId( memberId);
            memberInfo.setIsactive("1");
            memberInfo = memberInfoDao.selectOne( memberInfo);
            memberInfo.setIsactive("0");
            return memberInfo.updateById();
        }catch (Exception e){
            logger.error("查询用户是否分享中秋异常",e);
        }
        return false;
    }
}
