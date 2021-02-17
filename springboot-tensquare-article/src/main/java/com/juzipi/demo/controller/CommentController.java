package com.juzipi.demo.controller;


import com.juzipi.demo.entity.Result;
import com.juzipi.demo.entity.StatusCode;
import com.juzipi.demo.pojo.Comment;
import com.juzipi.demo.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("comment")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 查询所有
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        List<Comment> comments = commentService.findAll();
        if (!CollectionUtils.isEmpty(comments)){
            return new Result(true, StatusCode.OK,"查询成功",comments);
        }
        return new Result(false,StatusCode.ERROR,"查询失败");

    }


    /**
     * 根据id查询评论
     * @param commentId
     * @return
     */
    @RequestMapping(value = "{commentId}",method = RequestMethod.GET)
    public Result findById(@PathVariable("commentId") String commentId){
         Comment comment = commentService.fidById(commentId);
         if (StringUtils.isNotBlank(comment)){
             return new Result(true,StatusCode.OK,"查询成功",comment);
         }

        return new Result(false,StatusCode.ERROR,"查询失败");
    }


    /**
     * 新增评论
     * @param comment
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Comment comment){
        if (StringUtils.isNotBlank(comment)){
            commentService.save(comment);
            return new Result(true,StatusCode.OK,"新增成功");
        }

        return new Result(false,StatusCode.ERROR,"新增失败");
    }


    /**
     * 修改评论
     * @param commentId
     * @param comment
     * @return
     */
    @RequestMapping(value = "{commentId}",method = RequestMethod.PUT)
    public Result updateById(@PathVariable("commentId") String commentId,@RequestBody Comment comment){
        //设置评论主键
        comment.set_id(commentId);

        if (StringUtils.isNotBlank(commentId)){
            //修改
            commentService.updateById(comment);
            return new Result(true,StatusCode.OK,"修改成功");
        }

        return new Result(false,StatusCode.ERROR,"修改失败");
    }


    /**
     * 根据id删除评论
     * @param commentId
     * @return
     */
    @RequestMapping(value = "{commentId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable("commentId") String commentId){
        if (StringUtils.isNotBlank(commentId)){
            commentService.deleteById(commentId);
            return new Result(true,StatusCode.OK,"删除成功");
        }

        return new Result(false,StatusCode.ERROR,"查询失败");
    }


    /**
     * 根据文章Id查询评论
     * @param articleId
     * @return
     */
    @RequestMapping(value = "article/{articleId}",method = RequestMethod.GET)
    public Result findByArticleId(@PathVariable("articleId") String articleId){
        List<Comment> commentList = commentService.findByArticleId(articleId);
        if (!CollectionUtils.isEmpty(commentList)){
            return new Result(true,StatusCode.OK,"查询成功",commentList);
        }

        return new Result(false,StatusCode.ERROR,"查询失败");
    }


    /**
     * 评论点赞
     * @param commentId
     * @return
     */
    @RequestMapping(value = "thumbup/{commentId}",method = RequestMethod.PUT)
    public Result thumbupPlus(@PathVariable("commentId")  String commentId){
        //把用户点赞信息保存到redis中
        //点赞前查询用户点赞信息来判断是否可以点赞

        //模拟用户id
        String userId = "123";
        String key = userId+commentId;
        //查询用户点赞信息，根据用户id和评论id
        String result = stringRedisTemplate.opsForValue().get(key);
        //判断查询的结果
        if (StringUtils.isBlank(result)){
            //为空就可点赞
            commentService.thumbupPlus(commentId);
            //保存点赞信息
            stringRedisTemplate.opsForValue().set(key,"成啦!");
            return new Result(true,StatusCode.OK,"点赞成功");
        }else {
            //根据id取消点赞（点赞做减减）
            commentService.thumbupMinus(commentId);
            //根据key删除redis里的信息
            stringRedisTemplate.delete(key);
            //返回结果
            return new Result(true,StatusCode.OK,"取消点赞");
        }

    }

}
