package com.dukeai.manageloads.apiUtils.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.R;
import com.dukeai.manageloads.apiUtils.ApiClient;
import com.dukeai.manageloads.apiUtils.ApiUtils;
import com.dukeai.manageloads.apiUtils.DukeApi;
import com.dukeai.manageloads.interfaces.OnSuccessListener;
import com.dukeai.manageloads.model.FileUploadSuccessModel;
import com.dukeai.manageloads.model.GenericResponseWithJSONModel;
import com.dukeai.manageloads.model.UploadFileResponseModel;
import com.dukeai.manageloads.model.UserDataModel;
import com.dukeai.manageloads.utils.UserConfig;
import com.dukeai.manageloads.utils.Utilities;
import com.google.gson.Gson;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadFileRepository {
    DukeApi dukeApi;
    UserConfig userConfig = UserConfig.getInstance();
    UserDataModel userDataModel;

    public UploadFileRepository(Context context) {
        dukeApi = ApiClient.getClient(context).create(DukeApi.class);
    }

    public LiveData<FileUploadSuccessModel> uploadFile(final MultipartBody.Part[] files, final MultipartBody.Part fileCount, final MultipartBody.Part address,Context context) {
        userDataModel = userConfig.getUserDataModel();
        final MutableLiveData<FileUploadSuccessModel> data = new MutableLiveData<>();
        userDataModel.getJWTToken(context, new OnSuccessListener() {
            @Override
            public void onSuccess(String jwtToken) {
                Call<FileUploadSuccessModel> call = dukeApi.uploadMultipleFilesWithCount(jwtToken, userDataModel.getUserEmail(), files, fileCount, address);
                call.enqueue(new Callback<FileUploadSuccessModel>() {
                    @Override
                    public void onResponse(Call<FileUploadSuccessModel> call, Response<FileUploadSuccessModel> response) {
                        System.out.println("Address in Multi Upload "+new Gson().toJson(address));
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                        } else {
                            FileUploadSuccessModel model = new FileUploadSuccessModel();
                            try {
                                model.setMessage(ApiUtils.getApiError(response.errorBody().string(),context));
                                data.setValue(model);
                            } catch (Exception e) {
                                model.setMessage(Utilities.getStrings(context, R.string.failed_to_upload));
                                data.setValue(model);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FileUploadSuccessModel> call, Throwable t) {
                        FileUploadSuccessModel model = new FileUploadSuccessModel();
                        try {
                            model.setMessage(ApiUtils.getFailureErrorString(t,context));
                            data.setValue(model);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        return data;
    }

    public LiveData<GenericResponseWithJSONModel> getManifest(Context context) {
        userDataModel = userConfig.getUserDataModel();
        final MutableLiveData<GenericResponseWithJSONModel> data = new MutableLiveData<>();
        userDataModel.getJWTToken(context, new OnSuccessListener() {
            @Override
            public void onSuccess(String jwtToken) {
                Call<GenericResponseWithJSONModel> call = dukeApi.generateManifest(jwtToken, userDataModel.getUserEmail());
                call.enqueue(new Callback<GenericResponseWithJSONModel>() {
                    @Override
                    public void onResponse(Call<GenericResponseWithJSONModel> call, Response<GenericResponseWithJSONModel> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<GenericResponseWithJSONModel> call, Throwable t) {
                        try {
                            data.setValue(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        return data;
    }

    public LiveData<UploadFileResponseModel> upload(MultipartBody.Part address, MultipartBody.Part load_flag, MultipartBody.Part signature, MultipartBody.Part is_scan, MultipartBody.Part manifest, MultipartBody.Part[] file, MultipartBody.Part[] pdfFiles, MultipartBody.Part coordinates,Context context) {
        userDataModel = userConfig.getUserDataModel();
        final MutableLiveData<UploadFileResponseModel> data = new MutableLiveData<>();
        userDataModel.getJWTToken(context, new OnSuccessListener() {
            @Override
            public void onSuccess(String jwtToken) {
                Call<UploadFileResponseModel> call = dukeApi.upload(jwtToken, userDataModel.getUserEmail(), address, signature, is_scan, load_flag, coordinates,manifest, file, pdfFiles);
                call.enqueue(new Callback<UploadFileResponseModel>() {
                    @Override
                    public void onResponse(Call<UploadFileResponseModel> call, Response<UploadFileResponseModel> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                            System.out.println("Scan Response  => "+new Gson().toJson(response.body()));
                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<UploadFileResponseModel> call, Throwable t) {
                        try {
                            data.setValue(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        return data;
    }

}
