package com.fungo.system.dto;

import com.game.common.dto.InputDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;


/**
 * <p>
 *      用户消息入参数据封装
 * </p>
 *
 * @author mxf
 * @since 2018-12-03
 */
@Setter
@Getter
@ToString
public class MemberNoticeInput extends InputDto {

    /**
     * 用户ID
     */
    private String mb_id;

    private String gameId;

    /**
     * 消息类型
     * 7 -  007，系统推送类消息
     */
    @NotNull (message = "ntc_type 不能为空")
    private Integer ntc_type;

    /**
     * 功能描述:
     *  gamePackageName
     *  version
     * @auther: dl.zhang
     * @date: 2020/2/28 13:05
     */
    private List<Map<String,String>> gameInfo;



}
