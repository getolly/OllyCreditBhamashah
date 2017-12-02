package com.ollycredit.ui.card.card_history.fragments.repayments;


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

public class RepaymentPresenter extends BasePresenter<RepaymentView> {


    private DataManager dataManager;
    private Subscription mSubscription;

    @Inject
    public RepaymentPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(RepaymentView mMvpView) {
        super.attachView(mMvpView);
    }


    @Override
    public void detachView() {
        super.detachView();
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
                        //getmMvpView().getReypaymentFailed(GlobalUtils.NETWORK_REQUEST_CALL_FAILED, e.getMessage());
                        Log.e("Reypay_error",e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel ResponseModel) {

                        if (ResponseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_FAILED)) {
                           getmMvpView().getRepaymentFailed(GlobalConstants.NETWORK_REQUEST_VERIFY_OTP, ResponseModel.getMessage());
                        } else if (ResponseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().getRepaymentSuccess(ResponseModel);
                        } else {
                            //getmMvpView().getRepaymentFailed(GlobalUtils.NETWORK_REQUEST_CALL_FAILED, ResponseModel.getMessage());
                        }
                    }
                });
    }

    public void getRepaymentDisplayData() {
        mSubscription = dataManager.fetchRepaymentDisplayData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Reypay_error",e.getMessage());

                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        getmMvpView().getRepaymentDataSuccess(responseModel);
                    }
                });
    }

}