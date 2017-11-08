package com.insulardevelopment.touristslittlehelper.ui.route;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Route;
import com.insulardevelopment.touristslittlehelper.ui.main.MainActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Активити для построения маршрута
 */

public class NewRouteActivity extends AbstractRouteActivity {

    private static final String START_AND_FINISH = "start and finish";

    private EditText nameEt;
    private Button saveRouteBtn;

    private RouteViewModel routeViewModel;

    public static void start(Context context, boolean hasStartAndFinish) {
        Intent intent = new Intent(context, NewRouteActivity.class);
        intent.putExtra(START_AND_FINISH, hasStartAndFinish);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_route);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        routeViewModel = getViewModel(RouteViewModel.class);
        places = routeViewModel.getPlaces(getIntent().getBooleanExtra(START_AND_FINISH, false));

        initViews();
        moveToLatLng = routeViewModel.getLatLng();
        mapFragment.getMapAsync(this);

        routeViewModel.getRoute(places, getResources().getString(R.string.google_api_key))
                .observe(this, this::setupRoute);

        saveRouteBtn.setOnClickListener(view -> {
            route.setName(nameEt.getText().toString());
            routeViewModel.saveRoute(route);
            MainActivity.start(NewRouteActivity.this);
        });
    }

    private void setupRoute(Route serverRoute) {
        route = serverRoute;
        route.setCity(getCity());
        setContent();
        drawRoute(serverRoute.decodePoly());
    }

    private String getCity() {
        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(places.get(0).getLatitude(), places.get(0).getLongitude(), 1);
            return addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    protected void initViews() {
        super.initViews();
        nameEt = findViewById(R.id.route_name_text_view);
        saveRouteBtn = findViewById(R.id.save_route_btn);
    }
}