package com.game.common.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 会员编码列表
 * </p>
 *
 * @author lzh
 * @since 2018-08-17
 */
@TableName("t_member_no")
public class MemberNo extends Model<MemberNo> {

    private static final long serialVersionUID = 1L;

	private Integer id;
    /**
     * 会员编号
     */
	@TableField("member_no")
	private String memberNo;
    /**
     * 创建时间
     */
	@TableField("creacted_at")
	private Date creactedAt;
    /**
     * 更新时间
     */
	@TableField("updated_at")
	private Date updatedAt;
    /**
     * 绑定的会员id
     */
	@TableField("member_id")
	private String memberId;
    /**
     * 状态
     */
	private Integer state;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

	public Date getCreactedAt() {
		return creactedAt;
	}

	public void setCreactedAt(Date creactedAt) {
		this.creactedAt = creactedAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
