package com.game.common.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 次方字典表
 * </p>
 *
 * @author lzh
 * @since 2018-07-25
 */
@TableName("t_sys_power")
public class SysPower extends Model<SysPower> {

    private static final long serialVersionUID = 1L;

	@TableId(value="Id", type= IdType.AUTO)
	private Integer Id;


	public Integer getId() {
		return Id;
	}

	public void setId(Integer Id) {
		this.Id = Id;
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

}
