package com.fungo.system.service.portal;

import com.fungo.system.dto.FollowInptPageDao;
import com.game.common.api.InputPageDto;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;

import java.util.Map;

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

    /**
     * 获取用户关注
     * @param memberId 当前登录用户id
     * @param memberId2 目标用户id
     * @param inputPage
     * @return
     * @throws Exception
     */
    FungoPageResultDto<Map<String,Object>> getFollower(String memberId, String memberId2, FollowInptPageDao inputPage) throws Exception;

    /**
	 * 获取用户粉丝
	 * @param memberId 当前登录用户id
	 * @param memberId2 目标用户id
	 * @param inputPage
	 * @return
	 * @throws Exception
	 */
	public FungoPageResultDto<Map<String,Object>> getFollowee(String memberId, String memberId2, InputPageDto inputPage) throws Exception;

}
