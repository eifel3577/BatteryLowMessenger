package com.example.batterylowmessenger.di.components;

import com.example.batterylowmessenger.di.modules.AppModule;
import com.example.batterylowmessenger.repository.ContactsRepository;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = AppModule.class)
@Singleton
public interface AppComponent {
    void inject(ContactsRepository contactsRepository);

}