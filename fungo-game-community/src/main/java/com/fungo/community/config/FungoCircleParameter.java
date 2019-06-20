package com.fungo.community.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/20
 */
@Component
@ConfigurationProperties(prefix = "circle")
public class FungoCircleParameter {

    private int postnumber;

    public int getPostnumber() {
        return postnumber;
    }

    public void setPostnumber(int postnumber) {
        this.postnumber = postnumber;
    }
}
