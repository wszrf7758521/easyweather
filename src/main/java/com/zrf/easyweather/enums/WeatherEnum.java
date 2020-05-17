package com.zrf.easyweather.enums;

public enum WeatherEnum {

    SUNNNY(1,"晴"),CLOUDY(2,"多云"),OVERCAST(3,"阴"),RAINY(4,"雨"),FOG(5,"雾"),SAND(6,"扬沙");

    private int code;
    private String name;

    private WeatherEnum(int code,String name){
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
