package com.zrk.bsblog.repository.es;

import com.zrk.bsblog.domain.es.EsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

public interface EsBlogRepository extends ElasticsearchRepository<EsBlog,String> {

    //模糊查询
    Page<EsBlog> findByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(
            String title, String summary, String content, String tags, Pageable pageable);

    //根据Blog的id查询EsBlog
    EsBlog findByBlogId(Long blogId);

}
