package com.game.common.dto.game;


import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 游戏测试会员关联表
 * </p>
 *
 * @author lzh
 * @since 2018-06-14
 */
public class GameSurveyRel  {

	private String id;
    /**
     * 会员id
     */
	private String memberId;
    /**
     * 游戏id
     */
	private String gameId;
    /**
     * 是否同意协议，0 ，1已同意
     */
	private Integer agree;
    /**
     * 通知状态 0没通知，1已通知
     */
	private Integer notice;
    /**
     * 状态
     */
	private Integer state;
    /**
     * 批次号，打批
     */
	private String batchNo;
    /**
     * 手机型号
     */
	private String phoneModel;
    /**
     * appleId
     */
	private String appleId;
    /**
     * 姓名
     */
	private String surname;
    /**
     * 名称
     */
	private String name;
    /**
     * 创建时间
     */
	private Date createdAt;
    /**
     * 更新时间
     */
	private Date updatedAt;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public Integer getAgree() {
		return agree;
	}

	public void setAgree(Integer agree) {
		this.agree = agree;
	}

	public Integer getNotice() {
		return notice;
	}

	public void setNotice(Integer notice) {
		this.notice = notice;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getPhoneModel() {
		return phoneModel;
	}

	public void setPhoneModel(String phoneModel) {
		this.phoneModel = phoneModel;
	}

	public String getAppleId() {
		return appleId;
	}

	public void setAppleId(String appleId) {
		this.appleId = appleId;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

}
