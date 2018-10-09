package com.example.batterylowmessenger.repository;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.example.batterylowmessenger.App;
import com.example.batterylowmessenger.CheckedContact;
import com.example.batterylowmessenger.LoadData;
import com.example.batterylowmessenger.data.Contact;
import com.example.batterylowmessenger.data.ContactDao;
import com.example.batterylowmessenger.data.MyDatabase;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ContactsRepository {

    @Inject
    Executor executor;
    @Inject
    Context context;
    private MyDatabase database;
    private ContactDao contactDao;


    @Inject
    public ContactsRepository() {
        App.getAppComponent().inject(this);
        database = App.getInstance().getDatabase();
        contactDao = database.contactDao();
    }

    public void getContact(final boolean fromDataBase, @NonNull final LoadData.LoadContactCallback callback){

        executor.execute(new Runnable() {
            @Override
            public void run() {
                if(!fromDataBase) {
                    List<Contact> contactList = contactDao.loadAll();
                    if (contactList.size() > 0) {
                        callback.onContactsLoaded(contactList);
                        return;
                    }
                }

                ContentResolver contentResolver = context.getContentResolver();
                Cursor cursorContentResolver = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

                try {
                    if (cursorContentResolver.getCount() > 0) {

                        while (cursorContentResolver.moveToNext()) {
                            final Contact contact = new Contact();
                            int hasPhoneNumber = Integer.parseInt(cursorContentResolver.getString(cursorContentResolver.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                            if (hasPhoneNumber > 0) {
                                String id = cursorContentResolver.getString(cursorContentResolver.getColumnIndex(ContactsContract.Contacts._ID));
                                contact.setContactID(id);

                                String name = cursorContentResolver.getString(cursorContentResolver.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                contact.setContactName(name);


                                Cursor phoneCursor = contentResolver.query(
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                        new String[]{id},
                                        null);

                                if (phoneCursor.moveToNext()) {
                                    String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                    contact.setContactNumber(phone);
                                }

                                contact.setChecked(false);

                                executor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        contactDao.save(contact);
                                    }
                                });

                                phoneCursor.close();
                            }
                        }
                    }
                }
                finally {
                    if (cursorContentResolver != null) {
                        cursorContentResolver.close();
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onContactsLoaded(contactDao.loadAll());
                            }
                        });
                    }
                }
            }
        });
    }

    public void putValueToDatabase(final Contact contact, final boolean checked,@NonNull final CheckedContact.CheckedContactCallback contactCallback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                contact.setChecked(checked);
                contactDao.save(contact);
                contactCallback.onContactsLoaded(contactDao.getCheckedList().isEmpty());
            }
        });
    }

    public void getCheckedList(@NonNull final CheckedContact.CheckedContactCallback contactCallback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                contactCallback.onContactsLoaded(contactDao.getCheckedList().isEmpty());
            }
        });
    }

    public void getCheckedContactList(@NonNull final LoadData.LoadContactCallback contactCallback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                contactCallback.onContactsLoaded(contactDao.getCheckedList());
            }
        });
    }


}
