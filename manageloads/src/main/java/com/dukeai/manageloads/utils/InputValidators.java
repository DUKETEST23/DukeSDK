package com.dukeai.manageloads.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidators {


    public static Boolean validateInput(String type, String value) {
        String regEx = null;
        switch (type.toLowerCase()) {
            case AppConstants.StringConstants.EMAIL:
                regEx = AppConstants.RegularExpressionConstants.EMAIL_REGEXP;
                break;
            case AppConstants.StringConstants.PASSWORD:
                regEx = AppConstants.RegularExpressionConstants.PASSWORD_REGEXP;
                break;
            case AppConstants.StringConstants.PHONE:
                regEx = AppConstants.RegularExpressionConstants.PHONE_REGEXP;
                break;
            case AppConstants.StringConstants.USERNAME:
                regEx = AppConstants.RegularExpressionConstants.USERNAME_REGEXP;
                break;
            case AppConstants.StringConstants.CODE:
                regEx = AppConstants.RegularExpressionConstants.VERIFICATION_CODE;
                break;
            case AppConstants.StringConstants.OLD_PASSWORD:
                regEx = AppConstants.RegularExpressionConstants.PASSWORD_REGEXP;
                break;
            case AppConstants.StringConstants.NEW_PASSWORD:
                regEx = AppConstants.RegularExpressionConstants.PASSWORD_REGEXP;
                break;
            case AppConstants.StringConstants.DECIMAL_NUM:
                regEx = AppConstants.RegularExpressionConstants.DECIMAL_REGEXP;
                break;
            case AppConstants.StringConstants.PROMO_CODE:
                if (value.isEmpty()) {
                    return true;
                } else {
                    regEx = AppConstants.RegularExpressionConstants.REFERRAL_ID_REGEXP;
                }
                break;
            case AppConstants.StringConstants.ALPHA_NUMERIC:
                regEx = AppConstants.RegularExpressionConstants.ALPHA_NUMERIC;
                break;
            case AppConstants.StringConstants.NON_EMPTY:
                regEx = AppConstants.RegularExpressionConstants.NON_EMPTY;
                break;
            case AppConstants.StringConstants.DESC:
                regEx = AppConstants.RegularExpressionConstants.DESC;
                break;
            case AppConstants.StringConstants.DATE_NON_EMPTY:
                regEx = AppConstants.RegularExpressionConstants.DATE_NON_EMPTY;
                break;
            case AppConstants.StringConstants.DESC_EMPTY:
                return !value.trim().isEmpty();
            case AppConstants.StringConstants.COMPANY_NAME:
                regEx = AppConstants.RegularExpressionConstants.USERNAME_REGEXP;
                break;
            case AppConstants.StringConstants.ADDRESS:
                if (value.isEmpty()) {
                    return true;
                }
                break;
            case AppConstants.StringConstants.RECIPIENT_PHONE:
                if (value.isEmpty()) {
                    return true;
                } else {
                    regEx = AppConstants.RegularExpressionConstants.NON_EMPTY;
                }
                break;

        }
        return checkPattern(value, regEx);
    }

    private static boolean checkPattern(String value, String regEx) {
        Pattern digitPattern;
        Matcher digitMatcher;

        digitPattern = Pattern.compile(regEx);
        digitMatcher = digitPattern.matcher(value);

        return (digitMatcher.matches());
    }
}