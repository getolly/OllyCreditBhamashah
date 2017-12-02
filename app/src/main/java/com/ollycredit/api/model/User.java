
package com.ollycredit.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;

    @SerializedName("mobile_num")
    @Expose
    private String mobileNum;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("pan_num")
    @Expose
    private Object panNum;
    @SerializedName("pan_error")
    @Expose
    private Object panError;
    @SerializedName("credit_limit")
    @Expose
    private Integer creditLimit;
    @SerializedName("max_credit_limit")
    @Expose
    private Integer maxCreditLimit;
    @SerializedName("balance")
    @Expose
    private Float balance;
    @SerializedName("amount_used")
    @Expose
    private Float amountUsed;
    @SerializedName("address_1")
    @Expose
    private Object address1;
    @SerializedName("address_2")
    @Expose
    private Object address2;
    @SerializedName("date_of_birth")
    @Expose
    private Object dateOfBirth;
    @SerializedName("mobile_no_validated")
    @Expose
    private Integer mobileNoValidated;
    @SerializedName("email_validated")
    @Expose
    private Integer emailValidated;
    @SerializedName("organization_name")
    @Expose
    private String organizationName;
    @SerializedName("gender_name")
    @Expose
    private Object genderName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getPanNum() {
        return panNum;
    }

    public void setPanNum(Object panNum) {
        this.panNum = panNum;
    }

    public Object getPanError() {
        return panError;
    }

    public void setPanError(Object panError) {
        this.panError = panError;
    }

    public Integer getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Integer creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Integer getMaxCreditLimit() {
        return maxCreditLimit;
    }

    public void setMaxCreditLimit(Integer maxCreditLimit) {
        this.maxCreditLimit = maxCreditLimit;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Float getAmountUsed() {
        return amountUsed;
    }

    public void setAmountUsed(Float amountUsed) {
        this.amountUsed = amountUsed;
    }

    public Object getAddress1() {
        return address1;
    }

    public void setAddress1(Object address1) {
        this.address1 = address1;
    }

    public Object getAddress2() {
        return address2;
    }

    public void setAddress2(Object address2) {
        this.address2 = address2;
    }

    public Object getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Object dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getMobileNoValidated() {
        return mobileNoValidated;
    }

    public void setMobileNoValidated(Integer mobileNoValidated) {
        this.mobileNoValidated = mobileNoValidated;
    }

    public Integer getEmailValidated() {
        return emailValidated;
    }

    public void setEmailValidated(Integer emailValidated) {
        this.emailValidated = emailValidated;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Object getGenderName() {
        return genderName;
    }

    public void setGenderName(Object genderName) {
        this.genderName = genderName;
    }


}
