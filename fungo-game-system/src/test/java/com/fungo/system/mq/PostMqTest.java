package com.fungo.system.mq;

import com.fungo.system.facede.impl.GameProxyServiceImpl;
import com.fungo.system.helper.mq.MQProduct;
import com.fungo.system.service.impl.VdServiceImpl;
import com.game.common.dto.community.CmmPostDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/11/2
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PostMqTest {

    @Autowired
    private GameProxyServiceImpl gameProxyServiceImpl;

    @Autowired
    private MQProduct mqProduct;

    @Test
    public void test(){

        CmmPostDto postParam = new CmmPostDto();
        postParam.setId("0009701ab56b4b4197992e4c63abe69b");
        postParam.setState(1);
        CmmPostDto post = gameProxyServiceImpl.selectCmmPostById(postParam);
        post.setVideoCoverImage("http://outin-d8556079df3311e89ef400163e1c8a9c.oss-cn-beijing.aliyuncs.com/283ba9f7dc424be898d9e8c29106e435/snapshots/08f3503ab15e42a29e345b132df3dcef-00003.jpg");
        post.setVideo("http://outin-d8556079df3311e89ef400163e1c8a9c.oss-cn-beijing.aliyuncs.com/283ba9f7dc424be898d9e8c29106e435/3a725df6c6c64fa6aa73fce21095bc1c-624bb34fa1dcbd916cad9c298da6787c-sd.mp4");
        post.setState(1);
        post.setUpdatedAt(new Date());
        post.setVideoUrls("[{\"format\":\"mp4\",\"duration\":58,\"encrypt\":false,\"fps\":25,\"bitrate\":1688,\"width\":1920,\"status\":\"success\",\"height\":1080,\"fileUrl\":\"http://outin-d8556079df3311e89ef400163e1c8a9c.oss-cn-beijing.aliyuncs.com/283ba9f7dc424be898d9e8c29106e435/3a725df6c6c64fa6aa73fce21095bc1c-624bb34fa1dcbd916cad9c298da6787c-sd.mp4\",\"definition\":\"SD\",\"size\":12311141}]");
        // @todo
        //                postService.updateById(post);
        mqProduct.postUpdate(post);
    }
}
