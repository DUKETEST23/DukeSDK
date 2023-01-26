package com.dukeai.manageloads.apiUtils;

public class AuthApiUtil {


//        public static String server_address = "https://tz736n035m.execute-api.us-east-1.amazonaws.com/dev/";
        public static String server_address = "https://gv6ygwnae7.execute-api.us-east-1.amazonaws.com/api/";


        public static DukeApi getInterface() {
            return RetrofitClient.getClient(server_address).create(DukeApi.class);
        }


}
