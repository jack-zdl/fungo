package com.game.common.dto.community.portal;

import lombok.Data;

/**
 * <p>
 *
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/6/1 13:10
 */
@Data
public class CmmCommunityIndexDto {
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
