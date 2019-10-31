package com.fungo.games.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.games.entity.FindCollectionGroup;
import com.game.common.bean.CollectionItemBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  * 游戏标签 Mapper 接口
 * </p>
 *
 * @author Carlos
 * @since 2018-05-09
 */
public interface FindCollectionGroupDao extends BaseMapper<FindCollectionGroup> {

    //管控台新游信息查询
    public List<CollectionItemBean> getCollectionItemAll(@Param("formId")String formId);
}