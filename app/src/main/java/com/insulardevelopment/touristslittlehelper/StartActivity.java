package com.insulardevelopment.touristslittlehelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.insulardevelopment.touristslittlehelper.placetype.PlaceType;
import com.insulardevelopment.touristslittlehelper.placetype.PlaceTypeAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        final DataBaseHelper databaseHelper = new DataBaseHelper(this);
        final List<PlaceType> placesTypes = new ArrayList<>();
        String path = "http://maps.gstatic.com/mapfiles/place_api/icons/", size = "-71.png";

        placesTypes.add(new PlaceType(1, "museum", "Музей", false, path + "museum" + size, 0));
        placesTypes.add(new PlaceType(2, "art_gallery", "Галерея", false, path + "art_gallery" + size, 0));
        placesTypes.add(new PlaceType(3, "park", "Парк", false, null, R.drawable.park_icon));
        placesTypes.add(new PlaceType(4, "casino", "Казино", false, path + "casino" + size, 0));
        placesTypes.add(new PlaceType(5, "church", "Собор", false, null, R.drawable.curch_icon));
        placesTypes.add(new PlaceType(6, "amusement_park", "Парк развлечений", false, null, R.drawable.amusement_park_icon));
        placesTypes.add(new PlaceType(7, "zoo", "Зоопарк", false, path + "zoo" + size, 0));
        placesTypes.add(new PlaceType(8, "stadium", "Стадион", false, path + "stadium" + size, 0));
        placesTypes.add(new PlaceType(9, "aquarium", "Океанариум", false, path + "aquarium" + size, 0));



        final PlaceTypeAdapter placeTypeAdapter = new PlaceTypeAdapter(this, placesTypes);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(placeTypeAdapter);

        confirmBtn = (Button) findViewById(R.id.start_confirm_btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    for(PlaceType placeType: placesTypes) {
                        try {
                            databaseHelper.getTestDao().create(placeType);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                MainActivity.start(StartActivity.this);
            }
        });
    }
}
