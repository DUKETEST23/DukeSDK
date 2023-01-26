package com.dukeai.manageloads.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.dukeai.manageloads.R;
import com.dukeai.manageloads.views.CustomHeader;


public class UploadFailureFragment extends Fragment {

    View failureView;
    CustomHeader customHeader;
    private OnFragmentInteractionListener mListener;

    public UploadFailureFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UploadFailureFragment newInstance(String param1, String param2) {
        UploadFailureFragment fragment = new UploadFailureFragment();
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
        failureView = inflater.inflate(R.layout.fragment_upload_failure, container, false);
        setCustomHeader();
        return failureView;
    }

    private void setCustomHeader() {
        customHeader.setToolbarTitle(getString(R.string.upload));
        customHeader.showHideHeaderTitle(View.GONE);
        customHeader.showHideProfileImage(View.GONE);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
