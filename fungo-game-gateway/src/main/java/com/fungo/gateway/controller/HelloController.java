package com.fungo.gateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.fungo.gateway.common.DecodeUrl;
import com.fungo.gateway.config.NacosFungoCircleConfig;
import com.fungo.gateway.utils.DateTools;
import com.fungo.gateway.utils.RSAEncryptUtils;
import com.fungo.gateway.utils.ResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 功能说明: <br>
 * 系统版本: 2.0 <br>
 * 开发人员: zhangdl <br>
 * 开发时间: 2018/3/1 16:07<br>
 */
@RestController
public class HelloController {

    @Autowired
    private NacosFungoCircleConfig nacosFungoCircleConfig;

    @RequestMapping("/api/gateway/hello")
    public String index(@RequestParam String name) {
        return "hello "+name+"，this is two messge";
    }

    @RequestMapping("/api/gateway/rsa")
    public ResultDto<Object> rsaEncrypt(){
        ResultDto<Object> resultDto = null;
        //生成公钥和私钥
        String messageEn = "";
        try {
            //加密字符串
            long days = DateTools.getAddDays( nacosFungoCircleConfig.getFestivalDays());
            String publicKey = nacosFungoCircleConfig.getRsaPublicKey(); //"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpt9/cMYK42vKTrTQZxXlaJ41SEffYdm8Mcdac0yE4MKEoocChuzCWpo8gGUsmaL620rfmgEOsxqb8suYcTxOGltwWphi5wBQJoIIW8sJx7dZpU2anxtuDsMsNQFlsJyQZvA8frNrfD6ezyTv9khrKbBFmIAk9oV9um2dfXfn8bwIDAQAB";
            JSONObject jsonObject = new JSONObject( );
            jsonObject.put( "days",days );
            jsonObject.put( "publicKey",publicKey );
            resultDto = ResultDto.ResultDtoFactory.buildSuccess(jsonObject );
        } catch (Exception e) {
            e.printStackTrace();
            resultDto = ResultDto.ResultDtoFactory.buildError( "获取公钥和时间戳异常" );
        }
        return resultDto;
    }

    @RequestMapping("/api/gateway/rsa/decode/{type}")
    @ResponseBody
    public ResultDto<Object> rsaDecode(@PathVariable("type") String type, @RequestBody DecodeUrl url){
        ResultDto<Object> resultDto = null;
        //生成公钥和私钥
        try {
            //加密字符串
            long days = DateTools.getAddDays( nacosFungoCircleConfig.getFestivalDays());
            String publicKey = nacosFungoCircleConfig.getRsaPublicKey();
            String privateKey = nacosFungoCircleConfig.getRsaPrivateKey(); //"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpt9/cMYK42vKTrTQZxXlaJ41SEffYdm8Mcdac0yE4MKEoocChuzCWpo8gGUsmaL620rfmgEOsxqb8suYcTxOGltwWphi5wBQJoIIW8sJx7dZpU2anxtuDsMsNQFlsJyQZvA8frNrfD6ezyTv9khrKbBFmIAk9oV9um2dfXfn8bwIDAQAB";
            JSONObject jsonObject = new JSONObject( );
            jsonObject.put( "days",days );
            jsonObject.put( "publicKey",privateKey );
            jsonObject.put( "privateKey",privateKey );
            if("1".equals( type )){
                String messageDe = RSAEncryptUtils.decrypt(url.getUrl(), privateKey);
                jsonObject.put( "url",messageDe );
            }else if("2".equals( type )){
                String messageDe = RSAEncryptUtils.encrypt(url.getUrl(), publicKey);
                jsonObject.put( "url",messageDe );
            }
            resultDto = ResultDto.ResultDtoFactory.buildSuccess(jsonObject );
        } catch (Exception e) {
            e.printStackTrace();
            resultDto = ResultDto.ResultDtoFactory.buildError( "获取公钥和时间戳异常" );
        }
        return resultDto;
    }
}
