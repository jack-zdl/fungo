package com.fungo.community.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.community.entity.MooMood;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
  * 心情 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-06-29
 */
@Repository
public interface MooMoodDao extends BaseMapper<MooMood> {


    int updateMooMoodCommentNum(@Param("commentId") String commentId);

}