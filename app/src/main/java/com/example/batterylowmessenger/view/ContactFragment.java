package com.example.batterylowmessenger.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.batterylowmessenger.MainActivity;
import com.example.batterylowmessenger.ModelFactory;
import com.example.batterylowmessenger.OnBackPressed;
import com.example.batterylowmessenger.adapters.ContactsAdapter;
import com.example.batterylowmessenger.data.Contact;
import com.example.batterylowmessenger.databinding.ContactFragmentBinding;
import com.example.batterylowmessenger.services.ApplicationService;
import com.example.batterylowmessenger.sharedPreferenceStorage.ApplicationSharedPreference;
import com.example.batterylowmessenger.viewModels.ContactFragmentViewModel;
import com.example.batterylowmessenger.viewModels.InteractionViewModel;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class ContactFragment extends Fragment implements OnBackPressed {

    private static final String EDIT_OR_CHANGE_IDENT = "editOrChangeIdent";

    private InteractionViewModel interactionViewModel;
    private ContactFragmentViewModel contactFragmentViewModel;
    private ContactFragmentBinding contactFragmentBinding;
    private ContactsAdapter adapter;
    private boolean editOrChangeContacts;
    private boolean contactsIsChecked;

    public ContactFragment() {}

    public static ContactFragment newInstance(boolean value){
        Bundle args = new Bundle();
        args.putBoolean(EDIT_OR_CHANGE_IDENT,value);
        ContactFragment contactFragment = new ContactFragment();
        contactFragment.setArguments(args);
        return contactFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            editOrChangeContacts = getArguments().getBoolean(EDIT_OR_CHANGE_IDENT);
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        contactFragmentBinding = ContactFragmentBinding.inflate(inflater,container,false);
        interactionViewModel = MainActivity.obtainViewModel(getActivity());
        contactFragmentViewModel = setupViewModel();
        observeBatteryFragmentViewModel(contactFragmentViewModel);
        contactFragmentBinding.setViewmodel(contactFragmentViewModel);
        contactFragmentBinding.setContactFragment(this);
        return contactFragmentBinding.getRoot();

    }

    private void observeBatteryFragmentViewModel(ContactFragmentViewModel viewModel){
        viewModel.getIsContactChecked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean!=null) {
                    if (!aBoolean) {
                        contactsIsChecked = false;
                    }
                    if (aBoolean) {
                        contactsIsChecked = true;
                    }
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupListAdapter();
    }

    private ContactFragmentViewModel setupViewModel(){
        return ViewModelProviders.of(this, new ModelFactory(editOrChangeContacts,
                getActivity().getApplication()))
                .get(ContactFragmentViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        contactFragmentViewModel.start();
    }

    private void setupListAdapter() {
        ListView listView = contactFragmentBinding.contactsList;


        adapter = new ContactsAdapter(
                new ArrayList<Contact>(0),
                contactFragmentViewModel
        );
        listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        interactionViewModel.select("openHomeFragment");
    }

    public void backToHomeFragment(View view){
        interactionViewModel.select("openHomeFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        prepareToStartService();
    }

    public void prepareToStartService(){

        if(contactsIsChecked&& ApplicationSharedPreference.getStoredMessage(getContext())!=null&&
                ApplicationSharedPreference.getStoredMessage(getContext()).length()>0) {

            Intent intent = new Intent(getContext(), ApplicationService.class);
            intent.putExtra(ApplicationService.HANDLE_REBOOT, true);
            getActivity().startService(intent);
        }
    }

}
