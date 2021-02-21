package com.juzipi.demo.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.juzipi.demo.client.ArticleClient;
import com.juzipi.demo.client.UserClient;
import com.juzipi.demo.entity.Result;
import com.juzipi.demo.mapper.NoticeFreshMapper;
import com.juzipi.demo.mapper.NoticeMapper;
import com.juzipi.demo.pojo.Notice;
import com.juzipi.demo.pojo.NoticeFresh;
import com.juzipi.demo.util.IdWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private NoticeFreshMapper noticeFreshMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private ArticleClient articleClient;
    @Autowired
    private UserClient userClient;


    //完善消息内容
    private void getInfo(Notice notice){
        //查询用户昵称
        Result userResult = userClient.selectById(notice.getOperatorId());

        HashMap userMap = (HashMap) userResult.getData();
            String nickname = userMap.get("nickname").toString();
            //设置作者的用户昵称到消息中
            //设置操作者用户昵称
            notice.setOperatorName(nickname);

        //查询对象名称
        Result articleResult = articleClient.fidnById(notice.getTargetId());

        HashMap articleMap = (HashMap) articleResult.getData();
        //设置对象名称到消息通知中
        String title = articleMap.get("title").toString();
        //设置消息标题
        notice.setTargetName(title);

    }

    //根据id查询消息
    public Notice selectById(String id) {
        Notice notice = noticeMapper.selectById(id);
            //完善消息
            getInfo(notice);
            return notice;

    }



    //查询分页结果集
    public Page<Notice> selectByPage(Notice notice, Integer page, Integer size) {
        //封装分页对象
        Page<Notice> pageData = new Page<>(page, size);
        //执行分页查询
        List<Notice> noticeList = noticeMapper.selectPage(pageData, new EntityWrapper<>(notice));

        //完善消息
        for (Notice n : noticeList){
            getInfo(n);
        }

        //设置结果集到分页对象中
        pageData.setRecords(noticeList);
        //返回
        return pageData;
    }



    //查询分页结果集
//    public Page<Notice> selectByList(Notice notice, Integer page, Integer size) {
//        if (StringUtils.isNotBlank(notice)){
//            //封装分页对象
//            Page<Notice> pageData = new Page<>(page,size);
//
//            //执行分页查询
//            List<Notice> noticeList = noticeMapper.selectPage(pageData, new EntityWrapper<>(notice));
//
//            //完善消息
//            for (Notice n: noticeList) {
//                getInfo(n);
//            }
//            //设置结果集到分页对象中
//            pageData.setRecords(noticeList);
//
//            //返回
//            return pageData;
//        }
//
//        return null;
//    }


    //新增消息
    public void save(Notice notice) {
        //设置初始值
        //设置状态 0未读，1以读
            notice.setState("0");
            notice.setCreatetime(new Date());
            //使用分布式id生成器生成id
            String id = idWorker.nextId() + "";
            notice.setId(id);
            noticeMapper.insert(notice);

            //新的待推送消息入库
//            NoticeFresh noticeFresh = new NoticeFresh();
//            noticeFresh.setNoticeId(id);//消息id
//            noticeFresh.setUserId(notice.getReceiverId());//待通知用户的id
//            noticeFreshMapper.insert(noticeFresh);

    }


    //修改消息
    public void updateById(Notice notice) {
//        if (StringUtils.isNotBlank(notice)){
            noticeMapper.updateById(notice);
//        }
    }


    //根据用户id查询该用户的待推送消息
    public Page<NoticeFresh> freshPage(String userId, Integer page, Integer size) {
        //封装查询条件
        NoticeFresh noticeFresh = new NoticeFresh();
        noticeFresh.setUserId(userId);
        //创建分页对象
        Page<NoticeFresh> pageData = new Page<>(page,size);
        //执行查询
        List<NoticeFresh> list = noticeFreshMapper.selectPage(pageData, new EntityWrapper<>(noticeFresh));

        //设置查询结果集到对象中
        pageData.setRecords(list);

        //返回
        return pageData;

    }


    //删除通知
    public void freshDelete(NoticeFresh noticeFresh) {
//        if (StringUtils.isNotBlank(noticeFresh)){
            noticeFreshMapper.delete(new EntityWrapper<>(noticeFresh));
//        }

    }
}
