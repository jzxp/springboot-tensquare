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
//        e.getMessage();
//        e.getStackTrace();
//        e.getCause();
        return new Result(false,StatusCode.ERROR,"再仔细检查一遍!不行就两边!");
    }

}
