package com.example.batterylowmessenger;


import java.util.List;

public interface CheckedContact {

    interface CheckedContactCallback {

        void onContactsLoaded(boolean resultCheck);
    }
}
