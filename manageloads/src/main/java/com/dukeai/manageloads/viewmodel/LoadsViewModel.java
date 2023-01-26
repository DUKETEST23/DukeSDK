package com.dukeai.manageloads.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dukeai.manageloads.apiUtils.repositories.LoadsRepository;
import com.dukeai.manageloads.model.CreateLoadModel;
import com.dukeai.manageloads.model.LoadsListModel;
import com.dukeai.manageloads.model.LoadsTransmitModel;
import com.dukeai.manageloads.model.RecipientsListModel;
import com.dukeai.manageloads.model.SingleLoadModel;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class LoadsViewModel extends ViewModel {
    LoadsRepository loadsRepository;
    LiveData<CreateLoadModel> createLoadModelLiveData;
    LiveData<RecipientsListModel> recipientsListModelLiveData;
    LiveData<GenericResponseModel> updateRecipientsLiveData;
    LiveData<LoadsListModel> loadsListModelLiveData;
    LiveData<SingleLoadModel> getLoadDetailLiveData;
    LiveData<GenericResponseModel> deleteLoadObjectLiveData;
    LiveData<LoadsTransmitModel> transmitLoadsModelLiveData;
    Context context;
   /*
    LiveData<LoadsTransmitModel> transmitProcessedDocsLiveData;*/

    LiveData<GenericResponseModel> deleteFromLoadLiveData;

    public void setContext(Context context) {
        this.context = context;
        loadsRepository = new LoadsRepository(context);
    }

    public LoadsViewModel() {
        loadsRepository = new LoadsRepository(getContext());
    }
    public Context getContext(){
        return context;
    }
    public LiveData<CreateLoadModel> createLoad(Context context) {
        createLoadModelLiveData = loadsRepository.createLoad(context);
        return createLoadModelLiveData;
    }

    public LiveData<GenericResponseModel> getDeleteLoadObjectLiveData(String loadUUID,Context context) {
        deleteLoadObjectLiveData = loadsRepository.deleteLoadObject(loadUUID,context);
        return deleteLoadObjectLiveData;
    }

    public LiveData<LoadsListModel> getLoadsListLiveData(JsonObject jsonObject, Context context) {
        loadsListModelLiveData = loadsRepository.getUserLoads(jsonObject,context);
        return loadsListModelLiveData;
    }

    public LiveData<GenericResponseModel> getUpdateRecipientsLiveData(JsonObject jsonObject,Context context) {
        updateRecipientsLiveData = loadsRepository.updateRecipient(jsonObject,context);
        return updateRecipientsLiveData;
    }

    public LiveData<RecipientsListModel> getRecipientsList(String flag,Context context) {
        recipientsListModelLiveData = loadsRepository.getRecipientsList(flag,context);
        return recipientsListModelLiveData;
    }

    public LiveData<LoadsTransmitModel> getTransmitLoadsModelLiveData(ArrayList<String> loadUUIDs, ArrayList<String> recipientsList,Context context) {
        transmitLoadsModelLiveData = loadsRepository.transmitLoads(loadUUIDs, recipientsList,context);
        return transmitLoadsModelLiveData;
    }

    /*
     public LiveData<LoadsTransmitModel> getTransmitProcessedDocsLiveData(ArrayList<String> docSHA1s) {
         transmitProcessedDocsLiveData = loadsRepository.transmitProcessedDocs(docSHA1s);
         return transmitProcessedDocsLiveData;
     }

     */

    public LiveData<GenericResponseModel> getDeleteFromLoadLiveData(String loadUUID, String sha1,Context context) {
        deleteFromLoadLiveData = loadsRepository.deleteFromLoad(loadUUID, sha1,context);
        return deleteFromLoadLiveData;
    }

    public LiveData<SingleLoadModel> getGetLoadDetailLiveData(String loadUUID,Context context) {
        getLoadDetailLiveData = loadsRepository.getLoadDetail(loadUUID,context);
        return getLoadDetailLiveData;
    }
}
