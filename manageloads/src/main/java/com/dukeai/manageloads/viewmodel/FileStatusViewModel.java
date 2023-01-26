package com.dukeai.manageloads.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dukeai.manageloads.apiUtils.repositories.FileStatusRepository;
import com.dukeai.manageloads.model.ByteStreamResponseModel;
import com.dukeai.manageloads.model.DocumentDetailsModel;
import com.dukeai.manageloads.model.DownloadImageModel;
import com.dukeai.manageloads.model.DownloadReportModel;
import com.dukeai.manageloads.model.FileDeleteSuccessModel;
import com.dukeai.manageloads.model.FileStatusModel;
import com.dukeai.manageloads.model.UpdatePaymentModel;

public class FileStatusViewModel extends ViewModel {
    FileStatusRepository fileStatusRepository;
    LiveData<FileStatusModel> fileStatusModelLiveData;
    LiveData<DownloadImageModel> liveData;
    LiveData<FileDeleteSuccessModel> deleteSuccessModelLiveData;
    LiveData<UpdatePaymentModel> updatePaymentModelLiveData;
    LiveData<DocumentDetailsModel> getDocumentDetailsLiveData;
    LiveData<ByteStreamResponseModel> getPODDocByteResponse;
    LiveData<UpdatePaymentModel> updateDocumentSignature;
    LiveData<DownloadReportModel> downloadReportModelLiveData;
    Context context;



    public FileStatusViewModel() {
        fileStatusRepository = new FileStatusRepository(getContext());
    }

    public Context getContext(){
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
        fileStatusRepository = new FileStatusRepository(context);
    }

    public LiveData<FileStatusModel> getFileStatusModelLiveData(String numberOfDocs, Context context) {

        fileStatusModelLiveData = fileStatusRepository.getFileStatus(numberOfDocs,context);
        return fileStatusModelLiveData;
    }
    public LiveData<DocumentDetailsModel> getDocumentDetails(String sha1,Context context) {
        getDocumentDetailsLiveData = fileStatusRepository.fetchDocumentDetails(sha1,context);
        return  getDocumentDetailsLiveData;
    }

    public LiveData<UpdatePaymentModel> updateDocumentSignature(String sha1, String signature, String is_scan,Context context) {
        updateDocumentSignature = fileStatusRepository.updateDocumentSignature(sha1, signature, is_scan,context);
        return updateDocumentSignature;
    }

    public LiveData<DownloadImageModel> downloadFile(String fileName, int screenWidth, int screenHeight, Boolean isInnerFile,Context context) {
        if (isInnerFile) {
            liveData = fileStatusRepository.downloadInnerFiles(fileName, screenWidth, screenHeight,context);
        } else {
            liveData = fileStatusRepository.downloadFile(fileName, screenWidth, screenHeight,context);
        }
        return liveData;
    }

    public LiveData<FileDeleteSuccessModel> deleteFile(String sha1,Context context) {
        deleteSuccessModelLiveData = fileStatusRepository.deleteFile(sha1,context);
        return deleteSuccessModelLiveData;
    }

}
