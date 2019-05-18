package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.system.dao.SysVersionDao;
import com.fungo.system.entity.SysVersion;
import com.fungo.system.service.SysVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lzh
 * @since 2018-08-21
 */
@Service
public class SysVersionServiceImap extends ServiceImpl<SysVersionDao, SysVersion> implements SysVersionService {

    @Autowired
    private  SysVersionDao sysVersionDao;
    @Override
    public HashMap<String, Object> queryAppVersionInfo(String version, String mobile_type, String channel_code) {
        return this.sysVersionDao.queryAppVersionInfo(version, mobile_type, channel_code);
    }
}
