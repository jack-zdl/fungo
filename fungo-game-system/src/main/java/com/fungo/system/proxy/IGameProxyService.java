package com.fungo.system.proxy;

import java.util.Map;

/**
 * <p>GameProxyImpl 对接外部接口</p>
 * @Author: dl.zhang
 * @Date: 2019/5/13
 */
public interface IGameProxyService {

    String getMemberIdByTargetId(Map<String, String> map);
}
