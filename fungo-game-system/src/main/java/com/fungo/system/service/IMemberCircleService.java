package com.fungo.system.service;

import com.fungo.system.entity.MemberCircle;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2020/1/3
 */
public interface IMemberCircleService {

    MemberCircle getCircleMainByMemberId(String circleId);
}
