package com.dukeai.manageloads.ui.fragments;

import static com.dukeai.manageloads.Duke.DocsOfALoad;
import static com.dukeai.manageloads.Duke.isLoadDocument;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.R;
import com.dukeai.manageloads.adapters.ProcessedDocumentsAdapter;
import com.dukeai.manageloads.interfaces.PopupActions;
import com.dukeai.manageloads.interfaces.UpdateTabCount;
import com.dukeai.manageloads.model.FileStatusModel;
import com.dukeai.manageloads.model.LoadDocumentModel;
import com.dukeai.manageloads.model.ProcessedDocumentsModel;
import com.dukeai.manageloads.utils.AppConstants;
import com.dukeai.manageloads.utils.CustomProgressLoader;
import com.dukeai.manageloads.utils.NavigationFlowManager;
import com.dukeai.manageloads.viewmodel.FileStatusViewModel;
import com.dukeai.manageloads.viewmodel.LoadsViewModel;
import com.dukeai.manageloads.views.ConfirmationComponent;
import com.google.gson.Gson;

import java.util.ArrayList;



public class ProcessedFragment extends Fragment implements ProcessedDocumentsAdapter.OnListItemClickListener, PopupActions {

    private View processedView;
    private ArrayList<ProcessedDocumentsModel> dataModels;
    private CustomProgressLoader customProgressLoader;
    private ProcessedDocumentsAdapter adapter;
    private Context context;
    private RecyclerView recyclerView;
    private LoadsViewModel loadsViewModel;
    private FileStatusViewModel fileStatusViewModel;
    private RelativeLayout emptyCardLayout;
    private UpdateTabCount updateTabCount;
    private ConfirmationComponent confirmationComponent;
    private PopupActions popupActions;
    private static final String TAG = "ProcessedFragment";

    public ProcessedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        processedView = inflater.inflate(R.layout.fragment_processed, container, false);
        initView(processedView);
        return processedView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (customProgressLoader != null) {
            customProgressLoader.hideDialog();
        }
        dataModels.clear();
        if (!getUserVisibleHint()) {
            return;
        }
        updateDocumentsData();
    }

    private void initView(View processedView) {

        popupActions = this;
        dataModels = new ArrayList<>();
        customProgressLoader = new CustomProgressLoader(getContext());
        recyclerView = processedView.findViewById(R.id.processed_documents_view);
        FrameLayout searchFrame = processedView.findViewById(R.id.search_frame);
        dataModels.clear();
        if(isLoadDocument) {
            searchFrame.setVisibility(View.GONE);
        } else {
            searchFrame.setVisibility(View.VISIBLE);
        }
        emptyCardLayout = processedView.findViewById(R.id.processed_empty_card);
        updateTabCount = (UpdateTabCount) getParentFragment();
        loadsViewModel = ViewModelProviders.of(getActivity()).get(LoadsViewModel.class);
        fileStatusViewModel = ViewModelProviders.of(this).get(FileStatusViewModel.class);
        customProgressLoader = new CustomProgressLoader(getContext());
        setRecyclerAdapter();


    }

    private void setDataToAdapter() {
        dataModels.clear();
        if (!customProgressLoader.isShowing()) {
            customProgressLoader.showDialog();
        }
//        dataModels.addAll(Duke.fileStatusModel.getAllProcessedDocuments());
        if(isLoadDocument) {
            if(DocsOfALoad.size()>0) {
                for(ProcessedDocumentsModel processedDocumentsModel: Duke.fileStatusModel.getAllProcessedDocuments()) {
                    for(LoadDocumentModel loadDocumentModel: DocsOfALoad) {
                        if(processedDocumentsModel.getSha1().equals(loadDocumentModel.getSha1())) {
                            dataModels.add(processedDocumentsModel);
                        }
                    }
                }
            }
        } else {
//            dataModels.addAll(Duke.fileStatusModel.getAllProcessedDocuments());
            for(ProcessedDocumentsModel doc: Duke.fileStatusModel.getAllProcessedDocuments()) {
                if(!doc.getProcessedData().getTitle().equals("Load Document")) {
                    dataModels.add(doc);
                }
            }
        }
        if (dataModels.size() <= 0) {
            return;
        }
        Log.d(TAG, "setDataToAdapter: 150 "+new Gson().toJson(dataModels));
        adapter.updateDataList(dataModels);
        adapter.notifyDataSetChanged();
        customProgressLoader.hideDialog();
    }

    private void setRecyclerAdapter() {
        adapter = new ProcessedDocumentsAdapter(context, R.layout.layout_processed_document_item, ProcessedFragment.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);
    }


    private void updateDocumentsData() {
        if (customProgressLoader == null || fileStatusViewModel == null) {
            return;
        }
        String numberOfDocs = "";
        customProgressLoader.showDialog();
        fileStatusViewModel.getFileStatusModelLiveData(numberOfDocs, getContext()).observe(this, new Observer<FileStatusModel>() {
            @Override
            public void onChanged(@Nullable FileStatusModel fileStatusModel) {
                if (fileStatusModel != null) {
                    Duke.fileStatusModel = fileStatusModel;
                    if (Duke.fileStatusModel != null && Duke.fileStatusModel.getInProcessDocumentsModels() != null && Duke.fileStatusModel.getAllProcessedDocuments().size() > 0) {
                        setDataToAdapter();
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyCardLayout.setVisibility(View.GONE);
                        if(Duke.isLoadDocument) {
                            if(Duke.DocsOfALoad.size()>0) {
                                if (updateTabCount != null) {
                                    updateTabCount.UpdateTabCount();
                                }
                                recyclerView.setVisibility(View.VISIBLE);
                                emptyCardLayout.setVisibility(View.GONE);
                            } else {
                                customProgressLoader.hideDialog();
                                recyclerView.setVisibility(View.GONE);
                                emptyCardLayout.setVisibility(View.VISIBLE);
                                TextView textView = emptyCardLayout.findViewById(R.id.error_message);
                                textView.setText(getString(R.string.no_docs_present_in_this_load));
                            }
                        } else {
                            if(dataModels.size()>0) {
                                if (updateTabCount != null) {
                                    updateTabCount.UpdateTabCount();
                                }
                                recyclerView.setVisibility(View.VISIBLE);
                                emptyCardLayout.setVisibility(View.GONE);
                            } else {
                                customProgressLoader.hideDialog();
                                recyclerView.setVisibility(View.GONE);
                                emptyCardLayout.setVisibility(View.VISIBLE);
                                TextView textView = emptyCardLayout.findViewById(R.id.error_message);
                                textView.setText(getString(R.string.no_processed_documents_to_list));
                            }
                        }
                    } else {
                        customProgressLoader.hideDialog();
                        recyclerView.setVisibility(View.GONE);
                        emptyCardLayout.setVisibility(View.VISIBLE);
                        TextView textView = emptyCardLayout.findViewById(R.id.error_message);
                        textView.setText(getString(R.string.no_processed_documents_to_list));

                        String message = "";
                        if (fileStatusModel != null && fileStatusModel.getMessage() != null) {
                            if (fileStatusModel.getMessage().contains(AppConstants.HomePageConstants.NO_GROUPS)) {
                                message = getString(R.string.not_part_of_any_group);
                            } else {
                                message = fileStatusModel.getMessage();
                            }
                            confirmationComponent = new ConfirmationComponent(getContext(), getResources().getString(R.string.error), message, false, getResources().getString(R.string.ok), popupActions, 1);
                        }
                    }
                    customProgressLoader.hideDialog();
                } else {
                    customProgressLoader.hideDialog();
                }
            }
        });
    }



    @SuppressLint("LongLogTag")
    @Override
    public void onListItemClick(int pos, ProcessedDocumentsModel dataModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstants.StringConstants.PROCESSED_DATA_MODEL, dataModel);
        bundle.putBoolean("IS_FROM_SEARCH", false);
        Log.d("Processed Doc Selected Data",new Gson().toJson(dataModel));
//        NavigationFlowManager.openFragmentsWithOutBackStack(new ProcessedDocumentsDetailsFragment(), bundle, getActivity(), R.id.dashboard_wrapper, ProcessedFragment.TAG);
        NavigationFlowManager.openFragments(new ProcessedDocumentsDetailsFragment(),bundle,getActivity(),R.id.dashboard_wrapper,ProcessedFragment.TAG);

    }

    @Override
    public void onPopupActions(String id, int dialogId) {

    }
}