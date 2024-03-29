package com.fungo.system.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 通知消息
 * </p>
 *
 * @author lzh
 * @since 2019-01-23
 */
@TableName("t_bas_notice")
public class BasNotice extends Model<BasNotice> {

    private static final long serialVersionUID = 1L;

	@TableId(value = "id",type = IdType.UUID)
	private String id;
    /**
     * 类型
0:点赞帖子 1:点赞评论 2:点赞游戏评价 7:点赞心情
3:评论帖子 8:评论心情 4:回复评论 5:回复游戏
9:回复心情评论 11:点赞心情评论 12:回复二级回复
6:系统消息  13 跳转个人简介
     */
	private Integer type;

	/**
	 * 功能描述: 渠道，ios android
	 * @auther: dl.zhang
	 * @date: 2019/8/9 16:23
	 */
	private String channel;

	@TableField("is_read")
	private Integer isRead;
    /**
     * 会员ID
     */
	@TableField("member_id")
	private String memberId;
    /**
     * 数据
     */
	private String data;

	/**
	 * 功能描述: -1 删除
	 * 0 正常
	 * @auther: dl.zhang
	 * @date: 2019/8/16 11:49
	 */
//	private int state;
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
	@TableField("is_push")
	private Integer isPush;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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

	public Integer getIsPush() {
		return isPush;
	}

	public void setIsPush(Integer isPush) {
		this.isPush = isPush;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
