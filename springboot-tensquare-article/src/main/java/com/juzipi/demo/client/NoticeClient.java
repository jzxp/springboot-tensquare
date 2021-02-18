package com.juzipi.demo.client;

import com.juzipi.demo.entity.Result;
import com.juzipi.demo.pojo.Notice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("tensquare-notice")
public interface NoticeClient {

    /**
     * 新增通知
     * @param notice
     * @return
     */
    @RequestMapping(value = "notice",method = RequestMethod.POST)
    public Result save(@RequestBody Notice notice);

}
