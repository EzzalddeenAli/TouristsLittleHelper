package com.insulardevelopment.touristslittlehelper.place.photo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Маргарита on 10.04.2017.
 */

public class PhotoPagerAdapter extends FragmentPagerAdapter {

    private List<Photo> photos;

    public PhotoPagerAdapter(FragmentManager fragmentManager, List<Photo> photos){
        super(fragmentManager);
        this.photos = photos;
    }

    @Override
    public Fragment getItem(int position) {
        return PhotoFragment.newInstance(photos.get(position));
    }

    @Override
    public int getCount() {
        return photos.size();
    }
}