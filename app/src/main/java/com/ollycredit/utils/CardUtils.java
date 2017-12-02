package com.ollycredit.utils;

import com.payu.india.Payu.PayuConstants;

/**
 * Created by dilpreet on 25/11/17.
 */

public class CardUtils {

    public static int getIssuerImage(String issuer) {

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            switch (issuer) {
                case PayuConstants.VISA:
                    return com.payu.payuui.R.drawable.logo_visa;
                case PayuConstants.LASER:
                    return com.payu.payuui.R.drawable.laser;
                case PayuConstants.DISCOVER:
                    return com.payu.payuui.R.drawable.discover;
                case PayuConstants.MAES:
                    return com.payu.payuui.R.drawable.mas_icon;
                case PayuConstants.MAST:
                    return com.payu.payuui.R.drawable.mc_icon;
                case PayuConstants.AMEX:
                    return com.payu.payuui.R.drawable.amex;
                case PayuConstants.DINR:
                    return com.payu.payuui.R.drawable.diner;
                case PayuConstants.JCB:
                    return com.payu.payuui.R.drawable.jcb;
                case PayuConstants.SMAE:
                    return com.payu.payuui.R.drawable.maestro;
                case PayuConstants.RUPAY:
                    return com.payu.payuui.R.drawable.rupay;
            }
            return 0;
        } else {

            switch (issuer) {
                case PayuConstants.VISA:
                    return com.payu.payuui.R.drawable.logo_visa;
                case PayuConstants.LASER:
                    return com.payu.payuui.R.drawable.laser;
                case PayuConstants.DISCOVER:
                    return com.payu.payuui.R.drawable.discover;
                case PayuConstants.MAES:
                    return com.payu.payuui.R.drawable.mas_icon;
                case PayuConstants.MAST:
                    return com.payu.payuui.R.drawable.mc_icon;
                case PayuConstants.AMEX:
                    return com.payu.payuui.R.drawable.amex;
                case PayuConstants.DINR:
                    return com.payu.payuui.R.drawable.diner;
                case PayuConstants.JCB:
                    return com.payu.payuui.R.drawable.jcb;
                case PayuConstants.SMAE:
                    return com.payu.payuui.R.drawable.maestro;
                case PayuConstants.RUPAY:
                    return com.payu.payuui.R.drawable.rupay;
            }
            return 0;
        }
    }


}
