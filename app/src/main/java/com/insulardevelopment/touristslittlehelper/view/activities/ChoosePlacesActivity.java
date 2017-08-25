package com.insulardevelopment.touristslittlehelper.view.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.network.APIWorker;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.view.adapters.PlacesPagerAdapter;
import com.insulardevelopment.touristslittlehelper.database.DataBaseHelper;
import com.insulardevelopment.touristslittlehelper.model.PlaceType;

import java.util.ArrayList;
import java.util.List;
/*
*   Активити для выбора мест
*/

public class ChoosePlacesActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String SELECTED_LATLNG = "latlng";
    private static final int RADIUS = 5000;

    private ViewPager placesViewPager;
    private TabLayout tabLayout;
    private Button nextBtn;
    private LatLng selectedLatLng;
    private List<Place> places;
    private Place startPlace, finishPlace;
    private boolean hasStartAndFinish = false;


    public static void start(Context context, LatLng latLng){
        Intent intent = new Intent(context, ChoosePlacesActivity.class);
        intent.putExtra(SELECTED_LATLNG, latLng);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_places);
        initViews();
        setTitle(R.string.popular_places_activity_description);
        Intent intent = getIntent();
        selectedLatLng = intent.getParcelableExtra(SELECTED_LATLNG);
        FragmentManager fragmentManager = getSupportFragmentManager();
        DataBaseHelper helper = new DataBaseHelper(this);
        List<PlaceType> types;
        try {
            types = helper.getTypeDao().queryForAll();
            APIWorker.getPlaces(selectedLatLng, RADIUS, types, "AIzaSyCjnoH7MNT5iS90ZHk4cV_fYj3ZZTKKp_Y")
                    .subscribe(places -> {
                        PlacesPagerAdapter adapter = new PlacesPagerAdapter(fragmentManager, selectedLatLng, places);
                        placesViewPager.setAdapter(adapter);
                        ChoosePlacesActivity.this.places = places;
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        tabLayout.setupWithViewPager(placesViewPager);
        nextBtn.setOnClickListener(this);
    }

    private void initViews(){
        placesViewPager = (ViewPager) findViewById(R.id.places_view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        nextBtn = (Button) findViewById(R.id.to_route_btn);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ChooseStartAndFinishPlaceActivity.CHOOSE_START_PLACE:
                if (resultCode == CommonStatusCodes.SUCCESS) {
                    if (data != null) {
                        startPlace = (Place) data.getSerializableExtra(ChooseStartAndFinishPlaceActivity.DATA);
                        hasStartAndFinish = true;
                    }
                }
                break;
            case ChooseStartAndFinishPlaceActivity.CHOOSE_FINISH_PLACE:
                if (resultCode == CommonStatusCodes.SUCCESS) {
                    if (data != null) {
                        finishPlace = (Place) data.getSerializableExtra(ChooseStartAndFinishPlaceActivity.DATA);
                        hasStartAndFinish = true;
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        ArrayList<Place> chosenPlaces = new ArrayList<>();
        for(Place p: places){
            if (p.isChosen()) {
                chosenPlaces.add(p);
            }
        }
        if (startPlace != null){
            chosenPlaces.add(0, startPlace);
        }
        if (finishPlace != null){
            chosenPlaces.add(finishPlace);
        }
        NewRouteActivity.start(ChoosePlacesActivity.this, chosenPlaces, hasStartAndFinish);
    }

}
