package com.dukeai.manageloads.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dukeai.manageloads.R;
import com.dukeai.manageloads.interfaces.CommonHeaderInterface;


public class CommonHeader extends LinearLayout {

    Context commonHeaderContext;
    View commonHeaderView;
    ImageView headerImage;
    TextView headerTitle;
    CommonHeaderInterface commonHeaderInterface;

    public CommonHeader(Context context) {
        super(context);
        this.commonHeaderContext = context;
        initView();
    }


    public CommonHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.commonHeaderContext = context;
        initView();
    }

    public void setHeader(String headerName) {
        this.headerTitle.setText(headerName);
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) commonHeaderContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        commonHeaderView = inflater.inflate(R.layout.common_header, this);
    }


    void onClickBack() {
        if (commonHeaderInterface != null) {
            commonHeaderInterface.onBackClicked(headerImage);
        } else {
            ((Activity) commonHeaderContext).onBackPressed();
        }
    }
}
