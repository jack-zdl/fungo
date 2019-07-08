package com.game.common.dto.system;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/18
 */
@Setter
@Getter
@ToString
public class CircleFollow {
    private String circleId;

    private boolean follow;

    public CircleFollow(String circleId, boolean follow) {
        this.circleId = circleId;
        this.follow = follow;
    }

    public CircleFollow() {
    }
}
