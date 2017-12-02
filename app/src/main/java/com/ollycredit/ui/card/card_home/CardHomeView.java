package com.ollycredit.ui.card.card_home;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.MVPView;

public interface CardHomeView extends MVPView{

    void getCardSuccess(ResponseModel responseModel);

    void emailVerifyReq(ResponseModel responseModel);

    void getCardFailed(int failedCode, String message);

    void getRepaymentSuccess(ResponseModel responseModel);

}
