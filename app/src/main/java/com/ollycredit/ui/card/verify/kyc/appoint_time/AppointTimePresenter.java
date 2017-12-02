package com.ollycredit.ui.card.verify.kyc.appoint_time;


import com.ollycredit.api.remote.DataManager;
import com.ollycredit.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;

public class AppointTimePresenter extends BasePresenter<AppointTimeView> {


    private DataManager dataManager;
    private Subscription mSubscription;

    @Inject
    public AppointTimePresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(AppointTimeView mMvpView) {
        super.attachView(mMvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }




}