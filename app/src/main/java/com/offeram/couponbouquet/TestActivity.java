package com.offeram.couponbouquet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.offeram.couponbouquet.adapters.CustomSwipeMerchantAdapter;
import com.offeram.couponbouquet.models.AllOffer;
import com.offeram.couponbouquet.responses.GetAllOffers;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    ViewPager viewPager;
    List<AllOffer> offerList = new ArrayList<>();
    String offersResponse = "", type = "";
    int position = -1;
    GetAllOffers go;
    CustomSwipeMerchantAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Note :-
        // 1) Both of the below variables are used for swipe in view pager and filter data according to tabs
        // i) offersResponse and ii) type
        // 2) 'position' named param is used for displaying the selected position of list in view pager

        Intent intent = getIntent();
        if (intent.hasExtra("offersResponse") && intent.hasExtra("type")) {
            offersResponse = intent.getStringExtra("offersResponse");
            type = intent.getStringExtra("type");
            position = intent.getIntExtra("position", -1);
        }

        viewPager = findViewById(R.id.viewPager);

        go = new Gson().fromJson(offersResponse, GetAllOffers.class);
        if(type.equalsIgnoreCase("all")){
            offerList = go.getAllOffer();
        } else if(type.equalsIgnoreCase("special offers")){
            // Special Offers are to be implemented here
        } else {
            for (int i = 0; i < go.getAllOffer().size(); i++) {
                if (go.getAllOffer().get(i).getCategoryId().equals(type)) {
                    offerList.add(go.getAllOffer().get(i));
                }
            }
        }

        if(offerList.size() > 0){
            viewPagerAdapter = new CustomSwipeMerchantAdapter(TestActivity.this, offerList);
            viewPager.setAdapter(viewPagerAdapter);
            if(position != -1){
                viewPager.setCurrentItem(position);
            }
        }

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
