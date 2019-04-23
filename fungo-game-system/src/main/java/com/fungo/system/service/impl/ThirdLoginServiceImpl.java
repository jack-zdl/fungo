package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fungo.system.dto.ThirdLoginInput;
import com.fungo.system.helper.ThirdPartyLoginHelper;
import com.fungo.system.service.ThirdLoginService;
import com.game.common.dto.ResultDto;
import com.game.common.dto.ThirdPartyUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ThirdLoginServiceImpl implements ThirdLoginService {

    @Override
    public ResultDto<String> checkThirdParty(Integer platformType, String accessToken, String openid, String channel)
            throws Exception {
        boolean flag = false;
        if (platformType == 0) {// 新浪微博
            flag = ThirdPartyLoginHelper.isSinaUser(accessToken, openid);
        } else if (platformType == 1) {// 微信
            flag = ThirdPartyLoginHelper.isWxUser(accessToken, openid);
        } else if (platformType == 4) {// QQ
            flag = ThirdPartyLoginHelper.isQQUser(accessToken, openid, channel);
        } else {
            return ResultDto.error("-1", "请求参数错误");
        }
        if (flag) {
            return ResultDto.success();
        } else {
            return ResultDto.error("-1", "验证第三方登录失败");
        }
    }

    @Override
    public ResultDto<ThirdLoginInput> wxVerify(String host, String code) throws Exception {
        if (StringUtils.isNotBlank(code)) {// 如果不为空
            // 获取token和openid
            JSONObject json = ThirdPartyLoginHelper.getWxTokenAndOpenid(code, host);
            String openId = json.getString("openid");
            if (StringUtils.isNotBlank(openId)) {// 如果openID存在
                // 获取第三方用户信息存放到session中
                ThirdPartyUser thirdUser = ThirdPartyLoginHelper.getWxUserinfo(json.getString("access_token"), openId);
                thirdUser.setProvider("WX");

                ThirdLoginInput input = new ThirdLoginInput();
                input.setAccessToken(json.getString("access_token"));
                input.setPlatformType(1);
                input.setGender(Integer.parseInt(thirdUser.getGender()));
                input.setName(thirdUser.getUserName());
                input.setOpenid(openId);
                //unionid
                input.setUnionid(thirdUser.getUnionid());
                input.setIconurl(thirdUser.getAvatarUrl());
                return ResultDto.success(input);
            } else {// 如果未获取到OpenID
                return ResultDto.error("-1", "验证失败,未获取openId");
            }
        } else {// 如果没有返回令牌，则直接返回到登录页面
            return ResultDto.error("-1", "code不能为空");
        }

    }

    @Override
    public ResultDto<ThirdLoginInput> qqVerify(String host, String code) throws Exception {

        if (StringUtils.isNotBlank(code)) {// 如果不为空
            // 获取token和openid
            Map<String, String> map = ThirdPartyLoginHelper.getQQTokenAndOpenid(code, host);
            String openId = map.get("openId");
            if (StringUtils.isNotBlank(openId)) {// 如果openID存在
                // 获取第三方用户信息存放到session中
                ThirdPartyUser thirdUser = ThirdPartyLoginHelper.getQQUserinfo(map.get("access_token"), openId);
                thirdUser.setProvider("QQ");

                ThirdLoginInput input = new ThirdLoginInput();
                input.setAccessToken(map.get("access_token"));
                input.setPlatformType(4);
                input.setGender(Integer.parseInt(thirdUser.getGender()));
                input.setName(thirdUser.getUserName());
                input.setOpenid(openId);
                input.setIconurl(thirdUser.getAvatarUrl());

                //同步获取用户QQ平台的UnionID
                input.setUnionid(thirdUser.getUnionid());

                return ResultDto.success(input);
            } else {// 如果未获取到OpenID
                return ResultDto.error("-1", "验证失败,未获取openId");
            }
        } else {
            return ResultDto.error("-1", "code不能为空");
        }

    }

    @Override
    public ResultDto<ThirdLoginInput> sinaVerify(String host, String code) throws Exception {

        if (StringUtils.isNotBlank(code)) {// 如果不为空
            // 获取token和uid
            JSONObject json = ThirdPartyLoginHelper.getSinaTokenAndUid(code, host);
            //授权用户的UID
            String uid = json.getString("uid");
            if (StringUtils.isNotBlank(uid)) {// 如果uid存在
                // 获取第三方用户信息存放到session中
                ThirdPartyUser thirdUser = ThirdPartyLoginHelper.getSinaUserinfo(json.getString("access_token"), uid);
                thirdUser.setProvider("SINA");

                ThirdLoginInput input = new ThirdLoginInput();
                input.setAccessToken(json.getString("access_token"));
                input.setPlatformType(0);
                input.setGender(Integer.parseInt(thirdUser.getGender()));
                input.setName(thirdUser.getUserName());
                input.setUid(uid);

                //sina微博下，同一用户，不同应用识别是否同一用户是uid
                input.setUnionid(uid);

                input.setIconurl(thirdUser.getAvatarUrl());
                return ResultDto.success(input);
            } else {// 如果未获取到OpenID
                return ResultDto.error("-1", "验证失败,未获取uid [" + json.getString("error_description") + "]");
            }
        } else {// 如果没有返回令牌，则直接返回到登录页面
            return ResultDto.error("-1", "code不能为空");

        }

    }

}
