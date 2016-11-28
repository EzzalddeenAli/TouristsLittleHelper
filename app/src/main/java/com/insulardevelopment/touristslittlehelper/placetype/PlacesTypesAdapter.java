package com.insulardevelopment.touristslittlehelper.placetype;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.insulardevelopment.touristslittlehelper.R;

import java.util.List;

/**
 * Created by админ on 28.11.2016.
 * адаптер для типов мест
 */

public class PlacesTypesAdapter extends RecyclerView.Adapter<PlacesTypesAdapter.MyViewHolder> {

    private List<PlacesTypes> placesTypesList;
    private boolean[] checked;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView placeTypeTextView;
        public CheckBox choosePlaceTypeCheckBox;

        public MyViewHolder(View view) {
            super(view);
            placeTypeTextView = (TextView) view.findViewById(R.id.place_type_text_view);
            choosePlaceTypeCheckBox = (CheckBox) view.findViewById(R.id.choose_place_type_check_box);
        }
    }

    public PlacesTypesAdapter(List<PlacesTypes> placesTypesList){
        this.placesTypesList = placesTypesList;
        checked = new boolean[placesTypesList.size()];
    }

    @Override
    public PlacesTypesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_type_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlacesTypesAdapter.MyViewHolder holder, final int position) {
        PlacesTypes placesTypes = placesTypesList.get(position);
        holder.placeTypeTextView.setText(placesTypes.getTypesName());
        holder.choosePlaceTypeCheckBox.setChecked(checked[position]);
        holder.choosePlaceTypeCheckBox.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checked[position] = !checked[position];
            }
        });
    }

    public boolean[] getChecked() {
        return checked;
    }

    @Override
    public int getItemCount() {
        return placesTypesList.size();
    }
}
