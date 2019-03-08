package com.example.batterylowmessenger;


import java.util.List;

//интерфейс коллбек для загрузки отмеченных контактов из БД
public interface CheckedContact {

    interface CheckedContactCallback {

        void onContactsLoaded(boolean resultCheck);
    }
}
