package dev.iamfoodie.rxjava.models;

import com.google.gson.annotations.SerializedName;

public class Comment {

    private Integer postId;

    private Integer id;

    private String name;

    private String email;

    @SerializedName("body")
    private String content;

    public Comment() {}

    public Comment(Integer postId, Integer id, String name, String email, String content) {
        this.postId = postId;
        this.id = id;
        this.name = name;
        this.email = email;
        this.content = content;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
