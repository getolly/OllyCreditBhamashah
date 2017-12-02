package com.ollycredit.ui.onboarding.login.pin;


import android.util.Log;

import com.google.gson.Gson;
import com.ollycredit.api.model.RequestModel;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.api.model.response.UpdateModel;
import com.ollycredit.api.remote.DataManager;
import com.ollycredit.base.BasePresenter;
import com.ollycredit.utils.GlobalConstants;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginPinPresenter extends BasePresenter<LoginPinView> {


    private DataManager dataManager;
    private Subscription mSubscription;

    @Inject
    public LoginPinPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(LoginPinView mMvpView) {
        super.attachView(mMvpView);
    }


    @Override
    public void detachView() {
        super.detachView();
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
                        Log.e("login: old user", new Gson().toJson(responseModel));
                        //getmMvpView().isUserExisting(false, "Welcome new user!" , responseModel);
                        getmMvpView().loginReqSuccess(responseModel);
                    }
                });
    }

    public void checkPin(final RequestModel requestModel) {

        mSubscription = dataManager.checkLoginPin(requestModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //getmMvpView().isUserExisting(true, e.getMessage() , null);
                        getmMvpView().pinReqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        //getmMvpView().isUserExisting(false, "Welcome new user!" , responseModel);

                        Log.e("login: check pin", new Gson().toJson(responseModel));

                        if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_FAILED)) {
                            getmMvpView().pinReqFailed(GlobalConstants.NETWORK_REQUEST_VERIFY_OTP, responseModel.getMessage());
                        } else if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().pinReqSuccess(responseModel);
                        } else {
                            getmMvpView().pinReqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, responseModel.getMessage());
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
                        getmMvpView().pinReqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {

                        Log.e("login: Userinfo", new Gson().toJson(responseModel));


                        if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_FAILED)) {

                            getmMvpView().pinReqFailed(GlobalConstants.NETWORK_REQUEST_LOGIN_USER, responseModel.getMessage());

                        } else if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {

                            getmMvpView().getUserSuccess(responseModel);

                        } else {

                            getmMvpView().pinReqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, responseModel.getMessage());

                        }

                    }
                });
    }

    public void checkForUpdate(int versionCode) {
        mSubscription = dataManager.checkForUpdate(versionCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<UpdateModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LoginPinPresenter.class.getSimpleName(), e.toString());
                    }

                    @Override
                    public void onNext(UpdateModel updateModel) {
                        if (updateModel.getStatus().equalsIgnoreCase(GlobalConstants.
                                NETWORK_RESPONSE_FAILED)) {
                            getmMvpView().showUpdateDialog(updateModel.getMessage());
                        }
                    }
                });
    }

}