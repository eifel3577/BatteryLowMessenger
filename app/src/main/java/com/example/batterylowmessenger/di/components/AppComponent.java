package com.example.batterylowmessenger.di.components;


import android.content.Context;

import com.example.batterylowmessenger.di.modules.AppModule;
import com.example.batterylowmessenger.di.modules.RepositoryModule;
import com.example.batterylowmessenger.repository.ContactsRepository;
import com.example.batterylowmessenger.services.ApplicationService;
import com.example.batterylowmessenger.viewModels.BatteryLevelFragmentViewModel;
import com.example.batterylowmessenger.viewModels.ContactFragmentViewModel;
import com.example.batterylowmessenger.viewModels.HomeFragmentViewModel;
import com.example.batterylowmessenger.viewModels.InfoFragmentViewModel;
import com.example.batterylowmessenger.viewModels.MessageFragmentViewModel;

import java.util.concurrent.Executor;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class,RepositoryModule.class})
@Singleton
public interface AppComponent {
    void inject(ContactsRepository contactsRepository);
    void inject(ApplicationService service);
    void inject(BatteryLevelFragmentViewModel batteryLevelFragmentViewModel);
    void inject(ContactFragmentViewModel contactFragmentViewModel);
    void inject(InfoFragmentViewModel infoFragmentViewModel);
    void inject(MessageFragmentViewModel messageFragmentViewModel);
    void inject(HomeFragmentViewModel homeFragmentViewModel);
}
