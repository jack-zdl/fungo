package com.game.common.dto.mall;

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
public class MallSeckillOrderDto implements Serializable {

    private static final long serialVersionUID = -3479702652115489082L;
    /**
     * 功能描述: 主键
     * @auther: dl.zhang
     * @date: 2019/7/23 10:43
     */
    private Integer id;
    /**
     * 功能描述: t_mall_goods的主键,逻辑外键
     * @auther: dl.zhang
     * @date: 2019/7/23 10:44
     */
    private String mallGoodsId;
    /**
     * 功能描述: 总库存
     * @auther: dl.zhang
     * @date: 2019/7/23 10:44
     */
    private Integer totalStock;
    /**
     * 功能描述: 剩余库存
     * @auther: dl.zhang
     * @date: 2019/7/23 10:44
     */
    private Integer residueStock;

    /**
     * 功能描述: 是否有效
     * @auther: dl.zhang
     * @date: 2019/7/23 10:44
     */
    private String isactive;

    /**
     * 创建时间
     */
    private String createdBy;

    /**
     * 更新时间
     */
    private Date createdAt;

    /**
     * 创建人
     */
    private String updatedBy;

    /**
     * 创建人名称
     */
    private Date updatedAt;

    /**
     * 功能描述: 版本
     * @auther: dl.zhang
     * @date: 2019/7/23 10:44
     */
    private Integer rversion;

    /**
     * 功能描述: 描述
     * @auther: dl.zhang
     * @date: 2019/7/23 10:45
     */
    private String description;

    private String orderId;


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


}