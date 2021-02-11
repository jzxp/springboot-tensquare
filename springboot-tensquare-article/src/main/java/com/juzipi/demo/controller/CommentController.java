package com.juzipi.demo.controller;


import com.juzipi.demo.entity.Result;
import com.juzipi.demo.entity.StatusCode;
import com.juzipi.demo.pojo.Comment;
import com.juzipi.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("comment")
public class CommentController {

    @Autowired
    private CommentService commentService;


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
}
