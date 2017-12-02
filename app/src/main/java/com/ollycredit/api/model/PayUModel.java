package com.ollycredit.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayUModel implements Parcelable {
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("txnid")
    @Expose
    private String txnid;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("productinfo")
    @Expose
    private String productinfo;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("udf1")
    @Expose
    private String udf1;
    @SerializedName("udf2")
    @Expose
    private String udf2;
    @SerializedName("udf3")
    @Expose
    private String udf3;
    @SerializedName("udf4")
    @Expose
    private String udf4;
    @SerializedName("udf5")
    @Expose
    private String udf5;
    @SerializedName("user_credentials")
    @Expose
    private String userCredentials;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProductinfo() {
        return productinfo;
    }

    public void setProductinfo(String productinfo) {
        this.productinfo = productinfo;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUdf1() {
        return udf1;
    }

    public void setUdf1(String udf1) {
        this.udf1 = udf1;
    }

    public String getUdf2() {
        return udf2;
    }

    public void setUdf2(String udf2) {
        this.udf2 = udf2;
    }

    public String getUdf3() {
        return udf3;
    }

    public void setUdf3(String udf3) {
        this.udf3 = udf3;
    }

    public String getUdf4() {
        return udf4;
    }

    public void setUdf4(String udf4) {
        this.udf4 = udf4;
    }

    public String getUdf5() {
        return udf5;
    }

    public void setUdf5(String udf5) {
        this.udf5 = udf5;
    }

    public String getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(String userCredentials) {
        this.userCredentials = userCredentials;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.txnid);
        dest.writeString(this.amount);
        dest.writeString(this.productinfo);
        dest.writeString(this.firstname);
        dest.writeString(this.email);
        dest.writeString(this.udf1);
        dest.writeString(this.udf2);
        dest.writeString(this.udf3);
        dest.writeString(this.udf4);
        dest.writeString(this.udf5);
        dest.writeString(this.userCredentials);
    }

    public PayUModel() {
    }

    protected PayUModel(Parcel in) {
        this.key = in.readString();
        this.txnid = in.readString();
        this.amount = in.readString();
        this.productinfo = in.readString();
        this.firstname = in.readString();
        this.email = in.readString();
        this.udf1 = in.readString();
        this.udf2 = in.readString();
        this.udf3 = in.readString();
        this.udf4 = in.readString();
        this.udf5 = in.readString();
        this.userCredentials = in.readString();
    }

    public static final Parcelable.Creator<PayUModel> CREATOR = new Parcelable.Creator<PayUModel>() {
        @Override
        public PayUModel createFromParcel(Parcel source) {
            return new PayUModel(source);
        }

        @Override
        public PayUModel[] newArray(int size) {
            return new PayUModel[size];
        }
    };
}
