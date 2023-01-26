package com.dukeai.manageloads.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dukeai.manageloads.R;
import com.dukeai.manageloads.interfaces.HeaderActions;
import com.dukeai.manageloads.model.ChangeThemeModel;

public class CustomToolbar implements View.OnClickListener {
    View headerView;
    Context headerContext;
    TextView headerTitle;
    ImageView headerImage;
    TextView toolbarTitle;
    ImageView backButton;
    TextView profileLabel;
    HeaderActions headerActions;
    RelativeLayout headerLayout;
    String title="";

    public CustomToolbar(Context headerContext) {
        this.headerContext = headerContext;
    }

    public void initViews(View v){

        headerLayout = v.findViewById(R.id.header_layout);
        backButton = v.findViewById(R.id.toolbar_back);
        headerTitle = v.findViewById(R.id.header_title);
        headerTitle.setText(title);
        toolbarTitle = v.findViewById(R.id.toolbar_title);
        headerImage = v.findViewById(R.id.header_image);
        profileLabel = v.findViewById(R.id.profile_text);
        backButton.setOnClickListener(this);
        toolbarTitle.setOnClickListener(this);
        headerTitle.setOnClickListener(this);
    }

    public void changeHeaderColor(int color){
        headerLayout.setBackgroundColor(color);
    }
    public void hideHeaderImage(){
        headerImage.setVisibility(View.INVISIBLE);
    }

    public void hideHeaderLable(){
        profileLabel.setVisibility(View.INVISIBLE);
    }

    public void hideToolbarTitle(){
        toolbarTitle.setVisibility(View.INVISIBLE);
    }
    public void hideToolbarBack(){
        backButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.toolbar_back) {
            onClickBackIcon();
        } else if (id == R.id.toolbar_title) {
            onClickToolbarTitle();
        }else if(id == R.id.header_title){
            onClickHeaderTitle();
        }
    }

    //  TODO: click on Header Back
    public void onClickBackIcon() {
        if (headerActions != null) {
            headerActions.onBackClicked();
        } else {
            ((Activity) headerContext).onBackPressed();
        }
    }


    //    TODO: click on Toolbar Title
    public void onClickToolbarTitle() {
        if (headerActions != null) {
            headerActions.onClickToolbarTitle();
        }
    }


    //    TODO: Header Title click
    public void onClickHeaderTitle() {
        if (headerActions != null) {
            headerActions.onClickHeaderTitle();
        }
    }

    public void setCurrentTheme() {
        ChangeThemeModel changeThemeModel = new ChangeThemeModel();
        backButton.setColorFilter(Color.parseColor(changeThemeModel.getHeaderTitleColor()));
        toolbarTitle.setTextColor(Color.parseColor(changeThemeModel.getHeaderTitleColor()));
        headerLayout.setBackgroundColor(Color.parseColor(changeThemeModel.getHeaderBackgroundColor()));
    }

    public void setToolbarTitle(String string) {
        toolbarTitle.setText(string);
    }

    public void setHeaderTextColor(int color) {
        toolbarTitle.setTextColor(color);
    }

    public void setBackButtonTint(int color){
        backButton.setColorFilter(color);
    }
}
