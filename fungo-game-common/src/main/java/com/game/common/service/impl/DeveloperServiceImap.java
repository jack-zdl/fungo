package com.game.common.service.impl;

import com.game.common.entity.Developer;
import com.game.common.mapper.DeveloperDao;
import com.game.common.service.DeveloperService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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
