package com.ollycredit.ui.onboarding.signup.create_pin;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.MVPView;

public interface CreatePinView extends MVPView{

    void reqSuccess(ResponseModel responseModel);

    void reqFailed(int failedCode, String message);

    void getUserSuccess(ResponseModel responseModel);

}
