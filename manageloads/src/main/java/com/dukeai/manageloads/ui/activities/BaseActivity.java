package com.dukeai.manageloads.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class BaseActivity extends AppCompatActivity {

    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int BITMAP_SAMPLE_SIZE = 1;
    public static final String IMAGE_EXTENSION = "jpg";
    public static final int LOCATION_TYPE = 5;
    /*public BroadcastReceiver logoutBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Utilities.resetGlobalData();
            NavigationFlowManager.openMainActivity(BaseActivity.this, true);
        }
    };*/


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setFullPage();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        registerReceivers();
    }

  /*  private void registerReceivers() {
        unregisterReceiver();
        try {
            LocalBroadcastManager.getInstance(this).registerReceiver(logoutBroadcast, new IntentFilter(AppConstants
                    .StringConstants.LOGOUT));
        } catch (Exception ignored) {
        }
    }

    private void unregisterReceiver() {
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(logoutBroadcast);
        } catch (Exception ignored) {
        }
    }*/

    private void setFullPage() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
