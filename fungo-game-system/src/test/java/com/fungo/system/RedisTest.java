package com.fungo.system;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis(){
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
}
