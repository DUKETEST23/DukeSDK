package com.dukeai.manageloads.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.dukeai.manageloads.apiUtils.repositories.UploadFileRepository;
import com.dukeai.manageloads.model.FileUploadSuccessModel;
import com.dukeai.manageloads.model.GenericResponseWithJSONModel;
import com.dukeai.manageloads.model.UploadFileResponseModel;

import okhttp3.MultipartBody;

public class UploadFileViewModel extends ViewModel {
    UploadFileRepository uploadFileRepository;
    LiveData<FileUploadSuccessModel> jsonElementLiveData;
    LiveData<GenericResponseWithJSONModel> manifestLiveData;
    LiveData<UploadFileResponseModel> uploadFileResponseModelLiveData;
    Context context;

    public void setContext(Context context) {
        this.context = context;
        uploadFileRepository = new UploadFileRepository(context);
    }

    public UploadFileViewModel() {
        uploadFileRepository = new UploadFileRepository(getContext());
    }
    public Context getContext(){
        return context;
    }

    public LiveData<FileUploadSuccessModel> uploadFile(MultipartBody.Part[] file, MultipartBody.Part fileCount, MultipartBody.Part address,Context context) {
        jsonElementLiveData = uploadFileRepository.uploadFile(file, fileCount, address,context);
        return jsonElementLiveData;
    }


    public LiveData<GenericResponseWithJSONModel> getManifestLiveData(Context context) {
        manifestLiveData = uploadFileRepository.getManifest(context);
        return manifestLiveData;
    }
    public LiveData<UploadFileResponseModel> getUploadFileResponseModelLiveData(MultipartBody.Part address, MultipartBody.Part load_flag, MultipartBody.Part signature, MultipartBody.Part is_scan, MultipartBody.Part coordinates, MultipartBody.Part manifest, MultipartBody.Part[] file, MultipartBody.Part[] pdfFiles,Context context) {
        uploadFileResponseModelLiveData = uploadFileRepository.upload(address, load_flag, signature, is_scan, manifest, file, pdfFiles,coordinates,context);
        return uploadFileResponseModelLiveData;
    }

}
