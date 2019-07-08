package com.fungo.community.entity.portal;

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
public class CmmCommunityIndex {
    /**
     * 目标ID
     */
    private String targetId;
    /**
     * 游戏id
     */
    private String gameId;
    /**
     * 目标名称
     */
    private String targetName;
    /**
     * icon
     */
    private String icon;
    /**
     * 文章数
     */
    private String postNum;
    /**
     * 热度
     */
    private String hotValue;

}
