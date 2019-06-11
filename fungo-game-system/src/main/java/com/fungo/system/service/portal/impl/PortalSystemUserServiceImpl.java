package com.fungo.system.service.portal.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.entity.MemberFollower;
import com.fungo.system.service.IUserService;
import com.fungo.system.service.MemberFollowerService;
import com.fungo.system.service.portal.PortalSystemIUserService;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.AuthorBean;
import com.game.common.repo.cache.facade.FungoCacheMember;
import com.game.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/6/11 14:31
 */
@Service
public class PortalSystemUserServiceImpl implements PortalSystemIUserService {
    @Autowired
    private FungoCacheMember fungoCacheMember;
    @Autowired
    private MemberFollowerService followService;
    @Autowired
    private IUserService iUserService;

    @Override
    public AuthorBean getUserCard(String cardId, String memberId) {
        AuthorBean author = null;
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_CARD + cardId;
        author = (AuthorBean) fungoCacheMember.getIndexCache(keyPrefix, memberId);
        if (null != author) {
            return author;
        }
        author = iUserService.getAuthor(cardId);
        if (!CommonUtil.isNull(memberId)) {
//			BasAction action=actionService.selectOne(new EntityWrapper<BasAction>().eq("type", "5").eq("member_id",memberId).eq("target_id", cardId).notIn("state", "-1"));
            MemberFollower one = followService.selectOne(new EntityWrapper<MemberFollower>().eq("member_id", memberId).eq("follower_id", cardId).andNew("state = {0}", 1).or("state = {0}", 2));
            if (one != null) {
                author.setIs_followed(true);
//                PC2.0新增相互关注业务添加字段 mutualFollowed
                if (one.getState().equals(2)){
                    author.setMutualFollowed("1");
                }
            }
        }
        //redis cache
        fungoCacheMember.excIndexCache(true, keyPrefix, memberId, author);
        return author;
    }
}
