package com.example.batterylowmessenger.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.example.batterylowmessenger.LoadData;
import com.example.batterylowmessenger.data.Contact;
import com.example.batterylowmessenger.repository.ContactsRepository;
import com.example.batterylowmessenger.sharedPreferenceStorage.ApplicationSharedPreference;

import java.util.ArrayList;
import java.util.List;


public class InfoFragmentViewModel extends AndroidViewModel {

    private Context context;
    private ContactsRepository repository;
    public ObservableField<String> checkedContactString = new ObservableField<>();
    public ObservableField<String>messageText = new ObservableField<>();
    public ObservableField<String>batteryLevelText = new ObservableField<>();

    public InfoFragmentViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ContactsRepository();
        this.context = getApplication();
    }

    public void start(){
        repository.getCheckedContactList(new LoadData.LoadContactCallback() {
            @Override
            public void onContactsLoaded(List<Contact> tasks) {
                StringBuilder builder = new StringBuilder();
                builder.append("Выбранные контакты:");
                builder.append("\n");
                for(Contact contact:tasks){
                    builder.append(contact.getContactName());
                    builder.append("\n");
                }
                checkedContactString.set(builder.toString());

            }

            @Override
            public void onDataNotAvailable() {
                StringBuilder builder = new StringBuilder();
                builder.append("Выбранные контакты:");
                builder.append("\n");
            }
        });

        if(ApplicationSharedPreference.getStoredMessage(context)!=null){
            StringBuilder builderMessage = new StringBuilder();
            builderMessage.append("Выбранное сообщение:");
            builderMessage.append("\n");
            builderMessage.append(ApplicationSharedPreference.getStoredMessage(context));
            messageText.set(builderMessage.toString());
        }

        populateBatteryLevelField();
    }

    private void populateBatteryLevelField(){
        StringBuilder builderBatteryLevel = new StringBuilder();
        builderBatteryLevel.append("Уровень заряда:");
        builderBatteryLevel.append("\n");
        if(ApplicationSharedPreference.getStoredBatteryLevel(context)!=null){
            if(ApplicationSharedPreference.getStoredBatteryLevel(context).startsWith("0")){
                builderBatteryLevel.append(ApplicationSharedPreference.getStoredBatteryLevel(context).substring(1));
            }
            else builderBatteryLevel.append(ApplicationSharedPreference.getStoredBatteryLevel(context));
        }
        else {
            builderBatteryLevel.append("3");
        }
        builderBatteryLevel.append(" %");
        batteryLevelText.set(builderBatteryLevel.toString());
    }

}