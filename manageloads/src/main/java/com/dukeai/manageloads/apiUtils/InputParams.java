package com.dukeai.manageloads.apiUtils;


import com.dukeai.manageloads.model.UserDataModel;
import com.dukeai.manageloads.utils.UserConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class InputParams {

    public static JsonObject updateDeviceToken(String deviceToken, String promoCode) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ApiConstants.UpdateDeviceToken.DEVICE_TOKEN, deviceToken);
        if (promoCode != null && !promoCode.equals("none")) {
            jsonObject.addProperty(ApiConstants.UpdateDeviceToken.PROMO_CODE, promoCode);
        }
        UserDataModel userDataModel = UserConfig.getInstance().getUserDataModel();
        jsonObject.addProperty(ApiConstants.UpdateDeviceToken.JOIN_FROM, userDataModel.getCognitoUserName() == null ? ApiConstants.COGNITO : userDataModel.getCognitoUserName());
        jsonObject.addProperty(ApiConstants.UpdateDeviceToken.USER_NAME, ((userDataModel.getName() == null) || userDataModel.getName().equals("")) ? "none" : userDataModel.getName());
        jsonObject.addProperty(ApiConstants.UpdateDeviceToken.USER_PHONE, "none" /**Mark:- Replace it with mobile number**/);
        jsonObject.addProperty(ApiConstants.UpdateDeviceToken.OS, ApiConstants.UpdateDeviceToken.ANDROID);
        return jsonObject;
    }

    public static JsonObject deleteDeviceToken(String deviceToken) {
        JsonObject jsonObject = new JsonObject();
        if (deviceToken == null) {
            deviceToken = "none";
        }
        jsonObject.addProperty(ApiConstants.UpdateDeviceToken.DEVICE_TOKEN, deviceToken);
        jsonObject.addProperty(ApiConstants.UpdateDeviceToken.OS, ApiConstants.UpdateDeviceToken.ANDROID);
        return jsonObject;
    }

    public static JsonObject migrateUserToCognito(String password) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ApiConstants.MigrateUserToCognito.PASSWORD, password);
        return jsonObject;
    }

    public static JsonObject fileStatus(String from, String to, String numOfDocs) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ApiConstants.FileStatus.FROM, from);
        jsonObject.addProperty(ApiConstants.FileStatus.TO, to);
        jsonObject.addProperty(ApiConstants.FileStatus.NUMBER_OF_DOCS, numOfDocs);
        return jsonObject;
    }

    public static JsonObject getFinancialSummary(String from, String to) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ApiConstants.FinancialSummary.FROM, from);
        jsonObject.addProperty(ApiConstants.FinancialSummary.TO, to);
        return jsonObject;
    }

    public static JsonObject generateReport(String from, String to, String period, String reportType) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ApiConstants.GenerateReport.FROM, from);
        jsonObject.addProperty(ApiConstants.GenerateReport.TO, to);
        jsonObject.addProperty(ApiConstants.GenerateReport.PERIOD, period);
        jsonObject.addProperty(ApiConstants.GenerateReport.REPORT_TYPE, reportType);
        return jsonObject;
    }

    public static JsonObject updatePromoCode(String promoCode) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ApiConstants.PromoCode.PROMO_CODE, promoCode);
        return jsonObject;
    }

    public static JsonObject federalInfoData(String fillingStatus, JsonObject preTaxDeduction, JsonObject itemizeDeduction, JsonObject standardDeduction, JsonObject taxCredit, JsonObject otherIncome) {
        JsonObject jsonObject = new JsonObject();
        JsonObject dataObject = new JsonObject();
        jsonObject.addProperty("Filling_Status", fillingStatus);
        jsonObject.add("Itemize_Deduction", new Gson().toJsonTree(itemizeDeduction));
        jsonObject.add("Pre_tax_deduction", new Gson().toJsonTree(preTaxDeduction));
        jsonObject.add("Standard_Deduction", new Gson().toJsonTree(standardDeduction));
        jsonObject.add("Tax_credit", new Gson().toJsonTree(taxCredit));
        jsonObject.add("Other_Income", new Gson().toJsonTree(otherIncome));
        dataObject.add("data", new Gson().toJsonTree(jsonObject));
        return dataObject;
    }

    public static JsonObject assetInfoData(String uniqueId, String vinVal, Float valAmount, String dateVal, String desc, String assetType, float model, boolean deleteType) {
        JsonObject jsonObject = new JsonObject();
        JsonObject dataObject = new JsonObject();
        jsonObject.addProperty("internal_id", uniqueId);
        jsonObject.addProperty("description", desc);
        jsonObject.addProperty("model", model);
        jsonObject.addProperty("id", vinVal);
        jsonObject.addProperty("purchase_date", dateVal);
        jsonObject.addProperty("delete", deleteType);
        jsonObject.addProperty("assets_type", assetType);
        jsonObject.addProperty("value", valAmount);
        dataObject.add("data", new Gson().toJsonTree(jsonObject));
        return dataObject;

    }

    public static JsonObject balanceSheetInfo(Float amount, String desc, String uniqueId, String value, boolean b) {
        JsonObject jsonObject = new JsonObject();
        JsonObject dataObject = new JsonObject();

        jsonObject.addProperty("internal_id", uniqueId);
        jsonObject.addProperty("description", desc);
        jsonObject.addProperty("value", amount);
        jsonObject.addProperty("account_type", value);
        jsonObject.addProperty("delete", b);
        dataObject.add("data", new Gson().toJsonTree(jsonObject));

        return dataObject;
    }

    public static JsonObject updateUserGroup(String cognitoUserName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ApiConstants.SignUpApi.JOIN_FROM, cognitoUserName == null ? "none" : cognitoUserName);
        return jsonObject;
    }

    public static JsonObject downloadReport(String fileName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ApiConstants.DownloadFile.FILENAME, fileName);
        return jsonObject;
    }
}
