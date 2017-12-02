package com.ollycredit.ui.card.verify.debit.debit_register;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.MVPView;

public interface VerifyBankView extends MVPView{


    void veriBankFailed(int failedCode, String message);

    void getUserSuccess(ResponseModel responseModel);

    void getHashSuccess(ResponseModel responseModel);

}
