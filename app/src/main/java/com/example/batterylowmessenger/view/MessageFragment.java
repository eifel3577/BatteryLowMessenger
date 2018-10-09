package com.example.batterylowmessenger.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.batterylowmessenger.App;
import com.example.batterylowmessenger.MainActivity;
import com.example.batterylowmessenger.databinding.MessageFragmentBinding;
import com.example.batterylowmessenger.services.ApplicationService;
import com.example.batterylowmessenger.sharedPreferenceStorage.ApplicationSharedPreference;
import com.example.batterylowmessenger.viewModels.InteractionViewModel;
import com.example.batterylowmessenger.viewModels.MessageFragmentViewModel;

import javax.inject.Inject;

public class MessageFragment extends Fragment {

    @Inject
    Context context;

    private InteractionViewModel interactionViewModel;
    private MessageFragmentViewModel messageFragmentViewModel;
    private MessageFragmentBinding messageFragmentBinding;
    private boolean contactsIsChecked;
    private boolean handleOnPauseMethod=false;

    public MessageFragment() {
        App.getAppComponent().inject(this);
    }

    public static MessageFragment newInstance(){
        return new MessageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        messageFragmentBinding = MessageFragmentBinding.inflate(inflater,container,false);
        interactionViewModel = MainActivity.obtainViewModel(getActivity());
        messageFragmentViewModel = setupViewModel();
        messageFragmentViewModel.checkContactList();
        messageFragmentBinding.setViewmodel(messageFragmentViewModel);
        messageFragmentBinding.setMessageFragment(this);
        observeMessageFragmentViewModel(messageFragmentViewModel);
        return messageFragmentBinding.getRoot();
    }

    private MessageFragmentViewModel setupViewModel(){
        return ViewModelProviders.of(this).get(MessageFragmentViewModel.class);
    }

    private void observeMessageFragmentViewModel(MessageFragmentViewModel viewModel){
        viewModel.getTextMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                ApplicationSharedPreference.setStoredMessage(context,s);

            }
        });
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

    public void backToHomeFragment(View view){
        handleOnPauseMethod = true;
        prepareToStartService();
        onPause();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(!handleOnPauseMethod){
            interactionViewModel.select("openMessageFragment");
        }
        if(handleOnPauseMethod){
            interactionViewModel.select("openHomeFragment");
        }
    }

    public void prepareToStartService(){
        if(contactsIsChecked&&ApplicationSharedPreference.getStoredMessage(context)!=null&&
                ApplicationSharedPreference.getStoredMessage(context).length()>0) {

            Intent intent = new Intent(context, ApplicationService.class);
            intent.putExtra(ApplicationService.HANDLE_REBOOT, true);
            getActivity().startService(intent);
        }
    }


}
