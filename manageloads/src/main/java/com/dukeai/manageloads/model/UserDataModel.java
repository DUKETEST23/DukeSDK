package com.dukeai.manageloads.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.dukeai.awsauth.Auth;
import com.dukeai.awsauth.AuthUserSession;
import com.dukeai.awsauth.handlers.AuthHandler;
import com.dukeai.awsauth.util.JWTParser;
import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.apiUtils.ApiConstants;
import com.dukeai.manageloads.interfaces.OnSuccessListener;

import org.json.JSONObject;

public class UserDataModel {

    CognitoUserSession cognitoUserSession;
    String cognitoUserName = ApiConstants.COGNITO, name = "";
    LoginType loginType = LoginType.Cognito;
    String userEmail, userPassword, userReferralId;
    Boolean isLoggedIn;
    private AuthUserSession authUserSession;


    public UserDataModel(String userEmail, String userPassword, String userReferralId, Boolean isLoggedIn) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userReferralId = userReferralId;
        this.isLoggedIn = isLoggedIn;
    }

    public UserDataModel(CognitoUserSession session, String name, String password) {
        this.cognitoUserSession = session;
        this.userEmail = name;
        this.userPassword = password;
    }

    public UserDataModel(AuthUserSession userSession) {
        setAuthUserSession(userSession);
        setLoginType(LoginType.Social);
        setLoggedIn(true);
        setDetails(JWTParser.getPayload(userSession.getIdToken().getJWTToken()));
    }

    private void setDetails(JSONObject obj) {
        try {
            if (obj.has("email")) {
                setUserEmail(obj.getString("email"));
            }
            if (obj.has("name")) {
                setName(obj.getString("name"));
                Duke.userName = getName();
            }
            if (obj.has("cognito:username")) {
                setCognitoUserName(obj.getString("cognito:username"));
            }
        } catch (Exception e) {
            Log.d("SET DETAILS EXCEPTION: ", "setDetails: " + e.getMessage());
        }
    }

    public String getStoredJWTToken() {
        if (getLoginType() == LoginType.Social) {
            return getAuthUserSession().getIdToken().getJWTToken();
        } else {
            return getCognitoUserSession().getIdToken().getJWTToken();
        }
    }

    public void getJWTToken(Context context, final OnSuccessListener resultListener) {
        if (loginType == LoginType.Social) {
            try {
                Auth authSDK = Duke.getAuth(context, new AuthHandler() {
                    @Override
                    public void onSuccess(final AuthUserSession session) {
                        resultListener.onSuccess(session.getIdToken().getJWTToken());
                        if (Duke.userName == null) {
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    setDetails(JWTParser.getPayload(session.getIdToken().getJWTToken()));
                                }
                            });
                        }
                    }

                    @Override
                    public void onSignout() {
                        Log.d("SIGN_OUT", "onSignout: ");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        resultListener.onSuccess("none");
                    }
                });
                authSDK.getSession();
            } catch (Exception e) {
                e.printStackTrace();
                resultListener.onSuccess("none");
            }
        } else {
            resultListener.onSuccess(cognitoUserSession.getIdToken().getJWTToken());
        }
    }

    public String getCognitoUserName() {
        return cognitoUserName;
    }

    public void setCognitoUserName(String cognitoUserName) {
        this.cognitoUserName = cognitoUserName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AuthUserSession getAuthUserSession() {
        return authUserSession;
    }

    public void setAuthUserSession(AuthUserSession authUserSession) {
        this.authUserSession = authUserSession;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public Boolean getLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public String getUserReferralId() {
        return userReferralId;
    }

    public void setUserReferralId(String userReferralId) {
        this.userReferralId = userReferralId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public CognitoUserSession getCognitoUserSession() {
        return cognitoUserSession;
    }

    public void setCognitoUserSession(CognitoUserSession cognitoUserSession) {
        this.cognitoUserSession = cognitoUserSession;
    }

    public enum LoginType {
        // This will call enum constructor with one
        // String argument
        Social("social"), Cognito("cognito");

        // declaring private variable for getting values
        private String rawValue;

        // enum constructor - cannot be public or protected
        private LoginType(String rawValue) {
            this.rawValue = rawValue;
        }

        // getter method
        public String getAction() {
            return this.rawValue;
        }
    }
}
