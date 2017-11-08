package com.insulardevelopment.touristslittlehelper.view.activities;

import android.content.Context;
import android.content.Intent;
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
import com.insulardevelopment.touristslittlehelper.network.Network;
import com.insulardevelopment.touristslittlehelper.model.Route;
import com.insulardevelopment.touristslittlehelper.view.AbstractActivity;
import com.insulardevelopment.touristslittlehelper.view.adapters.RouteAdapter;
import com.insulardevelopment.touristslittlehelper.view.viewmodel.MainViewModel;

import java.util.List;

/*
*   Главная ктивити для отображения сохраненных маршрутов
*/
public class MainActivity extends AbstractActivity {

    private MainViewModel mainViewModel;

    private Button mainRouteBtn;
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
                    for (int i = adapter.getRoutes().size(); i >= 0; i--) {
                        if (multiSelector.isSelected(i, 0)) {
                            mainViewModel.deleteRoute(adapter.getItem(i));
                        }
                    }
                    mode.finish();
                    if (adapter.getRoutes().size() == 0){
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

        mainViewModel = getViewModel(MainViewModel.class);

        mainViewModel.getRoutes().observe(this, routes -> {
            if (routes.size() == 0){
                findViewById(R.id.no_routes_rl).setVisibility(View.VISIBLE);
            } else {
                setupRecycler(routes);
            }
        });

        mainRouteBtn = findViewById(R.id.main_trace_btn);
        mainRouteBtn.setOnClickListener(v -> {
            if (Network.isAvailable(MainActivity.this)) {
                ChooseLocationActivity.start(MainActivity.this);
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_connection), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupRecycler(List<Route> routes){
        RecyclerView routeRecycler = findViewById(R.id.routes_recycler_view);
        adapter = new RouteAdapter(routes, this, multiSelector, callback);
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
