package com.example.batterylowmessenger.di.modules;


import android.content.Context;

import com.android.annotations.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Context appContext;

    public AppModule(@NonNull Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    Context provideContext(){
        return appContext;
    }

    @Provides
    Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}
