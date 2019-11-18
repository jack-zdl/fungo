package com.game.common.dto.game;

import com.game.common.api.InputPageDto;
import lombok.Data;

@Data
public class BangGameDto extends InputPageDto {
    private Integer sortType;

    private Integer offset;

}
