package com.ommi.bloodbank.Models;

public class ModelServiceCategories {
    private String ServiveName;

    public String getServiveName() {
        return ServiveName;
    }

    public void setServiveName(String serviveName) {
        ServiveName = serviveName;
    }

    public String getServiveId() {
        return ServiveId;
    }

    public void setServiveId(String serviveId) {
        ServiveId = serviveId;
    }

    private String ServiveId;

    public ModelServiceCategories(String serviveName, String serviveId) {
        ServiveName = serviveName;
        ServiveId = serviveId;
    }
}
