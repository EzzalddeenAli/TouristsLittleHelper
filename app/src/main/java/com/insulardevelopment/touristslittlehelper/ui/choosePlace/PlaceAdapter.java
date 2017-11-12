package com.insulardevelopment.touristslittlehelper.ui.choosePlace;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

    private OnCheckedChangeListener onCheckedChangeListener;

    public interface OnCheckedChangeListener {
        void onCheck(Place place);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnStartClickListener {
        void onStartClick(View view, int position);
    }

    public interface OnFinishClickListener {
        void onFinishClick(View view, int position);
    }

    public PlaceAdapter(Context context, List<Place> places, OnItemClickListener onItemClickListener) {
        this.places = places;
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    public List<Place> getItems() {
        return places;
    }

    public void setOnStartClickListener(OnStartClickListener onStartClickListener) {
        this.onStartClickListener = onStartClickListener;
    }

    public void setOnFinishClickListener(OnFinishClickListener onFinishClickListener) {
        this.onFinishClickListener = onFinishClickListener;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view;
        if (viewType == FOOTER_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_start_ans_finish_place_layout, parent, false);
            return new FooterViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_places_item, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof PlaceViewHolder) {
            PlaceViewHolder holder = (PlaceViewHolder) viewHolder;
            holder.setupHolder(places.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return places.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == places.size()) {
            return FOOTER_VIEW;
        }
        return super.getItemViewType(position);
    }

    public void updatePlace(Place place) {
        notifyItemChanged(places.indexOf(place));
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, addressTextView;
        public CheckBox checkBox;
        public ImageView iconImageView;

        public PlaceViewHolder(final View view) {
            super(view);
            nameTextView = view.findViewById(R.id.popular_places_item_name_text_view);
            addressTextView = view.findViewById(R.id.popular_places_item_address_text_view);
            checkBox = view.findViewById(R.id.choose_place_check_box);
            iconImageView = view.findViewById(R.id.popular_places_icon_image_view);

            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(v, getAdapterPosition()));
        }

        public void setupHolder(Place place) {
            checkBox.setOnCheckedChangeListener(null);
            addressTextView.setText(place.getFormattedAddress());
            nameTextView.setText(place.getName());
            checkBox.setChecked(place.isChosen());
            checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
                place.setChosen(b);
                onCheckedChangeListener.onCheck(place);
            });
            Glide.with(context)
                    .load(place.getIcon())
                    .into(iconImageView);

        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public Button chooseStartBtn, chooseFinishBtn;

        public FooterViewHolder(View itemView) {
            super(itemView);
            chooseStartBtn = itemView.findViewById(R.id.choose_start_place_btn);
            chooseFinishBtn = itemView.findViewById(R.id.choose_finish_place_btn);

            chooseStartBtn.setOnClickListener(view -> onStartClickListener.onStartClick(view, getAdapterPosition()));
            chooseFinishBtn.setOnClickListener(view -> onFinishClickListener.onFinishClick(view, getAdapterPosition()));
        }
    }

}
