package com.insulardevelopment.touristslittlehelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Маргарита on 20.11.2016.
 */

public class PlaceAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<PlaceEntity> objects;

    PlaceAdapter(Context context, ArrayList<PlaceEntity> places) {
        ctx = context;
        objects = places;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.popular_places_item, parent, false);
        }
        PlaceEntity place = (PlaceEntity) getItem(position);
        TextView nameTextView = (TextView) view.findViewById(R.id.popular_places_item_name_text_view);
        TextView addressTextView = (TextView) view.findViewById(R.id.popular_places_item_address_text_view);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.choose_place_check_box);
        nameTextView.setText(place.getName());
        addressTextView.setText(place.getAddress());
        return view;
    }
}
