package com.juzipi.demo.controller;


import com.juzipi.demo.entity.Result;
import com.juzipi.demo.entity.StatusCode;
import com.juzipi.demo.pojo.Comment;
import com.juzipi.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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

        return new Result(true, StatusCode.OK,"查询成功",comments);
    }


    /**
     * 根据id查询评论
     * @param commentId
     * @return
     */
    @RequestMapping(value = "{commentId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String commentId){
         Comment comment = commentService.fidById(commentId);

        return new Result(true,StatusCode.OK,"查询成功",comment);
    }


    /**
     * 新增评论
     * @param comment
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Comment comment){
        commentService.save(comment);
        return new Result(true,StatusCode.OK,"新增成功");
    }


    /**
     * 修改评论
     * @param commentId
     * @param comment
     * @return
     */
    @RequestMapping(value = "{commentId}",method = RequestMethod.PUT)
    public Result updateById(@PathVariable String commentId,@RequestBody Comment comment){
        //设置评论主键
        comment.set_id(commentId);
        //修改
        commentService.updateById(comment);

        return new Result(true,StatusCode.OK,"修改成功");
    }


    /**
     * 根据id删除评论
     * @param commentId
     * @return
     */
    @RequestMapping(value = "{commentId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String commentId){
        commentService.deleteById(commentId);

        return new Result(true,StatusCode.OK,"删除成功");
    }


    /**
     * 根据文章Id查询评论
     * @param articleId
     * @return
     */
    @RequestMapping(value = "article/{articleId}",method = RequestMethod.GET)
    public Result findByArticleId(@PathVariable String articleId){
        List<Comment> commentList = commentService.findByArticleId(articleId);

        return new Result(true,StatusCode.OK,"查询成功",commentList);
    }


    /**
     * 评论点赞
     * @param commentId
     * @return
     */
    @RequestMapping(value = "thumbup/{commentId}",method = RequestMethod.PUT)
    public Result thumbupPlus(@PathVariable String commentId){
        //把用户点赞信息保存到redis中
        //点赞前查询用户点赞信息来判断是否可以点赞

        //模拟用户id
        String userId = "123";
        //查询用户点赞信息，根据用户id和评论id
        String result = stringRedisTemplate.opsForValue().get("thumbup_" + userId + "and" + commentId);
        //判断查询的结果
        if (result == null){
            //为空就可点赞
            commentService.thumbupPlus(commentId);
            //保存点赞信息
            stringRedisTemplate.opsForValue().set("用户id:" + userId + " 评论id:" + commentId,"保存的点赞信息");
            return new Result(true,StatusCode.OK,"点赞成功");
        }
        else if (result != null){
            //获取点赞存入的key
            //删除key，点赞减一
            stringRedisTemplate.delete(result);
            commentService.thumbupMinus(commentId);
            //返回信息
            return new Result(true,StatusCode.OK,"取消点赞");
        }

        return null;
    }

}
