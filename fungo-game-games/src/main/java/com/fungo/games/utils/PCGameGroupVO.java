package com.fungo.games.utils;

import com.game.common.api.InputPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/7/25
 */
@Setter
@Getter
@ToString
public class PCGameGroupVO extends InputPageDto {
    
    /**
     * 功能描述: 内部集合条数
     * @auther: dl.zhang
     * @date: 2019/7/25 12:39
     */
    private int amount;
}
