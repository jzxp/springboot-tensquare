package com.juzipi.demo.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.juzipi.demo.entity.PageResult;
import com.juzipi.demo.entity.Result;
import com.juzipi.demo.entity.StatusCode;
import com.juzipi.demo.pojo.Notice;
import com.juzipi.demo.pojo.NoticeFresh;
import com.juzipi.demo.service.NoticeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("notice")
@CrossOrigin
public class NoticeController {

    @Autowired
    private NoticeService noticeService;


    /**
     * 根据id查询消息
     * @param id
     * @return
     */
    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    public Result selectById(@PathVariable String id){
        if (StringUtils.isNotBlank(id)){
            Notice notice = noticeService.selectById(id);
            return new Result(true, StatusCode.OK,"查询成功",notice);
        }
            return new Result(false, StatusCode.ERROR,"查询失败");


    }


    /**
     * 查询分页结果集
     * @param notice
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "search/{page}/{size}",method = RequestMethod.POST)
    public Result selectByList(@RequestBody Notice notice,
                               @PathVariable Integer page,
                               @PathVariable Integer size){
        Page<Notice> pageData = noticeService.selectByPage(notice,page,size);
        if (pageData == null){
            return new Result(false,StatusCode.ERROR,"查询失败");
        }
        PageResult<Notice> pageResult = new PageResult<>((long)pageData.getTotal(),pageData.getRecords());

        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }


    /**
     * 新增通知
     * @param notice
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Notice notice){
//        if (StringUtils.isNotBlank(notice)){
            noticeService.save(notice);

            return new Result(true,StatusCode.OK,"新增成功");
//        }
//            return new Result(false,StatusCode.ERROR,"新增失败");

    }


    /**
     * 修改通知
     * @param notice
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public Result updateById(@RequestBody Notice notice){
        String noticeId = notice.getId();
        noticeService.updateById(notice);
//        if (StringUtils.isNotBlank(noticeId)){
            return new Result(true,StatusCode.OK,"修改成功");
//        }
//        return new Result(false,StatusCode.ERROR,"修改失败");
    }


    /**
     * 根据用户id查询该用户的待推送消息
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "fresh/{userId}/{page}/{size}",method = RequestMethod.GET)
    public Result freshPage(@PathVariable String userId,
                            @PathVariable Integer page,
                            @PathVariable Integer size){
       Page<NoticeFresh> pageData = noticeService.freshPage(userId,page,size);

        PageResult<NoticeFresh> pageResult = new PageResult<>((long)pageData.getTotal(),pageData.getRecords());

        return new Result(true,StatusCode.OK,"查询成功",pageResult);

    }


    /**
     * 删除通知
     * @param noticeFresh
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public Result freshDelete(@RequestBody NoticeFresh noticeFresh){

//        if (StringUtils.isNotBlank(noticeFresh)){
            noticeService.freshDelete(noticeFresh);
            return new Result(true,StatusCode.OK,"删除成功");
//        }

//        return new Result(false,StatusCode.ERROR,"删除失败");
    }

}
