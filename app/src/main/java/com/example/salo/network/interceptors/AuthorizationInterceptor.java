package com.example.salo.network.interceptors;

import com.example.salo.LoginFragment;
import com.example.salo.NavigationHost;
import com.example.salo.application.MyApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthorizationInterceptor implements Interceptor  {
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request newRequest = chain.request().newBuilder()
                .build();
        Response response = chain.proceed(newRequest);
        if (response.code() == 401) {
            MyApplication context = (MyApplication) MyApplication.getAppContext();
            NavigationHost navigationHost = (NavigationHost) context.getCurrentActivity();
            navigationHost.navigateTo(new LoginFragment(), false);
        }
        return response;
    }
}
