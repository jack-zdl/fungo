package com.fungo.system.service.portal;

import com.game.common.dto.AuthorBean;

/**
 * <p>
 *
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/6/11 14:29
 */
public interface PortalSystemIUserService {
    //用户信息
    AuthorBean getUserCard(String cardId, String memberId);
}
