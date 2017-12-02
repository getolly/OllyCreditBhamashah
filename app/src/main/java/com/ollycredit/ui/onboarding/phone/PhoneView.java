package com.ollycredit.ui.onboarding.phone;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.MVPView;

public interface PhoneView extends MVPView {

    void reqRegisterUserSuccess(ResponseModel responseModel);

    void reqGetFlowSuccess(ResponseModel responseModel);

    void reqFailed(int failedCode, String message);

    void reqOTPVerificationSuccess(ResponseModel responseModel);

    void loginReqSuccess(ResponseModel responseModel);

    void reqCardActiveSucess(ResponseModel responseModel);

    void reqLoginSuccess(ResponseModel responseModel);

}
