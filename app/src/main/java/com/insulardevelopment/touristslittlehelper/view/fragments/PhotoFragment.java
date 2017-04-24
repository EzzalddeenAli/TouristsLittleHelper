package com.insulardevelopment.touristslittlehelper.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Photo;

/**
 * Фрагмент для отображения фото
 */

public class PhotoFragment extends Fragment{

    private Photo photo;
    private ImageView imageView;

    public PhotoFragment(Photo photo){
        super();
        this.photo = photo;
    }

    public static PhotoFragment newInstance(Photo photo){
        return new PhotoFragment(photo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        setRetainInstance(true);
        imageView = (ImageView) view.findViewById(R.id.photo_fragment_iv);
        Glide.with(getActivity())
                .load(photo.getUrl())
                .into(imageView);
        return view;
    }
}
