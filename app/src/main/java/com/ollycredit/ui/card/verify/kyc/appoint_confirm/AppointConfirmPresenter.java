package com.ollycredit.ui.card.verify.kyc.appoint_confirm;


import com.ollycredit.api.remote.DataManager;
import com.ollycredit.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;

public class AppointConfirmPresenter extends BasePresenter<AppointConfirmView> {


    private DataManager dataManager;
    private Subscription mSubscription;

    @Inject
    public AppointConfirmPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(AppointConfirmView mMvpView) {
        super.attachView(mMvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }




}