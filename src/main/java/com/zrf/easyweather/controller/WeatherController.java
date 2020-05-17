package com.zrf.easyweather.controller;
import com.zrf.easyweather.entity.CityInfo;
import com.zrf.easyweather.entity.WeatherRecord;
import com.zrf.easyweather.service.CityInfoService;
import com.zrf.easyweather.service.WeatherRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherRecordService weatherRecordService;

    @Autowired
    private CityInfoService cityInfoService;

    @GetMapping("/hello")
    public String hello(Model model){
        return "index";
    }


    @GetMapping("insert")
    public String insertWeatherRecord(){
        WeatherRecord weatherRecord = new WeatherRecord();
        weatherRecord.setProvince("北京");
        weatherRecord.setCity("东城区");
        weatherRecord.setAdcode("110101");
        weatherRecord.setWeather("阴");
        weatherRecord.setTemperature("15");
        weatherRecord.setWinddirection("北");
        weatherRecord.setWindpower("<3");
        weatherRecord.setHumidity("91");
        weatherRecordService.insertDate(weatherRecord);
        return "index";
    }
    @GetMapping("/findAll")
    public String findAll(){

        List<CityInfo> cityInfoList = cityInfoService.findAll();
        for (CityInfo c:cityInfoList
             ) {
            System.out.println(c);
        }
        return "index";
    }

}
