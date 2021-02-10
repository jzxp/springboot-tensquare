package com.juzipi.demo.service;

import com.juzipi.demo.mapper.ArticleMapper;
import com.juzipi.demo.pojo.Article;
import com.juzipi.demo.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private IdWorker idWorker;


    /**
     * 查询article 的所有数据
     * @return
     */
    public List<Article> finfAll() {
        return articleMapper.selectList(null);
    }

    /**
     * 根据id查询文章
     * @param articleId
     * @return
     */
    public Article fidnById(String articleId) {
        return articleMapper.selectById(articleId);
    }


    /**
     * 新增文章
     * @param article
     */
    public void save(Article article) {
        //使用分布式id生成器
        String id = idWorker.nextId() + "";
        article.setId(id);

        //初始化特殊数据
        article.setVisits(0);//浏览量
        article.setThumbup(0);//点赞数
        article.setComment(0);//评论数
        //新增
        articleMapper.insert(article);

    }
}
