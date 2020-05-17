package com.zrf.easyweather.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class LivesVO {

    private int id;

    private String province;

    private String city;

    private String adcode;

    private String weather;

    private String temperature;

    private String winddirection;

    private String windpower;

    private String humidity;

    private String reporttime;
}
