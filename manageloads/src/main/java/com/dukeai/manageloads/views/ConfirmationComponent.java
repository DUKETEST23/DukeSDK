package com.dukeai.manageloads.views;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.dukeai.manageloads.R;
import com.dukeai.manageloads.interfaces.PopupActions;
import com.dukeai.manageloads.utils.AppConstants;

import java.io.File;


public class ConfirmationComponent extends Dialog {
    PopupActions popupActions;
    TextView confirmationTitle;
    TextView confirmationMessage;
    Button neutralButton;
    Button positiveButton;
    Button negativeButton;
    Button openFileButton;
    TextView extraText;
    int DIALOG_ID;
    File path;

    public ConfirmationComponent(@NonNull Context context, String title, String message, Boolean isCancelable, String positiveBtnText, String negativeBtnText, PopupActions popupActions, int dialogId) {
        super(context);
        this.DIALOG_ID = dialogId;
        this.popupActions = popupActions;
        View view = View.inflate(context, R.layout.layout_confirmation_component, null);
        initView(view);
        setContentView(view);
        setInitialsForBoth(title, message, isCancelable, positiveBtnText, negativeBtnText);
    }

    private void initView(View view) {
        confirmationTitle = view.findViewById(R.id.confirmation_title);
        confirmationMessage = view.findViewById(R.id.confirmation_message);
        neutralButton = view.findViewById(R.id.neutral_button);
        neutralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNeutralButton();
            }
        });
        positiveButton = view.findViewById(R.id.positive_button);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickPositiveButton();
            }
        });
        negativeButton = view.findViewById(R.id.negative_button);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNegativeButton();
            }
        });
        openFileButton = view.findViewById(R.id.openFile);
        openFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickOpenFile();
            }
        });
    }

    public ConfirmationComponent(@NonNull Context context, String title, String message, Boolean isCancelable, String positiveBtnText, String negativeBtnText, PopupActions popupActions, int dialogId, String extraInfo) {
        super(context);
        this.DIALOG_ID = dialogId;
        this.popupActions = popupActions;
        View view = View.inflate(context, R.layout.layout_confirmation_component_with_extra_text, null);
        setContentView(view);
        setInitialsForBoth(title, message, isCancelable, positiveBtnText, negativeBtnText, extraInfo);
    }

    public ConfirmationComponent(Context context, String title, String message,
                                 Boolean isCancelable, String neutralBtn,
                                 PopupActions popupActions, int dialogId) {
        super(context);
        this.DIALOG_ID = dialogId;
        this.popupActions = popupActions;
        View view = View.inflate(context, R.layout.layout_confirmation_component, null);
        setContentView(view);
        setInitialsForNeutralBtn(title, message, isCancelable, neutralBtn);
    }

    public ConfirmationComponent(Context context, String title, String message, Boolean isCancelable, String neutralBtn, PopupActions popupActions, int dialogId, File isFile) {
        super(context);
        this.DIALOG_ID = dialogId;
        this.popupActions = popupActions;
        this.path = isFile;
        View view = View.inflate(context, R.layout.layout_confirmation_component, null);
        setContentView(view);
        setInitialsForNeutralBtn(title, message, isCancelable, neutralBtn, isFile);
    }

    private void setInitialsForBoth(String title, String message, Boolean isCancelable, String positiveBtnText, String negativeBtnText) {
        neutralButton.setVisibility(View.GONE);
        positiveButton.setText(positiveBtnText);
        negativeButton.setText(negativeBtnText);
        openFileButton.setVisibility(View.GONE);
        initPopup(title, message, isCancelable);
    }

    private void setInitialsForBoth(String title, String message, Boolean isCancelable, String positiveBtnText, String negativeBtnText, String extraInfo) {
        neutralButton.setVisibility(View.GONE);
        positiveButton.setText(positiveBtnText);
        negativeButton.setText(negativeBtnText);
        extraText.setText(extraInfo);
        openFileButton.setVisibility(View.GONE);
        initPopup(title, message, isCancelable);
    }

    private void setInitialsForNeutralBtn(String title, String message, Boolean isCancelable, String neutralBtn) {
        positiveButton.setVisibility(View.GONE);
        negativeButton.setVisibility(View.GONE);
        neutralButton.setText(neutralBtn);
        openFileButton.setVisibility(View.GONE);
        initPopup(title, message, isCancelable);
    }


    private void setInitialsForNeutralBtn(String title, String message, Boolean isCancelable, String neutralBtn, File path) {
        positiveButton.setVisibility(View.GONE);
        negativeButton.setVisibility(View.GONE);
        neutralButton.setText(neutralBtn);
        openFileButton.setVisibility(View.GONE);
//        if(path.getPath().length()>0){
//            openFileButton.setVisibility(View.VISIBLE);
//            initPopup(title, message, isCancelable, path);
//            return;
//        }
        initPopup(title, message, isCancelable);
    }

    private void initPopup(String title, String message, Boolean isCancelable, File openFile) {
        setPopupProperties(title, message, isCancelable, openFile);
        showPopup();
    }

    private void initPopup(String title, String message, Boolean isCancelable) {
        setPopupProperties(title, message, isCancelable);
        showPopup();
    }

    private void setPopupProperties(String title, String message, Boolean isCancelable, File openFile) {
        setPopupTitle(title);
        setPopupMessage(message);
        setCancelable(isCancelable);
//        openFile(openFile);
    }

    private void setPopupProperties(String title, String message, Boolean isCancelable) {
        setPopupTitle(title);
        setPopupMessage(message);
        setCancelable(isCancelable);
    }

    private void setPopupMessage(String msg) {
        confirmationMessage.setText(msg);
    }

    private void setPopupTitle(String tle) {
        confirmationTitle.setText(tle);
    }

    private void showPopup() {
        if (!this.isShowing()) {
            this.show();
        }
    }


    void onClickNegativeButton() {
        if (popupActions != null) {
            popupActions.onPopupActions(AppConstants.PopupConstants.NEGATIVE, DIALOG_ID);
        }
    }


    void onClickPositiveButton() {
        if (popupActions != null) {
            popupActions.onPopupActions(AppConstants.PopupConstants.POSITIVE, DIALOG_ID);
        }
    }


    void onClickNeutralButton() {
        if (popupActions != null) {
            popupActions.onPopupActions(AppConstants.PopupConstants.NEUTRAL, DIALOG_ID);
        }
    }



    void onClickOpenFile() {
        if (popupActions != null) {
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + File.separator + "DUKE.AI" + File.separator);
//            intent.setDataAndType(uri, DocumentsContract.Document.MIME_TYPE_DIR);
//            getContext().startActivity(intent);


            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            Bundle b = new Bundle();
            startActivity(getContext(), i, b);


//            Intent chooser = new Intent(Intent.ACTION_GET_CONTENT);
//            Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
//            chooser.addCategory(Intent.CATEGORY_OPENABLE);
//            chooser.setDataAndType(uri, "*/*");
//            try {
//                getContext().startActivity(chooser);
//            }
//            catch (android.content.ActivityNotFoundException ex)
//            {
//                Toast.makeText(getContext(), "Please install a File Manager.",
//                        Toast.LENGTH_SHORT).show();
//            }

        }
    }

}
