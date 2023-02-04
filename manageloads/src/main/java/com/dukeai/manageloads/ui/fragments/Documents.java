package com.dukeai.manageloads.ui.fragments;

import static com.dukeai.manageloads.Duke.isLoadDocument;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.R;
import com.dukeai.manageloads.adapters.DocumentsViewPagerAdapter;
import com.dukeai.manageloads.interfaces.HeaderActions;
import com.dukeai.manageloads.interfaces.UpdateTabCount;
import com.dukeai.manageloads.interfaces.UpdateTabStylesListener;
import com.dukeai.manageloads.interfaces.UploadDocumentInterface;
import com.dukeai.manageloads.model.ChangeThemeModel;
import com.dukeai.manageloads.utils.AppConstants;
import com.dukeai.manageloads.utils.CustomToolbar;
import com.dukeai.manageloads.utils.NavigationFlowManager;
import com.dukeai.manageloads.utils.Utilities;
import com.dukeai.manageloads.views.CustomHeader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class Documents extends Fragment implements HeaderActions, UpdateTabStylesListener, UpdateTabCount {

    private String TAG = Documents.class.getSimpleName();
    private View documentsView;
    private String toFragment = AppConstants.StringConstants.IN_PROCESS;
    private TabLayout tabView;
    private ViewPager viewPager;
    private String currentFragment = "";
    private DocumentsViewPagerAdapter documentsViewPagerAdapter;
    private ProcessedFragment processedFragment;
    private CustomHeader customHeader;
    private FloatingActionButton uploadButton;
    private UploadDocumentInterface uploadDocumentInterface;

    public Documents() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Documents newInstance() {
        Documents fragment = new Documents();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        documentsView = inflater.inflate(R.layout.fragment_documents, container, false);
        initViews(documentsView);

        return documentsView;
        // Inflate the layout for this fragment
    }

    private void initViews(View documentsView) {
        Duke.documentsFragment = this;
        tabView = documentsView.findViewById(R.id.tab_layout_documents);
        viewPager = documentsView.findViewById(R.id.view_pager_documents);
        uploadButton = documentsView.findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpload();
            }
        });
        setButtonThemeColor();
        setCustomHeader(documentsView);

        View view = documentsView.findViewById(R.id.documents_header);
        ImageView back = view.findViewById(R.id.toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationFlowManager.openFragments(new LoadsFragment(), null, getActivity(), R.id.dashboard_wrapper);
            }
        });

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                NavigationFlowManager.openFragments(new LoadsFragment(), null, getActivity(), R.id.dashboard_wrapper);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);


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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        setCustomHeader();
//        setToolbarColor();
        getArgumentsData();
        setUpViewPager(viewPager);
        Duke.PDFDocFilenames.clear();
        Duke.PDFDocURIs.clear();
        tabView.setupWithViewPager(viewPager);
        // ITERATE OVER ALL TABS AND SET THE CUSTOM VIEW
        setCustomTabView();
        tabView.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    Bundle params = new Bundle();
                    params.putString("Type", "Processed");
                    currentFragment = AppConstants.StringConstants.PROCESS;
                }
                setCustomView(tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setCustomView(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                setCustomView(tab, true);
            }
        });

        if (toFragment.equals(AppConstants.StringConstants.PROCESS)) {
            Bundle params = new Bundle();
            params.putString("Type", "Processed");
            currentFragment = AppConstants.StringConstants.PROCESS;
            tabView.getTabAt(0).select();
        }

    }

    private void getArgumentsData() {
        Bundle args = getArguments();
        if (args != null && args.getString(AppConstants.StringConstants.NAVIGATE_TO) != null) {
            toFragment = args.getString(AppConstants.StringConstants.NAVIGATE_TO);
        }
        if (args != null && args.getString(AppConstants.StringConstants.DELETE_DOC_FROM_LOAD) != null) {
            Toast.makeText(getContext(), args.getString(AppConstants.StringConstants.DELETE_DOC_FROM_LOAD), Toast.LENGTH_LONG).show();
        }
        if (args != null && args.getString(AppConstants.StringConstants.ADD_DOC_TO_LOAD) != null) {
            Toast.makeText(getContext(), args.getString(AppConstants.StringConstants.ADD_DOC_TO_LOAD), Toast.LENGTH_LONG).show();
        }
    }

    private void setUpViewPager(ViewPager viewPager) {
        documentsViewPagerAdapter = new DocumentsViewPagerAdapter(getContext(), getChildFragmentManager(), this);
        processedFragment = new ProcessedFragment();
        documentsViewPagerAdapter.addFragment(processedFragment, getString(R.string.processed));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setSaveFromParentEnabled(false);
        viewPager.setAdapter(documentsViewPagerAdapter);

    }

    private void setCustomTabView() {
        for (int i = 0; i < tabView.getTabCount(); i++) {
            TabLayout.Tab tab = tabView.getTabAt(i);
            assert tab != null;
            tab.setCustomView(documentsViewPagerAdapter.getTabView(i));
        }
    }


    public void setCustomView(TabLayout.Tab tab, boolean isSelected) {
        View view = tab.getCustomView();
        if (view == null) {
            return;
        }
        TextView textView = view.findViewById(R.id.document_tab_item);
        if (isSelected) {
            textView.setTextSize(13);
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
            setHighlightedColor(textView);
        } else {
            textView.setTextSize(12);
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorCrete));
            setCurrentTheme(textView);

        }

        String processString = Utilities.getStrings(getContext(), R.string.processed);
        textView.setText(processString);

        View line = view.findViewById(R.id.item_indicator);
        setCurrentLineTheme(line);
        line.setVisibility(isSelected ? View.VISIBLE : View.INVISIBLE);
    }


    private void setCurrentTheme(TextView textView) {
        ChangeThemeModel changeThemeModel = new ChangeThemeModel();
        textView.setTextColor(Color.parseColor(changeThemeModel.getTabTextColor()));
    }

    private void setHighlightedColor(TextView textView) {
        ChangeThemeModel changeThemeModel = new ChangeThemeModel();
        textView.setTextColor(Color.parseColor(changeThemeModel.getHighlightedTabTextColor()));
    }

    private void setButtonThemeColor() {
        ChangeThemeModel changeThemeModel = new ChangeThemeModel();
        uploadButton.setColorFilter(Color.parseColor(changeThemeModel.getFloatingButtonColor()));
        uploadButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(changeThemeModel.getFloatingButtonBackgroundColor())));
    }

    @Override
    public void onClickProfile() {

    }

    @Override
    public void onBackClicked() {

    }

    @Override
    public void onClickToolbarTitle() {

    }

    @Override
    public void onClickHeaderTitle() {

    }

    @Override
    public void UpdateTabCount() {

    }

    @Override
    public void UpdateTabStyles() {

    }

    private void setCustomHeader(View v) {
        CustomToolbar headers = new CustomToolbar(getContext());
        headers.initViews(v);
        headers.hideHeaderImage();
        headers.hideHeaderLable();
        headers.setToolbarTitle(getString(R.string.title_activity_documents));
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setCurrentLineTheme(View line) {
        ChangeThemeModel changeThemeModel = new ChangeThemeModel();
        line.setBackgroundColor(Color.parseColor(changeThemeModel.getHighlightedTabTextColor()));
    }


    void onClickUpload() {
        if( ( (Duke.fileStatusModel.getMemberStatus().equals("NONE") || Duke.fileStatusModel.getMemberStatus().equals("FREE_POD") ) && Duke.fileStatusModel.getUploadLimit() - Duke.fileStatusModel.getUploadCount() == 0.0) ||
                ((Duke.fileStatusModel.getMemberStatus().equals("NONE") || Duke.fileStatusModel.getMemberStatus().equals("FREE_POD") ) && Duke.fileStatusModel.getUploadLimit() - Duke.fileStatusModel.getUploadCount() < 0.0)) {
            loadPaywall();
        } else {
            Bundle params = new Bundle();
            if (currentFragment == "REJECT") {
                params.putString("Page", "Document_rejected");
            } else if (currentFragment == "PROCESS") {
                params.putString("Page", "Document_processed");
            } else {
                params.putString("Page", "Document_" + currentFragment.toLowerCase());
            }

            if (uploadDocumentInterface != null) {
                Utilities.resetFileData();
                if(isLoadDocument) {
                    Duke.isNewLoadBeingCreated = false;
                    Duke.isDocumentAddingToLoad = true;
//                    Duke.isNewLoadBeingCreated = false;
//                    Duke.isDocumentAddingToLoad = true;
                }
                uploadDocumentInterface.uploadDocumentListener(false);
            }
        }
    }


    private void loadPaywall() {
     /*   Intent intent = new Intent(requireActivity(), PaymentActivity.class);
        requireActivity().startActivity(intent);
        requireActivity().finish();*/
    }

}