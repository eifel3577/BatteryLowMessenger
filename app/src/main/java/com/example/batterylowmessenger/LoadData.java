package com.example.batterylowmessenger;


import com.example.batterylowmessenger.data.Contact;

import java.util.List;

public interface LoadData {

    interface LoadContactCallback {

        void onContactsLoaded(List<Contact> tasks);

        void onDataNotAvailable();
    }
}
