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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.browser.customtabs.CustomTabsCallback;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import android.text.TextUtils;
import android.util.Log;

import com.amazonaws.cognito.clientcontext.data.UserContextDataProvider;
import com.dukeai.awsauth_sdk.sdk_exceptions.SDKAuthInvalidGrantException;
import com.dukeai.awsauth_sdk.sdk_exceptions.SDKAuthNavigationException;
import com.dukeai.awsauth_sdk.sdk_exceptions.SDKAuthServiceException;
import com.dukeai.awsauth_sdk.sdk_handlers.SDK_AuthHandler;
import com.dukeai.awsauth_sdk.sdk_util.SDK_AuthHttpClient;
import com.dukeai.awsauth_sdk.sdk_util.SDK_AuthHttpResponseParser;
import com.dukeai.awsauth_sdk.sdk_util.SDK_ClientConstants;
import com.dukeai.awsauth_sdk.sdk_util.SDK_LocalDataManager;
import com.dukeai.awsauth_sdk.sdk_util.SDK_Pkce;

import java.net.URL;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Local client for {@link SDK_Auth}.
 * <p>
 * Encapsulates user level operations, tokens {@link SDK_AuthUserSession}, handles
 * token caching, and token refresh.
 * Manages Cognito-Web user screens.
 * </p>
 */

@SuppressWarnings("checkstyle:javadocmethod")
public class SDK_AuthClient {
    /**
     * Android application context.
     */
    private final Context context;

    /**
     * Reference to the parent pool.
     */
    private final SDK_Auth pool;

    /**
     * Username used to instantiate this class.
     */
    private String userId;

    /**
     * Generated proof-key for PKCE.
     */
    private String proofKey;

    /**
     * SHA256 hash of the generated proof-key.
     */
    private String proofKeyHash;

    /**
     * Session state - stores the unique string generated for to set state query parameter.
     */
    private String state;

    /**
     * Callback handler.
     */
    private SDK_AuthHandler userHandler;
    /**
     * Callback for Custom Tabs to track navigation.
     */
    private final CustomTabsCallback customTabsCallback = new CustomTabsCallback() {
        @Override
        public void onNavigationEvent(final int navigationEvent, final Bundle extras) {
            super.onNavigationEvent(navigationEvent, extras);
            if (navigationEvent == SDK_ClientConstants.CHROME_NAVIGATION_CANCELLED) {
                final boolean hasReceivedRedirect = SDK_LocalDataManager.hasReceivedRedirect(pool.awsKeyValueStore,
                        context, pool.getAppId());
                Log.i("AuthClient", "customTab hidden callback, code has already been received: " + hasReceivedRedirect);
                if (!hasReceivedRedirect) {
                    userHandler.onFailure(new SDKAuthNavigationException("user cancelled"));
                    SDK_LocalDataManager.cacheHasReceivedRedirect(pool.awsKeyValueStore, context, pool.getAppId(), false);
                }
            }
        }
    };
    // - Chrome Custom Tabs Controls
    private CustomTabsClient mCustomTabsClient;
    private CustomTabsSession mCustomTabsSession;
    private CustomTabsIntent mCustomTabsIntent;
    private CustomTabsServiceConnection mCustomTabsServiceConnection;

    /**
     * Constructs {@link SDK_AuthClient} with no user name.
     *
     * @param context Required: The android application {@link Context}.
     * @param pool    Required: A reference to the parent, {@link SDK_Auth}.
     */
    protected SDK_AuthClient(final Context context, final SDK_Auth pool) {
        this(context, pool, null);
    }

    /**
     * Constructs an instance of the Cognito User with username.
     *
     * @param context  Required: The android application {@link Context}.
     * @param pool     Required:  A reference to the parent, {@link SDK_Auth}.
     * @param username Required: The username of the user in the Cognito User-Pool.
     */
    protected SDK_AuthClient(final Context context, final SDK_Auth pool, final String username) {
        this.context = context;
        this.pool = pool;
        this.userId = username;
        preWarmChrome();
    }

    /**
     * Set callback handler for {@link SDK_AuthClient}.
     *
     * @param handler Required: {@link SDK_AuthHandler}.
     */
    protected void setUserHandler(final SDK_AuthHandler handler) {
        if (handler == null) {
            throw new InvalidParameterException("Callback handler cannot be null");
        }
        userHandler = handler;
    }

    /**
     * Launches user authentication screen and returns a redirect Uri through an {@link Intent}.
     * <p>
     * Checks for cached, valid tokens and launches the Cognito Web UI if no valid tokens are
     * found. This method uses PKCE for authentication. This SDK, therefore, uses code-grant flow
     * to authenticate user. The proof-key and a state is generated and its hash is used in added
     * as query parameters to create the authentication FQDN.
     * The state value set this method is used to temporarily cache the proof-key on the device.
     * To exchange the code for tokens, the {@link SDK_Auth#getTokens(Uri)} method will use the
     * state in the redirect uri to fetch the stored proof-key.
     * </p>
     *
     * @param showSignInIfExpired true if the web UI should launch when the session is expired
     */
    protected void getSession(final boolean showSignInIfExpired) {
        try {
            proofKey = SDK_Pkce.generateRandom();
            proofKeyHash = SDK_Pkce.generateHash(proofKey);
            state = SDK_Pkce.generateRandom();
        } catch (Exception e) {
            userHandler.onFailure(e);
        }

        // Look for cached tokens
        SDK_AuthUserSession session =
                SDK_LocalDataManager.getCachedSession(pool.awsKeyValueStore, context, pool.getAppId(), userId, pool.getScopes());

        // Check if the session is valid and returns tokens
        if (session.isValidForThreshold()) {
            userHandler.onSuccess(session);
            return;
        }

        // Try refreshing the tokens
        if (session.getRefreshToken() != null && session.getRefreshToken().getToken() != null) {
            refreshSession(session, pool.getSignInRedirectUri(), pool.getScopes(), userHandler);
        } else if (showSignInIfExpired) {
            launchCognitoAuth(pool.getSignInRedirectUri(), pool.getScopes());
        } else {
            userHandler.onFailure(new Exception("No cached session"));
        }
    }

    /**
     * Forcefully Refresh Token
     * <p>
     * Added This Section to solve the problem of obtaining a new token during registration in Duke.ai.
     * </p>
     */
    protected void getRefreshSession() {
        // Look for cached tokens
        SDK_AuthUserSession session =
                SDK_LocalDataManager.getCachedSession(pool.awsKeyValueStore, context, pool.getAppId(), userId, pool.getScopes());
        if (session.getRefreshToken() != null && session.getRefreshToken().getToken() != null) {
            refreshSession(session, pool.getSignInRedirectUri(), pool.getScopes(), userHandler);
        } else {
            userHandler.onFailure(new Exception("No cached session"));
        }
    }

    /**
     * @return Current Username.
     */
    protected String getUsername() {
        return userId;
    }

    /**
     * Sets username.
     *
     * @param username Required: Username as a {@link String}.
     */
    protected void setUsername(final String username) {
        this.userId = username;
    }

    /**
     * Signs-out a user.
     * <p>
     * Clears cached tokens for the user. Launches the sign-out Cognito web end-point to
     * clear all Cognito Auth cookies stored by Chrome.
     * </p>
     */
    public void signOut() {
        SDK_LocalDataManager.clearCache(pool.awsKeyValueStore, context, pool.getAppId(), userId);
        launchSignOut(pool.getSignOutRedirectUri());
    }

    /**
     * Signs-out a user.
     * <p>
     * Clears cached tokens for the user. Launches the sign-out Cognito web end-point to
     * clear all Cognito Auth cookies stored by Chrome.
     * </p>
     *
     * @param clearLocalTokensOnly true if signs out the user from the client,
     *                             but the session may still be alive from the browser.
     */
    public void signOut(final boolean clearLocalTokensOnly) {
        SDK_LocalDataManager.clearCache(pool.awsKeyValueStore, context, pool.getAppId(), userId);
        if (!clearLocalTokensOnly) {
            launchSignOut(pool.getSignOutRedirectUri());
        }
    }

    /**
     * @return {@code true} if valid tokens are available for the user.
     */
    @SuppressWarnings("checkstyle:hiddenfield")
    public boolean isAuthenticated() {
        SDK_AuthUserSession session =
                SDK_LocalDataManager.getCachedSession(pool.awsKeyValueStore, context, pool.getAppId(), userId, pool.getScopes());
        return session.isValidForThreshold();
    }

    /**
     * Exchanges code in the Uri for with Cognito JWT.
     * <p>Checks if the Uri passed to this method is valid. We can avoid a function </p>
     *
     * @param uri Required: The redirect {@link Uri}.
     */
    public void getTokens(final Uri uri) {
        if (uri == null) {
            return;
        }
        // The flag
        SDK_LocalDataManager.cacheHasReceivedRedirect(pool.awsKeyValueStore, context, pool.getAppId(), true);
        getTokens(uri, userHandler);
    }

    /**
     * Unbind {@link SDK_AuthClient#mCustomTabsServiceConnection}
     */
    public void unbindServiceConnection() {
        if (mCustomTabsServiceConnection != null)
            context.unbindService(mCustomTabsServiceConnection);
    }

    /**
     * Internal method to exchange code for tokens.
     * <p>
     * Checks if the Uri contains a <b>state</b> query parameter. The FQDN for Cognito UI
     * Web-Page contains a state. This method considers Uri's without a state parameter as
     * <b><logout</b> redirect.
     * Checks if the value of the contained state variable is valid. This is necessary to ensure
     * that the SDK is parsing response from a known source. The SDK reads cache for proof-key
     * stored with the value of the state in the Uri. If a stored proof-key is found, the Uri
     * contains response from a request it generated.
     * Checks if the Uri contains an error query parameter. An error query parameter indicates
     * that the last request failed. This method invokes
     * {@link SDK_AuthHandler#onFailure(Exception)} callback to report failure.
     * When the above tests succeed, this method makes an http call to Amazon Cognito token
     * end-point to exchange code for tokens.
     * </p>
     *
     * @param uri      Required: The redirect uri from the service.
     * @param callback Required: {@link SDK_AuthHandler}.
     */
    private void getTokens(final Uri uri, final SDK_AuthHandler callback) {
        new Thread(new Runnable() {
            final Handler handler = new Handler(context.getMainLooper());
            Runnable returnCallback = new Runnable() {
                @Override
                public void run() {
                    callback.onFailure(new InvalidParameterException());
                }
            };

            @Override
            public void run() {
                final Uri fqdn = new Uri.Builder()
                        .scheme(SDK_ClientConstants.DOMAIN_SCHEME)
                        .authority(pool.getAppWebDomain())
                        .appendPath(SDK_ClientConstants.DOMAIN_PATH_OAUTH2)
                        .appendPath(SDK_ClientConstants.DOMAIN_PATH_TOKEN_ENDPOINT)
                        .build();

                String callbackState =
                        uri.getQueryParameter(SDK_ClientConstants.DOMAIN_QUERY_PARAM_STATE);

                if (callbackState != null) {
                    Set<String> tokenScopes = SDK_LocalDataManager.getCachedScopes(pool.awsKeyValueStore, context, callbackState);
                    String proofKeyPlain = SDK_LocalDataManager.getCachedProofKey(pool.awsKeyValueStore, context, callbackState);

                    if (proofKeyPlain == null) {
                        // The state value is unknown, exit.
                        return;
                    }

                    final String errorText =
                            uri.getQueryParameter(SDK_ClientConstants.DOMAIN_QUERY_PARAM_ERROR);

                    if (errorText != null) {
                        returnCallback = new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure(new SDKAuthServiceException(errorText));
                            }
                        };
                    } else {
                        // Make http POST call
                        final SDK_AuthHttpClient httpClient = new SDK_AuthHttpClient();
                        Map<String, String> httpHeaderParams = getHttpHeader();
                        Map<String, String> httpBodyParams = generateTokenExchangeRequest(uri, proofKeyPlain);

                        try {
                            String response =
                                    httpClient.httpPost(new URL(fqdn.toString()), httpHeaderParams, httpBodyParams);
                            final SDK_AuthUserSession session = SDK_AuthHttpResponseParser.parseHttpResponse(response);
                            userId = session.getUsername();

                            // Cache tokens if successful
                            SDK_LocalDataManager.cacheSession(pool.awsKeyValueStore, context, pool.getAppId(), userId, session, tokenScopes);

                            // Return tokens
                            returnCallback = new Runnable() {
                                @Override
                                public void run() {
                                    callback.onSuccess(session);
                                }
                            };
                        } catch (final Exception e) {
                            returnCallback = new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFailure(e);
                                }
                            };
                        }
                    }
                } else {
                    // User sign-out.
                    returnCallback = new Runnable() {
                        @Override
                        public void run() {
                            callback.onSignout();
                        }
                    };
                }
                handler.post(returnCallback);
            }
        }).start();
    }

    /**
     * Internal method to refresh tokens.
     * <p>
     * Makes an http call to Amazon Cognito token end-point to refresh token. On successful
     * token refresh, the refresh tokens is retained.
     * </p>
     *
     * @param session     Required: The android application {@link Context}.
     * @param redirectUri Required: The redirect Uri, which will be launched after authentication.
     * @param tokenScopes Required: A {@link Set<String>} specifying all scopes for the tokens.
     * @param callback    Required: {@link SDK_AuthHandler}.
     */
    private void refreshSession(final SDK_AuthUserSession session,
                                final String redirectUri,
                                final Set<String> tokenScopes,
                                final SDK_AuthHandler callback) {
        new Thread(new Runnable() {
            final Handler handler = new Handler(context.getMainLooper());
            Runnable returnCallback = new Runnable() {
                @Override
                public void run() {
                    launchCognitoAuth(redirectUri, tokenScopes);
                }
            };

            @Override
            public void run() {
                final Uri fqdn = new Uri.Builder()
                        .scheme(SDK_ClientConstants.DOMAIN_SCHEME)
                        .authority(pool.getAppWebDomain())
                        .appendPath(SDK_ClientConstants.DOMAIN_PATH_OAUTH2)
                        .appendPath(SDK_ClientConstants.DOMAIN_PATH_TOKEN_ENDPOINT)
                        .build();

                // Make http POST call
                final SDK_AuthHttpClient httpClient = new SDK_AuthHttpClient();
                Map<String, String> httpHeaderParams = getHttpHeader();
                Map<String, String> httpBodyParams = generateTokenRefreshRequest(redirectUri, session);

                try {
                    String response =
                            httpClient.httpPost(new URL(fqdn.toString()), httpHeaderParams, httpBodyParams);
                    SDK_AuthUserSession parsedSession = SDK_AuthHttpResponseParser.parseHttpResponse(response);
                    final SDK_AuthUserSession refreshedSession = new SDK_AuthUserSession(
                            parsedSession.getIdToken(),
                            parsedSession.getAccessToken(),
                            session.getRefreshToken());
                    final String username = refreshedSession.getUsername();

                    // Cache session
                    SDK_LocalDataManager.cacheSession(pool.awsKeyValueStore, context, pool.getAppId(), username, refreshedSession, pool.getScopes());

                    // Return tokens
                    returnCallback = new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(refreshedSession);
                        }
                    };
                } catch (final SDKAuthInvalidGrantException invg) {
                    returnCallback = new Runnable() {
                        @Override
                        public void run() {
                            launchCognitoAuth(redirectUri, tokenScopes);
                        }
                    };
                } catch (final Exception e) {
                    returnCallback = new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(e);
                        }
                    };
                }
                handler.post(returnCallback);
            }
        }).start();
    }

    /**
     * Generates header for the http request.
     *
     * @return Header parameters as a {@link Map<String, String>}.
     */
    private Map<String, String> getHttpHeader() {
        Map<String, String> httpHeaderParams = new HashMap<String, String>();
        httpHeaderParams.put(SDK_ClientConstants.HTTP_HEADER_PROP_CONTENT_TYPE,
                SDK_ClientConstants.HTTP_HEADER_PROP_CONTENT_TYPE_DEFAULT);

        // Add authorization header if the App Id has an associated Secret
        if (pool.getAppSecret() != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(pool.getAppId()).append(":").append(pool.getAppSecret());
            httpHeaderParams.put(SDK_ClientConstants.HTTP_HEADER_TYPE_AUTHORIZE, "Basic "
                    + SDK_Pkce.encodeBase64(builder.toString()));
        }
        return httpHeaderParams;
    }

    /**
     * Generates http body for token exchange.
     *
     * @param redirectUri Required: redirect_uri for token exchange.
     * @param proofKey    Required: The proof key for tokens.
     * @return Http request as a {@link Map<String, String>}.
     */
    private Map<String, String> generateTokenExchangeRequest(final Uri redirectUri,
                                                             final String proofKey) {
        Map<String, String> httpBodyParams = new HashMap<String, String>();
        httpBodyParams.put(SDK_ClientConstants.TOKEN_GRANT_TYPE,
                SDK_ClientConstants.TOKEN_GRANT_TYPE_AUTH_CODE);
        httpBodyParams.put(SDK_ClientConstants.DOMAIN_QUERY_PARAM_CLIENT_ID, pool.getAppId());
        httpBodyParams.put(SDK_ClientConstants.DOMAIN_QUERY_PARAM_REDIRECT_URI, pool.getSignInRedirectUri());
        httpBodyParams.put(SDK_ClientConstants.DOMAIN_QUERY_PARAM_CODE_VERIFIER, proofKey);
        httpBodyParams.put(SDK_ClientConstants.TOKEN_AUTH_TYPE_CODE,
                redirectUri.getQueryParameter(SDK_ClientConstants.TOKEN_AUTH_TYPE_CODE));
        return httpBodyParams;
    }

    /**
     * Generates http body for token refresh.
     *
     * @param redirectUri Required: redirect_uri for token refresh.
     * @param session     Required: User session containing the refresh token.
     * @return Http request as a {@link Map<String, String>}.
     */
    private Map<String, String> generateTokenRefreshRequest(final String redirectUri,
                                                            final SDK_AuthUserSession session) {
        Map<String, String> httpBodyParams = new HashMap<String, String>();
        httpBodyParams.put(SDK_ClientConstants.TOKEN_GRANT_TYPE, SDK_ClientConstants.HTTP_REQUEST_REFRESH_TOKEN);
        httpBodyParams.put(SDK_ClientConstants.DOMAIN_QUERY_PARAM_REDIRECT_URI, redirectUri);
        httpBodyParams.put(SDK_ClientConstants.DOMAIN_QUERY_PARAM_CLIENT_ID, pool.getAppId());
        httpBodyParams.put(SDK_ClientConstants.HTTP_REQUEST_REFRESH_TOKEN, session.getRefreshToken().getToken());
        final String userContextData = getUserContextData();
        if (userContextData != null) {
            httpBodyParams.put(SDK_ClientConstants.DOMAIN_QUERY_PARAM_USERCONTEXTDATA, userContextData);
        }
        return httpBodyParams;
    }

    /**
     * Creates the FQDM for Cognito's authentication endpoint and launches Cognito Auth web-domain.
     *
     * @param redirectUri Required: The redirect Uri, which will be launched after authentication.
     * @param tokenScopes Required: A {@link Set<String>} specifying all scopes for the tokens.
     */
    private void launchCognitoAuth(final String redirectUri, final Set<String> tokenScopes) {
        // Build the complete web domain to launch the login screen
        Uri.Builder builder = new Uri.Builder()
                .scheme(SDK_ClientConstants.DOMAIN_SCHEME)
                .authority(pool.getAppWebDomain())
                .appendPath(SDK_ClientConstants.DOMAIN_PATH_OAUTH2)
                .appendPath(SDK_ClientConstants.DOMAIN_PATH_SIGN_IN)
                .appendQueryParameter(SDK_ClientConstants.DOMAIN_QUERY_PARAM_CLIENT_ID, pool.getAppId())
                .appendQueryParameter(SDK_ClientConstants.DOMAIN_QUERY_PARAM_REDIRECT_URI, redirectUri)
                .appendQueryParameter(SDK_ClientConstants.DOMAIN_QUERY_PARAM_RESPONSE_TYPE,
                        SDK_ClientConstants.AUTH_RESPONSE_TYPE_CODE)
                .appendQueryParameter(SDK_ClientConstants.DOMAIN_QUERY_PARAM_CODE_CHALLENGE, proofKeyHash)
                .appendQueryParameter(SDK_ClientConstants.DOMAIN_QUERY_PARAM_CODE_CHALLENGE_METHOD,
                        SDK_ClientConstants.DOMAIN_QUERY_PARAM_CODE_CHALLENGE_METHOD_SHA256)
                .appendQueryParameter(SDK_ClientConstants.DOMAIN_QUERY_PARAM_STATE, state)
                .appendQueryParameter(SDK_ClientConstants.DOMAIN_QUERY_PARAM_USERCONTEXTDATA, getUserContextData());

        //check if identity provider set as param.
        if (!TextUtils.isEmpty(pool.getIdentityProvider())) {
            builder.appendQueryParameter(SDK_ClientConstants.DOMAIN_QUERY_PARAM_IDENTITY_PROVIDER, pool.getIdentityProvider());
        }
        //check if idp identifier set as param.
        if (!TextUtils.isEmpty(pool.getIdpIdentifier())) {
            builder.appendQueryParameter(SDK_ClientConstants.DOMAIN_QUERY_PARAM_IDP_IDENTIFIER, pool.getIdpIdentifier());
        }

        // Convert scopes into a string of comma separated values.
        final int noOfScopes = tokenScopes.size();
        if (noOfScopes > 0) {
            StringBuilder strBuilder = new StringBuilder();
            int index = 0;
            for (String scope : tokenScopes) {
                strBuilder.append(scope);
                if (index++ < noOfScopes - 1) {
                    strBuilder.append(" ");
                }
            }
            final String scopesStr = strBuilder.toString();
            builder.appendQueryParameter(SDK_ClientConstants.DOMAIN_QUERY_PARAM_SCOPES, scopesStr);
        }

        final Uri fqdn = builder.build();
        SDK_LocalDataManager.cacheState(pool.awsKeyValueStore, context, state, proofKey, tokenScopes);
        launchCustomTabs(fqdn);
    }

    /**
     * Creates the FQDM for Cognito's sign-out endpoint and launches Cognito Auth Web-Domain to
     * sign-out.
     *
     * @param redirectUri Required: The redirect Uri, which will be launched after authentication.
     */
    private void launchSignOut(final String redirectUri) {
        Uri.Builder builder = new Uri.Builder()
                .scheme(SDK_ClientConstants.DOMAIN_SCHEME)
                .authority(pool.getAppWebDomain()).appendPath(SDK_ClientConstants.DOMAIN_PATH_SIGN_OUT)
                .appendQueryParameter(SDK_ClientConstants.DOMAIN_QUERY_PARAM_CLIENT_ID, pool.getAppId())
                .appendQueryParameter(SDK_ClientConstants.DOMAIN_QUERY_PARAM_LOGOUT_URI, redirectUri);
        final Uri fqdn = builder.build();
        launchCustomTabs(fqdn);
    }

    /**
     * Launches App's Cognito webpage on Chrome Tab.
     *
     * @param uri Required: {@link Uri}.
     */
    private void launchCustomTabs(final Uri uri) {
        try {
            SDK_LocalDataManager.cacheHasReceivedRedirect(pool.awsKeyValueStore, context, pool.getAppId(), false);

            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(mCustomTabsSession);
            mCustomTabsIntent = builder.build();
            if (pool.getCustomTabExtras() != null)
                mCustomTabsIntent.intent.putExtras(pool.getCustomTabExtras());
            mCustomTabsIntent.intent.setPackage(SDK_ClientConstants.CHROME_PACKAGE);
            mCustomTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            mCustomTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mCustomTabsIntent.launchUrl(context, uri);
        } catch (final Exception e) {
            userHandler.onFailure(e);
        }
    }

    private String getUserContextData() {
        String userContextData = null;
        if (pool.isAdvancedSecurityDataCollectionEnabled()) {
            UserContextDataProvider dataProvider = UserContextDataProvider.getInstance();
            userContextData = dataProvider.getEncodedContextData(this.context, userId, pool.getUserPoolId(),
                    pool.getAppId());
        }
        return userContextData;
    }

    /**
     * Connects to Chrome Service on the device.
     */
    private void preWarmChrome() {
        mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(final ComponentName name, final CustomTabsClient client) {
                mCustomTabsClient = client;
                mCustomTabsClient.warmup(0L);
                mCustomTabsSession = mCustomTabsClient.newSession(customTabsCallback);
            }

            @Override
            public void onServiceDisconnected(final ComponentName name) {
                mCustomTabsClient = null;
            }
        };
        boolean chromeState = CustomTabsClient.bindCustomTabsService(context,
                SDK_ClientConstants.CHROME_PACKAGE, mCustomTabsServiceConnection);
    }
}

