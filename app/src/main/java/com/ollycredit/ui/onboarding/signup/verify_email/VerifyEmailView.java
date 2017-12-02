package com.ollycredit.ui.onboarding.signup.verify_email;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.MVPView;

public interface VerifyEmailView extends MVPView{


    void reqFailed(int failedCode, String message);

    void getUserSuccess(ResponseModel responseModel);

    void resendEmailSuccess(ResponseModel responseModel);

}
