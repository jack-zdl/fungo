package com.fungo.games.service.impl;



import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.games.service.GameMarketSpyDaoService;
import com.fungo.games.entity.GameMarketSpy;
import com.fungo.games.service.IGameMarketSpyService;
import com.game.common.dto.market.GameMarketSpyInput;
import com.game.common.util.PKUtil;
import com.game.common.util.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.IdentityHashMap;
import java.util.List;


@Service
public class GameMarketSpyServiceImpl implements IGameMarketSpyService {


    private static final Logger LOGGER = LoggerFactory.getLogger(GameMarketSpyServiceImpl.class);


    @Value("${sys.config.fungo.cluster.index}")
    private String clusterIndex;


    @Autowired
    private GameMarketSpyDaoService gameMarketSpyDaoService;


    @Transactional(rollbackFor = BusinessException.class)
    @Override
    public boolean addMarketSpyAction(GameMarketSpyInput gameMarketSpyInput) throws BusinessException {

        try {

            String imei = gameMarketSpyInput.getImei();
            String imsi = gameMarketSpyInput.getImsi();
            String model = gameMarketSpyInput.getModel();
            String vendor = gameMarketSpyInput.getVendor();
            String uuid = gameMarketSpyInput.getUuid();
            int os = gameMarketSpyInput.getOs();
            String mac = gameMarketSpyInput.getMac();
            String android_id = gameMarketSpyInput.getAndroid_id();
            String game_id = gameMarketSpyInput.getGame_id();
            String game_name = gameMarketSpyInput.getGame_name();


            GameMarketSpy gameMarketSpy = new GameMarketSpy();

            int clusterIndex_i = Integer.parseInt(clusterIndex);

            gameMarketSpy.setId(PKUtil.getInstance(clusterIndex_i).longPK());

            gameMarketSpy.setDeviceImei(imei);
            gameMarketSpy.setDeviceImsi(imsi);
            gameMarketSpy.setDeviceModel(model);
            gameMarketSpy.setDeviceVendor(vendor);
            gameMarketSpy.setDeviceUuid(uuid);
            //操作系统信息:1  android  ; 2 ios
            gameMarketSpy.setOs(os);
            gameMarketSpy.setDeviceMac(mac);
            gameMarketSpy.setAndroidId(android_id);
            gameMarketSpy.setGameId(game_id);
            gameMarketSpy.setGameName(game_name);

            //app在安装某个游戏后，上报数据
            gameMarketSpy.setIsObtain(2);

            Date curentDate = new Date();
            gameMarketSpy.setCreatedAt(curentDate);
            gameMarketSpy.setUpdatedAt(curentDate);

            //执行 保存
            return gameMarketSpy.insert();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BusinessException("-1", "记录用在营销推广页面点击的动作数据出现异常");
        }
    }


    @Override
    public GameMarketSpy queryMarketSpyAction(GameMarketSpyInput gameMarketSpyInput) throws BusinessException {
        GameMarketSpy gameMarketSpy = null;
        try {

            String imei = gameMarketSpyInput.getImei();
            String imsi = gameMarketSpyInput.getImsi();

            String uuid = gameMarketSpyInput.getUuid();
            int os = gameMarketSpyInput.getOs();
            String mac = gameMarketSpyInput.getMac();
            String android_id = gameMarketSpyInput.getAndroid_id();

            String gameId = gameMarketSpyInput.getGame_id();

            IdentityHashMap<String, Object> identityHashMap = new IdentityHashMap<String, Object>();

            if (StringUtils.isNotBlank(imei)) {
                identityHashMap.put("device_imei", imei);
            }

            if (StringUtils.isNotBlank(imsi)) {
                identityHashMap.put("device_imsi", imsi);
            }

            if (StringUtils.isNotBlank(uuid)) {
                identityHashMap.put("device_uuid", uuid);
            }

            if (os > 0) {
                identityHashMap.put("os", os);
            }


            if (StringUtils.isNotBlank(mac)) {
                identityHashMap.put("device_mac", mac);
            }

            if (StringUtils.isNotBlank(android_id)) {
                identityHashMap.put("android_id", android_id);
            }


            //游戏id
            identityHashMap.put("game_id", gameId);

            /**
             * 是否安装过某游戏
             * 1 未安装过
             * 2 已安装过
             */
            identityHashMap.put("is_obtain", 2);

            EntityWrapper<GameMarketSpy> entityWrapper = new EntityWrapper<GameMarketSpy>();
            entityWrapper.setSqlSelect("id,game_id,is_obtain");

            entityWrapper.allEq(identityHashMap);

            entityWrapper.orderBy("created_at", false);

            List<GameMarketSpy> gameMarketSpyList = gameMarketSpyDaoService.selectList(entityWrapper);
            if (null != gameMarketSpyList && !gameMarketSpyList.isEmpty()) {
                GameMarketSpy lastDownGame = gameMarketSpyList.get(0);
                return lastDownGame;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return gameMarketSpy;
    }

    //----------
}
