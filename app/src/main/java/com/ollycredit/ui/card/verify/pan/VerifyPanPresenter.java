package com.ollycredit.ui.card.verify.pan;


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

public class VerifyPanPresenter extends BasePresenter<VerifyPanView> {


    private DataManager dataManager;
    private Subscription mSubscription;

    @Inject
    public VerifyPanPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(VerifyPanView mMvpView) {
        super.attachView(mMvpView);
    }


    @Override
    public void detachView() {
        super.detachView();
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
                    public void onNext(ResponseModel responseModel) {
                        Log.e("verify pan response", new Gson().toJson(responseModel));

                        if (responseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_FAILED)) {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, responseModel.getMessage());
                        }
                        else if (responseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().panVerifyReq(responseModel);
                        }
                        else {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_VERIFY_PAN, responseModel.getMessage());
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
                        getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        //getmMvpView().isUserExisting(false, "Welcome new user!" , responseModel);
                        Log.e("userinfo response", new Gson().toJson(responseModel));

                        if (responseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_FAILED)) {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, responseModel.getMessage());
                        }
                        else if (responseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().userInfoReq(responseModel);
                        }
                        else {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_VERIFY_USER_PAN, responseModel.getMessage());
                        }

                    }
                });
    }



}