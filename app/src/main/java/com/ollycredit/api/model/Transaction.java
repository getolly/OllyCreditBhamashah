package com.ollycredit.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by abdulaziz on 10/23/16.
 */
public class Transaction {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("card_id")
    @Expose
    private Integer cardId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("currency_id")
    @Expose
    private Integer currencyId;
    @SerializedName("transaction_type_id")
    @Expose
    private Integer transactionTypeId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("merchant")
    @Expose
    private String merchant;
    @SerializedName("merchant_location")
    @Expose
    private String merchantLocation;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("date")
    @Expose
    private String date;
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

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(Integer transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getMerchantLocation() {
        return merchantLocation;
    }

    public void setMerchantLocation(String merchantLocation) {
        this.merchantLocation = merchantLocation;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
