package com.game.common.dto.community;

import com.baomidou.mybatisplus.annotations.TableField;
import com.game.common.dto.GameDto;
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
public class CmmCircleDto implements Serializable {

    private static final long serialVersionUID = 8286352368186073859L;

    private String id;

    private String circleName;

    private String circleIcon;

    private String intro;

    private String gameId;

    private String cmmId;

    private Integer type;

    private Integer state;

    private Integer memberNum;

    private Integer hotValue;

    private Long postNum;

    private Integer sort;

    private Date createdAt;

    private Date updatedAt;

    private String createdBy;

    private String updatedBy;


    private boolean follow = false;

    /**
     * 玩家排行(2.4.3) objectId avatar
     */
    private List<Map<String,Object>> eliteMembers = new ArrayList<>();

    @Setter
    @Getter
    @ToString
    public static class TagPost{
        private String tag;

        private PostOutBean postOutBean;
    }

}
