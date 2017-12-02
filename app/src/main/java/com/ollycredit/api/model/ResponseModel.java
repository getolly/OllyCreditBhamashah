package com.ollycredit.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ollycredit.utils.GlobalConstants;

import java.util.ArrayList;
import java.util.List;

public class ResponseModel {


    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("accountNumber")
    @Expose
    private String accountNumber;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("bhamashahId")
    @Expose
    private String bhamashahId;
    @SerializedName("aadharId")
    @Expose
    private String aadharId;
    @SerializedName("memberId")
    @Expose
    private String memberId;


    @SerializedName("AADHAR_ID")
    @Expose
    private String aADHARID;
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

    public String getaADHARID() {
        return aADHARID;
    }

    @SerializedName("hof_Details")
    @Expose
    public HofDetails hofDetails;

    public HofDetails getHofDetails() {
        return hofDetails;
    }

    public void setHofDetails(HofDetails hofDetails) {
        this.hofDetails = hofDetails;
    }

    public List<FamilyDetail> getFamilyDetails() {
        return familyDetails;
    }

    public void setFamilyDetails(List<FamilyDetail> familyDetails) {
        this.familyDetails = familyDetails;
    }

    @SerializedName("family_Details")

    @Expose
    public List<FamilyDetail> familyDetails = null;

    public void setaADHARID(String aADHARID) {
        this.aADHARID = aADHARID;
    }

    public String getmOTHERNAMEENG() {
        return mOTHERNAMEENG;
    }

    public void setmOTHERNAMEENG(String mOTHERNAMEENG) {
        this.mOTHERNAMEENG = mOTHERNAMEENG;
    }

    public String getdOB() {
        return dOB;
    }

    public void setdOB(String dOB) {
        this.dOB = dOB;
    }

    public String getsPOUCENAMEHND() {
        return sPOUCENAMEHND;
    }

    public void setsPOUCENAMEHND(String sPOUCENAMEHND) {
        this.sPOUCENAMEHND = sPOUCENAMEHND;
    }

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public String getfATHERNAMEHND() {
        return fATHERNAMEHND;
    }

    public void setfATHERNAMEHND(String fATHERNAMEHND) {
        this.fATHERNAMEHND = fATHERNAMEHND;
    }

    public String getnAMEENG() {
        return nAMEENG;
    }

    public void setnAMEENG(String nAMEENG) {
        this.nAMEENG = nAMEENG;
    }

    public String getfATHERNAMEENG() {
        return fATHERNAMEENG;
    }

    public void setfATHERNAMEENG(String fATHERNAMEENG) {
        this.fATHERNAMEENG = fATHERNAMEENG;
    }

    public String getgENDER() {
        return gENDER;
    }

    public void setgENDER(String gENDER) {
        this.gENDER = gENDER;
    }

    public String getnAMEHND() {
        return nAMEHND;
    }

    public void setnAMEHND(String nAMEHND) {
        this.nAMEHND = nAMEHND;
    }

    public String getsPOUCENAMEENG() {
        return sPOUCENAMEENG;
    }

    public void setsPOUCENAMEENG(String sPOUCENAMEENG) {
        this.sPOUCENAMEENG = sPOUCENAMEENG;
    }

    public Object getpHOTO() {
        return pHOTO;
    }

    public void setpHOTO(Object pHOTO) {
        this.pHOTO = pHOTO;
    }

    public String getmOTHERNAMEHND() {
        return mOTHERNAMEHND;
    }

    public void setmOTHERNAMEHND(String mOTHERNAMEHND) {
        this.mOTHERNAMEHND = mOTHERNAMEHND;
    }

    @SerializedName("NAME_HND")

    @Expose
    private String nAMEHND;
    @SerializedName("SPOUCE_NAME_ENG")
    @Expose
    private String sPOUCENAMEENG;
    @SerializedName("PHOTO")
    @Expose
    private Object pHOTO;
    @SerializedName("MOTHER_NAME_HND")
    @Expose
    private String mOTHERNAMEHND;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBhamashahId() {
        return bhamashahId;
    }

    public void setBhamashahId(String bhamashahId) {
        this.bhamashahId = bhamashahId;
    }

    public String getAadharId() {
        return aadharId;
    }

    public void setAadharId(String aadharId) {
        this.aadharId = aadharId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    @SerializedName("familyId")
    @Expose

    private String familyId;
    @SerializedName("mobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("fatherName")
    @Expose
    private String fatherName;

    @SerializedName("card")
    public Card card;

    @SerializedName("flow_id")
    public int userFlow;

    public @GlobalConstants.AppFlow
    int getUserFlow() {
        return userFlow;
    }

    public void setUserFlow(int userFlow) {
        this.userFlow = userFlow;
    }

    @SerializedName("credit_limit_flags")
    @Expose
    public CreditLimitFlags creditLimitFlags;
    @SerializedName("credit_limit_amounts")
    @Expose
    public CreditLimitAmounts creditLimitAmounts;


    @SerializedName("bill_counter")
    int billCounter;

    public int getBillCounter() {
        return billCounter;
    }

    public void setBillCounter(int billCounter) {
        this.billCounter = billCounter;
    }

    public CreditLimitFlags getCreditLimitFlags() {
        return creditLimitFlags;
    }

    public void setCreditLimitFlags(CreditLimitFlags creditLimitFlags) {
        this.creditLimitFlags = creditLimitFlags;
    }

    public CreditLimitAmounts getCreditLimitAmounts() {
        return creditLimitAmounts;
    }

    public void setCreditLimitAmounts(CreditLimitAmounts creditLimitAmounts) {
        this.creditLimitAmounts = creditLimitAmounts;
    }


    @SerializedName("preuser")
    @Expose
    private Preuser preuser;

    public Preuser getPreuser() {
        return preuser;
    }

    public void setPreuser(Preuser preuser) {
        this.preuser = preuser;
    }

    @SerializedName("pan_verified")
    @Expose
    private boolean panVerified;

    @SerializedName("pan_error")
    @Expose
    private String panError;

    public boolean isPanVerified() {
        return panVerified;
    }

    public void setPanVerified(boolean panVerified) {
        this.panVerified = panVerified;
    }

    public String getPanError() {
        return panError;
    }

    public void setPanError(String panError) {
        this.panError = panError;
    }

    @SerializedName("user")
    public User user;

    @SerializedName("transactions")
    public ArrayList<Transaction> transactionList;


    @SerializedName("last_transaction")
    @Expose
    private LastTransaction lastTransaction;


    public LastTransaction getLastTransaction() {
        return lastTransaction;
    }

    public void setLastTransaction(LastTransaction lastTransaction) {
        this.lastTransaction = lastTransaction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getStatusMessage() {
        return status;
    }

    public void setStatusMesaage(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

//    public String getServer_response() {
//        return server_response;
//    }
//
//    public void setServer_response(String server_response) {
//        this.server_response = server_response;
//    }
//
//    public String getReason() {
//        return reason;
//    }
//
//    public void setReason(String reason) {
//        this.reason = reason;
//    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(ArrayList<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }


    @SerializedName("payment_hash")
    @Expose
    private String paymentHash;
    @SerializedName("vas_for_mobile_sdk_hash")
    @Expose
    private String vasForMobileSdkHash;
    @SerializedName("payment_related_details_for_mobile_sdk_hash")
    @Expose
    private String paymentRelatedDetailsForMobileSdkHash;
    @SerializedName("delete_user_card_hash")
    @Expose
    private String deleteUserCardHash;
    @SerializedName("get_user_cards_hash")
    @Expose
    private String getUserCardsHash;
    @SerializedName("edit_user_card_hash")
    @Expose
    private String editUserCardHash;
    @SerializedName("save_user_card_hash")
    @Expose
    private String saveUserCardHash;
    @SerializedName("check_offer_status_hash")
    @Expose
    private String checkOfferStatusHash;

    public String getPaymentHash() {
        return paymentHash;
    }

    public void setPaymentHash(String paymentHash) {
        this.paymentHash = paymentHash;
    }

    public String getVasForMobileSdkHash() {
        return vasForMobileSdkHash;
    }

    public void setVasForMobileSdkHash(String vasForMobileSdkHash) {
        this.vasForMobileSdkHash = vasForMobileSdkHash;
    }

    public String getPaymentRelatedDetailsForMobileSdkHash() {
        return paymentRelatedDetailsForMobileSdkHash;
    }

    public void setPaymentRelatedDetailsForMobileSdkHash(String paymentRelatedDetailsForMobileSdkHash) {
        this.paymentRelatedDetailsForMobileSdkHash = paymentRelatedDetailsForMobileSdkHash;
    }

    public String getDeleteUserCardHash() {
        return deleteUserCardHash;
    }

    public void setDeleteUserCardHash(String deleteUserCardHash) {
        this.deleteUserCardHash = deleteUserCardHash;
    }

    public String getGetUserCardsHash() {
        return getUserCardsHash;
    }

    public void setGetUserCardsHash(String getUserCardsHash) {
        this.getUserCardsHash = getUserCardsHash;
    }

    public String getEditUserCardHash() {
        return editUserCardHash;
    }

    public void setEditUserCardHash(String editUserCardHash) {
        this.editUserCardHash = editUserCardHash;
    }

    public String getSaveUserCardHash() {
        return saveUserCardHash;
    }

    public void setSaveUserCardHash(String saveUserCardHash) {
        this.saveUserCardHash = saveUserCardHash;
    }

    public String getCheckOfferStatusHash() {
        return checkOfferStatusHash;
    }

    public void setCheckOfferStatusHash(String checkOfferStatusHash) {
        this.checkOfferStatusHash = checkOfferStatusHash;
    }


    //Repayment API Response
    @SerializedName("amount_used")
    public String amount_used;

    @SerializedName("amount_due")
    public String amount_due;

    @SerializedName("is_defaulted")
    public boolean is_defaulted;

    @SerializedName("due_text")
    public RepaymentDynamicData repay_due;

    @SerializedName("percentage_text")
    public RepaymentDynamicData repay_peresentage;


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("min_amount_due")
    @Expose
    private Double minAmountDue;
    @SerializedName("min_amount_percentage")
    @Expose
    private Double minAmountPercentage;

    @SerializedName("token")
    @Expose
    private String token;

    public String getAmount_used() {
        return amount_used;
    }

    public void setAmount_used(String amount_used) {
        this.amount_used = amount_used;
    }

    public String getAmount_due() {
        return amount_due;
    }

    public void setAmount_due(String amount_due) {
        this.amount_due = amount_due;
    }

    public boolean isIs_defaulted() {
        return is_defaulted;
    }

    public void setIs_defaulted(boolean is_defaulted) {
        this.is_defaulted = is_defaulted;
    }

    public RepaymentDynamicData getRepay_due() {
        return repay_due;
    }

    public void setRepay_due(RepaymentDynamicData repay_due) {
        this.repay_due = repay_due;
    }

    public RepaymentDynamicData getRepay_peresentage() {
        return repay_peresentage;
    }

    public void setRepay_peresentage(RepaymentDynamicData repay_peresentage) {
        this.repay_peresentage = repay_peresentage;
    }

    public Double getMinAmountDue() {
        return minAmountDue;
    }

    public void setMinAmountDue(Double minAmountDue) {
        this.minAmountDue = minAmountDue;
    }

    public Double getMinAmountPercentage() {
        return minAmountPercentage;
    }

    public void setMinAmountPercentage(Double minAmountPercentage) {
        this.minAmountPercentage = minAmountPercentage;
    }

    public List<RepaymentFetchedData> getRepaymentFetchedDataList() {
        return repaymentFetchedDataList;
    }

    public void setRepaymentFetchedDataList(List<RepaymentFetchedData> repaymentFetchedDataList) {
        this.repaymentFetchedDataList = repaymentFetchedDataList;
    }

    @SerializedName("contents")
    List<RepaymentFetchedData> repaymentFetchedDataList;

    public List<RepaymentFetchedData> getRepaymentDisplayDataList() {
        return repaymentFetchedDataList;
    }

    public void setRepaymentDisplayDataList(List<RepaymentFetchedData> repaymentFetchedDataList) {
        this.repaymentFetchedDataList = repaymentFetchedDataList;
    }

    public String getAmountUsed() {
        return amount_used;
    }

    public void setAmountUsed(String amount_used) {
        this.amount_used = amount_used;
    }

    public String getAmountDue() {
        return amount_due;
    }

    public void setAmountDue(String amount_due) {
        this.amount_due = amount_due;
    }

    public boolean getIsDefaulted() {
        return is_defaulted;
    }

    public void setIsDefaulted(boolean is_defaulted) {
        this.is_defaulted = is_defaulted;
    }

    public RepaymentDynamicData getdue() {
        return repay_due;
    }

    public void setdue(RepaymentDynamicData due) {
        this.repay_due = due;
    }

    public RepaymentDynamicData getperesentage() {
        return repay_peresentage;
    }

    public void setperesentage(RepaymentDynamicData peresentage) {
        this.repay_peresentage = peresentage;
    }


    public Boolean getCardActive() {
        return cardActive;
    }

    public void setCardActive(Boolean cardActive) {
        this.cardActive = cardActive;
    }

    @SerializedName("card_active")
    @Expose
    private Boolean cardActive;


}
