package com.dukeai.manageloads.apiUtils.repositories;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.R;
import com.dukeai.manageloads.apiUtils.ApiClient;
import com.dukeai.manageloads.apiUtils.ApiConstants;
import com.dukeai.manageloads.apiUtils.ApiUtils;
import com.dukeai.manageloads.apiUtils.DukeApi;
import com.dukeai.manageloads.apiUtils.InputParams;
import com.dukeai.manageloads.interfaces.OnSuccessListener;
import com.dukeai.manageloads.model.DocumentDetailsModel;
import com.dukeai.manageloads.model.DownloadImageModel;
import com.dukeai.manageloads.model.FileDeleteSuccessModel;
import com.dukeai.manageloads.model.FileStatusModel;
import com.dukeai.manageloads.model.SubscriptionPlan;
import com.dukeai.manageloads.model.UpdatePaymentModel;
import com.dukeai.manageloads.model.UserDataModel;
import com.dukeai.manageloads.utils.UserConfig;
import com.dukeai.manageloads.utils.Utilities;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.EnumUtils;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileStatusRepository {
    DukeApi dukeApi;
    UserConfig userConfig = UserConfig.getInstance();
    UserDataModel userDataModel;
    Context context;

    public FileStatusRepository(Context context) {
        this.context = context;
        dukeApi = ApiClient.getClient(context).create(DukeApi.class);
    }

    public LiveData<FileStatusModel> getFileStatus(final String numberOfDocs, Context context) {
        userDataModel = userConfig.getUserDataModel();
        final MutableLiveData<FileStatusModel> data = new MutableLiveData<>();

        userDataModel.getJWTToken(context, new OnSuccessListener() {
            @Override
            public void onSuccess(String jwtToken) {

                JsonObject jsonObject = InputParams.fileStatus(Utilities.getReportCurrentDate(), Utilities.getReportLastQuarterDate(), numberOfDocs);
                jsonObject.addProperty("app_build", "300");
                jsonObject.addProperty("app_version", "2.3.5");
                Call<FileStatusModel> call = dukeApi.getFileStatus(jwtToken, userDataModel.getUserEmail(), jsonObject);
                call.enqueue(new Callback<FileStatusModel>() {
                    @Override
                    public void onResponse(Call<FileStatusModel> call, Response<FileStatusModel> response) {
                        if (response.isSuccessful()) {
                            if (response != null && response.body() != null) {

                                Log.i("responsebodyy", response.body().getMemberStatus());
                                if(response.body().getUniqueUserId().length() > 0) {
                                    Duke.uniqueUserId = response.body().getUniqueUserId();
                                }
                                data.setValue(response.body());
                                if(checkMemberStatus(response.body().getMemberStatus())) {
                                    Duke.subscriptionPlan.setMemberStatus(SubscriptionPlan.getSubscriptionPlanType(response.body().getMemberStatus()));
                                } else {
                                    Duke.subscriptionPlan.setMemberStatus(SubscriptionPlan.MemberStatus.NONE);
                                }
//                                Duke.subscriptionPlan.setMemberStatus(SubscriptionPlan.getSubscriptionPlanType(response.body().getMemberStatus()));
                            } else {
                                FileStatusModel fileStatusModel = new FileStatusModel();
                                try {
                                    fileStatusModel.setMessage(Utilities.getStrings(context, R.string.got_empty_response));
                                    data.setValue(fileStatusModel);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    data.setValue(null);
                                }
                            }
                        } else {
                            FileStatusModel fileStatusModel = new FileStatusModel();
                            if (response != null && response.errorBody() != null) {
                                try {
                                    fileStatusModel.setMessage(ApiUtils.getApiError(response.errorBody().string(),context));
                                    data.setValue(fileStatusModel);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    data.setValue(null);
                                }
                            } else {
                                data.setValue(null);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FileStatusModel> call, Throwable t) {
                        FileStatusModel fileStatusModel = new FileStatusModel();
                        try {
                            fileStatusModel.setMessage(ApiUtils.getFailureErrorString(t,context));
                            data.setValue(fileStatusModel);
                        } catch (Exception e) {
                            data.setValue(null);
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        return data;
    }

    private boolean checkMemberStatus(String memberStatus) {
        boolean isMemberStatusValid = false;

        if(EnumUtils.isValidEnum(SubscriptionPlan.MemberStatus.class, memberStatus)){
            isMemberStatusValid = true;
        } else {
            isMemberStatusValid = false;
        }

        return isMemberStatusValid;
    }

    public LiveData<DocumentDetailsModel> fetchDocumentDetails(final String sha1,Context context) {
        userDataModel = userConfig.getUserDataModel();
        final MutableLiveData<DocumentDetailsModel> data = new MutableLiveData<>();
        userDataModel.getJWTToken(context, new OnSuccessListener() {
            @Override
            public void onSuccess(String jwtToken) {
                Call<DocumentDetailsModel> call = dukeApi.fetchDocumentDetails(jwtToken, userDataModel.getUserEmail(), sha1);
                call.enqueue(new Callback<DocumentDetailsModel>() {
                    @Override
                    public void onResponse(Call<DocumentDetailsModel> call, Response<DocumentDetailsModel> response) {
                        if (response.isSuccessful()) {
                            JsonObject obj = new JsonObject();
                            JSONObject object = new JSONObject();
                            DocumentDetailsModel documentDetailsModel = new DocumentDetailsModel();
                            documentDetailsModel = response.body();
                            data.setValue(response.body());
                        } else {
                            data.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<DocumentDetailsModel> call, Throwable t) {
                        DocumentDetailsModel model = new DocumentDetailsModel();
                        try {
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

    public LiveData<UpdatePaymentModel> updateDocumentSignature(String sha1, String signature, String is_scan,Context context) {
        userDataModel = userConfig.getUserDataModel();
        final MutableLiveData<UpdatePaymentModel> data = new MutableLiveData<>();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sha1", sha1);
        jsonObject.addProperty("signature", signature);
        jsonObject.addProperty("is_scan", is_scan);
        userDataModel.getJWTToken(context, new OnSuccessListener() {
            @Override
            public void onSuccess(String jwtToken) {
                Call<UpdatePaymentModel> call = dukeApi.updateDocumentSignature(jwtToken, userDataModel.getUserEmail(), jsonObject);

                call.enqueue(new Callback<UpdatePaymentModel>() {
                    @Override
                    public void onResponse(Call<UpdatePaymentModel> call, Response<UpdatePaymentModel> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                        } else {
                            data.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdatePaymentModel> call, Throwable t) {
                        UpdatePaymentModel updatePaymentModel = new UpdatePaymentModel();
                        try {
                            updatePaymentModel.setMsg(ApiUtils.getFailureErrorString(t,context));
                            data.setValue(updatePaymentModel);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        return data;
    }

    public LiveData<DownloadImageModel> downloadInnerFiles(final String fileName, final int screenWidth, final int screenHeight,Context context) {
        userDataModel = userConfig.getUserDataModel();
        final MutableLiveData<DownloadImageModel> data = new MutableLiveData<>();
        final String content_type = ApiConstants.DownloadFile.CONTENT_TYPE;
        final String accept = ApiConstants.DownloadFile.ACCEPT;
        userDataModel.getJWTToken(context, new OnSuccessListener() {
            @Override
            public void onSuccess(String jwtToken) {
                Call<ResponseBody> call = dukeApi.downloadFile(jwtToken, content_type, accept, userDataModel.getUserEmail(), fileName);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            DownloadImageModel model = new DownloadImageModel();
                            Bitmap bm = Utilities.decodeSampledBitmapFromResource(response.body().byteStream(), screenWidth, screenHeight);
                            model.setBitmap(bm);
                            data.setValue(model);
                        } else {
                            data.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        DownloadImageModel model = new DownloadImageModel();
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

    public LiveData<DownloadImageModel> downloadFile(final String fileName, final int screenWidth, final int screenHeight,Context context) {
        userDataModel = userConfig.getUserDataModel();
        final MutableLiveData<DownloadImageModel> data = new MutableLiveData<>();
        final String content_type = ApiConstants.DownloadFile.CONTENT_TYPE;
        final String accept = ApiConstants.DownloadFile.ACCEPT;
        userDataModel.getJWTToken(context, new OnSuccessListener() {
            @Override
            public void onSuccess(String jwtToken) {
                Call<ResponseBody> call = dukeApi.downloadFile(jwtToken, content_type, accept, userDataModel.getUserEmail(), fileName);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            DownloadImageModel model = new DownloadImageModel();
                            Bitmap bm = Utilities.decodeSampledBitmapFromResource(response.body().byteStream(), screenWidth, screenHeight);
                            Bitmap resized = Utilities.getResizedBitmap(bm, 200);
                            model.setBitmap(resized);
                            data.setValue(model);
                        } else {
                            data.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        DownloadImageModel model = new DownloadImageModel();
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

    public LiveData<FileDeleteSuccessModel> deleteFile(final String sha1,Context context) {
        userDataModel = userConfig.getUserDataModel();
        final MutableLiveData<FileDeleteSuccessModel> data = new MutableLiveData<>();
        userDataModel.getJWTToken(context, new OnSuccessListener() {
            @Override
            public void onSuccess(String jwtToken) {
                Call<FileDeleteSuccessModel> call = dukeApi.deleteFile(jwtToken, userDataModel.getUserEmail(), sha1);
                call.enqueue(new Callback<FileDeleteSuccessModel>() {
                    @Override
                    public void onResponse(Call<FileDeleteSuccessModel> call, Response<FileDeleteSuccessModel> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                        } else {
                            data.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<FileDeleteSuccessModel> call, Throwable t) {
                        FileDeleteSuccessModel model = new FileDeleteSuccessModel();
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

}
