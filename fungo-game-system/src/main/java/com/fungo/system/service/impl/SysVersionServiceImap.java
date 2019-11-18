package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.system.dao.SysVersionDao;
import com.fungo.system.entity.SysVersion;
import com.fungo.system.entity.SysVersionChannel;
import com.fungo.system.service.SysVersionChannelService;
import com.fungo.system.service.SysVersionService;
import com.fungo.system.service.portal.impl.PortalSystemIndexServiceImpl;
import com.game.common.dto.ResultDto;
import com.game.common.util.CommonUtil;
import com.game.common.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

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

    private static final Logger LOGGER = LoggerFactory.getLogger( SysVersionServiceImap.class);

    @Autowired
    private  SysVersionDao sysVersionDao;
    @Autowired
    private SysVersionChannelService sysVersionChannelService;
    @Override
    public HashMap<String, Object> queryAppVersionInfo(String version, String mobile_type, String channel_code) {
        return this.sysVersionDao.queryAppVersionInfo(version, mobile_type, channel_code);
    }

    /**
    * 功能描述:  查询是否隐藏VIP
     * @date: 2019/10/30 13:39
     */
    @Override
    public ResultDto<HashMap<String, Object>> getVipHide(String version, String mobile_type,String appChannel) {
        ResultDto<HashMap<String, Object>> resultDto = ResultDto.ResultDtoFactory.buildError( "没有查询到数据" );
        try {
            if(CommonUtil.isNull( appChannel )){
                List<SysVersion> sysVersions = selectList( new EntityWrapper<SysVersion>().eq( "version",version ).eq( "mobile_type",mobile_type ) );
                if(sysVersions != null && sysVersions.size()>0){
                    HashMap<String, Object> hashMap = new HashMap<>( );
                    hashMap.put( "hide",sysVersions.get(0).getIsHide() == 1 ? true : false );
                    resultDto = ResultDto.ResultDtoFactory.buildSuccess( hashMap);
                }
            }else {
                List<SysVersion> sysVersions = selectList( new EntityWrapper<SysVersion>().eq( "version",version ).eq( "mobile_type",mobile_type ) );
                if(sysVersions != null && sysVersions.size()>0){
                    String versionId = sysVersions.get(0).getId();
                    List<SysVersionChannel> sysVersionChannels = sysVersionChannelService.selectList( new EntityWrapper<SysVersionChannel>().eq( "version_id",versionId ).eq( "channel_code",appChannel ) );
                    if(sysVersionChannels != null && sysVersionChannels.size()>0){
                        HashMap<String, Object> hashMap = new HashMap<>( );
                        hashMap.put( "hide",sysVersionChannels.get(0).getIsHide() == 1 ? true : false );
                        resultDto = ResultDto.ResultDtoFactory.buildSuccess( hashMap);
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error( "查询是否隐藏VIP异常",e );
        }
        return resultDto;
    }
}
