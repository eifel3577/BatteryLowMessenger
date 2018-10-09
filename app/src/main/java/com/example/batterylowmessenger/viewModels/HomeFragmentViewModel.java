package com.example.batterylowmessenger.viewModels;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.example.batterylowmessenger.CheckedContact;
import com.example.batterylowmessenger.repository.ContactsRepository;

public class HomeFragmentViewModel extends AndroidViewModel {

    private ContactsRepository repository;
    MutableLiveData<Boolean> isContactChecked = new MutableLiveData<>();
    //public final ObservableBoolean visibleContacts = new ObservableBoolean(false);
    //public final ObservableBoolean visibleTextSms = new ObservableBoolean(true);


    public HomeFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new ContactsRepository(this.getApplication());
    }

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
