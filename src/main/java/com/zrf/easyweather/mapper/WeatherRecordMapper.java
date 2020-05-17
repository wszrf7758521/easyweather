package com.zrf.easyweather.mapper;

import com.zrf.easyweather.entity.WeatherRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WeatherRecordMapper {
    List<WeatherRecord> findAll();

    void insertDate(WeatherRecord weatherRecord);

    WeatherRecord queryWeatherByCodeAndTime(String adcode,String datetime );
}
