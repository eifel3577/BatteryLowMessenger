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

//создается в единственном экземпляре
@Singleton
public class ContactsRepository {
    
    // с помощью даггера получение экзекьютора для работы в отдельных потоках
    @Inject
    Executor executor;
    // с помощью даггера получение контекста
    @Inject
    Context context;
    //ссылка на БД
    private MyDatabase database;
    //ссылка на интерфейс работы с БД
    private ContactDao contactDao;


    //будет предоставляться как зависимость в даггер
    @Inject
    public ContactsRepository() {
        //просит даггер предоставить нужные зависимости для данного репозитория
        App.getAppComponent().inject(this);
        //инициализация БД
        database = App.getInstance().getDatabase();
        //инициализация интерфейса работы с БД
        contactDao = database.contactDao();
    }

    /**метод загружающий список контактов
     * @param fromDataBase - грузить данные из сети или из базы данных
     * @param callback - колбэк на ViewModel,куда будет идти результат*/
    public void getContact(final boolean fromDataBase, @NonNull final LoadData.LoadContactCallback callback){
        //экзекьютор выполняет код в отдельном потоке
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if(!fromDataBase) {
                    //получает все контакты из БД
                    List<Contact> contactList = contactDao.loadAll();
                    //если БД вернула список хотя бы с одним контактом
                    if (contactList.size() > 0) {
                        //то список отдается в колбек и метод заканчивается
                        callback.onContactsLoaded(contactList);
                        return;
                    }
                }
                
                    //ContentResolver это обьект поставщика контента.Поставщик контента управляет доступом к центральному
                    //репозиторию данных ОС телефона (контакты,смс,другие пользовательские данные).Поставщик контента предоставляет
                    //данные внешним приложениям
                    //в виде одной или нескольких таблиц, аналогичных таблицам в реляционной базе данных. 
                    ContentResolver contentResolver = context.getContentResolver();
                    //получает список контактов телефона в алфавитном порядке
                    Cursor cursorContentResolver = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

                    try {
                        //если в телефоне есть хоть один контакт
                        if (cursorContentResolver.getCount() > 0) {
                            //проходит по контактам и на каждом шаге    
                            while (cursorContentResolver.moveToNext()) {
                                //создает обьект Contact
                                final Contact contact = new Contact();
                                //получает от поставщика контента номер телефона контакта
                                int hasPhoneNumber = Integer.parseInt(cursorContentResolver.getString(cursorContentResolver.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                                //если у контакта есть телефонный номер
                                if (hasPhoneNumber > 0) {
                                    //получает ИД контакта и присваивает обьекту
                                    String id = cursorContentResolver.getString(cursorContentResolver.getColumnIndex(ContactsContract.Contacts._ID));
                                    contact.setContactID(id);
                                    //получает имя контакта и присваивает обьекту
                                    String name = cursorContentResolver.getString(cursorContentResolver.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                    contact.setContactName(name);

                                    
                                    Cursor phoneCursor = contentResolver.query(
                                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                            null,
                                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                            new String[]{id},
                                            null);
                                    //получает номер телефона
                                    if (phoneCursor.moveToNext()) {
                                        String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                        contact.setContactNumber(phone);
                                    }
                                    //ставит контакт как неотмеченный
                                    contact.setChecked(false);
                                    //работа с БД должна производится в отдельном потоке
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            //сохраняет контакт в БД
                                            contactDao.save(contact);
                                        }
                                    });
                                    //закрывает курсор
                                    phoneCursor.close();
                                }
                            }
                        }
                    } finally {
                        //после всего предыдущего в блоке try в любом случае
                        if (cursorContentResolver != null) {
                            //закрывает поставщик контента
                            cursorContentResolver.close();
                            executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    //вытягивает список контактов из БД в колбек
                                    callback.onContactsLoaded(contactDao.loadAll());
                                    //очищает БД
                                    contactDao.clearTable();
                                }
                            });
                        }
                    }

            }
        });
    }

    /**метод сохраняющий контакт в базу данных
     * @param contact - контакт который будет записываться в базу данных
     * @param checked - отмеченный ли этот контакт
     * @param contactCallback - колбэк возвращающий флаг есть ли в базе данных отмеченные контакты  */
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

    /**метод проверяет есть ли в списке контактов,хранящемся в базе данных,отмеченные контакты
     * @param contactCallback - колбэк куда пойдет булев есть ли отмеченные контакты */
    public void getCheckedList(@NonNull final CheckedContact.CheckedContactCallback contactCallback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                contactCallback.onContactsLoaded(contactDao.getCheckedList().isEmpty());
            }
        });
    }


    /**
     * метод возвращающий из базы список отмеченных контактов
     * @param contactCallback - колбек куда пойдет список отмеченных контактов
     */
    public void getCheckedContactList(@NonNull final LoadData.LoadContactCallback contactCallback){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                contactCallback.onContactsLoaded(contactDao.getCheckedList());
            }
        });
    }


}
