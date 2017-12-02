package com.ollycredit.ui.recovery.pin.validate_password;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.MVPView;

public interface RecoverPasswordDetailsView extends MVPView {

    void reqSuccess(ResponseModel responseModel);

    void  reqFailed(int failedCode, String message);


}
