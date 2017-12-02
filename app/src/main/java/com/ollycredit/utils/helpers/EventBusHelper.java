package com.ollycredit.utils.helpers;

import com.ollycredit.api.model.RequestModel;
import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.api.model.kyc.KycRequestModel;

/**
 * Created by ch8n on 18/7/17.
 */

public class EventBusHelper {

    private RequestModel requestModel;
    private ResponseModel responseModel;
    private KycRequestModel kycRequestModel;
    private int position;

    public EventBusHelper(RequestModel requestModel) {
        this.requestModel = requestModel;
    }

    public EventBusHelper(ResponseModel responseModel) {
        this.responseModel = responseModel;
    }

    public EventBusHelper(KycRequestModel kycRequestModel) {
        this.kycRequestModel = kycRequestModel;
    }

    public EventBusHelper(int position) {
        this.position = position;
    }

    public RequestModel getRequestModel() {
        return this.requestModel;
    }

    public ResponseModel getResponseModel() {
        return this.responseModel;
    }

    public KycRequestModel getKycRequestModel() {
        return this.kycRequestModel;
    }

    public int getViewPagerPosition() {
        return position;
    }

    public int getErrorFlag() {
        return position;
    }


}
