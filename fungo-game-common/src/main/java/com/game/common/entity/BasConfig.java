package com.game.common.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 配置信息
 * </p>
 *
 * @author lzh
 * @since 2018-05-23
 */
@TableName("t_bas_config")
public class BasConfig extends Model<BasConfig> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * key
     */
	@TableField("key_name")
	private String keyName;
    /**
     * 值
     */
	@TableField("value_info")
	private String valueInfo;
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


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getValueInfo() {
		return valueInfo;
	}

	public void setValueInfo(String valueInfo) {
		this.valueInfo = valueInfo;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
