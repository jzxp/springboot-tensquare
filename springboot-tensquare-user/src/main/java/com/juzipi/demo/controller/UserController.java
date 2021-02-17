package com.juzipi.demo.controller;


import com.juzipi.demo.entity.Result;
import com.juzipi.demo.entity.StatusCode;
import com.juzipi.demo.pojo.User;
import com.juzipi.demo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 通过用户id查询用户
     * @param userId
     * @return
     */
    @RequestMapping(value = "{userId}",method = RequestMethod.GET)
    public Result selectById(@PathVariable String userId){
        User user = userService.selectById(userId);
//        if (StringUtils.isNotBlank(user)){
            return new Result(true, StatusCode.OK,"查询成功", user);
//        }
//        return new Result(false,StatusCode.ERROR,"失败");

    }


    /**
     * 登录
     * @param user
     * @return
     */
    @RequestMapping(value = "login",method = RequestMethod.POST)
    public Result login(@RequestBody User user){
        User result = userService.login(user);
        if (StringUtils.isNotBlank(result)){
            return new Result(true,StatusCode.OK,"登录成功",result);
        }
        return new Result(false,StatusCode.ERROR,"登陆失败",null);


    }



}
