package com.fungo.system.proxy;

import com.game.common.dto.community.CmmPostDto;

/**
 * <p>MerberServiceImpl的对外接口层</p>
 * @Author: dl.zhang
 * @Date: 2019/5/15
 */
public interface IMemeberProxyService {

    CmmPostDto selectCmmPost(String id);
}
