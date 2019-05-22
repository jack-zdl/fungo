package com.fungo.community.dao.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.community.dao.mapper.CmmPostDao;
import com.fungo.community.dao.service.CmmPostDaoService;
import com.fungo.community.entity.CmmPost;
import com.game.common.bean.CollectionBean;
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




    @Override
    public List<String> getRecommendMembersFromCmmPost(long ccnt, long limitSize, List<String> wathMbsSet) {
        return cmmPostDao.getRecommendMembersFromCmmPost(ccnt, limitSize, wathMbsSet);
    }

    @Override
    public List<CollectionBean> getCollection(Page<CollectionBean> page, List<String> postIds) {
        return cmmPostDao.getCollection(page, postIds);
    }
    //------
}
