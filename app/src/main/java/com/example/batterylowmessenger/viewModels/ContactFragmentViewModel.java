package com.example.batterylowmessenger.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import com.example.batterylowmessenger.App;
import com.example.batterylowmessenger.CheckedContact;
import com.example.batterylowmessenger.LoadData;
import com.example.batterylowmessenger.data.Contact;
import com.example.batterylowmessenger.repository.ContactsRepository;

import java.util.List;

import javax.inject.Inject;

/**ViewModel для работы фрагмента ContactFragment с репозиторием*/
public class ContactFragmentViewModel extends ViewModel {

    /**получает зависимость ContactsRepository из даггера */
    @Inject
    ContactsRepository contactsRepository;

    private static final String TAG = "contactFragment";
    public final ObservableBoolean empty = new ObservableBoolean(false);
    public final ObservableBoolean isCheckedContact = new ObservableBoolean(true);
    public final ObservableBoolean loadContactList = new ObservableBoolean(false);
    public final ObservableList<Contact> items = new ObservableArrayList<>();
    private MutableLiveData<Boolean> isContactChecked = new MutableLiveData<>();
    private boolean fromRemote;

    /**инициализация даггера,установка флага fromRemote */
    public ContactFragmentViewModel(boolean id) {
        App.getAppComponent().inject(this);
        fromRemote = id;
    }

    public MutableLiveData<Boolean> getIsContactChecked() {
        return isContactChecked;
    }

    /**при старте ViewModel */
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
                for(Contact contact:contacts){
                    items.add(contact);
                }
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

