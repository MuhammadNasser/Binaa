package com.binaa.company;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import com.binaa.company.models.Image;
import com.binaa.company.utils.FullScreenImageAdapter;

import java.util.ArrayList;

public class FullScreenViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fullscreen_view);

        ViewPager viewPager = findViewById(R.id.pager);


        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);
        ArrayList<Image> images = (ArrayList<Image>) i.getSerializableExtra("images");

        FullScreenImageAdapter adapter = new FullScreenImageAdapter(FullScreenViewActivity.this, images);

        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);
    }
}
