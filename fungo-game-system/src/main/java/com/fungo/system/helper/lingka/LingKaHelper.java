package com.fungo.system.helper.lingka;

import com.game.common.util.lingka.LingKaConstant;
import com.game.common.util.lingka.LingKaDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/10/23
 */
@Component
public class LingKaHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(LingKaHelper.class);
    /**
     * 功能描述: 通知零卡，用户绑定某个券
     * @date: 2019/10/23 17:59
     */
    public boolean sendGiftCardToLingka(String userPhone,String type,String serverId ){
        boolean result = false;
        try {
            LingKaDataUtil.getSession( LingKaConstant.ADMIN_EMAIL );
            LingKaDataUtil.bindGiftCard( userPhone ,type ,serverId );
            result = true;
        }catch (Exception e){
            LOGGER.error( "通知零卡，用户绑定某个券出现异常",e );
        }
        return result;

    }
}
