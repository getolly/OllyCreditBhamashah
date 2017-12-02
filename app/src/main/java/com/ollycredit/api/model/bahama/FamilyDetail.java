
package com.ollycredit.api.model.bahama;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FamilyDetail {

    @SerializedName("AADHAR_ID")
    @Expose
    private int aADHARID;
    @SerializedName("MOTHER_NAME_ENG")
    @Expose
    private String mOTHERNAMEENG;
    @SerializedName("DOB")
    @Expose
    private String dOB;
    @SerializedName("SPOUCE_NAME_HND")
    @Expose
    private String sPOUCENAMEHND;
    @SerializedName("M_ID")
    @Expose
    private String mID;
    @SerializedName("FATHER_NAME_HND")
    @Expose
    private String fATHERNAMEHND;
    @SerializedName("NAME_ENG")
    @Expose
    private String nAMEENG;
    @SerializedName("FATHER_NAME_ENG")
    @Expose
    private String fATHERNAMEENG;
    @SerializedName("GENDER")
    @Expose
    private String gENDER;

    @SerializedName("NAME_HND")
    @Expose
    private String nAMEHND;

    @SerializedName("SPOUCE_NAME_ENG")
    @Expose
    private String sPOUCENAMEENG;

    @SerializedName("MOTHER_NAME_HND")
    @Expose
    private String mOTHERNAMEHND;

    public String getMOTHERNAMEENG() {
        return mOTHERNAMEENG;
    }

    public void setMOTHERNAMEENG(String mOTHERNAMEENG) {
        this.mOTHERNAMEENG = mOTHERNAMEENG;
    }

    public String getDOB() {
        return dOB;
    }

    public void setDOB(String dOB) {
        this.dOB = dOB;
    }

    public String getSPOUCENAMEHND() {
        return sPOUCENAMEHND;
    }

    public void setSPOUCENAMEHND(String sPOUCENAMEHND) {
        this.sPOUCENAMEHND = sPOUCENAMEHND;
    }

    public String getMID() {
        return mID;
    }

    public void setMID(String mID) {
        this.mID = mID;
    }

    public String getFATHERNAMEHND() {
        return fATHERNAMEHND;
    }

    public void setFATHERNAMEHND(String fATHERNAMEHND) {
        this.fATHERNAMEHND = fATHERNAMEHND;
    }

    public String getNAMEENG() {
        return nAMEENG;
    }

    public void setNAMEENG(String nAMEENG) {
        this.nAMEENG = nAMEENG;
    }

    public String getFATHERNAMEENG() {
        return fATHERNAMEENG;
    }

    public void setFATHERNAMEENG(String fATHERNAMEENG) {
        this.fATHERNAMEENG = fATHERNAMEENG;
    }

    public String getGENDER() {
        return gENDER;
    }

    public void setGENDER(String gENDER) {
        this.gENDER = gENDER;
    }

    public String getNAMEHND() {
        return nAMEHND;
    }

    public void setNAMEHND(String nAMEHND) {
        this.nAMEHND = nAMEHND;
    }

    public String getSPOUCENAMEENG() {
        return sPOUCENAMEENG;
    }

    public void setSPOUCENAMEENG(String sPOUCENAMEENG) {
        this.sPOUCENAMEENG = sPOUCENAMEENG;
    }

    public String getMOTHERNAMEHND() {
        return mOTHERNAMEHND;
    }

    public void setMOTHERNAMEHND(String mOTHERNAMEHND) {
        this.mOTHERNAMEHND = mOTHERNAMEHND;
    }

}
