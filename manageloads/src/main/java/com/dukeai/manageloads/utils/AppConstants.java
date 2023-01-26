package com.dukeai.manageloads.utils;

public class AppConstants {

    public static final String iosLink = "http://bit.ly/dukeaiiOS";
    public static final String androidLink = "http://bit.ly/dukeaiAndroid";

    //Change the theme
    // Values for themes can be { truck, duke }
    public static final String currentTheme = DukeManageLoads.theme;
    public static String SUPPORT_LINK = "https://duke.ai/";

    public class Ifta {
        public final static int GPS_REQUEST = 89;
        public final static int START_TRACKING_TRIP_DIALOG_ID = 107;
        public final static String LOCATION_PREFERENCE_NAME = "Location-Settings";
        public static final String LOCATION_UPDATE_STATUS_INCOMPLETE = "incomplete";
        public static final String LOCATION_UPDATE_STATUS_TERMINATE = "terminate";
        public static final String LOCATION_UPDATE_STATUS_FINISHED = "finished";
        public static final int STOP_TRIP_TRACKING_DIALOG_ID = 108;
        public static final String API_STATUS_SUCCESS = "Success";
        public static final String TRIP_CHANNEL_NAME = "Trip tracking channel.";
        public static final String NEW = "new";
        public static final String CONTINUE = "continue";
        public final static String LOCATION_   = "Location";
    }

    public class RegularExpressionConstants {
        final static String EMAIL_REGEXP = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        final static String PASSWORD_REGEXP = "[0-9a-zA-Z@#$%^!*()_\\-&\\S]{8,}$";
        final static String PHONE_REGEXP = "^((\\+)|(00))[0-9]{6,14}$";
        final static String PROMO_CODE_REGEXP = "^[0-9]{10}$";
        final static String REFERRAL_ID_REGEXP = EMAIL_REGEXP;
        final static String USERNAME_REGEXP = "^[a-zA-Z0-9-,'‘’ .&!]+$";
        final static String VERIFICATION_CODE = "^[0-9]{6}$";
        final static String DECIMAL_REGEXP = "^(\\d*\\.)?\\d+$";
        final static String ALPHA_NUMERIC = "^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$";
        final static String NON_EMPTY = "^(?!\\s*$).+";
        final static String DESC = "^[a-zA-Z0-9-,'‘’ .&!]+$";
        final static String DATE_NON_EMPTY = "^(?!\\s*$).+";
    }

    public class StringConstants {
        final public static String EMAIL = "email";
        final public static String PASSWORD = "password";
        final public static String PHONE = "phone";
        final public static String USERNAME = "username";
        final public static String PROMO_CODE = "promo";
        final public static String OLD_PASSWORD = "oldpw";
        final public static String NEW_PASSWORD = "newpw";
        final public static String DECIMAL_NUM = "decimalnum";
        final public static String CODE = "code";
        final public static String ERROR = "error";
        final public static String HINT = "hint";
        final public static String WARNING = "warning";
        final public static String INPROCESS_DATA_MODEL = "INPROCESS_DATA_MODEL";
        final public static String RECIPIENT_DATA_MODEL = "RECIPIENT_DATA_MODEL";
        final public static String LOADS_LIST_MODEL = "LOADS_LIST_MODEL";
        final public static String PROCESSED_DATA_MODEL = "PROCESSED_DATA_MODEL";
        final public static String REJECTED_DATA_MODEL = "REJECTED_DATA_MODEL";
        final public static String NAVIGATE_TO = "NAVIGATE_TO";
        final public static String ACTION = "ACTION";
        final public static String ACTION_TYPE_ADD = "ADD";
        final public static String ACTION_TYPE_UPDATE = "UPDATE";
        final public static String REJECT = "REJECT";
        final public static String IN_PROCESS = "IN_PROCESS";
        final public static String LOADS = "LOADS";
        final public static String PROCESS = "PROCESS";
        final public static String ADD_RECIPIENT_PAGE = "ADD_RECIPIENT_PAGE";
        final public static String EDIT_RECIPIENT_PAGE = "EDIT_RECIPIENT_PAGE";
        final public static String PREVENT_ALERT_POPUP = "PREVENT_ALERT_POPUP";
        final public static String AUTHORIZATION = "Authorization";
        final public static String REPORT_DATA = "REPORT_DATA";
        final public static String HELLO = "Hello";
        final public static String NO_INTERNET = "Unable to execute HTTP request";
        final public static String PASSWORD_DID_NOT_CONFIRM = "Password did not conform with policy";
        final public static String INVALID_PHONE_NUMBER_FORMAT = "Invalid phone number format";
        final public static String LOGOUT = "LOGOUT";
        final public static String ALPHA_NUMERIC = "alphanumeric";
        final public static String NON_EMPTY = "nonempty";
        final public static String DESC = "desc";
        public static final String DATE_NON_EMPTY = "nonemptydate";
        final public static String DESC_EMPTY = "descempty";
        final public static String COMPANY_NAME = "companyname";
        final public static String ADDRESS = "address";
        final public static String RECIPIENT_PHONE = "recipient_phone";
        final public static String DELETE_DOC_FROM_LOAD = "DELETE_DOCUMENT_FROM_LOAD";
        final public static String ADD_DOC_TO_LOAD = "ADD_DOCUMENT_TO_LOAD";
    }

    public class Pages {
        final public static String HOME = "Home";
        final public static String DOCUMENTS = "Documents";
        final public static String REPORTS = "Reports";
        final public static String LOADS = "Loads";
        final public static String PROFILE = "Profile";
    }

    public class UserAttributesConstants {
        final public static String SUB = "sub";
        final public static String PHONE_NUMBER_VERIFIED = "phone_number_verified";
        final public static String EMAIL_VERIFIED = "email_verified";
        final public static String PHONE_NUMBER = "phone_number";
    }

    public class SignUpDataConstants {
        final public static String EMAIL = "Email";
        final public static String PASSWORD = "Password";
        final public static String PHONE_NUMBER = "PhoneNumber";
        final public static String USER_NAME = "UserName";
        final public static String PROMO_CODE = "PromoCode";
    }

    public class UploadSelectionConstants {
        final public static String ONE_AS_ONE = "OneAsOne";
        final public static String ALL_AS_ONE = "AllAsOne";
        final public static String MANUAL = "Manual";
        final public static String UPLOAD = "Upload";
    }

    public class UploadConstants {
        final public static String CANCEL = "Cancel";
        final public static String CONFIRM_UPLOAD = "Confirm Upload";
        final public static String UPLOAD_FAILURE = "Upload Failure";
        final public static String FROM_UPLOAD_SELECTION = "FromUploadSelection";

    }

    public class ReportConstants {
        final public static String BALANCESHEET = "BalanceSheet";
        final public static String EXPENSES = "Expenses";
        final public static String YTD = "YTD";
        final public static String PL = "PL";
        final public static String IFTA = "IFTA";
        final public static String POD = "POD";
        final public static String SELFTAX = "self_tax";
        final public static String FEDTAX = "fed_tax";

        final public static String LIABILITIES = "Liabilities";
        final public static String LIABILITIES_TEXT = "Liabilities";
        final public static String OUT_OF_RANGE = "out of range";
        final public static String YEARTODATETAXLIABILITY = "Year To Date Tax Liability";
        final public static String PROFITANDLOSS = "Profit & Loss";
        final public static String IFTATAXFILINGS = "IFTA Tax Filings";
        final public static String PROOFOFDELIVERY = "Proof of Delivery";

        final public static String BALANCE_SHEET = "Balance Sheet";
        final public static String EXPENSE = "Expense";
        final public static String YTD_TEXT = "YTD Tax Liability";
        final public static String PL_TEXT = "Profit&Loss";
        final public static String SELFESTIMATOR = "Self Employment (Estimator)";
        final public static String FEDESTIMATOR = "Federal Tax (Estimator)";
        final public static String fedtax = "Federal Tax Liability";
        final public static String selftax = "Self Employment Tax";
    }

    public class VerificationCodeConstants {
        final public static String INVALID_VERIFICATION = "Invalid verification code provided";
        final public static String LOGIN_EMAIL = "LOGIN_EMAIL";
        final public static String LOGIN_PASSWORD = "LOGIN_PASSWORD";
    }

    public class ForgotEmail {
        final public static String EMAIL = "email";
        final public static String NOT_REGISTERED_WITH_US = "not registered with us";
        final public static String COMBINATION_NOT_FOUND = "client id combination not found";
        final public static String INVALID_VERIFICATION_CODE = "Invalid verification code provided";
        final public static String ATTEMPT_LIMIT_EXCEEDED = "Attempt limit exceeded";
        final public static String CAN_NOT_RESET_PASSWORD = "Cannot reset password";
    }

    public class UploadDocumentsConstants {
        final public static String PACKAGE = "package";
        final public static String FROM_GALLERY = "FROM GALLERY";
        final public static String FROM_CAMERA = "FROM CAMERA";
        final public static String BITMAP_IMAGE = "BITMAP IMAGE";
        final public static String MULTIPLE_DOCUMENTS = "MULTIPLE DOCUMENTS";
    }

    public class UserPreferencesConstants {
        final public static String OPEN_WITH = "OPEN_WITH";
        final public static String OPEN_WITH_HOME = "OPEN_WITH_HOME";
        final public static String OPEN_WITH_CAMERA = "OPEN_WITH_CAMERA";
        final public static String TOKEN = "TOKEN";
        final public static String DEVICE_TOKEN = "DEVICE_TOKEN";
        final public static String REFERRAL_ID = "REFERRAL_ID";
        final public static String LOGGED_IN = "LOGGED_IN";
        final public static String USER_NAME = "USER_NAME";
        final public static String ACCESS_TOKEN = "ACCESS_TOKEN";
        final public static String USER_SESSION = "USER_SESSION";
        final public static String USER_LOGGEDIN = "TRUE";
        final public static String ACCESS_TOKEN_EXPIRY = "ACCESS_TOKEN_EXPIRY";
        final public static String REFRESH_TOKEN = "REFRESH_TOKEN";
        final public static String REFRESH_TOKEN_EXPIRY = "REFRESH_TOKEN_EXPIRY";
        final public static String TOKEN_ID = "TOKEN_ID";
        final public static String TOKEN_ID_EXPIRY = "TOKEN_ID_EXPIRY";
    }


    public class PopupConstants {
        final public static String NEUTRAL = "neutral";
        final public static String POSITIVE = "positive";
        final public static String NEGATIVE = "negative";
    }

    public class HomePageConstants {
        final public static String NO_GROUPS = "not a member of any group";
    }

    public class ChangePasswordSheet {
        final public static String INCORRECT_STATEMENT = "Incorrect username or password";
    }

    public class LoginConstants {
        final public static String USER_NOT_CONFIRMED = "User is not confirmed";
        final public static String TO_LOGIN = "toLogin";
        final public static String TO_VERIFICATION = "toVerification";
        final public static String NO_INTERNET = "Unable to execute HTTP request";
        final public static String USER_DOES_NOT_EXIST = "User does not exist";
    }


}