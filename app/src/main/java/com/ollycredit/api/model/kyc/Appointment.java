
package com.ollycredit.api.model.kyc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Appointment {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("date_1")
    @Expose
    private String date1;
    @SerializedName("date_2")
    @Expose
    private String date2;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("address_1")
    @Expose
    private String address1;
    @SerializedName("address_2")
    @Expose
    private String address2;
    @SerializedName("mobile_num")
    @Expose
    private String mobileNum;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("pincode_id")
    @Expose
    private Integer pincodeId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPincodeId() {
        return pincodeId;
    }

    public void setPincodeId(Integer pincodeId) {
        this.pincodeId = pincodeId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
