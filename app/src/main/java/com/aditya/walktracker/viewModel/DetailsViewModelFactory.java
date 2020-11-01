package com.aditya.walktracker.viewModel;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

public class DetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private boolean mToSave;
    private Application mApplication;

    public DetailsViewModelFactory(Application application, boolean toSave){
        this.mToSave = toSave;
        this.mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailsViewModel(mApplication,mToSave);
    }
}
