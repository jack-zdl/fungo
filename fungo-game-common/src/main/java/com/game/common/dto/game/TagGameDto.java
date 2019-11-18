package com.game.common.dto.game;

import com.game.common.api.InputPageDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TagGameDto extends InputPageDto {
   // --------以下参数 客户端传递-------
    private List<String> tags;

    // 0 热度(周下载量) 1 评分 2 更新
    private Integer sortType = 0;

    //--------以下参数 数据库查询使用 自己封装-------
    private String tagIds;

    // 数据库 查询时的 offset
    private Integer offset;
    // tagIds 转数组的长度
    private Integer size;



}
