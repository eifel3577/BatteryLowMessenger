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
import android.widget.Toast;

import com.example.batterylowmessenger.MainActivity;
import com.example.batterylowmessenger.databinding.MessageFragmentBinding;
import com.example.batterylowmessenger.services.ApplicationService;
import com.example.batterylowmessenger.sharedPreferenceStorage.ApplicationSharedPreference;
import com.example.batterylowmessenger.viewModels.InteractionViewModel;
import com.example.batterylowmessenger.viewModels.MessageFragmentViewModel;

public class MessageFragment extends Fragment {

    private InteractionViewModel interactionViewModel;
    private MessageFragmentViewModel messageFragmentViewModel;
    private MessageFragmentBinding messageFragmentBinding;
    private boolean contactsIsChecked;

    public MessageFragment() {
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
                ApplicationSharedPreference.setStoredMessage(getContext(),s);

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
        interactionViewModel.select("openHomeFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        prepareToStartService();
    }

    public void prepareToStartService(){
        if(contactsIsChecked&&ApplicationSharedPreference.getStoredMessage(getContext())!=null&&
                ApplicationSharedPreference.getStoredMessage(getContext()).length()>0) {

            Intent intent = new Intent(getContext(), ApplicationService.class);
            intent.putExtra(ApplicationService.HANDLE_REBOOT, true);
            getActivity().startService(intent);
        }
    }


}
