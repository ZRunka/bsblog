package com.zrk.bsblog.service.impl;

import com.zrk.bsblog.domain.Comment;
import com.zrk.bsblog.repository.CommentRepository;
import com.zrk.bsblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void removeComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.getOne(id);
    }
}
