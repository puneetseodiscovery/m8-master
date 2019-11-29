package com.m8.m8;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    //private final static String BASE_API_URL = "https://app.m8s.world/api/";
    private final static String BASE_API_URL = "https://app.m8s.world/api/";
    //public final static String BASE_API_URL_FOR_START_CATEGORY = "https://app.m8s.world/public/upload/categories/";
    public final static String BASE_API_URL_FOR_START_CATEGORY = "https://app.m8s.world/public/upload/categories/";
    private static Retrofit retrofit = null;
    private static Gson gson = new GsonBuilder().create();

    private static HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor);



    private static OkHttpClient okHttpClient = okHttpClientBuilder.build();

     public static OkHttpClient okHttpClientN = new OkHttpClient.Builder()
            .connectTimeout(1000, TimeUnit.MINUTES)
            .readTimeout(1000, TimeUnit.SECONDS)
            .writeTimeout(900, TimeUnit.SECONDS)
            .build();


    public static <T> T createService(Class<T> serviceClass) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(okHttpClientN)
                    .baseUrl(BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(serviceClass);
    }

}