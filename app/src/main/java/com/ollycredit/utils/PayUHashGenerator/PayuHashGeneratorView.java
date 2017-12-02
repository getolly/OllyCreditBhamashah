package com.ollycredit.utils.PayUHashGenerator;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.MVPView;

/**
 * Created by ch8n on 14/11/17.
 */

public interface PayuHashGeneratorView extends MVPView {

    void getHashSuccess(ResponseModel responseModel);

    void getReqFailed(int errorCode, String msg);
}
