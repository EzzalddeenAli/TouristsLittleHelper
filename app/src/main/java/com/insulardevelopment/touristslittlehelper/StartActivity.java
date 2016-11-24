package com.insulardevelopment.touristslittlehelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/*
*   Активити для выбора необходимых типов мест при первом запуске приложения
*/

public class StartActivity extends AppCompatActivity {

    private ListView typesList;
    private Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        typesList = (ListView) findViewById(R.id.places_types_list_view);
        String[] types = new String[]{"Museum", "Theatre", "Art Gallery", "Park", "Casino", "Zoo"};
        ArrayAdapter<String> typesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, types);
        typesList.setAdapter(typesAdapter);

        confirmBtn = (Button) findViewById(R.id.start_confirm_btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.start(StartActivity.this);
            }
        });
    }
}
