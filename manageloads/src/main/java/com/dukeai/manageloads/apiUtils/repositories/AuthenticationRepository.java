package com.dukeai.manageloads.apiUtils.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dukeai.manageloads.apiUtils.ApiClient;
import com.dukeai.manageloads.apiUtils.AuthApiUtil;
import com.dukeai.manageloads.apiUtils.DukeApi;
import com.dukeai.manageloads.model.AuthModel;
import com.dukeai.manageloads.model.ConfigModel;
import com.dukeai.manageloads.model.UserDataModel;
import com.dukeai.manageloads.utils.UserConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationRepository {
    DukeApi dukeApi;
    UserConfig userConfig = UserConfig.getInstance();
    UserDataModel userDataModel;
    static final String TAG = "AuthenticationRepository";
    Response<AuthModel> responseReceived = null;
    Context context;

    public AuthenticationRepository(Context context) {
        dukeApi = ApiClient.getClient(context).create(DukeApi.class);
    }


    public LiveData<AuthModel> getAuth(Context context){
        this.context = context;
        final MutableLiveData<AuthModel> data = new MutableLiveData<>();
        dukeApi.authentication(ConfigModel.client_id,
                ConfigModel.api_key,
                ConfigModel.cust_id,
                ConfigModel.idToken,
                ConfigModel.accessToken,
                ConfigModel.refreshToken).enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(Call<AuthModel> call, Response<AuthModel> response) {
                if (response.isSuccessful()) {

                    if(response.body().getStatus()){
                        data.setValue(response.body());
                    }else{
                        AuthModel model = new AuthModel();
                        try {
//                        model.setMessage(response.errorBody().string());
                            data.setValue(model);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    AuthModel model = new AuthModel();
                    try {
//                        model.setMessage(response.errorBody().string());
                        data.setValue(model);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthModel> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }


}
