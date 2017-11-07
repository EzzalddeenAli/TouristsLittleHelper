package com.insulardevelopment.touristslittlehelper.view.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.insulardevelopment.touristslittlehelper.model.PlaceType;
import com.insulardevelopment.touristslittlehelper.view.AbstractActivity;
import com.insulardevelopment.touristslittlehelper.view.adapters.PlaceTypeAdapter;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.view.viewmodel.PlaceTypesViewModel;

import java.util.List;

/*
*   Активити для выбора необходимых типов мест при первом запуске приложения
*/

public class StartActivity extends AbstractActivity {

    public static final String FIRST_LAUNCHING = "first";
    public static final String APP_PREFERENCES = "preferences";

    private PlaceTypesViewModel placeTypesViewModel;

    private Button confirmBtn;
    private RecyclerView typesRecycler;
    private SharedPreferences sp;
    private PlaceTypeAdapter placeTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        placeTypesViewModel = getViewModel(PlaceTypesViewModel.class);

        initViews();
        sp = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean first = sp.getBoolean(FIRST_LAUNCHING, true);
        if (!first) {
            MainActivity.start(this);
        }

        setupRecycler(placeTypesViewModel.getPlaceTypes());

        confirmBtn.setOnClickListener(v -> {
            sp.edit()
              .putBoolean(FIRST_LAUNCHING, false)
              .apply();
            placeTypesViewModel.insertPlaceTypes();
            MainActivity.start(StartActivity.this);
        });
    }

    private void initViews() {
        confirmBtn = findViewById(R.id.start_confirm_btn);
        typesRecycler = findViewById(R.id.places_types_recycler_view);
    }

    private void setupRecycler(List<PlaceType> placeTypes){
        placeTypeAdapter = new PlaceTypeAdapter(this, placeTypes);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        typesRecycler.setLayoutManager(layoutManager);
        typesRecycler.setAdapter(placeTypeAdapter);
    }
}
