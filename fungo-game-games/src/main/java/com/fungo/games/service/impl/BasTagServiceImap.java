package com.fungo.games.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.games.dao.BasTagDao;
import com.fungo.games.entity.BasTag;
import com.fungo.games.service.BasTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    private BasTagDao basTagDao;

    @Override
    public List<BasTag> listGroupTagOrderBySortAndDownLoad(String groupId){
        return basTagDao.listGroupTagOrderBySortAndDownLoad(groupId);
    }
}
