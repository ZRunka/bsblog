package com.zrk.bsblog.service;

import com.zrk.bsblog.domain.User;
import com.zrk.bsblog.domain.es.EsBlog;
import com.zrk.bsblog.vo.TagVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EsBlogService {

    //删除EsBlog
    void removeEsBlog(String id);

    //更新EsBlog
    EsBlog updateEsBlog(EsBlog esBlog);

    //根据Blog的Id获取EsBlog
    EsBlog getEsBlogByBlogId(Long blogId);

    //最新博客列表，分页
    Page<EsBlog> listNewsEsBlogs(String keyword, Pageable pageable);

    //最热博客列表，分页
    Page<EsBlog> listHotestEsBlogs(String keyword,Pageable pageable);

    //博客列表，分页
    Page<EsBlog> listEsBlogs(Pageable pageable);

    //最新前5
    List<EsBlog> listTop5NewestEsBlogs();

    //最热前5
    List<EsBlog> listTop5HotestEsBlogs();

    //最热前30标签
    List<TagVO> listTop30Tags();

    //最热前12用户
    List<User> listTop12Users();
}
