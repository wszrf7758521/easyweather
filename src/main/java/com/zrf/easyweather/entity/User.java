package com.zrf.easyweather.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Id;

@Data
public class User {

    @Id
    private int userId;

    private String userName;

    private String email;

    private String phone;

    private String status;

    @CreatedDate
    private String createTime;

    private String city;

    private String province;

    private String adcode;


}
