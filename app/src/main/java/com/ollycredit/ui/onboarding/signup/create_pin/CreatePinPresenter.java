package com.ollycredit.ui.onboarding.signup.create_pin;


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

public class CreatePinPresenter extends BasePresenter<CreatePinView> {


    private DataManager dataManager;
    private Subscription mSubscription;

    @Inject
    public CreatePinPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(CreatePinView mMvpView) {
        super.attachView(mMvpView);
    }


    @Override
    public void detachView() {
        super.detachView();
    }

    public void createPin(final RequestModel requestModel) {

        mSubscription = dataManager.choosePinForPreUser(requestModel)
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

                        if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_FAILED)) {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CREATE_USER, responseModel.getMessage());
                        } else if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().reqSuccess(responseModel);
                        } else {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED, responseModel.getMessage());
                        }

                    }
                });
    }

    public void changePin(final RequestModel requestModel) {

        mSubscription = dataManager.choosePinForFlowOperation(requestModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED,
                                e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {

                        if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.
                                NETWORK_RESPONSE_FAILED)) {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CREATE_USER,
                                    responseModel.getMessage());
                        } else if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants
                                .NETWORK_RESPONSE_SUCCESS)) {
                            getmMvpView().reqSuccess(responseModel);
                        } else {
                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_CALL_FAILED,
                                    responseModel.getMessage());
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
                        getmMvpView().getUserSuccess(responseModel);
                    }
                });
    }



}