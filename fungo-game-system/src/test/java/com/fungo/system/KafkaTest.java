package com.fungo.system;

import com.fungo.system.controller.ActionController;
import com.fungo.system.service.impl.ActionServiceImap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.stream.IntStream;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class KafkaTest {


    private static final Logger LOGGER = LoggerFactory.getLogger( KafkaTest.class);

    @Autowired
    private ActionServiceImap actionServiceImap;

    @Test
    public void insertKafka(){
        LOGGER.error("错误日志推送kafka");
        actionServiceImap.insertKafkaLog();
    }

    @Test
    public  void sendKafkaLogByLogbackAppender() {
        Gson gson = new GsonBuilder().create();
        IntStream.rangeClosed(1,10).forEach(i->{
            LOGGER.error("aaaaa"+i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
