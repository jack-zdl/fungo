package com.fungo.system.helper.lingka;

import com.fungo.system.entity.Member;
import com.game.common.config.MyThreadLocal;
import com.game.common.util.CommonUtil;
import com.game.common.util.StringUtil;
import com.game.common.util.lingka.BindGiftcardDto;
import com.game.common.util.lingka.LingKaConstant;
import com.game.common.util.lingka.LingKaDataUtil;
import org.apache.commons.lang3.StringUtils;
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
    public BindGiftcardDto sendGiftCardToLingka(Member member, String type, String serverId ){
        BindGiftcardDto bindGiftcardDto = null;
        boolean result = false;
        try {

            String session =MyThreadLocal.getLingkaSession();
            if(CommonUtil.isNull( session )){
                boolean resultBoolean = LingKaDataUtil.getSession( LingKaConstant.ADMIN_EMAIL );
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
}
