package com.game.common.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 接口前台访问日志
 * </p>
 *
 * @author lzh
 * @since 2018-07-09
 */
@TableName("t_bas_log")
public class BasLog extends Model<BasLog> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 创建时间
     */
	@TableField("created_at")
	private Date createdAt;
    /**
     * 结束时间
     */
	@TableField("end_time")
	private Date endTime;
    /**
     * 运行时长
     */
	@TableField("run_time")
	private Integer runTime;
    /**
     * 路径
     */
	private String path;
    /**
     * 请求方法
     */
	private String method;
    /**
     * ip
     */
	private String ip;
    /**
     * 会员id
     */
	@TableField("member_id")
	private String memberId;
    /**
     * 输入数据
     */
	@TableField("input_data")
	private String inputData;
    /**
     * 返回数据
     */
	@TableField("out_data")
	private String outData;
    /**
     * 渠道，ios android
     */
	private String channel;
    /**
     * 详情业务ID
     */
	@TableField("biz_id")
	private String bizId;
    /**
     * 返回码
     */
	@TableField("re_code")
	private String reCode;
    /**
     * 状态
     */
	private Integer state;
    /**
     * 业务代码
     */
	@TableField("biz_code")
	private String bizCode;
    /**
     * 更新时间
     */
	@TableField("updated_at")
	private Date updatedAt;
    /**
     * token
     */
	private String token;
    /**
     * 品牌
     */
	private String brand;
    /**
     * User-Agent
     */
	@TableField("user_agent")
	private String userAgent;

	private String appversion;
	
	private String height;
	
	private String width;
	
	private String version;
	
	private String udid;
	

	public String getAppversion() {
		return appversion;
	}

	public void setAppversion(String appversion) {
		this.appversion = appversion;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getRunTime() {
		return runTime;
	}

	public void setRunTime(Integer runTime) {
		this.runTime = runTime;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getInputData() {
		return inputData;
	}

	public void setInputData(String inputData) {
		this.inputData = inputData;
	}

	public String getOutData() {
		return outData;
	}

	public void setOutData(String outData) {
		this.outData = outData;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getReCode() {
		return reCode;
	}

	public void setReCode(String reCode) {
		this.reCode = reCode;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getBizCode() {
		return bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
