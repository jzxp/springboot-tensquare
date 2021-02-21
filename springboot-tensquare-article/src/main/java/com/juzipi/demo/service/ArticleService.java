package com.juzipi.demo.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.juzipi.demo.client.NoticeClient;
import com.juzipi.demo.mapper.ArticleMapper;
import com.juzipi.demo.pojo.Article;
import com.juzipi.demo.pojo.Notice;
import com.juzipi.demo.util.IdWorker;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
//    @Autowired
//    private RedisTemplate redisTemplate;
    @Autowired
    private NoticeClient noticeClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;

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
        String userId = "3";
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

        //发消息给 rabbitmq 就是新消息的通知
        //一参数：交换机名: 文章订阅(交换机)
        //二参数：路由键：使用时文章作者的id
        //三参数：消息内容，只完成消息提醒
        rabbitTemplate.convertAndSend("文章订阅(交换机)",userId,id);
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



    public Boolean subscribe(String articleId , String userId) {
        //根据文章id查询文章作者id
        String authorId = articleMapper.selectById(articleId).getUserid();

        //创建一个rabbitmq的管理器
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate.getConnectionFactory());
        //声明一个交换机，用来处理新增文章消息
        DirectExchange exchange = new DirectExchange("文章订阅(交换机)");
        rabbitAdmin.declareExchange(exchange);
        //创建队列，每个用户都有自己的队列，通过用户id区分
        Queue queue = new Queue("文章-订阅-"+userId,true);

        //声明交换机和队列的绑定关系，确保队列只收到对应作者的新增文章消息
        //queue：队列  exchange：交换机    authorId：作者id
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(authorId);


        //存放用户订阅信息的集合key，存放作者id
        //article-subscribe
        String userKey = "用户订阅信息(key) : 作者id(value)" + userId;
        //存放作者订阅者的信息的集合key，存放订阅者id
        //article-author
        String authorKey = "作者订阅信息(key) : 用户id(value)"+authorId;
        //查询用户订阅关系，是否订阅

        Boolean flag = stringRedisTemplate.boundSetOps(userKey).isMember(authorId);
        if (flag){
            //取消订阅
            //在用户订阅的信息的集合中删除订阅的作者
            stringRedisTemplate.boundSetOps(userKey).remove(authorId);

            //在用户订阅的信息的集合中删除订阅者
            stringRedisTemplate.boundSetOps(authorKey).remove(userId);

            //如果取消订阅，就删除绑定关系
            rabbitAdmin.removeBinding(binding);

            //返回false
            return false;

        }
        //订阅
        stringRedisTemplate.boundSetOps(userKey).add(authorId);
        stringRedisTemplate.boundSetOps(authorKey).add(userId);
        //true
        //如果订阅就声明要绑定的队列
        rabbitAdmin.declareQueue(queue);
        //添加绑定关系
        rabbitAdmin.declareBinding(binding);
        return true;


    }


    /**
     * 文章点赞
     * @param articleId
     * @param userId
     */
    public void thumbup(String articleId, String userId) {
        Article article = articleMapper.selectById(articleId);
        //设置点赞初始值

        article.setThumbup(article.getThumbup()+1);
        articleMapper.updateById(article);

        //点赞成功后发送信息提醒
        Notice notice = new Notice();
        //接收消息的用户id
        notice.setReceiverId(article.getUserid());
        //进行操作的用户的id
        notice.setOperatorId(userId);
        //操作类型
        notice.setAction("publish");
        //被操作的对象，文章，评论等
        notice.setTargetType("article");
        //被操作对象的id
        notice.setTargetId(articleId);
        //通知类型
        notice.setType("user");

        //保存消息
        noticeClient.save(notice);

        //创建一个rabbitmq的管理器
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate.getConnectionFactory());

        //创建队列，每个作者都有自己的队列，通过作者 id 区分
        Queue queue = new Queue("文章-点赞-"+article.getUserid(),true);
        rabbitAdmin.declareQueue(queue);
        //发消息到队列中
        rabbitTemplate.convertAndSend("文章-点赞-"+article.getUserid(),articleId);

    }




}
