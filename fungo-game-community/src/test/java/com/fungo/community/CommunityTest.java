package com.fungo.community;

import com.fungo.community.helper.RedisActionHelper;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.util.SecurityMD5;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.LinkedList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CommunityTest {

    @Autowired
    private RedisActionHelper redisActionHelper;

    @Test
    public void redisActionHelper(){
        String postId = "e96c4f62d53f4e77974802198ae7973f";
        List<String> list = new LinkedList<String>();
//        list.add( SecurityMD5.encrypt16( FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_HTML_CONTENT + postId));
//        list.add(SecurityMD5.encrypt16(FungoCoreApiConstant.FUNGO_CORE_API_GAMR_POSTS));
//        list.add(SecurityMD5.encrypt16(FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_DETAIL));
        redisActionHelper.removePostRedisCache(list);
    }

    @Test
    public void communityTest(){
        System.out.println("---------------------");
    }

}
