package com.zrk.bsblog.domain;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;


@Entity
public class Blog implements Serializable {

    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "标题不能为空")
    @Size(min = 2,max = 50)
    @Column(nullable = false,length = 50)
    private String title;

    @NotEmpty(message = "摘要不能为空")
    @Size(min = 2,max = 300)
    @Column(nullable = false)
    private String summary;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @NotEmpty(message = "内容不能为空")
    @Size(min = 2)
    @Column(nullable = false)
    private String content;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @NotEmpty(message = "内容不能为空")
    @Size(min = 2)
    @Column(nullable = false)
    private String htmlContent;

    @OneToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Timestamp createTime;

    @Column(name = "readSize")
    private Integer readSize=0;

    @Column(name = "commentSize")
    private Integer commentSize=0;

    @Column(name = "voteSize")
    private Integer voteSize=0;

    /*@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "blog_id")
    private List<Comment> comments;*/
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "blog_comment", joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"))
    private List<Comment> comments;

    @OneToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;

    @Column(name = "tags",length = 100)
    private String tags;

   /* @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "blog_id")
    private List<Vote> votes;*/
   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   @JoinTable(name = "blog_vote", joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"),
           inverseJoinColumns = @JoinColumn(name = "vote_id", referencedColumnName = "id"))
   private List<Vote> votes;

    protected Blog() {
    }

    public Blog(String title, String summary, String content) {
        this.title = title;
        this.summary = summary;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        PegDownProcessor peg=new PegDownProcessor(Extensions.ALL_WITH_OPTIONALS);
        this.htmlContent=peg.markdownToHtml(content);

    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
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

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getReadSize() {
        return readSize;
    }

    public void setReadSize(Integer readSize) {
        this.readSize = readSize;
    }

    public Integer getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(Integer commentSize) {
        this.commentSize = commentSize;
    }

    public Integer getVoteSize() {
        return voteSize;
    }

    public void setVoteSize(Integer voteSize) {
        this.voteSize = voteSize;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        this.commentSize=this.comments.size();
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
        this.voteSize = this.votes.size();
    }

    //添加评论
    public void addComment(Comment comment){
        this.comments.add(comment);
        this.commentSize = this.comments.size();
    }

    //删除评论
    public void removeComment(Long commentId){
        for (int index = 0;index < this.comments.size();index++){
            if (comments.get(index).getId() == commentId){
                this.comments.remove(index);
                break;
            }
        }
        this.commentSize = this.comments.size();
    }

    //点赞
    public boolean addVote(Vote vote){
        boolean isExist=false;
        //判断重复
        for (int index=0; index < this.votes.size(); index ++ ) {
            if (this.votes.get(index).getUser().getId() == vote.getUser().getId()) {
                isExist = true;
                break;
            }
        }

        if (!isExist) {
            this.votes.add(vote);
            this.voteSize = this.votes.size();
        }

        return isExist;
    }

    //取消点赞
    public void removeVote(Long voteId) {
        for (int index=0; index < this.votes.size(); index ++ ) {
            if (this.votes.get(index).getId() == voteId) {
                this.votes.remove(index);
                break;
            }
        }

        this.voteSize = this.votes.size();
    }

}
