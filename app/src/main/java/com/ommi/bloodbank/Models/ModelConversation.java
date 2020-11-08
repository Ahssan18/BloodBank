package com.ommi.bloodbank.Models;

public class ModelConversation {
    String Image;
    String Name;
    String ReadStatus;

    public String getReadStatus() {
        return ReadStatus;
    }

    public void setReadStatus(String readStatus) {
        ReadStatus = readStatus;
    }

    public String getMessege() {
        return Messege;
    }

    public void setMessege(String messege) {
        Messege = messege;
    }

    String Messege;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;
    public ModelConversation() {
    }

    public String getConversationId() {
        return ConversationId;
    }

    public void setConversationId(String conversationId) {
        ConversationId = conversationId;
    }

    String ConversationId;

    public ModelConversation(String image, String name, String usetType, String bloodGroup, String myUserID, String otherUserId, String ConversationId,String date) {
        Image = image;
        Name = name;
        UsetType = usetType;
        BloodGroup = bloodGroup;
        MyUserID = myUserID;
        OtherUserId = otherUserId;
        this.ConversationId=ConversationId;
        this.date=date;
    }

    String UsetType;
    String BloodGroup;

    public String getMyUserID() {
        return MyUserID;
    }

    public void setMyUserID(String myUserID) {
        MyUserID = myUserID;
    }

    public String getOtherUserId() {
        return OtherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        OtherUserId = otherUserId;
    }

    String MyUserID;
    String OtherUserId;


    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }

    public String getUsetType() {
        return UsetType;
    }

    public void setUsetType(String usetType) {
        UsetType = usetType;
    }



}
