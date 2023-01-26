package com.dukeai.awsauth_sdk;

/*
 * Copyright 2013-2017 Amazon.com, Inc. or its affiliates.
 * All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.dukeai.awsauth_sdk.tokens_sdk.AccessToken_SDK;
import com.dukeai.awsauth_sdk.tokens_sdk.IdToken_SDK;
import com.dukeai.awsauth_sdk.tokens_sdk.RefreshToken_SDK;
import com.dukeai.awsauth_sdk.util_sdk.AuthClientConfig_SDK;

import java.util.Date;

/**
 * Encapsulates all Cognito tokens for a user.
 */
public class AuthUserSession_SDK {

    /**
     * Cognito identity token.
     */
    private IdToken_SDK idTokenSDK;

    /**
     * Cognito access token.
     */
    private AccessToken_SDK accessTokenSDK;

    /**
     * Cognito refresh token.
     */
    private RefreshToken_SDK refreshTokenSDK;

    /**
     * Constructs a new Cognito session.
     *
     * @param idTokenSDK      Required: ID Token for this session.
     * @param accessTokenSDK  Required: Access Token for this session.
     * @param refreshTokenSDK Required: Refresh Token.
     */
    public AuthUserSession_SDK(final IdToken_SDK idTokenSDK,
                               final AccessToken_SDK accessTokenSDK,
                               final RefreshToken_SDK refreshTokenSDK) {
        this.idTokenSDK = idTokenSDK;
        this.accessTokenSDK = accessTokenSDK;
        this.refreshTokenSDK = refreshTokenSDK;
    }

    /**
     * Returns ID Token.
     *
     * @return token as a String.
     */
    public IdToken_SDK getIdToken() {
        return idTokenSDK;
    }

    /**
     * Returns Access Token.
     *
     * @return token as a String.
     */
    public AccessToken_SDK getAccessToken() {
        return accessTokenSDK;
    }

    /**
     * Returns Refresh Token.
     *
     * @return token as a String.
     */
    public RefreshToken_SDK getRefreshToken() {
        return refreshTokenSDK;
    }

    /**
     * Returns if the access and id tokens have not expired.
     *
     * @return boolean to indicate if the access and id tokens have not expired.
     */
    public boolean isValid() {
        if (accessTokenSDK == null || accessTokenSDK.getJWTToken() == null) {
            return false;
        }
        try {
            Date currentTimeStamp = new Date();
            return (currentTimeStamp.before(accessTokenSDK.getExpiration()));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true if this session for the threshold set in
     * {@link AuthClientConfig_SDK#refreshThreshold}.
     *
     * @return boolean to indicate if the session is valid for atleast
     * {@link AuthClientConfig_SDK#refreshThreshold} seconds.
     */
    public boolean isValidForThreshold() {

        if (accessTokenSDK == null) {
            return false;
        }

        if (accessTokenSDK.getJWTToken() == null) {
            return false;
        }

        try {
            long currentTime = System.currentTimeMillis();
            long expiresInMilliSeconds = accessTokenSDK.getExpiration().getTime() - currentTime;
            return (expiresInMilliSeconds > AuthClientConfig_SDK.getRefreshThreshold());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns username contained in this session.
     * <p>
     * Reads the username from Access Tokens.
     * Returns null on Exceptions - This would mean that the contained tokens are not parsable
     * and hence are not valid.
     * </p>
     *
     * @return Username of the user to whom these tokens belong.
     */
    public String getUsername() {
        if (this.accessTokenSDK != null) {
            try {
                return this.accessTokenSDK.getUsername();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}

