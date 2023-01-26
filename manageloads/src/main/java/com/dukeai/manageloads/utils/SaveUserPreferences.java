package com.dukeai.manageloads.utils;

import android.os.AsyncTask;

import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.model.UserDataModel;
import com.google.gson.Gson;

public class SaveUserPreferences extends AsyncTask<UserDataModel, Void, Void> {

    @Override
    protected Void doInBackground(UserDataModel... userDataModels) {
        UserDataModel userDataModel = userDataModels[0];
        Gson gson = new Gson();
        String userInfoListJsonString = gson.toJson(userDataModel);
        if (userDataModel != null && userDataModel.getLoggedIn() != null && !userDataModel.getLoggedIn()) {
            PreferenceManager.saveString(Duke.getInstance(), AppConstants.UserPreferencesConstants.LOGGED_IN, null);
        } else {
            PreferenceManager.saveString(Duke.getInstance(), AppConstants.UserPreferencesConstants.LOGGED_IN, AppConstants.UserPreferencesConstants.USER_LOGGEDIN);
        }
        PreferenceManager.saveString(Duke.getInstance(), AppConstants.UserPreferencesConstants.USER_SESSION, userInfoListJsonString);
        return null;
    }
}
