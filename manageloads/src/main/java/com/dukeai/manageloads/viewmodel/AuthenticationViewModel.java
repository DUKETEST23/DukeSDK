package com.dukeai.manageloads.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dukeai.manageloads.apiUtils.repositories.AuthenticationRepository;
import com.dukeai.manageloads.model.AuthModel;

public class AuthenticationViewModel extends ViewModel {
    AuthenticationRepository authenticationRepository;
    LiveData<AuthModel> authModelLiveData;
    Context context;

    public AuthenticationViewModel(){

    }

    public void setContext(Context context) {
        this.context = context;
        authenticationRepository = new AuthenticationRepository(context);
    }

    public LiveData<AuthModel> authentication(){
        authModelLiveData = authenticationRepository.getAuth(context);
        return  authModelLiveData;
    }
}
