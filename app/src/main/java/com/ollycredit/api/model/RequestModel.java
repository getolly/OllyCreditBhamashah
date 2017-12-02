package com.ollycredit.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public class RequestModel {


    @SerializedName("phone")
    private String phone;

    @SerializedName("address")
    private String address;

    @SerializedName("pin_code")
    private String pincode;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    @SerializedName("residence_date")
    private String residence_date;

    @SerializedName("aadhaar_num")
    private String aadhaar_num;

    public String getAadhaar_num() {
        return aadhaar_num;
    }

    public void setAadhaar_num(String aadhaar_num) {
        this.aadhaar_num = aadhaar_num;
    }

    public String getPan_num() {
        return pan_num;
    }

    public void setPan_num(String pan_num) {
        this.pan_num = pan_num;
    }

    @SerializedName("pan_num")

    private String pan_num;


    @SerializedName("monthly_income")
    private String monthly_income;

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @SerializedName("designation")

    private String designation;

    public String getMonthly_income() {
        return monthly_income;
    }

    public void setMonthly_income(String monthly_income) {
        this.monthly_income = monthly_income;
    }

    public String getPincode() {
        return pincode;
    }

    public String getResidence_date() {
        return residence_date;
    }

    public void setResidence_date(String residence_date) {
        this.residence_date = residence_date;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @SerializedName("name")

    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("pin")
    private String pin;

    @SerializedName("password")
    private String password;

    @SerializedName("org_id")
    private int OrgId;

    public int getOrgId() {
        return OrgId;
    }

    public void setOrgId(int orgId) {
        OrgId = orgId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @SerializedName("dob")
    private String dob;

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getFname() {

        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    @SerializedName("token")
    private String token;

    @SerializedName("new_login")
    private Boolean newLoginflag;

    @SerializedName("content")
    private String feedbackContent;

    @SerializedName("data")
    private File feedbackData;

    @SerializedName("first_name")
    private String fname;

    @SerializedName("last_name")
    private String last;


    public Boolean getNewLoginflag() {
        return newLoginflag;
    }

    public void setNewLoginflag(Boolean newLoginflag) {
        this.newLoginflag = newLoginflag;
    }
    //    @SerializedName("password")
//    private String password;

    //TODO remove these two fields
    /////////////
    @SerializedName("question_id")
    private String questionID;
    @SerializedName("answer")
    private String answer;
    @SerializedName("pan")
    private String pan;

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }


    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public File getFeedbackData() {
        return feedbackData;
    }

    public void setFeedbackData(File feedbackData) {
        this.feedbackData = feedbackData;
    }


}
