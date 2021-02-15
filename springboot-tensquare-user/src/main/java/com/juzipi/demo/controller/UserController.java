package com.juzipi.demo.controller;


import com.juzipi.demo.entity.Result;
import com.juzipi.demo.entity.StatusCode;
import com.juzipi.demo.pojo.User;
import com.juzipi.demo.service.UserService;
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

        return new Result(true, StatusCode.OK,"查询成功", user);
    }


//    @RequestMapping(method = RequestMethod.POST)

}
