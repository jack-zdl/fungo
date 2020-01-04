package com.fungo.system;

import com.alibaba.fastjson.JSON;
import com.fungo.system.dto.MemberSNSBindInput;
import com.fungo.system.dto.MemberSNSBindOutput;
import com.fungo.system.entity.MemberCircle;
import com.fungo.system.service.IMemberSNSService;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.util.UUIDUtils;
import com.game.common.util.date.DateTools;
import org.hibernate.validator.constraints.br.TituloEleitoral;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private IMemberSNSService iMemberSNSService;

    @Test
    public void testRedis() {
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        String key = "test-id";
        String value = "process";

        Boolean successSet = opsForValue.setIfAbsent(key, value, 10, TimeUnit.SECONDS);
        System.out.println(successSet);
        Boolean successSet1 = opsForValue.setIfAbsent(key, value, 1, TimeUnit.SECONDS);
        System.out.println(successSet1);
        Boolean successSet2 = opsForValue.setIfAbsent(key, value, 1, TimeUnit.SECONDS);
        System.out.println(successSet2);
        try {
            System.out.println("开始睡眠。。。");
            Thread.sleep(10000L);
            System.out.println("睡眠结束。。。");
            Boolean successSet3 = opsForValue.setIfAbsent(key, value, 1, TimeUnit.SECONDS);
            System.out.println(successSet3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testCommunity() {
      /*  CmmPostDto cmmPostParam = new CmmPostDto();
        cmmPostParam.setId("0003d879a8bf48c9af94493746f25fcf111");
        CmmPostDto post = iGameProxyService.selectCmmPostById(cmmPostParam);  //this.postService.selectById(target_id);
        System.out.println(post + "---" + post.getCreatedAt());
        String date = DateTools.fmtDate(post.getCreatedAt());
        System.out.println(date);*/
    }

    @Test
    public void testTran() {
        String memberId = "e1148cf59c2842f1ad534416718e96e6";
        String bindInput = "{\"accessToken\":\"2D5D95A7F69B5600DC43B68FA19AF1B5\",\"bindAction\":2,\"expiration\":\"1560671797684\",\"gender\":0,\"iconurl\":\"http://thirdqq.qlogo.cn/g?b=oidb&k=evNYdHMVQCQk9gDp1U1peQ&s=100\",\"name\":\"峰少爷的剑\",\"openid\":\"2AD679D109E768261A3C07F3A84AD870\",\"os\":\"Android\",\"snsType\":4,\"uid\":\"2AD679D109E768261A3C07F3A84AD870\",\"unionid\":\"UID_A58DC3631249440EFE649EA3E1B20498\"}";
        MemberSNSBindInput input = JSON.parseObject(bindInput, MemberSNSBindInput.class);
        try {
            ResultDto<MemberSNSBindOutput> resultDto = iMemberSNSService.bindThirdSNSWithLogged(memberId, input);
            System.out.println(resultDto);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void  insertMemberCircle(){
        MemberCircle  memberCircle = new MemberCircle();
        memberCircle.setId( UUIDUtils.getUUID() );
        memberCircle.setType( 1 );
        memberCircle.setMemberId( "493d8fae5b77424ca067d815fce025fb" );
        memberCircle.setCircleId( "b3db7baef2714bb8bab6005df8924586" );
        memberCircle.setIsactive( "1" );
        memberCircle.setCreatedAt( new Date( ) );
        memberCircle.setCreatedBy( "493d8fae5b77424ca067d815fce025fb" );
        memberCircle.setRversion( 1 );
        memberCircle.insert();
    }
}
