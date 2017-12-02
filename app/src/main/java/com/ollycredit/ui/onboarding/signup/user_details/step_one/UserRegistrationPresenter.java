package com.ollycredit.ui.onboarding.signup.user_details.step_one;


import android.os.Handler;

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

public class UserRegistrationPresenter extends BasePresenter<UserRegistrationView> {


    private DataManager dataManager;
    private Subscription mSubscription;

    @Inject
    public UserRegistrationPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(UserRegistrationView mMvpView) {
        super.attachView(mMvpView);
    }


    @Override
    public void detachView() {
        super.detachView();
    }

    public void reqAutoFill(final String token) {

        mSubscription = dataManager.getPreUserInfo(token)
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
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CREATE_USER,
                                    responseModel.getMessage());
                        } else if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants
                                .NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().reqAutoFillSuccess(responseModel);
                        } else {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED,
                                    responseModel.getMessage());
                        }

                    }
                });
    }

    public void completeRegistration(final RequestModel requestModel) {

        mSubscription = dataManager.completeRegistration(requestModel)
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
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CREATE_USER,
                                    responseModel.getMessage());
                        } else if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants
                                .NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().reqCompleteSuccess(responseModel);
                        } else {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED,
                                    responseModel.getMessage());
                        }
                    }
                });
    }

    public void checkPanStatus(final String token, final boolean isFirstTimeCheck, final RequestModel requestModel) {
        mSubscription = dataManager.checkPanStatus(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        if (responseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_FAILED)) {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, responseModel.getMessage());
                        } else if (responseModel.getStatus().
                                equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {
                            if (responseModel.isPanVerified())
                                if (!isFirstTimeCheck)
                                    getmMvpView().reqPanSuccess(responseModel);
                                else
                                    getmMvpView().reqPanAlreadyVerified(responseModel);
                            else
                                verifPan(requestModel);
                        } else {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_VERIFY_PAN,
                                    responseModel.getMessage());
                        }
                    }
                });
    }


    public void verifPan(final RequestModel requestModel) {
        mSubscription = dataManager.verifyPan(requestModel)
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
                    public void onNext(final ResponseModel responseModel) {

                        if (responseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_FAILED)) {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, responseModel.getMessage());
                        } else if (responseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {
//                            getmMvpView().reqPanSuccess(responseModel);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    checkPanStatus(responseModel.getToken(), false, requestModel);
                                }
                            }, 3000);

                        } else {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_VERIFY_PAN, responseModel.getMessage());
                        }
                    }
                });
    }


}