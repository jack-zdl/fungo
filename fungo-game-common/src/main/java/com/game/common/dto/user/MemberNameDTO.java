package com.game.common.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2020/1/3
 */
@Setter
@Getter
@ToString
public class MemberNameDTO {

    private String id;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 头像
     */
    private String avatar;
}
