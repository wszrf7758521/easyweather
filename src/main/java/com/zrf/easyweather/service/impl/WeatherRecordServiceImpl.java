package com.zrf.easyweather.service.impl;

import com.zrf.easyweather.entity.WeatherRecord;
import com.zrf.easyweather.mapper.WeatherRecordMapper;
import com.zrf.easyweather.service.WeatherRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("appService")
public class WeatherRecordServiceImpl implements WeatherRecordService {



    @Autowired
    private WeatherRecordMapper weatherRecordMapper;

    @Override
    public List<WeatherRecord> findAll(){
        return weatherRecordMapper.findAll();
    }

    @Override
    public void insertDate(WeatherRecord weatherRecord) {
        weatherRecordMapper.insertDate(weatherRecord);
    }

    @Override
    public WeatherRecord queryWeatherByCodeAndTime(String adcode, String datetime) {
        return weatherRecordMapper.queryWeatherByCodeAndTime(adcode,datetime);
    }
}
