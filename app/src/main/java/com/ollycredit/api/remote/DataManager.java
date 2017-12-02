package com.ollycredit.api.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.ollycredit.api.local.PreferencesHelper;
import com.ollycredit.api.model.ChangePassResponseModel;
import com.ollycredit.api.model.PayUModel;
import com.ollycredit.api.model.RequestModel;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.api.model.bahama.UserAccDetail;
import com.ollycredit.api.model.bahama.UserPersonalDetails;
import com.ollycredit.api.model.kyc.KycRequestModel;
import com.ollycredit.api.model.kyc.KycResponseModel;
import com.ollycredit.api.model.response.UpdateModel;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;


@Singleton
public class DataManager {

    private final PreferencesHelper preferencesHelper;
    private final BaseApiManager baseApiManager;
    private final long clientId;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper, BaseApiManager baseApiManager) {
        this.preferencesHelper = preferencesHelper;
        this.baseApiManager = baseApiManager;
        clientId = this.preferencesHelper.getClientId();
    }

    public Observable<ResponseModel> registerUser(RequestModel requestModel) {
        return baseApiManager.getApiService().registerUser(requestModel);
    }

    public Observable<ResponseModel> completeRegistration(RequestModel requestModel) {
        return baseApiManager.getApiService().completeRegistration(requestModel, requestModel.getToken());
    }

    public Observable<ResponseModel> verifyPhoneRegister(RequestModel requestModel) {
        return baseApiManager.getApiService().verifyPhoneRegister(requestModel, requestModel.
                getToken());
    }

    public Observable<ResponseModel> verifyPhoneLogin(RequestModel requestModel) {
        return baseApiManager.getApiService().verifyPhoneLogin(requestModel, requestModel.
                getToken());
    }

    public Observable<ResponseModel> choosePinForPreUser(RequestModel requestModel) {
        return baseApiManager.getApiService().createPinForUser(requestModel,
                requestModel.getToken());
    }

    public Observable<ResponseModel> choosePinForFlowOperation(RequestModel requestModel) {
        return baseApiManager.getApiService().changePinForFlowOperation(requestModel,
                requestModel.getToken());
    }

    public Observable<ResponseModel> login(RequestModel requestModel) {
        return baseApiManager.getApiService().loginPhone(requestModel);
    }

    public Observable<ResponseModel> checkLoginPin(RequestModel requestModel) {
        return baseApiManager.getApiService().checkLogInPin(requestModel, requestModel.getToken());
    }

    public Observable<ResponseModel> getPersonalDetails(RequestModel requestModel) {
        return baseApiManager.getApiService().getPersonalDetails(requestModel, requestModel.getToken());
    }

    public Observable<ResponseModel> getProffesionalDetails(RequestModel requestModel) {
        return baseApiManager.getApiService().getProffesionalDetails(requestModel, requestModel.getToken());
    }

    public Observable<ResponseModel> getDocumentStored(RequestModel requestModel) {
        return baseApiManager.getApiService().getDocumentStored(requestModel, requestModel.getToken());
    }


    public Observable<ResponseModel> verifyPassword(RequestModel requestModel) {
        return baseApiManager.getApiService().verifyPassword(requestModel, requestModel.getToken());
    }

    public Observable<ResponseModel> verifyOtpRecovery(RequestModel requestModel) {
        return baseApiManager.getApiService().verifyPhoneRecovery(requestModel, requestModel.getToken());
    }

    public Observable<ResponseModel> resetPin(RequestModel requestModel) {
        return baseApiManager.getApiService().resetPin(requestModel, requestModel.getToken());
    }

    public Observable<ResponseModel> ForgetPinReset(RequestModel requestModel) {
        return baseApiManager.getApiService().ForgetPinReset(requestModel, requestModel.getToken());
    }

    public Observable<ResponseModel> getCard(String token) {
        return baseApiManager.getApiService().getCard(token);
    }

    public Observable<ResponseModel> getUser(String token) {
        return baseApiManager.getApiService().getUserAccount(token);
    }

    public Observable<ResponseModel> verifyPan(RequestModel requestModel) {
        return baseApiManager.getApiService().verifyPan(requestModel, requestModel.getToken());
    }

    public Observable<ResponseModel> checkPanStatus(String token) {
        return baseApiManager.getApiService().checkPanStatus(token);
    }

    public Observable<ResponseModel> getTransactions(String token) {
        return baseApiManager.getApiService().getTransactions(token);
    }

    public Observable<ResponseModel> createKycAppoint(KycRequestModel requestModel) {
        Log.e("createKycAppoint", new Gson().toJson(requestModel));
        return baseApiManager.getApiService().createKycAppoint(requestModel, requestModel.getToken());
    }

    public Observable<ResponseModel> updateKycAppoint(KycRequestModel requestModel) {
        Log.e("updateKycAppoint", new Gson().toJson(requestModel));
        return baseApiManager.getApiService().updateKycAppoint(requestModel, requestModel.getToken());
    }

    public Observable<KycResponseModel> getKycAppoint(String token) {
        Log.e("getKycAppoint", new Gson().toJson(token));
        return baseApiManager.getApiService().getKycAppoint(token);
    }

    public Observable<ResponseModel> generateHash(PayUModel payuParams) {
        return baseApiManager.getApiService().generateHash(payuParams);
    }

    public Observable<ResponseModel> getLimitValues(String token) {
        return baseApiManager.getApiService().getUserAccount(token);
    }

    public Observable<ResponseModel> sendUserFeedBack(MultipartBody.Part filePart, RequestBody content, String token) {
        return baseApiManager.getApiService().sendUserFeedback(filePart, content, token);
    }

    public Observable<ResponseModel> getRepayment(String token) {
        return baseApiManager.getApiService().repayment(token);
    }

    public Observable<ChangePassResponseModel> changePassword(ChangePassResponseModel passResponseModel) {
        return baseApiManager.getApiService().changePassword(passResponseModel, passResponseModel.getToken());
    }


    public Observable<ResponseModel> getFlow(String phoneNumber) {
        return baseApiManager.getApiService().getFlow(phoneNumber);
    }


    public Observable<ResponseModel> resendEmail(String token) {
        return baseApiManager.getApiService().resendEmail(token);
    }

    public Observable<ResponseModel> getPreUserInfo(String token) {
        return baseApiManager.getApiService().getPreUserInfo(token);
    }

    public Observable<ResponseModel> getCardActiveStatus(String token) {
        return baseApiManager.getApiService().getCardActiveStatus(token);
    }

    public Observable<ResponseModel> forgetPassword(String token) {
        return baseApiManager.getApiService().forgetPassword(token);
    }


    public Observable<ResponseModel> LoginUser(RequestModel requestModel) {
        Log.e("Login", new Gson().toJson(requestModel));
        return baseApiManager.getApiService().Login(requestModel);
    }

    public Observable<UpdateModel> checkForUpdate(int versionCode) {
        return baseApiManager.getApiService().checkForUpdate(versionCode);
    }

    public Observable<ResponseModel> fetchRepaymentDisplayData() {
        return baseApiManager.getApiService().checkRepaymentData();
    }

    public Observable<ResponseModel> getHelp() {
        return baseApiManager.getApiService().getHelp();
    }

    public Observable<List<UserAccDetail>> getBhramAccountDetails(String url) {
        return baseApiManager.getApiService().getBhramAccountDetails(url);
    }

    public Observable<UserPersonalDetails> getBhramUserDetails(String url) {
        return baseApiManager.getApiService().getBhramUserDetails(url);
    }
}



