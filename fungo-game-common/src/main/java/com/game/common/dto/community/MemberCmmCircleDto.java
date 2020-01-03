package com.game.common.dto.community;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class MemberCmmCircleDto implements Serializable {

    private static final long serialVersionUID = 8286352368186073859L;

    private String id;

    private String circleName;

    private String circleIcon;

}
