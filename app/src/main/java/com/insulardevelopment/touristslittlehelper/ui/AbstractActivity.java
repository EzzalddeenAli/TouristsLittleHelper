package com.insulardevelopment.touristslittlehelper.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by Маргарита on 06.11.2017.
 */

public abstract class AbstractActivity extends DaggerAppCompatActivity {
    @Inject
    protected ViewModelFactory viewModelFactory;

    protected <T extends ViewModel> T getViewModel(Class<T> aClass) {
        return ViewModelProviders.of(this, viewModelFactory)
                .get(aClass);
    }
}
