package com.example.salo.network.interceptors;

import android.content.Context;

import com.example.salo.application.MyApplication;
import com.example.salo.network.utils.CommonUtils;
import com.example.salo.network.utils.ConnectionInternetError;
import com.example.salo.network.utils.NetworkUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectivityInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Context context= MyApplication.getAppContext();
        Request originalRequest = chain.request();

        if (!NetworkUtils.isOnline(context)) {
            MyApplication beginApplication = (MyApplication) context;
            CommonUtils.hideLoading();
            ((ConnectionInternetError) beginApplication.getCurrentActivity()).navigateErrorPage();
        }
        Request newRequest = originalRequest.newBuilder()
                .build();
        return chain.proceed(newRequest);
    }
}

