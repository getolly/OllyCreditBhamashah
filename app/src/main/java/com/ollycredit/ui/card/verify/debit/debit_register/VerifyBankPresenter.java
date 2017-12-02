package com.ollycredit.ui.card.verify.debit.debit_register;


import android.util.Log;

import com.google.gson.Gson;
import com.ollycredit.api.model.PayUModel;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.api.remote.DataManager;
import com.ollycredit.base.BasePresenter;
import com.ollycredit.utils.GlobalConstants;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VerifyBankPresenter extends BasePresenter<VerifyBankView> {


    private DataManager dataManager;
    private Subscription mSubscription;

    @Inject
    public VerifyBankPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(VerifyBankView mMvpView) {
        super.attachView(mMvpView);
    }


    @Override
    public void detachView() {
        super.detachView();
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
                        Log.d("mytag", e.toString());
                        getmMvpView().veriBankFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        //getmMvpView().isUserExisting(false, "Welcome new user!" , responseModel);
                        getmMvpView().getUserSuccess(responseModel);
                    }
                });
    }

    public void getHash(final PayUModel payuParams) {

        mSubscription = dataManager.generateHash(payuParams)
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
                        //getmMvpView().isUserExisting(false, "Welcome new user!" , responseModel);
                        Log.e("trans", new Gson().toJson(responseModel));

                        if (responseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().getHashSuccess(responseModel);
                        } else {
                            getmMvpView().veriBankFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, responseModel.getMessage());
                        }
                    }
                });
    }



}