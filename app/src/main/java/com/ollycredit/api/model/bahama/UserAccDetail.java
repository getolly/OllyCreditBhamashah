
package com.ollycredit.api.model.bahama;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAccDetail {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("accountNumber")
    @Expose
    private String accountNumber;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("bhamashahId")
    @Expose
    private String bhamashahId;
    @SerializedName("aadharId")
    @Expose
    private long aadharId;
    @SerializedName("memberId")
    @Expose
    private String memberId;
    @SerializedName("familyId")
    @Expose
    private String familyId;
    @SerializedName("mobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("fatherName")
    @Expose
    private String fatherName;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBhamashahId() {
        return bhamashahId;
    }

    public void setBhamashahId(String bhamashahId) {
        this.bhamashahId = bhamashahId;
    }

    public long getAadharId() {
        return aadharId;
    }

    public void setAadharId(long aadharId) {
        this.aadharId = aadharId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

}
