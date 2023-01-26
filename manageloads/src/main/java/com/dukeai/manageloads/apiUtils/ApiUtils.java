package com.dukeai.manageloads.apiUtils;

import android.content.Context;

import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.R;
import com.dukeai.manageloads.utils.Utilities;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ApiUtils {
    public static String getApiError(String response, Context context) throws Exception {
        if (response == null || response.length() <= 1) {
            return Utilities.getStrings(context, R.string.invalid_error);
        }
        JsonObject errorObject = new JsonParser().parse(response).getAsJsonObject();
        // Throw the API error and return the message for futher use
        if (errorObject.has(ApiConstants.ERRORS.CODE)) {
            return errorObject.get(ApiConstants.ERRORS.MESSAGE).getAsString();
        } else if (errorObject.has(ApiConstants.ERRORS.MESSAGE)) {
            return errorObject.get(ApiConstants.ERRORS.MESSAGE).getAsString();
        } else if (errorObject.has(ApiConstants.ERRORS.MESSAGE_TOKEN_EXPIRY)) {
            return errorObject.get(ApiConstants.ERRORS.MESSAGE_TOKEN_EXPIRY).getAsString();
        }
        return Utilities.getStrings(context, R.string.invalid_error);
    }

    public static String getFailureErrorString(Throwable t,Context context) {
        if (!new ServiceManager(context).isNetworkAvailable(context)) {
            return context.getString(R.string.no_internet);
        } else {
            return t.getMessage();
        }
    }
}
