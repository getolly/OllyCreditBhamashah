package com.ollycredit.ui.user_profile.change_password;

import com.ollycredit.api.model.ChangePassResponseModel;
import com.ollycredit.base.MVPView;

public interface ChangePasswordView extends MVPView{


    void reqFailed(int failedCode, String message);

    void reqSuccess(ChangePassResponseModel responseModel);

}
