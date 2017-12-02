package com.ollycredit.ui.user_profile.user_details;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.MVPView;

public interface UserProfileView extends MVPView{

    void getUserSuccess(ResponseModel responseModel);

    void reqFailed(int failedCode, String message);

    void resendEmail(ResponseModel responseModel);

}
