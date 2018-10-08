package com.example.batterylowmessenger;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.batterylowmessenger.viewModels.ContactFragmentViewModel;

public class ModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final boolean id;

    public ModelFactory(boolean id) {
        super();
        this.id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == ContactFragmentViewModel.class) {
            return (T) new ContactFragmentViewModel(id);
        }
        return null;
    }
}