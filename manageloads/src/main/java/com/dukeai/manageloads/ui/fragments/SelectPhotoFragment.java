package com.dukeai.manageloads.ui.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.R;
import com.dukeai.manageloads.adapters.UploadImageSelectionAdapter;
import com.dukeai.manageloads.interfaces.PopupActions;
import com.dukeai.manageloads.interfaces.SelectPhotoListener;
import com.dukeai.manageloads.utils.AppConstants;
import com.dukeai.manageloads.utils.NavigationFlowManager;
import com.dukeai.manageloads.views.ConfirmationComponent;
import com.dukeai.manageloads.views.CustomHeader;

import java.util.ArrayList;
import java.util.HashMap;



public class SelectPhotoFragment extends Fragment implements SelectPhotoListener, PopupActions {

    View selectPhotoView;
    CustomHeader commonHeader;
    RecyclerView selectPicturesRecycler;
    Context context;
    UploadImageSelectionAdapter adapter;
    ArrayList<Bitmap> bitmapImagesList = new ArrayList<>();
    ArrayList<Bitmap> imageList = new ArrayList<>();
    ArrayList<Integer> imageCountArray = new ArrayList<>();
    HashMap<Integer, Integer> imagesSelectedPositions = new HashMap<>();
    ArrayList<Integer> selectedImagesPosition = new ArrayList<>();
    ConfirmationComponent confirmationComponent;
    String uploadAs = "";
    PopupActions popupActions;
    Boolean isFromUpload = false;
    private OnFragmentInteractionListener mListener;

    public SelectPhotoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SelectPhotoFragment newInstance() {
        SelectPhotoFragment fragment = new SelectPhotoFragment();
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
        selectPhotoView = inflater.inflate(R.layout.fragment_select_photo, container, false);
        context = getContext();
        popupActions = this;
        setInitials();
        return selectPhotoView;
    }

    private void setInitials() {
        uploadAs = null;
        Bundle args = getArguments();
        setImagesData();
        setEmptyRecycler();
        if (args != null && args.getBoolean(AppConstants.UploadConstants.FROM_UPLOAD_SELECTION)) {
            isFromUpload = true;
            bitmapImagesList.clear();
//            onClickReselectGroup();
        } else {
            isFromUpload = false;
            resetImages();
            imageList.clear();
            Duke.uploadingImageCount.clear();
            setRecyclerView();
        }
        selectedImagesPosition.clear();
        setCommonHeader();
    }

    private void setImagesData() {
        this.bitmapImagesList.clear();
        this.bitmapImagesList.addAll(Duke.uploadingImagesList);
    }


    private void setRecyclerView() {
        adapter.updateImagesList(this.bitmapImagesList);
    }

    private void setEmptyRecycler() {
        adapter = new UploadImageSelectionAdapter(context, R.layout.layout_upload_image_selection_item, this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        selectPicturesRecycler.setLayoutManager(mLayoutManager);
        selectPicturesRecycler.setHasFixedSize(true);
        selectPicturesRecycler.setItemAnimator(new DefaultItemAnimator());
        selectPicturesRecycler.setAdapter(adapter);
        selectPicturesRecycler.setFocusable(false);
    }

    private void setCommonHeader() {
        commonHeader.showHideBackButton(View.VISIBLE);
        commonHeader.showHideHeaderTitle(View.GONE);
        commonHeader.showHideProfileImage(View.GONE);
        commonHeader.setToolbarTitle(getString(R.string.select_photo));
//        commonHeader.setHeaderActions(this);
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSelectPhoto(ArrayList<Integer> bitmapsCount) {
        selectedImagesPosition.clear();
        selectedImagesPosition.addAll(bitmapsCount);
    }

    void resetImages() {
        int count = Duke.uploadingImagesList.size();
        Duke.sortedImagesList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Duke.sortedImagesList.add(i, null);
        }
    }

    // TODO: @OnClick(R.id.re_select_group)
    void onClickReselectGroup() {
        if (isImageSelected()) {
            adapter.updateImagesList(bitmapImagesList);
            selectedImagesPosition.clear();
        } else {
            if (Duke.uploadingImageCount != null && Duke.uploadingImageCount.size() > 0 && bitmapImagesList.size() != Duke.uploadingImagesList.size()) {
                int size = Duke.uploadingImageCount.size() - 1;
                int elementsToRemove = Duke.uploadingImageCount.get(size);
                Duke.uploadingImageCount.remove(size);

                int length = Duke.sortedImagesList.size() - 1;
                while (length >= 0 && elementsToRemove > 0) {
                    if (Duke.sortedImagesList.get(length) != null) {
                        Duke.sortedImagesList.set(length, null);
                        elementsToRemove--;
                    }
                    length--;
                }

                bitmapImagesList.clear();
                for (Bitmap bitmap : Duke.uploadingImagesList) {
                    if (Duke.sortedImagesList.indexOf(bitmap) == -1) {
                        bitmapImagesList.add(bitmap);
                    }
                }
                if (Duke.uploadingImagesList.size() == bitmapImagesList.size()) {
                    Duke.uploadingImageCount.clear();
                    resetImages();
                }
                adapter.updateImagesList(bitmapImagesList);
            }
        }
    }

    Boolean isImageSelected() {
        for (int i = 0; i < selectedImagesPosition.size(); i++) {
            if (selectedImagesPosition.get(i) > 0) {
                return true;
            }
        }
        return false;
    }

    // TODO: @OnClick(R.id.cancel_action)
    void onClickCancel() {
        uploadAs = AppConstants.UploadConstants.CANCEL;
        confirmationComponent = new ConfirmationComponent(getContext(), getString(R.string.confirmation), getString(R.string.cancel_upload), false, getString(R.string.yes), getString(R.string.no), popupActions, 1);
    }

    // TODO: @OnClick(R.id.group_upload_button)
    void onClickGroupAsOneFile() {
        int selectedCount = 0, count = selectedImagesPosition.size();

        int initialSize = 0;
        for (int i = 0; i < Duke.sortedImagesList.size(); i++) {
            if (Duke.sortedImagesList.get(i) != null) {
                initialSize++;
            }
        }

        for (int i = 0; i < count; i++) {
            if (selectedImagesPosition.get(i) > 0) {
                Duke.sortedImagesList.set(initialSize + selectedImagesPosition.get(i) - 1, bitmapImagesList.get(i - selectedCount));
                bitmapImagesList.remove(i - selectedCount);
                selectedCount++;
            }
        }
        if (selectedCount > 0) {
            Duke.uploadingImageCount.add(selectedCount);
            selectedImagesPosition.clear();
            if (bitmapImagesList != null && bitmapImagesList.size() > 0) {
                adapter.updateImagesList(bitmapImagesList);
            } else {
                NavigationFlowManager.openFragments(UploadSelectedFilesFragment.newInstance(), null, getActivity(), R.id.dashboard_wrapper);
            }
        }
    }

    @Override
    public void onPopupActions(String id, int dialogId) {
        if (uploadAs != null && uploadAs.equals(AppConstants.UploadConstants.CANCEL)) {
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
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
