package com.example.salo.retrofitProduct;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.salo.application.MyApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDTOService {
    private final Context context;
    private static ProductDTOService mInstance;
    private static final String BASE_URL = "https://autobazar.azurewebsites.net/api/"; //"http://10.0.2.2/api/"; //
    private Retrofit mRetrofit;

    private ProductDTOService() {
        context = MyApplication.getAppContext();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        SharedPreferences prefs=context.getSharedPreferences("jwtStore", Context.MODE_PRIVATE);
        final String token = "Bearer "+ prefs.getString("token","");

        Interceptor interJWT = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request newRequest = originalRequest.newBuilder()
                        .header("Authorization", token)
                        .build();
                return chain.proceed(newRequest);
            }
        };



        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interJWT)
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
