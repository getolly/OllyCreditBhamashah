package com.ollycredit.ui.recovery.pin.validate_phone;


import android.util.Log;

import com.google.gson.Gson;
import com.ollycredit.api.model.RequestModel;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.api.remote.DataManager;
import com.ollycredit.base.BasePresenter;
import com.ollycredit.utils.GlobalConstants;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RecoverPinPresenter extends BasePresenter<RecoverPinView> {


    private DataManager dataManager;
    private Subscription mSubscription;

    @Inject
    public RecoverPinPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(RecoverPinView mMvpView) {
        super.attachView(mMvpView);
    }


    @Override
    public void detachView() {
        super.detachView();
    }

    public void checkUser(final RequestModel requestModel) {

        mSubscription = dataManager.registerUser(requestModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        Log.e("Create user ", new Gson().toJson(responseModel));

                        if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_FAILED)) {
                            getmMvpView().ifNewUser(responseModel);

                        } else if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {

                            getmMvpView().ifNewUser(responseModel);

                        } else {

                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, responseModel.getMessage());
                        }

                    }
                });

    }

    public void loginOldUser(final RequestModel requestModel) {

        mSubscription = dataManager.login(requestModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //getmMvpView().isUserExisting(true, e.getMessage() , null);
                        getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_VERIFY_OTP, e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        //getmMvpView().isUserExisting(false, "Welcome new user!" , responseModel);

                        Log.e("login user ", new Gson().toJson(responseModel));
                        if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_FAILED)) {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_VERIFY_OTP, responseModel.getMessage());
                        } else if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().loginReqSuccess(responseModel);
                        } else {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, responseModel.getMessage());
                        }
                    }
                });
    }

    public void verifyOTP(final RequestModel requestModel) {

        mSubscription = dataManager.verifyOtpRecovery(requestModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        Log.e("verify c pin ", new Gson().toJson(responseModel));
                        if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_FAILED)) {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_VERIFY_OTP, responseModel.getMessage());
                        } else if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().otpReqSuccess(responseModel);
                        } else {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, responseModel.getMessage());
                        }
                    }
                });
    }

    public void getUserInfo(final String token) {

        mSubscription = dataManager.getUser(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //getmMvpView().isUserExisting(true, e.getMessage() , null);
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {

                        Log.e("recoverpin userinfo", new Gson().toJson(responseModel));

                        if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_FAILED)) {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_VERIFY_OTP, responseModel.getMessage());
                        } else if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().getUserSuccess(responseModel);
                        } else {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, responseModel.getMessage());
                        }

                        //getmMvpView().isUserExisting(false, "Welcome new user!" , responseModel);
//                        getmMvpView().getUserSuccess(responseModel);
                    }
                });
    }


}