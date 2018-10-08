package com.example.batterylowmessenger.viewModels;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.batterylowmessenger.CheckedContact;
import com.example.batterylowmessenger.repository.ContactsRepository;

public class HomeFragmentViewModel extends ViewModel {

    private final ContactsRepository contactsRepository = new ContactsRepository();
    private ContactsRepository repository = new ContactsRepository();
    MutableLiveData<Boolean> isContactChecked = new MutableLiveData<>();
    //public final ObservableBoolean visibleContacts = new ObservableBoolean(false);
    //public final ObservableBoolean visibleTextSms = new ObservableBoolean(true);

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
