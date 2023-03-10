package com.example.snake;

public class UserComment {
    private String comment;
    private  String rating;

    public UserComment() {
        this.comment = comment;
        this.rating = rating;
    }

    public UserComment(String comment, String rating) {
        this.comment = comment;
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
