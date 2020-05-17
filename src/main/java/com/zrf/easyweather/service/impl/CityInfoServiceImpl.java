package com.zrf.easyweather.service.impl;

import com.zrf.easyweather.entity.CityInfo;
import com.zrf.easyweather.entity.WeatherRecord;
import com.zrf.easyweather.mapper.CityInfoMapper;
import com.zrf.easyweather.mapper.WeatherRecordMapper;
import com.zrf.easyweather.service.CityInfoService;
import com.zrf.easyweather.service.WeatherRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityInfoServiceImpl implements CityInfoService {

    @Autowired
    private CityInfoMapper cityInfoMapper;

    @Override
    public List<CityInfo> findAll(){
        return cityInfoMapper.findAll();
    }

}
