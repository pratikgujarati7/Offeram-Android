package com.offeram.couponbouquet;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
// import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.offeram.couponbouquet.fragments.FragmentFavorites;
import com.offeram.couponbouquet.fragments.FragmentMyAccount;
import com.offeram.couponbouquet.fragments.FragmentNotifications;
import com.offeram.couponbouquet.fragments.FragmentOffers;
import com.offeram.couponbouquet.fragments.FragmentStarred;
import com.offeram.couponbouquet.responses.Common;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    //    BottomNavigationViewEx navigationView;
    Toolbar toolbar;
    int lastPos = 0;
    Fragment fragment;
    public LinearLayout bottomView, offerLayout, favouriteLayout, notificationLayout, myAccountLayout;
    ImageView unreadIv;
    String from = "";
    Common c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        if (intent.hasExtra("from")) {
            from = intent.getStringExtra("from");
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        bottomView = findViewById(R.id.bottomView);
        offerLayout = bottomView.findViewById(R.id.offerLayout);
        favouriteLayout = bottomView.findViewById(R.id.favouriteLayout);
        notificationLayout = bottomView.findViewById(R.id.notificationLayout);
        myAccountLayout = bottomView.findViewById(R.id.myAccountLayout);
        unreadIv = bottomView.findViewById(R.id.unreadIv);

        offerLayout.setTag("offers");
        favouriteLayout.setTag("favourites");
        notificationLayout.setTag("notifications");
        myAccountLayout.setTag("myaccount");
        offerLayout.setOnClickListener(this);
        favouriteLayout.setOnClickListener(this);
        notificationLayout.setOnClickListener(this);
        myAccountLayout.setOnClickListener(this);

//        unreadIv.setImageDrawable(new ColorDrawable(getResources().getColor(R.color.darkPurple)));
        String splashResponse = Config.getSharedPreferences(HomeActivity.this, "splashResponse");
        Log.e("In HomeActivity", "Splash Response : " + splashResponse);
        c = new Gson().fromJson(splashResponse, Common.class);
        Log.e("In HomeActivity", "OnCreate From Db CountForNoti : " + c.getCountForNotification());
//        if(c.getCountForNotification() == 0){
//            unreadIv.setVisibility(View.GONE);
//        } else if(c.getCountForNotification() > 0){
//            unreadIv.setVisibility(View.VISIBLE);
//        }
        if (from.equals("")) {
            if (c.getCountForNotification() == 0) {
                ((ImageView) bottomView.findViewById(R.id.notificationIv)).setImageResource(R.drawable.notifications_inactive);
            } else if (c.getCountForNotification() > 0) {
                ((ImageView) bottomView.findViewById(R.id.notificationIv)).setImageResource(R.drawable.notifications_unread);
            }
            loadFragment("offers");
        } else if (from.equals("pingedOffer")) {
            fragment = new FragmentStarred();
            Bundle bundle=new Bundle();
            bundle.putString("from","pingedOffer");
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            favouriteLayout.setOnClickListener(this);
            favouriteLayout.setTag("favourites");
        } else {
            loadFragment("notifications");
            ((ImageView) bottomView.findViewById(R.id.notificationIv)).setImageResource(R.drawable.notifications_active);
            ((ImageView) bottomView.findViewById(R.id.offerIv)).setImageResource(R.drawable.offers_inactive);
        }

//        navigationView = findViewById(R.id.navigationView);
//        navigationView.enableAnimation(false);
//        navigationView.enableShiftingMode(false);
//        navigationView.enableItemShiftingMode(false);
//        navigationView.setCurrentItem(0);
//        loadFragment("offers");
//
//        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
////                Below is the logic for icon and text selector in navigation view
//                int selPos = navigationView.getMenuItemPosition(item);
//                Log.e("In HomeAct", "SelPos n LastPos : " + selPos + " n " + lastPos);
////                if (selPos != lastPos) {
////                    if (selPos == 0) {
////                        navigationView.getMenu().getItem(selPos).getIcon().setVisible(false, false);
////                        navigationView.getMenu().getItem(selPos).setIcon(R.drawable.offers_active);
////                    } else if (selPos == 1) {
////                        navigationView.getMenu().getItem(selPos).setIcon(R.drawable.favourites_active);
////                    } else if (selPos == 2) {
////                        navigationView.getMenu().getItem(selPos).setIcon(R.drawable.notifications_active);
////                    } else if (selPos == 3) {
////                        navigationView.getMenu().getItem(selPos).setIcon(R.drawable.myaccount_active);
////                    }
////
////                    if(lastPos == 0) {
////                        navigationView.getMenu().getItem(lastPos).setIcon(R.drawable.offers_inactive);
////                    } else if(lastPos == 1) {
////                        navigationView.getMenu().getItem(lastPos).setIcon(R.drawable.favourites_inactive);
////                    } else if(lastPos == 2) {
////                        navigationView.getMenu().getItem(lastPos).setIcon(R.drawable.notifications_inactive);
////                    } else if(lastPos == 3) {
////                        navigationView.getMenu().getItem(lastPos).setIcon(R.drawable.myaccount_inactive);
////                    }
////                }
////                lastPos = selPos;
//                switch (item.getItemId()) {
//                    case R.id.bottom_offers:
//                        loadFragment("offers");
//                        return true;
//                    case R.id.bottom_favorites:
//                        loadFragment("favorites");
//                        return true;
//                    case R.id.bottom_notifications:
//                        loadFragment("notifications");
//                        return true;
//                    case R.id.bottom_my_account:
//                        loadFragment("myaccount");
//                        return true;
//                    default:
//                        break;
//                }
//                return false;
//            }
//        });

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("In HomeActivity", "In OnCreateOptions");
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("In HomeActivity", "In Options");
        return true;
    }*/

    public void loadFragment(String name) {
        if (name.equalsIgnoreCase("offers")) {
            fragment = new FragmentOffers();
        } else if (name.equalsIgnoreCase("favourites")) {
//            fragment = new FragmentFavorites();
            // Changed temporarily as pinged tab is hidden for now
            fragment = new FragmentStarred();
            Bundle bundle=new Bundle();
            bundle.putString("from","home");
            fragment.setArguments(bundle);
        } else if (name.equalsIgnoreCase("notifications")) {
            fragment = new FragmentNotifications();
        } else if (name.equalsIgnoreCase("myaccount")) {
            fragment = new FragmentMyAccount();
        }
//        Bundle bundle = new Bundle();
//        bundle.putString("userProfileResponse", new Gson().toJson(up));
//        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        Log.e("In HomeAct", "In OnClick");
        ImageView offerIv, favouriteIv, notificationIv, myAccountIv, unreadIv;
        offerIv = bottomView.findViewById(R.id.offerIv);
        favouriteIv = bottomView.findViewById(R.id.favouriteIv);
        notificationIv = bottomView.findViewById(R.id.notificationIv);
        myAccountIv = bottomView.findViewById(R.id.myAccountIv);

        if (view.getTag().toString().equals("offers")) {
            offerIv.setImageResource(R.drawable.offers_active);
            favouriteIv.setImageResource(R.drawable.activities_inactive);
            notificationIv.setImageResource(R.drawable.notifications_inactive);
            myAccountIv.setImageResource(R.drawable.myaccount_inactive);
        } else if (view.getTag().toString().equals("favourites")) {
            offerIv.setImageResource(R.drawable.offers_inactive);
            favouriteIv.setImageResource(R.drawable.activities_active);
            notificationIv.setImageResource(R.drawable.notifications_inactive);
            myAccountIv.setImageResource(R.drawable.myaccount_inactive);
        } else if (view.getTag().toString().equals("notifications")) {
            offerIv.setImageResource(R.drawable.offers_inactive);
            favouriteIv.setImageResource(R.drawable.activities_inactive);
            notificationIv.setImageResource(R.drawable.notifications_active);
            myAccountIv.setImageResource(R.drawable.myaccount_inactive);
        } else if (view.getTag().toString().equals("myaccount")) {
            offerIv.setImageResource(R.drawable.offers_inactive);
            favouriteIv.setImageResource(R.drawable.activities_inactive);
            notificationIv.setImageResource(R.drawable.notifications_inactive);
            myAccountIv.setImageResource(R.drawable.myaccount_active);
        }
        loadFragment(view.getTag().toString());
    }
}
