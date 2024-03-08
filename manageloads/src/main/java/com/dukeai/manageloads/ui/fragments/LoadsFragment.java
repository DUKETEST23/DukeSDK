package com.dukeai.manageloads.ui.fragments;

import static com.dukeai.manageloads.Duke.DocsOfALoad;
import static com.dukeai.manageloads.Duke.loadsDocuments;
import static com.dukeai.manageloads.ui.activities.BaseActivity.LOCATION_TYPE;
import static com.dukeai.manageloads.utils.AppConstants.Ifta.LOCATION_;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoAccessToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoIdToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoRefreshToken;
import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.R;
import com.dukeai.manageloads.adapters.CustomRecipientListAdapter;
import com.dukeai.manageloads.adapters.LoadsFilterSpinnerAdapter;
import com.dukeai.manageloads.adapters.LoadsListAdapter;
import com.dukeai.manageloads.interfaces.OnPeriodSubmitListener;
import com.dukeai.manageloads.interfaces.PopupActions;
import com.dukeai.manageloads.interfaces.UploadDocumentInterface;
import com.dukeai.manageloads.model.FileStatusModel;
import com.dukeai.manageloads.model.LoadsFilterDataModel;
import com.dukeai.manageloads.model.LoadsListModel;
import com.dukeai.manageloads.model.LoadsTransmitModel;
import com.dukeai.manageloads.model.RecipientDataModel;
import com.dukeai.manageloads.model.RecipientsListModel;
import com.dukeai.manageloads.model.SelectRecipientDataModel;
import com.dukeai.manageloads.model.SingleLoadModel;
import com.dukeai.manageloads.model.UserDataModel;
import com.dukeai.manageloads.model.UserLoadsModel;
import com.dukeai.manageloads.ui.activities.PaymentActivity;
import com.dukeai.manageloads.utils.AppConstants;
import com.dukeai.manageloads.utils.CustomProgressLoader;
import com.dukeai.manageloads.utils.CustomToolbar;
import com.dukeai.manageloads.utils.DasSpinner;
import com.dukeai.manageloads.utils.NavigationFlowManager;
import com.dukeai.manageloads.utils.PeriodSelectorAlert;
import com.dukeai.manageloads.utils.UserConfig;
import com.dukeai.manageloads.utils.Utilities;
import com.dukeai.manageloads.viewmodel.FileStatusViewModel;
import com.dukeai.manageloads.viewmodel.GenericResponseModel;
import com.dukeai.manageloads.viewmodel.LoadsViewModel;
import com.dukeai.manageloads.views.ConfirmationComponent;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.revenuecat.purchases.Purchases;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class LoadsFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback, LoadsListAdapter.OnListItemClickListener, CustomRecipientListAdapter.OnListItemClickListener, CustomRecipientListAdapter.OnlastItemLoadedListener {

    private View loadsView;
    private RecyclerView loads_recycler_view;
    private CustomProgressLoader customProgressLoader;
    private LoadsViewModel loadsViewModel;
    private ArrayList<UserLoadsModel> loadsListModels = new ArrayList<>();
    private LoadsListAdapter adapter;
    private TextView noLoadsForThisPeriod;
    private String accessToken;
    private String idToken;
    private String refreshToken;
    private String userName;
    private String password;
    private UserConfig userConfig = UserConfig.getInstance();
    public final String TAG = this.getClass().getSimpleName();
    public static boolean isLoadDocument = false;
    private DasSpinner spinner;
    private ArrayList<LoadsFilterDataModel> loadsFilterDataModels = new ArrayList<>();
    private LoadsFilterSpinnerAdapter loadsFilterSpinnerAdapter;
    private int selectedPeriod = 3;
    private PeriodSelectorAlert periodSelectorAlert;
    private int pos = 3;
    private Calendar fromCalendar = Calendar.getInstance();
    private Calendar toCalender = Calendar.getInstance();
    private TextView datesSelected;
    private Date fromDate = null;
    private Date toDate = null;
    private String sortBy = "";
    private int loadAdapterCurrentItem = -1;
    private ConfirmationComponent confirmationComponent;
    private static final int DELETE_LOAD_DIALOG_ID = 18;
    private TextView addRecipientText;
    private TextView addRecipient;
    private ArrayList<RecipientDataModel> selectedCustomRecipients = new ArrayList<>();
    private RecyclerView customRecipientsListRecyclerView;
    private ImageView editIcon, editIcon1;
    private boolean isCustomRecipientAlreadySelected = false;
    private CustomRecipientListAdapter customRecipientListAdapter;
    private SnapHelper snapHelper;
    private LinearSnapHelper linearSnapHelper;
    private RecyclerView.LayoutManager layoutManager;
    private ConstraintLayout editPanelWrapper;
    private View snapView;
    private int snapPosition = 0;
    private String[] availableRecipients;
    private NumberPicker numberPicker;
    private ArrayList<RecipientDataModel> recipientDataModels = new ArrayList<>();
    private Button btnTransmit;
    private ArrayList<String> transmitToTheseUsers = new ArrayList<>();
    private UploadDocumentInterface uploadDocumentInterface;
    private ConstraintLayout btnCreateLoad;
    private FileStatusViewModel fileStatusViewModel;
    private PopupActions popupActions;
    public final static String LOCATION_SETTINGS_KEY = "LOCATION_SETTINGS_KEY";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loadsView = inflater.inflate(R.layout.loads_fragment, container, false);
        initViews(loadsView);
        setInitials();

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                getActivity().finish();
                loadsDocuments.clear();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        return loadsView;
    }

    private void setInitials() {
        //Clears the list to prevent any unintentional operation on them
        Duke.customRecipientsList.clear();
        Duke.globalRecipientsList.clear();
        //Resets the flag to smoothen the flow of load or normal upload docs
        Duke.isDocumentAddingToLoad = false;
        Duke.isNewLoadBeingCreated = false;
        Duke.PDFDocFilenames.clear();
        //To avoid trail of un-uploaded doc
        Duke.sortedUploadingImageStoragePaths.clear();
        Duke.uploadingImagesList.clear();
        Duke.imageStoragePath = "";
        Duke.uploadingImageStoragePaths.clear();
        Duke.PDFDocFilenames.clear();
        Duke.PDFDocURIs.clear();


        Bundle args = getArguments();
        if (args != null) {
            if (args.getString(AppConstants.StringConstants.ADD_DOC_TO_LOAD) != null) {
                Toast.makeText(getContext(), args.getString(AppConstants.StringConstants.ADD_DOC_TO_LOAD), Toast.LENGTH_LONG).show();
            } else if (args.get("upload_status") != null) {
                Duke.PDFDocURIs.clear();
                Toast.makeText(getContext(), getArguments().get("upload_status").toString(), Toast.LENGTH_LONG).show();
            }
        }
    }


    private void initViews(View loadsView) {
        Duke.loadsFragment = this;
//        getArgs();
        customProgressLoader = new CustomProgressLoader(getContext());

        loadsViewModel = new ViewModelProvider(this).get(LoadsViewModel.class);

        loadsViewModel.setContext(getContext());

        fileStatusViewModel = ViewModelProviders.of(this).get(FileStatusViewModel.class);
        fileStatusViewModel.setContext(getContext());


        loads_recycler_view = loadsView.findViewById(R.id.loads_recycler_view);
        LinearLayoutManager mgr = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        loads_recycler_view.setLayoutManager(mgr);
        loads_recycler_view.setHasFixedSize(true);
        adapter = new LoadsListAdapter(getContext(), LoadsFragment.this);
        loads_recycler_view.setAdapter(adapter);
        noLoadsForThisPeriod = loadsView.findViewById(R.id.no_loads_for_selected_period);
        spinner = loadsView.findViewById(R.id.loads_filter_spinner);
        datesSelected = loadsView.findViewById(R.id.datesSelected);

        customRecipientsListRecyclerView = loadsView.findViewById(R.id.custom_recipients_recycler_view);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        customRecipientsListRecyclerView.setHasFixedSize(true);
        customRecipientListAdapter = new CustomRecipientListAdapter(this, this);
        snapHelper = new PagerSnapHelper();
        linearSnapHelper = new LinearSnapHelper();
        customRecipientsListRecyclerView.setLayoutManager(layoutManager);
        linearSnapHelper.attachToRecyclerView(customRecipientsListRecyclerView);
        customRecipientsListRecyclerView.setAdapter(customRecipientListAdapter);

        snapView = snapHelper.findSnapView(layoutManager);


//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());


        datesSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomPeriodSelectorDialog();
            }
        });

        addRecipientText = loadsView.findViewById(R.id.add_recipient_text);
        addRecipientText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(AppConstants.StringConstants.ACTION, AppConstants.StringConstants.ACTION_TYPE_ADD);
//                NavigationFlowManager.addNewFragment(new AddRecipientFragment(), bundle, getActivity(), R.id.dashboard_wrapper, AppConstants.Pages.LOADS);
                NavigationFlowManager.replaceFragment(new AddRecipientFragment(), bundle, getActivity(), R.id.dashboard_wrapper);
            }
        });

        addRecipient = loadsView.findViewById(R.id.select_recipient);
        addRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectRecipientDialog();
            }
        });

        editIcon = loadsView.findViewById(R.id.edit_btn);
        editIcon1 = loadsView.findViewById(R.id.edit_icon);

        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditBtnClicked(view);
            }
        });

        btnTransmit = loadsView.findViewById(R.id.transmit);
        btnTransmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickTrasmit(view);
            }
        });

        editPanelWrapper = loadsView.findViewById(R.id.custom_recipients_recycler_view_wrapper);
        numberPicker = loadsView.findViewById(R.id.numberPicker);


        Duke.selectRecipientDataRestList.clear();
        setCustomHeader();
        setLoadsList("last_year");
//        TODO: Swipe To Delete
        setSwipeActionCallback();
//        TODO: Get Recipient Data
        getRecipientsData();
        setToolbarColor();
        fetchLatestStatus();
        setRecipientList();
        btnCreateLoad = loadsView.findViewById(R.id.create_load_btn);
        btnCreateLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    selectedCustomRecipients.clear();
                    Duke.restRecipients.clear();
                    openAddDocumentPanel();
                } catch (Exception ex) {
                    Toast.makeText(getContext(), "" + ex, Toast.LENGTH_SHORT).show();
                }
            }
        });

        requestLocationPermission(LOCATION_TYPE, LOCATION_);
    }

    private void setRecipientList() {
        if (Duke.selectedRecipientsList.size() != 0) {
//            afterDialogDismissed();
            selectedCustomRecipients.clear();
            Duke.selectRecipientDataRestList.clear();
            for (RecipientDataModel recipientDataModel : Duke.selectedRecipientsList) {
                if (Duke.restRecipients.contains(recipientDataModel.getEmail())) {
                    selectedCustomRecipients.add(recipientDataModel);
                    Duke.selectRecipientDataRestList.add(new SelectRecipientDataModel(true, recipientDataModel.getEmail()));

                }
            }

            if (Duke.restRecipients.size() > 0) {
                addRecipient.setText(Duke.restRecipients.size() + " Recipients Selected");
            } else {
                addRecipient.setText("Select Recipients");
            }

            if (selectedCustomRecipients.size() > 0) {

                if (selectedCustomRecipients.size() == 1) {
                    RecyclerView.MarginLayoutParams param = (RecyclerView.MarginLayoutParams) customRecipientsListRecyclerView.getLayoutParams();
                    param.height = getResources().getDimensionPixelSize(R.dimen.edit_panel_40);  //working-1
                    editIcon1.setVisibility(View.INVISIBLE);
//                customRecipientsListRecyclerView.smoothScrollToPosition(0);
                    customRecipientsListRecyclerView.setLayoutParams(param);    //working-1
                } else if (selectedCustomRecipients.size() == 2) {
                    RecyclerView.MarginLayoutParams param = (RecyclerView.MarginLayoutParams) customRecipientsListRecyclerView.getLayoutParams();
                    param.height = getResources().getDimensionPixelSize(R.dimen.edit_panel_80);
                    editIcon1.setVisibility(View.INVISIBLE);
//                customRecipientsListRecyclerView.smoothScrollToPosition(1);
                    customRecipientsListRecyclerView.setLayoutParams(param);
                } else {
                    editIcon1.setVisibility(View.VISIBLE);
                    RecyclerView.MarginLayoutParams param = (RecyclerView.MarginLayoutParams) customRecipientsListRecyclerView.getLayoutParams();
                    param.height = getResources().getDimensionPixelSize(R.dimen.edit_panel_100);
                    customRecipientsListRecyclerView.scrollToPosition(Integer.MAX_VALUE / 2);
                }

                ArrayList<RecipientDataModel> selected = new ArrayList<>();
                for (RecipientDataModel pm : selectedCustomRecipients) {
                    selected.add(pm);
                }

                if (isCustomRecipientAlreadySelected) {
                    Log.d(TAG, "afterDialogDismissed: selectedCustomRecipients list " + selectedCustomRecipients.size());
                    customRecipientListAdapter.updatelist(selected);
                    customProgressLoader.hideDialog();
                } else {
                    isCustomRecipientAlreadySelected = true;
                    customRecipientListAdapter.updatelist(selectedCustomRecipients);
                    editPanelWrapper.setVisibility(View.VISIBLE);
                    customProgressLoader.hideDialog();
                }
                editPanelWrapper.setVisibility(View.VISIBLE);
            } else {
                editPanelWrapper.setVisibility(View.INVISIBLE);
                customProgressLoader.hideDialog();
            }

        }

        SelectRecipientsDialog selectRecipientsDialog = new SelectRecipientsDialog();
//        selectRecipientsDialog.setRestrecipientChecked();

    }


    public void onEditBtnClicked(View view) {
        Log.d("number picker value: ", " - data: " + availableRecipients[numberPicker.getValue()]);
        Log.d("all recipients", recipientDataModels.toString());
        Duke.loadsFragment = this;

        RecipientDataModel recipientDataModelForEdit = null;
        for (RecipientDataModel recipientDataModel : recipientDataModels) {
            if (recipientDataModel.getEmail().equals(availableRecipients[numberPicker.getValue()])) {
                recipientDataModelForEdit = recipientDataModel;
                break;
            }
        }

        if (recipientDataModelForEdit != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(AppConstants.StringConstants.RECIPIENT_DATA_MODEL, recipientDataModelForEdit);
            bundle.putString(AppConstants.StringConstants.ACTION, AppConstants.StringConstants.ACTION_TYPE_UPDATE);
            NavigationFlowManager.openFragments(new AddRecipientFragment(), bundle, getActivity(), R.id.dashboard_wrapper);
        }
    }


    private void setToolbarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.yellow_BBAE27));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setLoadsList(String filterType) {
        customProgressLoader.showDialog();
/*
        if(Duke.loadsDocuments.size()!=0){
            adapter.updateDataList(Duke.loadsDocuments);
        }else{*/
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sort_by", filterType);
        loadsViewModel.getLoadsListLiveData(jsonObject,getActivity()).observe(getViewLifecycleOwner(), new Observer<LoadsListModel>() {
            @Override
            public void onChanged(LoadsListModel loadsListModel) {
                try {
                    if (loadsListModel.getRecipientsList() != null) {
                        Log.d("data", loadsListModel.status);

                        int count = 0;
//                    *************REMOVE THIS AFTER TEST***************
                        //Code to limit only 8 load docs
//                    for(UserLoadsModel userLoadsModel: loadsListModel.userLoadsModels) {
//                        if(count<8) {
//                            loadsListModels.add(userLoadsModel);
//                            count++;
//                        }
//                    }
//                    *************REMOVE THIS AFTER TEST***************
                        loadsListModels = loadsListModel.userLoadsModels;
                        Duke.loadsDocuments = loadsListModel.userLoadsModels;
                        Log.d(TAG, "onChanged: " + new Gson().toJson(loadsListModel.userLoadsModels));

                        adapter.updateDataList(loadsListModels);
//                    applyFilters(pos);

                        if (loadsListModels.size() > 0) {
                            loads_recycler_view.setVisibility(View.VISIBLE);
                            noLoadsForThisPeriod.setVisibility(View.INVISIBLE);
                            customProgressLoader.hideDialog();
                        } else {
                            loads_recycler_view.setVisibility(View.INVISIBLE);
                            noLoadsForThisPeriod.setVisibility(View.VISIBLE);
                            customProgressLoader.hideDialog();
                        }
                    } else {
                        customProgressLoader.hideDialog();
                    }
                } catch (Exception exception) {
                    customProgressLoader.hideDialog();
                    Log.e(TAG, "onChanged: 410" + String.valueOf(exception));

                }
                customProgressLoader.hideDialog();
            }
        });
        // }


    }

    @Override
    public void onListItemClick(int pos, UserLoadsModel dataModel, String loaduuid) {
        // Firebase: Send click view document button event
        DocsOfALoad.clear();
        selectedCustomRecipients.clear();
        Duke.restRecipients.clear();
        loadsViewModel.getGetLoadDetailLiveData(dataModel.getLoadUuid(),getContext()).observe(this, new Observer<SingleLoadModel>() {
            @Override
            public void onChanged(SingleLoadModel singleLoadModel) {
                if (singleLoadModel.getUserLoadsModels().getDocuments().size() > 0) {
                    DocsOfALoad.addAll(singleLoadModel.getUserLoadsModels().getDocuments());
                }
                Log.d("load details", singleLoadModel.toString());
                Bundle args = new Bundle();
                args.putString(AppConstants.StringConstants.NAVIGATE_TO, AppConstants.StringConstants.PROCESS);
                isLoadDocument = true;
                Duke.selectedLoadUUID = dataModel.getLoadUuid();
//                Duke.selectedLoadUUID = dataModel.getLoadUuid();
                NavigationFlowManager.openFragments(Documents.newInstance(), args, getActivity(), R.id.dashboard_wrapper, AppConstants.StringConstants.PROCESS);
//                NavigationFlowManager.replaceFragment(Documents.newInstance(), null, getActivity(), R.id.dashboard_wrapper);
            }
        });

    }

    private void setCustomHeader() {
        CustomToolbar headers = new CustomToolbar(getContext());
        headers.initViews(loadsView);
        headers.hideHeaderImage();
        headers.hideHeaderLable();
        headers.hideToolbarTitle();
        headers.hideToolbarBack();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setCustomHeader();
//        TODO: for custom filter
        setPeriodSelectorSpinner();
        Bundle args = getArguments();
        if (args != null && args.getString(AppConstants.StringConstants.ADD_DOC_TO_LOAD) != null) {
//            Toast.makeText(getContext(), args.getString(AppConstants.StringConstants.ADD_DOC_TO_LOAD), Toast.LENGTH_LONG).show();
        }
    }

    //    Allternate Implmentation of Period Filter
    private void setPeriodSelectorSpinner() {
        loadsFilterDataModels.clear();
        loadsFilterDataModels.add(new LoadsFilterDataModel(getResources().getString(R.string.last_week)));
        loadsFilterDataModels.add(new LoadsFilterDataModel(getResources().getString(R.string.last_month)));
        loadsFilterDataModels.add(new LoadsFilterDataModel(getResources().getString(R.string.last_quarter)));
        loadsFilterDataModels.add(new LoadsFilterDataModel(getResources().getString(R.string.year_to_date)));
        loadsFilterDataModels.add(new LoadsFilterDataModel(getResources().getString(R.string.custom)));

        loadsFilterSpinnerAdapter = new LoadsFilterSpinnerAdapter(getContext(), R.layout.report_spinner_item, loadsFilterDataModels);
        spinner.setAdapter(loadsFilterSpinnerAdapter);
        spinner.setSelection(3);


        final AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPeriod = i;
                if (selectedPeriod == 4) {
                    if (periodSelectorAlert == null || !periodSelectorAlert.isShowing()) {
                        if (loadsListModels.size() > 0) {
                            showCustomPeriodSelectorDialog();
                        } else {
                            showCustomPeriodSelectorDialog();
                        }
                    }

                } else {
                    pos = i;
                    applyFilters(pos);
                    datesSelected.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("&*&**&*&*&*&*&*&", "onNothingSelected: ");
            }
        };


        //
        //        periodSpinner.setOnItemSelectedListener(itemSelectedListener);
        spinner.post(new Runnable() {
            public void run() {
                spinner.setOnItemSelectedListener(itemSelectedListener);
            }
        });
    }

    public void showCustomPeriodSelectorDialog() {

        /**Show dialog**/

        periodSelectorAlert = new PeriodSelectorAlert(getContext(), new OnPeriodSubmitListener() {
            @Override
            public void onSubmitClick(Calendar fromCalendarResponse, Calendar toCalendarResponse) {
                periodSelectorAlert.dismiss();
                fromCalendar = fromCalendarResponse;
                toCalender = toCalendarResponse;
                //Apply Filters Now based on the Period selected
                applyFilters(4);

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodSelectorAlert.dismiss();
                selectedPeriod = pos;
                if (selectedPeriod != 4)
                    spinner.setSelection(selectedPeriod); //YTD
            }
        });
    }

    public void applyFilters(int pos) {
        this.pos = pos;

        fromDate = new Date();
        toDate = new Date();
        Date docDate = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        if (selectedPeriod == 4) {
            fromDate = fromCalendar.getTime();
            toDate = toCalender.getTime();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    datesSelected.setText(Utilities.getFormattedDate(fromCalendar) + " - " + Utilities.getFormattedDate(toCalender));
                    datesSelected.setVisibility(View.VISIBLE);
                }
            });
        } else {
            try {
                fromDate = format.parse(Utilities.getStartDate(selectedPeriod));
                toDate = format.parse(Utilities.getEndDateReport(pos));
            } catch (Exception e) {
                Log.d("FILTER DATE EXCEPTION", "e: " + e.getStackTrace());
            }
        }

        switch (selectedPeriod) {
            case 0:
                sortBy = "last_week";
                break;
            case 1:
                sortBy = "last_month";
                break;
            case 2:
                sortBy = "last_quarter";
                break;
            case 4:
                sortBy = "custom";
                break;
            default:
                sortBy = "last_year";
        }

        DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

        JsonObject jsonObject = new JsonObject();
        String from = "";
        String to = "";
        if (sortBy.length() > 0) {
            if (fromDate != null || toDate != null) {
                from = formatDate.format(fromCalendar.getTime());
                to = formatDate.format(toCalender.getTime());
            } else {
                Log.d("Date error from:", fromDate.toString());
                Log.d("Date error from:", toDate.toString());
                from = formatDate.format(fromDate);
                to = formatDate.format(toDate);
            }
            if (sortBy.equals("custom") && from.length() > 0 && to.length() > 0) {
                jsonObject.addProperty("start_date", from);
                jsonObject.addProperty("end_date", to);
            }
            jsonObject.addProperty("sort_by", sortBy);
        }

        Log.d(Duke.TAG, "Request ==> " + new Gson().toJson(jsonObject));
        loadsViewModel.getLoadsListLiveData(jsonObject, getContext()).observe(getActivity(), new Observer<LoadsListModel>() {
            @Override
            public void onChanged(LoadsListModel loadsListModel) {
                if (loadsListModel.getRecipientsList() != null) {
                    Log.d("data", loadsListModel.status);
                    loadsListModels = loadsListModel.userLoadsModels;
                    Duke.loadsDocuments = loadsListModel.userLoadsModels;/*
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    loads_recycler_view.setHasFixedSize(true);*/
                    Log.d(Duke.TAG, "====>  " + new Gson().toJson(loadsListModels));
                    adapter = new LoadsListAdapter(getContext(), LoadsFragment.this);
                    adapter.updateDataList(loadsListModels);
//                    loads_recycler_view.setLayoutManager(layoutManager);
                    loads_recycler_view.setAdapter(adapter);

                    if (loadsListModels.size() > 0) {
                        loads_recycler_view.setVisibility(View.VISIBLE);
                        noLoadsForThisPeriod.setVisibility(View.INVISIBLE);
                    } else {
                        Duke.selectedLoadsForTransmission.clear();
                        loads_recycler_view.setVisibility(View.INVISIBLE);
                        noLoadsForThisPeriod.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }


    private void setSwipeActionCallback() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Take action for the swiped item
                int position = viewHolder.getAdapterPosition();

                loadAdapterCurrentItem = position;
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        showDeleteConfirmationAlert(position);
                        break;
                    case ItemTouchHelper.RIGHT:
                        break;
                }
            }

            //Add icons for swipe on delete action

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRed))
                        .addSwipeLeftActionIcon(R.drawable.ic_delete_icon_white)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(loads_recycler_view);
    }

    private void showDeleteConfirmationAlert(int pos) {
        confirmationComponent = new ConfirmationComponent(getContext(), getString(R.string.delete_load), getString(R.string.delete_load_msg), false, Utilities.getStrings(getContext(), R.string.delete), "Cancel", new PopupActions() {
            @Override
            public void onPopupActions(String id, int dialogId) {
                if (id.equals(AppConstants.PopupConstants.POSITIVE)) {

                    loadsViewModel.getDeleteLoadObjectLiveData(loadsListModels.get(pos).getLoadUuid(),getContext()).observe(getActivity(), new Observer<GenericResponseModel>() {
                        @Override
                        public void onChanged(GenericResponseModel genericResponseModel) {
                            if (genericResponseModel != null && genericResponseModel.getStatus().toLowerCase().equals("success")) {
                                confirmationComponent.dismiss();
                                adapter.notifyItemRemoved(loadAdapterCurrentItem);
                                applyFilters(selectedPeriod);
                            }
                            Toast.makeText(getContext(), genericResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    confirmationComponent.dismiss();
                    adapter.notifyDataSetChanged();
                }
            }
        }, DELETE_LOAD_DIALOG_ID);
    }


    @Override
    public void onListItemClick(int pos, RecipientDataModel dataModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstants.StringConstants.RECIPIENT_DATA_MODEL, dataModel);
        bundle.putString(AppConstants.StringConstants.ACTION, AppConstants.StringConstants.ACTION_TYPE_UPDATE);
        NavigationFlowManager.openFragments(new AddRecipientFragment(), bundle, getActivity(), R.id.dashboard_wrapper);
    }

    @Override
    public void onLastItemLodedListener() {
        customProgressLoader.hideDialog();
    }

    private void getRecipientsData() {
        customProgressLoader.showDialog();
        Duke.customRecipientsList.clear();
        Duke.globalRecipientsList.clear();
        Duke.selectedRecipients.clear();
        selectedCustomRecipients.clear();

        loadsViewModel.getRecipientsList("all",getContext()).observe(getViewLifecycleOwner(), new Observer<RecipientsListModel>() {
            @Override
            public void onChanged(RecipientsListModel recipientsListModel) {
                try {
                    recipientDataModels = recipientsListModel.recipientsList;
                } catch (Exception ex) {
                    recipientDataModels = recipientsListModel.recipientsList;
                    Log.e(TAG, "onChanged:741 " + ex);
                }
//                availableRecipients = new String[recipientsListModel.recipientsList.size()];
                for (int i = 0; i < recipientsListModel.recipientsList.size(); i++) {
//                    availableRecipients[i] = recipientsListModel.recipientsList.get(i).getEmail();
                    if (recipientsListModel.getRecipientsList().get(i).getIsCustomRecipient().toLowerCase().equals("true")) {
                        if (!Duke.restRecipients.contains(recipientsListModel.getRecipientsList().get(i).getEmail())) {
                            Duke.customRecipientsList.add(recipientsListModel.getRecipientsList().get(i));
                        }
                    } else {
                        Duke.globalRecipientsList.add(recipientsListModel.getRecipientsList().get(i));
                    }
                }
                if (Duke.selectedRecipients.size() > 0) {
                    afterDialogDismissed();
                } else {
                    customProgressLoader.hideDialog();
                }
//                customProgressLoader.hideDialog();
//                setCustomRecipientList();
                //setUserAddedRecipients();
            }
        });
    }


    private void openSelectRecipientDialog() {
        SelectRecipientsDialog selectRecipientsDialog = new SelectRecipientsDialog();
//        selectRecipientsDialog.setOnDismissListener(dismissListener);
        selectRecipientsDialog.setCancelable(false);
        selectRecipientsDialog.show(getFragmentManager(), "Select Recipients Dialog");
        selectRecipientsDialog.setDismissListener(new SelectRecipientsDialog.DismissListener() {
            @Override
            public void onDismiss(boolean isCanceled) {
                if (!isCanceled) {
                    customProgressLoader.showDialog();
                    afterDialogDismissed();
                }
            }
        });
    }

    public void afterDialogDismissed() {
        selectedCustomRecipients.clear();

        for (RecipientDataModel recipientDataModel : Duke.customRecipientsList) {
            if (Duke.selectedRecipients.contains(recipientDataModel.getEmail())) {
                selectedCustomRecipients.add(recipientDataModel);
            }
        }


        if (Duke.selectedRecipients.size() > 0) {
            addRecipient.setText(Duke.selectedRecipients.size() + " Recipients Selected");
        } else {
            addRecipient.setText("Select Recipients");
        }

        if (selectedCustomRecipients.size() > 0) {

            if (selectedCustomRecipients.size() == 1) {
                RecyclerView.MarginLayoutParams param = (RecyclerView.MarginLayoutParams) customRecipientsListRecyclerView.getLayoutParams();
                param.height = getResources().getDimensionPixelSize(R.dimen.edit_panel_40);  //working-1
                editIcon1.setVisibility(View.INVISIBLE);
//                customRecipientsListRecyclerView.smoothScrollToPosition(0);
                customRecipientsListRecyclerView.setLayoutParams(param);    //working-1
            } else if (selectedCustomRecipients.size() == 2) {
                RecyclerView.MarginLayoutParams param = (RecyclerView.MarginLayoutParams) customRecipientsListRecyclerView.getLayoutParams();
                param.height = getResources().getDimensionPixelSize(R.dimen.edit_panel_80);
                editIcon1.setVisibility(View.INVISIBLE);
//                customRecipientsListRecyclerView.smoothScrollToPosition(1);
                customRecipientsListRecyclerView.setLayoutParams(param);
            } else {
                editIcon1.setVisibility(View.VISIBLE);
                RecyclerView.MarginLayoutParams param = (RecyclerView.MarginLayoutParams) customRecipientsListRecyclerView.getLayoutParams();
                param.height = getResources().getDimensionPixelSize(R.dimen.edit_panel_100);
                customRecipientsListRecyclerView.scrollToPosition(Integer.MAX_VALUE / 2);
            }

            ArrayList<RecipientDataModel> selected = new ArrayList<>();
            for (RecipientDataModel pm : selectedCustomRecipients) {
                selected.add(pm);
            }

            if (isCustomRecipientAlreadySelected) {
                Log.d(TAG, "afterDialogDismissed: selectedCustomRecipients list " + selectedCustomRecipients.size());
                customRecipientListAdapter.updatelist(selected);
                customProgressLoader.hideDialog();
            } else {
                isCustomRecipientAlreadySelected = true;
                Duke.selectedRecipientsList.addAll(selectedCustomRecipients);
                setCustomRecipientList();
            }
            editPanelWrapper.setVisibility(View.VISIBLE);
        } else {
            editPanelWrapper.setVisibility(View.INVISIBLE);
            customProgressLoader.hideDialog();
        }
    }


    private void setCustomRecipientList() {


        customRecipientListAdapter.updatelist(selectedCustomRecipients);
        editPanelWrapper.setVisibility(View.VISIBLE);
        customProgressLoader.hideDialog();
    }


    public void onClickTrasmit(View view) {
        customProgressLoader.showDialog();

        //Transmit Load API
        UserConfig userConfig = UserConfig.getInstance();
        UserDataModel userDataModel;
        userDataModel = userConfig.getUserDataModel();

        Log.d("selected loads", Duke.selectedLoadsForTransmission.toArray().toString());

        if (Duke.selectedRecipients.size() > 0 && Duke.selectedLoadsForTransmission.size() > 0) {
            transmitToTheseUsers.addAll(Duke.selectedRecipients);

            loadsViewModel.getTransmitLoadsModelLiveData(Duke.selectedLoadsForTransmission, transmitToTheseUsers,getContext()).observe(this, new Observer<LoadsTransmitModel>() {
                @Override
                public void onChanged(LoadsTransmitModel loadsTransmitModel) {
                    customProgressLoader.hideDialog();
                    transmitToTheseUsers.clear();
                    /*if(loadsTransmitModel.message != null && loadsTransmitModel.message.length()>0) {
                        Toast.makeText(getContext(), loadsTransmitModel.message, Toast.LENGTH_LONG).show();
                    }*/
                    try {
                        Duke.selectedRecipients.clear();
                        Duke.restRecipients.clear();
                        Toast.makeText(getContext(), loadsTransmitModel.message.toString(), Toast.LENGTH_LONG).show();
                        NavigationFlowManager.replaceFragment(new LoadsFragment(), null, getActivity(), R.id.dashboard_wrapper);
                        Log.d(TAG, "onChanged:852 " + loadsTransmitModel.message.toString());
                    } catch (Exception ex) {

                    }
                }
            });
        } else {
            customProgressLoader.hideDialog();
            if (Duke.selectedRecipients.size() == 0) {
                Toast.makeText(getContext(), "Please select recipient(s)!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Please select load(s) to transmit!", Toast.LENGTH_LONG).show();
            }

        }
//        Purchases.setDebugLogsEnabled(true);
//        Purchases.configure(getContext(), "aZcnnlsPEikLbYMmVvkRuIkKQiPBUJBB");
//        loadPaywall();
    }

    private void loadPaywall() {
        Intent intent = new Intent(requireActivity(), PaymentActivity.class);
        requireActivity().startActivity(intent);
        requireActivity().finish();
    }

    private void openAddDocumentPanel() {
        if ((Duke.fileStatusModel.getMemberStatus().equals("NONE") || Duke.fileStatusModel.getMemberStatus().equals("FREE_POD")) && Duke.fileStatusModel.getUploadLimit() - Duke.fileStatusModel.getUploadCount() < 1.0) {
            /*Intent intent = new Intent(requireActivity(), PaymentActivity.class);
            requireActivity().startActivity(intent);
            requireActivity().finish();*/
        } else {
            Bundle params = new Bundle();
            params.putString("Page", "CreateLoad");
            isLoadDocument = true;
            Duke.isDocumentAddingToLoad = false;
            Duke.isNewLoadBeingCreated = true;
            uploadDocumentInterface.uploadDocumentListener(false);
//            uploadDocument = new UploadDocument(getContext(), getActivity(), savedInstance, false, LoadsFragment.this);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UploadDocumentInterface) {
            uploadDocumentInterface = (UploadDocumentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private void fetchLatestStatus() {
        customProgressLoader.showDialog();

        fileStatusViewModel.getFileStatusModelLiveData("",getActivity()).observe(getViewLifecycleOwner(), new Observer<FileStatusModel>() {
            @Override
            public void onChanged(FileStatusModel fileStatusModel) {
                if (fileStatusModel != null && fileStatusModel.getMessage() == null) {
                    Duke.fileStatusModel = fileStatusModel;
                } else {
                    String message = "";
                    if (fileStatusModel != null) {
                        // Firebase: Send Error information
                        UserDataModel userDataModel;
                        userDataModel = userConfig.getUserDataModel();
                        Bundle params = new Bundle();
                        params.putString("Page", "Home");
                        params.putString("Description", "Not a member");
                        params.putString("UserEmail", userDataModel.getUserEmail());
                        if (fileStatusModel.getMessage().contains(AppConstants.HomePageConstants.NO_GROUPS)) {
                            message = getString(R.string.not_part_of_any_group);
                        } else {
                            message = fileStatusModel.getMessage();
                        }
                        confirmationComponent = new ConfirmationComponent(getContext(), getResources().getString(R.string.error), message, false, getResources().getString(R.string.ok), popupActions, 1);
                    }
                }
            }
        });
    }

    private void requestLocationPermission(final int type, final String from) {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (type == LOCATION_TYPE) {
                            if (report.areAllPermissionsGranted()) {
                                Duke.isLocationPermissionProvided = true;

                            } else if (report.isAnyPermissionPermanentlyDenied()) {
                                Duke.isLocationPermissionProvided = false;
//                                showSettingsDialog();
                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                    }
                }).onSameThread()
                .check();
    }

}