package com.ollycredit.base;

/**
 * Created by ch8n on 3/5/17.
 */

public interface Presenter<V extends MVPView> {

    void attachView(V mvpView);

    void detachView();
}
