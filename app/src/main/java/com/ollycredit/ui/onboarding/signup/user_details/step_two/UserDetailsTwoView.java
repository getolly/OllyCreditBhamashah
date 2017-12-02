package com.ollycredit.ui.onboarding.signup.user_details.step_two;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.MVPView;

public interface UserDetailsTwoView extends MVPView{

    void reqSuccess(ResponseModel responseModel);

    void reqFailed(int failedCode, String message);

}
