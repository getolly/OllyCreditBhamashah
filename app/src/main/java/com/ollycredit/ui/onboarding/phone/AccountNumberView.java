package com.ollycredit.ui.onboarding.phone;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.api.model.bahama.UserAccDetail;
import com.ollycredit.api.model.bahama.UserPersonalDetails;
import com.ollycredit.base.MVPView;

import java.util.List;

/**
 * Created by ch8n on 2/12/17.
 */

public interface AccountNumberView extends MVPView{

    void getBramAccountDetails(List<UserAccDetail> bramAccDetailsModel);
    void getBramUserDetails(UserPersonalDetails bramAccDetailsModel);
    void getComplRegSuccess(ResponseModel responseModel);

    void PersonalDetails(ResponseModel responseModel);

    void ProffesionalDetails(ResponseModel responseModel);

    void DocumentStored(ResponseModel responseModel);
}

