package com.dukeai.manageloads.apiUtils;

import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ResponseInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request request = chain.request();
        final Response response = chain.proceed(request);
        final String method = request.method();
        if (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT") ||
                method.equalsIgnoreCase("PATCH")) {
            final ResponseBody body = response.body();
            String responseString = body.string();
            if (responseString == null || responseString.length() <= 2) {
                responseString = new JsonObject().toString();
            }
            MediaType contentType = body.contentType();
            ResponseBody newBody = ResponseBody.create(contentType, responseString);
            return response.newBuilder().body(newBody).build();
        } else {
            return response;
        }
    }
}
