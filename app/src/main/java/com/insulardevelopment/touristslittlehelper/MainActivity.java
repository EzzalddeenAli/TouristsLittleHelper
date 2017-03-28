package com.insulardevelopment.touristslittlehelper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.insulardevelopment.touristslittlehelper.place.Place;
import com.insulardevelopment.touristslittlehelper.route.Route;
import com.insulardevelopment.touristslittlehelper.route.RouteAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button mainRouteBtn;
    private List<Route> routes;

    public static void start(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainRouteBtn = (Button)findViewById(R.id.main_trace_btn);
        mainRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseLocationActivity.start(MainActivity.this);
            }
        });
        RecyclerView routeRecycler = (RecyclerView) findViewById(R.id.routes_recycler_view);
        routes = new ArrayList<>();
        routes.add(new Route("Воронеж, Воронежская область, Россия", "Музеи Воронежа", new ArrayList<Place>(), 10, 4, ""));
        routes.add(new Route("Москва, Московская область, Россия", "Галереи Москвы", new ArrayList<Place>(), 7.4, 5, ""));
        RouteAdapter adapter = new RouteAdapter(routes);
        routeRecycler.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        routeRecycler.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.type_action_menu:
                PlaceTypesActivity.start(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
