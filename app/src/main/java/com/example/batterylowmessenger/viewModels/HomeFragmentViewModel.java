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

//viewModel для работы с HomeFragment
public class HomeFragmentViewModel extends ViewModel {

    //получение репозитория ContactsRepository через даггер
    @Inject
    ContactsRepository repository;

    //просит даггер предоставить нужные зависимости для данного фрагмента
    public HomeFragmentViewModel() {
        App.getAppComponent().inject(this);
    }

    //создание трансляции boolean-значения, ечть ли отмеченные контакты
    MutableLiveData<Boolean> isContactChecked = new MutableLiveData<>();

    //возвращает трансляцию boolean-значения, ечть ли отмеченные контакты
    public LiveData<Boolean> getIsContactChecked(){
        return isContactChecked;
    }

    //вызывается когда фрагмент HomeFragment появляется на экране
    public void start() {
        //запрашивает репозиторий есть ли отмеченные контакты и отдает полученный булев в трансляцию isContactChecked
        repository.getCheckedList(new CheckedContact.CheckedContactCallback() {
            @Override
            public void onContactsLoaded(boolean resultCheck) {
                isContactChecked.postValue(resultCheck);
            }
        });
    }
}
