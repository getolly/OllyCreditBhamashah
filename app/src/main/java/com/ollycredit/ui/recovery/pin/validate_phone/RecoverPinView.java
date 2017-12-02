package com.ollycredit.ui.recovery.pin.validate_phone;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.MVPView;

public interface RecoverPinView extends MVPView {

    void ifNewUser(ResponseModel responseModel);

    void  reqFailed(int failedCode, String message);

    void loginReqSuccess(ResponseModel responseModel);

    void otpReqSuccess(ResponseModel responseModel);

    void getUserSuccess(ResponseModel responseModel);


}
