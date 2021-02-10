package com.juzipi.demo.controller;


import com.juzipi.demo.entity.Result;
import com.juzipi.demo.entity.StatusCode;
import com.juzipi.demo.pojo.Article;
import com.juzipi.demo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;




    /**
     * 查询所有文章
     * @return
     */
//    @GetMapping("article")
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        List<Article> list = articleService.finfAll();
        return new Result(true, StatusCode.OK,"查询成功",list);
    }


    /**
     * 根据id查询文章
     * @param articleId
     * @return
     */
//    @GetMapping("article/{articleId}")
    @RequestMapping(value = "{articleId}",method = RequestMethod.GET)
    public Result fidnById(@PathVariable String articleId){
        Article article = articleService.fidnById(articleId);
        return new Result(true,StatusCode.OK,"查询成功",article);
    }


    /**
     * 增加文章
     * @param article
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Article article){
        articleService.save(article);
        return new Result(true,StatusCode.OK,"新增成功");
    }
}
