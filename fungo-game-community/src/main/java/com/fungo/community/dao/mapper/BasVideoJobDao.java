package com.fungo.community.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.community.entity.BasVideoJob;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  * 压缩视频 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-11-08
 */
public interface BasVideoJobDao extends BaseMapper<BasVideoJob> {

    List<BasVideoJob> getBasVideoJobByPostIds(@Param("type")String type,  @Param("postIds")List<String> postIds);

}