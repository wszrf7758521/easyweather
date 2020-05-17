package com.zrf.easyweather.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class WeatherRecord {

    @Id
    private int id;

    private String province;

    private String city;

    private String adcode;

    private String weather;

    private String temperature;

    private String winddirection;

    private String windpower;

    private String humidity;

    @CreatedDate
    private String reportTime;

}
