package com.fungo.system.mall.service.impl;


import com.fungo.system.mall.daoService.MallLogsDaoService;
import com.fungo.system.mall.entity.MallLogs;
import com.fungo.system.mall.service.IMallLogsService;
import com.game.common.dto.mall.MallLogsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *      商城日志服务实现类
 * </p>
 *
 * @author mxf
 * @since 2019-01-18
 */
@Service
public class MallLogsServiceImpl implements IMallLogsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MallLogsServiceImpl.class);

    @Autowired
    private MallLogsDaoService mallLogsDaoService;

    @Override
    public void addMallLog(final MallLogsDto logsDto) {
        try {

            MallLogs logsEntity = new MallLogs();

            //spring的BeanUtils比apache的性能高
            BeanUtils.copyProperties(logsDto, logsEntity);
            mallLogsDaoService.insert(logsEntity);

        } catch (Exception ex) {
            LOGGER.error("添加商城访问日志出现异常", ex);
            ex.printStackTrace();
        }
    }
}
