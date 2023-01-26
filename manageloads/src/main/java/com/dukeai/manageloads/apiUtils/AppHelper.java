package com.dukeai.manageloads.apiUtils;

import android.content.Context;
import android.graphics.Color;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.regions.Regions;
import com.dukeai.manageloads.model.ItemToDisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AppHelper {

    private static final String TAG = "AppHelper";
    // App settings
    private static final String userPoolId = ApiConstants.USER_POOL_ID;
    private static final String clientId = ApiConstants.CLIENT_ID;
    private static final String clientSecret = ApiConstants.CLIENT_SECRET;
    private static final Regions cognitoRegion = ApiConstants.COGNITO_REGION;
    private static List<String> attributeDisplaySeq;
    private static Map<String, String> signUpFieldsC2O;
    private static Map<String, String> signUpFieldsO2C;
    private static AppHelper appHelper;
    private static CognitoUserPool userPool;
    private static String user;
    private static List<ItemToDisplay> currDisplayedItems;
    private static int itemCount;
    private static List<ItemToDisplay> firstTimeLogInDetails;
    private static Map<String, String> firstTimeLogInUserAttributes;
    private static List<String> firstTimeLogInRequiredAttributes;
    private static int firstTimeLogInItemsCount;
    private static Map<String, String> firstTimeLogInUpDatedAttributes;
    private static String firstTimeLoginNewPassword;
    private static List<ItemToDisplay> mfaOptions;
    private static List<String> mfaAllOptionsCode;
    // User details from the service
    private static CognitoUserSession currSession;
    private static CognitoUserDetails userDetails;

    // User details to display - they are the current values, including any local modification
    private static boolean phoneVerified;
    private static boolean emailVerified;

    private static boolean phoneAvailable;
    private static boolean emailAvailable;

    private static Set<String> currUserAttributes;

    public static void init(Context context) {
        setData();

        if (appHelper != null && userPool != null) {
            return;
        }

        if (appHelper == null) {
            appHelper = new AppHelper();
        }

        if (userPool == null) {

            // Create a user pool with default ClientConfiguration
            userPool = new CognitoUserPool(context, userPoolId, clientId, clientSecret, cognitoRegion);

            // This will also work

//            ClientConfiguration clientConfiguration = new ClientConfiguration();
//            AmazonCognitoIdentityProvider cipClient = new AmazonCognitoIdentityProviderClient(new AnonymousAWSCredentials(), clientConfiguration);
//            cipClient.setRegion(Region.getRegion(cognitoRegion));
//            userPool = new CognitoUserPool(context, userPoolId, clientId, clientSecret, cipClient);


        }

        phoneVerified = false;
        phoneAvailable = false;
        emailVerified = false;
        emailAvailable = false;

        currUserAttributes = new HashSet<String>();
        currDisplayedItems = new ArrayList<ItemToDisplay>();
        firstTimeLogInDetails = new ArrayList<ItemToDisplay>();
        firstTimeLogInUpDatedAttributes = new HashMap<String, String>();

        mfaOptions = new ArrayList<ItemToDisplay>();
    }

    public static CognitoUserPool getPool() {
        return userPool;
    }

    public static Map<String, String> getSignUpFieldsC2O() {
        return signUpFieldsC2O;
    }

    public static Map<String, String> getSignUpFieldsO2C() {
        return signUpFieldsO2C;
    }

    public static List<String> getAttributeDisplaySeq() {
        return attributeDisplaySeq;
    }

    public static CognitoUserSession getCurrSession() {
        return currSession;
    }

    public static void setCurrSession(CognitoUserSession session) {
        currSession = session;
    }

    public static CognitoUserDetails getUserDetails() {
        return userDetails;
    }

    public static void setUserDetails(CognitoUserDetails details) {
        userDetails = details;
        refreshWithSync();
    }

    public static String getCurrUser() {
        return user;
    }

    public static void setUser(String newUser) {
        user = newUser;
    }

    public static boolean isPhoneVerified() {
        return phoneVerified;
    }

    public static void setPhoneVerified(boolean phoneVerif) {
        phoneVerified = phoneVerif;
    }

    public static boolean isEmailVerified() {
        return emailVerified;
    }

    public static void setEmailVerified(boolean emailVerif) {
        emailVerified = emailVerif;
    }

    public static boolean isPhoneAvailable() {
        return phoneAvailable;
    }

    public static void setPhoneAvailable(boolean phoneAvail) {
        phoneAvailable = phoneAvail;
    }

    public static boolean isEmailAvailable() {
        return emailAvailable;
    }

    public static void setEmailAvailable(boolean emailAvail) {
        emailAvailable = emailAvail;
    }

    public static void clearCurrUserAttributes() {
        currUserAttributes.clear();
    }

    public static void addCurrUserattribute(String attribute) {
        currUserAttributes.add(attribute);
    }

    public static List<String> getNewAvailableOptions() {
        List<String> newOption = new ArrayList<String>();
        for (String attribute : attributeDisplaySeq) {
            if (!(currUserAttributes.contains(attribute))) {
                newOption.add(attribute);
            }
        }
        return newOption;
    }

    public static String formatException(Exception exception) {
        String formattedString = "Internal Error";
        String temp = exception.getMessage();

        if (temp != null && temp.length() > 0) {
            formattedString = temp.split("\\(")[0];
            if (temp != null && temp.length() > 0) {
                return formattedString;
            }
        }

        return formattedString;
    }

    public static int getItemCount() {
        return itemCount;
    }


    public static int getFirstTimeLogInItemsCount() {
        return firstTimeLogInItemsCount;
    }

    public static ItemToDisplay getItemForDisplay(int position) {
        return currDisplayedItems.get(position);
    }


    public static ItemToDisplay getUserAttributeForFirstLogInCheck(int position) {
        return firstTimeLogInDetails.get(position);
    }

    public static void setUserAttributeForDisplayFirstLogIn(Map<String, String> currAttributes, List<String> requiredAttributes) {
        firstTimeLogInUserAttributes = currAttributes;
        firstTimeLogInRequiredAttributes = requiredAttributes;
        firstTimeLogInUpDatedAttributes = new HashMap<String, String>();
        refreshDisplayItemsForFirstTimeLogin();
    }

    public static void setUserAttributeForFirstTimeLogin(String attributeName, String attributeValue) {
        if (firstTimeLogInUserAttributes == null) {
            firstTimeLogInUserAttributes = new HashMap<String, String>();
        }
        firstTimeLogInUserAttributes.put(attributeName, attributeValue);
        firstTimeLogInUpDatedAttributes.put(attributeName, attributeValue);
        refreshDisplayItemsForFirstTimeLogin();
    }

    public static Map<String, String> getUserAttributesForFirstTimeLogin() {
        return firstTimeLogInUpDatedAttributes;
    }

    public static String getPasswordForFirstTimeLogin() {
        return firstTimeLoginNewPassword;
    }

    public static void setPasswordForFirstTimeLogin(String password) {
        firstTimeLoginNewPassword = password;
    }

    private static void refreshDisplayItemsForFirstTimeLogin() {
        firstTimeLogInItemsCount = 0;
        firstTimeLogInDetails = new ArrayList<ItemToDisplay>();

        for (Map.Entry<String, String> attr : firstTimeLogInUserAttributes.entrySet()) {
            if ("phone_number_verified".equals(attr.getKey()) || "email_verified".equals(attr.getKey())) {
                continue;
            }
            String message = "";
            if ((firstTimeLogInRequiredAttributes != null) && (firstTimeLogInRequiredAttributes.contains(attr.getKey()))) {
                message = "Required";
            }
            ItemToDisplay item = new ItemToDisplay(attr.getKey(), attr.getValue(), message, Color.BLACK, Color.DKGRAY, Color.parseColor("#329AD6"), 0, null);
            firstTimeLogInDetails.add(item);
            firstTimeLogInRequiredAttributes.size();
            firstTimeLogInItemsCount++;
        }

        for (String attr : firstTimeLogInRequiredAttributes) {
            if (!firstTimeLogInUserAttributes.containsKey(attr)) {
                ItemToDisplay item = new ItemToDisplay(attr, "", "Required", Color.BLACK, Color.DKGRAY, Color.parseColor("#329AD6"), 0, null);
                firstTimeLogInDetails.add(item);
                firstTimeLogInItemsCount++;
            }
        }
    }

    public static void setMfaOptionsForDisplay(List<String> options, Map<String, String> parameters) {
        mfaAllOptionsCode = options;
        mfaOptions = new ArrayList<ItemToDisplay>();
        String textToDisplay = "";
        for (String option : options) {
            if ("SMS_MFA".equals(option)) {
                textToDisplay = "Send SMS";
                if (parameters.containsKey("CODE_DELIVERY_DESTINATION")) {
                    textToDisplay = textToDisplay + " to " + parameters.get("CODE_DELIVERY_DESTINATION");
                }
            } else if ("SOFTWARE_TOKEN_MFA".equals(option)) {
                textToDisplay = "Use TOTP";
                if (parameters.containsKey("FRIENDLY_DEVICE_NAME")) {
                    textToDisplay = textToDisplay + ": " + parameters.get("FRIENDLY_DEVICE_NAME");
                }
            }
            ItemToDisplay item = new ItemToDisplay("", textToDisplay, "", Color.BLACK, Color.DKGRAY, Color.parseColor("#329AD6"), 0, null);
            mfaOptions.add(item);
            textToDisplay = "Unsupported MFA";
        }
    }

    public static List<String> getAllMfaOptions() {
        return mfaAllOptionsCode;
    }

    public static String getMfaOptionCode(int position) {
        return mfaAllOptionsCode.get(position);
    }

    public static ItemToDisplay getMfaOptionForDisplay(int position) {
        if (position >= mfaOptions.size()) {
            return new ItemToDisplay(" ", " ", " ", Color.BLACK, Color.DKGRAY, Color.parseColor("#37A51C"), 0, null);
        }
        return mfaOptions.get(position);
    }

    public static int getMfaOptionsCount() {
        return mfaOptions.size();
    }

    //public static


    private static void setData() {
        // Set attribute display sequence
        attributeDisplaySeq = new ArrayList<String>();
        attributeDisplaySeq.add("name");
        attributeDisplaySeq.add("email");
        attributeDisplaySeq.add("phone_number");
        attributeDisplaySeq.add("password");
        attributeDisplaySeq.add("verificationCode");

        signUpFieldsC2O = new HashMap<String, String>();
        signUpFieldsC2O.put("Name", "name");
        signUpFieldsC2O.put("Email", "email");
        signUpFieldsC2O.put("Phone Number", "phone_number");
        signUpFieldsC2O.put("Password-signup", "password");
        signUpFieldsC2O.put("Verification Code", "verificationCode");
        signUpFieldsC2O.put("Phone number verified", "phone_number_verified");
        signUpFieldsC2O.put("Email verified", "email_verified");

        signUpFieldsO2C = new HashMap<String, String>();
        signUpFieldsO2C.put("name", "Name");
        signUpFieldsO2C.put("email", "Email");
        signUpFieldsO2C.put("phone_number", "Phone Number");
        signUpFieldsO2C.put("password", "Password-signup");
        signUpFieldsO2C.put("phone_number_verified", "Phone number verified");
        signUpFieldsO2C.put("email_verified", "Email verified");

    }

    private static void refreshWithSync() {
        // This will refresh the current items to display list with the attributes fetched from service
        List<String> tempKeys = new ArrayList<>();
        List<String> tempValues = new ArrayList<>();

        emailVerified = false;
        phoneVerified = false;

        emailAvailable = false;
        phoneAvailable = false;

        currDisplayedItems = new ArrayList<ItemToDisplay>();
        currUserAttributes.clear();
        itemCount = 0;

        for (Map.Entry<String, String> attr : userDetails.getAttributes().getAttributes().entrySet()) {

            tempKeys.add(attr.getKey());
            tempValues.add(attr.getValue());

            if (attr.getKey().contains("email_verified")) {
                emailVerified = attr.getValue().contains("true");
            } else if (attr.getKey().contains("phone_number_verified")) {
                phoneVerified = attr.getValue().contains("true");
            }

            if (attr.getKey().equals("email")) {
                emailAvailable = true;
            } else if (attr.getKey().equals("phone_number")) {
                phoneAvailable = true;
            }
        }

        // Arrange the input attributes per the display sequence
        Set<String> keySet = new HashSet<>(tempKeys);
        for (String det : attributeDisplaySeq) {
            if (keySet.contains(det)) {
                // Adding items to display list in the required sequence

                ItemToDisplay item = new ItemToDisplay(signUpFieldsO2C.get(det), tempValues.get(tempKeys.indexOf(det)), "",
                        Color.BLACK, Color.DKGRAY, Color.parseColor("#37A51C"),
                        0, null);

                if (det.contains("email")) {
                    if (emailVerified) {
                        item.setDataDrawable("checked");
                        item.setMessageText("Email verified");
                    } else {
                        item.setDataDrawable("not_checked");
                        item.setMessageText("Email not verified");
                        item.setMessageColor(Color.parseColor("#E94700"));
                    }
                }

                if (det.contains("phone_number")) {
                    if (phoneVerified) {
                        item.setDataDrawable("checked");
                        item.setMessageText("Phone number verified");
                    } else {
                        item.setDataDrawable("not_checked");
                        item.setMessageText("Phone number not verified");
                        item.setMessageColor(Color.parseColor("#E94700"));
                    }
                }

                currDisplayedItems.add(item);
                currUserAttributes.add(det);
                itemCount++;
            }
        }
    }

    private static void modifyAttribute(String attributeName, String newValue) {
        //

    }

    private static void deleteAttribute(String attributeName) {

    }
}
