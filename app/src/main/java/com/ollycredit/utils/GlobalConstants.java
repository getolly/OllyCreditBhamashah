package com.ollycredit.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by ch8n on 25/7/17.
 */

public class GlobalConstants {


    //Tracker constants

    public final static String USER_APP_FLOW = "user_flow";
    public final static int APP_FLOW_PRE_USER = 0;

    public final static int APP_FLOW_OPERATIONAL = 1;
    public final static int APP_FLOW_CLASSIC = 2;
    public final static int APP_FLOW_SELECT = 3;
    public final static int APP_FLOW_PREPAID = 4;
    @IntDef({APP_FLOW_PRE_USER, APP_FLOW_OPERATIONAL, APP_FLOW_CLASSIC, APP_FLOW_SELECT, APP_FLOW_PREPAID})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AppFlow{}


    public final static String PREF_IS_CARD_ACTIVE = "isUserReturning";
    //category
    public final static String CATEGORY_REPAYMENT = "REPAYMENT";

    public final static String CATEGORY_TRANSACTION = "TRANSACTION";
    public final static String CATEGORY_INCREASE_LIMIT = "INCREASE_CREDIT_LIMIT";
    public final static String CATEGORY_OLLY_CARD = "OLLY_CREDIT_CARD";
    public final static String CATEGORY_CARD_HOME = "CARD_HOME";
    public final static String CATEGORY_DILAOG = "DIALOG_BOX";
    public final static String CATEGORY_CARD_HISTORY = "CARD_HISTORY";
    public final static String CATEGORY_NAV_DRAWER = "NAVIGATION_DRAWER";
    public final static String CATEGORY_ONBOARDING = "USER_ONBOARDING";
    public final static String CATEGORY_VERIFY_DOCUMENTS = "VERIFY_DOCUMENTS";
    public final static String CATEGORY_RECOVER_CREDENTIALS = "RECOVER_CREDENTIALS";
    public final static String CATEGORY_TERMS_CONDITIONS = "TERMS_CONDITIONS";
    public final static String CATEGORY_USER_PROFILE = "USER_PROFILE";

    //actions
    public final static String ACTION_GOTO = "GO_TO";
    public final static String ACTION_COPY_OLLY_CARD = "COPY_CARD_NUMBER";
    public final static String ACTION_DIALOG_OK = "DIALOG_OK";
    public final static String ACTION_DIALOG_CANCEL = "DIALOG_CANCEL";
    public final static String ACTION_BUTTON_CLICKED = "BUTTON_CLICKED";
    public final static String ACTION_CANCEL = "CANCEL";
    public final static String ACTION_ACCEPT = "ACCEPT";
    public final static String ACTION_NEXT = "NEXT";
    public final static String ACTION_RESEND_OTP = "RESEND_OTP";
    public final static String ACTION_PAY_NOW = "PAY_NOW";
    public final static String ACTION_EDIT_PHONE = "CHANGE_PHONE";
    public final static String ACTION_VERIFY = "VERIFY";
    public final static String ACTION_SWIPE = "SWIPE";
    public final static String ACTION_TOGGLE = "TOGGLE";
    public final static String ACTION_BACK = "BACK_TO";
    public final static String ACTION_RESET = "RESET";


    //lable
    public final static String LABLE_REPAYMENT = "REPAYMENT";
    public final static String LABLE_TO_REPAY_AMOUNT = "TO_REPAY_AMOUNT";
    public final static String LABLE_VERIFY_EMAIL = "VERIFY_EMAIL";
    public final static String LABLE_TRANSACTION = "TRANSACTION";
    public final static String LABLE_INCREASE_LIMIT = "INCREASE_CREDIT_LIMIT";
    public final static String LABLE_OLLY_CARD = "OLLY_CREDIT_CARD";
    public final static String LABLE_ERROR = "DIALOG_ERROR";
    public final static String LABLE_INFO_PROMPT = "DIALOG_INFO_PROMPT";
    public final static String LABLE_REPAYMENT_RULES = "REPAYMENT_RULES_INFO";

    public final static String LABLE_DIALOG_CHANGE_EMAIL = "DIALOG_CHANGE_EMAIL";
    public final static String LABLE_DIALOG_CHANGE_PHONE = "DIALOG_CHANGE_PHONE";
    public final static String LABLE_DIALOG_LIMIT_INC = "DIALOG_LIMIT_INCREASE";
    public final static String LABLE_DIALOG_CON_ERROR = "DIALOG_CONNECTION_ERROR";
    public final static String LABLE_DIALOG_SOME_ERROR = "DIALOG_SOMETHING_WENT_WRONG";
    public final static String LABLE_DIALOG_LOADING = "DIALOG_LOADING";
    public final static String LABLE_DIALOG_OTP_VERIFY = "DIALOG_OTP_VERIFY";
    public final static String LABLE_DIALOG_REPAYMENT = "DIALOG_REPAYMENT";
    public final static String LABLE_DIALOG_TERMS_CONDITIONS = "DIALOG_TERMS_CONDITION";
    public final static String LABLE_DIALOG_FEEDBACK = "DIALOG_USER_FEEDBACK";

    public final static String LABLE_CARD_HISTORY = "CARD_HISTORY";
    public final static String LABLE_TO_HOME = "TO_CARD_HOME";
    public final static String LABLE_TO_NEXT_TC = "NEXT_TERMS_CONDITION";
    public final static String LABLE_RECOVER_EMAIL = "RECOVER_EMAIL";
    public final static String LABLE_TO_APPOINT_EKYC_ADDRESS = "TO_APPOINT_EKYC_ADDRESS";
    public final static String LABLE_TO_REPAYMENT = "TO_REPAYMENT";
    public final static String LABLE_TO_VERIFY_PHONE = "TO_VERIFY_PHONE";
    public final static String LABLE_TO_USER_DETAILS = "TO_USER_DETAILS";
    public final static String LABLE_TO_RECOVER_PASSWORD = "TO_RECOVER_PASSWORD";
    public final static String LABLE_TO_RECOVER_PIN = "TO_RECOVER_PIN";
    public final static String LABLE_RECOVER_PIN = "RECOVER_PIN";
    public final static String LABLE_RECOVER_PASSWORD = "RECOVER_PASSWORD";
    public final static String LABLE_TO_TRANSACTION = "TO_TRANSACTIONS";
    public final static String LABLE_TO_APPOINT_EKYC_DATETIME = "TO_APPOINT_EKYC_DATE_TIME";
    public final static String LABLE_APPOINT_EKYC_DATETIME = "APPOINT_EKYC_DATE_TIME";
    public final static String LABLE_TO_INCREASE_LIMIT = "TO_INCREASE_LIMIT";
    public final static String LABLE_TO_USER_PROFILE = "TO_USER_PROFILE";
    public final static String LABLE_TO_TERMS_CONDITIONS = "TO_OLLY_TERMS_COMDITIONS";
    public final static String LABLE_TO_LEGAL_TERMS = "TO_OLLY_LEGAL_TERMS";
    public final static String LABLE_TO_PRIVATE_TERMS = "TO_OLLY_PRIVATE_TERMS";
    public final static String LABLE_TO_HELP = "TO_OLLY_HELP";
    public final static String LABLE_TO_START_EKYC = "TO_START_EKYC_APPOINT";
    public final static String LABLE_RE_OLLY_CARD = "REFRESH_OLLY_CREDIT_CARD";
    public final static String LABLE_VERIFY_PAN = "VERIFY_PAN_NUMBER";
    public final static String LABLE_VERIFY_DEBIT = "LINK_DEBIT_CARD";
    public final static String LABLE_VERIFY_EKYC = "APPOINT_EKYC";
    public final static String LABLE_VERIFY_REPAYMENT = "MAKE_REPAYMENT";
    public final static String LABLE_SUBMIT_DETAILS = "SUBMIT_DETAILS";
    public final static String LABLE_NAV_DRAWER = "NAVIGATION_DRAWER";
    public final static String LABLE_TO_CONFIRM_REPAY = "TO_CONFIRM_REPAYMENT";
    public final static String LABLE_TO_CONFIRM_EKYC = "TO_CONFIRM_EKYC_APPOINT";
    public final static String LABLE_TO_CUSTOMER_CARE = "TO_CUSTOMER_CARE";
    public final static String LABLE_TO_LOGIN_PIN = "TO_LOGIN_PIN";
    public final static String LABLE_TO_RESET_PASSWORD = "TO_RESET_PASSWORD";
    public final static String LABLE_RESET_PASSWORD = "RESET_PASSWORD";


    //base activity constants

    public final static int BASE_PERMISSION_RECEIVE_SMS_CODE = 1005;

    // base Error Dialog flags
    public final static int ERROR_DIALOG_SOMETHING = 0;
    public final static int ERROR_DIALOG_CONNECTION = 1;


    //phone activity constants
    public final static int NETWORK_REQUEST_CALL_FAILED = 1001;
    public final static int NETWORK_TOKEN_EXPIRE = 10012;
    public final static int NETWORK_REQUEST_CREATE_USER = 1002;
    public final static int NETWORK_REQUEST_LOGIN_USER = 1006;
    public final static int NETWORK_REQUEST_CREDIT_LIMIT_VALUES = 1007;
    public final static int NETWORK_REQUEST_VERIFY_OTP = 1003;
    public final static int NETWORK_REQUEST_VERIFY_PAN = 1008;
    public final static int NETWORK_REQUEST_VERIFY_USER_PAN = 1009;
    public final static int USER_ALREADY_EXIST = 1010;
    public final static int USER_NOT_EXIST = 1011;
    public final static int HELP_FETCH_FAILED = 10012;
    public final static int USER_NOT_FULLY_REG = 1013;


    //kyc appoint
    public final static int NETWORK_REQUEST_APPOINT_KYC = 1004;

    //network call flags
    public final static String NETWORK_RESPONSE_FAILED = "failure";
    public final static String NETWORK_RESPONSE_SUCCESS = "success";
    public final static boolean ONLINE = true;
    public final static boolean OFFLINE = false;


    // splash activity constants
    public final static int SPLASH_SCREEN_TIME_OUT = 1500;


    //preference keys
    public final static String PREF_USER_NUMB_KEY = "default_sim_number";
    public final static String PREF_DEFAULT_STRING_VALUE = "failed";
    public final static String PREF_USER_EMAIL_KEY = "default_email";
    public final static String PREF_USER_NAME_KEY = "default_name";
    public final static String PREF_USER_PAN_KEY = "default_pan";


    //
    public final static String PREF_PAN_VERIFIED = "flag_pan_verified";
    public final static String PREF_DEBIT_VERIFIED = "flag_debit_verified";
    public final static String PREF_EKYC_VERIFIED = "flag_ekyc_verified";
    public final static String PREF_REPAYED_VERIFIED = "flag_repayed_verified";
    public final static String PREF_EMAIL_VERIFIED = "flag_email_verified";
    public final static String PREF_USER_ONBOARDED = "flag_user_onboarded";

    //
    public final static String PREF_BAL = "pref_balance";
    public final static String PREF_USED = "pref_used";
    public final static String PREF_MAX = "pref_max";
    public final static String PREF_C_LIMIT = "pref_limit";

    public final static String DEFAULT_VALUE = "default";


    //Broadcast - Incoming message
    public final static String OTP_ACTION_VALUE = "android.provider.Telephony.SMS_RECEIVED";
    public final static String OTP_SMS_MSG_ADDRESS_ONE = "olly";
    public final static String OTP_SMS_MSG_ADDRESS_TWO = "MM-";
    public final static String OTP_SMS_MSG_ADDRESS_THREE = "59011";

    //network action
    public final static String NETWORK_ACTION_VALUE = "android.net.conn.CONNECTIVITY_CHANGE";


    public static String PREF_PAN_LIMIT = "pan_credit_limit";
    public static String PREF_DEBIT_LIMIT = "debit_credit_limit";
    public static String PREF_EKYC_LIMIT = "kyc_credit_limit";
    public static String PREF_REPAYED_LIMIT = "repay_credit_limit";
    public static String PREF_EMAIL_VERIFY = "user_email_verified";
    public static String SEND_FEEDBACK_MESSAGE = "send_feedback";
    public static String PREF_USER_CREDIT_LIMIT = "user_credit_limit";


    public static String FIRST_TIME_RUN = "first_time_run";
    public static String PREF_EKYC_SCHEDULED = "ekyc_scheduled";


    //repayments
    public final static int PAY_METHOD_DEBIT = 0;
    public final static int PAY_METHOD_NETBANK = 1;
    public final static int PAY_METHOD_UPI = 2;
    public final static int PAY_METHOD_SAVED_CARDS = 3;
    public final static int DEFAULT = -1;
    @IntDef({DEFAULT, PAY_METHOD_DEBIT, PAY_METHOD_NETBANK, PAY_METHOD_UPI, PAY_METHOD_SAVED_CARDS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PayMethod{}

    //Payment Method results
    public final static int PAYMENT_REQUEST_CODE = 10;
    public final static int PAYMENT_PROCEED = 1;
    public final static int PAYMENT_DO_NOT_PROCEED = 0;


    //hackathon

    public final static int HACKATHON_APP_FLOW = 10;
    public final static String HACKATHON_APP_KEY = "HACKATHON_APP_FLOW";
    public final static String PREF_ADHAR = "PREF_ADHAR";
    public final static String PREF_AccountNumber = "PREF_AccountNumber";
    public final static String PREF_getBhamashahId = "PREF_getBhamashahId";
    public final static String PREF_BAMA_DOB = "PREF_BAMA_DOB";
    public final static String PREF_BAMA_FIRST_NAME = "PREF_BAMA_FIRST_NAME";
    public final static String PREF_BAMA_LAST_NAME = "PREF_BAMA_LAST_NAME";



}
