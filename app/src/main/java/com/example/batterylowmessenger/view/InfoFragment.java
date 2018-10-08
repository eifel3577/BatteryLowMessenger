package com.example.batterylowmessenger.view;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.batterylowmessenger.MainActivity;
import com.example.batterylowmessenger.OnBackPressed;
import com.example.batterylowmessenger.databinding.InfoFragmentBinding;
import com.example.batterylowmessenger.viewModels.InfoFragmentViewModel;
import com.example.batterylowmessenger.viewModels.InteractionViewModel;

public class InfoFragment extends Fragment implements OnBackPressed {

    private InfoFragmentViewModel viewModel;
    private InfoFragmentBinding binding;
    private InteractionViewModel interactionViewModel;


    public static InfoFragment newInstance(){
        return new InfoFragment();
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = setupViewModel();
        interactionViewModel = MainActivity.obtainViewModel(getActivity());
        binding = InfoFragmentBinding.inflate(inflater,container,false);
        binding.setViewmodel(viewModel);
        binding.textViewSelectContacts.setMovementMethod(new ScrollingMovementMethod());
        binding.textViewSelectMessage.setMovementMethod(new ScrollingMovementMethod());
        binding.textViewSelectChargeLevel.setMovementMethod(new ScrollingMovementMethod());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.start();
    }

    private InfoFragmentViewModel setupViewModel(){
        return ViewModelProviders.of(this).get(InfoFragmentViewModel.class);
    }

    @Override
    public void onBackPressed() {}

}
