package com.fungo.system.helper.lingka;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.fungo.system.entity.Member;
import com.game.common.config.MyThreadLocal;
import com.game.common.util.CommonUtil;
import com.game.common.util.lingka.BindGiftcardDto;
import com.game.common.util.lingka.LingKaConstant;
import com.game.common.util.lingka.LingKaDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Map;

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
    public BindGiftcardDto sendGiftCardToLingka(Member member, String type, String serverId ){
        BindGiftcardDto bindGiftcardDto = null;
        boolean result = false;
        try {

            String session =MyThreadLocal.getLingkaSession();
            if(CommonUtil.isNull( session )){
                boolean resultBoolean = LingKaDataUtil.getAdminSession( LingKaConstant.ADMIN_EMAIL );
                if(resultBoolean){
                    MyThreadLocal.setLingkaSession("true");
                }
            }
            try {
                bindGiftcardDto = LingKaDataUtil.bindGiftCard( member.getMobilePhoneNum() ,type ,serverId );
                bindGiftcardDto.setResult( true);
            }catch (Exception e){
                LOGGER.error("零卡用户绑定某个券",e  );
                bindGiftcardDto.setResult( false);
            }

        }catch (Exception e){
            LOGGER.error( "通知零卡，用户绑定某个券出现异常",e );
            bindGiftcardDto.setResult( false);
        }
        return bindGiftcardDto;

    }

    /**
     * 功能描述: 用戶支付回調成功通知零卡
     * @date: 2019/10/23 17:59
     */
    public Map<String,Object> sendPayLogToLingka(String payUrl , Map map){
        Map<String,Object> hashMap = null;
        try {
            String session =MyThreadLocal.getLingkaSession();
            if(CommonUtil.isNull( session )){
                boolean resultBoolean = LingKaDataUtil.getAdminSession( LingKaConstant.ADMIN_EMAIL );
                if(resultBoolean){
                    MyThreadLocal.setLingkaSession("true");
                }else {

                }
            }
            String result = HttpUtil.post(payUrl,map);
            hashMap = JSON.parseObject( result );
        }catch (Exception e){
            LOGGER.error( "用戶支付回調成功通知零卡出现异常",e );
        }
        return hashMap;

    }
}
