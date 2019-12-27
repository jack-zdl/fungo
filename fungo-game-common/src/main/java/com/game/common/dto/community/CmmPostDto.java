package com.game.common.dto.community;

import com.game.common.api.InputPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * <p>
 * 		社区帖子
 * </p>
 *
 * @author lzh
 * @since 2018-12-27
 */
@Setter
@Getter
@ToString
public class CmmPostDto extends InputPageDto {


	private static final long serialVersionUID = -2160150540329361706L;
	/**
     * 社区ID
     */
	private String communityId;
	private String id;
    /**
     * 会员id
     */
	private String memberId;
    /**
     * 标题
     */
	private String title;
    /**
     * 标签
     */
	private String tags;
    /**
     * html内容
     */
	private String htmlOrigin;
    /**
     * 内容
     */
	private String content;
    /**
     * 图片
     */
	private String images;
	private String coverImage;
    /**
     * 编辑时间
     */
	private Date editedAt;
    /**
     * 组织
     */
	private String origin;
    /**
     * 状态 -1:已删除 0:压缩转码中 1:正常
     */
	private Integer state;
    /**
     * 评论数
     */
	private Integer commentNum;
    /**
     * 点赞数
     */
	private Integer likeNum;
    /**
     * 收藏数
     */
	private Integer collectNum;
    /**
     * 查看数
     */
	private Integer watchNum;
    /**
     * 举报数
     */
	private Integer reportNum;
    /**
     * 创建时间
     */
	private Date createdAt;
    /**
     * 更新时间
     */
	private Date updatedAt;

	private Integer recommend;
    /**
     * 类型 1:普通 2:精华 3:置顶
     */
	private Integer type;

    /**
     * 视频 v2.4
     */
	private String video;
    /**
     * 游戏链接json集合
     */
	private String gameList;
    /**
     * 置顶状态
     */
	private Integer topic;
    /**
     * 视频地址集合，json格式
V2.4.3版本添加
     */
	private String videoUrls;
    /**
     * 最后回复时间
     */
	private Date lastReplyAt;
    /**
     * 视频封面
     */
	private String videoCoverImage;

	private Long postId;

	private String circleId;

	private String circleName;

	/**
	 * 功能描述: 1 自己查询自己的信息
	 * @auther: dl.zhang
	 * @date: 2019/8/13 14:13
	 */
	private int  queryType;

}
