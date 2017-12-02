package com.ollycredit.ui.user_profile.change_password;


import android.util.Log;

import com.google.gson.Gson;
import com.ollycredit.api.model.ChangePassResponseModel;
import com.ollycredit.api.remote.DataManager;
import com.ollycredit.base.BasePresenter;
import com.ollycredit.utils.GlobalConstants;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChangePasswordPresenter extends BasePresenter<ChangePasswordView> {


    private DataManager dataManager;
    private Subscription mSubscription;

    @Inject
    public ChangePasswordPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(ChangePasswordView mMvpView) {
        super.attachView(mMvpView);
    }


    @Override
    public void detachView() {
        super.detachView();
    }

    public void changeUserPassword(ChangePassResponseModel passResponseModel) {

        mSubscription = dataManager.changePassword(passResponseModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ChangePassResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, e.getMessage());
                    }

                    @Override
                    public void onNext(ChangePassResponseModel responseModel) {

                        Log.e("Change Password:", new Gson().toJson(responseModel));


                        if (responseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_FAILED)) {

                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_LOGIN_USER, responseModel.getMessage());

                        } else if (responseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {

                            getmMvpView().reqSuccess(responseModel);

                        } else {

                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, responseModel.getMessage());
                        }

                    }

                });
    }


}