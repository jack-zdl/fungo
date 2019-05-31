package com.fungo.community.entity.custom;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 *  PC2.0圈子首页列表实体
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/5/31 17:40
 */
@Data
public class CmmCommunityIndexList {
    private String id;
    /**
     * 游戏id
     */
    private String gameId;
    /**
     * 名称
     */
    private String name;
    /**
     * icon
     */
    private String icon;
    /**
     * 介绍
     */
    private String intro;
    /**
     * 图片
     */
    private String coverImage;
    /**
     * 社区类型
     */
    private Integer type;
    /**
     * 状态  -1:已删除,0:未上架,1:运营中
     */
    private Integer state;
    /**
     * 推荐数
     */
    private Integer followeeNum;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 更新时间
     */
    private Date updatedAt;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 更新人
     */
    private String updatedBy;
    /**
     * 热度值
     */
    private Integer hotValue;
    /**
     * 推荐状态 0:未推荐,1:推荐
     */
    private Integer recommendState;
    /**
     * 标记
     */
    private Integer sort;
    /**
     * 帖子数
     */
    private Integer postNum;
}
