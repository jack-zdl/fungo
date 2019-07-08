package com.fungo.system.mall.daoService.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.system.mall.daoService.MallOrderDaoService;
import com.fungo.system.mall.entity.MallOrder;
import com.fungo.system.mall.mapper.MallOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author mxf
 * @since 2019-01-14
 */
@Service
public class MallOrderDaoServiceImpl extends ServiceImpl<MallOrderDao, MallOrder> implements MallOrderDaoService {


    @Autowired
    private MallOrderDao mallOrderDao;

    /**
     * 用户兑换的游戏礼包查询
     * @param mbId
     * @param startOffset
     * @param pageSize
     * @return
     */
    @Override
    public List<Map<String, Object>> queryMbGameOrderList(String mbId, Integer startOffset,
                                                          Integer pageSize) {
        return mallOrderDao.queryMbGameOrderList(mbId, startOffset, pageSize);
    }

    /**
     * 用户兑换的游戏礼包总数查询
     * @param mbId
     * @param startOffset
     * @param pageSize
     * @return
     */
    @Override
    public Map<String, Object> queryMbGameOrderListCount(String mbId, Integer startOffset,
                                                         Integer pageSize) {
        return mallOrderDao.queryMbGameOrderListCount(mbId, startOffset, pageSize);
    }
//---
}
