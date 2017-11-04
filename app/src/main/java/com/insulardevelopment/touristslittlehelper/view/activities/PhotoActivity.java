package com.insulardevelopment.touristslittlehelper.view.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.insulardevelopment.touristslittlehelper.R;
import com.insulardevelopment.touristslittlehelper.model.Photo;
import com.insulardevelopment.touristslittlehelper.view.adapters.PhotoPagerAdapter;

import java.util.ArrayList;

/*
*   Активити для отображения фотографий
*/

public class PhotoActivity extends AppCompatActivity {

    private static final String PHOTOS = "photos";

    private ArrayList<Photo> photos;

    public static void start(Context context, ArrayList<Photo> photos){
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putExtra(PHOTOS, photos);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        photos = (ArrayList<Photo>) getIntent().getSerializableExtra(PHOTOS);
        final ViewPager photosViewPager = findViewById(R.id.photos_view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        PhotoPagerAdapter adapter = new PhotoPagerAdapter(fragmentManager, photos);
        photosViewPager.setAdapter(adapter);
    }
}
