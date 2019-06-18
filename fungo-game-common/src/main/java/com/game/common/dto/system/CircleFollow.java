package com.game.common.dto.system;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/18
 */
public class CircleFollow {
    private String circleId;

    private boolean follow;

    public CircleFollow(String circleId, boolean follow) {
        this.circleId = circleId;
        this.follow = follow;
    }

    public CircleFollow() {
    }

    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }
}
