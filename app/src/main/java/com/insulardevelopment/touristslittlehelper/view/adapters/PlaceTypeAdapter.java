package com.insulardevelopment.touristslittlehelper.view.adapters;

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
import com.insulardevelopment.touristslittlehelper.model.PlaceType;

import java.util.List;

/**
 * адаптер для типов мест
 */

public class PlaceTypeAdapter extends RecyclerView.Adapter<PlaceTypeAdapter.MyViewHolder> {

    private List<PlaceType> placeTypeList;
    private Context context;

    public PlaceTypeAdapter(Context context, List<PlaceType> placeTypeList){
        this.placeTypeList = placeTypeList;
        this.context = context;
    }

    public List<PlaceType> getPlaceTypeList() {
        return placeTypeList;
    }

    @Override
    public PlaceTypeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_type_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaceTypeAdapter.MyViewHolder holder, final int position) {
        holder.setupHolder(placeTypeList.get(position));
    }

    @Override
    public int getItemCount() {
        return placeTypeList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView placeTypeTextView;
        CheckBox choosePlaceTypeCheckBox;
        ImageView iconIv;

        MyViewHolder(View view) {
            super(view);
            placeTypeTextView = view.findViewById(R.id.place_type_text_view);
            choosePlaceTypeCheckBox = view.findViewById(R.id.choose_place_type_check_box);
            iconIv = view.findViewById(R.id.start_icon_iv);
        }

        void setupHolder(PlaceType placeType){
            placeTypeTextView.setText(placeType.getRusName());
            choosePlaceTypeCheckBox.setChecked(placeTypeList.get(getAdapterPosition()).isChosen());
            choosePlaceTypeCheckBox.setOnClickListener(v -> placeType.setChosen(!placeType.isChosen()));

            String url = "http://maps.gstatic.com/mapfiles/place_api/icons/" + placeType.getTypesName() + "-71.png";
            if (placeType.getIconId() == 0) {
                Glide.with(context)
                     .load(url)
                     .into(iconIv);
            } else {
                iconIv.setImageDrawable(context.getResources().getDrawable(placeType.getIconId()));
            }
        }
    }
}
