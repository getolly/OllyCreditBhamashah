package com.ollycredit.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dilpreet on 24/10/17.
 */

public class RepaymentFetchedData {

    @SerializedName("name")
    public String name;
    @SerializedName("content")
    public String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
