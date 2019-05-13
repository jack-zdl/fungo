package com.fungo.community.dao.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.community.dao.mapper.CmmPostDao;
import com.fungo.community.dao.service.CmmPostDaoService;
import com.fungo.community.entity.CmmPost;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 社区帖子 服务实现类
 * </p>
 *
 * @author lzh
 * @since 2018-04-20
 */
@Service
public class CmmPostDaoServiceImap extends ServiceImpl<CmmPostDao, CmmPost> implements CmmPostDaoService {


    @Autowired
    private CmmPostDao cmmPostDao;


    /**
     * 精品帖子数大于2的用户
     * @return
     */
    @Override
    public List<Map> getHonorQualificationOfEssencePost() {
        return cmmPostDao.getHonorQualificationOfEssencePost();
    }
    //------
}
