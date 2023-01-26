package com.dukeai.manageloads.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Validator {
    TextInputEditText textInputLayout;
    Context context;
    public boolean validateField(View v) {
        String viewTag = v.getTag().toString();
        Boolean isValid = InputValidators.validateInput(viewTag, ((TextInputEditText) v).getText().toString());
        String msg;
        if (isValid) {
            msg = null;
        } else {
            msg = "! " + UIMessages.getMessage(AppConstants.StringConstants.ERROR, viewTag, v.getContext());
        }

//        if(textInputLayout.getError() == null)
        showErrorMessage(msg,v.getId());

        return isValid;
    }

    public  void showErrorMessage(String msg, int id) {

        if (textInputLayout == null) {
            textInputLayout = ((Activity) context).findViewById(id);
        }

        if (textInputLayout != null) {
            textInputLayout.setError(msg);
        }
    }


}
