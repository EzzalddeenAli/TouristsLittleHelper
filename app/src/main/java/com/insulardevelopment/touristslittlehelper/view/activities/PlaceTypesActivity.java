package com.insulardevelopment.touristslittlehelper.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.PlaceType;
import com.insulardevelopment.touristslittlehelper.view.AbstractActivity;
import com.insulardevelopment.touristslittlehelper.view.adapters.PlaceTypeAdapter;
import com.insulardevelopment.touristslittlehelper.view.viewmodel.PlaceTypesViewModel;

import java.util.List;

/**
 * Активити для изменения типов мест
 */

public class PlaceTypesActivity extends AbstractActivity {

    private PlaceTypesViewModel placeTypesViewModel;

    private RecyclerView recyclerView;
    private PlaceTypeAdapter placeTypeAdapter;

    public static void start(Context context){
        Intent intent = new Intent(context, PlaceTypesActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_types);

        placeTypesViewModel = getViewModel(PlaceTypesViewModel.class);

        setTitle(getResources().getString(R.string.types));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.type_menu_recycler);

        placeTypesViewModel.getSavedPlaceTypes().observe(this, placeTypes -> setupRecycler(placeTypes));
    }

    private void setupRecycler(List<PlaceType> placeTypes){
        placeTypeAdapter = new PlaceTypeAdapter(this, placeTypes);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(placeTypeAdapter);
    }

    private void saveTypes(){
        placeTypesViewModel.updatePlaceTypes(placeTypeAdapter.getPlaceTypeList());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveTypes();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        saveTypes();
        super.onBackPressed();
    }
}
