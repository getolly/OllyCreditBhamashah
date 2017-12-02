package com.ollycredit.ui.card.verify.kyc.appoint_address;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.MVPView;

public interface AppointAddressView extends MVPView {

    void reqSuccess(ResponseModel responseModel);

    void reqFailed(int failedCode, String message);


    //void setUserInfo(RequestModel requestModel);


}
