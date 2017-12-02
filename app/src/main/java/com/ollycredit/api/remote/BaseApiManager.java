package com.ollycredit.api.remote;

import com.ollycredit.BuildConfig;
import com.ollycredit.api.service.ApiService;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.inject.Singleton;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Singleton
public class BaseApiManager {



//    public final static String BASE_URL = "http://ec2-52-204-144-146.compute-1.amazonaws.com:8000/"; // dev
//    public final static String BASE_URL = "http://ec2-52-204-144-146.compute-1.amazonaws.com:8001/"; // dev 2 old flow

    //public final static String BASE_URL = "http://api.m.getolly.ga/"; // prod server



    private static Retrofit retrofit;

    private static ApiService apiService;

    public BaseApiManager() {
        String authToken = "";
        createService(authToken);
    }

    private static void init() {
        apiService = createApi(ApiService.class);
    }

    private static <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }

    public static void createService(String authToken) {


        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(logger)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();

//        retrofit = new Retrofit.Builder()
//                .baseUrl(new BaseURL().getUrl())
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .client(okHttpClient)
//                .build();
        init();
    }

    public ApiService getApiService() {
        return apiService;
    }

}
