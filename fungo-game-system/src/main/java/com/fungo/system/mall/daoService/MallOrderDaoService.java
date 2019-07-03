package com.fungo.system.mall.daoService;

import com.baomidou.mybatisplus.service.IService;
import com.fungo.system.mall.entity.MallOrder;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author mxf
 * @since 2019-01-14
 */
public interface MallOrderDaoService extends IService<MallOrder> {


    /**
     * 用户兑换的游戏礼包查询
     * @param mbId
     * @param startOffset
     * @param pageSize
     * @return
     */
    public List<Map<String, Object>> queryMbGameOrderList(String mbId, Integer startOffset,
                                                          Integer pageSize);

    /**
     * 用户兑换的游戏礼包总数查询
     * @param mbId
     * @param startOffset
     * @param pageSize
     * @return
     */
    public Map<String, Object> queryMbGameOrderListCount(String mbId, Integer startOffset,
                                                         Integer pageSize);

}
