package com.insulardevelopment.touristslittlehelper.ui.main;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Route;
import com.insulardevelopment.touristslittlehelper.ui.route.RouteActivity;

import java.util.List;

/**
 * Адаптер для маршрутов
 */

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private List<Route> routes;
    private Context context;
    private MultiSelector multiSelector;
    private ActionMode actionMode;
    private ModalMultiSelectorCallback callback;

    public class RouteViewHolder extends SwappingHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView nameTextView, cityTextView, distanceTextView, timeTextView;
        public View container;

        public RouteViewHolder(final View view) {
            super(view, multiSelector);
            container = view;
            nameTextView = view.findViewById(R.id.route_name_text_view);
            distanceTextView = view.findViewById(R.id.route_distance_text_view);
            timeTextView = view.findViewById(R.id.route_time_text_view);
            cityTextView = view.findViewById(R.id.city_name_route_text_view);
            view.setLongClickable(true);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void bindRoute(Route route){
            nameTextView.setText(route.getName());
            distanceTextView.setText(String.valueOf(String.format("%.1f", ((double) route.getDistance())/1000)) + "км");
            timeTextView.setText(String.valueOf(String.format("%.1f", ((double) route.getTime())/3600)) + "ч");
            cityTextView.setText(route.getCity());
            container.setLongClickable(true);
            container.setOnClickListener(this);
            container.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (multiSelector.isSelectable()) {
                setActivated(!isActivated());
                multiSelector.setSelected(RouteViewHolder.this, isActivated());
                if (multiSelector.getSelectedPositions().size() == 0) {
                    actionMode.finish();
                }
            } else {
                RouteActivity.start(context, routes.get(getAdapterPosition()));
            }
        }

        @Override
        public boolean onLongClick(View v) {
            actionMode = ((AppCompatActivity) context).startSupportActionMode(callback);
            multiSelector.setSelected(RouteViewHolder.this, true);
            multiSelector.setSelectable(true);
            return true;
        }
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public Route getItem(int position){
        return routes.get(position);
    }

    public RouteAdapter(List<Route> routes, Context context, MultiSelector multiSelector, ModalMultiSelectorCallback callback) {
        this.routes = routes;
        this.context = context;
        this.multiSelector = multiSelector;
        this.callback = callback;
    }

    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_item, parent, false);
        return new RouteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RouteViewHolder holder, int position) {
        final Route route = routes.get(position);
        holder.bindRoute(route);
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

}
