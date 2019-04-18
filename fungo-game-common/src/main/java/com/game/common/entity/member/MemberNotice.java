package com.game.common.entity.member;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户消息临时表
 * </p>
 *
 * @author mxf
 * @since 2019-03-15
 */
@TableName("t_member_notice")
public class MemberNotice extends Model<MemberNotice> {

    private static final long serialVersionUID = 1L;

	private Long id;
    /**
     * 用户ID
     */
	@TableField("mb_id")
	private String mbId;
    /**
     * 消息类型
1 -  007，系统推送类消息

     */
	@TableField("ntc_type")
	private Integer ntcType;
    /**
     * 是否读取
1 已读取
2 未读取
     */
	@TableField("is_read")
	private Integer isRead;
    /**
     * 消息数据json格式
     */
	@TableField("ntc_data")
	private String ntcData;
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
     * 创建人id
     */
	@TableField("created_by")
	private String createdBy;
    /**
     * 创建人名称
     */
	@TableField("created_name")
	private String createdName;
    /**
     * 扩展字段1
     */
	private String ext1;
    /**
     * 扩展字段2
     */
	private String ext2;
    /**
     * 扩展字段3
     */
	private String ext3;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMbId() {
		return mbId;
	}

	public void setMbId(String mbId) {
		this.mbId = mbId;
	}

	public Integer getNtcType() {
		return ntcType;
	}

	public void setNtcType(Integer ntcType) {
		this.ntcType = ntcType;
	}

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	public String getNtcData() {
		return ntcData;
	}

	public void setNtcData(String ntcData) {
		this.ntcData = ntcData;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedName() {
		return createdName;
	}

	public void setCreatedName(String createdName) {
		this.createdName = createdName;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
