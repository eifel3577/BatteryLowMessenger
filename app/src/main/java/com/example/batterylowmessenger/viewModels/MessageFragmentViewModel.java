package com.example.batterylowmessenger.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.batterylowmessenger.CheckedContact;
import com.example.batterylowmessenger.repository.ContactsRepository;

public class MessageFragmentViewModel extends AndroidViewModel {

    public final ObservableBoolean madeSaveButtonEnabled = new ObservableBoolean(false);
    MutableLiveData<String>textMessage =new MutableLiveData<>();
    private ContactsRepository contactsRepository;
    private MutableLiveData<Boolean>isContactChecked = new MutableLiveData<>();

    public MessageFragmentViewModel(@NonNull Application application) {
        super(application);
        contactsRepository = new ContactsRepository(this.getApplication());
    }

    public MutableLiveData<String> getTextMessage(){
        return textMessage;
    }

    public MutableLiveData<Boolean> getIsContactChecked() {
        return isContactChecked;
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        madeSaveButtonEnabled.set(true);
        if(s.length()<1) madeSaveButtonEnabled.set(false);
        else textMessage.postValue(s.toString());
    }

    public void checkContactList(){
        contactsRepository.getCheckedList(new CheckedContact.CheckedContactCallback() {
            @Override
            public void onContactsLoaded(boolean resultCheck) {
                if(resultCheck){
                    isContactChecked.postValue(false);
                }
                if(!resultCheck){
                    isContactChecked.postValue(true);
                }
            }
        });
    }

}