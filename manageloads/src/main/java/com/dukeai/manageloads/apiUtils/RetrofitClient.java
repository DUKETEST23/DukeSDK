package com.dukeai.manageloads.apiUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static Retrofit getClient(String baseUrl) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(50000, TimeUnit.SECONDS)
                .readTimeout(50000, TimeUnit.SECONDS).build();

        Retrofit retrofit = null;
        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson)).client(client)
                    .build();

        }

        return retrofit;
    }
}
