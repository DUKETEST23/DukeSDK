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

import com.dukeai.awsauth_sdk.sdk_tokens.SDK_AccessTokenSDK;
import com.dukeai.awsauth_sdk.sdk_tokens.SDK_IdTokenSDK;
import com.dukeai.awsauth_sdk.sdk_tokens.SDK_RefreshTokenSDK;
import com.dukeai.awsauth_sdk.sdk_util.SDK_AuthClientConfig;

import java.util.Date;

/**
 * Encapsulates all Cognito tokens for a user.
 */
public class SDK_AuthUserSession {

    /**
     * Cognito identity token.
     */
    private SDK_IdTokenSDK SDKIdToken;

    /**
     * Cognito access token.
     */
    private SDK_AccessTokenSDK SDKAccessToken;

    /**
     * Cognito refresh token.
     */
    private SDK_RefreshTokenSDK SDKRefreshToken;

    /**
     * Constructs a new Cognito session.
     *
     * @param SDKIdToken      Required: ID Token for this session.
     * @param SDKAccessToken  Required: Access Token for this session.
     * @param SDKRefreshToken Required: Refresh Token.
     */
    public SDK_AuthUserSession(final SDK_IdTokenSDK SDKIdToken,
                               final SDK_AccessTokenSDK SDKAccessToken,
                               final SDK_RefreshTokenSDK SDKRefreshToken) {
        this.SDKIdToken = SDKIdToken;
        this.SDKAccessToken = SDKAccessToken;
        this.SDKRefreshToken = SDKRefreshToken;
    }

    /**
     * Returns ID Token.
     *
     * @return token as a String.
     */
    public SDK_IdTokenSDK getIdToken() {
        return SDKIdToken;
    }

    /**
     * Returns Access Token.
     *
     * @return token as a String.
     */
    public SDK_AccessTokenSDK getAccessToken() {
        return SDKAccessToken;
    }

    /**
     * Returns Refresh Token.
     *
     * @return token as a String.
     */
    public SDK_RefreshTokenSDK getRefreshToken() {
        return SDKRefreshToken;
    }

    /**
     * Returns if the access and id tokens have not expired.
     *
     * @return boolean to indicate if the access and id tokens have not expired.
     */
    public boolean isValid() {
        if (SDKAccessToken == null || SDKAccessToken.getJWTToken() == null) {
            return false;
        }
        try {
            Date currentTimeStamp = new Date();
            return (currentTimeStamp.before(SDKAccessToken.getExpiration()));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true if this session for the threshold set in
     * {@link SDK_AuthClientConfig#refreshThreshold}.
     *
     * @return boolean to indicate if the session is valid for atleast
     * {@link SDK_AuthClientConfig#refreshThreshold} seconds.
     */
    public boolean isValidForThreshold() {

        if (SDKAccessToken == null) {
            return false;
        }

        if (SDKAccessToken.getJWTToken() == null) {
            return false;
        }

        try {
            long currentTime = System.currentTimeMillis();
            long expiresInMilliSeconds = SDKAccessToken.getExpiration().getTime() - currentTime;
            return (expiresInMilliSeconds > SDK_AuthClientConfig.getRefreshThreshold());
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
        if (this.SDKAccessToken != null) {
            try {
                return this.SDKAccessToken.getUsername();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}

