package com.zrf.easyweather.service;

import com.zrf.easyweather.entity.CityInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CityInfoService {
    List<CityInfo> findAll();

}
