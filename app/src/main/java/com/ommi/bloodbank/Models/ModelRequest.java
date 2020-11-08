package com.ommi.bloodbank.Models;

public class ModelRequest {
    String id;
    String Name;
    String age;
    String Address;
    String Phone;
    String TotalRating;
    String Reward;

    public String getTotalRating() {
        return TotalRating;
    }

    public void setTotalRating(String totalRating) {
        TotalRating = totalRating;
    }

    public String getReward() {
        return Reward;
    }

    public void setReward(String reward) {
        Reward = reward;
    }

    public String getDonationTime() {
        return DonationTime;
    }

    public void setDonationTime(String donationTime) {
        DonationTime = donationTime;
    }

    String DonationTime;

    public ModelRequest(String id, String name, String age, String address, String phone, String blood, String country, String city, String lat, String lng, String text,String Reward,String DonationNumber,String TotalRating) {
        this.id = id;
        Name = name;
        this.age = age;
        Address = address;
        Phone = phone;
        Blood = blood;
        this.country = country;
        this.city = city;
        this.lat = lat;
        this.lng = lng;
        this.text = text;
        this.DonationTime=DonationNumber;
        this.TotalRating=TotalRating;
        this.Reward=Reward;
    }

    String Blood;
    String country;
    String city;
    String lat;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    String lng;
    String text;




    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getBlood() {
        return Blood;
    }

    public void setBlood(String blood) {
        Blood = blood;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }



}
