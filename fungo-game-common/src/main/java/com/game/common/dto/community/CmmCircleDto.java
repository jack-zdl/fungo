package com.game.common.dto.community;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public class CmmCircleDto implements Serializable {

    private static final long serialVersionUID = 8286352368186073859L;

    private String id;

    private String circleName;

    private String circleIcon;

    private String intro;

    private Integer type;

    private Integer state;

    private Integer memberNum;

    private Integer hotValue;

    private Integer sort;

    private Date createdAt;

    private Date updatedAt;

    private String createdBy;

    private String updatedBy;

    private String test;


}