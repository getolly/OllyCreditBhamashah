package com.ollycredit.ui.card.verify.kyc.kyc_register;


import android.util.Log;

import com.google.gson.Gson;
import com.ollycredit.api.model.kyc.KycRequestModel;
import com.ollycredit.api.model.kyc.KycResponseModel;
import com.ollycredit.api.remote.DataManager;
import com.ollycredit.base.BasePresenter;
import com.ollycredit.utils.GlobalConstants;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AppointKycPresenter extends BasePresenter<AppointKycView> {


    private DataManager dataManager;
    private Subscription mSubscription;

    @Inject
    public AppointKycPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(AppointKycView mMvpView) {
        super.attachView(mMvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }


    public void getAppointInfo(final KycRequestModel requestModel) {

        Log.e("appointment :", new Gson().toJson(requestModel));


        mSubscription = dataManager.getKycAppoint(requestModel.getToken())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<KycResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, e.getMessage());
                    }

                    @Override
                    public void onNext(KycResponseModel responseModel) {

                        Log.e("create appoint", new Gson().toJson(responseModel));

                        if (responseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_FAILED)) {

                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_APPOINT_KYC, responseModel.getMessage());

                        } else if (responseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {

                            getmMvpView().reqSuccess(responseModel);

                        } else {

                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, responseModel.getMessage());

                        }

                    }
                });
    }


}