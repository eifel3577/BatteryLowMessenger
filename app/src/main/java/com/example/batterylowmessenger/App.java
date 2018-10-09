package com.example.batterylowmessenger;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.batterylowmessenger.data.MyDatabase;
import com.example.batterylowmessenger.di.components.AppComponent;
import com.example.batterylowmessenger.di.components.DaggerAppComponent;
import com.example.batterylowmessenger.di.modules.AppModule;
import com.example.batterylowmessenger.di.modules.RepositoryModule;


public class App extends Application {

    public static App instance;
    private MyDatabase database;
    private static AppComponent appComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appComponent = buildAppComponent();

        database = Room.databaseBuilder(this, MyDatabase.class, "database")
                .build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ru"));
    }

    protected AppComponent buildAppComponent() {

        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .repositoryModule(new RepositoryModule())
                .build();
    }



    public static App getInstance() {
        return instance;
    }

    public MyDatabase getDatabase() {return database;}

    public static AppComponent getAppComponent(){
        return appComponent;
    }


}
