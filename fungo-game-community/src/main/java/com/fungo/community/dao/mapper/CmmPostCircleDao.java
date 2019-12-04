package com.fungo.community.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.community.entity.CmmPost;
import com.fungo.community.entity.TCmmPostCircle;
import com.game.common.bean.CollectionBean;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 社区帖子 Mapper 接口
 * </p>
 *
 * @author Carlos
 * @since 2018-04-20
 */
public interface CmmPostCircleDao extends BaseMapper<TCmmPostCircle> {

}