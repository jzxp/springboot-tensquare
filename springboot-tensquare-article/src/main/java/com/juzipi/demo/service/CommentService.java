package com.juzipi.demo.service;


import com.juzipi.demo.pojo.Comment;
import com.juzipi.demo.repository.CommentRepository;
import com.juzipi.demo.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private MongoTemplate mongoTemplate;


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


    //根据文章id查询评论
    public List<Comment> findByArticleId(String articleId) {
        //调用持久层
        List<Comment> comments = commentRepository.findByArticleid(articleId);
        //
        return comments;
    }


    //评论点赞加
    public void thumbupPlus(String commentId) {
        /*//根据评论id查询评论
        Comment comment = commentRepository.findById(commentId).get();
        //对评论点赞数加一
        Integer i = comment.getThumbup() + 1;
        comment.setThumbup(i);
        //保存修改数据
        commentRepository.save(comment);*/

        //封装修改条件
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(commentId));
        //封装修改数值
        Update update = new Update();
        //使用inc列值增长
        update.inc("thumbup",1);

        //点赞优化
        //直接修改数据，参数分别是修改条件，修改数值，mongoDB的集合名称
        mongoTemplate.updateFirst(query,update,"comment");

    }

    //评论点赞减
    public void thumbupMinus(String commentId) {

        //封装修改条件
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(commentId));
        //封装修改数值
        Update update = new Update();
        //使用inc列值增长
        update.inc("thumbup",-1);

        //点赞优化
        //直接修改数据，参数分别是修改条件，修改数值，mongoDB的集合名称
        mongoTemplate.updateFirst(query,update,"comment");

    }
}
