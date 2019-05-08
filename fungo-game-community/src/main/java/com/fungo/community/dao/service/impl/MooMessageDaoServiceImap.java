package com.fungo.community.dao.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.community.dao.mapper.MooMessageDao;
import com.fungo.community.dao.service.MooMessageDaoService;
import com.fungo.community.entity.MooMessage;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 心情评论 服务实现类
 * </p>
 *
 * @author lzh
 * @since 2018-04-20
 */
@Service
public class MooMessageDaoServiceImap extends ServiceImpl<MooMessageDao, MooMessage> implements MooMessageDaoService {
	
}
