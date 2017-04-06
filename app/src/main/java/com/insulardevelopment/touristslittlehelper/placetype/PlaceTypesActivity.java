package com.insulardevelopment.touristslittlehelper.placetype;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.insulardevelopment.touristslittlehelper.database.DataBaseHelper;
import com.insulardevelopment.touristslittlehelper.R;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlaceTypesActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private List<PlaceType> placesTypes = null;
    public static final String CHOSEN_TYPES = "types";
    public static final String APP_PREFERENCES = "preferences";
    private DataBaseHelper databaseHelper;

    public static void start(Context context){
        Intent intent = new Intent(context, PlaceTypesActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_types);
        sp = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.type_menu_recycler);
        Set types = sp.getStringSet(StartActivity.CHOSEN_TYPES, new HashSet<String>());
        databaseHelper = new DataBaseHelper(this);

        try {
            placesTypes = databaseHelper.getTypeDao().queryForAll();
            final PlaceTypeAdapter placeTypeAdapter = new PlaceTypeAdapter(this, placesTypes);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(placeTypeAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                try {
                    for(PlaceType placeType: placesTypes) {
                        UpdateBuilder<PlaceType, Integer> updateBuilder = databaseHelper.getTypeDao().updateBuilder();
                        updateBuilder.where().eq("id", placeType.getId());
                        updateBuilder.updateColumnValue("chosen", placeType.isChosen());
                        updateBuilder.update();
                    }
                } catch (Exception e){
                }
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
