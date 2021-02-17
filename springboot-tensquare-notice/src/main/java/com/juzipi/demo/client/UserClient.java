package com.juzipi.demo.client;


import com.juzipi.demo.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("tensquare-user")
public interface UserClient {

    /**
     * 通过用户id查询用户
     * @param userId
     * @return
     */
    @RequestMapping(value = "user/{userId}",method = RequestMethod.GET)
    public Result selectById(@PathVariable("userId") String userId);
}
