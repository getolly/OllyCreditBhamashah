package com.ollycredit.ui.onboarding.phone;


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

public class PhonePresenter extends BasePresenter<PhoneView> {


    private DataManager dataManager;
    private Subscription mSubscription;

    @Inject
    public PhonePresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(PhoneView mMvpView) {
        super.attachView(mMvpView);
    }


    @Override
    public void detachView() {
        super.detachView();
    }


    public void getFlow(final String phoneNumber) {

        mSubscription = dataManager.getFlow(phoneNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED,
                                e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.
                                NETWORK_RESPONSE_FAILED)) {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED,
                                    responseModel.getMessage());
                        } else if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants
                                .NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().reqGetFlowSuccess(responseModel);
                        }

                    }
                });

    }

    public void registerUser(final RequestModel requestModel) {

        mSubscription = dataManager.registerUser(requestModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED,
                                e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.
                                NETWORK_RESPONSE_FAILED)) {
                            if (responseModel.getStatusMessage().toLowerCase().contains("token")) {
                                getmMvpView().reqFailed(GlobalConstants.NETWORK_TOKEN_EXPIRE,
                                        responseModel.getMessage());
                            } else {
                                getmMvpView().reqRegisterUserSuccess(responseModel);
                            }
                        } else if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants
                                .NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().reqRegisterUserSuccess(responseModel);
                        } else {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED,
                                    responseModel.getMessage());
                        }

                    }
                });

    }


    public void loginUser(final RequestModel requestModel) {

        mSubscription = dataManager.LoginUser(requestModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED,
                                e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.
                                NETWORK_RESPONSE_FAILED)) {

                            getmMvpView().reqFailed(GlobalConstants.USER_NOT_EXIST, responseModel.
                                    getMessage());

                        } else if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants
                                .NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().reqLoginSuccess(responseModel);
                        } else {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED,
                                    responseModel.getMessage());
                        }

                    }
                });
    }


    public void verifyPhoneOTPForRegister(final RequestModel requestModel) {

        mSubscription = dataManager.verifyPhoneRegister(requestModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED,
                                e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.
                                NETWORK_RESPONSE_FAILED)) {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_VERIFY_OTP,
                                    responseModel.getMessage());
                        } else if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants
                                .NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().reqOTPVerificationSuccess(responseModel);
                        } else {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED,
                                    responseModel.getMessage());
                        }
                    }
                });

    }


    public void verifyPhoneOTPForLogin(final RequestModel requestModel) {

        mSubscription = dataManager.verifyPhoneLogin(requestModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED,
                                e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.
                                NETWORK_RESPONSE_FAILED)) {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_VERIFY_OTP,
                                    responseModel.getMessage());
                        } else if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants
                                .NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().reqOTPVerificationSuccess(responseModel);
                        } else {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED,
                                    responseModel.getMessage());
                        }
                    }
                });

    }


    public void getCardActiveStatus(final String token) {

        mSubscription = dataManager.getCardActiveStatus(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED,
                                e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {

                        if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.
                                NETWORK_RESPONSE_FAILED)) {

                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED,
                                    responseModel.getMessage());

                        } else if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants
                                .NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().reqCardActiveSucess(responseModel);
                        } else {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED,
                                    responseModel.getMessage());
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
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        getmMvpView().loginReqSuccess(responseModel);
                    }
                });
    }


}