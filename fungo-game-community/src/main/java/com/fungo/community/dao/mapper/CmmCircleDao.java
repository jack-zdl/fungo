package com.fungo.community.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.community.entity.CmmCircle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * <p>
 * 社区 Mapper 接口
 * </p>
 *
 * @author Carlos
 * @since 2018-06-28
 */
@Repository
public interface CmmCircleDao extends BaseMapper<CmmCircle> {

    List<CmmCircle> selectCmmCommunityByBrowse(@Param("ids") List<String> ids);

}