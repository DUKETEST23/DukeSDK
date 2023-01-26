package com.dukeai.manageloads.apiUtils;

import android.content.Context;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.auth0.android.jwt.JWT;
import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.R;
import com.dukeai.manageloads.interfaces.PopupActions;
import com.dukeai.manageloads.interfaces.SessionCallback;
import com.dukeai.manageloads.utils.AppConstants;
import com.dukeai.manageloads.utils.UserConfig;
import com.dukeai.manageloads.views.ConfirmationComponent;
import java.io.IOException;
import java.util.Date;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkInterceptor implements Interceptor {

    String userName, password;
    ConfirmationComponent confirmationComponent;
    PopupActions popupActions;
    Response response;
    private UserConfig userConfig = UserConfig.getInstance();
    Context context;

    public NetworkInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(final Chain chain) throws IOException {
        if (!new ServiceManager(context).isNetworkAvailable(context)) {
            throw (new IOException(context.getString(R.string.no_internet)));
        } else {
            final Request originalRequest = chain.request();
            final Headers headers = originalRequest.headers();
            if (headers != null && headers.get(AppConstants.StringConstants.AUTHORIZATION) != null && headers.get(AppConstants.StringConstants.AUTHORIZATION).length() > 0 && !Duke.isWithOutToken) {
                String token = headers.get(AppConstants.StringConstants.AUTHORIZATION);
//                String token = "eyJraWQiOiJiTEw2ZTYrb21wbHo3SG5INkxGeEh4ZGFuRmNBUzU5a09XOElFOFhLb3pzPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJmNTE4MGI2ZS02MjIxLTRkZjItOWQzMy0xOTAwOGNhOWMwOGUiLCJjb2duaXRvOmdyb3VwcyI6WyJhY2NvdW50YW50cyIsInVzZXJzIl0sImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAudXMtZWFzdC0xLmFtYXpvbmF3cy5jb21cL3VzLWVhc3QtMV9RVm1JS1MzYUEiLCJwaG9uZV9udW1iZXJfdmVyaWZpZWQiOmZhbHNlLCJjb2duaXRvOnVzZXJuYW1lIjoiUFJBREhVTU5ARElWQU1JLkNPTSIsImF1ZCI6IjdqaTVkZjdiZ3FtbnZ0YjRtNTI3bzEzNGVlIiwiZXZlbnRfaWQiOiIzNDAzMWI1Yy03ZGIwLTRjNzItYjYwNS0wYTczYmMwNmZkMGMiLCJ0b2tlbl91c2UiOiJpZCIsImF1dGhfdGltZSI6MTY2NTQ5MjIxMiwibmFtZSI6IlBSQURIVU1OQERJVkFNSS5DT00iLCJwaG9uZV9udW1iZXIiOiIrMTIzNDU2Nzg5NTgiLCJjdXN0b206YXBwX3R5cGUiOiJEVUtFIiwiZXhwIjoxNjY1NDk1ODEyLCJpYXQiOjE2NjU0OTIyMTIsImVtYWlsIjoiUFJBREhVTU5ARElWQU1JLkNPTSJ9.V4bJAJGDognVtPb5sjZlk7Np5HDgsYx9ldjWVTKrua80ce6xGrh4mL7bTDto1UrJsNOlbvSsy4gkBCC9aharYlzDW4XRhDlgxNSbEnfvFqUHSTWjue8A3mzzCiN15pRiBBwZS567WI5RE0frVfS06m9OUKY7OzhaMfTuPDoklj9SBqPD53A6Ad-7paoTf-VJ64-EynL73i_zzBTXAUPKFpmu0s9XDqPZLVqs2ORLBcOY0MLfyZiDsxfNkaHWxkG9_2RvXSVm2v0jraBFB-RFXb1v2BQQtzOqFz-vkhRBZha-fFAr8Ez7TeBEWnSLx3tgHMZP3KghXa_9FqmRbmYk5g";
                JWT jwt = new JWT(token);
                if (jwt.getExpiresAt() != null && jwt.getExpiresAt().getTime() < new Date().getTime()) {
                    NewUserSession.getNewSession(new SessionCallback() {
                        @Override
                        public void processDone(CognitoUserSession session) {
                            if (session != null) {
                                Request.Builder requestBuilder = originalRequest.newBuilder()
                                        .header(AppConstants.StringConstants.AUTHORIZATION, session.getIdToken().getJWTToken()).method(originalRequest.method(), originalRequest.body());
                                Request request = requestBuilder.build();
                                try {
                                    response = chain.proceed(request);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
//                                Utilities.sendLogoutBroadcast();
                            }
                        }
                    });
                } else {
                    response = chain.proceed(chain.request());
                }
            } else {
                response = chain.proceed(chain.request());
            }
        }
        return response;
    }

}
