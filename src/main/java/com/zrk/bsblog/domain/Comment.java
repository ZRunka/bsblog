package com.zrk.bsblog.domain;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

//评论
@Entity
public class Comment implements Serializable {

    private static final long serialVersion = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "评论内容不能为空")
    @Size(min=2,max=500)
    @Column(nullable = false)
    private String content;

    @OneToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Timestamp createTime;

    /*@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="blog_id")
    private Blog blog_id;*/

    protected Comment() {
    }

    public Comment(/*Blog blog_id,*/User user, String content) {
        this.content = content;
        this.user = user;
//        this.blog_id = blog_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    /*public Blog getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(Blog blog_id) {
        this.blog_id = blog_id;
    }*/
}
