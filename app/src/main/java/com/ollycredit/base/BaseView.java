package com.ollycredit.base;

import com.ollycredit.api.model.ResponseModel;

/**
 * Created by almal on 2017-08-16.
 */

public interface BaseView extends MVPView {

    void sendFeedbackSuccess(ResponseModel responseModel);
    void sendFeedbackFailure(int failedCode,String message);
}
