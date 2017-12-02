package com.ollycredit.ui.card.card_history.fragments.transaction;


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

public class TransactionPresenter extends BasePresenter<TransactionView> {


    private DataManager dataManager;
    private Subscription mSubscription;

    @Inject
    public TransactionPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(TransactionView mMvpView) {
        super.attachView(mMvpView);
    }


    @Override
    public void detachView() {
        super.detachView();
    }


    public void getTransactions(final String token) {

        mSubscription = dataManager.getTransactions(token)
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
                    }

                    @Override
                    public void onNext(ResponseModel cardResponseModel) {
                        //getmMvpView().isUserExisting(false, "Welcome new user!" , responseModel);
                        Log.e("trans", new Gson().toJson(cardResponseModel));

                        if (cardResponseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().getTranSuccess(cardResponseModel);
                        } else {
                            getmMvpView().getReqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, cardResponseModel.getMessage());
                        }
                    }
                });
    }

    public void getrepayment(final String token) {


        mSubscription = dataManager.getRepayment(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {
                        Log.e("Reypay", "complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        getmMvpView().getReqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, e.getMessage());
                        Log.e("Reypay_error", e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel ResponseModel) {

                        Log.e("Reypay", new Gson().toJson(ResponseModel));

                        if (ResponseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_FAILED)) {
                            getmMvpView().getReqFailed(GlobalConstants.NETWORK_REQUEST_VERIFY_OTP, ResponseModel.getMessage());
                        } else if (ResponseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().getRepaymentSuccess(ResponseModel);
                        } else {
                            getmMvpView().getReqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, ResponseModel.getMessage());
                        }
                    }
                });
    }


}