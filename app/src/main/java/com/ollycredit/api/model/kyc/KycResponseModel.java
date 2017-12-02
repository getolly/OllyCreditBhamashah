
package com.ollycredit.api.model.kyc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KycResponseModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("appointment")
    @Expose
    private Appointment appointment;


    @SerializedName("token")
    @Expose
    private String token;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
