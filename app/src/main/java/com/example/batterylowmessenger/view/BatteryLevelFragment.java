package com.example.batterylowmessenger.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.batterylowmessenger.App;
import com.example.batterylowmessenger.MainActivity;
import com.example.batterylowmessenger.OnBackPressed;
import com.example.batterylowmessenger.R;
import com.example.batterylowmessenger.databinding.BatteryLevelFragmentBinding;
import com.example.batterylowmessenger.services.ApplicationService;
import com.example.batterylowmessenger.sharedPreferenceStorage.ApplicationSharedPreference;
import com.example.batterylowmessenger.viewModels.BatteryLevelFragmentViewModel;
import com.example.batterylowmessenger.viewModels.InteractionViewModel;

import javax.inject.Inject;

public class BatteryLevelFragment extends Fragment implements OnBackPressed {

    @Inject
    Context context;

    private InteractionViewModel interactionViewModel;
    private BatteryLevelFragmentBinding binding;
    private BatteryLevelFragmentViewModel viewModel;
    private boolean contactsIsChecked;


    public BatteryLevelFragment() {
        App.getAppComponent().inject(this);
    }

    public static BatteryLevelFragment newInstance(){
        return new BatteryLevelFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BatteryLevelFragmentBinding.inflate(inflater,container,false);
        interactionViewModel = MainActivity.obtainViewModel(getActivity());
        viewModel = setupViewModel();
        viewModel.checkContactList();
        binding.setViewmodel(viewModel);
        binding.setBatteryLevelFragment(this);
        observeBatteryFragmentViewModel(viewModel);

        return binding.getRoot();
    }

    private void observeBatteryFragmentViewModel(BatteryLevelFragmentViewModel viewModel){
        viewModel.getBatteryLevel().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                ApplicationSharedPreference.setStoredBatteryLevel(getContext(),s);
            }
        });

        viewModel.getIsDataCorrect().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null) {
                    if (!aBoolean) Toast.makeText(context,context.getResources().getString(R.string.enter_correct_value), Toast.LENGTH_SHORT).show();
                }
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



    private BatteryLevelFragmentViewModel setupViewModel(){
        return ViewModelProviders.of(this).get(BatteryLevelFragmentViewModel.class);
    }

    public void backToHomeFragment(View view){
        prepareToStartService();
        interactionViewModel.select("openHomeFragment");
    }

    @Override
    public void onBackPressed() {
        prepareToStartService();
        interactionViewModel.select("openHomeFragment");
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