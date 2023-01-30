package com.dukeai.manageloads.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import com.dukeai.manageloads.R;


public class CustomProgressLoader extends Dialog {
    public CustomProgressLoader(Context context) {
        super(context);
    }

    public void showDialog() {
        this.show();
    }

    public void hideDialog() {
        this.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_progress_loader);
        this.setCancelable(false);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
