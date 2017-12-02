package com.ollycredit.ui.onboarding.signup.user_details.step_one;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.MVPView;

public interface UserRegistrationView extends MVPView{


    void reqFailed(int failedCode, String message);

    void reqPanSuccess(ResponseModel responseModel);

    void reqPanAlreadyVerified(ResponseModel responseModel);

    void reqUserInfoSuccess(ResponseModel responseModel);

    void reqCompleteSuccess(ResponseModel responseModel);

    void reqAutoFillSuccess(ResponseModel responseModel);
}
