package com.fungo.community.helper;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/4/26
 */
//@Component
public class MQProduct {

    @Autowired
    private  AmqpTemplate template;


}
