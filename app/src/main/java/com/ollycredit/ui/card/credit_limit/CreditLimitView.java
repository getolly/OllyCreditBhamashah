package com.ollycredit.ui.card.credit_limit;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.MVPView;

public interface CreditLimitView extends MVPView {

    void reqSucces(ResponseModel responseModel);

    void reqFail(int failedCode, String message);


}
