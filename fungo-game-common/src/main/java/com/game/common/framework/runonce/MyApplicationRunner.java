package com.game.common.framework.runonce;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class MyApplicationRunner implements ApplicationRunner{

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("#######################################自定义系统初始化#######################################");
        RunContainer.executeOnece();
        System.out.println("#######################################自定义初始化完成#######################################");
    }

}
