package com.example.batterylowmessenger;

import android.databinding.BindingAdapter;
import android.widget.ListView;

import com.example.batterylowmessenger.adapters.ContactsAdapter;
import com.example.batterylowmessenger.data.Contact;

import java.util.List;



public class ContactsListBindings {
    @SuppressWarnings("unchecked")
    @BindingAdapter({"app:items"})
    public static void setItems(ListView listView, List<Contact> items) {
        ContactsAdapter adapter = (ContactsAdapter) listView.getAdapter();
        if (adapter != null)
        {
            adapter.replaceData(items);
        }
    }
}
