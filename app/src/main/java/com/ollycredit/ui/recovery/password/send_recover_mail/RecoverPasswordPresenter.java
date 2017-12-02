package com.ollycredit.ui.recovery.password.send_recover_mail;


import android.util.Log;

import com.google.gson.Gson;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.api.remote.DataManager;
import com.ollycredit.base.BasePresenter;
import com.ollycredit.utils.GlobalConstants;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RecoverPasswordPresenter extends BasePresenter<RecoverPasswordView> {


    private DataManager dataManager;
    private Subscription mSubscription;

    @Inject
    public RecoverPasswordPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(RecoverPasswordView mMvpView) {
        super.attachView(mMvpView);
    }


    @Override
    public void detachView() {
        super.detachView();
    }

    public void sendRecoverMail(String token) {

        mSubscription = dataManager.forgetPassword(token)
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
                        Log.e("forgot email", new Gson().toJson(responseModel));

                        if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_FAILED)) {

                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_LOGIN_USER, responseModel.getMessage());

                        } else if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {

                            getmMvpView().reqSuccess(responseModel);

                        } else {

                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, responseModel.getMessage());

                        }

                    }
                });
    }

}