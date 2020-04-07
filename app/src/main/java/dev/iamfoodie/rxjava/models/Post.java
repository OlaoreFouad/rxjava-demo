package dev.iamfoodie.rxjava.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Post {

    @Expose()
    private Integer id;

    @Expose()
    private Integer userId;

    @Expose()
    private String title;

    @Expose()
    @SerializedName("body")
    private String content;

    @Expose(serialize = false, deserialize = false)
    private List<Comment> comments;

    public Post() {}

    public Post(Integer id, Integer userId, String title, String content, List<Comment> comments) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.comments = comments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
