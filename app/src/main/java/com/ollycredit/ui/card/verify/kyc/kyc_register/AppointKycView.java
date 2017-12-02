package com.ollycredit.ui.card.verify.kyc.kyc_register;

import com.ollycredit.api.model.kyc.KycResponseModel;
import com.ollycredit.base.MVPView;

public interface AppointKycView extends MVPView {

    void reqSuccess(KycResponseModel responseModel);

    void reqFailed(int failedCode, String message);



}
