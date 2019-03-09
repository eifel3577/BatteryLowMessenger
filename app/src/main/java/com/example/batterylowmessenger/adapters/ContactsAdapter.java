package com.example.batterylowmessenger.adapters;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.batterylowmessenger.ContactsItemUserActionsListener;
import com.example.batterylowmessenger.data.Contact;
import com.example.batterylowmessenger.databinding.ContactItemBinding;
import com.example.batterylowmessenger.viewModels.ContactFragmentViewModel;

import java.util.List;
import java.util.Random;

//адаптер для отображения списка контактов
public class ContactsAdapter extends BaseAdapter {
    //ссылка на viewModel
    private final ContactFragmentViewModel contactsViewModel;
    //список контактов
    private List<Contact> contacts;

    public ContactsAdapter(List<Contact> contacts,
                           ContactFragmentViewModel contactsViewModel) {
        this.contactsViewModel = contactsViewModel;
        setList(contacts);
    }

    public void replaceData(List<Contact> contacts) {
        setList(contacts);
    }

    @Override
    public int getCount() {
        return contacts != null ? contacts.size() : 0;
    }

    @Override
    public Contact getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, final View view, final ViewGroup viewGroup) {

        ContactItemBinding binding;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

            binding = ContactItemBinding.inflate(inflater, viewGroup, false);
        }
        else {
            binding = DataBindingUtil.getBinding(view);
        }

        ContactsItemUserActionsListener userActionsListener = new ContactsItemUserActionsListener() {

            @Override
            public void onCompleteChanged(Contact contact, View v) {
                boolean checked = ((CheckBox)v).isChecked();
                //контакт в списке отмечен
                contactsViewModel.contactChecked(contact,checked);

            }
        };

        //создается рандомный цвет
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        //большая белая первая буква имени контакта в цветном круге рандомного цвета
        TextDrawable myDrawable = TextDrawable.builder().beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .toUpperCase()
                .endConfig()
                .buildRound(contacts.get(position).getContactName().substring(0,1),color);

        binding.toDoListItemColorImageView.setImageDrawable(myDrawable);

        binding.setContact(contacts.get(position));

        binding.setListener(userActionsListener);

        binding.executePendingBindings();
        return binding.getRoot();
    }

    //принимает список контактов,обновляет отображение
    private void setList(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }
}