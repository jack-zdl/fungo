package com.game.common.util.lingka;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.config.RequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.client.config.RequestConfig.Builder;
import java.util.HashMap;
import java.util.Map;

import static com.game.common.util.lingka.LingKaConstant.*;

/**
 *  统一获取零卡响应数据工具
 *  ysx
 */
public class LingKaDataUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(LingKaDataUtil.class);


    private static int connectTimeout = 5000;//连接超时时间
    private int connectionRequestTimeout = 10000;//从连接池获取连接超时时间
    private static int socketTimeout = 5000;//获取数据超时时间
    private String charset = "utf-8";


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
//        String result = HttpUtil.post(url, hashMap);
        String result = HttpRequest.post(url).setConnectionTimeout( connectTimeout ).body( hashMap.toString() ).timeout( socketTimeout ).execute().body();
        // 解析数据并响应
        return result;
    }

    /**
     * 功能描述: 登陆获取session
     * @date: 2019/10/21 18:08
     */
    public static boolean getSession(String userPhone){
        try {
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("phone", userPhone);
            paramMap.put("password", ADMIN_PASSWORD);
            String s = listTabGame(LINGKA_LOGIN_URL,paramMap);
            JSONObject jsonObject = JSON.parseObject( s );
            return jsonObject.get( "type" ) != null ;
        }catch (Exception e){
            LOGGER.error( "登陆获取session",e );
        }
        return false;
    }

    /**
     * 功能描述:  用户绑定套餐或者券
     * @param: userPhone 用户手机号
     * @Param  type 套餐类型
     * @Param  serverId 业务id 用于零卡那边记录我这日志
     * @return: void
     * @auther: dl.zhang
     * @date: 2019/10/23 17:09
     */
    public static BindGiftcardDto bindGiftCard(String userPhone,String type,String serverId){
        BindGiftcardDto bindGiftcardDto = null;
        try {
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("user", userPhone);
            paramMap.put("type", type);
            paramMap.put("serverId", serverId);
            String s = HttpUtil.post(LINGKA_GIFTCARD_BIND,paramMap);
            bindGiftcardDto = JSON.parseObject( s,BindGiftcardDto.class );
        }catch (Exception e){
            LOGGER.error( "用户绑定套餐或者券异常",e );
        }
        return bindGiftcardDto;
    }

    public static void main(String[] args) {
        LingKaDataUtil.getSession("18221537170");
        LingKaDataUtil.bindGiftCard("18221537170","1","1");
    }




}
