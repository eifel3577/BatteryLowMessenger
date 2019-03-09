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

    /**true если репозиторий вернул пустой список */
    public final ObservableBoolean empty = new ObservableBoolean(false);
    /**false если в возвращенном от репозитория списке есть хотя бы один отмеченный контакт */
    public final ObservableBoolean isCheckedContact = new ObservableBoolean(true);
    /**false если загрузка из репозитория окончена,true если загрузка продолжается*/
    public final ObservableBoolean loadContactList = new ObservableBoolean(false);
    /**список контактов,которые вернул репозитория */
    public final ObservableList<Contact> items = new ObservableArrayList<>();
    /**true если в базе данных есть отмеченные контакты,false если нет */
    private MutableLiveData<Boolean> isContactCheckedInDatabase = new MutableLiveData<>();
    /**флаг грузить список контактов из сети или из базы данных */
    private boolean fromRemote;

    /**инициализация даггера,установка флага fromRemote */
    public ContactFragmentViewModel(boolean id) {
        App.getAppComponent().inject(this);
        fromRemote = id;
    }

    public MutableLiveData<Boolean> getIsContactChecked() {
        return isContactCheckedInDatabase;
    }

    /**при старте ViewModel ставит флаг "идет загрузка списка контактов" в true,загружает список
     * контактов из репозитория*/
    public void start() {
        loadContactList.set(true);
        loadContacts(fromRemote);
        checkContactList();
    }

    /**метод просит репозиторий дать ответ есть ли в базе данных отмеченные контакты */
    public void checkContactList(){
        contactsRepository.getCheckedList(new CheckedContact.CheckedContactCallback() {
            @Override
            public void onContactsLoaded(boolean resultCheck) {
                if(resultCheck){
                    isContactCheckedInDatabase.postValue(false);
                }
                if(!resultCheck){
                    isContactCheckedInDatabase.postValue(true);
                }
            }
        });
    }

    /**берет на вход флаг,передает его репозиторию и ввзывает на репозитории метод,загружающий
     * список контактов
     * @param id - флаг грузить данные из сети или из базы данных
     * загруженные контакты пишутся в items,если в списке есть отмеченные контакты,то флаг
     * isCheckedContact ставится в false*/
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

    /**вставляет в базу данных новый контакт,пишет в isCheckedContact есть ли в базе данных отмеченные
     * контакты */
    public void contactChecked(Contact contact,boolean checked){
        contactsRepository.putValueToDatabase(contact, checked, new CheckedContact.CheckedContactCallback() {
            @Override
            public void onContactsLoaded(boolean resultCheck) {
                isCheckedContact.set(resultCheck);
            }
        });

    }

}

