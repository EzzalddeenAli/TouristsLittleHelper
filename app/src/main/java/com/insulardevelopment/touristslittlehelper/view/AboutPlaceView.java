package com.insulardevelopment.touristslittlehelper.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Place;
import com.insulardevelopment.touristslittlehelper.view.activities.PlaceActivity;

/**
 * Created by Маргарита on 30.08.2017.
 */

public class AboutPlaceView extends RelativeLayout{

    private View view;
    private Context context;
    private Place place;

    private RelativeLayout relativeLayout;
    private LinearLayout checkLayout;
    private LinearLayout addressLayout;
    private TextView placeNameTv, addressTv;
    private Button moreInfoBtn;
    private ImageView iconIv;
    private ImageButton closeIb;
    private CheckBox placeCb;

    private int color;
    private boolean showAddress;
    private boolean checkable;

    public AboutPlaceView(Context context) {
        super(context);
        this.context = context;
    }

    public AboutPlaceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AboutPlaceView,
                0, 0);

        try {
            checkable = a.getBoolean(R.styleable.AboutPlaceView_checkable, false);
            color = a.getColor(R.styleable.AboutPlaceView_backgroundColor, ContextCompat.getColor(context, R.color.transparent_yellow));
            showAddress = a.getBoolean(R.styleable.AboutPlaceView_showAddress, false);
        } finally {
            a.recycle();
        }

    }

    public void setPlace(Place place) {
        this.place = place;
        createView();
    }

    public View createView() {
        view = inflate(context, R.layout.place_info_layout, this);
        initViews(view);
        setupView();
        return view;
    }

    private void setupView(){
        relativeLayout.setVisibility(VISIBLE);
        relativeLayout.setBackgroundColor(color);
        placeNameTv.setText(place.getName());
        Glide.with(context)
                .load(place.getIcon())
                .into(iconIv);
        moreInfoBtn.setOnClickListener(view -> PlaceActivity.start(context, place));
        if (checkable){
            checkLayout.setVisibility(VISIBLE);
            placeCb.setOnCheckedChangeListener((compoundButton, b) -> place.setChosen(b));
            placeCb.setChecked(place.isChosen());
        }
        if (showAddress){
            addressLayout.setVisibility(VISIBLE);
            addressTv.setText(place.getFormattedAddress());
        }
        closeIb.setOnClickListener(view -> relativeLayout.setVisibility(View.INVISIBLE));
    }

    private void initViews(View view){
        relativeLayout = view.findViewById(R.id.place_info_rl);
        checkLayout = view.findViewById(R.id.check_layout);
        addressLayout = view.findViewById(R.id.address_layout);
        addressTv = view.findViewById(R.id.address_text_view);
        closeIb = view.findViewById(R.id.close_ib);
        iconIv = view.findViewById(R.id.place_info_icon_iv);
        placeNameTv = view.findViewById(R.id.place_name_info_text_view);
        placeCb = view.findViewById(R.id.map_choose_place_check_box);
        moreInfoBtn = view.findViewById(R.id.more_info_place_btn);
    }
}
