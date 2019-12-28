package com.fungo.system.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class ScoreGroupDTO   {

	private String id;
    /**
     * 名称
     */
	private String name;

    /**
     * 任务类型
			1 分值
			   11 任务 获取经验值
			2 虚拟币
				21 营销活动  获取fungo币
				22  签到获取fungo币
	 					220 V2.4.6签到版本签到获取fungo币
				23 任务 获取fungo币
			3 分值和虚拟币共有
     */
	private Integer taskType;
    /**
     * 任务类型
		1 新手任务
		2 社区任务
		3 游戏任务
		4 精品
		5 其他

     */
	private Integer taskFlag;

    /**
     * 排序号
     */
	private Integer sort;

    /**
     * 是否启用
     *   0-未启动
     *   1-启用
     */
	private Integer isActive;

}
