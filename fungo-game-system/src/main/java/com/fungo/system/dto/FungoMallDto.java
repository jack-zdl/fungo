package com.fungo.system.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/7/18
 */
@Setter
@Getter
@ToString
public class FungoMallDto implements Serializable {

    private static final long serialVersionUID = 4689603663640279603L;

    /**
     * 功能描述: 添加商品类型  1 虚拟  2 实物
     * @auther: dl.zhang
     * @date: 2019/7/18 13:33
     */
    private int type;

    /**
     * 功能描述: 添加商品名称
     * @auther: dl.zhang
     * @date: 2019/7/18 13:33
     */
    private String goodsName;

    /**
     * 功能描述: 添加商品代号 1 虚拟 2 实物 21 22 23
     * @auther: dl.zhang
     * @date: 2019/7/18 13:33
     */
    private Integer goodsType;

    /**
     * 功能描述: 添加商品大图片路径
     * @auther: dl.zhang
     * @date: 2019/7/18 13:33
     */
    private String bigUrl;

    /**
     * 功能描述: 添加商品小图片路径
     * @auther: dl.zhang
     * @date: 2019/7/18 13:33
     */
    private String smallUrl;
    
    /**
     * min-image 里面的status
     * @auther: dl.zhang
     * @date: 2019/7/18 13:35
     */
    private String status;
    
    /**
     * 功能描述: min-image 里面的size
     * @auther: dl.zhang
     * @date: 2019/7/18 13:36
     */
    private String size;

    /**
     * 产品状态 :
     -1 已删除 ，1 已 下架  ，  2 已 上架
     */
    private Integer goodsStatus;

    /**
     * 商品类型
     * 1 实物
     * 2 虚拟物品
     *    21 零卡
     *    22 京东卡
     *    23 QB卡
     * 3 游戏礼包
     * @auther: dl.zhang
     * @date: 2019/7/18 13:36
     */
    private String style;

    /**
     * 功能描述: 价格
     * @auther: dl.zhang
     * @date: 2019/7/18 13:43
     */
    private Long price;

    /**
     * 功能描述: 排序
     * @auther: dl.zhang
     * @date: 2019/7/18 14:04
     */
    private Integer sort;
}
