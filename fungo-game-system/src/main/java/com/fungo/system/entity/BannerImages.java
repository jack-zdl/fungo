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

/**
 * <p>
 * 广告_图片
 * </p>
 *
 * @author lzh
 * @since 2018-12-27
 */
@Setter
@Getter
@ToString
@TableName("t_banner_images")
public class BannerImages extends Model<BannerImages> {

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
	 * @param: t_bannner的主键,逻辑外键
	 * @auther: dl.zhang
	 * @date: 2019/7/23 10:37
	 */
	@TableField(value = "banner_id")
	private String bannerId;

	/**
	 * 功能描述:banner图片的类型
	 * <li>1 pc的22:9的尺寸的图片 </li>
	 * <li>2 待增加 </li>
	 * @auther: dl.zhang
	 * @date: 2019/7/23 10:43
	 */
	@TableField(value = "type")
	private int type;

	/**
	 * 功能描述: 上传图片模板
	 * @auther: dl.zhang
	 * @date: 2019/7/23 10:44
	 */
	@TableField(value = "banner_image")
	private String bannerImage;

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
		return this.id;
	}
}
