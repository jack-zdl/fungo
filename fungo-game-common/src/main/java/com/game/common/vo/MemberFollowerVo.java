package com.game.common.vo;

import com.game.common.api.InputPageDto;

import java.io.Serializable;

/**
 * <p>会员关注粉丝表VO类</p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/10
 */
public class MemberFollowerVo extends InputPageDto implements Serializable {

    private static final long serialVersionUID = -2157605438307990502L;
    /**
     * 发起会员id
     */
    private String memberId;

    /**
     * 状态 1 :A关注B 2:AB互相关注，3:AB取消关注，4：B关注A
     */
    private Integer state;

    /**
     * 功能描述: 目标对象
     * @auther: dl.zhang
     * @date: 2019/9/23 14:13
     */
    private String followId;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getFollowId() {
        return followId;
    }

    public void setFollowId(String followId) {
        this.followId = followId;
    }
}
