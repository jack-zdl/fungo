package com.game.common.dto.system;

import com.game.common.api.InputPageDto;

import java.util.List;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/18
 */
public class CircleFollowVo extends InputPageDto {

    private String memberId;

    private List<CircleFollow> circleFollows;

    public List<CircleFollow> getCircleFollows() {
        return circleFollows;
    }

    public void setCircleFollows(List<CircleFollow> circleFollows) {
        this.circleFollows = circleFollows;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }


}
