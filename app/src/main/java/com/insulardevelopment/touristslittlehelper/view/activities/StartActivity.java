package com.insulardevelopment.touristslittlehelper.view.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.insulardevelopment.touristslittlehelper.database.DataBaseHelper;
import com.insulardevelopment.touristslittlehelper.model.PlaceType;
import com.insulardevelopment.touristslittlehelper.view.adapters.PlaceTypeAdapter;
import com.insulardevelopment.touristslittlehelper.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
*   Активити для выбора необходимых типов мест при первом запуске приложения
*/

public class StartActivity extends AppCompatActivity {

    public static final String FIRST_LAUNCHING = "first";
    public static final String APP_PREFERENCES = "preferences";

    private Button confirmBtn;
    private RecyclerView typesRecycler;
    private SharedPreferences sp;
    private PlaceTypeAdapter placeTypeAdapter;
    private DataBaseHelper databaseHelper;
    private List<PlaceType> placesTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initViews();
        sp = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean first = sp.getBoolean(FIRST_LAUNCHING, true);
        if (!first){
            MainActivity.start(this);
        }
        databaseHelper = new DataBaseHelper(this);
        setPlaceTypes();
        placeTypeAdapter = new PlaceTypeAdapter(this, placesTypes);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        typesRecycler.setLayoutManager(layoutManager);
        typesRecycler.setAdapter(placeTypeAdapter);
        confirmBtn.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(FIRST_LAUNCHING, false);
            editor.commit();
                for(PlaceType placeType: placesTypes) {
                    try {
                        databaseHelper.getTypeDao().create(placeType);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            MainActivity.start(StartActivity.this);
        });
    }

    private void initViews(){
        confirmBtn = findViewById(R.id.start_confirm_btn);
        typesRecycler = findViewById(R.id.places_types_recycler_view);
    }

    private void setPlaceTypes(){
        placesTypes = new ArrayList<>();
        String path = "http://maps.gstatic.com/mapfiles/place_api/icons/", size = "-71.png";
        placesTypes.add(new PlaceType(1, "museum", "Музей", false, path + "museum" + size, 0));
        placesTypes.add(new PlaceType(2, "art_gallery", "Галерея", false, path + "art_gallery" + size, 0));
        placesTypes.add(new PlaceType(3, "park", "Парк", false, null, R.drawable.park_icon_400));
        placesTypes.add(new PlaceType(4, "casino", "Казино", false, path + "casino" + size, 0));
        placesTypes.add(new PlaceType(5, "church", "Собор", false, null, R.drawable.curch_icon_360));
        placesTypes.add(new PlaceType(6, "amusement_park", "Парк развлечений", false, null, R.drawable.amusement_park_icon));
        placesTypes.add(new PlaceType(7, "zoo", "Зоопарк", false, path + "zoo" + size, 0));
        placesTypes.add(new PlaceType(8, "stadium", "Стадион", false, path + "stadium" + size, 0));
        placesTypes.add(new PlaceType(9, "aquarium", "Океанариум", false, path + "aquarium" + size, 0));
    }
}
