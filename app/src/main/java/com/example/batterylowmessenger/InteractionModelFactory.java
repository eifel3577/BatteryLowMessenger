package com.example.batterylowmessenger;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.example.batterylowmessenger.viewModels.InteractionViewModel;

public class InteractionModelFactory extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile InteractionModelFactory INSTANCE;


    public static InteractionModelFactory getInstance() {

        if (INSTANCE == null) {
            synchronized (InteractionModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new InteractionModelFactory();
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private InteractionModelFactory() {

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(InteractionViewModel.class)) {
            //noinspection unchecked
            return (T) new InteractionViewModel();
        }
        return null;
    }


}
