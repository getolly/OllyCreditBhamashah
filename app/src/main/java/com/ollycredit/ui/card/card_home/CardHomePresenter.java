package com.ollycredit.ui.card.card_home;


import android.util.Log;

import com.google.gson.Gson;
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

public class CardHomePresenter extends BasePresenter<CardHomeView> {


    private DataManager dataManager;
    private Subscription mSubscription;

    @Inject
    public CardHomePresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(CardHomeView mMvpView) {
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
                        getmMvpView().getCardFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {


                        if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_FAILED)) {

                            getmMvpView().getCardFailed(GlobalConstants.NETWORK_REQUEST_LOGIN_USER, responseModel.getMessage());

                        } else if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {

                            getmMvpView().emailVerifyReq(responseModel);

                        } else {

                            getmMvpView().getCardFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, responseModel.getMessage());

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
                        getmMvpView().getCardFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, e.getMessage());
                        Log.e("Reypay_error", e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel ResponseModel) {

                        Log.e("Reypay", new Gson().toJson(ResponseModel));

                        if (ResponseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_FAILED)) {
                            getmMvpView().getCardFailed(GlobalConstants.NETWORK_REQUEST_VERIFY_OTP, ResponseModel.getMessage());
                        } else if (ResponseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().getRepaymentSuccess(ResponseModel);
                        } else {
                            getmMvpView().getCardFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, ResponseModel.getMessage());
                        }
                    }
                });
    }


    public void getCard(final String token) {

        mSubscription = dataManager.getCard(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getmMvpView().getCardFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, e.getMessage());
                        Log.d("mytag", e.toString());
                    }

                    @Override
                    public void onNext(ResponseModel cardResponseModel) {
                        //getmMvpView().isUserExisting(false, "Welcome new user!" , responseModel);
                        Log.e("get card", new Gson().toJson(cardResponseModel));

                        if (cardResponseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_FAILED)) {
                            getmMvpView().getCardFailed(GlobalConstants.NETWORK_REQUEST_VERIFY_OTP, cardResponseModel.getMessage());
                        } else if (cardResponseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().getCardSuccess(cardResponseModel);
                        } else {
                            getmMvpView().getCardFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, cardResponseModel.getMessage());
                        }
                    }
                });
    }

}