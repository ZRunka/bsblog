package com.zrk.bsblog.service;

import com.zrk.bsblog.domain.Vote;

import java.util.List;

public interface VoteService {

    //删除
    void removeVote(Long id);

    //根据id获取
    Vote getVoteById(Long id);

}
