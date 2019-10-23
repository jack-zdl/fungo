package com.fungo.games.service;

import com.baomidou.mybatisplus.service.IService;
import com.fungo.games.entity.BasTag;

import java.util.List;

/**
 * <p>
 * 标签 服务类
 * </p>
 *
 * @author lzh
 * @since 2018-05-09
 */
public interface BasTagService extends IService<BasTag> {

    List<BasTag> listGroupTagOrderBySortAndDownLoad(String groupId);
}
