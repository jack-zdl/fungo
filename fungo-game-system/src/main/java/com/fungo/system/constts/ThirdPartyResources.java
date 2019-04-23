package com.fungo.system.constts;

public class ThirdPartyResources {

	//QQ配置信息
//  public static final String AppIdQQ = "1106324018"; 
//  public static final String AppIdQQIos = "1106500902"; 
//    
//  public static final String AppKeyQQ = "eHJkifDCHO8I0OJg"; 
	public static final String AppIdQQAndroid = "1106324018";
    public static final String AppIdQQ = "101517861"; 
    public static final String AppIdQQIos = "1106500902"; 
    
    public static final String AppKeyQQ = "9f3ffe30267069520ba44ee25466c577"; 
//  public static final String RedirectUrlQQ = "/callback/qq";
//    public static final String RedirectUrlQQ = "/redirect";
    public static final String ScopeQQ = "get_user_info";
    public static final String GETUserInfoURLQQ = "https://graph.qq.com/user/get_user_info";
    public static final String AccessTokenURLQQ = "https://graph.qq.com/oauth2.0/token";
    public static final String GetOpenIDURLQQ = "https://graph.qq.com/oauth2.0/me";
    public static final String AuthorizeURLQQ = "https://graph.qq.com/oauth2.0/authorize";

    /**
     *  获取用户QQ平台的UnionID
     *  同一用户，对同一个QQ互联平台下的不同应用，unionID是相同的
     *  https请求方式：GET
     * https://graph.qq.com/oauth2.0/me?access_token=ACCESSTOKEN&unionid=1
     */
    public static final String QQ_UNION_ID_URL = "https://graph.qq.com/oauth2.0/me";


    //sina配置信息
//  public static final String AppIdSina = "596385369";
//  public static final String AppKeySina = "28713169f390416a4f3f19eb1fb7e44a";
    public static final String AppIdSina = "2862566731";
    public static final String AppKeySina = "84dae229ff39857465c0917263a2a39c";
//    public static final String RedirectURLSina = "/callback/sina.com";
    public static final String ScopeSina = "all";
    public static final String GetUserInfoURLSina = "https://api.weibo.com/2/users/show.json";
    public static final String AccessTokenURLSina = "https://api.weibo.com/oauth2/access_token";
    public static final String AuthorizeURLSina = "https://api.weibo.com/oauth2/authorize";
    
    
    //wx配置信息
//    public static final String AppIdWX = "wx2e9c3933aaea8a64";
//    public static final String AppKeyWX = "3a51828ae945b6985f740b57d9626152";
      public static final String AppIdWX = "wx9231d920bf32f043";
      public static final String AppKeyWX = "b56a002aae953512fdac1d121f2670ce";
//  public static final String RedirectUrlWX = "localhost:8080/sns/callback/wx";
//    public static final String RedirectUrlWX = "/redirect";
    public static final String ScopeWX = "snsapi_login";
    public static final String GETUserInfoURLWX = "https://api.weixin.qq.com/sns/userinfo";
    public static final String AccessTokenURLWX = "https://api.weixin.qq.com/sns/oauth2/access_token";
    public static final String AuthorizeURLWX = "https://open.weixin.qq.com/connect/qrconnect";


    
    //跳转页面
    public static final String ThirdLoginSuccess = "";
    public static final String ThirdLoginFailure = "";
    public static final String ToBindUrl = "";
    
    public static final String GlobalRedirectURLDev = "http://pc.mingbonetwork.com/redirect";
    public static final String GlobalRedirectURLPro = "http://www.fungoweb.com/redirect";
}
