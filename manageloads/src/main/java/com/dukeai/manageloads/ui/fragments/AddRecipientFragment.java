package com.dukeai.manageloads.ui.fragments;


import static com.dukeai.manageloads.Duke.loadsDocuments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.R;
import com.dukeai.manageloads.interfaces.HeaderActions;
import com.dukeai.manageloads.model.RecipientDataModel;
import com.dukeai.manageloads.utils.AppConstants;
import com.dukeai.manageloads.utils.CustomToolbar;
import com.dukeai.manageloads.utils.InputValidators;
import com.dukeai.manageloads.utils.NavigationFlowManager;
import com.dukeai.manageloads.utils.UIMessages;
import com.dukeai.manageloads.viewmodel.GenericResponseModel;
import com.dukeai.manageloads.viewmodel.LoadsViewModel;
import com.dukeai.manageloads.views.CustomEditInputField;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;


public class AddRecipientFragment extends Fragment implements HeaderActions {


    CustomToolbar headers;
    View addRecipientsView;
    private TextInputLayout companyNameField;
    private TextInputLayout emailField;
    private TextInputEditText addressField;
    private TextInputEditText phoneField;
    private TextInputEditText contactField;
    TextView deleteRecipient;
    String originalEmail;
    Button recipientBtn;
    LoadsViewModel loadsViewModel;
    RecipientDataModel data;
    String updateMode = "ADD";


    public AddRecipientFragment() {
        // Required empty public constructor
    }

    public static AddRecipientFragment newInstance() {
        AddRecipientFragment fragment = new AddRecipientFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addRecipientsView = inflater.inflate(R.layout.fragment_add_recipient, container, false);
        loadsViewModel = ViewModelProviders.of(this).get(LoadsViewModel.class);
        initView(addRecipientsView);
        setCustomHeader(addRecipientsView);
        return addRecipientsView;
    }

    private void initView(View v) {

        companyNameField = v.findViewById(R.id.company_name_wrapper);
        emailField = v.findViewById(R.id.email_wrapper);
        addressField = v.findViewById(R.id.address_value);
        phoneField = v.findViewById(R.id.phone_value);
        contactField = v.findViewById(R.id.contact_value);
        deleteRecipient = v.findViewById(R.id.delete);

        recipientBtn = v.findViewById(R.id.submit);
        recipientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmitClicked(view);
            }
        });
        deleteRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClicked(view);
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setInitials();
    }

    private void setToolbarColor() {
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
    }

    private void setInitials() {
        prePopulateData(getArguments());
        setToolbarColor();
        setDeleteBtnVisibilty();
    }

    private void setDeleteBtnVisibilty() {
        if (updateMode.equals("update")) {
            deleteRecipient.setVisibility(View.VISIBLE);
        }
    }

    private void prePopulateData(Bundle bundle) {
        if (bundle != null && bundle.get(AppConstants.StringConstants.RECIPIENT_DATA_MODEL) != null) {
            data = (RecipientDataModel) bundle.getSerializable(AppConstants.StringConstants.RECIPIENT_DATA_MODEL);
            if (bundle.get(AppConstants.StringConstants.ACTION) != null && bundle.get(AppConstants.StringConstants.ACTION).equals(AppConstants.StringConstants.ACTION_TYPE_UPDATE)) {
                companyNameField.getEditText().setText(data.getCompanyName());
                emailField.getEditText().setText(data.getEmail());
                originalEmail = data.getEmail();
                addressField.setText(data.getAddress());
                phoneField.setText(data.getPhone());
                contactField.setText(data.getContact());
                headers.setToolbarTitle("Edit  Recipient");
                recipientBtn.setText("Update");
            }
            updateMode = "update";
        }

        if (bundle != null && bundle.get(AppConstants.StringConstants.ACTION).equals(AppConstants.StringConstants.ACTION_TYPE_ADD)) {
            headers.setToolbarTitle("Add  Recipient");
        }
    }

    public void setCustomHeader(View view) {
        headers = new CustomToolbar(getContext());
        headers.initViews(view);
        headers.hideHeaderImage();
        headers.hideHeaderLable();
        headers.setHeaderTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        headers.setBackButtonTint(ContextCompat.getColor(getContext(), R.color.colorWhite));
        View add = view.findViewById(R.id.add_recipient_header);
        add.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
        ImageView back =  add.findViewById(R.id.toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationFlowManager.addNewFragment(Duke.loadsFragment, null, getActivity(), R.id.dashboard_wrapper);
//                getActivity().onBackPressed();
            }
        });
    }

    public boolean validateField(View v,View v1) {
        String viewTag = v1.getTag().toString();
        Boolean isValid = InputValidators.validateInput(viewTag, ((TextInputLayout) v).getEditText().getText().toString());
        String msg;
        if (isValid) {
            msg = null;
        } else {
            msg = "! " + UIMessages.getMessage(AppConstants.StringConstants.ERROR, viewTag,getContext());
        }
        ((TextInputLayout)v).setError(msg);
        return isValid;
    }

    //    TODO: onSubmit click
    void onSubmitClicked(View v) {
        boolean isCompanyNameValid = validateField(companyNameField,companyNameField.getEditText());
        boolean isEmailIdValid = validateField(emailField,emailField.getEditText());


        if (isCompanyNameValid && isEmailIdValid) {
            String flag = "";
            if (updateMode.equals("update")) {
                flag = "update";
            } else {
                flag = "add";
            }

            JsonObject payload = new JsonObject();
            payload.addProperty("app_version", "2.3.7");
            payload.addProperty("flag", flag);
            if (flag.equals("update")) {
                payload.addProperty("original_email", originalEmail);
            }
            payload.addProperty("email", emailField.getEditText().getText().toString());
            payload.addProperty("company_name", companyNameField.getEditText().getText().toString());
            payload.addProperty("address", addressField.getText().toString().length() > 0 ? addressField.getText().toString() : " ");
            payload.addProperty("phone", phoneField.getText().toString().length() > 0 ? phoneField.getText().toString() : " ");
            payload.addProperty("contact", contactField.getText().toString().length() > 0 ? contactField.getText().toString() : " ");

            loadsViewModel.getUpdateRecipientsLiveData(payload,getContext()).observe(this, new Observer<GenericResponseModel>() {
                @Override
                public void onChanged(GenericResponseModel genericResponseModel) {
                    if (genericResponseModel != null && genericResponseModel.getStatus() != null && genericResponseModel.getStatus().toLowerCase().equals("success")) {
                        Toast.makeText(getActivity(), genericResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                        if (Duke.selectedRecipients.contains(originalEmail)) {
                            Duke.selectedRecipients.remove(originalEmail);
                        }
                        Bundle args = new Bundle();
                        args.putString(AppConstants.StringConstants.NAVIGATE_TO, AppConstants.StringConstants.LOADS);
                        NavigationFlowManager.addNewFragment(Duke.loadsFragment, args, getActivity(), R.id.dashboard_wrapper);
//                        NavigationFlowManager.openFragments(Duke.loadsFragment, args, getActivity(), R.id.dashboard_wrapper);
                    } else {
                        Toast.makeText(getActivity(), genericResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                        Bundle args = new Bundle();
                        args.putString(AppConstants.StringConstants.NAVIGATE_TO, AppConstants.StringConstants.LOADS);
                        NavigationFlowManager.addNewFragment(Duke.loadsFragment, args, getActivity(), R.id.dashboard_wrapper);
//                        NavigationFlowManager.openFragments(Duke.loadsFragment, args, getActivity(), R.id.dashboard_wrapper);
                    }
                }
            });
        }
    }

    //    TODO: on Delete click
    void onDeleteClicked(View v) {
        JsonObject payload = new JsonObject();
        payload.addProperty("app_version", "2.3.7");
        payload.addProperty("flag", "delete");
        payload.addProperty("email", emailField.getEditText().getText().toString());
        loadsViewModel.getUpdateRecipientsLiveData(payload,getContext()).observe(this, new Observer<GenericResponseModel>() {
            @Override
            public void onChanged(GenericResponseModel genericResponseModel) {
                if (genericResponseModel != null && genericResponseModel.getStatus() != null && genericResponseModel.getStatus().toLowerCase().equals("success")) {
                    Toast.makeText(getActivity(), genericResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    Bundle args = new Bundle();
                    if (Duke.selectedRecipients.contains(emailField.getEditText().getText().toString())) {
                        Duke.selectedRecipients.remove(emailField.getEditText().getText().toString());
                    }

                    if (Duke.restRecipients.contains(emailField.getEditText().getText().toString())) {
                        Duke.restRecipients.remove(emailField.getEditText().getText().toString());
                    }

                    args.putString(AppConstants.StringConstants.NAVIGATE_TO, AppConstants.StringConstants.LOADS);
                    /*NavigationFlowManager.addNewFragment(Duke.loadsFragment, args, getActivity(), R.id.dashboard_wrapper);*/
/*
                    OnBackPressedCallback callback = new OnBackPressedCallback(true *//* enabled by default *//*) {
                        @Override
                        public void handleOnBackPressed() {
                            // Handle the back button event
                            getActivity().finish();
                        }
                    };
                    requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);*/

                    NavigationFlowManager.openFragments(Duke.loadsFragment, args, getActivity(), R.id.dashboard_wrapper);
                }
            }
        });
    }

    @Override
    public void onClickProfile() {

    }

    @Override
    public void onBackClicked() {
        getActivity().onBackPressed();
    }

    @Override
    public void onClickToolbarTitle() {

    }

    @Override
    public void onClickHeaderTitle() {

    }

}