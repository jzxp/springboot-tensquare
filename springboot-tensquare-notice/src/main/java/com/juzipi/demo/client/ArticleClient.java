package com.juzipi.demo.client;


import com.juzipi.demo.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("tensquare-article")
public interface ArticleClient {

    /**
     * 根据id查询文章
     * @param articleId
     * @return
     */
    @RequestMapping(value = "article/{articleId}",method = RequestMethod.GET)
    public Result fidnById(@PathVariable("articleId") String articleId);
}
