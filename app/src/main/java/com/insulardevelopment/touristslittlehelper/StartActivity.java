package com.insulardevelopment.touristslittlehelper;

import android.content.Context;
import android.content.SharedPreferences;
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
import java.util.Set;
import java.util.StringTokenizer;

/*
*   Активити для выбора необходимых типов мест при первом запуске приложения
*/

public class StartActivity extends AppCompatActivity {

    private Button confirmBtn;
    private SharedPreferences sp;
    public static final String CHOSEN_TYPES = "types";
    public static final String APP_PREFERENCES = "preferences";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        sp = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.places_types_recycler_view);
        final List<PlacesTypes> placesTypes = new ArrayList<>();

        placesTypes.add(0, new PlacesTypes("museum", "Музей", false));
        placesTypes.add(0, new PlacesTypes("theatre", "Театр", false));
        placesTypes.add(0, new PlacesTypes("art_gallery", "Галерея", false));
        placesTypes.add(0, new PlacesTypes("park", "Парк", false));
        placesTypes.add(0, new PlacesTypes("casino", "Казино", false));
        placesTypes.add(0, new PlacesTypes("church", "Собор", false));


        final PlacesTypesAdapter placesTypesAdapter = new PlacesTypesAdapter(this, placesTypes);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(placesTypesAdapter);

        confirmBtn = (Button) findViewById(R.id.start_confirm_btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Set<String> types = placesTypesAdapter.getChecked();
                SharedPreferences.Editor edit = sp.edit();
                edit.clear();
                edit.putStringSet(CHOSEN_TYPES, types);
                edit.commit();
                MainActivity.start(StartActivity.this);
            }
        });
    }
}
