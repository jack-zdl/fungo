package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.system.dao.DeveloperDao;
import com.fungo.system.entity.Developer;
import com.fungo.system.service.DeveloperService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 开发者信息 服务实现类
 * </p>
 *
 * @author lzh
 * @since 2018-06-08
 */
@Service
public class DeveloperServiceImap extends ServiceImpl<DeveloperDao, Developer> implements DeveloperService {
	
}
