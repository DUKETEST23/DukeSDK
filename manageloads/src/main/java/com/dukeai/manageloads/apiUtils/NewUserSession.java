package com.dukeai.manageloads.apiUtils;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.dukeai.manageloads.interfaces.SessionCallback;
import com.dukeai.manageloads.model.UserDataModel;
import com.dukeai.manageloads.utils.SaveUserPreferences;
import com.dukeai.manageloads.utils.UserConfig;


public class NewUserSession {

    static AuthenticationHandler authenticationHandler;
    static String userName = "PRADHUMN@DIVMAI.COM", password = "MrPM@Divami.com";
    static AuthenticationDetails authenticationDetails;
    static private UserConfig userConfig = UserConfig.getInstance();

    private static void getUserAuthentication(AuthenticationContinuation continuation) {
        userName = userConfig.getUserDataModel().getUserEmail().toUpperCase();
        password = userConfig.getUserDataModel().getUserPassword();
        authenticationDetails = new AuthenticationDetails(userName, password, null);
        continuation.setAuthenticationDetails(authenticationDetails);
        continuation.continueTask();
    }

    public static void getNewSession(final SessionCallback callback) {
        AppHelper.getPool().getUser(userName).getSession(new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                AppHelper.setCurrSession(userSession);
                UserDataModel userDataModel = new UserDataModel(userSession, userName, password);
                userConfig.setUserDataModel(userDataModel);
                new SaveUserPreferences().execute(userDataModel);
                callback.processDone(userSession);
            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                getUserAuthentication(authenticationContinuation);
            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {

            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {

            }

            @Override
            public void onFailure(Exception exception) {
                callback.processDone(null);
            }
        });
    }
}
