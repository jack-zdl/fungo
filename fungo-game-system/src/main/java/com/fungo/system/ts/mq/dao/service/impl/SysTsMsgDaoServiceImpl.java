package com.fungo.system.ts.mq.dao.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.system.ts.mq.dao.mapper.SysTsMsgDao;
import com.fungo.system.ts.mq.dao.service.SysTsMsgDaoService;
import com.fungo.system.ts.mq.entity.TransactionMessageDomain;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分布式事务之数据消息表 服务实现类
 * </p>
 *
 * @author mxf
 * @since 2019-05-14
 */
@Service
public class SysTsMsgDaoServiceImpl extends ServiceImpl<SysTsMsgDao, TransactionMessageDomain> implements SysTsMsgDaoService {
	
}
