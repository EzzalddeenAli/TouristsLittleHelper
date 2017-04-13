package com.insulardevelopment.touristslittlehelper.place.photo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.insulardevelopment.touristslittlehelper.R;
import java.util.ArrayList;

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
        final ViewPager photosViewPager = (ViewPager) findViewById(R.id.photos_view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        PhotoPagerAdapter adapter = new PhotoPagerAdapter(fragmentManager, photos);
        photosViewPager.setAdapter(adapter);
    }
}
