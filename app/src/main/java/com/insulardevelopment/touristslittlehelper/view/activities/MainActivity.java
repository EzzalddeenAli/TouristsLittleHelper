package com.insulardevelopment.touristslittlehelper.view.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.database.DataBaseHelper;
import com.insulardevelopment.touristslittlehelper.network.Newtork;
import com.insulardevelopment.touristslittlehelper.model.Route;
import com.insulardevelopment.touristslittlehelper.view.adapters.RouteAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
*   Главная ктивити для отображения сохраненных маршрутов
*/
public class MainActivity extends AppCompatActivity {

    private Button mainRouteBtn;
    private List<Route> routes;
    private DataBaseHelper helper;
    private RouteAdapter adapter;
    private MultiSelector multiSelector = new MultiSelector();
    private ModalMultiSelectorCallback callback = new ModalMultiSelectorCallback(multiSelector) {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.route_menu, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete_action_menu:
                    for (int i = routes.size(); i >= 0; i--) {
                        if (multiSelector.isSelected(i, 0)) {
                            try {
                                helper.getRouteDao().delete(routes.get(i));
                                routes.remove(i);
                                adapter.notifyItemChanged(i);

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    mode.finish();
                    if (routes.size() == 0){
                        findViewById(R.id.no_routes_rl).setVisibility(View.VISIBLE);
                    }
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelector.clearSelections();
            multiSelector.setSelectable(false);
            super.onDestroyActionMode(mode);
        }
    };

    public static void start(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getResources().getString(R.string.routes));
        mainRouteBtn = (Button)findViewById(R.id.main_trace_btn);
        mainRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Newtork.isAvalaible(MainActivity.this)) {
                    ChooseLocationActivity.start(MainActivity.this);
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_connection), Toast.LENGTH_LONG).show();
                }
            }
        });
        routes = new ArrayList<>();
        helper = new DataBaseHelper(this);
        try {
            routes = helper.getRouteDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (routes.size() == 0){
            findViewById(R.id.no_routes_rl).setVisibility(View.VISIBLE);
        }else {
            RecyclerView routeRecycler = (RecyclerView) findViewById(R.id.routes_recycler_view);
            adapter = new RouteAdapter(routes, this, multiSelector, callback);
            routeRecycler.setAdapter(adapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            routeRecycler.setLayoutManager(layoutManager);
        }
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
