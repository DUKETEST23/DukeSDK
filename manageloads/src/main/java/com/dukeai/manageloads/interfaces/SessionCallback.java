package com.dukeai.manageloads.interfaces;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;

public interface SessionCallback {
    void processDone(CognitoUserSession session);
}
