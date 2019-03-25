package com.zrk.bsblog.repository;

import com.zrk.bsblog.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote,Long> {
}
