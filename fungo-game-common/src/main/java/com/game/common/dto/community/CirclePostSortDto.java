package com.game.common.dto.community;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/20
 */
@Setter
@Getter
@ToString
public class CirclePostSortDto {
    private String sortType;

    private String sortName;

    public CirclePostSortDto(String sortType, String sortName) {
        this.sortType = sortType;
        this.sortName = sortName;
    }

    public CirclePostSortDto() {
    }
}
