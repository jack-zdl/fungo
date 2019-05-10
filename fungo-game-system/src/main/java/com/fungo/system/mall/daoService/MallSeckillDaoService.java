package com.fungo.system.mall.daoService;

import com.baomidou.mybatisplus.service.IService;
import com.fungo.system.mall.entity.MallSeckill;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城秒杀信息表 服务类
 * </p>
 *
 * @author mxf
 * @since 2019-01-14
 */
public interface MallSeckillDaoService extends IService<MallSeckill> {

    /**
     * 查询秒杀的商品
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public List<Map<String,Object>> querySeckillGoods(String startTime, String endTime) throws Exception;

}
