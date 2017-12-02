package com.ollycredit.ui.help;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.api.remote.DataManager;
import com.ollycredit.base.BasePresenter;
import com.ollycredit.utils.GlobalConstants;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dilpreet on 24/10/17.
 */

public class HelpPresenter extends BasePresenter<HelpView> {

    private DataManager dataManager;
    private Subscription mSubscription;

    @Inject
    public HelpPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(HelpView mMvpView) {
        super.attachView(mMvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void fetchHelp() {
        mSubscription = dataManager.getHelp()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants.
                                NETWORK_RESPONSE_FAILED)) {

                            getmMvpView().reqFailed(GlobalConstants.NETWORK_REQUEST_LOGIN_USER,
                                    responseModel.getMessage());

                        } else if (responseModel.getStatusMessage().equalsIgnoreCase(GlobalConstants
                                .NETWORK_RESPONSE_SUCCESS)) {

                            getmMvpView().helpDetails(responseModel);

                        } else {

                            getmMvpView().reqFailed(GlobalConstants.HELP_FETCH_FAILED,
                                    responseModel.getMessage());

                        }
                    }
                });
    }
}
