package com.juzipi.demo.service;

import com.juzipi.demo.mapper.NoticeFreshMapper;
import com.juzipi.demo.mapper.NoticeMapper;
import com.juzipi.demo.pojo.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private NoticeFreshMapper noticeFreshMapper;
}
