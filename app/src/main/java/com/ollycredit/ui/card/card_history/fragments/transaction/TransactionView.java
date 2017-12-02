package com.ollycredit.ui.card.card_history.fragments.transaction;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.MVPView;

public interface TransactionView extends MVPView{

    void getTranSuccess(ResponseModel responseModel);

    void getReqFailed(int failedCode, String message);

    void getRepaymentSuccess(ResponseModel responseModel);

}
