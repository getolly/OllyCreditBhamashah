package com.ollycredit.utils;

/**
 * Created by ch8n on 3/8/17.
 */

public interface DialogInteractor {

    interface OtpDialogInteractor {

        void sendOtp(String otp);

        void sendPhoneNo(String phone);

        void otpIncorrect();

        void otpcorrect();

        void btnOtpReset();


    }



    interface ErrorDialogInteractor {

        void promptErrorSomthing();

        void promptErrorConnection();
    }


}
