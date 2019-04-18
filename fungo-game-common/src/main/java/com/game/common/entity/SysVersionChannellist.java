package com.game.common.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lzh
 * @since 2018-10-30
 */
@TableName("t_sys_version_channellist")
public class SysVersionChannellist extends Model<SysVersionChannellist> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 平台类型
     */
	@TableField("mobile_type")
	private String mobileType;
    /**
     * 渠道码
     */
	@TableField("channel_code")
	private String channelCode;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMobileType() {
		return mobileType;
	}

	public void setMobileType(String mobileType) {
		this.mobileType = mobileType;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
