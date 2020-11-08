package com.ommi.bloodbank.Models;

public class ModelServiceProvider {
    String id;
    String Name;
    String Phone;

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

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getMessege() {
        return Messege;
    }

    public void setMessege(String messege) {
        Messege = messege;
    }

    public String getParagraph() {
        return Paragraph;
    }

    public void setParagraph(String paragraph) {
        Paragraph = paragraph;
    }

    String Address;

    public ModelServiceProvider(String id, String name, String phone, String address,  String paragraph,String image) {
        this.id = id;
        Name = name;
        Phone = phone;
        Address = address;
        Paragraph = paragraph;
        this.image=image;
    }

    String Messege;
    String Paragraph;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String image;
}
