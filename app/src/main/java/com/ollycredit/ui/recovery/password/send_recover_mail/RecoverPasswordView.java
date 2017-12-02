package com.ollycredit.ui.recovery.password.send_recover_mail;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.MVPView;

public interface RecoverPasswordView extends MVPView{


    void reqFailed(int failedCode, String message);

    void reqSuccess(ResponseModel responseModel);


}
