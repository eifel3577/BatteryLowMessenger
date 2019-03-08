package com.example.batterylowmessenger;


import com.example.batterylowmessenger.data.Contact;

import java.util.List;

//интерфейс колбек для возврата контактов из БД
public interface LoadData {

    interface LoadContactCallback {
        //БД вернула список контактов
        void onContactsLoaded(List<Contact> tasks);
        //в БД 
        void onDataNotAvailable();
    }
}
