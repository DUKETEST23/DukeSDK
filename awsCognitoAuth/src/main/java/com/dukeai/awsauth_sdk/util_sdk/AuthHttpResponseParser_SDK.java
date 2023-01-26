package com.dukeai.awsauth_sdk.util_sdk;

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

import com.dukeai.awsauth_sdk.AuthUserSession_SDK;
import com.dukeai.awsauth_sdk.exceptions_sdk.AuthClientException_SDK;
import com.dukeai.awsauth_sdk.exceptions_sdk.AuthInvalidGrantException_SDK;
import com.dukeai.awsauth_sdk.exceptions_sdk.AuthInvalidParameterException_SDK;
import com.dukeai.awsauth_sdk.exceptions_sdk.AuthServiceException_SDK;
import com.dukeai.awsauth_sdk.tokens_sdk.AccessToken_SDK;
import com.dukeai.awsauth_sdk.tokens_sdk.IdToken_SDK;
import com.dukeai.awsauth_sdk.tokens_sdk.RefreshToken_SDK;

import org.json.JSONObject;

/**
 * Helper to parse the response from users.
 */

public class AuthHttpResponseParser_SDK {

    /**
     * This is a helper class, cannot instantiate.
     */
    private AuthHttpResponseParser_SDK() {
    }

    /**
     * Parses the http response from Cognito service and extracts tokens.
     * <p>
     * Throws {@link AuthInvalidGrantException_SDK when }
     * </p>
     *
     * @param responseStr Required: Response from Cognito Service Token-Endpoint.
     * @return {@link AuthUserSession_SDK}.
     */
    public final static AuthUserSession_SDK parseHttpResponse(String responseStr) {
        if (responseStr == null || responseStr.isEmpty()) {
            throw new AuthInvalidParameterException_SDK(
                    "Invalid (null) response from Amazon Cognito Auth endpoint");
        }

        AccessToken_SDK accessTokenSDK = new AccessToken_SDK(null);
        IdToken_SDK idTokenSDK = new IdToken_SDK(null);
        RefreshToken_SDK refreshTokenSDK = new RefreshToken_SDK(null);

        JSONObject responseJson;
        try {
            responseJson = new JSONObject(responseStr);
            if (responseJson.has(ClientConstants_SDK.DOMAIN_QUERY_PARAM_ERROR)) {
                String errorText = responseJson.getString(ClientConstants_SDK.DOMAIN_QUERY_PARAM_ERROR);
                if (ClientConstants_SDK.HTTP_RESPONSE_INVALID_GRANT.equals(errorText)) {
                    throw new AuthInvalidGrantException_SDK(errorText);
                } else {
                    throw new AuthServiceException_SDK(errorText);
                }
            }

            if (responseJson.has(ClientConstants_SDK.HTTP_RESPONSE_ACCESS_TOKEN)) {
                accessTokenSDK = new AccessToken_SDK(responseJson.getString(ClientConstants_SDK.HTTP_RESPONSE_ACCESS_TOKEN));
            }

            if (responseJson.has(ClientConstants_SDK.HTTP_RESPONSE_ID_TOKEN)) {
                idTokenSDK = new IdToken_SDK(responseJson.getString(ClientConstants_SDK.HTTP_RESPONSE_ID_TOKEN));
            }

            if (responseJson.has(ClientConstants_SDK.HTTP_RESPONSE_REFRESH_TOKEN)) {
                refreshTokenSDK = new RefreshToken_SDK(responseJson.getString(ClientConstants_SDK.HTTP_RESPONSE_REFRESH_TOKEN));
            }
        } catch (AuthInvalidGrantException_SDK invg) {
            throw invg;
        } catch (AuthServiceException_SDK seve) {
            throw seve;
        } catch (Exception e) {
            throw new AuthClientException_SDK(e.getMessage(), e);
        }
        return new AuthUserSession_SDK(idTokenSDK, accessTokenSDK, refreshTokenSDK);
    }
}

