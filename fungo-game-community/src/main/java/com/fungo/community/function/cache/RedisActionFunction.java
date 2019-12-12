package com.fungo.community.function.cache;

import com.fungo.community.helper.RedisActionHelper;
import com.fungo.community.service.impl.PostServiceImpl;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.repo.cache.facade.FungoCacheArticle;
import com.game.common.util.SecurityMD5;
import net.bytebuddy.asm.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/12/5
 */
@Component
public class RedisActionFunction {

    private static final Logger logger = LoggerFactory.getLogger( RedisActionFunction.class);

    @Autowired
    private FungoCacheArticle fungoCacheArticle;
    @Autowired
    private RedisActionHelper redisActionHelper;
    /**
     * 功能描述: 删除文章后清除redis 相关的key
     * @return: boolean
     * @date: 2019/12/5 14:37
     */
    public boolean deletePostRedisHandle(String userId,String postId){
        try {
            List<String> list = new LinkedList<>();
            list.add( SecurityMD5.encrypt16( FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_HTML_CONTENT  + postId)); //文章内容html-内容
            list.add(SecurityMD5.encrypt16(FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_HTML_TITLE + postId));   //文章内容html-标题
            list.add(SecurityMD5.encrypt16(FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_DETAIL));  //帖子详情
            list.add(SecurityMD5.encrypt16(FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_COMMENTS));   //帖子/心情评论列表
            list.add(SecurityMD5.encrypt16(FungoCoreApiConstant.FUNGO_CORE_API_INDEX_POST_LIST));    //首页文章帖子列表(v2.4)
            list.add(SecurityMD5.encrypt16(FungoCoreApiConstant.FUNGO_CORE_API_COMMUNITYS_POST_LIST));    //帖子列表
            list.add(SecurityMD5.encrypt16(FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_TOPIC));    //社区置顶文章(2.4.3)
            list.add(SecurityMD5.encrypt16(FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_POSTS));   // 我的文章(2.4.3)
            list.add(SecurityMD5.encrypt16(FungoCoreApiConstant.FUNGO_CORE_API_INDEX_RECOMMEND_INDEX)); // 首页-首页(v2.4)
            list.add(SecurityMD5.encrypt16(FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + userId));   // 个人信息
            list.add(SecurityMD5.encrypt16(FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INCENTS_FORTUNE_COIN_POST + userId));    // fun币消耗详情
            list.add(FungoCoreApiConstant.PUB_POST);    // fun币消耗详情
            redisActionHelper.removePostRedisCache(list);
        }catch (Exception e){
            logger.error( "删除文章后清除Redis事件",e );
            return false;
        }
        return true;
    }

    /**
     * 功能描述: 删除文章后清除redis 相关的key
     * @return: boolean
     * @date: 2019/12/5 14:37
     */
    public boolean updatePostRedisHandle(){
        try {
            List<String> list = new LinkedList<>();
            list.add(FungoCoreApiConstant.PUB_POST);    // fun币消耗详情
            redisActionHelper.removePostRedisCache(list);
        }catch (Exception e){
            logger.error( "删除文章后清除Redis事件",e );
            return false;
        }
        return true;
    }
}
