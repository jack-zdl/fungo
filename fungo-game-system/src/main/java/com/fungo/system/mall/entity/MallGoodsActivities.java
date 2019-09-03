package com.fungo.system.mall.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@ToString
@TableName("t_mall_goods_activities")
public class MallGoodsActivities extends Model<MallGoodsActivities> {

    private static final long serialVersionUID = 1L;

    /**
     * 功能描述: 主键
     * @auther: dl.zhang
     * @date: 2019/7/23 10:43
     */
    @TableId(value = "id",type = IdType.UUID)
    private String id;

    /**
     * 功能描述:
     * @param: t_mall_goods的主键,逻辑外键
     * @auther: dl.zhang
     * @date: 2019/7/23 10:37
     */
    @TableField(value = "mall_goods_id")
    private String mallGoodsId;

    /**
     * 功能描述:活动类型1 中秋活动 2 待增加
     * @auther: dl.zhang
     * @date: 2019/7/23 10:43
     */
    @TableField(value = "type")
    private int type;

    /**
     * 功能描述: 1 N, 2 R , 3 SR ,4 SSR
     * @auther: dl.zhang
     * @date: 2019/7/23 10:44
     */
    @TableField(value = "mall_goods_color_type")
    private String mallGoodsColorType;

    /**
     * 功能描述: 是否有效
     * @auther: dl.zhang
     * @date: 2019/7/23 10:44
     */
    @TableField(value = "isactive")
    private String isactive;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private Date createdAt;
    /**
     * 更新时间
     */
    @TableField("updated_at")
    private Date updatedAt;
    /**
     * 创建人
     */
    @TableField("created_by")
    private String createdBy;
    /**
     * 创建人名称
     */
    @TableField("updated_by")
    private String updatedBy;

    /**
     * 功能描述: 版本
     * @auther: dl.zhang
     * @date: 2019/7/23 10:44
     */
    @TableField(value = "rversion")
    private int rversion;

    /**
     * 功能描述: 描述
     * @auther: dl.zhang
     * @date: 2019/7/23 10:45
     */
    @TableField(value = "description")
    private String description;



    @Override
    protected Serializable pkVal() {
        return null;
    }
}
