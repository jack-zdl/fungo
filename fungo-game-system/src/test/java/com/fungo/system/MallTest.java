package com.fungo.system;

import com.alibaba.fastjson.JSON;
import com.fungo.system.dto.MemberSNSBindInput;
import com.fungo.system.dto.MemberSNSBindOutput;
import com.fungo.system.function.FungoVMallGoodsExcelParserService;
import com.fungo.system.service.IMemberSNSService;
import com.game.common.dto.ResultDto;
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
public class MallTest {
    @Autowired
    private FungoVMallGoodsExcelParserService fungoVMallGoodsExcelParserService;


    @Test
    public void testRedis() {
        try {
//            fungoVMallGoodsExcelParserService.excuteParserToVCard();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
