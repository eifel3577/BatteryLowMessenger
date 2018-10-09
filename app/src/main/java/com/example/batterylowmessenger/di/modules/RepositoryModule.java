package com.example.batterylowmessenger.di.modules;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.batterylowmessenger.repository.ContactsRepository;

import java.util.concurrent.Executor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @NonNull
    @Singleton
    public ContactsRepository provideRepository() {
        return new ContactsRepository();
    }
}
