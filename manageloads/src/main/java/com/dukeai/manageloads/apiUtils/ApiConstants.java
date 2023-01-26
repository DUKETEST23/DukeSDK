package com.dukeai.manageloads.apiUtils;

import com.amazonaws.regions.Regions;
import com.dukeai.manageloads.utils.AppConstants;
import com.dukeai.manageloads.utils.DukeManageLoads;

public class ApiConstants {
    public static String CLIENT_ID = DukeManageLoads.CLIENT_ID;
    public static String USER_POOL_ID = DukeManageLoads.USER_POOL_ID;
    public static String CLIENT_SECRET = DukeManageLoads.CLIENT_SECRET;
    public static Regions COGNITO_REGION = Regions.US_EAST_1;
    public static final String COGNITO = "cognito";

    String theme = AppConstants.currentTheme;
    public String app_type = getAppType();

    public String getAppType() {
        String appType = "";
        if (theme.equals("duke")) {
            appType = "DUKE";
        } else if (theme.equals("truck")) {
            appType = "TRUCK";
        }
        return appType;
    }

    public static class UpdateDeviceToken {
        public static final String DEVICE_TOKEN = "DeviceTk";
        public static final String PROMO_CODE = "promoCode";
        public static final String OS = "OS";
        public static final String ANDROID = "Android";
        public static final String USER_NAME = "user_name";
        public static final String USER_PHONE = "user_phone";
        public static final String JOIN_FROM = "join_from";
    }

    public static class UploadDocuments {
        public static final String FILE_COUNT = "fileCount";
    }

    public static class MigrateUserToCognito {
        public static final String PASSWORD = "pw";
    }

    public static class FileStatus {
        public static final String FROM = "from";
        public static final String TO = "to";
        public static final String NUMBER_OF_DOCS = "num_docs";
        public static final String NONE = "none";
    }

    public static class FinancialSummary {
        public static final String FROM = "from";
        public static final String TO = "to";
    }

    public static class DownloadFile {
        public static final String CONTENT_TYPE = "application/octet-stream";
        public static final String ACCEPT = "application/octet-stream";
        public static final String FILENAME = "report_name";
    }

    public static class GenerateReport {
        public static final String FROM = "from";
        public static final String TO = "to";
        public static final String PERIOD = "period";
        public static final String REPORT_TYPE = "report_type";
        public static final String VERSION = "version";
    }

    public class SignUpApi {
        public static final String NAME = "Name";
        public static final String EMAIL = "Email";
        public static final String PHONE_NUMBER = "Phone Number";
        public static final String PROMO_CODE = "Promo Code";
        public static final String APP_TYPE = "custom:app_type";
        //        public final String app_type = getAppType();
        public static final String JOIN_FROM = "join_from";

    }

    public class ERRORS {
        public static final String SUCCESS = "Success";
        public static final String CODE = "Code";
        public static final String MESSAGE = "Message";
        public static final String MESSAGE_TOKEN_EXPIRY = "message";

    }

    public class PromoCode {
        public static final String PROMO_CODE = "promoCode";
        public static final String PROMO_CODE_CHECK = "promoCodeCheck";
    }

    public class Loads {
        public static final String CREATE_LOAD = "createLoad";
    }

    public class IFTA {
        public static final String ACCEPT = "application/json";
    }
}
