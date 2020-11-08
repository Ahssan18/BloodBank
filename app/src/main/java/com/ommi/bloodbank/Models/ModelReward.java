package com.ommi.bloodbank.Models;

public class ModelReward {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(String rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public String getNoofBloodDonation() {
        return NoofBloodDonation;
    }

    public void setNoofBloodDonation(String noofBloodDonation) {
        NoofBloodDonation = noofBloodDonation;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    String id;
    String name;
    String rating;
    String rewardPoints;
    String NoofBloodDonation;



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;

    public ModelReward(String id, String name, String rating, String rewardPoints, String noofBloodDonation, String image,String date) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.rewardPoints = rewardPoints;
        NoofBloodDonation = noofBloodDonation;
        Image = image;
        this.date=date;
    }

    String Image;
}
