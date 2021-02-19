package com.juzipi.demo.controller;




import com.baomidou.mybatisplus.plugins.Page;
import com.juzipi.demo.entity.PageResult;
import com.juzipi.demo.entity.Result;
import com.juzipi.demo.entity.StatusCode;
import com.juzipi.demo.pojo.Article;
import com.juzipi.demo.service.ArticleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("article")
@CrossOrigin
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


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


    /**
     * 查询分页结果集
     * @param page
     * @param size
     * @param map
     * @return
     */
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


    /**
     * 根据文章id和用户id建立订阅关系
     * @param map
     * @return
     */
    @RequestMapping(value = "subscribe",method = RequestMethod.POST)
    public Result subscribe(@RequestBody Map map){
        //如果返回true就是订阅文章作者
        //false就是取消订阅
        Boolean flag = articleService.subscribe(map.get("articleId").toString(),map.get("userId").toString());
        if (flag){
            return new Result(true,StatusCode.OK,"订阅成功");
        }

        return new Result(true,StatusCode.OK,"取消订阅成功");
    }


    /**
     * 根据文章id点赞
     * @param articleId
     * @return
     */
    @RequestMapping(value = "thumbup/{articleId}",method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String articleId){
        //根据用户id点赞

        String userId = "3";
        String key = "根据文章id点赞" + userId + "_" + articleId;

        //查询用户点赞信息，根据用户id和文章id
        String flag = stringRedisTemplate.opsForValue().get(key);

        //判断查询到的结果是否为false
        if (StringUtils.isBlank(flag)){
            //为false就可以点赞
            articleService.thumbup(articleId,userId);
            stringRedisTemplate.opsForValue().set(key,"赞");
            return new Result(true,StatusCode.OK,"点赞成功");

        }
        return new Result(false,StatusCode.OK,"已经点过赞了");
    }



}
