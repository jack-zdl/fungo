package com.game.common.aliyun.green;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20180509.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.game.common.config.FungoConfig;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/11/7
 */
public class MyIAcsClient {

    public static IAcsClient recognitionClient = null;

    private static String ALIYUN_ACCESS_KEY_ID = new FungoConfig().getALIYUN_ACCESS_KEY_ID();

    private static String ALIYUN_ACCESS_KEY_SECRET = new FungoConfig().getALIYUN_ACCESS_KEY_SECRET();

    private static String REGION_ID = new FungoConfig().getREGION_ID();

    public static IAcsClient initIAcsClient(){
        /**
         * ALIYUN_ACCESS_KEY_ID和ALIYUN_ACCESS_KEY_SECRET,请替换成您自己的aliyun ak.
         * 访问regionId支持: cn-shanghai,cn-beijing,ap-southeast-1, us-west-1, 其他区域暂不支持, 请勿使用
         * 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
         */
//        String ALIYUN_ACCESS_KEY_ID =  ;
//        String ALIYUN_ACCESS_KEY_SECRET = "SK6OH0X6KEFw70T7SmnR1C7p0sAPWi";
//        String REGION_ID = "cn-shanghai";

        IClientProfile profile = DefaultProfile.getProfile(REGION_ID, ALIYUN_ACCESS_KEY_ID, ALIYUN_ACCESS_KEY_SECRET);
        recognitionClient = new DefaultAcsClient(profile);
        return recognitionClient;
    }

    public static IAcsClient getIAcsClient(){
        if(recognitionClient == null){
            initIAcsClient();
        }
        return recognitionClient;
    }



}
