
package com.ollycredit.api.model.bahama;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPersonalDetails {

    @SerializedName("hof_Details")
    @Expose
    private HofDetails hofDetails;

    public HofDetails getHofDetails() {
        return hofDetails;
    }

    public void setHofDetails(HofDetails hofDetails) {
        this.hofDetails = hofDetails;
    }


}
