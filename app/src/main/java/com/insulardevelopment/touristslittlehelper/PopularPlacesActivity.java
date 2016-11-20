package com.insulardevelopment.touristslittlehelper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class PopularPlacesActivity extends AppCompatActivity {

    private ArrayList<PlaceEntity> places = new ArrayList<>();
    private PlaceAdapter placeAdapter;

    public static void start(Context context){
        Intent intent = new Intent(context, PopularPlacesActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_places);

        places.add(new PlaceEntity("Пантеон", "Piazza della Rotonda, 00186 Roma, Италия"));
        places.add(new PlaceEntity("Колизей", "Piazza del Colosseo, 1, 00184 Roma, Италия"));
        placeAdapter = new PlaceAdapter(this, places);

        ListView popularPlacesListView = (ListView) findViewById(R.id.popular_places_list_view);
        popularPlacesListView.setAdapter(placeAdapter);
    }
}