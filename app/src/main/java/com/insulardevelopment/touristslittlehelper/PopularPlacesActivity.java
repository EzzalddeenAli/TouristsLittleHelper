package com.insulardevelopment.touristslittlehelper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/*
*   Активити, содержащее список популярных мест
*/

public class PopularPlacesActivity extends AppCompatActivity {

    private ArrayList<Place> places = new ArrayList<>();
    private PlaceAdapter placeAdapter;
    private Button nextButton;

    public static void start(Context context){
        Intent intent = new Intent(context, PopularPlacesActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_places);

        places.add(new Place("Пантеон", "Piazza della Rotonda, 00186 Roma, Италия"));
        places.add(new Place("Колизей", "Piazza del Colosseo, 1, 00184 Roma, Италия"));
        placeAdapter = new PlaceAdapter(places, new PlaceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PlaceActivity.start(PopularPlacesActivity.this);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView popularPlacesRecyclerView = (RecyclerView) findViewById(R.id.popular_places_recycler_view);

        popularPlacesRecyclerView.setLayoutManager(layoutManager);
        popularPlacesRecyclerView.setAdapter(placeAdapter);
        popularPlacesRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceActivity.start(PopularPlacesActivity.this);
            }
        });
        nextButton = (Button) findViewById(R.id.next_btn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    TestActivity.start(PopularPlacesActivity.this);
            }
        });
    }
}
