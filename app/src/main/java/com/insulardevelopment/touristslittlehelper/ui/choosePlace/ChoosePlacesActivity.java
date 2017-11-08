package com.insulardevelopment.touristslittlehelper.ui.choosePlace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.Button;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.ui.AbstractActivity;
import com.insulardevelopment.touristslittlehelper.ui.chooseLocation.ChooseStartAndFinishPlaceActivity;
import com.insulardevelopment.touristslittlehelper.ui.chooseLocation.LocationViewModel;
import com.insulardevelopment.touristslittlehelper.ui.placeType.PlaceTypesViewModel;
import com.insulardevelopment.touristslittlehelper.ui.route.NewRouteActivity;
/*
*   Активити для выбора мест
*/

public class ChoosePlacesActivity extends AbstractActivity {

    private static final String SELECTED_LATLNG = "latlng";
    private static final int RADIUS = 5000;

    private ViewPager placesViewPager;
    private TabLayout tabLayout;
    private Button nextBtn;

    private ChoosePlacesViewModel choosePlacesViewModel;
    private PlaceTypesViewModel placeTypesViewModel;

    public static void start(Context context, LatLng latLng) {
        Intent intent = new Intent(context, ChoosePlacesActivity.class);
        intent.putExtra(SELECTED_LATLNG, latLng);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LocationViewModel.CHOOSE_START_PLACE:
                if (resultCode == CommonStatusCodes.SUCCESS) {
                    if (data != null) {
                        choosePlacesViewModel.setStartPlace((Place) data.getSerializableExtra(ChooseStartAndFinishPlaceActivity.DATA));
                    }
                }
                break;
            case LocationViewModel.CHOOSE_FINISH_PLACE:
                if (resultCode == CommonStatusCodes.SUCCESS) {
                    if (data != null) {
                        choosePlacesViewModel.setFinishPlace((Place) data.getSerializableExtra(ChooseStartAndFinishPlaceActivity.DATA));
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_places);

        choosePlacesViewModel = getViewModel(ChoosePlacesViewModel.class);
        placeTypesViewModel = getViewModel(PlaceTypesViewModel.class);
        initViews();
        setTitle(R.string.popular_places_activity_description);

        choosePlacesViewModel.setSelectedLatLng(getIntent().getParcelableExtra(SELECTED_LATLNG));
        FragmentManager fragmentManager = getSupportFragmentManager();

        placeTypesViewModel.getSavedPlaceTypes().observe(this, placeTypes -> {
            choosePlacesViewModel.getPlaces(choosePlacesViewModel.getSelectedLatLng(), RADIUS, placeTypes, getString(R.string.google_api_key))
                    .observe(this, placesList -> {
                        choosePlacesViewModel.setPlaces(placesList);
                        PlacesPagerAdapter adapter = new PlacesPagerAdapter(fragmentManager);
                        placesViewPager.setAdapter(adapter);
                        tabLayout.setupWithViewPager(placesViewPager);
                    });
        });

        nextBtn.setOnClickListener(v -> {
            choosePlacesViewModel.saveChosenPlaces();
            NewRouteActivity.start(ChoosePlacesActivity.this, choosePlacesViewModel.hasStartOrFinish());
        });
    }

    private void initViews() {
        placesViewPager = findViewById(R.id.places_view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        nextBtn = findViewById(R.id.to_route_btn);
    }
}
