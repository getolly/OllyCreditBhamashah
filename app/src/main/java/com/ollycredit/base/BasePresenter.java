package com.ollycredit.base;

import com.ollycredit.api.remote.DataManager;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by ch8n on 3/5/17.
 */

public class BasePresenter<T extends MVPView> implements Presenter<T> {

    private T mMvpView;


    private DataManager dataManager;
    private Subscription mSubscription;


    public BasePresenter() {
    }

    @Inject
    public BasePresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(T mMvpView) {
        this.mMvpView = mMvpView;
    }

    @Override
    public void detachView() {
        this.mMvpView = null;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public T getmMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }


    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}
