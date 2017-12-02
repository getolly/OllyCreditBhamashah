package com.ollycredit.base;

/**
 * Created by almal on 2017-08-16.
 */

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.api.remote.DataManager;
import com.ollycredit.utils.GlobalConstants;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ConcreteBasePresenter extends BasePresenter<BaseView> {


    private DataManager dataManager;
    private Subscription mSubscription;

    @Inject
    public ConcreteBasePresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(BaseView mBaseView) {
        super.attachView(mBaseView);
    }


    @Override
    public void detachView() {
        super.detachView();
    }


    public void sendUserFeedback(MultipartBody.Part filePart, RequestBody content, String token) {
        mSubscription = dataManager.sendUserFeedBack(filePart, content, token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getmMvpView().sendFeedbackFailure(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel cardResponseModel) {

                        if (cardResponseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_FAILED)) {
                            getmMvpView().sendFeedbackFailure(GlobalConstants.NETWORK_REQUEST_VERIFY_OTP, cardResponseModel.getMessage());
                        } else if (cardResponseModel.getStatus().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().sendFeedbackSuccess(cardResponseModel);
                        } else {
                            getmMvpView().sendFeedbackFailure(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, cardResponseModel.getMessage());
                        }
                    }
                });
    }


}