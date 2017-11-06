package com.insulardevelopment.touristslittlehelper.di;

import android.content.Context;

import com.insulardevelopment.touristslittlehelper.MyApplication;
import com.insulardevelopment.touristslittlehelper.network.NetworkModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Маргарита on 06.11.2017.
 */

@Module(includes = {NetworkModule.class, ViewModelModule.class})
public class AppModule {
    @Provides
    public Context provideContext(MyApplication app) {
        return app.getApplicationContext();
    }
}
