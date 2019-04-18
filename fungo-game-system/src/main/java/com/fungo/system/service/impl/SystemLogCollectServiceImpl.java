package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.fungo.system.dto.LogCollectInput;
import com.fungo.system.service.ISystemLogCollectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SystemLogCollectServiceImpl implements ISystemLogCollectService {


    private static final Logger logger = LoggerFactory.getLogger(SystemLogCollectServiceImpl.class);

    @Override
    public Map<String, Object> collectLog(LogCollectInput collectInput) {
        try {
            logger.info("****************执行app和web系统运行,用户操作日志收集--collectInput: {}", JSON.toJSONString(collectInput));
        } catch (Exception ex) {
            logger.error("收集用户操作数据出现异常", ex);
        }
        return null;
    }
}
