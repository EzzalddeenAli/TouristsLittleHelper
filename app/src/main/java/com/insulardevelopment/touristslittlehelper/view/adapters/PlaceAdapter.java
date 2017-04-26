package com.insulardevelopment.touristslittlehelper.view.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Place;

import java.util.List;

/**
 * Адаптер мест для отображения в виде списка
 */

public class PlaceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int FOOTER_VIEW = 1;

    private List<Place> places;
    private OnItemClickListener onItemClickListener;
    private OnStartClickListener onStartClickListener;
    private OnFinishClickListener onFinishClickListener;
    private Context context;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public interface OnStartClickListener {
        public void onStartClick(View view, int position);
    }

    public interface OnFinishClickListener {
        public void onFinishClick(View view, int position);
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

    public class FooterViewHolder extends RecyclerView.ViewHolder{

        public Button chooseStartBtn, chooseFinishBtn;

        public FooterViewHolder(View itemView) {
            super(itemView);
            chooseStartBtn = (Button) itemView.findViewById(R.id.choose_start_place_btn);
            chooseFinishBtn = (Button) itemView.findViewById(R.id.choose_finish_place_btn);
        }
    }

    public PlaceAdapter(Context context, List<Place> places, OnItemClickListener onItemClickListener) {
        this.places = places;
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    public void setOnStartClickListener(OnStartClickListener onStartClickListener) {
        this.onStartClickListener = onStartClickListener;
    }

    public void setOnFinishClickListener(OnFinishClickListener onFinishClickListener) {
        this.onFinishClickListener = onFinishClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view;
        if (viewType == FOOTER_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_start_ans_finish_place_layout, parent, false);
            FooterViewHolder viewHolder = new FooterViewHolder(view);
            return viewHolder;
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_places_item, parent, false);
        PlaceViewHolder viewHolder = new PlaceViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        try {
            if (viewHolder instanceof PlaceViewHolder) {
                PlaceViewHolder holder = (PlaceViewHolder) viewHolder;
                final Place place = places.get(position);
                holder.checkBox.setOnCheckedChangeListener(null);
                holder.addressTextView.setText(place.getFormattedAddress());
                holder.nameTextView.setText(place.getName());
                holder.checkBox.setChecked(place.isChosen());
                holder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> place.setChosen(b));
                Glide.with(context)
                        .load(place.getIcon())
                        .into(holder.iconImageView);
                holder.container.setOnClickListener(v -> onItemClickListener.onItemClick(v, position));
            } else if (viewHolder instanceof FooterViewHolder) {
                FooterViewHolder holder = (FooterViewHolder) viewHolder;
                holder.chooseStartBtn.setOnClickListener(view -> onStartClickListener.onStartClick(view, position));
                holder.chooseFinishBtn.setOnClickListener(view -> onFinishClickListener.onFinishClick(view, position));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return places.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == places.size()) {
            return FOOTER_VIEW;
        }
        return super.getItemViewType(position);
    }

}
