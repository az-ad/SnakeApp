package com.example.snake.Utills;

public class Posts {
    private  String datePost,postDesc,postImageUrl,userProfileImageUrl,name;

    public Posts() {
    }

    public Posts(String datePost, String postDesc, String postImageUrl, String userProfileImageUrl, String nameUser) {
        this.datePost = datePost;
        this.postDesc = postDesc;
        this.postImageUrl = postImageUrl;
        this.userProfileImageUrl = userProfileImageUrl;
        this.name = nameUser;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
