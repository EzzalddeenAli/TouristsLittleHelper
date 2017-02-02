package com.insulardevelopment.touristslittlehelper;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ChoosePlacesActivity extends AppCompatActivity {

    public static void start(Context context){
        Intent intent = new Intent(context, ChoosePlacesActivity.class);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_places);
        ViewPager placesViewPager = (ViewPager) findViewById(R.id.places_view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        PlacesPagerAdapter adapter = new PlacesPagerAdapter(fragmentManager);
        placesViewPager.setAdapter(adapter);
    }
}
