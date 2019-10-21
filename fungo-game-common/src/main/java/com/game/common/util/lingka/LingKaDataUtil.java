package com.game.common.util.lingka;

import cn.hutool.http.HttpUtil;
import java.util.Map;

/**
 *  统一获取零卡响应数据工具
 *  ysx
 */
public class LingKaDataUtil {

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
        String s = HttpUtil.get(url,hashMap);
        // 解析数据并响应
        return s;
    }







}
