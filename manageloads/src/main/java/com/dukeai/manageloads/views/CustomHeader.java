package com.dukeai.manageloads.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.R;
import com.dukeai.manageloads.interfaces.HeaderActions;
import com.dukeai.manageloads.model.ChangeThemeModel;
import com.dukeai.manageloads.utils.Utilities;

public class CustomHeader extends LinearLayout implements View.OnClickListener {
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

    public CustomHeader(Context context) {
        super(context);
        this.headerContext = context;
        initView();
    }

    public CustomHeader(Context context,String headerTitle){
        super(context);
        this.headerContext = context;
        initView();
    }

    public CustomHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.headerContext = context;
        initView();
    }

    public CustomHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.headerContext = context;
        initView();
    }

    public void setImageTintColor(int color) {
        backButton.setColorFilter(color);
    }

    public void setHeaderActions(HeaderActions actions) {
        this.headerActions = actions;
    }

    public void showHideProfileImage(int flag) {
        this.headerImage.setVisibility(flag);
        this.profileLabel.setVisibility(flag);
    }

    public void setProfileTextGone() {
        this.profileLabel.setVisibility(GONE);
    }

    public void showHideHeaderTitle(int flag) {
        this.headerTitle.setVisibility(flag);
    }

    public void setHeaderTitle(String title) {
        this.headerTitle.setText(title);
    }

    public void setHeaderImage(Bitmap bm) {
        this.headerImage.setImageBitmap(bm);
    }

    public void setToolbarTitle(String title) {
        this.toolbarTitle.setText(title);
    }

    public void showHideBackButton(int flag) {
        this.backButton.setVisibility(flag);
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) headerContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headerView = inflater.inflate(R.layout.custom_header, this);

        headerLayout = headerView.findViewById(R.id.header_layout);
        backButton = headerView.findViewById(R.id.toolbar_back);
        headerTitle = headerView.findViewById(R.id.header_title);
        headerTitle.setText(title);
        toolbarTitle = headerView.findViewById(R.id.toolbar_title);
        headerImage = headerView.findViewById(R.id.header_image);
        profileLabel = headerView.findViewById(R.id.profile_text);
        backButton.setOnClickListener(this);
        toolbarTitle.setOnClickListener(this);
        headerTitle.setOnClickListener(this);

        setCurrentTheme();
        setHeaderImageBitmap();
    }

    private void setHeaderImageBitmap() {
        if (Duke.ProfileImage != null) {
            headerImage.setImageBitmap(Duke.ProfileImage);
        } else {
            headerImage.setImageDrawable(Utilities.getDrawable(headerContext, R.drawable.header_profile));
        }
    }

    /*@OnClick(R.id.header_image)
    public void navigateToProfile() {
        if (headerActions != null) {
            headerActions.onClickProfile();
        }
    }
*/
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
}
