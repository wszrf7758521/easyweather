package com.zrf.easyweather.mapper;

import com.zrf.easyweather.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> findAll();
}
