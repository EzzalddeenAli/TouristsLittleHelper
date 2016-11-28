package com.insulardevelopment.touristslittlehelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.insulardevelopment.touristslittlehelper.placetype.PlacesTypes;
import com.insulardevelopment.touristslittlehelper.placetype.PlacesTypesAdapter;

import java.util.ArrayList;
import java.util.List;

/*
*   Активити для выбора необходимых типов мест при первом запуске приложения
*/

public class StartActivity extends AppCompatActivity {

    private Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.places_types_recycler_view);
        final List<PlacesTypes> placesTypes = new ArrayList<>();

        placesTypes.add(0, new PlacesTypes("Museum"));
        placesTypes.add(0, new PlacesTypes("Theatre"));
        placesTypes.add(0, new PlacesTypes("Art Gallery"));
        placesTypes.add(0, new PlacesTypes("Park"));
        placesTypes.add(0, new PlacesTypes("Casino"));
        placesTypes.add(0, new PlacesTypes("Zoo"));

        final PlacesTypesAdapter placesTypesAdapter = new PlacesTypesAdapter(placesTypes);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(placesTypesAdapter);

        confirmBtn = (Button) findViewById(R.id.start_confirm_btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean[] checked = placesTypesAdapter.getChecked();
                MainActivity.start(StartActivity.this);
            }
        });
    }
}
