package com.juzipi.demo.repository;

import com.juzipi.demo.pojo.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment,String> {




}
