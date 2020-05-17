package com.zrf.easyweather.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class WeatherInfoVO {

    private String status;

    private String count;

    private String info;

    private String infocode;

    private List<LivesVO> lives;

}
