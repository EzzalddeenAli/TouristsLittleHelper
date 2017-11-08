package com.insulardevelopment.touristslittlehelper.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;

import com.insulardevelopment.touristslittlehelper.ui.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * Created by Маргарита on 07.11.2017.
 */

public abstract class AbstractFragment extends DaggerFragment {

    @Inject
    protected ViewModelFactory viewModelFactory;

    protected <T extends ViewModel> T getViewModel(Class<T> aClass) {
        return ViewModelProviders.of(this, viewModelFactory)
                .get(aClass);
    }

    protected <T extends ViewModel> T getActivityViewModel(Class<T> aClass) {
        return ViewModelProviders.of(getActivity(), viewModelFactory)
                .get(aClass);
    }
}
