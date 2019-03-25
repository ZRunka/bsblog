package com.zrk.bsblog.service.impl;

import com.zrk.bsblog.domain.Authority;
import com.zrk.bsblog.repository.AuthorityRepository;
import com.zrk.bsblog.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Authority getAuthorityById(Long id) {
        return authorityRepository.getOne(id);
    }
}
