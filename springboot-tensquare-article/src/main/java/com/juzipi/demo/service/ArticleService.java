package com.juzipi.demo.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.juzipi.demo.mapper.ArticleMapper;
import com.juzipi.demo.pojo.Article;
import com.juzipi.demo.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
        //查询所有
        return articleMapper.selectList(null);
    }

    /**
     * 根据id查询文章
     * @param articleId
     * @return
     */
    public Article fidnById(String articleId) {
        //根据id查找
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


    /**
     * 根据id修改文章
     * @param article
     */
    public void updateById(Article article) {
        //根据主键id修改
        articleMapper.updateById(article);

        //根据条件修改
//        UpdateWrapper<Article> wrapper = new UpdateWrapper<>();
//        wrapper.eq("id",article.getId());
//        articleMapper.update(article, wrapper);

    }


    /**
     * 根据id删除文章
     * @param articleId
     */
    public void deleteById(String articleId) {
        //根据id删除
        articleMapper.deleteById(articleId);

    }

    /**
     * 查询分页结果集
     * @param map
     * @param page
     * @param size
     * @return
     */
    public Page<Article> findByPage(Map<String, Object> map, Integer page, Integer size) {
        //查询条件
        EntityWrapper<Article> wrapper = new EntityWrapper<>();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            if (StringUtils.isEmpty(map.get(key))){
                wrapper.eq(key,map.get(key));
            }
            //第一个参数是否把后面的条件加入到查询条件中
            //跟上面的方法效果一样
//            wrapper.eq(map.get(key)!=null,key,map.get(key));
        }
        //设置分页参数
        Page<Article> pageData = new Page<>(page,size);
        //执行查询
        //第一个分页参数,第二个是查询条件
        List<Article> list = articleMapper.selectPage(pageData,wrapper);
        //分页结果集转换为list， getRecords()
        pageData.setRecords(list);

        //返回
        return pageData;
    }
}
