package com.techindiana.school.parent.retrofit_utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.techindiana.school.parent.retrofit_utils.restUtils.RestURLs;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Tech Indiana on 28-Jun-16.
 */
public class RetrofitUtils {
    static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(RestURLs.REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(RestURLs.REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(RestURLs.REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .addNetworkInterceptor(new AddHeaderInterceptor())
                    .build();
/*
           Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();*/

            //making object of RestAdapter
            retrofit = new Retrofit.Builder()
                    .baseUrl(RestURLs.COMMON_API_URL)
                  // .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitWithoutHeader() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(RestURLs.REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(RestURLs.REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(RestURLs.REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .addNetworkInterceptor(new AddHeaderInterceptor())
                    .build();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            //making object of RestAdapter
            retrofit = new Retrofit.Builder()
                    .baseUrl(RestURLs.COMMON_API_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                   // .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
