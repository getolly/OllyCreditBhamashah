package com.ollycredit.ui.onboarding.login.pin;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.MVPView;

public interface LoginPinView extends MVPView{

    void loginReqSuccess(ResponseModel responseModel);

    void loginReqFailed(int failedCode, String message);

    void pinReqSuccess(ResponseModel responseModel);

    void pinReqFailed(int failedCode, String message);

    void getUserSuccess(ResponseModel responseModel);

    void showUpdateDialog(String msg);
}
