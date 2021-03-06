package com.juzipi.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable, CharSequence {

    private String _id;
    //文章id
    private String articleid;
    //内容
    private String content;
    //用户id
    private String userid;
    //父id
    private String parentid;
    //发布日期
    private Date publishdate;
    //点赞数
    private Integer thumbup;


    @Override
    public int length() {
        return 0;
    }

    @Override
    public char charAt(int index) {
        return 0;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return null;
    }
}
