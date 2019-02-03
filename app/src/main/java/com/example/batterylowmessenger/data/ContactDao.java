package com.example.batterylowmessenger.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ContactDao {

    /**сохранение в базу данных.Если такой контакт в базе данных уже есть,он будет заменен
     * вставляемым */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Contact contact);

    @Query("SELECT * FROM contact")
    List<Contact> loadAll();

    /**возвращает из базы данных список отмеченных контактов */
    @Query("SELECT * FROM contact WHERE isChecked = 1")
    List<Contact> getCheckedList();

    @Query("SELECT * FROM contact WHERE isChecked = 1")
    LiveData<List<Contact>> getContacts();

    @Query("DELETE FROM contact")
    void clearTable();

}