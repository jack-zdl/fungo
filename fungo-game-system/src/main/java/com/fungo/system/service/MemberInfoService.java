package com.fungo.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.fungo.system.entity.MemberInfo;

/**
 * <p>
 * 广告 服务类
 * </p>
 *
 * @author lzh
 * @since 2018-06-22
 */
public interface MemberInfoService extends IService<MemberInfo> {

    boolean shareFestival(String memberId);
}
