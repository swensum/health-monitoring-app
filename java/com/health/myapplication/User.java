package com.health.myapplication;
public class User {
    private int id;
    private String fullName;
    private String mobile;
    private String address;
    private String email;
    private String password;
    private String sex;
    private String division;
    private String profileImageUri;
    public User(String fullName, String mobile, String address, String email, String password, String sex, String division, String profileImageUri) {
        this.id = -1;
        this.fullName = fullName;
        this.mobile = mobile;
        this.address = address;
        this.email = email;
        this.password = password;
        this.sex = sex;
        this.division = division;
        this.profileImageUri = profileImageUri;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public String getSex() {
        return sex;
    }



    public String getDivision() {
        return division;
    }


    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfileImageUri() {
        return profileImageUri;
    }

    public void setProfileImageUri(String profileImageUri) {
        this.profileImageUri = profileImageUri;
    }


    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}
