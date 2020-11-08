package com.ommi.bloodbank.Models;

public class ModelState {
    public String getStateid() {
        return stateid;
    }

    public void setStateid(String stateid) {
        this.stateid = stateid;
    }

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

    String stateid;

    public ModelState( String statename,String stateid) {
        this.stateid = stateid;
        this.statename = statename;
    }

    String statename;
}
