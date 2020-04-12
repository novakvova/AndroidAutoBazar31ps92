package com.example.salo.prductview.network.api;

import android.content.Context;

import com.example.salo.application.MyApplication;
import com.example.salo.network.interceptors.AuthorizationInterceptor;
import com.example.salo.network.interceptors.ConnectivityInterceptor;
import com.example.salo.network.interceptors.JWTInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDTOService {
    private final Context context;
    private static ProductDTOService mInstance;
    private static final String BASE_URL = "https://autobaza21.azurewebsites.net/api/"; //"http://10.0.2.2/api/"; //
    private Retrofit mRetrofit;

    private ProductDTOService() {
        context = MyApplication.getAppContext();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new ConnectivityInterceptor())
                .addInterceptor(new JWTInterceptor())
                .addInterceptor(new AuthorizationInterceptor())
                .addInterceptor(interceptor);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public static ProductDTOService getInstance() {
        if (mInstance == null) {
            mInstance = new ProductDTOService();
        }
        return mInstance;
    }

    public ProductDTOHolderApi getJSONApi() {
        return mRetrofit.create(ProductDTOHolderApi.class);
    }
}
