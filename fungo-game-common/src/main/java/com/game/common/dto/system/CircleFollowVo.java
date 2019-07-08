package com.game.common.dto.system;

import com.game.common.api.InputPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/18
 */
@Setter
@Getter
@ToString
public class CircleFollowVo extends InputPageDto {

    private String memberId;

    /**
     * 功能描述:
     * <p>
     * 点赞 | 0
     * 回复 | 1
     * 评论 | 2
     * 分享 | 3
     * 收藏 | 4
     * 关注 | 5
     * 举报 | 6
     * 下载 | 7
     * 推荐 | 8
     * 不推荐 | 9
     * 忽略 | 10
     * 发帖 | 11
     * 浏览 | 12
     * 登录 | 13
     * </p>
     * @auther: dl.zhang
     * @date: 2019/6/21 18:14
     */
    private String actionType;
    
    private List<CircleFollow> circleFollows;


}
