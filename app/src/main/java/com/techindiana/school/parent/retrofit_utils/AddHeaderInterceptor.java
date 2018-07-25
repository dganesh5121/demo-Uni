package com.techindiana.school.parent.retrofit_utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Devendra P. Chaudhari on 28-Nov-16.
 */

public class AddHeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder builder = chain.request().newBuilder();
        //builder.addHeader("API-KEY", "b3V0c291cmNl");

        return chain.proceed(builder.build());
    }
}