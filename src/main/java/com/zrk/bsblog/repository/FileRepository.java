package com.zrk.bsblog.repository;

import com.zrk.bsblog.domain.File;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRepository extends MongoRepository<File, String> {
}
