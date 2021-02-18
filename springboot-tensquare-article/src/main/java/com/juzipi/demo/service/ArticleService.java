package com.juzipi.demo.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.juzipi.demo.client.NoticeClient;
import com.juzipi.demo.mapper.ArticleMapper;
import com.juzipi.demo.pojo.Article;
import com.juzipi.demo.pojo.Notice;
import com.juzipi.demo.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private NoticeClient noticeClient;

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
        //TODO:使用jwt鉴权获取当前用户的信息 用户的id 也就是文章作者id
        String userId = "1";
        //设置用户id
        article.setUserid(userId);

        //使用分布式id生成器
        String id = idWorker.nextId() + "";
        article.setId(id);

        //初始化特殊数据
        article.setVisits(0);//浏览量
        article.setThumbup(0);//点赞数
        article.setComment(0);//评论数
        //新增
        articleMapper.insert(article);

        //新增文章后，创建消息，通知给订阅者
//        String userKey = "article-subscribe" + article.getUserid();
        //获取订阅者信息
        String authorKey = "订阅者id: " + userId;
        Set<String> members = stringRedisTemplate.boundSetOps(authorKey).members();

        //给订阅者创建消息通知
        for (String uid: members) {
            //创建消息对象
            Notice notice = new Notice();
            //接收消息的用户id
            notice.setReceiverId(uid);
            //作者id
            notice.setOperatorId(userId);
            //操作类型(评论，点赞等)
            notice.setAction("publish");
            //被操作的对象，例如文章，评论等
            notice.setTargetType("article");
            //被操作对象的id，例如文章id，评论id
            notice.setTargetId(id);
            //通知类型
            notice.setType("sys");

            noticeClient.save(notice);
        }


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



    public Boolean subscribe(String userId , String articleId) {
        //根据文章id查询文章作者id
        String authorId = articleMapper.selectById(articleId).getUserid();

        //存放用户订阅信息的集合key，存放作者id
        //article-subscribe
        String userKey = "文章订阅信息: 存放作者id" + userId;
        //存放作者订阅者的信息的集合key，存放订阅者id
        //article-author
        String authorKey = "文章读者信息: 存放用户id"+authorId;
        //查询用户订阅关系，是否订阅

        Boolean flag = stringRedisTemplate.boundSetOps(userKey).isMember(authorId);
        if (flag){
            //取消订阅
            //在用户订阅的信息的集合中删除订阅的作者
            stringRedisTemplate.boundSetOps(userKey).remove(authorId);

            //在用户订阅的信息的集合中删除订阅者
            stringRedisTemplate.boundSetOps(authorKey).remove(userId);

            //返回false
            return false;

        }
        //订阅
        stringRedisTemplate.boundSetOps(userKey).add(authorId);
        stringRedisTemplate.boundSetOps(authorKey).add(userId);
        //true
        return true;


    }
}
