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

//фрагмент отображает выбранные контакты, смс и уровень батареи (если не выбран то 5 по умолчанию)
public class InfoFragment extends Fragment implements OnBackPressed {

    //ссылка на ViewModel
    private InfoFragmentViewModel viewModel;
    //ссылка на биндинг
    private InfoFragmentBinding binding;
    //ссылка на навигационную viewModel
    private InteractionViewModel interactionViewModel;

    //инициализирует фрагмент
    public static InfoFragment newInstance(){
        return new InfoFragment();
    }


    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //инициализирует ViewModel
        viewModel = setupViewModel();
        //инициализирует навигационную viewModel
        interactionViewModel = MainActivity.obtainViewModel(getActivity());
        //инициализирует биндинг
        binding = InfoFragmentBinding.inflate(inflater,container,false);
        //прикрепляет биндинг к viewModel
        binding.setViewmodel(viewModel);
        //textView будут скролится
        setScrollable();
        return binding.getRoot();
    }

    private void setScrollable() {
        binding.textViewSelectContacts.setMovementMethod(new ScrollingMovementMethod());
        binding.textViewSelectMessage.setMovementMethod(new ScrollingMovementMethod());
        binding.textViewSelectChargeLevel.setMovementMethod(new ScrollingMovementMethod());
    }


    @Override
    public void onResume() {
        super.onResume();
        //при появлении фрагмента на экране будет выполнятсяметод start() у viewModel
        viewModel.start();
    }

    private InfoFragmentViewModel setupViewModel(){
        return ViewModelProviders.of(this).get(InfoFragmentViewModel.class);
    }

    @Override
    public void onBackPressed() {}

}
