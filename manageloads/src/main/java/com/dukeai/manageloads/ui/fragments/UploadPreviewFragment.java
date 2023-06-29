package com.dukeai.manageloads.ui.fragments;

import static com.dukeai.manageloads.Duke.DocsOfALoad;
import static com.dukeai.manageloads.ui.fragments.LoadsFragment.isLoadDocument;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.R;
import com.dukeai.manageloads.adapters.UploadImagesRecyclerViewAdapter;
import com.dukeai.manageloads.apiUtils.ApiConstants;
import com.dukeai.manageloads.interfaces.HeaderActions;
import com.dukeai.manageloads.interfaces.PopupActions;
import com.dukeai.manageloads.interfaces.UploadDocumentInterface;
import com.dukeai.manageloads.interfaces.UploadImagePreviewClickListener;
import com.dukeai.manageloads.interfaces.UploadSelectionListener;
import com.dukeai.manageloads.interfaces.UploadStatusClickActions;
import com.dukeai.manageloads.model.ChangeThemeModel;
import com.dukeai.manageloads.model.CreateLoadModel;
import com.dukeai.manageloads.model.FileUploadSuccessModel;
import com.dukeai.manageloads.model.GenericResponseWithJSONModel;
import com.dukeai.manageloads.model.SingleLoadModel;
import com.dukeai.manageloads.model.UploadFileResponseModel;
import com.dukeai.manageloads.model.UserDataModel;
import com.dukeai.manageloads.ui.activities.DashboardActivity;
import com.dukeai.manageloads.utils.AppConstants;
import com.dukeai.manageloads.utils.CustomProgressLoader;
import com.dukeai.manageloads.utils.CustomToolbar;
import com.dukeai.manageloads.utils.NavigationFlowManager;
import com.dukeai.manageloads.utils.UploadStatusDialog;
import com.dukeai.manageloads.utils.UserConfig;
import com.dukeai.manageloads.utils.Utilities;
import com.dukeai.manageloads.viewmodel.LoadsViewModel;
import com.dukeai.manageloads.viewmodel.UploadFileViewModel;
import com.dukeai.manageloads.views.ConfirmationComponent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.single.PermissionListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MultipartBody;


public class UploadPreviewFragment extends Fragment implements UploadImagePreviewClickListener, PopupActions, HeaderActions, UploadSelectionListener {

    View uploadPreview;
    CustomToolbar customHeader;
    RecyclerView imagesRecyclerView;
    HorizontalScrollView scrollView;
    ImageView previewImage;
    Button uploadButton;
    RelativeLayout addImageView;
    RelativeLayout cancleLayout;
    ImageView closeMark;
    ImageView camIcon;
    Button rescanBtn;


    LoadsViewModel loadsViewModel;
    UploadDocumentInterface uploadDocumentInterface;
    UploadImagesRecyclerViewAdapter adapter;
    UploadFileViewModel uploadFileViewModel;
    CustomProgressLoader customProgressLoader;
    ConfirmationComponent confirmationComponent;
    PopupActions popupActions;
    int screenWidth, screenHeight;
    String uploadAs;
    Bitmap imageBitmap;
    ArrayList<Integer> bitmapsCount = new ArrayList<>();
    Boolean isPermissionGranted = false;
    UploadStatusDialog uploadStatusDialog;
    MultipartBody.Part[] list2 = new MultipartBody.Part[Duke.uploadingImageStoragePaths.size()];
    MultipartBody.Part count2;
    String currentLocation = "none";
    String latitude = "none";
    String longitude = "none";
    MultipartBody.Part coordinates;
    boolean isDocumentAPDF = false;

    ArrayList<String> cordinatesArray;

    public UploadPreviewFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static UploadPreviewFragment newInstance() {
        return new UploadPreviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        uploadPreview = inflater.inflate(R.layout.fragment_upload_preview, container, false);
        popupActions = this;
//        ButterKnife.bind(this, uploadPreview);
        rescanBtn = uploadPreview.findViewById(R.id.rescan);
        rescanBtn.setOnClickListener(this::navigateToRescan);
        initViews(uploadPreview);
        setCustomHeader(uploadPreview);
        setCurrentTheme();
        return uploadPreview;
    }

    private void initViews(View v) {
        previewImage = v.findViewById(R.id.preview_image);
        scrollView = v.findViewById(R.id.list_view);
        imagesRecyclerView = v.findViewById(R.id.images_view);
        uploadButton = v.findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUploadButton();
            }
        });
        addImageView = v.findViewById(R.id.add_picture);

        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAddPicture();
            }
        });

        cancleLayout = v.findViewById(R.id.cancel_action);
        cancleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCancelAction();
            }
        });

//        rescan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                navigateToRescan(view);
//            }
//        });

        closeMark = v.findViewById(R.id.close_mark);
        camIcon = v.findViewById(R.id.cam_icon);

    }

//    public void onClickRescan() {
//        Duke.letUserAdjustCrop = true;
//        resetFileUploads();
//        Bundle params = new Bundle();
//        params.putString("Page", "Upload_Document");
//        if (uploadDocumentInterface != null) {
//            uploadDocumentInterface.uploadDocumentListener(false);
//        }
//    }

    private void getWindowWidthAndHeight() {
        screenWidth = Utilities.getScreenWidthInPixels(getActivity());
        screenHeight = Utilities.getScreenHeightInPixels(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setInitials();
    }

    private void setInitials() {
        customProgressLoader = new CustomProgressLoader(getContext());
        loadsViewModel = ViewModelProviders.of(this).get(LoadsViewModel.class);
        uploadFileViewModel = ViewModelProviders.of(getActivity()).get(UploadFileViewModel.class);
        cordinatesArray = new ArrayList<>();
        getArgumentsData();
        getWindowWidthAndHeight();
        setRecyclerView();
//        setImageView(null);
    }

    private void getArgumentsData() {
        Bundle args = getArguments();
        if (args != null) {
            String path = args.getString(AppConstants.UploadDocumentsConstants.BITMAP_IMAGE);
            currentLocation = args.getString("address");
            latitude =args.getString("lat");
            longitude  = args.getString("longi");
            if(path.substring(path.length()-3, path.length()).toLowerCase().equals("pdf")) {
                try {
                    ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(new File(path), ParcelFileDescriptor.MODE_READ_ONLY);
                    PdfRenderer pdfRenderer = new PdfRenderer(fileDescriptor);
                    PdfRenderer.Page page = pdfRenderer.openPage(0);
                    Bitmap bitmapfromPDF = Bitmap.createBitmap(1440, 2160, Bitmap.Config.ARGB_8888);
                    // Paint bitmap before rendering
                    Canvas canvas = new Canvas(bitmapfromPDF);
                    canvas.drawColor(Color.WHITE);
                    canvas.drawBitmap(bitmapfromPDF, 0, 0, null);
                    page.render(bitmapfromPDF, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                    imageBitmap = bitmapfromPDF;
                    isDocumentAPDF = true;
                    page.close();
                    pdfRenderer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                /**Resizing the Image before Displaying**/
                imageBitmap = Utilities.getPortraitResizedBitmap(BitmapFactory.decodeFile(path), 2160, 1440); /**Temp. Solution**/
            }

            previewImage.setImageBitmap(imageBitmap);
            if (isPermissionGranted) {
                setImageFilePath(path);
            } else {
                checkWritePermissions(path);
            }
            System.out.println("LIBRARY~" + "getArgumentsData()");
            Log.e("LIBRARY~", "getArgumentsData()");

            customProgressLoader.hideDialog();
//            Log.e("loader", )

//            if (customProgressLoader != null && customProgressLoader.isShowing()) {
//                customProgressLoader.hideDialog();
//            }

//            setImageView(imageBitmap);
        }
    }

    private void setImageFilePath(String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Duke.imageStoragePath = path;
        } else {
            Duke.imageStoragePath = saveImagePath(imageBitmap);
        }
        if(!isDocumentAPDF) {
            Duke.uploadingImagesList.add(imageBitmap);
            Duke.uploadingImageStoragePaths.add(Duke.imageStoragePath);
        } else {
            Duke.uploadingPDFStoragePaths.add(Duke.imageStoragePath);
        }
        System.out.println("LIBRARY~" + "setImageFilePath()");
        Log.e("LIBRARY~", "setImageFilePath()");
    }

    void checkWritePermissions(String path) {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        isPermissionGranted = true;
                        setImageFilePath(path);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        isPermissionGranted = false;
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                Toast.makeText(getActivity(), "Error occurred! ", Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "Error occurred! ", Toast.LENGTH_LONG).show();
            }
        }).onSameThread()
                .check();
    }

    String saveImagePath(Bitmap finalBitmap) {
        String root = "";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = "DukeFiles_" + timeStamp + ".jpg";

        OutputStream out;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getActivity().getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fname);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            try {
                out = resolver.openOutputStream(imageUri);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                try {
                    out.flush();
                    return Environment.DIRECTORY_PICTURES + "/" + fname;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "SavedImages");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            File file = new File(myDir, fname);
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                out = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("LIBRARY~" + "saveImagePath()");
        Log.e("LIBRARY~", "saveImagePath()");
        return root + "/SavedImages/" + fname;
    }

    private void setImageView(Bitmap bm) {
        if (bm != null) {
            previewImage.setImageBitmap(bm);
            Log.e("LIBRARY~", "setImageView-1()");
        } else if (Duke.uploadingImagesList != null && Duke.uploadingImagesList.size() > 0) {
            previewImage.setImageBitmap(Duke.uploadingImagesList.get(Duke.uploadingImagesList.size() - 1));
            Log.e("LIBRARY~", "setImageView-2()");
            customProgressLoader.hideDialog();
//            if (customProgressLoader != null && customProgressLoader.isShowing()) {
//                customProgressLoader.dismiss();
//            }
        } else {
            Log.e("LIBRARY~", "setImageView-3()");
        }
    }

    private void setRecyclerView() {
        adapter = new UploadImagesRecyclerViewAdapter(Duke.uploadingImagesList, R.layout.upload_preview_list_item, getContext(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        imagesRecyclerView.setLayoutManager(layoutManager);
        imagesRecyclerView.setHasFixedSize(true);
        imagesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        imagesRecyclerView.setAdapter(adapter);
        if (Duke.uploadingImagesList.size() >= 2) {
            scrollView.setVisibility(View.VISIBLE);
        } else {
            scrollView.setVisibility(View.INVISIBLE);
        }
        /**Restrict user to max 5 images**/
        /*if (Duke.uploadingImagesList.size() < 5 && !isLoadDocument && !Duke.isDocumentBeingScanned) {
            addImageView.setVisibility(View.VISIBLE);
        }*/ //for single Image scan
        if (Duke.uploadingImagesList.size() < 5 && !isLoadDocument) {
            addImageView.setVisibility(View.VISIBLE);
        }//for Multi Image scan
        System.out.println("LIBRARY~" + "setRecyclerView()");
        Log.e("LIBRARY~", "setRecyclerView()");
    }

    private void setCustomHeader(View v) {
        customHeader = new CustomToolbar(getContext());
        customHeader.initViews(v);
        customHeader.hideHeaderImage();
        customHeader.hideHeaderLable();
        customHeader.hideToolbarTitle();
        customHeader.hideToolbarBack();

        if(Duke.isDocumentBeingScanned) {
            customHeader.setToolbarTitle("Scan");
        } else {
            customHeader.setToolbarTitle(getString(R.string.upload));
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UploadDocumentInterface) {
            uploadDocumentInterface = (UploadDocumentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement UploadDocumentInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        uploadDocumentInterface = null;
        popupActions = null;
        if (customProgressLoader != null && customProgressLoader.isShowing()) {
            customProgressLoader.hideDialog();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (customProgressLoader != null && customProgressLoader.isShowing()) {
            customProgressLoader.hideDialog();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Duke.letUserAdjustCrop = false;
        if (customProgressLoader != null && customProgressLoader.isShowing()) {
            customProgressLoader.hideDialog();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (customProgressLoader != null && customProgressLoader.isShowing()) {
            customProgressLoader.hideDialog();
        }
    }

//    TODO: @OnClick(R.id.add_picture)
    void onClickAddPicture() {
        // Firebase: Send click upload button event
        Bundle params = new Bundle();
//        params.putString("Page", "Scan_Document");
        params.putString("Page", "Upload_Document");
        if (uploadDocumentInterface != null) {
            uploadDocumentInterface.uploadDocumentListener(false);
        }
    }

//    TODO: @OnClick(R.id.cancel_action)
    void onClickCancelAction() {
        // Firebase: Send click cancel button event
        Bundle params = new Bundle();
        params.putString("Button", "Cancel");
        uploadAs = null;
        confirmationComponent = new ConfirmationComponent(getContext(), getString(R.string.confirmation),
                getString(R.string.cancel_upload), false,
                getString(R.string.yes), getString(R.string.no),
                popupActions, 1);
    }

    public void uploadFiles(String type) {
        type = "AllAsOne";
        uploadButton.setEnabled(false);
        customProgressLoader.showDialog();

        bitmapsCount.clear();
        switch (type) {
            case AppConstants.UploadSelectionConstants.UPLOAD:
                bitmapsCount.add(1);
                break;
            case AppConstants.UploadSelectionConstants.ALL_AS_ONE:
                bitmapsCount.add(Duke.uploadingImagesList.size());
                break;
            case AppConstants.UploadSelectionConstants.ONE_AS_ONE:
                for (int i = 0; i < Duke.uploadingImagesList.size(); i++) {
                    bitmapsCount.add(1);
                }
                break;
        }

        if (Duke.uploadingImageStoragePaths.size() <= 0 && Duke.uploadingPDFStoragePaths.size() <= 0) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    customProgressLoader.hideDialog();
                }
            });

            return;
        }
        System.out.println("LIBRARY~" + "uploadFiles()~1");
        Log.e("LIBRARY~", "uploadFiles()~1");

        MultipartBody.Part[] list = new MultipartBody.Part[Duke.uploadingImageStoragePaths.size()];
        MultipartBody.Part[] pdfList;
        list = Utilities.getMultipartBody(Duke.uploadingImageStoragePaths, false, screenWidth, screenHeight);
        Log.d("as", Duke.PDFDocURIs.toString());
        pdfList = Utilities.getPDFMultipartBody(getActivity(), null, Duke.PDFDocURIs, screenWidth, screenHeight);

        MultipartBody.Part fileCount = Utilities.getFileCountArray(bitmapsCount);
        MultipartBody.Part address = MultipartBody.Part.createFormData("address", currentLocation);
        String data = latitude+","+longitude;
        coordinates = MultipartBody.Part.createFormData("coordinates",  data);
//        coordinates = latitude+","+longitude;
//        cordinatesArray.add(latitude);
//        cordinatesArray.add(longitude);

        list2 = list;
        count2 = fileCount;

        System.out.println("LIBRARY~" + "uploadFiles()~2");
        Log.e("LIBRARY~", "uploadFiles()~2");

        if (list != null && pdfList != null && list.length <= 0 && pdfList.length<=0) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    customProgressLoader.hideDialog();
                }
            });
            return;
        }

        String[] filenames = new String[Duke.uploadingImageStoragePaths.size()];
        ArrayList<String> filenamesArr = new ArrayList<>();

        for(int i = 0; i<filenames.length; i++) {
            filenames[i] = '"' + Utilities.getFileName(i) + '"';
        }

        for(int i=0; i<Duke.uploadingImageStoragePaths.size(); i++) {
            filenamesArr.add(Utilities.getFileName(i));
        }


        if(Duke.isDocumentAddingToLoad && Duke.selectedLoadUUID.length()>0) {
            System.out.println("LIBRARY~" + "uploadFiles()~3");
            Log.e("LIBRARY~", "uploadFiles()~3");
            MultipartBody.Part signature = MultipartBody.Part.createFormData("signature", "");

            MultipartBody.Part is_scan = MultipartBody.Part.createFormData("is_scan", "false");
            MultipartBody.Part manifest;

            JSONObject manifestObj = new JSONObject();
            JSONObject cordinatesObj = new JSONObject();

            for(int i =0;i<cordinatesArray.size();i++){
                if(i==0){
                    try {
                        cordinatesObj.put("lat",cordinatesArray.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(i==1){
                    try {
                        cordinatesObj.put("long",cordinatesArray.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


            //Generate load object API
            uploadFileViewModel.getManifestLiveData(getContext()).observe(getActivity(), new Observer<GenericResponseWithJSONModel>() {
                @Override
                public void onChanged(GenericResponseWithJSONModel jsonObject) {
                    try {


                        manifestObj.put("sha1", jsonObject.getData().getAsJsonObject("manifest").get("sha1").getAsString());
                        ArrayList<String> combinedDocs = new ArrayList<>();
                        combinedDocs.addAll(Duke.PDFDocFilenames);
                        combinedDocs.addAll(filenamesArr);
                        manifestObj.put("filenames", new JSONArray(combinedDocs));
//                        manifestObj.put("filenames", new JSONArray(filenamesArr));
                        MultipartBody.Part load_flag = MultipartBody.Part.createFormData("load_flag", Duke.selectedLoadUUID);
                        MultipartBody.Part manifest = MultipartBody.Part.createFormData("manifest", manifestObj.toString());
//                        MultipartBody.Part coordinates = MultipartBody.Part.createFormData("coordinates",cordinatesObj.toString());
                        coordinates = MultipartBody.Part.createFormData("coordinates", data);

                        uploadFileViewModel.getUploadFileResponseModelLiveData(address, load_flag, signature, is_scan,coordinates, manifest, list2, pdfList,getContext()).observe(getActivity(), new Observer<UploadFileResponseModel>() {
                            @Override
                            public void onChanged(UploadFileResponseModel uploadFileResponseModel) {
                                customProgressLoader.hideDialog();
                                Bundle bundle = new Bundle();

                                if (uploadFileResponseModel != null && uploadFileResponseModel.getStatus() != null && uploadFileResponseModel.getStatus().toLowerCase().equals(ApiConstants.ERRORS.SUCCESS.toLowerCase())) {
                                    bundle.putString("upload_status", "Document upload successful!");
                                    bundle.putString(AppConstants.StringConstants.ADD_DOC_TO_LOAD, "Document added successfully!");
                                } else {
                                    bundle.putString("upload_status", "Document upload failed!");
                                    bundle.putString(AppConstants.StringConstants.ADD_DOC_TO_LOAD, "Document upload failed!");
                                }

                                bundle.putString(AppConstants.StringConstants.NAVIGATE_TO, AppConstants.StringConstants.PROCESS);
                                refreshLoadDocs(bundle);

//                                if(uploadFileResponseModel.getMessage() != null) {
//                                    if(uploadFileResponseModel.getStatus().toLowerCase().equals("success")) {
//                                        bundle.putString("upload_status", "Document upload successful!");
//                                    } else {
//                                        bundle.putString("upload_status", "Document upload failed!");
//                                    }
//                                }
//                                NavigationFlowManager.openFragments(new LoadsFragment(), bundle, getActivity(), R.id.dashboard_wrapper);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if(Duke.isDocumentBeingScanned) {

            MultipartBody.Part signature = MultipartBody.Part.createFormData("signature", "");

            MultipartBody.Part is_scan = MultipartBody.Part.createFormData("is_scan", "true");

            MultipartBody.Part manifest;

            JSONObject manifestObj = new JSONObject();
            JSONObject cordinatesObj = new JSONObject();
            for(int i =0;i<cordinatesArray.size();i++){
                if(i==0){
                    try {
                        cordinatesObj.put("lat",cordinatesArray.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(i==1){
                    try {
                        cordinatesObj.put("long",cordinatesArray.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            uploadFileViewModel.getManifestLiveData(getContext()).observe(getActivity(), new Observer<GenericResponseWithJSONModel>() {
                @Override
                public void onChanged(GenericResponseWithJSONModel jsonObject) {
                    try {
                        //                        cordinatesObj.put("lat",new JSONArray(latitude));
//                        cordinatesObj.put("long",new JSONArray(longitude));
                        ObjectMapper objectMapper = new ObjectMapper();
                        manifestObj.put("sha1", jsonObject.getData().getAsJsonObject("manifest").get("sha1").getAsString());
                        ArrayList<String> combinedDocs = new ArrayList<>();
                        combinedDocs.addAll(Duke.PDFDocFilenames);
                        combinedDocs.addAll(filenamesArr);
                        manifestObj.put("filenames", new JSONArray(combinedDocs));

                        MultipartBody.Part load_flag = MultipartBody.Part.createFormData("load_flag", "");
                        MultipartBody.Part manifest = MultipartBody.Part.createFormData("manifest", manifestObj.toString());
//                        MultipartBody.Part coordinates = MultipartBody.Part.createFormData("coordinates",cordinatesObj.toString());

                        uploadFileViewModel.getUploadFileResponseModelLiveData(address, load_flag, signature, is_scan,coordinates, manifest, list2, pdfList,getContext()).observe(getActivity(), new Observer<UploadFileResponseModel>() {
                            @Override
                            public void onChanged(UploadFileResponseModel uploadFileResponseModel) {
                                customProgressLoader.hideDialog();
                                if(uploadFileResponseModel.getMessage() != null) {
//                                    if(uploadFileResponseModel.getStatus().toLowerCase().equals("success")) {
//                                        bundle.putString("upload_status", "Document scanned successfully!");
//                                    } else {
//                                        bundle.putString("upload_status", "Document scanning failed!");
//                                    }
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(DashboardActivity.IS_DOC_SCAN_SUCCESSFUL, true);
                                    NavigationFlowManager.openFragments(Duke.loadsFragment, bundle, getActivity(), R.id.dashboard_wrapper);
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } else if(Duke.isNewLoadBeingCreated) {

            MultipartBody.Part signature = MultipartBody.Part.createFormData("signature", "");

            MultipartBody.Part is_scan = MultipartBody.Part.createFormData("is_scan", "false");
            MultipartBody.Part manifest;

            JSONObject manifestObj = new JSONObject();
            JSONObject cordinatesObj = new JSONObject();

            loadsViewModel.createLoad(getContext()).observe(this, new Observer<CreateLoadModel>() {
                @Override
                public void onChanged(CreateLoadModel createLoadModel) {
                    //Generate load object API
                    JsonObject payload = new JsonObject();
                    payload.addProperty("load_flag", createLoadModel.getData());

                    uploadFileViewModel.getManifestLiveData(getContext()).observe(getActivity(), new Observer<GenericResponseWithJSONModel>() {
                        @Override
                        public void onChanged(GenericResponseWithJSONModel jsonObject) {
                            try {

                                ObjectMapper objectMapper = new ObjectMapper();
                                manifestObj.put("sha1", jsonObject.getData().getAsJsonObject("manifest").get("sha1").getAsString());
//                                manifestObj.put("filenames", new JSONArray(filenamesArr));
                                ArrayList<String> combinedDocs = new ArrayList<>();
                                combinedDocs.addAll(Duke.PDFDocFilenames);
                                combinedDocs.addAll(filenamesArr);
                                manifestObj.put("filenames", new JSONArray(combinedDocs));
//                                manifestObj.accumulate("filenames", filenamesArr);

                                MultipartBody.Part load_flag = MultipartBody.Part.createFormData("load_flag", createLoadModel.getData());
                                MultipartBody.Part manifest = MultipartBody.Part.createFormData("manifest", manifestObj.toString());

                                uploadFileViewModel.getUploadFileResponseModelLiveData(address, load_flag, signature, is_scan,coordinates, manifest, list2, pdfList,getContext()).observe(getActivity(), new Observer<UploadFileResponseModel>() {
                                    @Override
                                    public void onChanged(UploadFileResponseModel uploadFileResponseModel) {
                                        customProgressLoader.hideDialog();
//                                        Log.d("sdf", uploadFileResponseModel.getStatus());
//                                        Log.d("sdf", uploadFileResponseModel.getStatus());
                                        Bundle bundle = new Bundle();
                                        if(uploadFileResponseModel.getMessage() != null) {
                                            if(uploadFileResponseModel.getStatus().toLowerCase().equals("success")) {
                                                bundle.putString("upload_status", "Document upload successful!");
                                            } else {
                                                bundle.putString("upload_status", "Document upload failed!");
                                            }
//                                            if(uploadFileResponseModel.getMessage().contains("Upload queued")) {
//                                                bundle.putString("upload_status", "Document upload successful!");
//                                            } else {
//                                                bundle.putString("upload_status", "Document upload failed!");
//                                            }
                                        }
                                        NavigationFlowManager.openFragments(Duke.loadsFragment, bundle, getActivity(), R.id.dashboard_wrapper);
                                    }
                                });
                            } catch (Exception e) {
                                customProgressLoader.hideDialog();
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        } else  {
            uploadFileViewModel.uploadFile(list2, count2, address,getContext()).observe(getActivity(), new Observer<FileUploadSuccessModel>() {
                @Override
                public void onChanged(@Nullable FileUploadSuccessModel jsonElement) {
                    uploadButton.setEnabled(true);
                    uploadAs = null;
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        customProgressLoader.hideDialog();
//                    }
//                });
                    if (jsonElement != null && jsonElement.getCode() != null && jsonElement.getCode().equals(ApiConstants.ERRORS.SUCCESS)) {
                        resetFileUploads();
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(DashboardActivity.IS_DOCUMENT_UPLOAD_SUCCESSFUL, true);
                        NavigationFlowManager.openFragments(Duke.loadsFragment, bundle, getActivity(), R.id.dashboard_wrapper);
                    } else {
                        if (jsonElement != null && jsonElement.getMessage() != null) {
                            /**Dismiss Dialogs(If any)**/
                            if (uploadStatusDialog != null)
                                uploadStatusDialog.dismiss();
                            /**Show dialog**/
                            uploadStatusDialog = new UploadStatusDialog(getContext(), 10, UploadStatusDialog.DialogType.UPLOAD_ERROR, jsonElement.getMessage(), new UploadStatusClickActions() {
                                @Override
                                public void onButtonCick(int dialogId, int type) {
                                    uploadStatusDialog.dismiss();
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(DashboardActivity.IS_DOCUMENT_UPLOAD_SUCCESSFUL, false);
                                    NavigationFlowManager.openFragments(Duke.loadsFragment, bundle, getActivity(), R.id.dashboard_wrapper);
                                }
                            });
                            uploadStatusDialog.setCancelable(true);
                            uploadStatusDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    customProgressLoader.hideDialog();
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    private void refreshLoadDocs(Bundle bundle) {
        DocsOfALoad.clear();
        if(Duke.selectedLoadUUID.length() > 1) {
            loadsViewModel.getGetLoadDetailLiveData(Duke.selectedLoadUUID,getContext()).observe(this, new Observer<SingleLoadModel>() {
                @Override
                public void onChanged(SingleLoadModel singleLoadModel) {
                    if(singleLoadModel.getUserLoadsModels().getDocuments().size()>0) {
                        DocsOfALoad.addAll(singleLoadModel.getUserLoadsModels().getDocuments());
                    }
                    isLoadDocument = true;
                    customProgressLoader.hideDialog();
                    NavigationFlowManager.openFragments(Duke.loadsFragment, bundle, getActivity(), R.id.dashboard_wrapper);
//                    NavigationFlowManager.openFragments(Documents.newInstance(), bundle, getActivity(), R.id.dashboard_wrapper, AppConstants.StringConstants.PROCESS);
                }
            });
        }
    }

    private String getManifestObject(String[] filenames) {
        JSONObject manifestObj = new JSONObject();

//        loadsViewModel.createLoad().observe(this, new Observer<CreateLoadModel>() {
//            @Override
//            public void onChanged(CreateLoadModel createLoadModel) {
//                //Generate load object API
//                JsonObject payload = new JsonObject();
//                payload.addProperty("load_flag", createLoadModel.getData());
//                uploadFileViewModel.getManifestLiveData().observe(getActivity(), new Observer<JsonObject>() {
//                    @Override
//                    public void onChanged(JsonObject jsonObject) {
//                        try {
//                            manifestObj.put("sha1", jsonObject.getAsJsonObject("manifest").get("sha1"));
//                            manifestObj.put("filenames", filenames);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });

        return manifestObj.toString();
    }

//    TODO: @OnClick(R.id.upload_button)
    void onClickUploadButton() {
       /* if (Duke.uploadingImagesList != null && Duke.uploadingImagesList.size() > 1 && (!Duke.isLoadDocument && !Duke.isNewLoadBeingCreated && !Duke.isDocumentAddingToLoad)) {
            uploadSelectionBottomSheet = new UploadSelectionBottomSheet(getContext(), this);
        } else {*/
//            uploadAs = AppConstants.UploadSelectionConstants.UPLOAD;
            uploadAs = "AllAsOne";
//            uploadFiles(AppConstants.UploadSelectionConstants.UPLOAD);
            uploadFiles("AllAsOne");
            Bundle params = new Bundle();
            params.putString("Button", "Directly upload");

            // Firebase: Send UPLOAD event
            UserConfig userConfig = UserConfig.getInstance();
            UserDataModel userDataModel;
            userDataModel = userConfig.getUserDataModel();
            Bundle params1 = new Bundle();
            params1.putString("UserEmail", userDataModel.getUserEmail());
            params1.putString("Type", "Directly upload");
//            confirmationComponent = new ConfirmationComponent(getContext(), getString(R.string.confirmation), getString(R.string.upload_this_image), false, getString(R.string.yes), getString(R.string.no), popupActions,1);
//        }
    }

    public void resetFileUploads() {
        Duke.imageStoragePath = "";
        Duke.uploadingImageStoragePaths = new ArrayList<>();
        Duke.uploadingImagesList = new ArrayList<>();
        Duke.sortedUploadingImageStoragePaths = new ArrayList<>();
        Duke.sortedImagesList = new ArrayList<>();
        Duke.uploadingImageCount = new ArrayList<>();
    }

    @Override
    public void onUploadImagePreviewClickListener(Bitmap bm) {
        setImageView(bm);
    }

    @Override
    public void onPopupActions(String id, int dialogId) {
        if (uploadAs != null && uploadAs.equals(AppConstants.UploadSelectionConstants.ONE_AS_ONE)) {
            switch (id) {
                case AppConstants.PopupConstants.POSITIVE:
                    confirmationComponent.dismiss();
                    uploadFiles(AppConstants.UploadSelectionConstants.ONE_AS_ONE);
                    break;
                case AppConstants.PopupConstants.NEGATIVE:
                    confirmationComponent.dismiss();
                    break;
            }
        } else if (uploadAs != null && uploadAs.equals(AppConstants.UploadSelectionConstants.ALL_AS_ONE)) {
            switch (id) {
                case AppConstants.PopupConstants.POSITIVE:
                    confirmationComponent.dismiss();
                    uploadFiles(AppConstants.UploadSelectionConstants.ALL_AS_ONE);
                    break;
                case AppConstants.PopupConstants.NEGATIVE:
                    confirmationComponent.dismiss();
                    break;
            }
        } else if (uploadAs != null && uploadAs.equals(AppConstants.UploadSelectionConstants.UPLOAD)) {
            switch (id) {
                case AppConstants.PopupConstants.POSITIVE:
                    confirmationComponent.dismiss();
                    uploadFiles(AppConstants.UploadSelectionConstants.UPLOAD);
                    break;
                case AppConstants.PopupConstants.NEGATIVE:
                    confirmationComponent.dismiss();
                    break;
            }
        } else {
            switch (id) {
                case AppConstants.PopupConstants.POSITIVE:
                    confirmationComponent.dismiss();
                    resetFileUploads();
                    NavigationFlowManager.openFragments(Duke.loadsFragment, null, getActivity(), R.id.dashboard_wrapper);
                    break;
                case AppConstants.PopupConstants.NEGATIVE:
                    confirmationComponent.dismiss();
                    break;
                case AppConstants.PopupConstants.NEUTRAL:
                    confirmationComponent.dismiss();
                    NavigationFlowManager.openFragments(new UploadFailureFragment(), null, getActivity(), R.id.dashboard_wrapper);
                    break;
            }
        }
    }

    @Override
    public void onUploadSelection(String string) {
        Duke.letUserAdjustCrop = false;
//        uploadSelectionBottomSheet.hideDialog();
        UserConfig userConfig = UserConfig.getInstance();
        UserDataModel userDataModel;
        userDataModel = userConfig.getUserDataModel();
        Bundle params = new Bundle();
        Bundle params1 = new Bundle();
        switch (string) {
            case AppConstants.UploadSelectionConstants.ONE_AS_ONE:
                uploadAs = AppConstants.UploadSelectionConstants.ONE_AS_ONE;
                params.putString("Button", "One As One");

                params1.putString("Type", "One As One");
                params1.putString("UserEmail", userDataModel.getUserEmail());
                uploadFiles(AppConstants.UploadSelectionConstants.ONE_AS_ONE);
//                confirmationComponent = new ConfirmationComponent(getContext(), getString(R.string.confirmation), getString(R.string.upload_each_as_one_file), false, getString(R.string.yes), getString(R.string.no), popupActions,1);
                break;
            case AppConstants.UploadSelectionConstants.ALL_AS_ONE:
                uploadAs = AppConstants.UploadSelectionConstants.ALL_AS_ONE;
                params.putString("Button", "All As One");

                params1.putString("Type", "All As One");
                params1.putString("UserEmail", userDataModel.getUserEmail());
                uploadFiles(AppConstants.UploadSelectionConstants.ALL_AS_ONE);
//                confirmationComponent = new ConfirmationComponent(getContext(), getString(R.string.confirmation), getString(R.string.upload_all_images_as_one_file), false, getString(R.string.yes), getString(R.string.no), popupActions,1);
                break;
            case AppConstants.UploadSelectionConstants.MANUAL:
                params.putString("Button", "Manual configuration");
                NavigationFlowManager.openFragments(new SelectPhotoFragment(), null, getActivity(), R.id.dashboard_wrapper);
                break;
        }
    }

    @Override
    public void onClickProfile() {

    }

    @Override
    public void onBackClicked() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        int backStackLength = fm.getBackStackEntryCount();
        if (backStackLength > 1) {
            fm.popBackStack();
        }
    }

    @Override
    public void onClickToolbarTitle() {

    }

    @Override
    public void onClickHeaderTitle() {

    }

    private void setCurrentTheme() {
        ChangeThemeModel changeThemeModel = new ChangeThemeModel();
        closeMark.setColorFilter(Color.parseColor(changeThemeModel.getFloatingButtonColor()));
        cancleLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(changeThemeModel.getFloatingButtonBackgroundColor())));
        camIcon.setColorFilter(Color.parseColor(changeThemeModel.getFloatingButtonColor()));
        addImageView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(changeThemeModel.getFloatingButtonBackgroundColor())));
//        String rescanTxt = "Rescan?";
//        SpannableString spannableString = new SpannableString(rescanTxt);
//        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
//        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), 0);
//
//        rescan.setText(spannableString);
    }

    public void navigateToRescan(View view) {
        Duke.letUserAdjustCrop = true;
        resetFileUploads();
        Bundle params = new Bundle();
        params.putString("Page", "Upload_Document");
        if (uploadDocumentInterface != null) {
            uploadDocumentInterface.uploadDocumentListener(false);
        }
    }
}
