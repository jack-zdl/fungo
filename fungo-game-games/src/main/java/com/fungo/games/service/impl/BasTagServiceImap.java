package com.fungo.games.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.games.dao.BasTagDao;
import com.fungo.games.entity.BasTag;
import com.fungo.games.service.BasTagService;
import com.game.common.bean.TagBean;
import com.game.common.dto.ResultDto;
import com.game.common.dto.game.BasTagDto;
import com.game.common.dto.game.BasTagGroupDto;
import com.game.common.util.CommonUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 标签 服务实现类
 * </p>
 *
 * @author lzh
 * @since 2018-05-09
 */
@Service
public class BasTagServiceImap extends ServiceImpl<BasTagDao, BasTag> implements BasTagService {

}
