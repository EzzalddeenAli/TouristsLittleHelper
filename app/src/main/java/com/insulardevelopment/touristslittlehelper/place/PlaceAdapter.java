package com.insulardevelopment.touristslittlehelper.place;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.insulardevelopment.touristslittlehelper.R;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Маргарита on 20.11.2016.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {
    private List<Place> places;
    private OnItemClickListener onItemClickListener;
    private Drawable drawable;
    private Context context;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, addressTextView;
        public View container;
        public CheckBox checkBox;
        public ImageView iconImageView;

        public PlaceViewHolder(final View view) {
            super(view);
            container = view;
            nameTextView = (TextView) view.findViewById(R.id.popular_places_item_name_text_view);
            addressTextView = (TextView) view.findViewById(R.id.popular_places_item_address_text_view);
            checkBox = (CheckBox) view.findViewById(R.id.choose_place_check_box);
            iconImageView = (ImageView) view.findViewById(R.id.popular_places_icon_image_view);
        }
    }


    public PlaceAdapter(Context context, List<Place> places, OnItemClickListener onItemClickListener) {
        this.places = places;
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    @Override
    public PlaceAdapter.PlaceViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_places_item, parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PlaceAdapter.PlaceViewHolder holder, final int position) {
        final Place place = places.get(position);
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.addressTextView.setText(place.getFormattedAddress());
        holder.nameTextView.setText(place.getName());
        holder.checkBox.setChecked(place.isChosen());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                place.setChosen(isChecked);
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                place.setChosen(b);
            }
        });
        Glide.with(context)
                .load(place.getIcon())
                .into(holder.iconImageView);
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
