package com.insulardevelopment.touristslittlehelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.insulardevelopment.touristslittlehelper.placetype.PlacesTypes;
import com.insulardevelopment.touristslittlehelper.placetype.PlacesTypesAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlaceTypesActivity extends AppCompatActivity {

    private SharedPreferences sp;
    public static final String CHOSEN_TYPES = "types";
    public static final String APP_PREFERENCES = "preferences";

    public static void start(Context context){
        Intent intent = new Intent(context, PlaceTypesActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_types);
        sp = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.type_menu_recycler);
        Set types = sp.getStringSet(StartActivity.CHOSEN_TYPES, new HashSet<String>());
        final List<PlacesTypes> placesTypes = new ArrayList<>();

        placesTypes.add(new PlacesTypes("museum", "Музей", false));
        placesTypes.add(new PlacesTypes("art_gallery", "Галерея", false));
        placesTypes.add(new PlacesTypes("park", "Парк", false));
        placesTypes.add(new PlacesTypes("casino", "Казино", false));
        placesTypes.add(new PlacesTypes("church", "Собор", false));
        placesTypes.add(new PlacesTypes("amusement_park", "Парк развлечений", false));
        placesTypes.add(new PlacesTypes("zoo", "Зоопарк", false));
        placesTypes.add(new PlacesTypes("stadium", "Стадион", false));
        placesTypes.add(new PlacesTypes("aquarium", "Океанариум", false));



        final PlacesTypesAdapter placesTypesAdapter = new PlacesTypesAdapter(this, placesTypes);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(placesTypesAdapter);
    }
}
