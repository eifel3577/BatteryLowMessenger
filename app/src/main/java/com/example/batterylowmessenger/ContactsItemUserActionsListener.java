package com.example.batterylowmessenger;


import android.view.View;

import com.example.batterylowmessenger.data.Contact;

public interface ContactsItemUserActionsListener {

    void onCompleteChanged(Contact contact, View v);
}
