package com.fungo.system.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/10/18
 */
@Setter
@Getter
@ToString
public class InviteInfoVO {

    /**
     * 功能描述: 邀请的用户达到LV2数目
     */
    private long inviteUserNum;

    /**
     * 功能描述: 邀请的用户购买一个月数目
     */
    private long newUserOneNum;

    /**
     * 功能描述: 邀请的用户购买三个月数目
     */
    private long newUserThreeNum;

    /**
     * 功能描述: 邀请的用户购买六个月数目
     */
    private long newUserSixNum;


}
