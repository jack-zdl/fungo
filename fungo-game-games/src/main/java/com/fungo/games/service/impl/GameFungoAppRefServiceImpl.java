package com.fungo.games.service.impl;



import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.games.service.GameFungoappRefDaoService;
import com.fungo.games.entity.GameFungoappRef;
import com.fungo.games.service.IGameFungoAppRefService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.IdentityHashMap;
import java.util.List;


@Service
public class GameFungoAppRefServiceImpl implements IGameFungoAppRefService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameFungoAppRefServiceImpl.class);


    @Autowired
    private GameFungoappRefDaoService gameFungoappRefDaoService;


    /**
     * 迁移变动 entityWrapper.setSqlSelect("id,fungo_app_url as fungoAppUrl");
     * @param game_id 游戏ID
     * @param fungo_channel_id fungo apk渠道id
     * @return
     */
    @Override
    public GameFungoappRef queryGameFungoappRef(String game_id, String fungo_channel_id) {

        GameFungoappRef gameFungoappRef = null;

        try {

            IdentityHashMap<String, Object> identityHashMap = new IdentityHashMap<String, Object>();

            if (StringUtils.isNotBlank(game_id)) {
                identityHashMap.put("game_id", game_id);
            }
            if (StringUtils.isNotBlank(fungo_channel_id)) {
                identityHashMap.put("fungo_channel_id", fungo_channel_id);
            }

            EntityWrapper<GameFungoappRef> entityWrapper = new EntityWrapper<GameFungoappRef>();
//            entityWrapper.setSqlSelect("id,fungo_app_url");
//            迁移变动
//            2019-05-09
//            lyc
            entityWrapper.setSqlSelect("id,fungo_app_url as fungoAppUrl");
            entityWrapper.allEq(identityHashMap);
            entityWrapper.orderBy("created_at", false);

            List<GameFungoappRef> gameFungoappRefList = gameFungoappRefDaoService.selectList(entityWrapper);
            if (null != gameFungoappRefList && !gameFungoappRefList.isEmpty()) {
                gameFungoappRef = gameFungoappRefList.get(0);
            }

        } catch (Exception ex) {
            LOGGER.error("获取游戏与Fungo app Apk捆绑包下载地址失败,异常堆栈如下:");
            ex.printStackTrace();
        }
        return gameFungoappRef;
    }

    //-----------
}
