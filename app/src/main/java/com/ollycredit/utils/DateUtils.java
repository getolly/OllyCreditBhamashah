package com.ollycredit.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dilpreet on 07/11/17.
 */

public class DateUtils {

    public static String convertDatePattern(String dateString, String format) {
        SimpleDateFormat pickerFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat finalFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        Date date = null;
        try {
            date = pickerFormat.parse(dateString);
        } catch (ParseException e) {
            Log.d(DateUtils.class.getSimpleName(), e.getLocalizedMessage());
        }
        return finalFormat.format(date);
    }
}
