package com.ommi.bloodbank.Models;

public class ModelDonnar {
    String id;
    String name;
    String address;
    String age;
    String lat;
    String lng;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String country;
    String city;
    String totalRating;
    String DonationTime;

    public String getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(String totalRating) {
        this.totalRating = totalRating;
    }

    public String getDonationTime() {
        return DonationTime;
    }

    public void setDonationTime(String donationTime) {
        DonationTime = donationTime;
    }

    public String getReward() {
        return Reward;
    }

    public void setReward(String reward) {
        Reward = reward;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    String Reward;
    String Status;

    public String getPlasma() {
        return Plasma;
    }

    public void setPlasma(String plasma) {
        Plasma = plasma;
    }

    public String getBloodTransfusion() {
        return BloodTransfusion;
    }

    public void setBloodTransfusion(String bloodTransfusion) {
        BloodTransfusion = bloodTransfusion;
    }

    String Plasma, BloodTransfusion;

    public ModelDonnar(String id, String name, String address, String age, String lat, String lng, String country, String city, String phone, String group, String img, String type,String totalRating,String Donationtime,String reward,String status) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.age = age;
        this.lat = lat;
        this.lng = lng;
        this.country = country;
        this.city = city;
        this.phone = phone;
        this.group = group;
        this.img = img;
        this.type = type;
        this.totalRating=totalRating;
        this.DonationTime=Donationtime;
        this.Reward=reward;
        this.Status=status;
    }

    String phone;
    String group;
    String img;
    String type;
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }



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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }




}
