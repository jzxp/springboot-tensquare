package com.juzipi.demo.service;


import com.juzipi.demo.pojo.Comment;
import com.juzipi.demo.repository.CommentRepository;
import com.juzipi.demo.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private IdWorker idWorker;

    //查询所有评论
    public List<Comment> findAll() {
        List<Comment> list = commentRepository.findAll();
        return list;
    }


    //根据id查询评论
    public Comment fidById(String commentId) {
        Comment comment = commentRepository.findById(commentId).get();

        return comment;
    }


    //保存评论
    public void save(Comment comment) {
        //使用分布式id生成id
        String id = idWorker.nextId()+"";
        //初始化点赞，发布时间
        comment.set_id(id);
        comment.setPublishdate(new Date());
        comment.setThumbup(0);
        //保存数据
        commentRepository.save(comment);
    }


    //根据id修改评论
    public void updateById(Comment comment) {
        //使用的是MongoRepository的方法
        //save方法，如果主键存在就修改，不存在就新增
        commentRepository.save(comment);
    }


    //根据id删除评论
    public void deleteById(String commentId) {
        commentRepository.deleteById(commentId);
    }
}
