package com.dallmeier.evidencer.di;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dallmeier.evidencer.base.BaseApplication;
import com.dallmeier.evidencer.common.Constant;
import com.dallmeier.evidencer.network.ApiService;
import com.dallmeier.evidencer.network.ApiServiceMap;
import com.dallmeier.evidencer.network.NullOnEmptyConverterFactory;
import com.dallmeier.evidencer.utils.SharedPrefUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static java.util.concurrent.TimeUnit.SECONDS;

@Module
@InstallIn(SingletonComponent.class)
public class BaseApplicationModule {
    @Provides
    @Singleton
    public OkHttpClient initOkhttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(loggingInterceptor);
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Accept", "application/json")
                    .header("Authorization", SharedPrefUtil.getInstance().getAccessToken())
                    .method(original.method(), original.body());
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });
        Log.d("token", SharedPrefUtil.getInstance().getAccessToken());
        /* configRetrofit(Constant.DOMAIN, httpClient, gson());*/
        httpClient.protocols(Collections.singletonList(Protocol.HTTP_1_1));
        return httpClient.readTimeout(30, SECONDS).connectTimeout(30, TimeUnit.SECONDS).build();
    }

    @Provides
    @Singleton
    public static Retrofit.Builder configRetrofit(OkHttpClient httpClient) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.addConverterFactory(new NullOnEmptyConverterFactory());
        builder.addConverterFactory(GsonConverterFactory.create(gson));
        builder.addConverterFactory(ScalarsConverterFactory.create());
        builder.client(httpClient);
        return builder;
    }

    @Provides
    @Singleton
    public static Picasso configPicasso(OkHttpClient httpClient) {
        int memClass = ((ActivityManager) BaseApplication.getInstance()
                .getSystemService(Context.ACTIVITY_SERVICE))
                .getLargeMemoryClass();
        int cacheSize = 1024 * 1024 * memClass / 4;

        return new Picasso.Builder(BaseApplication.getInstance()).memoryCache(
                new LruCache(cacheSize))
                .downloader(new OkHttp3Downloader(httpClient))
                .build();
    }

    @Provides
    @Singleton
    public static Gson gson() {
        return new Gson();
    }

    @Provides
    @Singleton
    public static Handler handler() {
        return new Handler(Looper.getMainLooper());
    }

    @Provides
    @Singleton
    public ApiService apiService(Retrofit.Builder retrofitBuilder) {
        retrofitBuilder.baseUrl(Constant.DOMAIN);
        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(ApiService.class);
    }

    @Provides
    @Singleton
    public static ApiServiceMap apiServiceMap(Retrofit.Builder retrofitBuilder) {
        retrofitBuilder.baseUrl(Constant.STREET_MAP_SEARCH);
        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(ApiServiceMap.class);
    }
}
