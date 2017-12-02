package com.ollycredit.ui.card.card_history.fragments.repayments;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.MVPView;

public interface RepaymentView extends MVPView{

    void getReqFailed(int failedCode, String message);

    void getRepaymentSuccess(ResponseModel responseModel);

    void getRepaymentFailed(int failedCode, String message);

    void getRepaymentDataSuccess(ResponseModel responseModel);

}
