package com.game.common.mall.daoService.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.game.common.mall.daoService.MallSeckillDaoService;
import com.game.common.mall.entity.MallSeckill;
import com.game.common.mall.mapper.MallSeckillDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城秒杀信息表 服务实现类
 * </p>
 *
 * @author mxf
 * @since 2019-01-14
 */
@Service
public class MallSeckillDaoServiceImpl extends ServiceImpl<MallSeckillDao, MallSeckill> implements MallSeckillDaoService {


    @Autowired
    private  MallSeckillDao mallSeckillDao;

    @Override
    public List<Map<String, Object>> querySeckillGoods(String startTime, String endTime) throws Exception {
        return mallSeckillDao.querySeckillGoods(startTime, endTime);
    }

}
