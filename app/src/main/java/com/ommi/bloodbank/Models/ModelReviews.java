package com.ommi.bloodbank.Models;

public class ModelReviews {
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String image;
    String name;
    String review;

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    float Rating;

    public ModelReviews(String image, String name, String review, String date, String id,float rating) {
        this.image = image;
        this.name = name;
        this.review = review;
        this.date = date;
        this.id = id;
        this.Rating=rating;
    }

    String date;
    String id;
}
