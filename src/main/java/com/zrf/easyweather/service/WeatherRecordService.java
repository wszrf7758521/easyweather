package com.zrf.easyweather.service;

import com.zrf.easyweather.entity.WeatherRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WeatherRecordService {
    List<WeatherRecord> findAll();

    void insertDate(WeatherRecord weatherRecord);

    WeatherRecord queryWeatherByCodeAndTime(String adcode,String datetime );
}
