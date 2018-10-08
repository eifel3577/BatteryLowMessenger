package com.example.batterylowmessenger;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.batterylowmessenger.data.MyDatabase;
import com.example.batterylowmessenger.di.components.AppComponent;
import com.example.batterylowmessenger.di.components.DaggerAppComponent;
import com.example.batterylowmessenger.di.modules.AppModule;

public class App extends Application {

    public static App instance;
    private static AppComponent component;
    private MyDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        component = buildComponent();
        database = Room.databaseBuilder(this, MyDatabase.class, "database")
                .build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ru"));
    }

    public static AppComponent getComponent(){
        return component;
    }

    protected AppComponent buildComponent() {

        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public MyDatabase getDatabase() {return database;}
}
