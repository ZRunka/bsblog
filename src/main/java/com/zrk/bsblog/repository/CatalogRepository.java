package com.zrk.bsblog.repository;

import com.zrk.bsblog.domain.Authority;
import com.zrk.bsblog.domain.Catalog;
import com.zrk.bsblog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatalogRepository extends JpaRepository<Catalog,Long> {

    //根据用户查询
    List<Catalog> findByUser(User user);

    //根据用户、分类名称查询
    List<Catalog> findByUserAndName(User user,String name);
}
