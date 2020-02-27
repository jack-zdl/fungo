package com.game.common.dto.game;

import com.game.common.api.InputPageDto;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BangGameDto extends InputPageDto {
    private Integer sortType;

    private Integer offset;

    private List<Map<String,String>> gameInfo;

}
