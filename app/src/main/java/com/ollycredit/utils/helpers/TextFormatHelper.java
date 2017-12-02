package com.ollycredit.utils.helpers;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by ch8n on 21/8/17.
 */

public class TextFormatHelper {


    public static String indianRupeesFormat(Double amount) {
        String rupeeString = NumberFormat.getCurrencyInstance(new Locale("en", "IN")).format(amount).trim();
        int rupee = rupeeString.length();
        if (rupeeString.contains("Rs.")) {
            return rupeeString.substring(3, rupee - 3);
        } else {
            return rupeeString.substring(2, rupee - 3);
        }
    }
}
