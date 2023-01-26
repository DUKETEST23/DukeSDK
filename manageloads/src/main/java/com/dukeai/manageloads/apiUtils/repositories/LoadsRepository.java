package com.dukeai.manageloads.apiUtils.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.apiUtils.ApiClient;
import com.dukeai.manageloads.apiUtils.DukeApi;
import com.dukeai.manageloads.interfaces.OnSuccessListener;
import com.dukeai.manageloads.model.CreateLoadModel;
import com.dukeai.manageloads.model.LoadsListModel;
import com.dukeai.manageloads.model.LoadsTransmitModel;
import com.dukeai.manageloads.model.RecipientsListModel;
import com.dukeai.manageloads.model.SingleLoadModel;
import com.dukeai.manageloads.model.UserDataModel;
import com.dukeai.manageloads.utils.UserConfig;
import com.dukeai.manageloads.viewmodel.GenericResponseModel;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadsRepository {
    DukeApi dukeApi;
    UserConfig userConfig = UserConfig.getInstance();
    UserDataModel userDataModel;
    static final String TAG = "LoadsRepository";
    Response<LoadsTransmitModel> responseReceived = null;
    Context context;

    public LoadsRepository(Context context) {
        this.context = context;
        dukeApi = ApiClient.getClient(context).create(DukeApi.class);
    }


    public LiveData<LoadsListModel> getUserLoads(JsonObject jsonObject, Context context) {

        userDataModel = userConfig.getUserDataModel();
        final MutableLiveData<LoadsListModel> data = new MutableLiveData<>();
        userDataModel.getJWTToken(context, new OnSuccessListener() {
            @Override
            public void onSuccess(String jwtToken) {
                Call<LoadsListModel> call = dukeApi.getUserLoads(jwtToken, userDataModel.getUserEmail(), jsonObject);
                call.enqueue(new Callback<LoadsListModel>() {
                    @Override
                    public void onResponse(Call<LoadsListModel> call, Response<LoadsListModel> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                        } else {
                            LoadsListModel model = new LoadsListModel("", "", "");
                            try {
//                        model.setMessage(response.errorBody().string());
                                data.setValue(model);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoadsListModel> call, Throwable t) {
                        data.setValue(null);
                    }
                });
            }
        });
        return data;
    }

    public LiveData<SingleLoadModel> getLoadDetail(String loadUUID,Context context) {
        userDataModel = userConfig.getUserDataModel();
        final MutableLiveData<SingleLoadModel> data = new MutableLiveData<>();
        userDataModel.getJWTToken(context, new OnSuccessListener() {
            @Override
            public void onSuccess(String jwtToken) {
                Call<SingleLoadModel> call = dukeApi.getLoadDetail(jwtToken, userDataModel.getUserEmail(), loadUUID);

                call.enqueue(new Callback<SingleLoadModel>() {
                    @Override
                    public void onResponse(Call<SingleLoadModel> call, Response<SingleLoadModel> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                        } else {
                            SingleLoadModel model = new SingleLoadModel();
                            try {
//                        model.setMessage(response.errorBody().string());
                                data.setValue(model);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SingleLoadModel> call, Throwable t) {
                        data.setValue(null);
                    }
                });
            }
        });
        return data;
    }


    public LiveData<GenericResponseModel> deleteLoadObject(String loadUUID,Context context) {
        userDataModel = userConfig.getUserDataModel();
        final MutableLiveData<GenericResponseModel> data = new MutableLiveData<>();
        userDataModel.getJWTToken(context, new OnSuccessListener() {
            @Override
            public void onSuccess(String jwtToken) {
                Call<GenericResponseModel> call = dukeApi.deleteLoadObject(jwtToken, userDataModel.getUserEmail(), loadUUID);

                call.enqueue(new Callback<GenericResponseModel>() {
                    @Override
                    public void onResponse(Call<GenericResponseModel> call, Response<GenericResponseModel> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                        } else {
                            GenericResponseModel model = new GenericResponseModel();
                            try {
//                        model.setMessage(response.errorBody().string());
                                data.setValue(model);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GenericResponseModel> call, Throwable t) {
                        data.setValue(null);
                    }
                });
            }
        });
        return data;
    }

    public LiveData<GenericResponseModel> updateRecipient(JsonObject jsonObject,Context context) {
        userDataModel = userConfig.getUserDataModel();
        final MutableLiveData<GenericResponseModel> data = new MutableLiveData<>();
        userDataModel.getJWTToken(context, new OnSuccessListener() {
            @Override
            public void onSuccess(String jwtToken) {
                Call<GenericResponseModel> call = dukeApi.updateRecipientDetail(jwtToken, userDataModel.getUserEmail(), jsonObject);

                call.enqueue(new Callback<GenericResponseModel>() {
                    @Override
                    public void onResponse(Call<GenericResponseModel> call, Response<GenericResponseModel> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                        } else {
                            GenericResponseModel model = new GenericResponseModel();
                            try {
//                        model.setMessage(response.errorBody().string());
                                data.setValue(model);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GenericResponseModel> call, Throwable t) {
                        data.setValue(null);
                    }
                });
            }
        });
        return data;
    }

    public LiveData<RecipientsListModel> getRecipientsList(String flag,Context context) {
        userDataModel = userConfig.getUserDataModel();
        Log.d(TAG, "getRecipientsList: User Email "+userDataModel.getUserEmail());
        final MutableLiveData<RecipientsListModel> data = new MutableLiveData<>();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("app_version", "2.3.7");
        jsonObject.addProperty("flag", flag);
        userDataModel.getJWTToken(context, new OnSuccessListener() {
            @Override
            public void onSuccess(String jwtToken) {
                Call<RecipientsListModel> call = dukeApi.getRecipientsList(jwtToken, userDataModel.getUserEmail(), jsonObject);

                call.enqueue(new Callback<RecipientsListModel>() {
                    @Override
                    public void onResponse(Call<RecipientsListModel> call, Response<RecipientsListModel> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                        } else {
                            RecipientsListModel model = new RecipientsListModel();
                            try {
//                        model.setMessage(response.errorBody().string());
                                data.setValue(model);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RecipientsListModel> call, Throwable t) {
                        data.setValue(null);
                    }
                });
            }
        });
        return data;
    }

    public LiveData<GenericResponseModel> deleteFromLoad(String loadUUID, String sha1,Context context) {
        userDataModel = userConfig.getUserDataModel();
        final MutableLiveData<GenericResponseModel> data = new MutableLiveData<>();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("app_version", "2.3.7");
        jsonObject.addProperty("doc_sha", sha1);
        userDataModel.getJWTToken(context, new OnSuccessListener() {
            @Override
            public void onSuccess(String jwtToken) {
                Call<GenericResponseModel> call = dukeApi.deleteFromLoad(jwtToken, userDataModel.getUserEmail(), loadUUID, jsonObject);

                call.enqueue(new Callback<GenericResponseModel>() {
                    @Override
                    public void onResponse(Call<GenericResponseModel> call, Response<GenericResponseModel> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                        } else {
                            GenericResponseModel model = new GenericResponseModel();
                            try {
//                        model.setMessage(response.errorBody().string());
                                data.setValue(model);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GenericResponseModel> call, Throwable t) {
                        data.setValue(null);
                    }
                });
            }
        });
        return data;
    }

    public LiveData<LoadsTransmitModel> transmitLoads(ArrayList<String> loadUUIDs, ArrayList<String> recipientList,Context context) {
        userDataModel = userConfig.getUserDataModel();
        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("app_version", "2.3.7");
        jsonParams.put("recipient", recipientList);
        jsonParams.put("load_uuid", loadUUIDs);

        Log.d("json param: ", jsonParams.toString());

        final MutableLiveData<LoadsTransmitModel> data = new MutableLiveData<>();
        userDataModel.getJWTToken(context, new OnSuccessListener() {
            @Override
            public void onSuccess(String jwtToken) {
                Call<LoadsTransmitModel> call = dukeApi.transmitLoads(jwtToken, userDataModel.getUserEmail(), jsonParams);

                call.enqueue(new Callback<LoadsTransmitModel>() {
                    @Override
                    public void onResponse(Call<LoadsTransmitModel> call, Response<LoadsTransmitModel> response) {
                        responseReceived = response;
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                        } else {
                            LoadsTransmitModel model = new LoadsTransmitModel();
                            try {
//                        model.setMessage(response.errorBody().string());
                                data.setValue(model);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoadsTransmitModel> call, Throwable t) {
                        LoadsTransmitModel model = new LoadsTransmitModel();
                        if (responseReceived != null) {
                            model.setMessage(responseReceived.message());
                        } else {
                            model.setMessage("Error: Something unexpected happened!");
                        }
                        data.setValue(model);
                    }
                });
            }
        });
        return data;
    }


    public LiveData<CreateLoadModel> createLoad(Context context) {
        userDataModel = userConfig.getUserDataModel();
        final MutableLiveData<CreateLoadModel> data = new MutableLiveData<>();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("app_version", "2.3.7");
        userDataModel.getJWTToken(context, new OnSuccessListener() {
            @Override
            public void onSuccess(String jwtToken) {
                Call<CreateLoadModel> call = dukeApi.createLoad(jwtToken, userDataModel.getUserEmail(), jsonObject);

                call.enqueue(new Callback<CreateLoadModel>() {
                    @Override
                    public void onResponse(Call<CreateLoadModel> call, Response<CreateLoadModel> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                        } else {
                            CreateLoadModel model = new CreateLoadModel();
                            try {
//                        model.setMessage(response.errorBody().string());
                                data.setValue(model);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CreateLoadModel> call, Throwable t) {
                        data.setValue(null);
                    }
                });
            }
        });
        return data;
    }

}
