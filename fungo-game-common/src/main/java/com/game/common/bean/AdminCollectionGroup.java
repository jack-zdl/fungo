package com.game.common.bean;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class AdminCollectionGroup {

	private String id;
	/**
	 * 游戏集合名称
	 */
	private String name;
	/**
	 * 状态
	 */
	private Integer state;
	/**
	 * 合集介绍
	 */
	private String detail;
	/**
	 * 是否上线 -1:下线 0:上线
	 */
	private String isOnline;
	/**
	 * 合集封面地址
	 */
	private String coverPicture;
	/**
	 * 排序号
	 */
	private Integer sort;
	/**
	 * 合集游戏详情
	 */
	private List<CollectionItemBean> list;
	/**
	 * 点赞总数
	 */
	private Integer count;
	/**
	 * 是否点赞
	 */
	private Boolean isClick;

}
