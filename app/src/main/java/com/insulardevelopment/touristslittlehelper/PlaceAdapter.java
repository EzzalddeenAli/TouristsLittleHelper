package com.insulardevelopment.touristslittlehelper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Маргарита on 20.11.2016.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {
    private List<Place> places;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, addressTextView;
        public View container;
        public CheckBox checkBox;

        public PlaceViewHolder(final View view) {
            super(view);
            container = view;
            nameTextView = (TextView) view.findViewById(R.id.popular_places_item_name_text_view);
            addressTextView = (TextView) view.findViewById(R.id.popular_places_item_address_text_view);
            checkBox = (CheckBox) view.findViewById(R.id.choose_place_check_box);
        }
    }

    public PlaceAdapter(List<Place> places,  OnItemClickListener onItemClickListener) {
        this.places = places;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public PlaceAdapter.PlaceViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_places_item, parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaceAdapter.PlaceViewHolder holder, final int position) {
        Place place = places.get(position);
        holder.addressTextView.setText(place.getAddress());
        holder.nameTextView.setText(place.getName());
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }
}
