package com.insulardevelopment.touristslittlehelper.placetype;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.insulardevelopment.touristslittlehelper.R;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by админ on 28.11.2016.
 * адаптер для типов мест
 */

public class PlacesTypesAdapter extends RecyclerView.Adapter<PlacesTypesAdapter.MyViewHolder> {

    private List<PlacesTypes> placesTypesList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView placeTypeTextView;
        public CheckBox choosePlaceTypeCheckBox;
        public ImageView iconIv;

        public MyViewHolder(View view) {
            super(view);
            placeTypeTextView = (TextView) view.findViewById(R.id.place_type_text_view);
            choosePlaceTypeCheckBox = (CheckBox) view.findViewById(R.id.choose_place_type_check_box);
            iconIv = (ImageView) view.findViewById(R.id.start_icon_iv);
        }
    }

    public PlacesTypesAdapter(Context context, List<PlacesTypes> placesTypesList){
        this.placesTypesList = placesTypesList;
        this.context = context;
    }

    @Override
    public PlacesTypesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_type_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlacesTypesAdapter.MyViewHolder holder, final int position) {
        final PlacesTypes placesTypes = placesTypesList.get(position);
        holder.placeTypeTextView.setText(placesTypes.getRusName());
        holder.choosePlaceTypeCheckBox.setChecked(placesTypesList.get(position).isChosen());
        holder.choosePlaceTypeCheckBox.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                placesTypes.setChosen(!placesTypes.isChosen());
            }
        });
        Glide.with(context)
                .load("http://maps.gstatic.com/mapfiles/place_api/icons/" + placesTypes.getTypesName() + "-71.png")
                .into(holder.iconIv);
    }

    public Set<String> getChecked() {
        Set<String> types = new HashSet<>();
        for(PlacesTypes type: placesTypesList){
            if (type.isChosen()) types.add(type.getTypesName());
        }
        return types;
    }

    @Override
    public int getItemCount() {
        return placesTypesList.size();
    }
}
