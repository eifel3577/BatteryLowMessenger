package com.example.batterylowmessenger.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.batterylowmessenger.MainActivity;
import com.example.batterylowmessenger.OnBackPressed;
import com.example.batterylowmessenger.databinding.HomeFragmentBinding;
import com.example.batterylowmessenger.sharedPreferenceStorage.ApplicationSharedPreference;
import com.example.batterylowmessenger.viewModels.HomeFragmentViewModel;
import com.example.batterylowmessenger.viewModels.InteractionViewModel;

public class HomeFragment extends Fragment implements OnBackPressed {

    private HomeFragmentViewModel viewModel;
    private HomeFragmentBinding binding;
    private InteractionViewModel interactionViewModel;


    public static HomeFragment newInstance(){
        return new HomeFragment();
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = HomeFragmentBinding.inflate(inflater,container,false);
        viewModel = setupViewModel();
        interactionViewModel = MainActivity.obtainViewModel(getActivity());
        binding.setHomeFragment(this);

        viewModel.getIsContactChecked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean!=null&&!aBoolean){
                    if(ApplicationSharedPreference.getStoredMessage(getContext())!=null && ApplicationSharedPreference.getStoredMessage(getContext()).length()>0){
                        interactionViewModel.select("openInfoFragment");
                    }
                }
            }
        });

        return binding.getRoot();
    }

    private HomeFragmentViewModel setupViewModel(){
        return ViewModelProviders.of(this).get(HomeFragmentViewModel.class);
    }

    public void toContactList(View view){
        interactionViewModel.select("openContactList");
    }
    public void toMessage(View view){
        interactionViewModel.select("openMessageFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.start();
    }

    @Override
    public void onBackPressed() {}
}
