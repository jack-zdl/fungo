package com.fungo.system.ts.mq.msService;


import com.fungo.system.ts.mq.dao.service.SysTsMsgDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *      分布式事务之数据消息业务微服务接口
 * </p>
 * since: V3.0.0
 * @author mxf
 * @since 2019-05-10
 */
@RestController
public class MSServiceTSController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MSServiceTSController.class);


    @Autowired
    private SysTsMsgDaoService sysTsMsgDaoService;





    //--------
}
