package com.game.common.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HomePageBean {

	private String id;
	/**
	 * 游戏id
	 */
	private String gameId;
	/**
	 * 游戏集合名称
	 */
	private String name;
	/**
	 * 标签
	 */
	private String tags;
	/**
	 * 国家
	 */
	private String origin;
	/**
	 * 评分
	 */
	private Double score;
	/**
	 * 推荐语
	 */
	private String rmdLag;
	/**
	 * 推荐理由
	 */
	private String rmdReason;
	/**
	 * 视频地址
	 */
	private String video;
	/**
	 * 图片
	 */
	private String appImages;
	/**
	 * 图片
	 */
	private String pcImages;
	/**
	 * 'mark标识'
	 */
	private boolean  make;
	/**
	 * 'isClause标识'
	 */
	private boolean isClause;
	/**
	 * '游戏图标'
	 */
	private String  icon;
	/**
	 * 'IOS状态 0:待开启，1预约。2.测试，3已上线,4：可下载
	 */
	private Integer iosState;
	/**
	 * '安卓状态 0:待开启，1预约。2.测试，3已上线
	 */
	private Integer androidState;
	/**
	 * 圈子主键
	 */
	private String circleId;

	/**
	 *  安卓下载地址
	 * @return
	 */
	private String apk;
	/**
	 *  游戏大小
	 * @return
	 */
	private Long gameSize;
	/**
	 *  安卓包名
	 * @return
	 */
	private String androidPackageName;
	/**
	 *  版本号
	 * @return
	 */
	private String version;
	/**
	 *  主版本号
	 * @return
	 */
	private String versionMain;
	/**
	 *  子版本号
	 * @return
	 */
	private String versionChild;
	/**
	 *  是否加速
	 * @return
	 */
	private Boolean canFast;
	/**
	 *  图片s
	 * @return
	 */
	private String images;
	/**
	 *  游戏图标
	 * @return
	 */
	private String coverImage;

	private Long gameIdtSn;

}
