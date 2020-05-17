package com.zrf.easyweather.entity;

import lombok.Data;

import javax.persistence.Id;

@Data
public class CityInfo {

    private String cityName;

    @Id
    private String adcode;

    private String citycode;


}
