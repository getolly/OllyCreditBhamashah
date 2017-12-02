package com.ollycredit.api.model.kyc;

import com.google.gson.annotations.SerializedName;

public class KycRequestModel {

    @SerializedName("token")
    private String token;

    //kyc appoint

    @SerializedName("date_1")
    private String appointKYCDateOne;

    @SerializedName("date_2")
    private String appointKYCDateTwo;
    @SerializedName("time")
    private String appointKYCTime;
    @SerializedName("address_1")
    private String appointKYCAddressOne;
    @SerializedName("address_2")
    private String appointKYCAddressTwo;
    @SerializedName("phone")
    private String appointKYCPhone;
    @SerializedName("pincode")
    private String appointKYCPin;

    private boolean reschedule = false;

    public boolean isReschedule() {
        return reschedule;
    }

    public void setReschedule(boolean reschedule) {
        this.reschedule = reschedule;
    }


    public String getAppointKYCDateOne() {
        return appointKYCDateOne;
    }

    public void setAppointKYCDateOne(String appointKYCDateOne) {
        this.appointKYCDateOne = appointKYCDateOne;
    }

    public String getAppointKYCDateTwo() {
        return appointKYCDateTwo;
    }

    public void setAppointKYCDateTwo(String appointKYCDateTwo) {
        this.appointKYCDateTwo = appointKYCDateTwo;
    }

    public String getAppointKYCTime() {
        return appointKYCTime;
    }

    public void setAppointKYCTime(String appointKYCTime) {
        this.appointKYCTime = appointKYCTime;
    }

    public String getAppointKYCAddressOne() {
        return appointKYCAddressOne;
    }

    public void setAppointKYCAddressOne(String appointKYCAddressOne) {
        this.appointKYCAddressOne = appointKYCAddressOne;
    }

    public String getAppointKYCAddressTwo() {
        return appointKYCAddressTwo;
    }

    public void setAppointKYCAddressTwo(String appointKYCAddressTwo) {
        this.appointKYCAddressTwo = appointKYCAddressTwo;
    }

    public String getAppointKYCPhone() {
        return appointKYCPhone;
    }

    public void setAppointKYCPhone(String appointKYCPhone) {
        this.appointKYCPhone = appointKYCPhone;
    }

    public String getAppointKYCPin() {
        return appointKYCPin;
    }

    public void setAppointKYCPin(String appointKYCPin) {
        this.appointKYCPin = appointKYCPin;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
