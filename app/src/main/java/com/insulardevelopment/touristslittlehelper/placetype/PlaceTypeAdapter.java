package com.insulardevelopment.touristslittlehelper.placetype;

import android.content.Context;
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
import java.util.List;
import java.util.Set;

/**
 * Created by админ on 28.11.2016.
 * адаптер для типов мест
 */

public class PlaceTypeAdapter extends RecyclerView.Adapter<PlaceTypeAdapter.MyViewHolder> {

    private List<PlaceType> placeTypeList;
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

    public PlaceTypeAdapter(Context context, List<PlaceType> placeTypeList){
        this.placeTypeList = placeTypeList;
        this.context = context;
    }

    @Override
    public PlaceTypeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_type_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaceTypeAdapter.MyViewHolder holder, final int position) {
        final PlaceType placeType = placeTypeList.get(position);
        holder.placeTypeTextView.setText(placeType.getRusName());
        holder.choosePlaceTypeCheckBox.setChecked(placeTypeList.get(position).isChosen());
        holder.choosePlaceTypeCheckBox.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v) {
                placeType.setChosen(!placeType.isChosen());
            }
        });
        if (placeType.getIconId() == 0) {
            Glide.with(context)
                    .load("http://maps.gstatic.com/mapfiles/place_api/icons/" + placeType.getTypesName() + "-71.png")
                    .into(holder.iconIv);
        } else {
            holder.iconIv.setImageDrawable(context.getResources().getDrawable(placeType.getIconId()));
        }
    }

    public Set<String> getChecked() {
        Set<String> types = new HashSet<>();
        for(PlaceType type: placeTypeList){
            if (type.isChosen()) types.add(type.getTypesName());
        }
        return types;
    }

    @Override
    public int getItemCount() {
        return placeTypeList.size();
    }
}
