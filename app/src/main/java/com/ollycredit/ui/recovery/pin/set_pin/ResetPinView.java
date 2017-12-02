package com.ollycredit.ui.recovery.pin.set_pin;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.MVPView;

public interface ResetPinView extends MVPView{

    void reqSuccess(ResponseModel responseModel);

    void reqFailed(int failedCode, String message);

    void getUserSuccess(ResponseModel responseModel);


}
