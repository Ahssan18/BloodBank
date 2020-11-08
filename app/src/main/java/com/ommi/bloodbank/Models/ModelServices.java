package com.ommi.bloodbank.Models;

public class ModelServices {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    String Image;

    public ModelServices(String id, String image, String name) {
        this.id = id;
        Image = image;
        Name = name;
    }

    String Name;
}
