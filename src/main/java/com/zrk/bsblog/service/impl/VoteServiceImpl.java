package com.zrk.bsblog.service.impl;

import com.zrk.bsblog.domain.Vote;
import com.zrk.bsblog.repository.VoteRepository;
import com.zrk.bsblog.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Override
    public void removeVote(Long id) {
        voteRepository.deleteById(id);
    }

    @Override
    public Vote getVoteById(Long id) {
        return voteRepository.getOne(id);
    }
}
