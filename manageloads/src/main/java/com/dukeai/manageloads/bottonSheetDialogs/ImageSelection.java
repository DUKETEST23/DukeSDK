package com.dukeai.manageloads.bottonSheetDialogs;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.R;
import com.dukeai.manageloads.interfaces.UploadImageActions;
import com.dukeai.manageloads.model.ChangeThemeModel;
import com.dukeai.manageloads.utils.Utilities;
import com.google.android.material.bottomsheet.BottomSheetDialog;



// Firebase: Setup

public class ImageSelection extends BottomSheetDialog {
    UploadImageActions uploadImageActions;
    View sheetView;
    Boolean isFromProfile;
    TextView title;
    ImageView cameraIcon;
    ImageView galleryIcon;
    ImageView closeIcon;
    RelativeLayout camera;
    RelativeLayout gallery;
    Context context;

    public ImageSelection(@NonNull Context context, UploadImageActions uploadImageInterface, Boolean fromProfile) {
        super(context);
        this.context = context;
        this.uploadImageActions = uploadImageInterface;
        this.isFromProfile = fromProfile;
        sheetView = getLayoutInflater().inflate(R.layout.image_selection_bottom_sheet, null);
        initViews(sheetView);
        setContentView(sheetView);
        setTitle();
        changeTheme();

    }

    private void initViews(View view) {
        title = view.findViewById(R.id.title);
        cameraIcon = view.findViewById(R.id.camera_image);
        galleryIcon = view.findViewById(R.id.gallery_image);
        closeIcon = view.findViewById(R.id.close);
        camera = view.findViewById(R.id.camera);
        gallery = view.findViewById(R.id.gallery);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraOptions();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGalleryOptions();
            }
        });

        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCloseIcon();
            }
        });
    }

    private void setTitle() {
        if (isFromProfile) {
            title.setText(Utilities.getStrings(getContext(), R.string.upload_photo));
        } else {
            title.setText(Utilities.getStrings(getContext(), R.string.add_a_document));
        }
    }

//    TODO: @OnClick(R.id.camera)
    void openCameraOptions() {
        // Firebase: Send click Camera button event
        Bundle params = new Bundle();
        params.putString("Type", "Camera");
        uploadImageActions.openCameraImages();
    }

// TODO:   @OnClick(R.id.gallery)
    void openGalleryOptions() {
        // Firebase: Send click Gallery button event
        Bundle params = new Bundle();
        params.putString("Type", "Gallery");
        uploadImageActions.openGalleryImages();
    }

//   TODO: @OnClick(R.id.close)
    void onClickCloseIcon() {
        Bundle params = new Bundle();
        params.putString("Type", "Cancel");
        this.dismiss();
    }

    public void showDialog() {
        this.show();
    }

    public void dismissDialog() {
        this.dismiss();
    }

    public void changeTheme() {

        ChangeThemeModel changeThemeModel = new ChangeThemeModel();
        cameraIcon.setColorFilter(Color.parseColor(changeThemeModel.getInputFieldIconColor()));
        galleryIcon.setColorFilter(Color.parseColor(changeThemeModel.getInputFieldIconColor()));
        title.setTextColor(Color.parseColor(changeThemeModel.getPopupHeadingTextColor()));
        closeIcon.setColorFilter(Color.parseColor(changeThemeModel.getPopupHeadingTextColor()));
    }
}
