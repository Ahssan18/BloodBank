package com.ommi.bloodbank.Models;

public class ModelPublicMesseges {
    String post;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    String key;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    String type;


    String bloodGroup;

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    int Score;

    public int getMyVote() {
        return myVote;
    }

    public void setMyVote(int myVote) {
        this.myVote = myVote;
    }

    int myVote;

    public ModelPublicMesseges(String post, String country, String id, String image, String name, String date, String type, String bloodGroup, int Score, int myVote, String key) {
        this.post = post;
        this.country = country;
        this.id = id;
        this.image = image;
        this.name = name;
        this.date = date;
        this.type = type;
        this.bloodGroup = bloodGroup;
        this.Score = Score;
        this.myVote = myVote;
        this.key = key;
    }


    public ModelPublicMesseges() {
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String country;
    String id;
    String image;
    String name;
    String date;
}
