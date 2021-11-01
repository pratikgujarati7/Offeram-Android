package com.offeram.couponbouquet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.offeram.couponbouquet.adapters.MyAccountListAdapter;
import com.offeram.couponbouquet.adapters.TopTenAdapter;
import com.offeram.couponbouquet.responses.Common;

public class FriendsAndFamily extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter pagerAdapter;
    private Toolbar toolbar;
    private TextView titleTv;
    private RecyclerView recyclerView;
    private String strjson;
    private TopTenAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendsandfamily);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleTv = (TextView) findViewById(R.id.titleTv);
        titleTv.setText("Friends & Family");
        Intent intent=getIntent();
        if (intent!=null){
            strjson=intent.getStringExtra("userDetail");
        }

//        tabLayout = findViewById(R.id.tabLayout);
//        viewPager = findViewById(R.id.viewPager);
//        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(pagerAdapter);
//        tabLayout.setupWithViewPager(viewPager);

        recyclerView = findViewById(R.id.recycleview_topten);
        recyclerView.setLayoutManager(new LinearLayoutManager(FriendsAndFamily.this));
        Common c=new Gson().fromJson(strjson,Common.class);
        adapter = new TopTenAdapter(FriendsAndFamily.this,c.getToptenredeemerslist());
        recyclerView.setAdapter(adapter);

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Bundle bundle = null;
            switch (position) {
                case 0:
//                    fragment = new FragmentGroupBy();
//                    bundle = new Bundle();
//                    bundle.putString("strfromcountry", "country");
//                    bundle.putString("countryId", countryId);
//                    bundle.putString("allIds", allIds);
//                    bundle.putString("allstatus", allstatus);
//
//                    fragment.setArguments(bundle);
                    return fragment;

                case 1:
//                    fragment = new FragmentRequest();
//                    bundle = new Bundle();
//                    bundle.putString("strfromcountry", "country");
//                    bundle.putString("countryId", countryId);
//                    bundle.putString("allIds", allIds);
//                    bundle.putString("allstatus", allstatus);
//
//                    fragment.setArguments(bundle);
                    return fragment;

                default:
                    return null;
            }

        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "My Contact";
                case 1:
                    return "Top 10 in Surat";
            }
            return null;
        }


        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
