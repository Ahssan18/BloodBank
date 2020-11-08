package com.ommi.bloodbank.Models;

public class ModelUserData {
    String id;
    String name;
    String phone;
    String image;
    String country;
    String city;
    String address;
    String plasma;
    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    String CountryCode;

    public String getPlasma() {
        return plasma;
    }

    public void setPlasma(String plasma) {
        this.plasma = plasma;
    }

    public String getBloodTransfusion() {
        return BloodTransfusion;
    }

    public void setBloodTransfusion(String bloodTransfusion) {
        BloodTransfusion = bloodTransfusion;
    }

    String BloodTransfusion;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLattitute() {
        return lattitute;
    }

    public void setLattitute(String lattitute) {
        this.lattitute = lattitute;
    }

    public String getLongitute() {
        return longitute;
    }

    public void setLongitute(String longitute) {
        this.longitute = longitute;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getUsertype() {
        return Usertype;
    }

    public void setUsertype(String usertype) {
        Usertype = usertype;
    }

    String lattitute;

    public ModelUserData(String id, String name, String phone, String image, String country, String city, String address, String lattitute, String longitute, String age, String bloodGroup, String usertype,String status) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.image = image;
        this.country = country;
        this.city = city;
        this.address = address;
        this.lattitute = lattitute;
        this.longitute = longitute;
        this.age = age;
        this.bloodGroup = bloodGroup;
        Usertype = usertype;
        this.status=status;
    }

    String longitute;
    String age;
    String bloodGroup;
    String Usertype;

}
