package com.zrk.bsblog.service;


import com.zrk.bsblog.domain.Comment;

public interface CommentService {


    //删除
    void removeComment(Long id);

    //根据id获取评论
    Comment getCommentById(Long id);
}
