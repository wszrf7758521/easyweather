package com.zrf.easyweather;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zrf.easyweather.mapper")
public class EasyweatherApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyweatherApplication.class, args);
    }

}
