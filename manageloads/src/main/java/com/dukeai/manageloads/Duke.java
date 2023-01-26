package com.dukeai.manageloads;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.dukeai.awsauth.Auth;
import com.dukeai.awsauth.handlers.AuthHandler;
import com.dukeai.manageloads.apiUtils.AppHelper;
import com.dukeai.manageloads.model.FileStatusModel;
import com.dukeai.manageloads.model.LoadDocumentModel;
import com.dukeai.manageloads.model.RecipientDataModel;
import com.dukeai.manageloads.model.SelectRecipientDataModel;
import com.dukeai.manageloads.model.SubscriptionPlan;
import com.dukeai.manageloads.model.UserLoadsModel;
import com.dukeai.manageloads.ui.fragments.Documents;
import com.dukeai.manageloads.ui.fragments.LoadsFragment;
import com.dukeai.manageloads.utils.DukeManageLoads;

import java.util.ArrayList;

import okhttp3.MultipartBody;

public class Duke extends Application implements LifecycleObserver {

    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    public static Duke appContext;
    public static Boolean isWithOutToken = false;
    public static transient FileStatusModel fileStatusModel = new FileStatusModel();
    public static ArrayList<Bitmap> uploadingImagesList = new ArrayList<>();
    public static ArrayList<Bitmap> sortedImagesList = new ArrayList<>();
    public static ArrayList<Integer> uploadingImageCount = new ArrayList<>();
    public static ArrayList<UserLoadsModel> loadsDocuments = new ArrayList<>();
    public static ArrayList<String> uploadingImageStoragePaths = new ArrayList<>();
    public static ArrayList<String> uploadingPDFStoragePaths = new ArrayList<>();
    public static ArrayList<String> sortedUploadingImageStoragePaths = new ArrayList<>();
    public static ArrayList<String> podDocsStoragePaths = new ArrayList<>();
    public static ArrayList<RecipientDataModel> globalRecipientsList = new ArrayList<>();
    public static ArrayList<RecipientDataModel> customRecipientsList = new ArrayList<>();
    public static ArrayList<String> selectedRecipients = new ArrayList<>();
    public static ArrayList<String> selectedLoadsForTransmission = new ArrayList<>();
    public static String FileStoragePath = "";
    public static String PODDocumentsPath = "";
    public static Bitmap ProfileImage;
    public static String deviceToken;
    public static String imageStoragePath = "";
    public static String profileImageStoragePath = "";
    public static String userName = "";
    public static String referralId;
    public static SubscriptionPlan subscriptionPlan = new SubscriptionPlan(SubscriptionPlan.MemberStatus.FREE);
    public static String stateName = "";
    public static String uniqueUserId = "";
    public static boolean isLocationPermissionProvided = false;
    public static MultipartBody.Part address = MultipartBody.Part.createFormData("address", "none");
    public static boolean isLoadDocument = true;
    public static ArrayList<LoadDocumentModel> DocsOfALoad = new ArrayList<>();
    public static boolean isDocumentAddingToLoad = false;
    public static boolean isNewLoadBeingCreated = false;
    public static boolean isDocumentBeingScanned = false;
    public static String selectedLoadUUID = "";
    public static String selectedLoadSHA1 = "";
    public static ArrayList<Uri> PDFDocURIs = new ArrayList<>();
    public static ArrayList<String> PDFDocFilenames = new ArrayList<>();
    public static Uri FileStoragePathURI;
    public static LoadsFragment loadsFragment = null;
    public static Fragment fragment;
    public static Documents  documentsFragment = null;
    public static Auth auth;

    public static ArrayList<SelectRecipientDataModel> selectRecipientDataForGlobalList = new ArrayList<>();
    public static ArrayList<SelectRecipientDataModel> selectRecipientDataCustomList = new ArrayList<>();
    public static ArrayList<SelectRecipientDataModel> selectRecipientDataRestList = new ArrayList<>();
    public static ArrayList<RecipientDataModel> selectedRecipientsList = new ArrayList<>();
    public static ArrayList<String> restRecipients = new ArrayList<>();
    public static String TAG = "PM=========>";
    public static String idToken = "";

    public static synchronized Duke getInstance() {
        return appContext;
    }

    public static Auth getAuth(Context context, AuthHandler authHandlerSDK) {
        Auth.Builder builder = new Auth.Builder().setAppClientId(DukeManageLoads.appClientId)
                .setAppClientSecret(DukeManageLoads.appClientSecret)
                .setAppCognitoWebDomain(DukeManageLoads.appCognitoWebDomain)
                .setApplicationContext(context)
                .setSignInRedirect(DukeManageLoads.appRedirectScheme)
                .setSignOutRedirect(DukeManageLoads.appRedirectScheme)
                .setAuthHandler(authHandlerSDK);
        auth = builder.build();
        return auth;


    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        Log.d(TAG, "onCreate: "+appContext);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        AppHelper.init(appContext);
        createNotificationChannels();

    }

    public void initDuke(){
        super.onCreate();
        appContext=this;
        Log.d(TAG, "initDuke: ");
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        AppHelper.init(appContext);
        createNotificationChannels();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        //App in background

    }


    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("This is Channel 2");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }
}
