package com.fungo.system.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>用户位置信息</p>
 *
 * @Author: dl.zhang
 * @Date: 2020/3/11
 */
@Setter
@Getter
@ToString
public class UserGeoDto {

    private Double x;

    private Double y;

    private String geoName;
}
