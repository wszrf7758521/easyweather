package com.zrf.easyweather.service;

import com.zrf.easyweather.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> findAll();

}
