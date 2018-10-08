package com.example.batterylowmessenger.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.batterylowmessenger.MainActivity;
import com.example.batterylowmessenger.databinding.UserRulesBinding;
import com.example.batterylowmessenger.viewModels.InteractionViewModel;

public class UserRulesFragment extends Fragment {

    private InteractionViewModel interactionViewModel;
    private UserRulesBinding binding;

    public static UserRulesFragment newInstance(){
        return new UserRulesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding =UserRulesBinding.inflate(inflater,container,false);
        interactionViewModel = MainActivity.obtainViewModel(getActivity());
        binding.setUserRulesFragment(this);
        return binding.getRoot();
    }

    public void backToHomeFragment(View view){
        interactionViewModel.select("openHomeFragment");
    }
}
