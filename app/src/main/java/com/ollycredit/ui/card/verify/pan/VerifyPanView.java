package com.ollycredit.ui.card.verify.pan;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.MVPView;

public interface VerifyPanView extends MVPView{

    void panVerifyReq(ResponseModel responseModel);

    void reqFailed(int failedCode, String message);

    void userInfoReq(ResponseModel responseModel);

}
