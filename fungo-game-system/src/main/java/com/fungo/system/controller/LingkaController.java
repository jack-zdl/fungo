package com.fungo.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.fungo.system.config.NacosFungoCircleConfig;
import com.fungo.system.facede.impl.GameProxyServiceImpl;
import com.fungo.system.helper.mq.MQProduct;
import com.fungo.system.service.MemberPlayLogService;
import com.fungo.system.service.SysVersionService;
import com.fungo.system.tools.WXPayUtil;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.vo.MemberPlayLogVO;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * <p>Lingka控制层</p>
 * @Author: dl.zhang
 * @Date: 2019/10/22
 */
@Controller
public class LingkaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LingkaController.class);

    @Autowired
    private MemberPlayLogService memberPlayLogService;
    @Autowired
    private SysVersionService sysVersionService;
    @Autowired
    private NacosFungoCircleConfig nacosFungoCircleConfig;

    @PostMapping(value = "/api/system/member/log")
    public ResultDto<String> saveLingkaMemeberPlayLog(@Validated @RequestBody MemberPlayLogVO memberPlayLogVO){
        ResultDto<String> resultDto = null;
//        resultDto = memberPlayLogService.saveMemberPalyLog( memberPlayLogVO);
        return  resultDto;
    }


//    /**
//     * 功能描述: 支付宝回调接口
//     * @date: 2019/10/25 10:34
//     */
//    @PostMapping(value = "/api/system/alipay")
//    @ResponseBody
//    public Object  saveAlipay( HttpServletRequest request , String out_trade_no,String trade_no,String trade_status ,String total_amount, @RequestBody JSONObject alipayJson){
//        System.out.println( "-------------------Post请求方式的/api/system/alipay" );
//        LOGGER.error( "-------------------Post请求方式的/api/system/alipay" );
//        System.out.println( "-------------------Post请求方式的/api/system/alipay參數参数="+out_trade_no+"1==="+trade_no+"1==="+trade_status+"1==="+total_amount);
//        ResultDto<String> resultDto = null;
////        BufferedReader br = null;
////        StringBuilder sb = new StringBuilder("");
////        try
////        {
////            br = request.getReader();
////            String str;
////            while ((str = br.readLine()) != null)
////            {
////                sb.append(str);
////            }
////            br.close();
////        }
////        catch (IOException e)
////        {
////            e.printStackTrace();
////        }
////        System.out.println("++++++++++++++++++++++++++++++"+sb);
////        LOGGER.error("++++++++++++++++++++++++++++++"+sb);
//        resultDto = memberPlayLogService.saveMemberPalyLog(request, alipayJson);
//        if(resultDto.getStatus() == 1){
//            return "success";
//        }
//        return "failed";
//    }

    /**
     * 功能描述: 支付宝异步回调接口
     *   //todo 验签如果成功的话，还需要校验该订单中的订单号、总金额等信息来防止恶意攻击
     *   //todo 处理业务逻辑，更新订单状态，更新库存等...
     *   支付宝文档要求：程序执行完后必须打印输出“success”（不包含引号）。如果商户反馈给支付宝的字符不是success这7个字符，支付宝服务器会不断重发通知，直到超过24小时22分钟。
     *   一般情况下，25小时以内完成8次通知（通知的间隔频率一般是：4m,10m,10m,1h,2h,6h,15h）；
     * @date: 2019/10/25 10:34
     */
    @RequestMapping(value = "/api/system/alipay",method = RequestMethod.POST)
    @ResponseBody
    public Object  alipayCallback( HttpServletRequest request, HttpServletRequest response,@RequestBody Map<String,String> paramObject){
        LOGGER.error( "+++++++++++++++這是是get请求方式的/api/system/alipay" );
        Map<String, String> params = Maps.newHashMap();
        try {
            if(paramObject != null && paramObject.size() > 0){
                params = paramObject;
                LOGGER.error("body体返回的数据"+ JSON.toJSONString(params));
            }else {
                Map<String, String[]> parameterMap = request.getParameterMap();
                Set<Map.Entry<String, String[]>> entries = parameterMap.entrySet();
                for (Map.Entry<String, String[]> entry : entries){
                    String key = entry.getKey();
                    String[] values = entry.getValue();
                    String valueStr = "";
                    for(int i=0; i<values.length; i++){
                        valueStr = (i==values.length) ? valueStr+values[i] : valueStr+values[i]+",";
                    }
                    params.put(key, valueStr);
                }
                LOGGER.error("request返回的数据"+ JSON.toJSONString(params));
            }

            /**
             * 在进行验签前去掉参数sign_type，通过文档知道，需要在通知返回参数列表中，除去sign、sign_type两个参数，然后再进行验签，通过查看SDK的源码，我们发现SDK提供的验签
             * 方法中已经将sign参数移除了，那么这里我们只需要去掉sign_type这个参数。
             * 同时，在进行验签的时候，要特别注意我们的签名类型是RSA2
             */
            //非常重要，需要验证回调是不是支付宝发出的，防止被恶意攻击，同时也要避免支付宝的重复通知
            boolean signVerified = true;
//            signVerified = AlipaySignature.rsaCheckV1(params, nacosFungoCircleConfig.getAlipayPublicKey(), nacosFungoCircleConfig.getAlipayCharset(), nacosFungoCircleConfig.getAlipaySignType()); //调用SDK验证签名
            if(signVerified){
                ResultDto<String> resultDto =  memberPlayLogService.saveAliMemberPalyLog(request, params);
            }
        } catch (Exception e) {
            LOGGER.error("支付宝回调验证异常", e);
            return "failed";
        }
            return "success";
    }

    /**
     * 功能描述: 微信异步回调接口
     * @date: 2019/10/25 10:34
     */
    @RequestMapping(value = {"/api/system/wechat","/api/system/weixinpay"},method = RequestMethod.POST)
    public void saveWeiXinpay( HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultDto<String> resultDto = null;
        try {
            InputStream inputStream =  request.getInputStream();
            //BufferedReader是包装设计模式，性能更搞
            BufferedReader in =  new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            StringBuffer sb = new StringBuffer();
            //1、将微信回调信息转为字符串
            String line ;
            while ((line = in.readLine()) != null){
                sb.append(line);
            }
            in.close();
            inputStream.close();

            //2、将xml格式字符串格式转为map集合
            Map<String,String> callbackMap = WXPayUtil.xmlToMap(sb.toString());
            System.out.println(callbackMap.toString());
            //3、转为有序的map
            SortedMap<String,String> sortedMap = WXPayUtil.getSortedMap(callbackMap);
            //4、判断签名是否正确
            boolean isValidate = true;
//            isValidate = WXPayUtil.isCorrectSign(sortedMap,nacosFungoCircleConfig.getWechatAppKey());
            if(isValidate){
                memberPlayLogService.saveWeChatMemberPalyLog( sortedMap,request,response);
                response.setContentType("text/xml");
                response.getWriter().println("success");
            }else {
                LOGGER.error("微信异步回调接口验签失败");
                response.setContentType("text/xml");
                response.getWriter().println("fail");
            }
        }catch (Exception e){
            LOGGER.error( "微信异步回调接口异常",e);
            //7、通知微信订单处理失败
            response.setContentType("text/xml");
            response.getWriter().println("fail");
        }
            return;
    }

    /**
     * 功能描述: vip是否隐藏接口
     * @date: 2019/10/25 10:34
     */
    @PostMapping(value = "/api/system/vip/hide")
    @ResponseBody
    public ResultDto<HashMap<String, Object>> getVipHide(HttpServletRequest request,@RequestBody Map<String,Object> hashMap){

        String appVersion = "2.5.1";
        if(StringUtils.isNoneBlank( (String) hashMap.get("appversion") )){
            appVersion = (String) hashMap.get("appversion");
        }
        String os = request.getHeader("os");
        if(os == null){
            os = "";
        }
        //app渠道编码
        String app_channel = null;
        if(StringUtils.isNoneBlank( (String) hashMap.get("appChannel") )){
            app_channel = (String) hashMap.get("appChannel");
        }
       return sysVersionService.getVipHide(appVersion,os,app_channel );
    }


    @Autowired
    private GameProxyServiceImpl gameProxyServiceImpl;

    @Autowired
    private MQProduct mqProduct;

    @GetMapping("/api/system/test")
    public void test(){

        CmmPostDto postParam = new CmmPostDto();
        postParam.setId("0009701ab56b4b4197992e4c63abe69b");
        postParam.setState(1);
        CmmPostDto post = gameProxyServiceImpl.selectCmmPostById(postParam);
        post.setVideoCoverImage("http://outin-d8556079df3311e89ef400163e1c8a9c.oss-cn-beijing.aliyuncs.com/283ba9f7dc424be898d9e8c29106e435/snapshots/08f3503ab15e42a29e345b132df3dcef-00003.jpg");
        post.setVideo("http://outin-d8556079df3311e89ef400163e1c8a9c.oss-cn-beijing.aliyuncs.com/283ba9f7dc424be898d9e8c29106e435/3a725df6c6c64fa6aa73fce21095bc1c-624bb34fa1dcbd916cad9c298da6787c-sd.mp4");
        post.setState(1);
        post.setUpdatedAt(new Date());
        post.setVideoUrls("[{\"format\":\"mp4\",\"duration\":58,\"encrypt\":false,\"fps\":25,\"bitrate\":1688,\"width\":1920,\"status\":\"success\",\"height\":1080,\"fileUrl\":\"http://outin-d8556079df3311e89ef400163e1c8a9c.oss-cn-beijing.aliyuncs.com/283ba9f7dc424be898d9e8c29106e435/3a725df6c6c64fa6aa73fce21095bc1c-624bb34fa1dcbd916cad9c298da6787c-sd.mp4\",\"definition\":\"SD\",\"size\":12311141}]");
        // @todo
        //                postService.updateById(post);
        mqProduct.postUpdate(post);
    }
}
