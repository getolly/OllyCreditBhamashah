package com.ollycredit.utils.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ch8n on 17/8/17.
 */

public class ValidationHelper {

    public boolean isNumberInString(String target) {

        Pattern pattern = Pattern.compile("\\d|\\W");
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }


}
