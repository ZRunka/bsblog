package com.zrk.bsblog.service;

import com.zrk.bsblog.domain.Authority;

import java.util.Optional;

public interface AuthorityService {

    Authority getAuthorityById(Long id);
}
