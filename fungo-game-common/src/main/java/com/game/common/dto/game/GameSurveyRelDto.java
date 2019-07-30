package com.game.common.dto.game;


import com.game.common.api.InputPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@Setter
@Getter
@ToString
public class GameSurveyRelDto extends InputPageDto {

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

	/**
	 * 功能描述: 游戏名称
	 * @date: 2019/7/30 11:15
	 */
	private String gameName;


}
