package com.github.brunodles.githubpopular.api;

import com.github.brunodles.githubpopular.api.gson.BooleanDeserializer;
import com.github.brunodles.githubpopular.api.gson.MixedDateDeserializer;
import com.github.brunodles.okhttp.CacheOverrideInterceptor;
import com.github.brunodles.okhttp.GithubAuthInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.Date;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class Api {

    private final Retrofit retrofit;

    public Api(String baseUrl, File cacheDir, Func0<String> clientIdProvider,
               Func0<String> clientSecretProvider) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        File httpCacheDirectory = new File(cacheDir, "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        GithubAuthInterceptor githubAuthIntecerptor = new GithubAuthInterceptor(
                clientIdProvider, clientSecretProvider);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(githubAuthIntecerptor)
                .addInterceptor(new CacheOverrideInterceptor())
                .cache(cache)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson()))
                .addCallAdapterFactory(
                        RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    private Gson gson() {
        BooleanDeserializer deserializer = new BooleanDeserializer();
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new MixedDateDeserializer(
                        "yyyy-MM-dd'T'HH:mm:ssZ", "yyyy-MM-dd'T'HH:mm:ss' 00:00'"))
                .registerTypeAdapter(Boolean.TYPE, deserializer)
                .registerTypeAdapter(Boolean.class, deserializer)
                .create();
    }

    public GithubEndpoint github() {
        return retrofit.create(GithubEndpoint.class);
    }
}
