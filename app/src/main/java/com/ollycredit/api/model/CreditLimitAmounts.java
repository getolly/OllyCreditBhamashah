
package com.ollycredit.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreditLimitAmounts {

    @SerializedName("pan_number_linked")
    @Expose
    private String panNumberLinked;
    @SerializedName("card_linked")
    @Expose
    private String cardLinked;
    @SerializedName("kyc_linked")
    @Expose
    private String kycLinked;
    @SerializedName("successful_payments")
    @Expose
    private String successfulPayments;

    public String getPanNumberLinked() {
        return panNumberLinked;
    }

    public void setPanNumberLinked(String panNumberLinked) {
        this.panNumberLinked = panNumberLinked;
    }

    public String getCardLinked() {
        return cardLinked;
    }

    public void setCardLinked(String cardLinked) {
        this.cardLinked = cardLinked;
    }

    public String getKycLinked() {
        return kycLinked;
    }

    public void setKycLinked(String kycLinked) {
        this.kycLinked = kycLinked;
    }

    public String getSuccessfulPayments() {
        return successfulPayments;
    }

    public void setSuccessfulPayments(String successfulPayments) {
        this.successfulPayments = successfulPayments;
    }

}
