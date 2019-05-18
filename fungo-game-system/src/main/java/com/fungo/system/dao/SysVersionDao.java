package com.fungo.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.system.entity.SysVersion;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-08-21
 */
public interface SysVersionDao extends BaseMapper<SysVersion> {

    /**
     *获取app版本设置信息：
     *        如：是否强制跟新，是否关闭游戏下载开关，是否关闭广告banner开关，是否强制更新等等信息
     * @param version app release版本号
     * @param mobile_type app 平台类型 Android/IOS
     * @param channel_code app渠道编码，详见接口文档
     * @return
     */
    public HashMap<String, Object> queryAppVersionInfo(@Param("version") String version, @Param("mobile_type") String mobile_type, @Param("channel_code") String channel_code);

}