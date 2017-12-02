package com.ollycredit.ui.onboarding.login.password;


import com.ollycredit.api.remote.DataManager;
import com.ollycredit.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;

public class LoginPasswordPresenter extends BasePresenter<LoginPaswordView> {


    private DataManager dataManager;
    private Subscription mSubscription;

    @Inject
    public LoginPasswordPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(LoginPaswordView mMvpView) {
        super.attachView(mMvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }


    

}