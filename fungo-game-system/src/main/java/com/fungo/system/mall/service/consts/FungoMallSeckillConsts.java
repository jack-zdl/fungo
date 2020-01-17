package com.fungo.system.mall.service.consts;

/**
 * <p>
 *    fungo商城-秒杀业务基本数据定义
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public class FungoMallSeckillConsts {

    /**
     * AES 秘钥的加盐
     */
    public static final String AES_SALT = "885e3615218dd26916";


    /**
     * 秒杀活动日期
     */
    public static final String[] SECKILL_DATE = new String[]{
            "2019-02-03", "2019-02-04",
            "2019-02-05", "2019-02-06",
            "2019-02-07", "2019-02-08",
            "2019-02-09", "2019-02-10"
    };


    /**
     * 秒杀开始日期 int型
     */
    public static final int SECKILL_START_DATE = 20190401;


    /**
     * 秒杀结束日期
     */
    public static final int SECKILL_END_DATE = 20201231;


    /**
     * 秒杀开始日期 String型且有日期格式
     */
    public static final String SECKILL_START_DATE_FORMAT = "2019-04-01";


    /**
     * 秒杀开始时间
     */
    public static final String SECKILL_START_TIME = "00:00:00";


    /**
     * 秒杀结束时间
     */
    public static final String SECKILL_END_TIME = "23:59:59";


    /**
     * 秒杀商品缓存key
     *
     */
    public static final String CACHE_KEY_MALL_SECKILL_GOODS = "MallSeckill_Goods_";


    /**
     * 秒杀订单缓存key
     *
     */
    public static final String CACHE_KEY_MALL_SECKILL_ORDER = "MallSeckill_order_";

    /**
     * 订单过期时长 4小时
     *
     */
    public static final int ORDER_EXPIRE_HOUR = 4;


    //-----------------秒杀成功系统消息模板---------------------
    /**
     * 1 实物
     * 2 虚拟物品
     *    21 零卡
     *    22 京东卡
     *    23 QB卡
     */
    /**
     * 零卡 消息
     */
    public static final String MSG_SECKILL_SUCCESS_LINGKA = "恭喜您获得“<span style='color:#242529'>零卡网络加速器付费版（1个月）</span>”，兑换码为：{ \n" +
            " 请至官网：<a href='https://www.linkersocks.com/user/account'><span style='color:#4995D7'>https://www.linkersocks.com/user/account</span> </a>进行兑换";


    /**
     * QB 10元 消息
     */
    public static final String MSG_SECKILL_SUCCESS_QB_TEN = "恭喜您获得“<span style='color:#242529'>10QB</span>”， 卡号：<span style='color:#242529'>{</span>  密码为：<span style='color:#242529'>}</span> \n" +
            " 请至官网：<a href='https://pay.qq.com'><span style='color:#4995D7'>https://pay.qq.com/</span> </a>进行兑换";

    /**
     *  京东卡 10元消息
     */
    public static final String MSG_SECKILL_SUCCESS_JD_TEN = "恭喜您获得“<span style='color:#242529'>京东卡（10元）</span>”， 卡号为：<span style='color:#242529'>{</span>， 密码为：<span style='color:#242529'>}</span> \n" +
            "请在 <span style='color:#242529'>[</span> 期间至京东App-我的钱包-礼品卡界面兑换";


    /**
     *  实物商品消息
     */
    public static final String MSG_SECKILL_SUCCESS_physical = "恭喜您兑换得 “<span style='color:#242529'>$</span>” ！我们将在3个工作日内为您邮寄！\n" +
            "请耐心等待，同时您可在我的-礼包乐园-我的礼品查看您的兑换记录。感谢支持小Fun，要天天开心哦～";

    /**
     *  游戏礼包购买成功消息
     */
    public static final String MSG_GAME_GOODS_SUCCESS = "领取成功!\n" + "兑换码为 “$”，点击复制可直接前往游戏兑换。";

    public static final String FESTIVAL_GOODS_SUCCESS_lv1 = "恭喜获得“{”，激活码为“}”，请至steam客户端左下角“添加游戏”进行激活";

    /**
     * QB 10元 消息
     */
    public static final String FESTIVAL_GOODS_SUCCESS_lv2 = "恭喜您获得“<span style='color:#242529'>$</span>”， 卡号：<span style='color:#242529'>{</span>  密码为：<span style='color:#242529'>}</span> \n" +
            " 请至官网：<a href='https://pay.qq.com'><span style='color:#4995D7'>https://pay.qq.com/</span> </a>进行兑换";

//    public static final String FESTIVAL_GOODS_SUCCESS_lv2 = "恭喜获得“喜加一Lv.2”，激活码为“xxxxx”，请至steam客户端左下角“添加游戏”进行激活";
    public static final String FESTIVAL_GOODS_SUCCESS_lv3 = "恭喜获得“喜加一Lv.3”，激活码为“xxxxx”，请至steam客户端左下角“添加游戏”进行激活";
    public static final String FESTIVAL_GOODS_SUCCESS_lv4 = "恭喜获得“随机大作”，激活码为“xxxxx”，请至steam客户端左下角“添加游戏”进行激活";

    public static final String FESTIVAL_GOODS_SUCCESS_lv5 = "恭喜获得“一箱快乐水”！正确填写收货信息我们将在3个工作日内为您邮寄！";
    public static final String FESTIVAL_GOODS_SUCCESS_lv6 = "恭喜获得“口袋妖怪摆件”！正确填写收货信息我们将在3个工作日内为您邮寄！";
    public static final String FESTIVAL_GOODS_SUCCESS_lv7 = "恭喜获得“小米蓝牙耳机”！正确填写收货信息我们将在3个工作日内为您邮寄！";
    public static final String FESTIVAL_GOODS_SUCCESS_lv8 = "恭喜您获得“100QB”，兑换码为：XXXXXXXXXX 请至官网：https://pay.qq.com/进行兑换";

    //恭喜您! 兑换成功∩_∩请在启动-福利-体验券查看或使用该礼品
    /**
     *  零卡商品消息
     */
    public static final String MSG_SECKILL_SUCCESS_LINGKA_BAIJINVIP = "恭喜您获得“<span style='color:#242529'> { </span>”！您可在启动-福利-体验券查看并使用您的礼品。感谢支持小Fun,要天天开心哦~";


    //"恭喜你！兑换成功∩_∩请在我的消息-通知栏查看您的谷歌账号密码";
    /**
     *  零卡谷歌商品消息
     */
    public static final String MSG_SECKILL_SUCCESS_LINGKA_GOOGLE = "恭喜您兑换得“谷歌账号“，账号为：{ ，密码为：}，辅助邮箱为：+ ， 您可在我的-礼包乐园-我的礼品查看您的兑换记录。感谢支持小Fun，要天天开心哦～";
//            "恭喜您获得“<span style='color:#242529'> { </span>”！您可在我的-礼包乐园-我的礼包查询你的兑换记录。感谢支持小Fun,要天天开心哦~";

}
