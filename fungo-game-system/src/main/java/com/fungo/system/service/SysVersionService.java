package com.fungo.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.fungo.system.entity.SysVersion;
import com.game.common.dto.ResultDto;

import java.util.HashMap;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lzh
 * @since 2018-08-21
 */
public interface SysVersionService extends IService<SysVersion> {
    /**
     *获取app版本设置信息：
     *        如：是否强制跟新，是否关闭游戏下载开关，是否关闭广告banner开关，是否强制更新等等信息
     * @param version app release版本号
     * @param mobile_type app 平台类型 Android/IOS
     * @param channel_code app渠道编码，详见接口文档
     * @return
     */
    HashMap<String, Object> queryAppVersionInfo(String version, String mobile_type, String channel_code);

    ResultDto<HashMap<String, Object>> getVipHide(String version, String mobile_type,String appChannel);

}
