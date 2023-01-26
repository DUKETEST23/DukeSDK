package com.dukeai.awsauth_sdk.sdk_util;

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

import com.dukeai.awsauth_sdk.SDK_AuthUserSession;
import com.dukeai.awsauth_sdk.sdk_exceptions.SDK_AuthClientException;
import com.dukeai.awsauth_sdk.sdk_exceptions.SDKAuthInvalidGrantException;
import com.dukeai.awsauth_sdk.sdk_exceptions.SDKAuthInvalidParameterException;
import com.dukeai.awsauth_sdk.sdk_exceptions.SDKAuthServiceException;
import com.dukeai.awsauth_sdk.sdk_tokens.SDK_AccessTokenSDK;
import com.dukeai.awsauth_sdk.sdk_tokens.SDK_IdTokenSDK;
import com.dukeai.awsauth_sdk.sdk_tokens.SDK_RefreshTokenSDK;

import org.json.JSONObject;

/**
 * Helper to parse the response from users.
 */

public class SDK_AuthHttpResponseParser {

    /**
     * This is a helper class, cannot instantiate.
     */
    private SDK_AuthHttpResponseParser() {
    }

    /**
     * Parses the http response from Cognito service and extracts tokens.
     * <p>
     * Throws {@link SDKAuthInvalidGrantException when }
     * </p>
     *
     * @param responseStr Required: Response from Cognito Service Token-Endpoint.
     * @return {@link SDK_AuthUserSession}.
     */
    public final static SDK_AuthUserSession parseHttpResponse(String responseStr) {
        if (responseStr == null || responseStr.isEmpty()) {
            throw new SDKAuthInvalidParameterException(
                    "Invalid (null) response from Amazon Cognito Auth endpoint");
        }

        SDK_AccessTokenSDK SDKAccessToken = new SDK_AccessTokenSDK(null);
        SDK_IdTokenSDK SDKIdToken = new SDK_IdTokenSDK(null);
        SDK_RefreshTokenSDK SDKRefreshToken = new SDK_RefreshTokenSDK(null);

        JSONObject responseJson;
        try {
            responseJson = new JSONObject(responseStr);
            if (responseJson.has(SDK_ClientConstants.DOMAIN_QUERY_PARAM_ERROR)) {
                String errorText = responseJson.getString(SDK_ClientConstants.DOMAIN_QUERY_PARAM_ERROR);
                if (SDK_ClientConstants.HTTP_RESPONSE_INVALID_GRANT.equals(errorText)) {
                    throw new SDKAuthInvalidGrantException(errorText);
                } else {
                    throw new SDKAuthServiceException(errorText);
                }
            }

            if (responseJson.has(SDK_ClientConstants.HTTP_RESPONSE_ACCESS_TOKEN)) {
                SDKAccessToken = new SDK_AccessTokenSDK(responseJson.getString(SDK_ClientConstants.HTTP_RESPONSE_ACCESS_TOKEN));
            }

            if (responseJson.has(SDK_ClientConstants.HTTP_RESPONSE_ID_TOKEN)) {
                SDKIdToken = new SDK_IdTokenSDK(responseJson.getString(SDK_ClientConstants.HTTP_RESPONSE_ID_TOKEN));
            }

            if (responseJson.has(SDK_ClientConstants.HTTP_RESPONSE_REFRESH_TOKEN)) {
                SDKRefreshToken = new SDK_RefreshTokenSDK(responseJson.getString(SDK_ClientConstants.HTTP_RESPONSE_REFRESH_TOKEN));
            }
        } catch (SDKAuthInvalidGrantException invg) {
            throw invg;
        } catch (SDKAuthServiceException seve) {
            throw seve;
        } catch (Exception e) {
            throw new SDK_AuthClientException(e.getMessage(), e);
        }
        return new SDK_AuthUserSession(SDKIdToken, SDKAccessToken, SDKRefreshToken);
    }
}

