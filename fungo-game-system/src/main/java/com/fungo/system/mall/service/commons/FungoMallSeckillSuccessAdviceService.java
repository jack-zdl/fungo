package com.fungo.system.mall.service.commons;


import com.alibaba.fastjson.JSON;
import com.fungo.system.mall.entity.MallVirtualCard;
import com.fungo.system.mall.service.consts.FungoMallSeckillConsts;
import com.fungo.system.service.IFungoAdvicePushService;
import com.fungo.system.service.IPushService;
import com.game.common.bean.advice.BasNoticeDto;
import com.game.common.consts.Setting;
import com.game.common.util.UUIDUtils;
import com.game.common.util.date.DateTools;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

/**
 * <p>
 *    fungo商城-秒杀成功后，通知用户业务
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */

@Service
public class FungoMallSeckillSuccessAdviceService {

    private static final Logger logger = LoggerFactory.getLogger(FungoMallSeckillSuccessAdviceService.class);


    @Autowired
    private IFungoAdvicePushService iFungoAdvicePushService;

    @Autowired
    private IPushService pushService;


    /**
     * 推送秒杀商品信息给用户
     * @param mb_id
     * @param goodsName
     * @param goodsType
     * @param cardSn
     * @param cardPwd
     * @param period
     * @return
     */
    public boolean pushMsgToMember(String mb_id, String goodsName, Integer goodsType, String cardSn, String cardPwd, String period, MallVirtualCard vCard) {

        try {

            BasNoticeDto noticeDto = new BasNoticeDto();
            noticeDto.setId(UUIDUtils.getUUID());
            noticeDto.setType(Setting.MSG_TYPE_SYSTEM);
            noticeDto.setIsRead(0);
            noticeDto.setMemberId(mb_id);

            HashMap<String, String> msgContentMap = new HashMap<>();
            /**
             * 值|表意|说明|备注
             *
             * 1 | 内链 | 跳转APP内部链接 | 根据targetType与targetId决定跳转逻辑
             * 2 | 外链 | 跳转外部链接 |
             * 3 | 高亮 | 只高亮，没有操作
             */
            if (StringUtils.isBlank(cardSn)) {
                cardSn = "";
            }

            if (StringUtils.isBlank(cardPwd)) {
                cardPwd = "";
            }

            if (StringUtils.isBlank(period)) {
                period = "";
            }

            String msgDataContent = "";

            switch (goodsType) {
                //零卡
                case 21:
                    msgDataContent = FungoMallSeckillConsts.MSG_SECKILL_SUCCESS_LINGKA_BAIJINVIP;
                    msgDataContent = msgDataContent.replace("{", cardSn);
                    msgContentMap.put("actionType", "2");
                    break;
                // 京东卡 10元
                case 22:
                    msgDataContent = FungoMallSeckillConsts.MSG_SECKILL_SUCCESS_JD_TEN;
                    msgDataContent = msgDataContent.replace("{", cardSn).replace("}", cardPwd).replace("[", period);
                    msgContentMap.put("actionType", "2");
                    break;
                //QB卡
                case 23:
                    msgDataContent = FungoMallSeckillConsts.MSG_SECKILL_SUCCESS_QB_TEN;
                    msgDataContent = msgDataContent.replace("{", cardSn).replace("}", cardPwd);
                    msgContentMap.put("actionType", "2");
                    break;
                case 24:
                    msgDataContent = FungoMallSeckillConsts.FESTIVAL_GOODS_SUCCESS_lv1;
                    msgDataContent = msgDataContent.replace("{", goodsName).replace("}", cardSn);
                    msgContentMap.put("actionType", "2");
                    break;
                case 25:
                    msgDataContent = FungoMallSeckillConsts.FESTIVAL_GOODS_SUCCESS_lv2;
                    msgDataContent = msgDataContent.replace("$", goodsName).replace("{", cardSn).replace("}", cardPwd);
                    msgContentMap.put("actionType", "2");
                    break;
                //零卡 谷歌
                case 26:
                    msgDataContent = FungoMallSeckillConsts.MSG_SECKILL_SUCCESS_LINGKA_GOOGLE;
                    msgDataContent = msgDataContent.replace("{", cardSn);
                    msgDataContent = msgDataContent.replace("}", cardPwd);
                    msgDataContent = msgDataContent.replace("+", vCard.getExt1()); //当虚拟商品的类型为26时，ext1就为辅助邮箱
                    msgContentMap.put("actionType", "2");
                    break;
                //零卡 白金VIP月卡
                case 27:
                case 28:        //零卡 游戏VIP月卡
                    msgDataContent = FungoMallSeckillConsts.MSG_SECKILL_SUCCESS_LINGKA_BAIJINVIP;
                    msgDataContent = msgDataContent.replace("{", goodsName);
                    msgContentMap.put("actionType", "1");
                    msgContentMap.put("targetType", "12");
                    break;
                case 1:
                    msgDataContent = FungoMallSeckillConsts.MSG_SECKILL_SUCCESS_physical;
                    msgDataContent = msgDataContent.replace("$", goodsName);
                    msgContentMap.put("actionType", "3");
                    break;
                default:
                    msgDataContent = FungoMallSeckillConsts.MSG_SECKILL_SUCCESS_physical;
                    msgDataContent = msgDataContent.replace("$", goodsName);
                    msgContentMap.put("actionType", "3");
                    break;
            }

            msgContentMap.put("msgTime", DateTools.fmtDate(new Date()));
            msgContentMap.put("content", msgDataContent);
            noticeDto.setData(JSON.toJSONString(msgContentMap));

            logger.info("推送秒杀商品信息给用户:{}", JSON.toJSONString(noticeDto));

            boolean isAdd = iFungoAdvicePushService.addNotice(noticeDto);

            //执行推送
            pushService.push(mb_id, -1, "");

            return isAdd;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

}
