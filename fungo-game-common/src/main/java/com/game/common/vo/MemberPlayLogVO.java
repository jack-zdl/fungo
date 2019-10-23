package com.game.common.vo;

import com.game.common.validate.an.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

@Setter
@Getter
@ToString
public class MemberPlayLogVO {


    @NotNull
    private String memberPhone;

    @NotNull
    private String playId;

    @NotNull
    private String businessId;

    private String couponType;

    private String couponId;

    private String payType;

    private String description;

}