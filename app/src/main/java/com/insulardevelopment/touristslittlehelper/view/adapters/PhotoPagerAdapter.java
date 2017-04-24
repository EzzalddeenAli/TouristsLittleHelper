package com.insulardevelopment.touristslittlehelper.view.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.insulardevelopment.touristslittlehelper.model.Photo;
import com.insulardevelopment.touristslittlehelper.view.fragments.PhotoFragment;

import java.util.List;

/**
 * PagerAdapter для фотографий
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