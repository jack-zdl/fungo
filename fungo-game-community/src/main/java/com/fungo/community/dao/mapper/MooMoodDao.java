package com.fungo.community.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.community.entity.MooMood;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
  * 心情 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-06-29
 */
public interface MooMoodDao extends BaseMapper<MooMood> {


    int updateMooMoodCommentNum(@Param("commentId") String commentId);

}