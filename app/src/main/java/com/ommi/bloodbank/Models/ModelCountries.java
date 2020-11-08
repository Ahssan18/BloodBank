package com.ommi.bloodbank.Models;

public class ModelCountries {
    public ModelCountries(String countryid, String countrycode, String countryname) {
        this.countryid = countryid;
        this.countrycode = countrycode;
        this.countryname = countryname;
    }

    String countryid;

    public String getCountryid() {
        return countryid;
    }

    public void setCountryid(String countryid) {
        this.countryid = countryid;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    String countrycode;
    String countryname;
}
