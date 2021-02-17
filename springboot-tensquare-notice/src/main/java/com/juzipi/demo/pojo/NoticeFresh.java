package com.juzipi.demo.pojo;

import com.baomidou.mybatisplus.annotations.TableName;

@TableName("tb_notice_fresh")
public class NoticeFresh implements CharSequence {
    
    private String userId;
    private String noticeId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

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