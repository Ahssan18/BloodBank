package com.ommi.bloodbank.Models;

public class ModelNotification {
   String id;
    String blood;
    String address;
    String date;
    String Reward;
    String DonationTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    String msg;

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

    public String getTotalRating() {
        return TotalRating;
    }

    public void setTotalRating(String totalRating) {
        TotalRating = totalRating;
    }

    String TotalRating;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDonnarid() {
        return donnarid;
    }

    public void setDonnarid(String donnarid) {
        this.donnarid = donnarid;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    String donnarid;

    public ModelNotification(String id, String blood, String address, String donnarid, String requestid,String date,String name,String DonationTime,String TotalRating,String Reward,String sender_id,String title,String msg) {
        this.id = id;
        this.blood = blood;
        this.address = address;
        this.donnarid = donnarid;
        this.requestid = requestid;
        this.date=date;
        this.name=name;
        this.Reward=Reward;
        this.DonationTime=DonationTime;
        this.TotalRating=TotalRating;
        this.sender_id=sender_id;
        this.msg=msg;
        this.title=title;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    String sender_id;

    String requestid;
}
