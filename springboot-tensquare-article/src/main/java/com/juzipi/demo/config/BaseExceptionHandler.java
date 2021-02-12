package com.juzipi.demo.config;

import com.juzipi.demo.entity.Result;
import com.juzipi.demo.entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class BaseExceptionHandler {

    //公共处理异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handler(Exception e){
        e.printStackTrace();
        return new Result(false,StatusCode.ERROR,"报错了");
    }

}
