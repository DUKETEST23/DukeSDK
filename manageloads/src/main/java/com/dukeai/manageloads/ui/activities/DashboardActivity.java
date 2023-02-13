package com.dukeai.manageloads.ui.activities;

import static com.dukeai.manageloads.ui.activities.BaseActivity.CAMERA_CAPTURE_IMAGE_REQUEST_CODE;
import static com.dukeai.manageloads.ui.activities.BaseActivity.PICK_IMAGE_REQUEST;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoAccessToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoIdToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoRefreshToken;
import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.R;
import com.dukeai.manageloads.apiUtils.ApiConstants;
import com.dukeai.manageloads.interfaces.UploadDocumentInterface;
import com.dukeai.manageloads.interfaces.UploadStatusClickActions;
import com.dukeai.manageloads.model.AuthModel;
import com.dukeai.manageloads.model.ChangeThemeModel;
import com.dukeai.manageloads.model.ConfigModel;
import com.dukeai.manageloads.model.FileUploadSuccessModel;
import com.dukeai.manageloads.model.UserDataModel;
import com.dukeai.manageloads.ui.fragments.LoadsFragment;
import com.dukeai.manageloads.ui.fragments.UploadPreviewFragment;
import com.dukeai.manageloads.utils.AppConstants;
import com.dukeai.manageloads.utils.CameraUtils;
import com.dukeai.manageloads.utils.CustomProgressLoader;
import com.dukeai.manageloads.utils.NavigationFlowManager;
import com.dukeai.manageloads.utils.UploadDocument;
import com.dukeai.manageloads.utils.UploadStatusDialog;
import com.dukeai.manageloads.utils.UriUtils;
import com.dukeai.manageloads.utils.UserConfig;
import com.dukeai.manageloads.utils.Utilities;
import com.dukeai.manageloads.viewmodel.AuthenticationViewModel;
import com.dukeai.manageloads.viewmodel.FileStatusViewModel;
import com.dukeai.manageloads.viewmodel.LoadsViewModel;
import com.dukeai.manageloads.viewmodel.UploadFileViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DashboardActivity extends AppCompatActivity implements UploadDocumentInterface {


    public final static String LOCATION_SETTINGS_KEY = "LOCATION_SETTINGS_KEY";
    public final static String IS_DOCUMENT_UPLOAD_SUCCESSFUL = "isUploadSuccessful";
    public final static String IS_DOC_SCAN_SUCCESSFUL = "isScanSuccessful";
    public final static String IS_DOCUMENT_INVALID = "INVALID_DOCUMENT";
    private String TAG = DashboardActivity.class.getSimpleName();
    private Boolean toLogin = false, toVerification = false;
    private RelativeLayout parent_layout;
    private UserConfig userConfig = UserConfig.getInstance();
    private Bundle savedInstance;
    private UploadDocument uploadDocument;
    private Context context;
    private FusedLocationProviderClient fusedLocationProviderClient;

    //Stores the latest locations
    private String address = "";
    private String latitude = "";
    private String longitude = "";
    private ArrayList<String> pdfURIs = new ArrayList<>();
    private ArrayList<Uri> uriList = new ArrayList<>();
    private ArrayList<Uri> imageUriList = new ArrayList<>();
    private ArrayList<String> imageURIs = new ArrayList<>();
    private ArrayList<Integer> bitmapsCount = new ArrayList<>();
    private Uri uri2;
    private CustomProgressLoader customProgressLoader;
    private UploadFileViewModel uploadFileViewModel;
    private int screenWidth, screenHeight;
    private String upload = null;
    private UploadStatusDialog uploadStatusDialog;
    private Bitmap imageBitmap;
    private String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int PERMISSION_ALL = 1;
    private Intent mServiceIntent;
    private String latlong = "";
    private AuthenticationViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        savedInstance = savedInstanceState;
        new Duke();
        initViews();
//        loadFragments();
        setToolbarColor();
    }

    private void initViews() {
        context = this;
        customProgressLoader = new CustomProgressLoader(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Intent in = getIntent();
        String accessToken = in.getStringExtra("accessToken");
        String idToken = in.getStringExtra("idToken");
        String refreshToken = in.getStringExtra("refreshToken");
        String userName = in.getStringExtra("userName");
        String password = in.getStringExtra("password");

        getAuthStatus();
//        customProgressLoader.showDialog();
//        customProgressLoader.hideDialog();
    }

    public void getAuthStatus() {
        authViewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        authViewModel.setContext(DashboardActivity.this);
        authViewModel.authentication().observe(this, new Observer<AuthModel>() {
            @Override
            public void onChanged(AuthModel authModel) {
                try {
                    if (authModel.getStatus()) {

                        String idToken = authModel.getCredentials().getIdToken();
                        Duke.idToken = authModel.getCredentials().getIdToken();
                        String refreshToken = authModel.getCredentials().getRefreshToken();
                        String accessToken = authModel.getCredentials().getAccessToken();
                        loadFragments(idToken, refreshToken, accessToken, ConfigModel.cust_id, "password");
                    } else {
                        Log.e("ERROR: ", "Authentication error for manage loads.");
                    }
                } catch (Exception ex) {
                    customProgressLoader.hideDialog();
                    Log.e(TAG, "onChanged: " + ex);
                }
            }
        });
    }

    private void setToolbarColor() {
//        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(Duke.getInstance(), R.color.colorTransparent));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = DashboardActivity.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.yellow_BBAE27));
        }
    }
    private void loadFragments(String idToken, String refreshToken, String accessToken,String userName,String password) {


        Bundle args  =  new Bundle();
        args.putString("accessToken",accessToken);
        args.putString("idToken ",idToken );
        args.putString("refreshToken",refreshToken);
        args.putString("userName",userName);
        args.putString("password",password);

//        CognitoIdToken id = new CognitoIdToken("eyJraWQiOiJiTEw2ZTYrb21wbHo3SG5INkxGeEh4ZGFuRmNBUzU5a09XOElFOFhLb3pzPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJmNTE4MGI2ZS02MjIxLTRkZjItOWQzMy0xOTAwOGNhOWMwOGUiLCJjb2duaXRvOmdyb3VwcyI6WyJhY2NvdW50YW50cyIsInVzZXJzIl0sImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAudXMtZWFzdC0xLmFtYXpvbmF3cy5jb21cL3VzLWVhc3QtMV9RVm1JS1MzYUEiLCJwaG9uZV9udW1iZXJfdmVyaWZpZWQiOmZhbHNlLCJjb2duaXRvOnVzZXJuYW1lIjoiUFJBREhVTU5ARElWQU1JLkNPTSIsImF1ZCI6IjdqaTVkZjdiZ3FtbnZ0YjRtNTI3bzEzNGVlIiwiZXZlbnRfaWQiOiIzNDAzMWI1Yy03ZGIwLTRjNzItYjYwNS0wYTczYmMwNmZkMGMiLCJ0b2tlbl91c2UiOiJpZCIsImF1dGhfdGltZSI6MTY2NTQ5MjIxMiwibmFtZSI6IlBSQURIVU1OQERJVkFNSS5DT00iLCJwaG9uZV9udW1iZXIiOiIrMTIzNDU2Nzg5NTgiLCJjdXN0b206YXBwX3R5cGUiOiJEVUtFIiwiZXhwIjoxNjY1NDk1ODEyLCJpYXQiOjE2NjU0OTIyMTIsImVtYWlsIjoiUFJBREhVTU5ARElWQU1JLkNPTSJ9.V4bJAJGDognVtPb5sjZlk7Np5HDgsYx9ldjWVTKrua80ce6xGrh4mL7bTDto1UrJsNOlbvSsy4gkBCC9aharYlzDW4XRhDlgxNSbEnfvFqUHSTWjue8A3mzzCiN15pRiBBwZS567WI5RE0frVfS06m9OUKY7OzhaMfTuPDoklj9SBqPD53A6Ad-7paoTf-VJ64-EynL73i_zzBTXAUPKFpmu0s9XDqPZLVqs2ORLBcOY0MLfyZiDsxfNkaHWxkG9_2RvXSVm2v0jraBFB-RFXb1v2BQQtzOqFz-vkhRBZha-fFAr8Ez7TeBEWnSLx3tgHMZP3KghXa_9FqmRbmYk5g");
        CognitoIdToken id = new CognitoIdToken(idToken);
        CognitoAccessToken accessToken1 = new CognitoAccessToken(accessToken);
        CognitoRefreshToken refreshToken1 = new CognitoRefreshToken(refreshToken);
        CognitoUserSession userSession = new CognitoUserSession(id, accessToken1, refreshToken1);
        UserDataModel userDataModel = new UserDataModel(userSession, userName, password);

        userConfig.setUserDataModel(userDataModel);

        parent_layout= findViewById(R.id.parent_layout);
        ChangeThemeModel changeThemeModel = new ChangeThemeModel();
        parent_layout.setBackgroundColor(Color.parseColor(changeThemeModel.getBackgroundColor()));
//        NavigationFlowManager.openFragments(new LoadsFragment(), args, this, R.id.dashboard_wrapper);
//        customProgressLoader.hideDialog();
        NavigationFlowManager.addNewFragment(new LoadsFragment(),null,this,R.id.dashboard_wrapper);
//        customProgressLoader.hideDialog();
    }

    @Override
    public void uploadDocumentListener(Boolean isOpen) {
        if (isOpen) {
            uploadDocument = new UploadDocument(DashboardActivity.this, this, savedInstance, false, null, isOpen);
        } else {
            uploadDocument = new UploadDocument(DashboardActivity.this, this, savedInstance, false, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                CameraUtils.refreshGallery(context.getApplicationContext(), Duke.imageStoragePath);

                /**Fix for Image Rotation Issue**/
                rotateImageIfNecessary(Duke.imageStoragePath);
                if (Duke.isLocationPermissionProvided) {
                    previewCapturedImage();
//                    openPreviewImage(Duke.imageStoragePath, "none", "", "");
                } else {
//                    openPreviewImage(Duke.imageStoragePath, "none", "", "");
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Duke.isLocationPermissionProvided = true;
                        getCurrentLocation(data);
                    } else {
                        openPreviewImage(Duke.imageStoragePath, "none", "", "");
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this.getApplicationContext(), getString(R.string.cancelled_image_capture), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.getApplicationContext(), getString(R.string.failed_to_capture_image), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST) {

            if (resultCode == RESULT_CANCELED) {
                return;
            }

            //Fetch Location - if permission is available
            if (Duke.isLocationPermissionProvided) {
                getCurrentLocation(data);
            } else {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Duke.isLocationPermissionProvided = true;
                    getCurrentLocation(data);
                } else {
                    performUploadTasks(data, "none", latitude, longitude);
                }
            }

        } else {
            //Call Fragment's onActivityResult
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.dashboard_wrapper);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void rotateImageIfNecessary(String imagePath) {
        try {
            ExifInterface ei = null;
            ei = new ExifInterface(imagePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap rotatedBitmap = null;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            int rotation = 1;
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotation = ExifInterface.ORIENTATION_ROTATE_90;
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotation = ExifInterface.ORIENTATION_ROTATE_180;
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotation = ExifInterface.ORIENTATION_ROTATE_270;
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
            /**Rotate only when the image is not in correct orientation**/
            if (rotation != ExifInterface.ORIENTATION_NORMAL) {
                Utilities.saveBitmap(imagePath, rotatedBitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void previewCapturedImage() {
//        final CustomProgressLoader customProgressLoader = new CustomProgressLoader(this);
//        customProgressLoader.showDialog();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(DashboardActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        Log.d("location fetched", addresses.get(0).getAddressLine(0));
                        if (addresses.get(0) != null && addresses.get(0).getAddressLine(0).length() > 0) {
                            address = addresses.get(0).getAddressLine(0);
                            latitude = String.valueOf(location.getLatitude());
                            longitude = String.valueOf(location.getLongitude());
                            System.out.println("Current Lat " + latitude + " Long " + longitude);
                            openPreviewImage(Duke.imageStoragePath, address, latitude, longitude);
                        }
//                        customProgressLoader.hideDialog();
                    } catch (Exception e) {
                        openPreviewImage(Duke.imageStoragePath, "none", "", "");
                        Log.d("location not fetched", e.getLocalizedMessage());
                        customProgressLoader.hideDialog();
                    }
                } else {
                    openPreviewImage(Duke.imageStoragePath, "none", "", "");
                    customProgressLoader.hideDialog();
                }
            }
        });
        Log.d("current address", address);
    }

    private void openPreviewImage(String path, String address, String lat, String longi) {
        Bundle args = new Bundle();
        args.putString(AppConstants.UploadDocumentsConstants.BITMAP_IMAGE, path);
        Log.e("FILE_PATH", path);
        args.putString("address", address);
        args.putString("lat", lat);
        args.putString("longi", longi);
        NavigationFlowManager.openFragments(UploadPreviewFragment.newInstance(), args, DashboardActivity.this, R.id.dashboard_wrapper);
    }

    private void getCurrentLocation(Intent data) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(DashboardActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        Log.d("location fetched", addresses.get(0).getAddressLine(0));
                        if (addresses.get(0) != null && addresses.get(0).getAddressLine(0).length() > 0) {
                            address = addresses.get(0).getAddressLine(0);
                            latitude = String.valueOf(location.getLatitude());
                            longitude = String.valueOf(location.getLongitude());
                            performUploadTasks(data, address, latitude, longitude);
                        }
                    } catch (Exception e) {
                        performUploadTasks(data, "none", "", "");
                        Log.d("location not fetched", e.getLocalizedMessage());
                    }
                } else {
                    performUploadTasks(data, "none", "", "");
                }
            }
        });
        Log.d("current address", address);
    }

    private void performUploadTasks(Intent data, String locationToBE, String latitude, String longitude) {
        if (Duke.isNewLoadBeingCreated || Duke.isDocumentAddingToLoad || Duke.isDocumentBeingScanned) {
            loadDocumentUpload(data, locationToBE, latitude, longitude);
        } else {
            normalDocUpload(data, locationToBE, latitude, longitude);
        }
    }

    private void loadDocumentUpload(Intent data, String locationToBE, String latitude, String longitude) {
        Uri uri = null;
        int pdfCount = 0;
        int imageCount = 0;
        pdfURIs.clear();
        imageURIs.clear();
        imageUriList.clear();
        uriList.clear();
//        Duke.PDFDocURIs.clear();

        if (data != null) {
            if (data.getClipData() != null) {
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    ContentResolver cR = context.getContentResolver();
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    String type = mime.getExtensionFromMimeType(cR.getType(data.getClipData().getItemAt(i).getUri()));
                    if(type==null){
                        type = cR.getType(data.getClipData().getItemAt(i).getUri());
                    }
                    try{
                        if (type.equalsIgnoreCase("image/pdf")) {
                            pdfCount++;
                            uriList.add(data.getClipData().getItemAt(i).getUri());
                            pdfURIs.add(data.getClipData().getItemAt(i).getUri().getPath());
                            uri2 = data.getClipData().getItemAt(i).getUri();
                            Duke.PDFDocURIs.add(data.getClipData().getItemAt(i).getUri());
                        }else if (type.equalsIgnoreCase("pdf")) {
                            pdfCount++;
                            uriList.add(data.getClipData().getItemAt(i).getUri());
                            pdfURIs.add(data.getClipData().getItemAt(i).getUri().getPath());
                            uri2 = data.getClipData().getItemAt(i).getUri();
                            Duke.PDFDocURIs.add(data.getClipData().getItemAt(i).getUri());
                        } else {
                            imageCount++;
                            imageUriList.add(data.getClipData().getItemAt(i).getUri());
                            imageURIs.add(data.getClipData().getItemAt(i).getUri().getPath());
                        }
                    }catch (Exception ex){
                        Log.e("Exception: "," DashboardActivity-379 "+ex.toString());
                    }
                }
            } else {
                uri2 = data.getData();
                ContentResolver cR = context.getContentResolver();
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String type = mime.getExtensionFromMimeType(cR.getType(uri2));
                if (type.equalsIgnoreCase("pdf")) {
//                    uri = data.getData();
                    if (data.getData() != null) {
                        uri = data.getData();
                    } else {
                        uri = data.getClipData().getItemAt(0).getUri();
                    }
                    uriList.add(uri);
                    Duke.PDFDocURIs.add(uri);
                    pdfURIs.add(uri.getPath());

                    /** Code needed when adding feature to detect corrupt PDF **/
//                    if(inavlidPDFDocumentCheck()) {
//                        uploadPDFDoc(pdfURIs, locationToBE);
//                    } else {
//                        invalidDocumentDetected();
//                        return;
//                    }
//                    uploadPDFDoc(pdfURIs, locationToBE);
                    String path = uri.getPath();
//                    makeFileCopyInCacheDir(uri);
//                    String fullPath = Commons.getPath(uri, context);
//                    openPreviewImage(fullPath, locationToBE);
//                    if ( UriUtils.getPathFromUri(this,uri) != null && !UriUtils.getPathFromUri(this,uri).isEmpty()) {
//                        openPreviewImage(UriUtils.getPathFromUri(this,uri), locationToBE);
//                    }
                    String realPath = UriUtils.getMediaFilePathForN(uri, this);
                    if (realPath != null) {
                        openPreviewImage(realPath, locationToBE, latitude, longitude);
                    }
//                    if (Utilities.getPath(context, uri) != null && !Utilities.getPath(context, uri).isEmpty()) {
//                        openPreviewImage(Utilities.getPath(context, uri), locationToBE);
//                    }
//                    openPreviewImage(uri.getPath(), locationToBE);
                } else {
                    pdfURIs.clear();
//                    uri = data.getData();
                    if (data.getData() != null) {
                        uri = data.getData();
                    } else {
                        uri = data.getClipData().getItemAt(0).getUri();
                    }
                    imageUriList.add(uri);
                    imageURIs.add(uri.getPath());
                }
            }
        }

        if ((pdfURIs.size() + imageURIs.size()) > 1) {
            ArrayList<String> pathList = new ArrayList<>();

//            openPreviewDocuments();
        }

        try {
            if (data.getData() != null) {
                uri = data.getData();
            } else {
                uri = data.getClipData().getItemAt(0).getUri();
            }
        }catch (Exception ex){
            Log.e("Exception","-DASHBOARD 448  "+ex);
        }


        /*try{

            uri = data.getData();
        }catch (Exception ex){
          try{
              uri = data.getClipData().getItemAt(0).getUri();
          }catch (Exception e){
              Log.e("Exception-DASHBOARD 507 ",ex+"");
          }
            Log.e("Exception-DASHBOARD 509 ",ex+"");
        }*/


        if (pdfURIs.size() == 0 && imageURIs.size() > 0) {
            Duke.imageStoragePath = Utilities.getPath(context, uri);
//            Duke.imageStoragePath = Utilities.getPath(context, data.getClipData().getItemAt(0).getUri());
            if (Duke.imageStoragePath != null && !Duke.imageStoragePath.isEmpty() && (Duke.imageStoragePath.toLowerCase().endsWith(".jpg") || Duke.imageStoragePath.toLowerCase().endsWith(".jpeg")
                    || Duke.imageStoragePath.toLowerCase().endsWith(".png"))) {
                openPreviewImage(Duke.imageStoragePath, locationToBE, latitude, longitude);
            } else {
                Duke.imageStoragePath = "";
            }
        }
    }

    private void normalDocUpload(Intent data, String locationToBE, String latitude, String longitude) {
        Uri uri = null;
        int pdfCount = 0;
        int imageCount = 0;
        pdfURIs.clear();
        imageURIs.clear();
        imageUriList.clear();
        uriList.clear();

        if (data != null) {
            if (data.getClipData() != null) {
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    ContentResolver cR = context.getContentResolver();
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    String type = mime.getExtensionFromMimeType(cR.getType(data.getClipData().getItemAt(i).getUri()));
                    try{
                        if (type.equalsIgnoreCase("pdf")) {
                            pdfCount++;
                            uriList.add(data.getClipData().getItemAt(i).getUri());
                            pdfURIs.add(data.getClipData().getItemAt(i).getUri().getPath());
                            uri2 = data.getClipData().getItemAt(i).getUri();
                        } else {
                            imageCount++;
                            imageUriList.add(data.getClipData().getItemAt(i).getUri());
                            imageURIs.add(data.getClipData().getItemAt(i).getUri().getPath());
                        }
                    }catch (Exception ex){

                    }
                }
            } else {
                uri2 = data.getData();
                ContentResolver cR = context.getContentResolver();
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String type = mime.getExtensionFromMimeType(cR.getType(uri2));
                if (type.equalsIgnoreCase("pdf")) {
//                    uri = data.getData();
                    if (data.getData() != null) {
                        uri = data.getData();
                    } else {
                        uri = data.getClipData().getItemAt(0).getUri();
                    }
                    uriList.add(uri);
                    pdfURIs.add(uri.getPath());
                    /** Code needed when adding feature to detect corrupt PDF **/
//                    if(inavlidPDFDocumentCheck()) {
//                        uploadPDFDoc(pdfURIs, locationToBE);
//                    } else {
//                        invalidDocumentDetected();
//                        return;
//                    }
//                    uploadPDFDoc(pdfURIs, locationToBE);
                    String path = uri.getPath();
                    if (Utilities.getPath(context, uri) != null && !Utilities.getPath(context, uri).isEmpty()) {
                        openPreviewImage(Utilities.getPath(context, uri), locationToBE, latitude, longitude);
                    }
                } else {
                    pdfURIs.clear();
//                    uri = data.getData();
                    if (data.getData() != null) {
                        uri = data.getData();
                    } else {
                        uri = data.getClipData().getItemAt(0).getUri();
                    }
                    imageUriList.add(uri);
                    imageURIs.add(uri.getPath());
                }
            }
        }

        //Check For corrupted Docs
        for (int i = 0; i < imageUriList.size(); i++) {
            String path = Utilities.getPath(context, imageUriList.get(i));
            Bitmap isBitmapValid = Utilities.getPortraitResizedBitmap(BitmapFactory.decodeFile(path), 2160, 1440);
            if (isBitmapValid == null) {
                invalidDocumentDetected();
                return;
            }
        }

        /** Code for corrupt PDF detection **/
//        if(!inavlidPDFDocumentCheck()){
//            invalidDocumentDetected();
//            return;
//        }

        try {

            ContentResolver cR = context.getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String type = mime.getExtensionFromMimeType(cR.getType(uri2));

        } catch (Exception e) {
        }

        if (pdfURIs.size() > 0 && imageURIs.size() < 1) {
            uri2 = data.getData();
            uploadPDFDoc(pdfURIs, locationToBE);
//            String path = Utilities.getPath(context, uri2);
//            if (path != null && !path.isEmpty()) {
//                openPreviewImage(path, locationToBE);
//            }
            return;
        }

        if (pdfURIs.size() > 0 && imageURIs.size() > 0) {
            uploadPDFDoc(pdfURIs, locationToBE);
            uploadFiles(locationToBE);
            return;
        }

        if (pdfURIs.size() == 0 && imageURIs.size() > 1) {
            uploadFiles(locationToBE);
            return;
        }

//        if(uri != null) {
//        }
        if (data.getData() != null) {
            uri = data.getData();
        } else {
            uri = data.getClipData().getItemAt(0).getUri();
        }
        if (pdfURIs.size() == 0 && imageURIs.size() > 0) {
            Duke.imageStoragePath = Utilities.getPath(context, uri);
            if (Duke.imageStoragePath != null && !Duke.imageStoragePath.isEmpty() && (Duke.imageStoragePath.toLowerCase().endsWith(".jpg") || Duke.imageStoragePath.toLowerCase().endsWith(".jpeg")
                    || Duke.imageStoragePath.toLowerCase().endsWith(".png"))) {
                openPreviewImage(Duke.imageStoragePath, locationToBE, latitude, longitude);
            } else {
                Duke.imageStoragePath = "";
            }
        }

    }


    private void invalidDocumentDetected() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(DashboardActivity.IS_DOCUMENT_INVALID, true);
        NavigationFlowManager.openFragments(Duke.loadsFragment, bundle, DashboardActivity.this, R.id.dashboard_wrapper);
        return;
    }


    private void uploadPDFDoc(final ArrayList<String> pdfURIList, String streetAddress) {
        customProgressLoader.showDialog();
        uploadFileViewModel = ViewModelProviders.of(this).get(UploadFileViewModel.class);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ArrayList<Integer> count = new ArrayList<>();
                for (int i = 0; i < pdfURIList.size(); i++) {
                    count.clear();
                    count.add(1);
                    MultipartBody.Part[] list = getMultipartBody(pdfURIList, uriList.get(i), screenWidth, screenHeight);
                    MultipartBody.Part fileCount = DashboardActivity.getFileCountArray(count);
                    MultipartBody.Part address = MultipartBody.Part.createFormData("address", streetAddress);

                    if (list != null && list.length <= 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                customProgressLoader.hideDialog();
                            }
                        });
                        return;
                    }
                    uploadFileViewModel.uploadFile(list, fileCount, address,DashboardActivity.this).observe(DashboardActivity.this, new Observer<FileUploadSuccessModel>() {
                        @Override
                        public void onChanged(@Nullable FileUploadSuccessModel jsonElement) {
                            upload = null;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    customProgressLoader.hideDialog();
                                }
                            });
                            if (jsonElement != null && jsonElement.getCode() != null && jsonElement.getCode().equals(ApiConstants.ERRORS.SUCCESS)) {
                                Bundle bundle = new Bundle();
                                bundle.putBoolean(DashboardActivity.IS_DOCUMENT_UPLOAD_SUCCESSFUL, true);
                                NavigationFlowManager.openFragments(Duke.loadsFragment, bundle, DashboardActivity.this, R.id.dashboard_wrapper);
                            } else {
                                if (jsonElement != null && jsonElement.getMessage() != null) {
                                    upload = AppConstants.UploadConstants.UPLOAD_FAILURE;
                                    /**Dismiss Dialogs(If any)**/
                                    if (uploadStatusDialog != null)
                                        uploadStatusDialog.dismiss();
                                    /**Show dialog**/
                                    uploadStatusDialog = new UploadStatusDialog(DashboardActivity.this, 10, UploadStatusDialog.DialogType.UPLOAD_ERROR, jsonElement.getMessage(), new UploadStatusClickActions() {
                                        @Override
                                        public void onButtonCick(int dialogId, int type) {
                                            uploadStatusDialog.dismiss();
                                            Bundle bundle = new Bundle();
                                            bundle.putBoolean(DashboardActivity.IS_DOCUMENT_UPLOAD_SUCCESSFUL, false);
                                            NavigationFlowManager.openFragments(Duke.loadsFragment, bundle, DashboardActivity.this, R.id.dashboard_wrapper);
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        });
    }


    private MultipartBody.Part[] getMultipartBody(ArrayList<String> pdfURIList, Uri uri, int width, int height) {

        String resultBase64Encoded = "";

        MultipartBody.Part[] list = new MultipartBody.Part[1];

        try {

            InputStream in = getContentResolver().openInputStream(uri);
            byte[] bytes = getBytes(in);
            resultBase64Encoded = Base64.encodeToString(bytes, Base64.DEFAULT);

            RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), resultBase64Encoded);
            String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
            String fileName = timeStamp + ".pdf";

            MultipartBody.Part body = MultipartBody.Part.createFormData(fileName, fileName, requestFile);
            list[0] = body;

        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        return list;
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static MultipartBody.Part getFileCountArray(ArrayList<Integer> bitmapCount) {
        if (bitmapCount != null && bitmapCount.size() > 0) {
            String str = Arrays.toString(bitmapCount.toArray());
            MultipartBody.Part body = MultipartBody.Part.createFormData(ApiConstants.UploadDocuments.FILE_COUNT, str);
            return body;
        }
        return null;
    }

    public void uploadFiles(String streetAddress) {
        Log.d("location", streetAddress);
        customProgressLoader.showDialog();
        uploadFileViewModel = ViewModelProviders.of(this).get(UploadFileViewModel.class);
        uploadFileViewModel.setContext(getApplicationContext());

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                bitmapsCount.clear();
                ArrayList<Integer> count = new ArrayList<>();
                for (int i = 0; i < imageUriList.size(); i++) {
                    resetFileUploads();
                    count.clear();
                    count.add(1);

                    Duke.imageStoragePath = Utilities.getPath(context, imageUriList.get(i));
                    imageBitmap = Utilities.getPortraitResizedBitmap(BitmapFactory.decodeFile(Duke.imageStoragePath), 2160, 1440); /**Temp. Solution**/
                    Duke.imageStoragePath = saveImagePath(imageBitmap);
                    Duke.uploadingImagesList.add(imageBitmap);
                    Duke.uploadingImageStoragePaths.add(Duke.imageStoragePath);

                    MultipartBody.Part[] list = new MultipartBody.Part[Duke.uploadingImageStoragePaths.size()];
                    list = Utilities.getMultipartBody(Duke.uploadingImageStoragePaths, false, screenWidth, screenHeight);

                    MultipartBody.Part fileCount = Utilities.getFileCountArray(count);

                    MultipartBody.Part address = MultipartBody.Part.createFormData("address", streetAddress);

                    if (list != null && list.length <= 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                customProgressLoader.hideDialog();
                            }
                        });
                        return;
                    }

                    uploadFileViewModel.uploadFile(list, fileCount, address,DashboardActivity.this).observe(DashboardActivity.this, new Observer<FileUploadSuccessModel>() {
                        @Override
                        public void onChanged(@Nullable FileUploadSuccessModel jsonElement) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    customProgressLoader.hideDialog();
                                }
                            });
                            if (jsonElement != null && jsonElement.getCode() != null && jsonElement.getCode().equals(ApiConstants.ERRORS.SUCCESS)) {
//                                resetFileUploads();
                                Bundle bundle = new Bundle();
                                bundle.putBoolean(DashboardActivity.IS_DOCUMENT_UPLOAD_SUCCESSFUL, true);
                                NavigationFlowManager.openFragments(Duke.loadsFragment, bundle, DashboardActivity.this, R.id.dashboard_wrapper);
                            } else {
                                if (jsonElement != null && jsonElement.getMessage() != null) {
                                    /**Dismiss Dialogs(If any)**/
                                    if (uploadStatusDialog != null)
                                        uploadStatusDialog.dismiss();
                                    /**Show dialog**/
                                    uploadStatusDialog = new UploadStatusDialog(DashboardActivity.this, 10, UploadStatusDialog.DialogType.UPLOAD_ERROR, jsonElement.getMessage(), new UploadStatusClickActions() {
                                        @Override
                                        public void onButtonCick(int dialogId, int type) {
                                            uploadStatusDialog.dismiss();
                                            Bundle bundle = new Bundle();
                                            bundle.putBoolean(DashboardActivity.IS_DOCUMENT_UPLOAD_SUCCESSFUL, false);
                                            NavigationFlowManager.openFragments(Duke.loadsFragment, bundle, DashboardActivity.this, R.id.dashboard_wrapper);
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        });
    }


    public void resetFileUploads() {
        Duke.imageStoragePath = "";
        Duke.uploadingImageStoragePaths = new ArrayList<>();
        Duke.uploadingImagesList = new ArrayList<>();
        Duke.sortedUploadingImageStoragePaths = new ArrayList<>();
        Duke.sortedImagesList = new ArrayList<>();
        Duke.uploadingImageCount = new ArrayList<>();
    }


    String saveImagePath(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "SavedImages");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = "DukeFiles_" + timeStamp + ".jpg";
        File file = new File(myDir, fname);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root + "/SavedImages/" + fname;
    }
}