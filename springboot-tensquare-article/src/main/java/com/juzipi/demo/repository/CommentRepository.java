package com.juzipi.demo.repository;

import com.juzipi.demo.pojo.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface CommentRepository extends MongoRepository<Comment,String> {
    //SpringDataMongoDB 支持通过查询方法名进行查询定义的方式
    //根据文章id查询评论数据
    List<Comment> findByArticleid(String articleId);


    //根据发布时间和点赞数查询                           需要传入这两个数据的类型和参数
    List<Comment> findByPublishdateAndThumbup(Date publishdate,Integer thumbup);
    //根据用户id查询，并且根据发布时间倒序排序
    List<Comment> findByUseridOrderByPublishdateDesc(String userid);


}
