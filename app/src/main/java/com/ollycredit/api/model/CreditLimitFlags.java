
package com.ollycredit.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreditLimitFlags {

    @SerializedName("is_pan_number_linked")
    @Expose
    private Integer isPanNumberLinked;
    @SerializedName("is_card_linked")
    @Expose
    private Integer isCardLinked;
    @SerializedName("is_kyc_linked")
    @Expose
    private Integer isKycLinked;
    @SerializedName("has_successful_payments")
    @Expose
    private Integer hasSuccessfulPayments;

    public Integer getIsPanNumberLinked() {
        return isPanNumberLinked;
    }

    public void setIsPanNumberLinked(Integer isPanNumberLinked) {
        this.isPanNumberLinked = isPanNumberLinked;
    }

    public Integer getIsCardLinked() {
        return isCardLinked;
    }

    public void setIsCardLinked(Integer isCardLinked) {
        this.isCardLinked = isCardLinked;
    }

    public Integer getIsKycLinked() {
        return isKycLinked;
    }

    public void setIsKycLinked(Integer isKycLinked) {
        this.isKycLinked = isKycLinked;
    }

    public Integer getHasSuccessfulPayments() {
        return hasSuccessfulPayments;
    }

    public void setHasSuccessfulPayments(Integer hasSuccessfulPayments) {
        this.hasSuccessfulPayments = hasSuccessfulPayments;
    }

}
