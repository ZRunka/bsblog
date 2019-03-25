package com.zrk.bsblog.repository;


import com.zrk.bsblog.domain.Blog;
import com.zrk.bsblog.domain.Catalog;
import com.zrk.bsblog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog,Long> {

    //根据用户名，博客标题分页查询博客列表
    Page<Blog> findByUserAndTitleLikeOrderByCreateTimeDesc(User user, String title,Pageable pageable);

    //根据用户名分页查询用户列表
    Page<Blog> findByUserAndTitleLike(User user, String title, Pageable pageable);

    //用户博客关键字查找
    Page<Blog> findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(
            String title, User user, String tags, User user2, Pageable pageable);

    //根据分类查询博客列表
    Page<Blog> findByCatalog(Catalog catalog, Pageable pageable);

    //所有微博关键字查找
    Page<Blog> findByTitleLike(String title, Pageable pageable);

    //所有博客带关键字查找
    Page<Blog> findByTitleLikeOrTagsLikeOrderByCreateTimeDesc(String title,
                                                              String tags, Pageable pageable);

    //最新前五博客
    @Query(value = "select * FROM  blog order by create_time desc limit 0,5", nativeQuery = true)
    List<Blog> findByBlogListTop5NewestBlogs();

    //最热前五博客
    @Query(value = "select * FROM  blog order by read_size desc,comment_size desc,vote_size desc,create_time desc limit 0,5", nativeQuery = true)
    List<Blog> findByBlogListTop5HotestBlogs();
}
