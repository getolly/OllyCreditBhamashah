
package com.ollycredit.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Card {

    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("cvv")
    @Expose
    private String cvv;
    @SerializedName("valid")
    @Expose
    private String valid;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("credit_limit")
    @Expose
    private Integer creditLimit;
    @SerializedName("max_credit_limit")
    @Expose
    private Integer maxCreditLimit;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

}
