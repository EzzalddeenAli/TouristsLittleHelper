package com.insulardevelopment.touristslittlehelper.place;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

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
    public void onBindViewHolder(final PlaceAdapter.PlaceViewHolder holder, final int position) {
        final Place place = places.get(position);
        holder.addressTextView.setText(place.getFormattedAddress());
        holder.nameTextView.setText(place.getName());
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream is = (InputStream) new URL(place.getIcon()).getContent();
                    drawable = Drawable.createFromStream(is, "src name");
                } catch (Exception e) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (drawable != null)
                    holder.iconImageView.setImageDrawable(drawable);
            }

        }.execute();
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
