package com.juzipi.demo.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


@Data
@ToString
//数据库表
@TableName("tb_article")
//Serializable 实现序列化接口
public class Article implements Serializable {
    //ID
    //手动输入方式
    @TableId(type = IdType.INPUT)
    private String id;
    //专栏id
    private String columnid;
    //用户id
    private String userid;
    //标题
    private String title;
    //文章正文
    private String content;
    //文章封面
    private String image;
    //发表日期
    private Date createtime;
    //修改日期
    private Date updatetime;
    //是否公开
    private String ispublic;
    //是否置顶
    private String istop;
    //浏览量
    private Integer visits;
    //点赞数
    private Integer thumbup;
    //评论数
    private Integer comment;
    //审核状态
    private String state;
    //所属频道
    private String channelid;
    //URL
    private String url;
    //类型
    private String type;


}
