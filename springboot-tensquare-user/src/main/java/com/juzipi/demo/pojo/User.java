package com.juzipi.demo.pojo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;


@Data
@TableName("tb_user")
public class User {
    @TableId(type = IdType.INPUT)
    //id
    private String id;
    //手机
    private String mobile;
    //密码
    private String password;
    //
    private String nickname;
    //性别
    private String sex;
    private Date birthday;
    private String avatar;
    private String email;
    private Date regdate;
    private Long online;
    private String interest;
    private String personality;
    private Integer fanscount;
    private Integer followcount;

}
