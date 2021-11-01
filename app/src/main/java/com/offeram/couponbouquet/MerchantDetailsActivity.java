package com.offeram.couponbouquet;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.offeram.couponbouquet.adapters.AllOutletAdapter;
import com.offeram.couponbouquet.adapters.MerchantDetailsAdapter;
import com.offeram.couponbouquet.adapters.PhotoListAdapter;
import com.offeram.couponbouquet.adapters.UserReviewListAdapter;
import com.offeram.couponbouquet.models.Coupon;
import com.offeram.couponbouquet.models.CouponOutlet;
import com.offeram.couponbouquet.models.Merchant;
import com.offeram.couponbouquet.models.OutletForOffer;
import com.offeram.couponbouquet.models.Rating;
import com.offeram.couponbouquet.responses.Common;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MerchantDetailsActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    public String merchantDetails, merchantId, searchText, outlet = "", versionId = "15", paymentId = "", latitude = "",
            longitude = "";
    Toolbar toolbar;
    //    SliderLayout slider;
    NestedScrollView nestedScrollView;
    LinearLayout mainLayout, reviewLayout, callLayout, mapLayout, addReviewLayout, menuLayout, infraLayout;
    TextView couponTv, buyNowBtn;
    CardView nodataCard;
    //    RecyclerView couponsList;
    // New Design Components
    ImageView backIv, imageIv, vegIv, nonVegIv, eggIv;
    TextView numOfOffersTv, numOfOffersTv1, nameTv, outletTv, moreOutletsTv, reviewTv, totalReviewTv, timingsTv, statusTv;
    FloatingActionButton fabBtn;
    RecyclerView couponsList, menuList, infrastructureList;
    SliderLayout slider;
    MerchantDetailsAdapter adapter;
    PhotoListAdapter menuAdapter, infraAdapter;
    List<Coupon> coupons;
    HashMap<String, String> url_maps;
    Merchant m;
    GestureDetector gestureDetector;
    int unUsedCoupons = 0, charPos = -1;
    String rating = "";
    ProgressDialog pd;
    Boolean isValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_details_test);

        Intent intent = getIntent();
        if (intent != null) {
            merchantId = intent.getStringExtra("merchantId");
            if (intent.hasExtra("searchText")) {
                searchText = intent.getStringExtra("searchText");
            }
        }
        Log.e("In MerchantDetailsAct", "Merchant Id n Outlet : " + merchantId + " n " + outlet);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        // getSupportActionBar().setTitle(m.getCompanyName());
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        reviewLayout = (LinearLayout) findViewById(R.id.reviewLayout);
        callLayout = (LinearLayout) findViewById(R.id.callLayout);
        mapLayout = (LinearLayout) findViewById(R.id.mapLayout);
        addReviewLayout = (LinearLayout) findViewById(R.id.addReviewLayout);
        menuLayout = (LinearLayout) findViewById(R.id.menuLayout);
        infraLayout = (LinearLayout) findViewById(R.id.infraLayout);
        slider = (SliderLayout) findViewById(R.id.slider);
        couponTv = (TextView) findViewById(R.id.couponTv);
        nodataCard = (CardView) findViewById(R.id.nodataCard);
        fabBtn = (FloatingActionButton) findViewById(R.id.fabBtn);
//        buyNowBtn = (TextView) findViewById(R.id.buyNowBtn);
        couponsList = (RecyclerView) findViewById(R.id.couponsList);
        menuList = (RecyclerView) findViewById(R.id.menuList);
        infrastructureList = (RecyclerView) findViewById(R.id.infrastructureList);

        numOfOffersTv = (TextView) findViewById(R.id.numOfOffersTv);
        numOfOffersTv1 = (TextView) findViewById(R.id.numOfOffersTv1);
        nameTv = (TextView) findViewById(R.id.nameTv);
        outletTv = (TextView) findViewById(R.id.outletTv);
        moreOutletsTv = (TextView) findViewById(R.id.moreOutletsTv);
        reviewTv = (TextView) findViewById(R.id.reviewTv);
        totalReviewTv = (TextView) findViewById(R.id.totalReviewTv);
        timingsTv = (TextView) findViewById(R.id.timingsTv);
        statusTv = (TextView) findViewById(R.id.statusTv);
        backIv = (ImageView) findViewById(R.id.backIv);
        imageIv = (ImageView) findViewById(R.id.imageIv);
        vegIv = (ImageView) findViewById(R.id.vegIv);
        nonVegIv = (ImageView) findViewById(R.id.nonVegIv);
        eggIv = (ImageView) findViewById(R.id.eggIv);
        couponsList.setLayoutManager(new LinearLayoutManager(MerchantDetailsActivity.this));
        menuList.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));
        infrastructureList.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));

//        gestureDetector = new GestureDetector(MerchantDetailsActivity.this, MerchantDetailsActivity.this);

//        if (Config.getSharedPreferences(MerchantDetailsActivity.this, "isDemo").equals("1")) {
//            versionId = Config.getSharedPreferences(MerchantDetailsActivity.this, "demoVersionId");
//            paymentId = Config.getSharedPreferences(MerchantDetailsActivity.this, "demoPaymentId");
//        } else if (Config.getSharedPreferences(MerchantDetailsActivity.this, "isDemo").equals("0")) {
//            versionId = Config.getSharedPreferences(MerchantDetailsActivity.this, "versionId");
//            paymentId = Config.getSharedPreferences(MerchantDetailsActivity.this, "paymentId");
//        }
//        versionId = Config.getSharedPreferences(MerchantDetailsActivity.this, "versionId");
        paymentId = Config.getSharedPreferences(MerchantDetailsActivity.this, "paymentId");
        latitude = Config.getSharedPreferences(MerchantDetailsActivity.this, "latitude");
        longitude = Config.getSharedPreferences(MerchantDetailsActivity.this, "longitude");


        if (paymentId != null && paymentId.equals("0")) {
            fabBtn.setVisibility(View.VISIBLE);
        } else {
            fabBtn.setVisibility(View.GONE);
        }

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MerchantDetailsActivity.this, BuyNowActivity.class);
                startActivity(intent);
            }
        });

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        reviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!totalReviewTv.getText().toString().equalsIgnoreCase("0 Review"))
                    showReviewListDialog();
            }
        });

        callLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOutletDialog();
            }
        });

        mapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOutletDialog();
            }
        });

        addReviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReviewDialog();
            }
        });

        moreOutletsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOutletDialog();
            }
        });

//        nestedScrollView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                return onSwipe();
//            }
//        });

//        if (Config.isConnectedToInternet(MerchantDetailsActivity.this)) {
//            pd = new ProgressDialog(MerchantDetailsActivity.this);
//            pd.setMessage("Loading...");
//            pd.setCancelable(false);
//            pd.show();
//            String userId = Config.getSharedPreferences(MerchantDetailsActivity.this, "userId");
//
//            if (searchText != null) {
//                RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
//                Log.e("In MerchantDetails", "With search text Params : " + userId + " , " + versionId + " , " + paymentId + ", " + searchText);
//                Call<SearchByCoupon> call = apiInterface.searchByCouponOrCompany(userId, versionId, paymentId, searchText);
//                call.enqueue(new Callback<SearchByCoupon>() {
//                    @Override
//                    public void onResponse(Call<SearchByCoupon> call, Response<SearchByCoupon> response) {
//                        pd.dismiss();
//                        SearchByCoupon sc = response.body();
//                        if (sc.getSuccess() == 1) {
//                            coupons = sc.getSearchedCouponsList();
//                            mainLayout.setVisibility(View.VISIBLE);
//                            noDataTv.setVisibility(View.GONE);
////                            getSupportActionBar().setTitle(coupons.get(0).getCompanyName());
//                            getSupportActionBar().setTitle("Search Results");
//
////                            Log.e("In MerchantDetailsAct", "Response : " + new Gson().toJson(response.body()));
//                            if (coupons != null && !coupons.isEmpty()) {
//                                adapter = new MerchantDetailsAdapter(MerchantDetailsActivity.this, coupons, "Search Results", "");
//                                couponsList.setAdapter(adapter);
//
//                                url_maps = new HashMap<String, String>();
//                                for (int i = 0; i < coupons.size(); i++) {
//                                    url_maps.put(i + "", coupons.get(i).getCouponImage());
////                                    if (coupons.get(i).getIsUsed().equals("0")) {
////                                        ++unUsedCoupons;
////                                    }
//                                }
//
//                                for (String name : url_maps.keySet()) {
//                                    DefaultSliderView textSliderView = new DefaultSliderView(MerchantDetailsActivity.this);
//                                    // initialize a SliderLayout
//                                    textSliderView
//                                            .description(name)
//                                            .image(url_maps.get(name))
//                                            .setScaleType(BaseSliderView.ScaleType.Fit)
//                                            .setOnSliderClickListener(MerchantDetailsActivity.this);
//
//                                    //add your extra information
//                                    textSliderView.bundle(new Bundle());
//                                    textSliderView.getBundle()
//                                            .putString("extra", name);
//
//                                    slider.addSlider(textSliderView);
//                                }
//
//                                if (coupons.size() == 1) {
//                                    slider.stopAutoCycle();
//                                }
//
////                                couponTv.setText(unUsedCoupons + " Premium Coupons ");
//                                couponTv.setText(coupons.size() + " Premium Coupons ");
//
//                            } else {
//                                mainLayout.setVisibility(View.GONE);
//                                noDataTv.setVisibility(View.VISIBLE);
//                            }
//                        } else {
//                            mainLayout.setVisibility(View.GONE);
//                            noDataTv.setVisibility(View.VISIBLE);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<SearchByCoupon> call, Throwable t) {
//                        pd.dismiss();
//                        Toast.makeText(MerchantDetailsActivity.this, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
//                    }
//                });
//            } else {
//                RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
//                Log.e("In MerchantDetails", "Without Search Params : " + userId + " , " + versionId + " , " + paymentId);
//                Call<Merchant> call = apiInterface.getMerchantDetails(userId, versionId, paymentId, merchantId);
//                call.enqueue(new Callback<Merchant>() {
//                    @Override
//                    public void onResponse(Call<Merchant> call, Response<Merchant> response) {
//                        pd.dismiss();
//                        m = response.body();
//                        if (m != null) {
//                            mainLayout.setVisibility(View.VISIBLE);
//                            noDataTv.setVisibility(View.GONE);
//                            getSupportActionBar().setTitle(m.getCompanyName());
//
//                            coupons = m.getCoupons();
//                            if (coupons != null && !coupons.isEmpty()) {
//                                adapter = new MerchantDetailsAdapter(MerchantDetailsActivity.this, coupons, "Merchant Details", m.getCompanyName());
//                                couponsList.setAdapter(adapter);
//
//                                url_maps = new HashMap<String, String>();
//                                for (int i = 0; i < coupons.size(); i++) {
//                                    url_maps.put(i + "", coupons.get(i).getCouponImage());
////                                    if (coupons.get(i).getIsUsed().equals("0")) {
////                                        ++unUsedCoupons;
////                                    }
//                                }
//
//                                for (String name : url_maps.keySet()) {
//                                    DefaultSliderView textSliderView = new DefaultSliderView(MerchantDetailsActivity.this);
//                                    // initialize a SliderLayout
//                                    textSliderView
//                                            .description(name)
//                                            .image(url_maps.get(name))
//                                            .setScaleType(BaseSliderView.ScaleType.Fit)
//                                            .setOnSliderClickListener(MerchantDetailsActivity.this);
//
//                                    //add your extra information
//                                    textSliderView.bundle(new Bundle());
//                                    textSliderView.getBundle()
//                                            .putString("extra", name);
//
//                                    slider.addSlider(textSliderView);
//                                }
//
//                                if (coupons.size() == 1) {
//                                    slider.stopAutoCycle();
//                                }
//
////                                couponTv.setText(unUsedCoupons + " Premium Coupons ");
//                                couponTv.setText(coupons.size() + " Premium Coupons ");
//
//                            } else {
//                                mainLayout.setVisibility(View.GONE);
//                                noDataTv.setVisibility(View.VISIBLE);
//                            }
//                        } else {
//                            Toast.makeText(MerchantDetailsActivity.this, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Merchant> call, Throwable t) {
//                        pd.dismiss();
//                        Toast.makeText(MerchantDetailsActivity.this, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//        } else {
//            Config.showAlertForInternet(MerchantDetailsActivity.this);
//        }
//
//        slider.setPresetTransformer(SliderLayout.Transformer.Default);
//        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
////        slider.setCustomAnimation(new DescriptionAnimation());
//        slider.setDuration(4000);
//
//        slider.addOnPageChangeListener(this);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("In MerchantDetailsAct", "In OnResume");
        /* The whole code is for old design...
        if (Config.isConnectedToInternet(MerchantDetailsActivity.this)) {
            pd = new ProgressDialog(MerchantDetailsActivity.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
            String userId = Config.getSharedPreferences(MerchantDetailsActivity.this, "userId");

            slider.removeAllSliders();
            if (searchText != null) {
                RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
                Log.e("In MerchantDetails", "With search text Params : " + userId + " , " + versionId + " , " + paymentId + ", " + searchText);
                Call<SearchByCoupon> call = apiInterface.searchByCouponOrCompany(userId, versionId, paymentId, searchText);
                call.enqueue(new Callback<SearchByCoupon>() {
                    @Override
                    public void onResponse(Call<SearchByCoupon> call, Response<SearchByCoupon> response) {
                        pd.dismiss();
                        SearchByCoupon sc = response.body();
                        if (sc.getSuccess() == 1) {
                            coupons = sc.getSearchedCouponsList();
                            mainLayout.setVisibility(View.VISIBLE);
                            noDataTv.setVisibility(View.GONE);
//                            getSupportActionBar().setTitle(coupons.get(0).getCompanyName());
                            getSupportActionBar().setTitle("Search Results");

//                            Log.e("In MerchantDetailsAct", "Response : " + new Gson().toJson(response.body()));
                            if (coupons != null && !coupons.isEmpty()) {
                                adapter = new MerchantDetailsAdapter(MerchantDetailsActivity.this, coupons, "Search Results", "");
                                couponsList.setAdapter(adapter);

                                url_maps = new HashMap<String, String>();
                                for (int i = 0; i < coupons.size(); i++) {
                                    url_maps.put(i + "", coupons.get(i).getCouponImage());
//                                    if (coupons.get(i).getIsUsed().equals("0")) {
//                                        ++unUsedCoupons;
//                                    }
                                }

                                for (String name : url_maps.keySet()) {
                                    DefaultSliderView textSliderView = new DefaultSliderView(MerchantDetailsActivity.this);
                                    // initialize a SliderLayout
                                    textSliderView
                                            .description(name)
                                            .image(url_maps.get(name))
                                            .setScaleType(BaseSliderView.ScaleType.Fit)
                                            .setOnSliderClickListener(MerchantDetailsActivity.this);

                                    //add your extra information
                                    textSliderView.bundle(new Bundle());
                                    textSliderView.getBundle()
                                            .putString("extra", name);

                                    slider.addSlider(textSliderView);
                                }

                                if (coupons.size() == 1) {
                                    slider.stopAutoCycle();
                                }

//                                couponTv.setText(unUsedCoupons + " Premium Coupons ");
                                couponTv.setText(coupons.size() + " Premium Coupons ");

                            } else {
                                mainLayout.setVisibility(View.GONE);
                                noDataTv.setVisibility(View.VISIBLE);
                            }
                        } else {
                            mainLayout.setVisibility(View.GONE);
                            noDataTv.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchByCoupon> call, Throwable t) {
                        pd.dismiss();
                        Toast.makeText(MerchantDetailsActivity.this, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
                Log.e("In MerchantDetails", "Without Search Params : " + userId + " , " + versionId + " , " + paymentId);
                Call<Merchant> call = apiInterface.getMerchantDetails(userId, versionId, paymentId, merchantId);
                call.enqueue(new Callback<Merchant>() {
                    @Override
                    public void onResponse(Call<Merchant> call, Response<Merchant> response) {
                        pd.dismiss();
                        m = response.body();
                        if (m != null) {
                            mainLayout.setVisibility(View.VISIBLE);
                            noDataTv.setVisibility(View.GONE);
                            getSupportActionBar().setTitle(m.getCompanyName());

                            coupons = m.getCoupons();
                            if (coupons != null && !coupons.isEmpty()) {
                                adapter = new MerchantDetailsAdapter(MerchantDetailsActivity.this, coupons, "Merchant Details", m.getCompanyName());
                                couponsList.setAdapter(adapter);

                                url_maps = new HashMap<String, String>();
                                for (int i = 0; i < coupons.size(); i++) {
                                    url_maps.put(i + "", coupons.get(i).getCouponImage());
//                                    if (coupons.get(i).getIsUsed().equals("0")) {
//                                        ++unUsedCoupons;
//                                    }
                                }

                                for (String name : url_maps.keySet()) {
                                    DefaultSliderView textSliderView = new DefaultSliderView(MerchantDetailsActivity.this);
                                    // initialize a SliderLayout
                                    textSliderView
                                            .description(name)
                                            .image(url_maps.get(name))
                                            .setScaleType(BaseSliderView.ScaleType.Fit)
                                            .setOnSliderClickListener(MerchantDetailsActivity.this);

                                    //add your extra information
                                    textSliderView.bundle(new Bundle());
                                    textSliderView.getBundle()
                                            .putString("extra", name);

                                    slider.addSlider(textSliderView);
                                }

                                if (coupons.size() == 1) {
                                    slider.stopAutoCycle();
                                }

//                                couponTv.setText(unUsedCoupons + " Premium Coupons ");
                                couponTv.setText(coupons.size() + " Premium Coupons ");

                            } else {
                                mainLayout.setVisibility(View.GONE);
                                noDataTv.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(MerchantDetailsActivity.this, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Merchant> call, Throwable t) {
                        pd.dismiss();
                        Toast.makeText(MerchantDetailsActivity.this, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else {
            Config.showAlertForInternet(MerchantDetailsActivity.this);
        }  */

        // Below is the code for new design
        getMerchantDetailsApiCall();

    }

    public void getMerchantDetailsApiCall() {
        // Below is the code for new design
        slider.removeAllSliders();
        if (Config.isConnectedToInternet(MerchantDetailsActivity.this)) {
            pd = new ProgressDialog(MerchantDetailsActivity.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
            String userId = Config.getSharedPreferences(MerchantDetailsActivity.this, "userId");

            slider.removeAllSliders();
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Log.e("In MerchantDetails", "Without Search Params : " + userId + " , "
                    + versionId + " , " + paymentId + " n " + merchantId);
            Call<Merchant> call = apiInterface.getMerchantDetails(userId, versionId, paymentId, merchantId, latitude, longitude, Config.getSharedPreferences(MerchantDetailsActivity.this, "cityID"));
            call.enqueue(new Callback<Merchant>() {
                @Override
                public void onResponse(Call<Merchant> call, Response<Merchant> response) {
                    pd.dismiss();
                    Log.e("In MerchantDetails", "Response : " + new Gson().toJson(response.body()));
                    m = response.body();
                    if (m != null) {
                        mainLayout.setVisibility(View.VISIBLE);
                        nodataCard.setVisibility(View.GONE);
//                        getSupportActionBar().setTitle(m.getCompanyName());

                        coupons = m.getCoupons();
                        if (coupons != null && !coupons.isEmpty()) {
                            adapter = new MerchantDetailsAdapter(MerchantDetailsActivity.this, coupons,
                                    "Merchant Details", m.getCompanyName(), new Gson().toJson(m));
                            couponsList.setAdapter(adapter);

                            url_maps = new HashMap<String, String>();
                            for (int i = 0; i < coupons.size(); i++) {
                                url_maps.put(i + "", coupons.get(i).getCouponImage());
//                                    if (coupons.get(i).getIsUsed().equals("0")) {
//                                        ++unUsedCoupons;
//                                    }
                            }

                            for (String name : url_maps.keySet()) {
                                DefaultSliderView textSliderView = new DefaultSliderView(MerchantDetailsActivity.this);
                                // initialize a SliderLayout
                                textSliderView
                                        .description(name)
                                        .image(url_maps.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.Fit)
                                        .setOnSliderClickListener(MerchantDetailsActivity.this);

                                //add your extra information
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle()
                                        .putString("extra", name);

                                slider.addSlider(textSliderView);
                            }

                            if (coupons.size() == 1) {
                                slider.stopAutoCycle();
                            }

//                             couponTv.setText(unUsedCoupons + " Premium Coupons ");
                            numOfOffersTv.setText(coupons.size() + " Offers");
                            numOfOffersTv1.setText(coupons.size() + " Premium Offers");
                            nameTv.setText(m.getCompanyName());
                            if (m.getAverageRatings() != null && (m.getAverageRatings().equals("") || m.getAverageRatings().equals("0"))) {
                                reviewTv.setText("-");
                            } else {
                                reviewTv.setText(m.getAverageRatings());
                            }

                            /*  Note :- For getting distinct values of time from all outlets
                            String timingArr[] = m.getTimings().split(",");
                            String timing = "", tempStr = "";
                            for (int i = 0; i < timingArr.length; i++) {
                                if (i == 0)
                                    timing = timingArr[i];
                                else {
                                    for (int j = i+1; j < timingArr.length; j++){
                                        if(!timingArr[j].equals(timingArr[i])){
                                            if(timing.equals("")){
                                                timing = timingArr[i];
                                            } else {
                                                timing += ", " + timingArr[i];
                                            }
                                        }
                                    }
                                }
                            }

                            timingsTv.setText(timing);  */
                            String totalReview = "";
                            if (m.getTotalRating().equals("0")) {
                                totalReview = m.getTotalRating() + " Review";
                            } else {
                                totalReview = m.getTotalRating() + " Reviews";
                            }
                            totalReviewTv.setText(totalReview);
                            int isVeg = 0, isNonVeg = 0, isEgg = 0;
                            String typeArr[] = m.getType().split(",");
                            for (int i = 0; i < typeArr.length; i++) {
                                if (typeArr[i].equalsIgnoreCase("veg")) {
                                    isVeg = 1;
                                }
                                if (typeArr[i].equalsIgnoreCase("non-veg")) {
                                    isNonVeg = 1;
                                }
                                if (typeArr[i].equalsIgnoreCase("eggitarian")) {
                                    isEgg = 1;
                                }
                            }
                            if (isVeg == 1) {
                                vegIv.setVisibility(View.VISIBLE);
                            }
                            if (isNonVeg == 1) {
                                nonVegIv.setVisibility(View.VISIBLE);
                            }
                            if (isEgg == 1) {
                                eggIv.setVisibility(View.VISIBLE);
                            }

                            outlet = getOutletAccordingToDistance(m.getOutlets());
                            charPos = outlet.indexOf("+");
                            Log.e("In MerchantDetailsAct", "Char + Pos : " + charPos);

                            if (charPos != -1) {
                                outletTv.setText(outlet.substring(0, charPos));
//                                String moreOutlets = outlet.substring(charPos);
//                                moreOutlets = moreOutlets.replace("More", "Outlets");
                                moreOutletsTv.setText(outlet.substring(charPos));
                            } else {
                                outletTv.setText(outlet);
//                                moreOutletsTv.setText("+ 0 More");
                                moreOutletsTv.setVisibility(View.GONE);
                            }
                            String timings = "";
                            int status = -1;
                            for (CouponOutlet co : m.getCoupons().get(0).getCouponAtAvailableOutlets()) {
                                String outletText = outletTv.getText().toString();
                                if (outletText.contains(","))
                                    outletText = outletText.substring(0, outletText.length() - 2);
//                                Log.e("In MerchantDetailsAct", "In For outletText : " + outletText);
                                if (co.getAreaName().equals(outletText)) {
                                    status = co.getStatus();
                                    if (co.getStartTime() != null && !co.getStartTime().equals("") && co.getEndTime() != null
                                            && !co.getEndTime().equals("")) {
                                        timings = co.getStartTime() + "-" + co.getEndTime();
                                    }
                                    if (co.getStartTime2() != null && !co.getStartTime2().equals("") && co.getEndTime2() != null
                                            && !co.getEndTime2().equals("")) {
                                        String timingStr = co.getStartTime2() + "-" + co.getEndTime2();
                                        if (!timingStr.equals(timings))
                                            timings += ", \n" + timingStr;
                                    }
                                }
                            }
//                            Log.e("In MerchantDetailsAct", "Timing Str : " + timings);
                            timingsTv.setText(timings);
                            if (status == 0) {
                                statusTv.setText("Closed");
                                statusTv.setBackgroundResource(R.drawable.bg_red);
                            } else if (status == 1) {
                                statusTv.setText("Open Now");
                                statusTv.setBackgroundResource(R.drawable.bg_green);
                            }

                            Glide.with(MerchantDetailsActivity.this).load(m.getCompanyLogo())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .thumbnail(0.5f)
                                    .error(R.drawable.ic_launcher)
                                    .into(imageIv);

                            if (m.getMenuPhotos() != null && m.getMenuPhotos().size() > 0) {
                                menuLayout.setVisibility(View.VISIBLE);
                                menuAdapter = new PhotoListAdapter(MerchantDetailsActivity.this, m.getMenuPhotos(), "1", m);
                                menuList.setAdapter(menuAdapter);
                            } else {
                                menuLayout.setVisibility(View.GONE);
                            }
                            if (m.getInfrastructurePhotos() != null && m.getInfrastructurePhotos().size() > 0) {
                                infraLayout.setVisibility(View.VISIBLE);
                                infraAdapter = new PhotoListAdapter(MerchantDetailsActivity.this, m.getInfrastructurePhotos(), "0", m);
                                infrastructureList.setAdapter(infraAdapter);
                            } else {
                                infraLayout.setVisibility(View.GONE);
                            }

                        } else {
                            mainLayout.setVisibility(View.GONE);
                            fabBtn.setVisibility(View.GONE);
                            nodataCard.setVisibility(View.VISIBLE);
                        }
                    } else {
//                        Toast.makeText(MerchantDetailsActivity.this, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
                        mainLayout.setVisibility(View.GONE);
                        fabBtn.setVisibility(View.GONE);
                        nodataCard.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onFailure(Call<Merchant> call, Throwable t) {
                    pd.dismiss();
//                    Log.e("Error Merchant Details", "Message : " + t.getMessage());
//                    Toast.makeText(MerchantDetailsActivity.this, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
                    Config.showDialog(MerchantDetailsActivity.this, Config.FailureMsg);
                }
            });
        } else {
            Config.showAlertForInternet(MerchantDetailsActivity.this);
        }

        slider.setPresetTransformer(SliderLayout.Transformer.Default);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
//        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(4000);

        slider.addOnPageChangeListener(this);
    }

    public void showOutletDialog() {
        final DialogPlus outletDialog = DialogPlus.newDialog(MerchantDetailsActivity.this)
                .setContentHolder(new ViewHolder(R.layout.dialog_outlet_address_layout_test))
                .setGravity(Gravity.CENTER)
                .create();
        outletDialog.show();

        View outletView = outletDialog.getHolderView();
        TextView titleTv = outletView.findViewById(R.id.titleTv);
        TextView numOfOutletsTv = outletView.findViewById(R.id.numOfOutletsTv);
        RecyclerView outletList = outletView.findViewById(R.id.outletList);
        outletList.setLayoutManager(new LinearLayoutManager(MerchantDetailsActivity.this));

        titleTv.setText(m.getCompanyName());
        if (m.getCoupons().get(0).getCouponAtAvailableOutlets().size() == 1) {
            numOfOutletsTv.setText("Total " + m.getCoupons().get(0).getCouponAtAvailableOutlets().size() + " Outlet");
        } else {
            numOfOutletsTv.setText("Total " + m.getCoupons().get(0).getCouponAtAvailableOutlets().size() + " Outlets");
        }

        List<CouponOutlet> sortedList = Config.sortListDistanceWise(m.getCoupons().get(0).getCouponAtAvailableOutlets());
        AllOutletAdapter adapter = new AllOutletAdapter(MerchantDetailsActivity.this, sortedList,
                new Gson().toJson(m));
        outletList.setAdapter(adapter);

    }

    public void showReviewDialog() {
        final DialogPlus reviewDialog = DialogPlus.newDialog(MerchantDetailsActivity.this)
                .setContentHolder(new ViewHolder(R.layout.dialog_add_review_layout))
                .setGravity(Gravity.CENTER)
                .setCancelable(false)
                .create();
        reviewDialog.show();

        View reviewView = reviewDialog.getHolderView();
        ImageView closeIv = reviewView.findViewById(R.id.closeIv);
        TextView titleTv = reviewView.findViewById(R.id.titleTv);
        final RatingBar ratingBar = reviewView.findViewById(R.id.ratingBar);
        final EditText commentEt = reviewView.findViewById(R.id.commentEt);
        TextView submitTv = reviewView.findViewById(R.id.submitTv);
        TextView cancelTv = reviewView.findViewById(R.id.cancelTv);

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.lightGrey), PorterDuff.Mode.SRC_ATOP);

        titleTv.setText(m.getCompanyName());
        if (m.getRatings() != null && m.getRatings().size() > 0) {
            String userId = Config.getSharedPreferences(MerchantDetailsActivity.this, "userId");
            for (Rating r : m.getRatings()) {
                if (r.getUserId().equals(userId)) {
                    rating = r.getRating();
                    ratingBar.setRating(Float.parseFloat(r.getRating()));
                    commentEt.setText(r.getComment());
                }
            }
        }

        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewDialog.dismiss();
            }
        });

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewDialog.dismiss();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Log.e("In MerchantDetailsAct", "Rating bar Val : " + v);
                rating = v + "";
            }
        });

        submitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isValid = true;
                String message = "", comment = "";

                comment = commentEt.getText().toString();

                if (rating.equalsIgnoreCase("")) {
                    isValid = false;
                    message = "Must select stars";
                }

                if (isValid) {
                    reviewDialog.dismiss();
                    if (Config.isConnectedToInternet(MerchantDetailsActivity.this)) {
                        pd = new ProgressDialog(MerchantDetailsActivity.this);
                        pd.setMessage("Loading...");
                        pd.setCancelable(false);
                        pd.show();

                        String userId = Config.getSharedPreferences(MerchantDetailsActivity.this, "userId");
                        RetroApiInterface apiInterface = new RetroApiClient().getClient().create(RetroApiInterface.class);
                        Call<Common> call = apiInterface.addReview(userId, rating, comment, merchantId);
                        call.enqueue(new Callback<Common>() {
                            @Override
                            public void onResponse(Call<Common> call, Response<Common> response) {
                                pd.dismiss();
                                Common c = response.body();
                                final Dialog dialog = new Dialog(MerchantDetailsActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.dialog_ok_layout);

                                TextView titleTv = (TextView) dialog.findViewById(R.id.titleTv);
                                TextView messageTv = (TextView) dialog.findViewById(R.id.messageTv);
                                Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
                                titleTv.setVisibility(View.GONE);
                                if (c.getSuccess() == 1) {
                                    String message = "Your review added successfully";
                                    if (m.getRatings() != null && m.getRatings().size() > 0) {
                                        String userId = Config.getSharedPreferences(MerchantDetailsActivity.this, "userId");
                                        for (Rating r : m.getRatings()) {
                                            if (r.getUserId().equals(userId)) {
                                                message = "Your review updated successfully";
                                            }
                                        }
                                    }
                                    messageTv.setText(message);

                                } else
                                    messageTv.setText(c.getMessage());

                                okBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        getMerchantDetailsApiCall();
                                    }
                                });

                                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
                                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                                dialog.getWindow().setLayout(width, height);

                                dialog.show();
                            }

                            @Override
                            public void onFailure(Call<Common> call, Throwable t) {
                                pd.dismiss();
                                final Dialog dialog = new Dialog(MerchantDetailsActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.dialog_ok_layout);

                                TextView titleTv = (TextView) dialog.findViewById(R.id.titleTv);
                                TextView messageTv = (TextView) dialog.findViewById(R.id.messageTv);
                                Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
                                titleTv.setVisibility(View.GONE);
                                messageTv.setText(Config.FailureMsg);
                                okBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
                                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                                dialog.getWindow().setLayout(width, height);

                                dialog.show();
                            }
                        });
                    } else {
                        Config.showAlertForInternet(MerchantDetailsActivity.this);
                    }
                } else {
                    final Dialog dialog = new Dialog(MerchantDetailsActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_ok_layout);

                    TextView titleTv = (TextView) dialog.findViewById(R.id.titleTv);
                    TextView messageTv = (TextView) dialog.findViewById(R.id.messageTv);
                    Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
                    titleTv.setVisibility(View.GONE);
                    messageTv.setText(message);
                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    dialog.getWindow().setLayout(width, height);

                    dialog.show();
                }
            }
        });


    }

    public void showReviewListDialog() {
        final DialogPlus userReviewDialog = DialogPlus.newDialog(MerchantDetailsActivity.this)
                .setContentHolder(new ViewHolder(R.layout.dialog_review_list_layout))
                .setGravity(Gravity.CENTER)
                .create();
        userReviewDialog.show();

        View userReviewView = userReviewDialog.getHolderView();
        TextView titleTv = userReviewView.findViewById(R.id.titleTv);
        TextView numOfUsersTv = userReviewView.findViewById(R.id.numOfUsersTv);
        RecyclerView userList = userReviewView.findViewById(R.id.userList);
        userList.setLayoutManager(new LinearLayoutManager(MerchantDetailsActivity.this));

        titleTv.setText(m.getCompanyName());
        if (m.getRatings().size() == 1) {
            numOfUsersTv.setText("Total " + m.getRatings().size() + " Review");
        } else {
            numOfUsersTv.setText("Total " + m.getRatings().size() + " Reviews");
        }
        UserReviewListAdapter adapter = new UserReviewListAdapter(MerchantDetailsActivity.this, m.getRatings());
        userList.setAdapter(adapter);

    }

    /**
     * Note : This method is implemented to get the outlets according to distance(i.e. minimum distance first)
     * This logic is already implemented in AllOfferAdapter but cannot get outlet string when redirected to this screen from
     * MyFirebaseMessagingService and UsedOffersActivity and so implemented again
     *
     * @return
     */
    public String getOutletAccordingToDistance(List<OutletForOffer> ofo) {
        String outletStr = "";

        if (ofo.size() > 0) {
            String outlet = ofo.get(0).getAreaName();
            double dist = ofo.get(0).getDistance();
            for (int i = 0; i < ofo.size(); i++) {
                OutletForOffer fo = ofo.get(i);
                if (dist > fo.getDistance()) {
                    dist = fo.getDistance();
                    outlet = fo.getAreaName();
                }
            }

            if (ofo.size() == 1) {
                outletStr = outlet;
            } else {
                outletStr = outlet + ", + " + (ofo.size() - 1) + " Outlets";
            }
        }

        return outletStr;
    }

//    @Override
//    public void onSwipe(int direction) {
//        String showToastMessage = "";
//
//        switch (direction) {
//            case SimpleGestureFilter.SWIPE_RIGHT:
//                showToastMessage = "You have Swiped Right.";
//                break;
//            case SimpleGestureFilter.SWIPE_LEFT:
//                showToastMessage = "You have Swiped Left.";
//                break;
//            case SimpleGestureFilter.SWIPE_DOWN:
//                showToastMessage = "You have Swiped Down.";
//                break;
//            case SimpleGestureFilter.SWIPE_UP:
//                showToastMessage = "You have Swiped Up.";
//                break;
//
//        }
//        Toast.makeText(this, showToastMessage, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onDoubleTap() {
//
//    }

//    @Override
//    public boolean onDown(MotionEvent motionEvent) {
//        return false;
//    }
//
//    @Override
//    public void onShowPress(MotionEvent motionEvent) {
//
//    }
//
//    @Override
//    public boolean onSingleTapUp(MotionEvent motionEvent) {
//        return false;
//    }
//
//    @Override
//    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
//        return false;
//    }
//
//    @Override
//    public void onLongPress(MotionEvent motionEvent) {
//
//    }
//
//    @Override
//    public boolean onFling(MotionEvent motionEvent1, MotionEvent motionEvent2, float v, float v1) {
//        if(motionEvent1.getY() - motionEvent2.getY() > 50){
//            Toast.makeText(MerchantDetailsActivity.this , " Swipe Up " , Toast.LENGTH_LONG).show();
//            return true;
//        }
//
//        if(motionEvent2.getY() - motionEvent1.getY() > 50){
//            Toast.makeText(MerchantDetailsActivity.this , " Swipe Down " , Toast.LENGTH_LONG).show();
//            return true;
//        }
//
//        if(motionEvent1.getX() - motionEvent2.getX() > 50){
//            Toast.makeText(MerchantDetailsActivity.this , " Swipe Left " , Toast.LENGTH_LONG).show();
//            return true;
//        }
//
//        if(motionEvent2.getX() - motionEvent1.getX() > 50) {
//            Toast.makeText(MerchantDetailsActivity.this, " Swipe Right ", Toast.LENGTH_LONG).show();
//            return true;
//        }
//        else {
//            return true ;
//        }
//    }


}
