package com.insulardevelopment.touristslittlehelper.network;

import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Маргарита on 06.11.2017.
 */

@Singleton
@Module
public class NetworkModule {

    private static final String BASIC_INTERCEPTOR = "basic";
    private static final String BODY_INTERCEPTOR = "body";
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";

    @Named(BASIC_INTERCEPTOR)
    @Singleton
    @Provides
    public HttpLoggingInterceptor provideHttpBasicLoggingInterceptor(){
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);
    }

    @Named(BODY_INTERCEPTOR)
    @Singleton
    @Provides
    public HttpLoggingInterceptor provideHttpBodyLoggingInterceptor(){
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient(@Named(BASIC_INTERCEPTOR)HttpLoggingInterceptor basicInterceptor,
                            @Named(BODY_INTERCEPTOR)HttpLoggingInterceptor bodyInterceptor){
        return  new OkHttpClient.Builder()
                .addInterceptor(basicInterceptor)
                .addInterceptor(bodyInterceptor)
                .build();
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    public RouteAPI provideRouteAPI(Retrofit retrofit){
        return retrofit.create(RouteAPI.class);
    }
}