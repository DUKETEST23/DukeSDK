package com.dukeai.manageloads.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.R;
import com.dukeai.manageloads.adapters.UploadImageFilesAdapter;
import com.dukeai.manageloads.apiUtils.ApiConstants;
import com.dukeai.manageloads.interfaces.PopupActions;
import com.dukeai.manageloads.interfaces.UploadStatusClickActions;
import com.dukeai.manageloads.model.FileUploadSuccessModel;
import com.dukeai.manageloads.model.UserDataModel;
import com.dukeai.manageloads.ui.activities.DashboardActivity;
import com.dukeai.manageloads.utils.AppConstants;
import com.dukeai.manageloads.utils.CustomProgressLoader;
import com.dukeai.manageloads.utils.NavigationFlowManager;
import com.dukeai.manageloads.utils.UploadStatusDialog;
import com.dukeai.manageloads.utils.UserConfig;
import com.dukeai.manageloads.utils.Utilities;
import com.dukeai.manageloads.viewmodel.UploadFileViewModel;
import com.dukeai.manageloads.views.ConfirmationComponent;
import com.dukeai.manageloads.views.CustomHeader;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MultipartBody;

public class UploadSelectedFilesFragment extends Fragment implements PopupActions{

    View view;
    CustomHeader customHeader;
    RecyclerView recyclerView;
    Button uploadButton;
    ConfirmationComponent confirmationComponent;
    PopupActions popupActions;
    String upload;
    UploadImageFilesAdapter adapter;
    ArrayList<ArrayList<Bitmap>> bitmaps = new ArrayList<>();
    ArrayList<Integer> imageCount = new ArrayList<>();
    CustomProgressLoader customProgressLoader;
    UploadFileViewModel uploadFileViewModel;
    int screenWidth, screenHeight;
    UploadStatusDialog uploadStatusDialog;
    private OnFragmentInteractionListener mListener;
    // Firebase: Setup

    private String currentLocation = "none";
    FusedLocationProviderClient fusedLocationProviderClient;


    public UploadSelectedFilesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UploadSelectedFilesFragment newInstance() {
        UploadSelectedFilesFragment fragment = new UploadSelectedFilesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upload_selected_files, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        popupActions = this;
        setInitials();
        return view;
    }

    private void setInitials() {
        customProgressLoader = new CustomProgressLoader(getContext());
        uploadFileViewModel = ViewModelProviders.of(this).get(UploadFileViewModel.class);
        getWindowWidthAndHeight();
        upload = null;
        setCustomHeader();
        setRecyclerData();
        setRecyclerView();
        getSortedImageList();
    }

    private void getSortedImageList() {
        Duke.sortedUploadingImageStoragePaths.clear();
        for (Bitmap bitmap : Duke.sortedImagesList) {
            int index = Duke.uploadingImagesList.indexOf(bitmap);
            if (index != -1) {
                String imgPath = Duke.uploadingImageStoragePaths.get(index);
                Duke.sortedUploadingImageStoragePaths.add(imgPath);
            }
        }
    }

    private void setRecyclerData() {
        imageCount.clear();
        imageCount.addAll(Duke.uploadingImageCount);
        bitmaps.clear();
        int count = 0;
        for (int i = 0; i < imageCount.size(); i++) {
            int currentImagesSize = imageCount.get(i);
            ArrayList<Bitmap> innerBitmaps = new ArrayList<>();
            for (int j = 0; j < currentImagesSize; j++) {
                innerBitmaps.add(Duke.sortedImagesList.get(count));
                count++;
            }
            bitmaps.add(innerBitmaps);
        }
    }

    private void setRecyclerView() {
        adapter = new UploadImageFilesAdapter(getContext(), R.layout.layout_upload_image_file_item, bitmaps);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setCustomHeader() {
        customHeader.showHideBackButton(View.VISIBLE);
        customHeader.showHideHeaderTitle(View.GONE);
        customHeader.showHideProfileImage(View.GONE);
        customHeader.setToolbarTitle(getString(R.string.select_photo));
//        customHeader.setHeaderActions(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // TODO: @OnClick(R.id.cancel_action)
    void onClickCancel() {
        upload = AppConstants.UploadConstants.CANCEL;
        confirmationComponent = new ConfirmationComponent(getContext(), getString(R.string.confirmation), getString(R.string.cancel_upload), false, getString(R.string.yes), getString(R.string.no), popupActions, 1);
    }

    // TODO: @OnClick(R.id.upload_button)
    void onClickUpload() {
        UserConfig userConfig = UserConfig.getInstance();
        UserDataModel userDataModel;
        userDataModel = userConfig.getUserDataModel();
        Bundle params = new Bundle();
        params.putString("Type", "Manual");
        params.putString("UserEmail", userDataModel.getUserEmail());


        upload = AppConstants.UploadConstants.CONFIRM_UPLOAD;
        if(Duke.isLocationPermissionProvided) {
            getCurrentLocation();
        } else {
            uploadFiles(currentLocation);
        }
//        confirmationComponent = new ConfirmationComponent(getContext(), getString(R.string.confirmation), getString(R.string.finish_and_upload), false, getString(R.string.yes), getString(R.string.no), popupActions,1);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if(location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        Log.d("location fetched", addresses.get(0).getAddressLine(0));
                        if(addresses.get(0) != null && addresses.get(0).getAddressLine(0).length() > 0) {
                            currentLocation = addresses.get(0).getAddressLine(0);
                            uploadFiles(currentLocation);
                        } else {
                            currentLocation = "none";
                            uploadFiles(currentLocation);
                        }
                    } catch (Exception e) {
                        currentLocation = "none";
                        uploadFiles(currentLocation);
                        Log.d("location not fetched", e.getLocalizedMessage());
                    }
                }
            }
        });
    }

    // TODO: @OnClick(R.id.regroup)
    void onClickRegroup() {
        Bundle args = new Bundle();
        args.putBoolean(AppConstants.UploadConstants.FROM_UPLOAD_SELECTION, true);
        NavigationFlowManager.openFragments(SelectPhotoFragment.newInstance(), args, getActivity(), R.id.dashboard_wrapper);
    }

    @Override
    public void onPopupActions(String id, int dialogId) {
        if (upload != null && upload.equals(AppConstants.UploadConstants.CANCEL)) {
            switch (id) {
                case AppConstants.PopupConstants.POSITIVE:
                    confirmationComponent.dismiss();
                    NavigationFlowManager.openFragments(Duke.loadsFragment, null, getActivity(), R.id.dashboard_wrapper);
                    break;
                case AppConstants.PopupConstants.NEGATIVE:
                    confirmationComponent.dismiss();
                    break;
                case AppConstants.PopupConstants.NEUTRAL:
                    confirmationComponent.dismiss();
                    break;
            }
        } else if (upload != null && upload.equals(AppConstants.UploadConstants.CONFIRM_UPLOAD)) {
            switch (id) {
                case AppConstants.PopupConstants.POSITIVE:
                    confirmationComponent.dismiss();
                    if(Duke.isLocationPermissionProvided){
                        getCurrentLocation();
                    } else {
                        uploadFiles(currentLocation);
                    }
                    break;
                case AppConstants.PopupConstants.NEGATIVE:
                    confirmationComponent.dismiss();
                    break;
                case AppConstants.PopupConstants.NEUTRAL:
                    confirmationComponent.dismiss();
                    break;
            }
        } else if (upload != null && upload.equals(AppConstants.UploadConstants.UPLOAD_FAILURE)) {
            switch (id) {
                case AppConstants.PopupConstants.POSITIVE:
                    confirmationComponent.dismiss();
                    break;
                case AppConstants.PopupConstants.NEGATIVE:
                    confirmationComponent.dismiss();
                    break;
                case AppConstants.PopupConstants.NEUTRAL:
                    confirmationComponent.dismiss();
                    NavigationFlowManager.openFragments(new UploadFailureFragment(), null, getActivity(), R.id.dashboard_wrapper);
                    break;
            }
        } else {
            switch (id) {
                case AppConstants.PopupConstants.POSITIVE:
                    confirmationComponent.dismiss();
                    break;
                case AppConstants.PopupConstants.NEGATIVE:
                    confirmationComponent.dismiss();
                    break;
                case AppConstants.PopupConstants.NEUTRAL:
                    confirmationComponent.dismiss();
                    break;
            }
        }
    }

    private void getWindowWidthAndHeight() {
        screenWidth = Utilities.getScreenWidthInPixels(getActivity());
        screenHeight = Utilities.getScreenHeightInPixels(getActivity());
    }


    void uploadFiles(String streetAddress) {

        customProgressLoader.showDialog();
        uploadButton.setEnabled(false);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                if (Duke.sortedUploadingImageStoragePaths.size() <= 0) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            customProgressLoader.hideDialog();
                        }
                    });
                    return;
                }
                MultipartBody.Part[] list = new MultipartBody.Part[Duke.sortedUploadingImageStoragePaths.size()];
                list = Utilities.getMultipartBody(Duke.sortedUploadingImageStoragePaths, false, screenWidth, screenHeight);

                MultipartBody.Part fileCount = Utilities.getFileCountArray(Duke.uploadingImageCount);
                MultipartBody.Part address = MultipartBody.Part.createFormData("address", streetAddress);

                if (list != null && list.length <= 0) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            customProgressLoader.hideDialog();
                        }
                    });
                    return;
                }
                uploadFileViewModel.uploadFile(list, fileCount, address,getContext()).observe(getActivity(), new Observer<FileUploadSuccessModel>() {
                    @Override
                    public void onChanged(@Nullable FileUploadSuccessModel jsonElement) {
                        uploadButton.setEnabled(true);
                        upload = null;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                customProgressLoader.hideDialog();
                            }
                        });
                        if (jsonElement != null && jsonElement.getCode() != null && jsonElement.getCode().equals(ApiConstants.ERRORS.SUCCESS)) {
                            resetFileUploads();
                            Bundle bundle = new Bundle();
                            bundle.putBoolean(DashboardActivity.IS_DOCUMENT_UPLOAD_SUCCESSFUL, true);
                            NavigationFlowManager.openFragments(Duke.loadsFragment, bundle, getActivity(), R.id.dashboard_wrapper);
                        } else {
                            if (jsonElement != null && jsonElement.getMessage() != null) {
                                upload = AppConstants.UploadConstants.UPLOAD_FAILURE;
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
                            }
                        }
                    }
                });
            }
        });
    }


    private void resetFileUploads() {
        Duke.imageStoragePath = "";
        Duke.uploadingImageStoragePaths = new ArrayList<>();
        Duke.uploadingImagesList = new ArrayList<>();
        Duke.sortedImagesList = new ArrayList<>();
        Duke.uploadingImageCount = new ArrayList<>();
        Duke.sortedUploadingImageStoragePaths = new ArrayList<>();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
