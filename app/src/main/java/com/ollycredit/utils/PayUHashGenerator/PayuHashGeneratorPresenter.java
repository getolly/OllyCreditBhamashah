package com.ollycredit.utils.PayUHashGenerator;

import android.util.Log;

import com.ollycredit.api.model.PayUModel;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.api.remote.DataManager;
import com.ollycredit.base.BasePresenter;
import com.ollycredit.ui.repayments.RepaymentActivity;
import com.ollycredit.utils.GlobalConstants;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ch8n on 14/11/17.
 */

public class PayuHashGeneratorPresenter extends BasePresenter<PayuHashGeneratorView> {

    private DataManager dataManager;
    private Subscription mSubscription;

    @Inject
    public PayuHashGeneratorPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(PayuHashGeneratorView mMvpView) {
        super.attachView(mMvpView);
    }


    @Override
    public void detachView() {
        super.detachView();
    }

    public void getHash(final PayUModel payuParams) {
        dataManager.generateHash(payuParams)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(RepaymentActivity.class.getSimpleName(), e.toString());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        if (responseModel.getStatus().equalsIgnoreCase(GlobalConstants.
                                NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().getHashSuccess(responseModel);
                        } else {
                            getmMvpView().getReqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED,
                                    responseModel.getMessage());
                        }
                    }
                });
    }

}
