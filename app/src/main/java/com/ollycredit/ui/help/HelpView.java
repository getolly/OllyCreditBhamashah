package com.ollycredit.ui.help;

import com.ollycredit.api.model.ResponseModel;
import com.ollycredit.base.MVPView;

/**
 * Created by dilpreet on 24/10/17.
 */

public interface HelpView extends MVPView {

    void helpDetails(ResponseModel responseModel);

    void reqFailed(int code, String msg);
}
