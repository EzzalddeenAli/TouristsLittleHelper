package com.insulardevelopment.touristslittlehelper.route;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.insulardevelopment.touristslittlehelper.R;

import java.util.List;

/**
 * Адаптер для маршрутов
 */

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private List<Route> routes;

    public class RouteViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, cityTextView, distanceTextView, timeTextView;

        public RouteViewHolder(final View view) {
            super(view);
            nameTextView = (TextView) view.findViewById(R.id.route_name_text_view);
            distanceTextView = (TextView) view.findViewById(R.id.route_distance_text_view);
            timeTextView = (TextView) view.findViewById(R.id.route_time_text_view);
            cityTextView = (TextView) view.findViewById(R.id.city_name_route_text_view);
        }
    }

    public RouteAdapter(List<Route> routes) {
        this.routes = routes;
    }

    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_item, parent, false);
        return new RouteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RouteViewHolder holder, int position) {
        Route route = routes.get(position);
        holder.nameTextView.setText(route.getName());
        holder.distanceTextView.setText(String.valueOf(route.getDistance())+"км");
        holder.timeTextView.setText(String.valueOf(route.getTime())+"ч");
        holder.cityTextView.setText(route.getCity());
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }
}
