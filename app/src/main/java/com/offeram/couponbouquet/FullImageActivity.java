package com.offeram.couponbouquet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.offeram.couponbouquet.adapters.FullScreenAdapter;
import com.offeram.couponbouquet.models.Merchant;
import com.offeram.couponbouquet.models.Photo;

import java.util.List;

public class FullImageActivity extends AppCompatActivity {

    ViewPager viewPager;
    String merchantDetails = "", selPos = "", isMenu = "";
    List<Photo> list = null;
    Merchant m = null;
    FullScreenAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        Intent intent = getIntent();
        if (intent.hasExtra("imagePos")) {
            merchantDetails = intent.getStringExtra("merchantDetails");
            selPos = intent.getStringExtra("imagePos");
            isMenu = intent.getStringExtra("isMenu");
            m = new Gson().fromJson(merchantDetails, Merchant.class);
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        if (isMenu.equals("1")) {
            list = m.getMenuPhotos();
        } else if (isMenu.equals("0")) {
            list = m.getInfrastructurePhotos();
        }
        adapter = new FullScreenAdapter(FullImageActivity.this, list, Integer.parseInt(selPos));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(Integer.parseInt(selPos));
    }
}
