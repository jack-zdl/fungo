package com.fungo.system.helper;

import com.alibaba.fastjson.JSONObject;
import com.fungo.system.constts.ThirdPartyResources;
import com.game.common.consts.Setting;
import com.game.common.dto.ThirdPartyUser;
import com.game.common.util.exception.BusinessException;
import com.game.common.util.network.HttpUtil;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 第三方登录辅助类
 *
 */
@Component
public class ThirdPartyLoginHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyLoginHelper.class);


    /**
     * 获取用户QQ平台的UnionID
     * 同一用户，对同一个QQ互联平台下的不同应用，unionID是相同的
     * https请求方式：GET
     * @param access_token
     * @return
     */
    public static String getUserUnionIDFromQQ(String access_token) {

        StringBuffer getURL = new StringBuffer();

        getURL.append(ThirdPartyResources.QQ_UNION_ID_URL);
        getURL.append("?")
                .append("access_token=")
                .append(access_token)
                .append("&unionid=1");

        String url = getURL.toString();
        String res = HttpUtil.httpClientPost(url);

        LOGGER.info("-getUserUnionIDFromQQ-url:{}---result:{}", url, res);

        if (StringUtils.isNotBlank(res)) {

            res = StringUtils.replace(res, "callback", "");
            res = StringUtils.replace(res, "(", "");
            res = StringUtils.replace(res, ")", "");
            res = StringUtils.replace(res, ";", "");

            JSONObject json = JSONObject.parseObject(res);
            if (json.containsKey("unionid")) {
                return json.getString("unionid");
            }
        }

        return null;
    }


    /**
     * 获取QQ用户信息
     *
     * @param token
     * @param openid
     */
    public static final ThirdPartyUser getQQUserinfo(String token, String openid) throws Exception {
        ThirdPartyUser user = new ThirdPartyUser();
        String url = ThirdPartyResources.GETUserInfoURLQQ;
        url = url + "?format=json&access_token=" + token + "&oauth_consumer_key="
                + ThirdPartyResources.AppIdQQ + "&openid=" + openid;
        String res = HttpUtil.httpClientPost(url);
        JSONObject json = JSONObject.parseObject(res);
        if (json.getIntValue("ret") == 0) {
            user.setUserName(json.getString("nickname"));
            String img = json.getString("figureurl_qq_2");
            if (img == null || "".equals(img)) {
                img = json.getString("figureurl_qq_1");
            }
            user.setAvatarUrl(img);
            String sex = json.getString("gender");
            if ("女".equals(sex)) {
                user.setGender("0");
            } else {
                user.setGender("1");
            }

            //同步获取用户QQ平台的UnionID
            String unionid = getUserUnionIDFromQQ(token);
            if (StringUtils.isNotBlank(unionid)) {
                user.setUnionid(unionid);
            }

        } else {
//			throw new IllegalArgumentException(json.getString("msg"));
            throw new BusinessException("-1", json.getString("msg"));
        }
        LOGGER.info("---getQQUserinfo:{}", user.toString());
        return user;
    }

    /**
     * 获取微信用户信息
     * https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419316518&token=e5bdca0aa07217906a15eaa4ede9ffe99ae86d85&lang=zh_CN
     * 响应结果：
     * 正确的Json返回结果：
     *
     * {
     * "openid":"OPENID",
     * "nickname":"NICKNAME",
     * "sex":1,
     * "province":"PROVINCE",
     * "city":"CITY",
     * "country":"COUNTRY",
     * "headimgurl": "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
     * "privilege":[
     * "PRIVILEGE1",
     * "PRIVILEGE2"
     * ],
     * "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
     * }
     *  参数				   说明
     * openid			    普通用户的标识，对当前开发者帐号唯一
     * nickname				普通用户昵称
     * sex					普通用户性别，1为男性，2为女性
     * province				普通用户个人资料填写的省份
     * city					普通用户个人资料填写的城市
     * country				国家，如中国为CN
     * headimgurl			用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
     * privilege			用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
     * unionid				用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
     */
    public static final ThirdPartyUser getWxUserinfo(String token, String openid) throws Exception {
        ThirdPartyUser user = new ThirdPartyUser();
        String url = ThirdPartyResources.GETUserInfoURLWX;
        url = url + "?access_token=" + token + "&openid=" + openid;
        String res = HttpUtil.httpClientPost(url);
        JSONObject json = JSONObject.parseObject(res);
        if (json.getString("errcode") == null) {
            String wx_nickname = json.getString("nickname");
            //编码转换
            if (StringUtils.isNotBlank(wx_nickname)) {
                wx_nickname = new String(wx_nickname.getBytes("ISO-8859-1"), "UTF-8");
            }
            user.setUserName(wx_nickname);
            String img = json.getString("headimgurl");
            if (img != null && !"".equals(img)) {
                user.setAvatarUrl(img);
            }
            String sex = json.getString("sex");
            if ("0".equals(sex)) {
                user.setGender("0");
            } else {
                user.setGender("1");
            }

            //unionid
            String unionid = json.getString("unionid");
            user.setUnionid(unionid);

        } else {
            throw new BusinessException("-1", json.getString("errcode"));
        }
        LOGGER.info("---getWxUserinfo:{}", user.toString());
        return user;
    }

    /**
     * docs:
     * https://open.weibo.com/wiki/2/users/show
     * 获取新浪用户信息
     *请求参数
     *  	必选	类型及范围	说明
     * access_token	true	string	采用OAuth授权方式为必填参数，OAuth授权后获得。
     * uid	false	int64	需要查询的用户ID。
     * screen_name	false	string	需要查询的用户昵称。
     *
     * 返回结果:
     * {
     *     "id": 1404376560,
     *     "screen_name": "zaku",
     *     "name": "zaku",
     *     "province": "11",
     *     "city": "5",
     *     "location": "北京 朝阳区",
     *     "description": "人生五十年，乃如梦如幻；有生斯有死，壮士复何憾。",
     *     "url": "http://blog.sina.com.cn/zaku",
     *     "profile_image_url": "http://tp1.sinaimg.cn/1404376560/50/0/1",
     *     "domain": "zaku",
     *     "gender": "m",
     *     "followers_count": 1204,
     *     "friends_count": 447,
     *     "statuses_count": 2908,
     *     "favourites_count": 0,
     *     "created_at": "Fri Aug 28 00:00:00 +0800 2009",
     *     "following": false,
     *     "allow_all_act_msg": false,
     *     "geo_enabled": true,
     *     "verified": false,
     *     "status": {
     *         "created_at": "Tue May 24 18:04:53 +0800 2011",
     *         "id": 11142488790,
     *         "text": "我的相机到了。",
     *         "source": "<a href="http://weibo.com" rel="nofollow">新浪微博</a>",
     *         "favorited": false,
     *         "truncated": false,
     *         "in_reply_to_status_id": "",
     *         "in_reply_to_user_id": "",
     *         "in_reply_to_screen_name": "",
     *         "geo": null,
     *         "mid": "5610221544300749636",
     *         "annotations": [],
     *         "reposts_count": 5,
     *         "comments_count": 8
     *     },
     *     "allow_all_comment": true,
     *     "avatar_large": "http://tp1.sinaimg.cn/1404376560/180/0/1",
     *     "verified_reason": "",
     *     "follow_me": false,
     *     "online_status": 0,
     *     "bi_followers_count": 215
     * }
     *返回字段说明
     * 返回值字段	字段类型	字段说明
     * id	int64	用户UID
     * idstr	string	字符串型的用户UID
     * screen_name	string	用户昵称
     * name	string	友好显示名称
     * province	int	用户所在省级ID
     * city	int	用户所在城市ID
     * location	string	用户所在地
     * description	string	用户个人描述
     * url	string	用户博客地址
     * profile_image_url	string	用户头像地址（中图），50×50像素
     * profile_url	string	用户的微博统一URL地址
     * domain	string	用户的个性化域名
     * weihao	string	用户的微号
     * gender	string	性别，m：男、f：女、n：未知
     * followers_count	int	粉丝数
     * friends_count	int	关注数
     * statuses_count	int	微博数
     * favourites_count	int	收藏数
     * created_at	string	用户创建（注册）时间
     * following	boolean	暂未支持
     * allow_all_act_msg	boolean	是否允许所有人给我发私信，true：是，false：否
     * geo_enabled	boolean	是否允许标识用户的地理位置，true：是，false：否
     * verified	boolean	是否是微博认证用户，即加V用户，true：是，false：否
     * verified_type	int	暂未支持
     * remark	string	用户备注信息，只有在查询用户关系时才返回此字段
     * status	object	用户的最近一条微博信息字段 详细
     * allow_all_comment	boolean	是否允许所有人对我的微博进行评论，true：是，false：否
     * avatar_large	string	用户头像地址（大图），180×180像素
     * avatar_hd	string	用户头像地址（高清），高清头像原图
     * verified_reason	string	认证原因
     * follow_me	boolean	该用户是否关注当前登录用户，true：是，false：否
     * online_status	int	用户的在线状态，0：不在线、1：在线
     * bi_followers_count	int	用户的互粉数
     * lang	string	用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语
     * @param token
     * @param uid
     * @return
     */
    public static final ThirdPartyUser getSinaUserinfo(String token, String uid) throws Exception {
        ThirdPartyUser user = new ThirdPartyUser();
        String url = ThirdPartyResources.GetUserInfoURLSina;
        url = url + "?access_token=" + token + "&uid=" + uid;
        String res = HttpUtil.httpClientPost(url);
        JSONObject json = JSONObject.parseObject(res);
        if (json.getString("error") == null) {
            String name = json.getString("name");
            String nickName = StringUtils.isBlank(json.getString("screen_name")) ? name : json.getString("screen_name");
            user.setAvatarUrl(json.getString("avatar_large"));
            user.setUserName(nickName);
            if ("f".equals(json.getString("gender"))) {
                user.setGender("0");
            } else {
                user.setGender("1");
            }
            user.setToken(token);
            user.setOpenid(uid);
            user.setProvider("sina");
        } else {
            throw new BusinessException("-1", json.getString("error") + "[" + json.getString("error_code") + "]");
        }
        LOGGER.info("---getSinaUserinfo:{}", user.toString());

        return user;
    }

    /**
     * 获取QQ的认证token和用户OpenID
     *
     * @param code
     * @param type
     * @return
     */
    public static final Map<String, String> getQQTokenAndOpenid(String code, String host) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        // 获取令牌
        String tokenUrl = ThirdPartyResources.AccessTokenURLQQ;
        tokenUrl = tokenUrl + "?grant_type=authorization_code&client_id=" + ThirdPartyResources.AppIdQQ
                + "&client_secret=" + ThirdPartyResources.AppKeyQQ + "&code=" + code
                + "&redirect_uri=" + getRedirectUrl(Setting.RUN_ENVIRONMENT);
        ;
        String tokenRes = HttpUtil.httpClientPost(tokenUrl);
        if (tokenRes != null && tokenRes.indexOf("access_token") > -1) {
            Map<String, String> tokenMap = toMap(tokenRes);
            map.put("access_token", tokenMap.get("access_token"));
            // 获取QQ用户的唯一标识openID
            String openIdUrl = ThirdPartyResources.GetOpenIDURLQQ;
            openIdUrl = openIdUrl + "?access_token=" + tokenMap.get("access_token");
            String openIdRes = HttpUtil.httpClientPost(openIdUrl);
            int i = openIdRes.indexOf("(");
            int j = openIdRes.indexOf(")");
            openIdRes = openIdRes.substring(i + 1, j);
            JSONObject openidObj = JSONObject.parseObject(openIdRes);
            map.put("openId", openidObj.getString("openid"));
        } else {
            throw new IllegalArgumentException("获取qq用户Token发生错误[" + tokenRes + "]");
        }
        return map;
    }

    /**
     * 获取微信的认证token和用户OpenID
     *
     * @param code
     * @param type
     * @return
     */
    public static final JSONObject getWxTokenAndOpenid(String code, String host) throws Exception {
//		Map<String, String> map = new HashMap<String, String>();
        // 获取令牌
        String tokenUrl = ThirdPartyResources.AccessTokenURLWX;
        tokenUrl = tokenUrl + "?appid=" + ThirdPartyResources.AppIdWX + "&secret="
                + ThirdPartyResources.AppKeyWX + "&code=" + code + "&grant_type=authorization_code";
        String tokenRes = HttpUtil.httpClientPost(tokenUrl);
        System.out.println(tokenRes);
        JSONObject json = null;
        if (tokenRes != null && tokenRes.indexOf("access_token") > -1) {
//			Map<String, String> tokenMap = toMap(tokenRes);
//			map.put("access_token", tokenMap.get("access_token"));
//			// 获取微信用户的唯一标识openid
//			map.put("openId", tokenMap.get("openid"));
            json = JSONObject.parseObject(tokenRes);
        } else {
//			throw new IllegalArgumentException(ThirdPartyResources.getMessage("THIRDPARTY.LOGIN.NOTOKEN", "weixin"));
            throw new BusinessException("-1", "获得微信用户token信息失败");
        }
        return json;
    }

    /**
     *   获取授权
     *   获取新浪登录认证token和用户id
     *
     *        HTTP请求方式
     *        POST
     *
     *       请求参数
     *       必选	类型及范围	说明
     *       client_id	true	string	申请应用时分配的AppKey。
     *       client_secret	true	string	申请应用时分配的AppSecret。
     *       grant_type	true	string	请求的类型，填写authorization_code
     *
     *       grant_type为authorization_code时
     *       必选	类型及范围	说明
     *       code	true	string	调用authorize获得的code值。
     *       redirect_uri	true	string	回调地址，需需与注册应用里的回调地址一致。
     *
     *       返回数据
     *       {
     *       "access_token": "ACCESS_TOKEN",
     *       "expires_in": 1234,
     *       "remind_in":"798114",
     *       "uid":"12341234"
     *       }
     *
     *       返回值字段	字段类型	字段说明
     *       access_token	string	用户授权的唯一票据，用于调用微博的开放接口，同时也是第三方应用验证微博用户登录的唯一票据，第三方应用应该用该票据和自己应用内的用户建立唯一影射关系，来识别登录状态，不能使用本返回值里的UID字段来做登录识别。
     *       expires_in	string	access_token的生命周期，单位是秒数。
     *       remind_in	string	access_token的生命周期（该参数即将废弃，开发者请使用expires_in）。
     *       uid	string	授权用户的UID，本字段只是为了方便开发者，减少一次user/show接口调用而返回的，第三方应用不能用此字段作为用户登录状态的识别，只有access_token才是用户授权的唯一票据。
     * @param code
     * @param type
     * @return
     */
    public static final JSONObject getSinaTokenAndUid(String code, String host) {
        JSONObject json = null;
        try {
            // 获取令牌
            String tokenUrl = ThirdPartyResources.AccessTokenURLSina;
            ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
            NameValuePair params1 = new NameValuePair();
            params1.setName("client_id");
            params1.setValue(ThirdPartyResources.AppIdSina);
            list.add(params1);
            NameValuePair params2 = new NameValuePair();
            params2.setName("client_secret");
            params2.setValue(ThirdPartyResources.AppKeySina);
            list.add(params2);
            NameValuePair params3 = new NameValuePair();
            params3.setName("grant_type");
            params3.setValue("authorization_code");
            list.add(params3);
            NameValuePair params4 = new NameValuePair();
            params4.setName("redirect_uri");
            params4.setValue(getRedirectUrl(Setting.RUN_ENVIRONMENT));
            list.add(params4);
            NameValuePair params5 = new NameValuePair();
            params5.setName("code");
            params5.setValue(code);
            list.add(params5);
            String tokenRes = HttpUtil.httpClientPost(tokenUrl, list);
            // String tokenRes = httpClient(tokenUrl);
            // {"access_token":"2.00AvYzKGWraycB344b3eb242NUbiQB","remind_in":"157679999","expires_in":157679999,"uid":"5659232590"}
            if (tokenRes != null && tokenRes.indexOf("access_token") > -1) {
                json = JSONObject.parseObject(tokenRes);
            } else {
//				throw new IllegalArgumentException(ThirdPartyResources.getMessage("THIRDPARTY.LOGIN.NOTOKEN", "sina"));
                throw new BusinessException("-1", "获得新浪微博用户token信息失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
//			logger.error(e);
        }
        return json;
    }

    /**
     * 将格式为s1&s2&s3...的字符串转化成Map集合
     *
     * @param str
     * @return
     */
    private static final Map<String, String> toMap(String str) {
        Map<String, String> map = new HashMap<String, String>();
        String[] strs = str.split("&");
        for (int i = 0; i < strs.length; i++) {
            String[] ss = strs[i].split("=");
            map.put(ss[0], ss[1]);
        }
        return map;
    }

    /**
     * 判断新浪用户是否存在
     * @param token
     * @param uid
     * @return
     * @throws Exception
     */
    public static final boolean isSinaUser(String token, String uid) throws Exception {
        ThirdPartyUser user = new ThirdPartyUser();
        String url = ThirdPartyResources.GetUserInfoURLSina;
        url = url + "?access_token=" + token + "&uid=" + uid;
        String res = HttpUtil.httpClientPost(url);
        JSONObject json = JSONObject.parseObject(res);
        if (json.getString("error") == null) {
            return true;
        } else {
            return false;
        }
    }

    /** 获取微信用户是否存在 */
    public static final boolean isWxUser(String token, String openid) throws Exception {
        ThirdPartyUser user = new ThirdPartyUser();
        String url = ThirdPartyResources.GETUserInfoURLWX;
        url = url + "?access_token=" + token + "&openid=" + openid;
        String res = HttpUtil.httpClientPost(url);
        JSONObject json = JSONObject.parseObject(res);
        if (json.getString("errcode") == null) {
            return true;
        } else {
            return false;
        }
    }

    /** 判断QQ用户是否存在 */
    public static final boolean isQQUser(String token, String openid, String channel) throws Exception {
        ThirdPartyUser user = new ThirdPartyUser();
        String url = "https://graph.qq.com/user/get_user_info";
        if ("iOS".equals(channel)) {
            url = url + "?format=json&access_token=" + token + "&oauth_consumer_key="
                    + ThirdPartyResources.AppIdQQIos + "&openid=" + openid;
        } else if ("pc".equals(channel)) {
            url = url + "?format=json&access_token=" + token + "&oauth_consumer_key="
                    + ThirdPartyResources.AppIdQQ + "&openid=" + openid;
        } else {
            url = url + "?format=json&access_token=" + token + "&oauth_consumer_key="
                    + ThirdPartyResources.AppIdQQAndroid + "&openid=" + openid;
        }
        String res = HttpUtil.httpClientPost(url);
        JSONObject json = JSONObject.parseObject(res);
        if (json.getIntValue("ret") == 0) {
            return true;
        } else {
            return false;
        }
    }

    private static String getRedirectUrl(String env) {
        if ("dev".equals(env)) {
            return ThirdPartyResources.GlobalRedirectURLDev;
        } else if ("pro".equals(env)) {
            return ThirdPartyResources.GlobalRedirectURLPro;
        } else {
            throw new BusinessException("-1", "无法识别当前运行环境");
        }

    }


}
