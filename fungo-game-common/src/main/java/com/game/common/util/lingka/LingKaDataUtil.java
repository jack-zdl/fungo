package com.game.common.util.lingka;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 *  统一获取零卡响应数据工具
 *  ysx
 */
public class LingKaDataUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(LingKaDataUtil.class);

    /**
     *  根据条件获取Tab游戏列表
     * @return Tab游戏列表
     */
    public static  String getSession( String url, Map hashMap){
        // 获取条件
        // 请求数据
        String s = HttpUtil.get(url,hashMap);
        // 解析数据并响应
        return s;
    }


    /**
     *  根据条件获取Tab游戏列表
     * @return Tab游戏列表
     */
    public static  String listTabGame( String url, Map hashMap){
        // 获取条件
        // 请求数据
        String result = HttpUtil.post(url, hashMap);
        // 解析数据并响应
        return result;
    }

    /**
     * 功能描述: 登陆获取session
     * @date: 2019/10/21 18:08
     */
    public static void getSession(){
        try {
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("email", LingKaConstant.ADMIN_EMAIL);
            paramMap.put("password", LingKaConstant.ADMIN_PASSWORD);
            String s = listTabGame(LingKaConstant.LINGKA_LOGIN_URL,paramMap);
            JSONObject jsonObject = JSON.parseObject( s );
            System.out.println(jsonObject.toJSONString());
        }catch (Exception e){
            LOGGER.error( "登陆获取session",e );
        }
    }

    /**
     * 功能描述: 登陆获取session
     * @date: 2019/10/21 18:08
     */
    public static void bindGiftCard(){
        try {
            String s = HttpUtil.get("https://www.lyn.cash/api/admin/giftcard/list");
            JSONObject jsonObject = JSON.parseObject( s );
            System.out.println(jsonObject.toJSONString());
        }catch (Exception e){
            LOGGER.error( "登陆获取session",e );
        }
    }

    public static void main(String[] args) {
        LingKaDataUtil.getSession();
        LingKaDataUtil.bindGiftCard();
    }




}
