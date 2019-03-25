package com.zrk.bsblog.service;


import com.zrk.bsblog.domain.Catalog;
import com.zrk.bsblog.domain.User;

import java.util.List;

public interface CatalogService {

    //保存目录
    Catalog saveCatalog(Catalog catalog);

    //删除
    void removeCatalog(Long id);

    //根据id获取目录
    Catalog getCatalogById(Long id);

    //获取列表
    List<Catalog> listCatalogs(User user);
}
