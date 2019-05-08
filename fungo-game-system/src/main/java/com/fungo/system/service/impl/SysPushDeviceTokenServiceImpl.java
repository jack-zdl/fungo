package com.fungo.system.service.impl;

import com.fungo.system.entity.SysPushToken;
import com.fungo.system.service.ISysPushDeviceTokenService;
import com.game.common.dto.GameSysPushDeviceTokenInput;
import com.game.common.util.PKUtil;
import com.game.common.util.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class SysPushDeviceTokenServiceImpl implements ISysPushDeviceTokenService {


    private static final Logger logger = LoggerFactory.getLogger(SysPushDeviceTokenServiceImpl.class);


    @Value("${sys.config.fungo.cluster.index}")
    private String clusterIndex;


    @Override
    public boolean addDeviceToken(GameSysPushDeviceTokenInput gameSysPushDeviceTokenInput) throws BusinessException {
        try {

            String imei = gameSysPushDeviceTokenInput.getImei();
            String imsi = gameSysPushDeviceTokenInput.getImsi();
            String model = gameSysPushDeviceTokenInput.getModel();
            String vendor = gameSysPushDeviceTokenInput.getVendor();
            String uuid = gameSysPushDeviceTokenInput.getUuid();
            int os = gameSysPushDeviceTokenInput.getOs();
            String mac = gameSysPushDeviceTokenInput.getMac();
            String android_id = gameSysPushDeviceTokenInput.getAndroid_id();

            String d_token = gameSysPushDeviceTokenInput.getD_token();
            int sb_type = gameSysPushDeviceTokenInput.getSb_type();


            SysPushToken sysPushToken = new SysPushToken();

            int clusterIndex_i = Integer.parseInt(clusterIndex);

            sysPushToken.setId(PKUtil.getInstance(clusterIndex_i).longPK());

            sysPushToken.setDeviceImei(imei);
            sysPushToken.setDeviceImsi(imsi);
            sysPushToken.setDeviceModel(model);
            sysPushToken.setDeviceVendor(vendor);
            sysPushToken.setDeviceUuid(uuid);
            //操作系统信息:1  android  ; 2 ios
            sysPushToken.setOs(os);
            sysPushToken.setDeviceMac(mac);
            sysPushToken.setAndroidId(android_id);

            sysPushToken.setDToken(d_token);

            sysPushToken.setSbType(sb_type);

            Date curentDate = new Date();
            sysPushToken.setCreatedAt(curentDate);
            sysPushToken.setUpdatedAt(curentDate);
            //执行 保存
            return sysPushToken.insert();
        } catch (Exception ex) {
            logger.error("消息推送目标设备token数据处理出现异常", ex);
        }
        return false;
    }
}
