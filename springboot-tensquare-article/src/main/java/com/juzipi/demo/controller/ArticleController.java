package com.juzipi.demo.controller;




import com.baomidou.mybatisplus.plugins.Page;
import com.juzipi.demo.entity.PageResult;
import com.juzipi.demo.entity.Result;
import com.juzipi.demo.entity.StatusCode;
import com.juzipi.demo.pojo.Article;
import com.juzipi.demo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("article")
@CrossOrigin
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


    /**
     * 修改文章
     * @param articleId
     * @param article
     * @return
     */
    @RequestMapping(value = "{articleId}",method = RequestMethod.PUT)
    public Result updateById(@PathVariable String articleId,@RequestBody Article article){
        //设置id
        article.setId(articleId);
        //执行修改
        articleService.updateById(article);

        return new Result(true,StatusCode.OK,"修改成功");
    }


    /**
     * 根据id删除文章
     * @param articleId
     * @return
     */
    @RequestMapping(value = "{articleId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String articleId){
        articleService.deleteById(articleId);

        return new Result(true,StatusCode.OK,"删除成功");
    }



    @RequestMapping(value = "search/{page}/{size}",method = RequestMethod.POST)
    //之前接收使用的使mapper,现在根据条件查询，
    //所有的条件都需要进行判断，遍历mapper的所有属性要使用反射的方式，性能较低
    //直接使用集合的方式遍历，接收数据改为map集合
    public Result findByPage(@PathVariable Integer page,
                             @PathVariable Integer size,
                             @RequestBody Map<String,Object> map){
        //根据条件分页查询
        Page<Article> pageData = articleService.findByPage(map,page,size);
        //封装分页返回对象
        PageResult<Article> pageResult = new PageResult<>((long)pageData.getTotal(), pageData.getRecords());
        //返回数据
        return new Result(true,StatusCode.OK,"查询成功",pageResult);


    }

}