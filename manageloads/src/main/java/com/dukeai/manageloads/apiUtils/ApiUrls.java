package com.dukeai.manageloads.apiUtils;

public class ApiUrls {
//    TODO production urls
    public static final String LOAD_API_BASE = "https://wrrflng4q0.execute-api.us-east-1.amazonaws.com/api/"; // da, is good
    public static final String DUKE_API_BASE = "https://25g071tuih.execute-api.us-east-1.amazonaws.com/api/"; // Oh boy...
    public static final String BASE_URL = "https://25g071tuih.execute-api.us-east-1.amazonaws.com/api/";

//    TODO dev urls
    //    public static final String DUKE_API_BASE = "https://8kp4o32tvh.execute-api.us-east-1.amazonaws.com/dev/"; // Oh boy...
    //    public static final String BASE_URL = "https://8kp4o32tvh.execute-api.us-east-1.amazonaws.com/dev/";
    //    public static final String LOAD_API_BASE = "https://onga2hgadl.execute-api.us-east-1.amazonaws.com/dev/"; // da, is good
    public static final String TRANSMIT_DOCS_API = "https://bqlr44p503.execute-api.us-east-1.amazonaws.com/dev/"; // da, is good
    public static final String DUKE_API_BASE_DOWNLOAD_REPORT = "https://bqlr44p503.execute-api.us-east-1.amazonaws.com/dev/"; // Oh boy...
    public static final String DOCUMENT_API_BASE = "https://vnv960yw3a.execute-api.us-east-1.amazonaws.com/dev/"; // Oh boy
//    public static final String AUTHENTICATION = "https://tz736n035m.execute-api.us-east-1.amazonaws.com/dev/";
    public static final String AUTHENTICATION = "https://gv6ygwnae7.execute-api.us-east-1.amazonaws.com/api/";

    public class Authentication{
        public static final String AUTH =AUTHENTICATION+"duke_sdk/authenticate";
    }

    public class Loads {
        public static final String CREATE_LOAD = LOAD_API_BASE + "{cust_id}" + "/create_load";
        public static final String RECIPIENTS_LIST = LOAD_API_BASE + "{cust_id}" + "/get_load_recipients";
        public static final String ADD_RECIPIENT = LOAD_API_BASE + "{cust_id}" + "/update_load_recipient";
        public static final String UPDATE_RECIPIENT = LOAD_API_BASE + "{cust_id}" + "/update_load_recipient";
        public static final String USER_LOADS = LOAD_API_BASE + "{cust_id}" + "/get_loads";
        public static final String DELETE_LOAD_OBJECT = LOAD_API_BASE + "{cust_id}" + "/" + "{loadUUID}" + "/" + "delete_load_object";
        public static final String DELETE_FROM_LOAD = LOAD_API_BASE + "{cust_id}" + "/" + "{loadUUID}" + "/" + "delete_from_load";
        public static final String ADD_TO_LOAD = LOAD_API_BASE + "{cust_id}" + "/" + "{loadUUID}" + "/" + "add_to_load";
        public static final String GET_LOAD_DETAIL = LOAD_API_BASE + "{cust_id}" + "/" + "{loadUUID}" + "/" + "get_load_detail";
        public static final String TRANSMIT_LOADS = LOAD_API_BASE + "{cust_id}" + "/transmit_load";
        public static final String TRANSMIT_PROCESSED_DOCS = TRANSMIT_DOCS_API + "{cust_id}" + "/download";
    }

    public class UserRegistration {
        public static final String FORGOT_PASSWORD = DUKE_API_BASE + "{cust_id}/forgetPassword";
    }

    public class FileStatus {
        public static final String FILE_STATUS = DUKE_API_BASE + "{cust_id}" + "/status";
        public static final String FILE_DOWNLOAD = DUKE_API_BASE + "{cust_id}" + "/" + "{filename}";
        public static final String DOWNLOAD_REPORT = DUKE_API_BASE_DOWNLOAD_REPORT + "{cust_id}" + "/" + "download_report";
        public static final String DELETE_FILE = BASE_URL + "{cust_id}" + "/" + "{sha1}" + "/fromuser";
    }

    public class DocumentDetails {
        public static final String URL = DOCUMENT_API_BASE + "{cust_id}" + "/" + "{sha1}";
//         public static final String URL = " https://aluzzhbxa4.execute-api.us-east-2.amazonaws.com/dev/" + "{cust_id}" + "/" + "{sha1}";
    }

    public class DocumentSignatureUpdate {
        public static final String URL = DUKE_API_BASE + "{cust_id}" + "/signature";
//         public static final String URL = "https://8kp4o32tvh.execute-api.us-east-1.amazonaws.com/dev/" + "{cust_id}" + "/signature";
    }

    // Upload uses DUKE-API
    public class MultiFileUpload {
        public static final String MULTI_FILE_UPLOAD = DUKE_API_BASE + "{cust_id}" + "/multiupload";
        public static final String NEW_MULTI_UPLOAD = DUKE_API_BASE + "{cust_id}" + "/multiupload_prime";
        public static final String GENERATE_MANIFEST = DUKE_API_BASE + "{cust_id}" + "/multiupload/gen_manifest";
//         public static final String MULTI_FILE_UPLOAD = ALPHA_STAGING_ENDPOINT + "{cust_id}" + "/multiupload";
//         public static final String NEW_MULTI_UPLOAD = ALPHA_STAGING_ENDPOINT + "{cust_id}" + "/multiupload_prime";
//         public static final String GENERATE_MANIFEST = ALPHA_STAGING_ENDPOINT + "{cust_id}" + "/multiupload/gen_manifest";
    }

}
