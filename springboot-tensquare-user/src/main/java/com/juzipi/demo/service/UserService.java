package com.juzipi.demo.service;

import com.juzipi.demo.mapper.UserMapper;
import com.juzipi.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;


    //通过用户id查询用户
    public User selectById(String userId){
        return userMapper.selectById(userId);
    }

}
