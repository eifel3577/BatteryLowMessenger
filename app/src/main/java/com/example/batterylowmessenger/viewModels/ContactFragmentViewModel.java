package com.example.batterylowmessenger.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import com.example.batterylowmessenger.CheckedContact;
import com.example.batterylowmessenger.LoadData;
import com.example.batterylowmessenger.data.Contact;
import com.example.batterylowmessenger.repository.ContactsRepository;

import java.util.List;

public class ContactFragmentViewModel extends ViewModel {

    public final ObservableBoolean empty = new ObservableBoolean(false);
    public final ObservableBoolean isCheckedContact = new ObservableBoolean(true);
    public final ObservableBoolean loadContactList = new ObservableBoolean(false);
    public final ObservableList<Contact> items = new ObservableArrayList<>();
    private ContactsRepository contactsRepository;
    private MutableLiveData<Boolean> isContactChecked = new MutableLiveData<>();
    private boolean fromRemote;

    public ContactFragmentViewModel(boolean id,@NonNull Application application) {
        fromRemote = id;
        contactsRepository = new ContactsRepository(application.getApplicationContext());
    }

    public MutableLiveData<Boolean> getIsContactChecked() {
        return isContactChecked;
    }

    public void start() {
        loadContactList.set(true);
        loadContacts(fromRemote);
        checkContactList();
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

    private void loadContacts(boolean id){
        contactsRepository.getContact(id, new LoadData.LoadContactCallback() {
            @Override
            public void onContactsLoaded(List<Contact> contacts) {

                items.clear();
                items.addAll(contacts);
                for(Contact contact:contacts){
                    if(contact.isChecked()){
                        isCheckedContact.set(false);
                        break;
                    }
                }
                empty.set(items.isEmpty());
                loadContactList.set(false);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    public void contactChecked(Contact contact,boolean checked){

        contactsRepository.putValueToDatabase(contact, checked, new CheckedContact.CheckedContactCallback() {
            @Override
            public void onContactsLoaded(boolean resultCheck) {
                isCheckedContact.set(resultCheck);
            }
        });

    }

}

