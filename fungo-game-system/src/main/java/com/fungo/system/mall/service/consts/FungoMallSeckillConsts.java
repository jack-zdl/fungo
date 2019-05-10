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
            "请在 <span style='color:#242529'>[</span> 前至京东App-我的钱包-礼品卡界面兑换";


    /**
     *  实物商品消息
     */
    public static final String MSG_SECKILL_SUCCESS_physical = "恭喜您兑换得 “<span style='color:#242529'>$</span>” ！我们将在3个工作日内为您邮寄！\n" +
            "请耐心等待，同时您可在我的-礼包乐园-我的礼品查看您的兑换记录。感谢支持小Fun，要天天开心哦～";


}
