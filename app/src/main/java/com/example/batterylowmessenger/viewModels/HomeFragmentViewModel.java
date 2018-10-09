package com.example.batterylowmessenger.viewModels;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.example.batterylowmessenger.App;
import com.example.batterylowmessenger.CheckedContact;
import com.example.batterylowmessenger.repository.ContactsRepository;

import javax.inject.Inject;

public class HomeFragmentViewModel extends ViewModel {

    @Inject
    ContactsRepository repository;

    public HomeFragmentViewModel() {
        App.getAppComponent().inject(this);
    }

    MutableLiveData<Boolean> isContactChecked = new MutableLiveData<>();


    public LiveData<Boolean> getIsContactChecked(){
        return isContactChecked;
    }

    public void start() {
        repository.getCheckedList(new CheckedContact.CheckedContactCallback() {
            @Override
            public void onContactsLoaded(boolean resultCheck) {
                isContactChecked.postValue(resultCheck);
            }
        });
    }
}
