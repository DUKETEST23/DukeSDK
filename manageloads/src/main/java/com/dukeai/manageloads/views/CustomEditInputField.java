package com.dukeai.manageloads.views;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.dukeai.manageloads.R;
import com.dukeai.manageloads.utils.AppConstants;
import com.dukeai.manageloads.utils.InputValidators;
import com.dukeai.manageloads.utils.UIMessages;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CustomEditInputField extends TextInputEditText implements TextWatcher, View.OnFocusChangeListener{
    TextInputLayout textInputLayout;
    Context ctx;
    int parentRef;
    String msg;

    public CustomEditInputField(final Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        ctx = context;
        final TypedArray array = getContext().obtainStyledAttributes(attributeSet, R.styleable.CustomEditInputField);
        parentRef = array.getResourceId(R.styleable.CustomEditInputField_parent_wrapper_id,
                -1);
        this.addTextChangedListener(this);
        this.setOnFocusChangeListener(this);


    }

    public CustomEditInputField(final Context context) {
        super(context);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            Boolean isValid = validateField(this);
            if (isValid)
                msg = null;
            else {
                setFormatErrorMessage(this.getTag().toString());
            }
            showErrorMessage(msg);
        }
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            String viewTag = v.getTag().toString();
            if (viewTag.equals("nonemptydate")) {
            } else {
                validateField(v);

            }

        }
    }


    public boolean validateField(View v) {
        String viewTag = v.getTag().toString();
        Boolean isValid = InputValidators.validateInput(viewTag, ((CustomEditInputField) v).getText().toString());
        String msg;
        if (isValid) {
            msg = null;
        } else {
            msg = "! " + UIMessages.getMessage(AppConstants.StringConstants.ERROR, viewTag,getContext());
        }

//        if(textInputLayout.getError() == null)
        showErrorMessage(msg);

        return isValid;
    }

    public void showErrorMessage(String msg) {

        if (textInputLayout == null) {
            textInputLayout = findViewById(parentRef);
        }

        if (textInputLayout != null) {
            textInputLayout.setError(msg);
        }
    }

    private void setFormatErrorMessage(String s) {
        msg = "! ";
        if (s.equalsIgnoreCase(AppConstants.StringConstants.EMAIL))
            msg += getResources().getString(R.string.email_format_error);
        else if (s.equalsIgnoreCase(AppConstants.StringConstants.PASSWORD))
            msg += getResources().getString(R.string.password_format_error);
        else if (s.equalsIgnoreCase(AppConstants.StringConstants.PHONE))
            msg += getResources().getString(R.string.phone_number_format_error);
        else if (s.equalsIgnoreCase(AppConstants.StringConstants.CODE))
            msg += getResources().getString(R.string.code_format_error);
        else if (s.equalsIgnoreCase(AppConstants.StringConstants.USERNAME))
            msg += getResources().getString(R.string.please_enter_valid_username);
        else if (s.equalsIgnoreCase(AppConstants.StringConstants.PROMO_CODE))
            msg += getResources().getString(R.string.please_enter_valid_promo_code);
        else if (s.equalsIgnoreCase(AppConstants.StringConstants.ALPHA_NUMERIC))
            msg += getResources().getString(R.string.please_enter_alpha_numeric_value);
        else if (s.equalsIgnoreCase(AppConstants.StringConstants.NON_EMPTY))
            msg += getResources().getString(R.string.please_enter_value);
        else if (s.equalsIgnoreCase(AppConstants.StringConstants.DECIMAL_NUM))
            msg += "Please enter amount";
        else if (s.equalsIgnoreCase(AppConstants.StringConstants.DESC))
            msg += "Please enter value";
        else if (s.equalsIgnoreCase(AppConstants.StringConstants.DATE_NON_EMPTY))
            msg += "Please enter date";
        else if (s.equalsIgnoreCase(AppConstants.StringConstants.DESC_EMPTY))
            msg += getResources().getString(R.string.please_enter_value);
        else if (s.equalsIgnoreCase(AppConstants.StringConstants.COMPANY_NAME))
            msg += getResources().getString(R.string.valid_company_name);
        else if (s.equalsIgnoreCase(AppConstants.StringConstants.ADDRESS))
            msg +="";
//        else if(s.equalsIgnoreCase(AppConstants.StringConstants.DECIMAL_NUM))
//            msg+=getResources().getString(R.string.please_enter_value);
    }


}
