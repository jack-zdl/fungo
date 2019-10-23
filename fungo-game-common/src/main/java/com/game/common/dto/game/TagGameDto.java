package com.game.common.dto.game;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TagGameDto {

    private List<String> tagIdList = new ArrayList<>();

    private Integer sortType;
}
