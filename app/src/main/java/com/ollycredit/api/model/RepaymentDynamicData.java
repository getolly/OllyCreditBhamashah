package com.ollycredit.api.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RepaymentDynamicData {



    //Repayment API Response
    @SerializedName("title")
    public String title="";
    @SerializedName("content")
    public String content="";




    public String getRepayTitle() {
        return title;
    }

    public void setRepayTitle(String title) {
        this.title = title;
    }

    public String getRepayContent() {
        return content;
    }

    public void setRepaycontent(String content) {
        this.content = content;
    }


}







