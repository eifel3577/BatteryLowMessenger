package com.example.batterylowmessenger.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.batterylowmessenger.App;
import com.example.batterylowmessenger.MainActivity;
import com.example.batterylowmessenger.OnBackPressed;
import com.example.batterylowmessenger.databinding.HomeFragmentBinding;
import com.example.batterylowmessenger.sharedPreferenceStorage.ApplicationSharedPreference;
import com.example.batterylowmessenger.viewModels.HomeFragmentViewModel;
import com.example.batterylowmessenger.viewModels.InteractionViewModel;

import javax.inject.Inject;


public class HomeFragment extends Fragment implements OnBackPressed {

    /**получение контекста из даггера */
    @Inject
    Context context;

    /**ViewModel для работы фрагмента с репозиторием */
    private HomeFragmentViewModel viewModel;
    /**биндинг для работы фрагмента с xml */
    private HomeFragmentBinding binding;
    /**навигационная ViewModel для работы с навигацией внутри приложения */
    private InteractionViewModel interactionViewModel;

    /**инициализация даггера */
    public HomeFragment() {
        App.getAppComponent().inject(this);
    }

    /**геттер для возвращения фрагмента */
    public static HomeFragment newInstance(){
        return new HomeFragment();
    }

    /**инициализация биндинга,ViewModel-ей, получение от ViewModel инфо есть ли выбранные контакты,если есть
     * то передача в навигационную ViewModel соответствующего стрингового идента*/
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
                    if(ApplicationSharedPreference.getStoredMessage(context)!=null && ApplicationSharedPreference.getStoredMessage(context).length()>0){
                        interactionViewModel.select("openInfoFragment");
                    }
                }
            }
        });

        return binding.getRoot();
    }

    /**создание ViewModel для работы c фрагментом */
    private HomeFragmentViewModel setupViewModel(){
        return ViewModelProviders.of(this).get(HomeFragmentViewModel.class);
    }


    /**вызывается при нажатии кнопки Выберите контакты для СМС
     * отправляет в навигационную ViewModel стринговый идент openContactList*/
    public void toContactList(View view){
        interactionViewModel.select("openContactList");
    }

    /**вызывается при нажатии кнопки Выберите текст для СМС
     * отправляет в навигационную ViewModel стринговый идент openMessageFragment*/
    public void toMessage(View view){
        interactionViewModel.select("openMessageFragment");
    }

    /**стартует ViewModel при открытии фрагмента */
    @Override
    public void onResume() {
        super.onResume();
        viewModel.start();
    }

    @Override
    public void onBackPressed() {}
}
