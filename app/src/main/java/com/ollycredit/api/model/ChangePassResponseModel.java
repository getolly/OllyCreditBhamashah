package com.ollycredit.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ch8n on 7/9/17.
 */

public class ChangePassResponseModel {
    @SerializedName("old_password")
    @Expose
    private String oldPassword;

    @SerializedName("new_password")
    @Expose
    private String newPassword;

    @SerializedName("token")
    @Expose
    public String token;

    @SerializedName("message")
    public String message;

    @SerializedName("status")
    public String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setOldPassword(String oldPassword) {

        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


}
