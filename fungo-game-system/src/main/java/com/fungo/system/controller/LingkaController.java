package com.fungo.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fungo.system.service.MemberPlayLogService;
import com.fungo.system.service.SysVersionService;
import com.game.common.dto.ResultDto;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    @PostMapping(value = "/api/system/member/log")
    public ResultDto<String> saveLingkaMemeberPlayLog(@Validated @RequestBody MemberPlayLogVO memberPlayLogVO){
        ResultDto<String> resultDto = null;
//        resultDto = memberPlayLogService.saveMemberPalyLog( memberPlayLogVO);
        return  resultDto;
    }


    /**
     * 功能描述: 支付宝回调接口
     * @date: 2019/10/25 10:34
     */
    @PostMapping(value = "/api/system/alipay")
    @ResponseBody
    public Object  saveAlipay( HttpServletRequest request , String out_trade_no,String trade_no,String trade_status ,String total_amount, @RequestBody JSONObject alipayJson){
        System.out.println( "-------------------Post请求方式的/api/system/alipay" );
        LOGGER.error( "-------------------Post请求方式的/api/system/alipay" );
        System.out.println( "-------------------Post请求方式的/api/system/alipay參數参数="+out_trade_no+"1==="+trade_no+"1==="+trade_status+"1==="+total_amount);
        ResultDto<String> resultDto = null;
        resultDto = memberPlayLogService.saveMemberPalyLog(request, alipayJson);
        if(resultDto.getStatus() == 1){
            return "success";
        }
        return "failed";
    }

    /**
     * 功能描述: 支付宝回调接口
     * @date: 2019/10/25 10:34
     */
    @RequestMapping(value = "/api/system/alipay")
    @ResponseBody
    public Object  alipayCallback( HttpServletRequest request){
        System.out.println( "+++++++++++++++++這是是get请求方式的/api/system/alipay" );
        LOGGER.error( "+++++++++++++++這是是get请求方式的/api/system/alipay" );
        Map<String, String> params = Maps.newHashMap();

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
        LOGGER.info("支付宝回调，sign:{}, trad_status:{}, 参数:{}", params.get("sign"), params.get("trade_status"), params.toString());
        System.out.println("]]]]]]]]]]]]]+"+ JSON.toJSONString(params));
        //非常重要，需要验证回调是不是支付宝发出的，防止被恶意攻击，同时也要避免支付宝的重复通知
        /**
         * 在进行验签前去掉参数sign_type，通过文档知道，需要在通知返回参数列表中，除去sign、sign_type两个参数，然后再进行验签，通过查看SDK的源码，我们发现SDK提供的验签
         * 方法中已经将sign参数移除了，那么这里我们只需要去掉sign_type这个参数。
         * 同时，在进行验签的时候，要特别注意我们的签名类型是RSA2
         */
        params.remove("sign_type");
        //rsaCheckV2方法有个重载，一个可以指定签名类型，另外一个不用指定签名类型，默认使用的是RSA
        //在这里需要传进去的publicKey 和 签名类型都是配置在zfbinfo.properties文件中，这个文件在预下单的时候就已经通过Configs进行了加载，所以就不需要再
        //写方法来读取该文件的内容，通过查看Configs这个类型的源码发现，该类中提供了静态获取相关值得方法，所以可以直接通过Configs中的方法获取需要的参数
        try {
//            boolean rsaCheck = AlipaySignature.rsaCheckV2(params, Configs.getPublicKey(), "utf-8", Configs.getSignType());
//            if(!rsaCheck){
//                return ServerResponse.createByErrorMessage("非法请求，验证不通过，如再恶意攻击将报警");
//            }
        } catch (Exception e) {
            LOGGER.error("支付宝回调验证异常", e);
        }
        //todo 验签如果成功的话，还需要校验该订单中的订单号、总金额等信息来防止恶意攻击


        //todo 处理业务逻辑，更新订单状态，更新库存等...
//        ServerResponse response =  new ServerHttpResponse(); //iOrderService.alipayCallback(params);
//        if(response.isSuccess()){
            /**
             * 支付宝文档要求：程序执行完后必须打印输出“success”（不包含引号）。如果商户反馈给支付宝的字符不是success这7个字符，支付宝服务器会不断重发通知，直到超过24小时22分钟。
             * 一般情况下，25小时以内完成8次通知（通知的间隔频率一般是：4m,10m,10m,1h,2h,6h,15h）；
             */
            return "success";
//        }
//        return "failed";
    }

    /**
     * 功能描述: 支付宝回调接口
     * @date: 2019/10/25 10:34
     */
    @PostMapping(value = "/api/system/weixinpay")
    public ResultDto<String> saveWeiXinpay( HttpServletRequest request, HttpServletResponse response){
        ResultDto<String> resultDto = null;
        resultDto = memberPlayLogService.saveWeiXinMemberPalyLog( request,response);
        return  resultDto;
    }

    /**
     * 功能描述: 支付宝回调接口
     * @date: 2019/10/25 10:34
     */
    @GetMapping(value = "/api/system/vip/hide")
    @ResponseBody
    public ResultDto<HashMap<String, Object>> getVipHide(HttpServletRequest request){

        String appVersion = "2.5.1";
        if(StringUtils.isNoneBlank(request.getHeader("appversion"))){
            appVersion = request.getHeader("appversion");
        }
        String os = request.getHeader("os");
        if(os == null){
            os = "";
        }
       return sysVersionService.getVipHide(appVersion,os );
    }
}
