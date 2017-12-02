package com.ollycredit.ui.onboarding.phone;


import android.util.Log;

import com.ollycredit.api.model.RequestModel;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.api.model.bahama.UserAccDetail;
import com.ollycredit.api.model.bahama.UserPersonalDetails;
import com.ollycredit.api.remote.DataManager;
import com.ollycredit.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AccountNumberPresenter extends BasePresenter<AccountNumberView> {


    private DataManager dataManager;
    private Subscription mSubscription;

    @Inject
    public AccountNumberPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(AccountNumberView mMvpView) {
        super.attachView(mMvpView);
    }


    @Override
    public void detachView() {
        super.detachView();
    }


    public void getBramAccountDetails(final String url) {

        mSubscription = dataManager.getBhramAccountDetails(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<UserAccDetail>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError:Acc ", e.getMessage());
                    }

                    @Override
                    public void onNext(List<UserAccDetail> responseModel) {
                        getmMvpView().getBramAccountDetails(responseModel);
                    }
                });
    }

    public void getBramUserDetails(final String url) {

        mSubscription = dataManager.getBhramUserDetails(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<UserPersonalDetails>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError:User ", e.getMessage());
                    }

                    @Override
                    public void onNext(UserPersonalDetails responseModel) {
                        getmMvpView().getBramUserDetails(responseModel);
                    }
                });
    }


    public void getRegisterCompleted(RequestModel requestModel) {

        mSubscription = dataManager.completeRegistration(requestModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError:Reg ", e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        getmMvpView().getComplRegSuccess(responseModel);
                    }
                });
    }


    public void getPersonalDetails(RequestModel requestModel) {

        mSubscription = dataManager.getPersonalDetails(requestModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError:pers ", e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        getmMvpView().PersonalDetails(responseModel);
                    }
                });
    }


    public void getProffesionalDetails(RequestModel requestModel) {

        mSubscription = dataManager.getProffesionalDetails(requestModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError:proff ", e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        getmMvpView().ProffesionalDetails(responseModel);
                    }
                });
    }


    public void getDocumentStored(RequestModel requestModel) {

        mSubscription = dataManager.getDocumentStored(requestModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError:doc ", e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        getmMvpView().DocumentStored(responseModel);
                    }
                });
    }
}