package com.zrk.bsblog.repository;

import com.zrk.bsblog.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {
}
