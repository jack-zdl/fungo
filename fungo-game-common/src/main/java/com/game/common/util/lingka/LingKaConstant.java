package com.game.common.util.lingka;

/**
 *  零卡对接相关常量
 */
public class LingKaConstant {

   public static final String URL = "https://www.lyn.cash/";
    /**
     *  登陸接口
     */
    public static final String LINGKA_LOGIN_URL = URL+"api/home/login";

    /**
     *  礼品绑定接口
     */
    public static final String LINGKA_GIFTCARD_BIND = URL+"api/admin/giftcard/send";

    /**
     *  礼品绑定接口
     */
    public static final String LINGKA_PAY_LOG = URL+"MingboService/listTapGames";

    /**
    * 功能描述   管理員邮箱
     * @date: 2019/10/21 18:11
     */
    public static final String ADMIN_EMAIL = "mingbo@lyn.cash";

    /**
     * 功能描述   管理員密码
     * @date: 2019/10/21 18:11
     */
    public static final String ADMIN_PASSWORD = "123456";
}
