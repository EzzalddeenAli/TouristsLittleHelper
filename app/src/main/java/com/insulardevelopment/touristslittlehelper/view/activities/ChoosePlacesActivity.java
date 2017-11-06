package com.insulardevelopment.touristslittlehelper.view.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.insulardevelopment.touristslittlehelper.view.AbstractActivity;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.view.adapters.PlacesPagerAdapter;
import com.insulardevelopment.touristslittlehelper.database.DataBaseHelper;
import com.insulardevelopment.touristslittlehelper.model.PlaceType;
import com.insulardevelopment.touristslittlehelper.view.viewmodel.ChoosePlacesViewModel;

import java.util.ArrayList;
import java.util.List;
/*
*   Активити для выбора мест
*/

public class ChoosePlacesActivity extends AbstractActivity implements View.OnClickListener{

    private static final String SELECTED_LATLNG = "latlng";
    private static final int RADIUS = 5000;

    private ViewPager placesViewPager;
    private TabLayout tabLayout;
    private Button nextBtn;
    private LatLng selectedLatLng;
    private List<Place> places;
    private Place startPlace, finishPlace;
    private boolean hasStartAndFinish = false;
    private static ArrayList<Place> chosenPlaces;

    private ChoosePlacesViewModel choosePlacesViewModel;

    public static void start(Context context, LatLng latLng){
        Intent intent = new Intent(context, ChoosePlacesActivity.class);
        intent.putExtra(SELECTED_LATLNG, latLng);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        choosePlacesViewModel = getViewModel(ChoosePlacesViewModel.class);
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

            choosePlacesViewModel.getPlaces(selectedLatLng, RADIUS, types, getString(R.string.google_api_key)).observe(this, placesList -> {
                PlacesPagerAdapter adapter = new PlacesPagerAdapter(fragmentManager, selectedLatLng, placesList);
                placesViewPager.setAdapter(adapter);
                places = placesList;
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        tabLayout.setupWithViewPager(placesViewPager);
        nextBtn.setOnClickListener(this);
    }

    public static ArrayList<Place> getChosenPlaces() {
        return chosenPlaces;
    }

    private void initViews(){
        placesViewPager = findViewById(R.id.places_view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        nextBtn = findViewById(R.id.to_route_btn);
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
        chosenPlaces = new ArrayList<>();
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
        NewRouteActivity.start(ChoosePlacesActivity.this, hasStartAndFinish);
    }

}
