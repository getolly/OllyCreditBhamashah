
package com.ollycredit.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HofDetails {

    @SerializedName("AADHAR_ID")
    @Expose
    private String aADHARID;
    @SerializedName("MOTHER_NAME_ENG")
    @Expose
    private String mOTHERNAMEENG;
    @SerializedName("DOB")
    @Expose
    private String dOB;
    @SerializedName("BHAMASHAH_ID")
    @Expose
    private String bHAMASHAHID;

    @SerializedName("M_ID")
    @Expose
    private String mID;
    @SerializedName("FAMILYIDNO")
    @Expose
    private String fAMILYIDNO;

    @SerializedName("NAME_ENG")
    @Expose
    private String nAMEENG;
    @SerializedName("FATHER_NAME_ENG")
    @Expose
    private String fATHERNAMEENG;
    @SerializedName("GENDER")
    @Expose
    private String gENDER;

    @SerializedName("SPOUCE_NAME_ENG")
    @Expose
    private String sPOUCENAMEENG;




    public String getAADHARID() {
        return aADHARID;
    }

    public void setAADHARID(String aADHARID) {
        this.aADHARID = aADHARID;
    }

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

    public String getBHAMASHAHID() {
        return bHAMASHAHID;
    }

    public void setBHAMASHAHID(String bHAMASHAHID) {
        this.bHAMASHAHID = bHAMASHAHID;
    }


    public String getMID() {
        return mID;
    }

    public void setMID(String mID) {
        this.mID = mID;
    }

    public String getFAMILYIDNO() {
        return fAMILYIDNO;
    }

    public void setFAMILYIDNO(String fAMILYIDNO) {
        this.fAMILYIDNO = fAMILYIDNO;
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


    public String getSPOUCENAMEENG() {
        return sPOUCENAMEENG;
    }

    public void setSPOUCENAMEENG(String sPOUCENAMEENG) {
        this.sPOUCENAMEENG = sPOUCENAMEENG;
    }


}
