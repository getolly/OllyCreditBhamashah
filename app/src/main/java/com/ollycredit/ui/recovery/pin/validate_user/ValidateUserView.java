package com.ollycredit.ui.recovery.pin.validate_user;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.MVPView;

public interface ValidateUserView extends MVPView{

    void reqSuccess(ResponseModel responseModel);

    void reqFailed(int failedCode, String message);

}
