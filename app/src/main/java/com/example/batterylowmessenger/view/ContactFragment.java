package com.example.batterylowmessenger.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.batterylowmessenger.App;
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

import javax.inject.Inject;

import static java.security.AccessController.getContext;

/**фрагмент выбора контактов */
public class ContactFragment extends Fragment implements OnBackPressed {

    /**получение контекста из даггера */
    @Inject
    Context context;


    private static final String EDIT_OR_CHANGE_IDENT = "editOrChangeIdent";

    /**навигационная ViewModel для работы с навигацией внутри приложения */
    private InteractionViewModel interactionViewModel;

    /**ViewModel для работы фрагмента с репозиторием */
    private ContactFragmentViewModel contactFragmentViewModel;
    /**биндинг для работы фрагмента с xml */
    private ContactFragmentBinding contactFragmentBinding;
    /**адаптер для вывода списка контактов */
    private ContactsAdapter adapter;
    private boolean editOrChangeContacts;
    private boolean contactsIsChecked;


    /**инициализация даггера */
    public ContactFragment() {
        App.getAppComponent().inject(this);
    }

    /**инициализирует фрагмент с учетом переданного флага value */
    public static ContactFragment newInstance(boolean value){
        Bundle args = new Bundle();
        args.putBoolean(EDIT_OR_CHANGE_IDENT,value);
        ContactFragment contactFragment = new ContactFragment();
        contactFragment.setArguments(args);
        return contactFragment;
    }

    /**если  */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            editOrChangeContacts = getArguments().getBoolean(EDIT_OR_CHANGE_IDENT);
        }
    }

    /**инициализация биндинга,навигационной ViewModel,ViewModel для работы фрагмента с репозиторием,
     *  */
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

    /**фабрика создает ViewModel для работы фрагмента с репозиторием с учетом флага editOrChangeContact*/
    private ContactFragmentViewModel setupViewModel(){
        return ViewModelProviders.of(this, new ModelFactory(editOrChangeContacts))
                .get(ContactFragmentViewModel.class);
    }

    /**при каждом начале работі фрагмента стартует работа ViewModel для работы фрагмента с репозиторием */
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
        prepareToStartService();
        interactionViewModel.select("openHomeFragment");
    }

    public void backToHomeFragment(View view){
        prepareToStartService();
        interactionViewModel.select("openHomeFragment");
    }



    public void prepareToStartService(){

        if(contactsIsChecked&& ApplicationSharedPreference.getStoredMessage(context)!=null&&
                ApplicationSharedPreference.getStoredMessage(context).length()>0) {

            Intent intent = new Intent(context, ApplicationService.class);
            intent.putExtra(ApplicationService.HANDLE_REBOOT, true);
            getActivity().startService(intent);
        }
    }

}
