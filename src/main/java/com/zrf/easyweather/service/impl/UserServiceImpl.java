package com.zrf.easyweather.service.impl;


import com.zrf.easyweather.entity.User;
import com.zrf.easyweather.mapper.UserMapper;
import com.zrf.easyweather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findAll(){
        return userMapper.findAll();
    }

}
