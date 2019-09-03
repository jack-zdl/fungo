package com.fungo.system.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.entity.MallSeckillOrder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MallSeckillOrderDao extends BaseMapper<MallSeckillOrder> {

    List<MallSeckillOrder> getMallSeckillOrderByActive(Page page);
}