package com.fungo.system.entity;

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
@TableName("t_mall_seckill_order")
public class MallSeckillOrder extends Model<MallSeckillOrder> {

    /**
     * 功能描述: 主键
     * @auther: dl.zhang
     * @date: 2019/7/23 10:43
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 功能描述: t_mall_goods的主键,逻辑外键
     * @auther: dl.zhang
     * @date: 2019/7/23 10:44
     */
    @TableField(value = "mall_goods_id")
    private String mallGoodsId;
    /**
     * 功能描述: 总库存
     * @auther: dl.zhang
     * @date: 2019/7/23 10:44
     */
    @TableField(value = "total_stock")
    private Integer totalStock;
    /**
     * 功能描述: 剩余库存
     * @auther: dl.zhang
     * @date: 2019/7/23 10:44
     */
    @TableField(value = "residue_stock")
    private Integer residueStock;

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
    @TableField("created_by")
    private String createdBy;

    /**
     * 更新时间
     */
    @TableField("created_at")
    private Date createdAt;

    /**
     * 创建人
     */
    @TableField("updated_by")
    private String updatedBy;

    /**
     * 创建人名称
     */
    @TableField("updated_at")
    private Date updatedAt;

    /**
     * 功能描述: 版本
     * @auther: dl.zhang
     * @date: 2019/7/23 10:44
     */
    @TableField(value = "rversion")
    private Integer rversion;

    /**
     * 功能描述: 描述
     * @auther: dl.zhang
     * @date: 2019/7/23 10:45
     */
    @TableField(value = "description")
    private String description;

    private static final long serialVersionUID = 1L;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", mallGoodsId=").append(mallGoodsId);
        sb.append(", totalStock=").append(totalStock);
        sb.append(", residueStock=").append(residueStock);
        sb.append(", isactive=").append(isactive);
        sb.append(", createdBy=").append(createdBy);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedBy=").append(updatedBy);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", rversion=").append(rversion);
        sb.append(", description=").append(description);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }



    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}