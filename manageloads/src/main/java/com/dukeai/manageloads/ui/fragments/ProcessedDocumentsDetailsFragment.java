package com.dukeai.manageloads.ui.fragments;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.dukeai.manageloads.Duke.DocsOfALoad;
import static com.dukeai.manageloads.Duke.isLoadDocument;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterViewFlipper;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.R;
import com.dukeai.manageloads.adapters.ProcessedDocumentsFlipperAdapter;
import com.dukeai.manageloads.apiUtils.ApiConstants;
import com.dukeai.manageloads.interfaces.DocumentDetailsUpdateObserver;
import com.dukeai.manageloads.interfaces.HeaderActions;
import com.dukeai.manageloads.interfaces.PopupActions;
import com.dukeai.manageloads.interfaces.UpdateDocumentNavigationIcons;
import com.dukeai.manageloads.interfaces.UploadDocumentInterface;
import com.dukeai.manageloads.model.ChangeThemeModel;
import com.dukeai.manageloads.model.DocumentDetailsModel;
import com.dukeai.manageloads.model.DownloadImageModel;
import com.dukeai.manageloads.model.FileDeleteSuccessModel;
import com.dukeai.manageloads.model.ProcessedDocumentsModel;
import com.dukeai.manageloads.model.SingleLoadModel;
import com.dukeai.manageloads.model.UpdatePaymentModel;
import com.dukeai.manageloads.utils.AppConstants;
import com.dukeai.manageloads.utils.CustomProgressLoader;
import com.dukeai.manageloads.utils.CustomToolbar;
import com.dukeai.manageloads.utils.DateFormatter;
import com.dukeai.manageloads.utils.NavigationFlowManager;
import com.dukeai.manageloads.utils.Utilities;
import com.dukeai.manageloads.viewmodel.FileStatusViewModel;
import com.dukeai.manageloads.viewmodel.GenericResponseModel;
import com.dukeai.manageloads.viewmodel.LoadsViewModel;
import com.dukeai.manageloads.views.ConfirmationComponent;
import com.dukeai.manageloads.views.CustomHeader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

// Firebase: Setup

public class ProcessedDocumentsDetailsFragment extends Fragment implements HeaderActions, PopupActions, UpdateDocumentNavigationIcons {
    ProcessedDocumentsModel data;
    View processDocumentView;
    ProcessedDocumentsFlipperAdapter processedDocumentsFlipperAdapter;
    ArrayList<DownloadImageModel> dataModel = new ArrayList<>();

    UploadDocumentInterface uploadDocumentInterface;
    TextView billName;
    TextView billType;
    FloatingActionButton uploadButton;
    TextView processedDate;
    TextView amount;
    TextView mainCategoryLabel;
    TextView subCategoryLabel;
    AdapterViewFlipper adapterViewFlipper;
    ImageView prevButton;
    ImageView nextButton;
    CustomHeader customHeader;
    FloatingActionButton closeIcon;
    TextView amountUnits;
    FloatingActionButton signatureIcon;
    FloatingActionButton submitIcon;
    TextView submitText;
    EditText signature_text;
    RelativeLayout documentContainer;
    RelativeLayout rootLayout;

    FileStatusViewModel fileStatusViewModel;
    LoadsViewModel loadsViewModel;
    CustomProgressLoader customProgressLoader;
    DateFormatter dateFormatter;
    ConfirmationComponent confirmationComponent;
    PopupActions popupActions;
    int screenWidth, screenHeight;
    UpdateDocumentNavigationIcons updateDocumentNavigationIcons;
    int apiCount = 0;
    boolean isFromSearch = false;
    String documentSha1 = "";

    // Firebase: Setup
//    private FirebaseAnalytics mFirebaseAnalytics;

    public ProcessedDocumentsDetailsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProcessedDocumentsDetailsFragment newInstance() {
        ProcessedDocumentsDetailsFragment fragment = new ProcessedDocumentsDetailsFragment();
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
        processDocumentView = inflater.inflate(R.layout.fragment_processed_documents_view, container, false);
        popupActions = this;
        updateDocumentNavigationIcons = this;
        initViews(processDocumentView);
        setInitials();
        setCurrentTheme();
        // Firebase: Setup
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(Objects.requireNonNull(getActivity()));
        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
//                signature_text.setText(data.getSignature());
                setSignatureInterfaceState();
            }
        });
        return processDocumentView;
    }

    private void initViews(View v) {
        rootLayout = v.findViewById(R.id.root);
        billName = v.findViewById(R.id.bill_name);
        billType = v.findViewById(R.id.bill_type);
        mainCategoryLabel = v.findViewById(R.id.main_category_name);
        subCategoryLabel = v.findViewById(R.id.sub_category_name);
        processedDate = v.findViewById(R.id.processed_date);
        amountUnits = v.findViewById(R.id.amount_units);
        amount = v.findViewById(R.id.amount);
        documentContainer = v.findViewById(R.id.document_container);
        adapterViewFlipper = v.findViewById(R.id.image_flipper);
        prevButton = v.findViewById(R.id.previous_button);
        nextButton = v.findViewById(R.id.next_button);
        signature_text = v.findViewById(R.id.signature_text);
        closeIcon = v.findViewById(R.id.close_fab_icon);
        signatureIcon = v.findViewById(R.id.signature);
        submitIcon = v.findViewById(R.id.submit);
        submitText = v.findViewById(R.id.submit_text);
        uploadButton = v.findViewById(R.id.upload_button);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUploadButton();
            }
        });

        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRemoveIcon();
            }
        });

        signatureIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSignatureIcon();
            }
        });

        submitIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSubmitBtn();
            }
        });

        setCustomHeader(v);
    }

    private void setInitials() {
        apiCount = 0;
        getWindowWidthAndHeight();
        dateFormatter = new DateFormatter();
        fileStatusViewModel = ViewModelProviders.of(this).get(FileStatusViewModel.class);
        loadsViewModel = ViewModelProviders.of(this).get(LoadsViewModel.class);
        customProgressLoader = new CustomProgressLoader(getContext());
        Duke.PDFDocFilenames.clear();
        Duke.PDFDocURIs.clear();
        if (isLoadDocument) {
            amount.setVisibility(View.INVISIBLE);
            amountUnits.setVisibility(View.INVISIBLE);
        } else {
            amount.setVisibility(View.VISIBLE);
            amountUnits.setVisibility(View.VISIBLE);
        }
        // setCustomHeader();
        setArgumentsData(getArguments());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (uploadDocumentInterface != null) {
        }
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

    private void getWindowWidthAndHeight() {
        screenWidth = Utilities.getScreenWidthInPixels(getActivity());
        screenHeight = Utilities.getScreenHeightInPixels(getActivity());
    }


    public void setArgumentsData(Bundle bundle) {
        if (bundle != null && bundle.get(AppConstants.StringConstants.PROCESSED_DATA_MODEL) != null) {
            data = (ProcessedDocumentsModel) bundle.getSerializable(AppConstants.StringConstants.PROCESSED_DATA_MODEL);
            if (data != null) {
                updateDataInFragment(data);
                if (data.getSha1() != null) {
                    documentSha1 = data.getSha1();
                }
                if (data.getSignature() != null && data.getSignature().length() > 0) {
                    if (data.getProcessedData().getDocType().toLowerCase().equals("scan")) {
                        billName.setText(data.getSignature());
                        billName.setText(data.getSignature());
                    }
                    signature_text.setText(data.getSignature());
                }
                setImagesInFlipper(data.getPages());
            }
            boolean fromScreen = bundle.getBoolean("IS_FROM_SEARCH");
            if (fromScreen) {
                isFromSearch = true;
            }
            setDocumentCategory(documentSha1);
        }
    }

    private void setDocumentCategory(String sh1) {
        fetchDocumentDetails(new DocumentDetailsUpdateObserver() {
            @Override
            public void onChanged(String status, DocumentDetailsModel documentDetailsModel) {
                if (status.toLowerCase().equals("success")) {
//                    Log.d("details fetched", documentDetailsModel.toString());
                    Gson gson = new Gson();
                    String json = gson.toJson(documentDetailsModel);
                    JSONObject obj = null;
                    String mainCateogry = null;
                    String category = null;
                    try {
                        obj = new JSONObject(json);
                        JSONObject extractObj = (JSONObject) obj.get("extracts");
                        mainCateogry = (String) ((JSONObject) ((JSONObject) ((JSONObject) extractObj.getJSONArray("data").get(0)).get("category_details")).getJSONArray("Receipt").get(0)).get("MainCategory");
                        category = (String) ((JSONObject) ((JSONObject) ((JSONObject) extractObj.getJSONArray("data").get(0)).get("category_details")).getJSONArray("Receipt").get(0)).get("category");
                        Log.d("My App", String.valueOf(obj));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (mainCateogry != null && category != null) {
                        mainCategoryLabel.setText(mainCateogry);
                        subCategoryLabel.setText(category);
                        mainCategoryLabel.setVisibility(View.VISIBLE);
                        subCategoryLabel.setVisibility(View.VISIBLE);
                    }

                    Log.d("My App", obj.toString());
                } else {
//                    Log.d("details not fetched", documentDetailsModel.toString());
                }
            }
        });
    }

    private void fetchDocumentDetails(DocumentDetailsUpdateObserver documentDetailsUpdateObserver) {
        fileStatusViewModel.getDocumentDetails(documentSha1,getContext()).observe(
                getViewLifecycleOwner(), new Observer<DocumentDetailsModel>() {
                    @Override
                    public void onChanged(DocumentDetailsModel documentDetailsModel) {
                        try {
                            String msg = "";
                            if (documentDetailsModel != null) {
                                msg = "SUCCESS";
                            }
                            documentDetailsUpdateObserver.onChanged(msg, documentDetailsModel);
                        } catch (Exception e) {
                            e.printStackTrace();
                            /**Error occurred**/
                            documentDetailsUpdateObserver.onChanged("ERROR", documentDetailsModel);
                        }
                    }
                });
    }

    private void setImagesInFlipper(final ArrayList<String> pages) {
        dataModel.clear();
        if (pages != null && pages.size() > 0) {
            customProgressLoader.showDialog();
        }
        apiCount = 0;
        loadDocuments(pages);
    }

    private void loadDocuments(final ArrayList<String> pages) {
        fileStatusViewModel.downloadFile(pages.get(apiCount), screenWidth, screenHeight, true,getContext()).observe(getViewLifecycleOwner(), new Observer<DownloadImageModel>() {
            @Override
            public void onChanged(@Nullable DownloadImageModel downloadImageModel) {
                if (downloadImageModel != null) {
                    dataModel.add(downloadImageModel);
                    if ((pages.size()) == (dataModel.size())) {
                        customProgressLoader.hideDialog();
                        if (dataModel != null && dataModel.size() > 1) {
                            prevButton.setVisibility(View.VISIBLE);
                            nextButton.setVisibility(View.VISIBLE);
                        } else {
                            prevButton.setVisibility(View.GONE);
                            nextButton.setVisibility(View.GONE);
                        }
                        processedDocumentsFlipperAdapter = new ProcessedDocumentsFlipperAdapter(requireContext(), R.layout.view_flipper_imageview, dataModel, updateDocumentNavigationIcons);
                        adapterViewFlipper.setAdapter(processedDocumentsFlipperAdapter);
                    } else {
                        customProgressLoader.hideDialog();
                    }
                }
                apiCount++;
                if (apiCount < pages.size()) {
                    loadDocuments(pages);
                }
            }
        });
    }

    private void updateDataInFragment(ProcessedDocumentsModel data) {
        billName.setText(data.getProcessedData().getTitle());
        if (data.getProcessedData().getTitle().equals("Load Document")) {
            if (data.getSha1().length() > 0) {
                billName.setText(data.getProcessedData().getTitle() + " " + data.getSha1().substring(0, 2));
            } else {
                billName.setText(data.getProcessedData().getTitle());
            }
        } else if (data.getProcessedData().getDocType().toLowerCase().equals("scan")) {
            billName.setText(data.getProcessedData().getTitle() + " " + data.getSha1().substring(0, 2));
        } else {
            billName.setText(data.getProcessedData().getTitle());
        }
        billType.setText(data.getProcessedData().getDocType());
//        uploadDate.setText(dateFormatter.getFormattedDate(data.getUploadDate()));
        if (data.getProcessedDate().length() > 0) {
            processedDate.setText(dateFormatter.getFormattedDate(data.getProcessedData().getDocDate()));
        }

        if (data.getProcessedData().getDocType().toLowerCase().equals("scan")) {
            amount.setVisibility(View.INVISIBLE);
            amountUnits.setVisibility(View.INVISIBLE);
        }

        amount.setText(data.getProcessedData().getNet().toString());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void setCustomHeader(View v) {
        CustomToolbar headers = new CustomToolbar(getContext());
        headers.initViews(v);
        headers.hideHeaderImage();
        headers.hideHeaderLable();
        headers.setToolbarTitle(getString(R.string.processed));

        View view = v.findViewById(R.id.header);
        ImageView iv = view.findViewById(R.id.toolbar_back);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationFlowManager.replaceFragment(Duke.documentsFragment,null,getActivity(),R.id.dashboard_wrapper);
            }
        });
    }


    //    TODO close fab icon
    void onClickRemoveIcon() {
        // Firebase: Send click delete button event
        Bundle params = new Bundle();
        params.putString("Page", "Processed Detail");
//        mFirebaseAnalytics.logEvent("DeleteButtonClick", params);
        confirmationComponent = new ConfirmationComponent(getContext(), getString(R.string.warning), getString(R.string.picture_will_not_be_there), false, getString(R.string.yes), getString(R.string.no), popupActions, 1);
    }

    void deleteFile() {
        if (data == null || data.getSha1() == null) {
            confirmationComponent.dismiss();
            return;
        }
        String sha1 = data.getSha1();
        if (sha1 == null) {
            confirmationComponent.dismiss();
            return;
        }

        customProgressLoader.showDialog();

        if (isLoadDocument) {
//            String loadUUID = null;
//            for(UserLoadsModel userLoadsModel: Duke.loadsDocuments) {
//                for(LoadDocumentModel loadDocumentModel: userLoadsModel.getDocuments()) {
//                    if(loadDocumentModel.getSha1().equals(sha1)) {
//                        loadUUID = userLoadsModel.getLoadUUID();
//                    }
//                }
//            }

            if (Duke.selectedLoadUUID.length() > 1) {
                loadsViewModel.getDeleteFromLoadLiveData(Duke.selectedLoadUUID, sha1,getContext()).observe(this, new Observer<GenericResponseModel>() {
                    @Override
                    public void onChanged(GenericResponseModel genericResponseModel) {
                        confirmationComponent.dismiss();
                        if (genericResponseModel.getStatus() != null && genericResponseModel.getStatus().toLowerCase().equals(ApiConstants.ERRORS.SUCCESS.toLowerCase())) {
//                            confirmationComponent = new ConfirmationComponent(getContext(), getString(R.string.success), getString(R.string.delete_success), false, getString(R.string.ok), popupActions, 1);
                            refreshLoadsList();
                        } else {
                            customProgressLoader.hideDialog();
                            confirmationComponent = new ConfirmationComponent(getContext(), getString(R.string.error), getString(R.string.something_unexpected_happened_we_re_sorry_please_try_again_later), false, getString(R.string.ok), popupActions, 1);
                        }
                    }
                });
            } else {
                confirmationComponent.dismiss();
                customProgressLoader.hideDialog();
                Toast.makeText(getContext(), "Load isn't part of your processed document yet! Please try again later.", Toast.LENGTH_LONG).show();
            }
        } else {
            fileStatusViewModel.deleteFile(sha1,getContext()).observe(this, new Observer<FileDeleteSuccessModel>() {
                @Override
                public void onChanged(@Nullable FileDeleteSuccessModel deviceTokenModel) {
                    confirmationComponent.dismiss();
                    customProgressLoader.hideDialog();
                    if (deviceTokenModel != null) {
                        confirmationComponent = new ConfirmationComponent(getContext(), getString(R.string.success), getString(R.string.delete_success), false, getString(R.string.ok), popupActions, 1);
                    }
                }
            });
        }
    }

    private void refreshLoadsList() {
        DocsOfALoad.clear();
        if (Duke.selectedLoadUUID.length() > 1) {
            loadsViewModel.getGetLoadDetailLiveData(Duke.selectedLoadUUID,getContext()).observe(this, new Observer<SingleLoadModel>() {
                @Override
                public void onChanged(SingleLoadModel singleLoadModel) {
                    if (singleLoadModel.getUserLoadsModels().getDocuments().size() > 0) {
                        DocsOfALoad.addAll(singleLoadModel.getUserLoadsModels().getDocuments());
                    }
                    Log.d("load details", singleLoadModel.toString());
                    Bundle args = new Bundle();
                    args.putString(AppConstants.StringConstants.DELETE_DOC_FROM_LOAD, "Document deleted successfully!");
                    args.putString(AppConstants.StringConstants.NAVIGATE_TO, AppConstants.StringConstants.PROCESS);
                    isLoadDocument = true;
                    customProgressLoader.hideDialog();
                    NavigationFlowManager.openFragments(Duke.documentsFragment, args, getActivity(), R.id.dashboard_wrapper, AppConstants.StringConstants.PROCESS);

                }
            });
        }
    }

    void navigateToProcess() {
        Bundle args = new Bundle();
        args.putString(AppConstants.StringConstants.NAVIGATE_TO, AppConstants.StringConstants.PROCESS);
        NavigationFlowManager.openFragments(Duke.documentsFragment, args, getActivity(), R.id.dashboard_wrapper);
//        NavigationFlowManager.replaceFragment(new Documents(), args, getActivity(), R.id.dashboard_wrapper);
    }


    //TODO Upload Button
    void onClickUploadButton() {
        // Firebase: Send click upload button event

        String sha1 = data.getSha1();
        if (sha1 == null) {
            Toast.makeText(getContext(), "Something wrong with the load. Please contact support.", Toast.LENGTH_LONG).show();
            return;
        }

        if (isLoadDocument) {
//            String loadUUID = null;
//            for(UserLoadsModel userLoadsModel: Duke.loadsDocuments) {
//                for(LoadDocumentModel loadDocumentModel: userLoadsModel.getDocuments()) {
//                    if(loadDocumentModel.getSha1().equals(sha1)) {
//                        loadUUID = userLoadsModel.getLoadUUID();
//                    }
//                }
//            }

            if (!(Duke.selectedLoadUUID.length() > 0)) {
                Toast.makeText(getContext(), "There is something wrong with the load. Please contact support.", Toast.LENGTH_LONG).show();
                return;
            }

            Duke.isNewLoadBeingCreated = false;
            Duke.isDocumentAddingToLoad = true;
        }

        if ((Duke.fileStatusModel.getMemberStatus().equals("NONE") || Duke.fileStatusModel.getMemberStatus().equals("FREE_POD")) && Duke.fileStatusModel.getUploadLimit() - Duke.fileStatusModel.getUploadCount() < 1.0) {
//            Intent intent = new Intent(requireActivity(), PaymentActivity.class);
//            requireActivity().startActivity(intent);
//            requireActivity().finish();
        } else {
            Bundle params = new Bundle();
            params.putString("Page", "Processed Detail");
//            mFirebaseAnalytics.logEvent("AddDocument", params);
            if (uploadDocumentInterface != null) {
                uploadDocumentInterface.uploadDocumentListener(false);
            }
        }

    }


    //TODO Next Button
    public void showNextImage() {
        // Firebase: Send click right button event
        Bundle params = new Bundle();
        params.putString("Page", "Processed Detail");
//        mFirebaseAnalytics.logEvent("ScrolltoRightBt", params);
        adapterViewFlipper.showNext();
    }


    //TODO Previous Button
    public void showPreviousImage() {
        // Firebase: Send click left button event
        Bundle params = new Bundle();
        params.putString("Page", "Processed Detail");
//        mFirebaseAnalytics.logEvent("ScrolltoLeftBt", params);
        adapterViewFlipper.showPrevious();
    }


    //    TODO Signature click
    void onClickSignatureIcon() {
        // Firebase: Send click delete button event
        documentContainer.setVisibility(View.GONE);
        closeIcon.setVisibility(View.GONE);
        uploadButton.setVisibility(View.GONE);
        signatureIcon.setVisibility(View.GONE);
        submitIcon.setVisibility(View.VISIBLE);
        submitText.setVisibility(View.VISIBLE);

        if(signature_text.getText().toString().trim().matches("")) {
            signature_text.setText("");
        }

        signature_text.setSelection(signature_text.getText().length());
        if(data.getProcessedData().getDocType().toLowerCase().equals("scan")) {
            signature_text.setHint("Enter Document Name");
        }
        signature_text.setVisibility(View.VISIBLE);
        signature_text.requestFocus();
        signature_text.setFocusableInTouchMode(true);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(signature_text, InputMethodManager.SHOW_FORCED);
    }


    //    TODO: submit click
    void onClickSubmitBtn() {
        String signature = signature_text.getText().toString();
        if (signature.length() > 0 && !signature.trim().matches("")) {
            closeKeyboard();
            setSignatureInterfaceState();
            customProgressLoader.showDialog();
            String is_scan = "false";
            if (data.getProcessedData().getDocType().toLowerCase().equals("scan")) {
                is_scan = "true";
            }
            Log.d("*********", signature + data.getSignature() + data.getSha1());
            String finalIs_scan = is_scan;
            fileStatusViewModel.updateDocumentSignature(data.getSha1(), signature, is_scan,getContext()).observe(this, new Observer<UpdatePaymentModel>() {
                @Override
                public void onChanged(@Nullable UpdatePaymentModel updatePaymentModel) {
                    customProgressLoader.hideDialog();
                    if (finalIs_scan == "true") {
                        billName.setText(signature);
                    }
                    Toast toast = Toast.makeText(getContext(), "Document Signed Successfully!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 260); // last two args are X and Y are used for setting position
                    toast.setDuration(Toast.LENGTH_SHORT);//you can even use milliseconds to display toast
                    toast.show();
                }
            });
        } else {
            closeKeyboard();
            setSignatureInterfaceState();
        }
    }

    private void setSignatureInterfaceState() {
        documentContainer.setVisibility(View.VISIBLE);
        closeIcon.setVisibility(View.VISIBLE);
        uploadButton.setVisibility(View.VISIBLE);
        signatureIcon.setVisibility(View.VISIBLE);
        submitIcon.setVisibility(View.GONE);
        submitText.setVisibility(View.GONE);
        signature_text.setVisibility(View.GONE);
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClickProfile() {
        Utilities.navigateToProfilePage(getActivity());
    }

    @Override
    public void onBackClicked() {
        closeKeyboard();
        if (isFromSearch) {
            if (getFragmentManager().getBackStackEntryCount() != 0) {
                getFragmentManager().popBackStack();
            } else {
//                NavigationFlowManager.openFragments(new SearchProcessedDocumentsFragment(), null, getActivity(), R.id.dashboard_wrapper);
            }
        } else {
            Bundle args = new Bundle();
            args.putString(AppConstants.StringConstants.NAVIGATE_TO, AppConstants.StringConstants.PROCESS);
            NavigationFlowManager.openFragments(Duke.documentsFragment, args, getActivity(), R.id.dashboard_wrapper);
//            NavigationFlowManager.replaceFragment(new Documents(), args, getActivity(), R.id.dashboard_wrapper);
        }
    }

    @Override
    public void onClickToolbarTitle() {

    }

    @Override
    public void onClickHeaderTitle() {

    }

    @Override
    public void onPopupActions(String id, int dialogId) {
        switch (id) {
            case AppConstants.PopupConstants.POSITIVE:
                deleteFile();
                break;
            case AppConstants.PopupConstants.NEGATIVE:
                confirmationComponent.dismiss();
                break;
            case AppConstants.PopupConstants.NEUTRAL:
                confirmationComponent.dismiss();
                navigateToProcess();
                break;
        }
    }

    @Override
    public void UpdateDocumentNavigation(int position) {
        if (dataModel.size() > 1) {
            if (position == 0) {
                prevButton.setVisibility(View.GONE);
            } else {
                prevButton.setVisibility(View.VISIBLE);
            }
            if (position == dataModel.size() - 1) {
                nextButton.setVisibility(View.GONE);
            } else {
                nextButton.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setCurrentTheme() {
        ChangeThemeModel changeThemeModel = new ChangeThemeModel();
        uploadButton.setColorFilter(Color.parseColor(changeThemeModel.getFloatingButtonColor()));
        uploadButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(changeThemeModel.getFloatingButtonBackgroundColor())));
        closeIcon.setColorFilter(Color.parseColor(changeThemeModel.getFloatingButtonColor()));
        closeIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(changeThemeModel.getFloatingButtonBackgroundColor())));
        amount.setTextColor(Color.parseColor(changeThemeModel.getFontColor()));
        amountUnits.setTextColor(Color.parseColor(changeThemeModel.getFontColor()));
        signatureIcon.setColorFilter(Color.parseColor(changeThemeModel.getFloatingButtonColor()));
        signatureIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(changeThemeModel.getFloatingButtonBackgroundColor())));
        submitIcon.setColorFilter(Color.parseColor(changeThemeModel.getFloatingButtonColor()));
        submitIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(changeThemeModel.getFloatingButtonBackgroundColor())));
//        prevButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(changeThemeModel.getFontColor())));
//        nextButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(changeThemeModel.getFontColor())));
//        prevButton.setColorFilter(Color.parseColor(changeThemeModel.getFontColor()));
    }
}
