package com.offeram.couponbouquet.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.Gson;
import com.offeram.couponbouquet.Config;
import com.offeram.couponbouquet.R;
import com.offeram.couponbouquet.RetroApiClient;
import com.offeram.couponbouquet.RetroApiInterface;
import com.offeram.couponbouquet.TestActivity;
import com.offeram.couponbouquet.models.AllOffer;
import com.offeram.couponbouquet.models.Coupon;
import com.offeram.couponbouquet.models.Merchant;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 11-Apr-18.
 */

public class CustomSwipeMerchantAdapter extends PagerAdapter {

    private Context mContext;
    List<AllOffer> data = new ArrayList<>();
    List<Coupon> coupons;
    MerchantDetailsAdapter adapter;
    HashMap<String, String> url_maps = new HashMap<>();
    Merchant m;
    String outlet = "";
    int charPos = -1;
    ProgressDialog pd;

    public CustomSwipeMerchantAdapter(Context context, List<AllOffer> data) {
        mContext = context;
        this.data = data;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
//        Log.e("In SwipeViewPager", "Position  : " + position);
//        ModelObject modelObject = ModelObject.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.activity_merchant_details_test, collection, false);

        final LinearLayout mainLayout = (LinearLayout) layout.findViewById(R.id.mainLayout);
        LinearLayout callLayout = (LinearLayout) layout.findViewById(R.id.callLayout);
        LinearLayout mapLayout = (LinearLayout) layout.findViewById(R.id.mapLayout);
        LinearLayout reviewLayout = (LinearLayout) layout.findViewById(R.id.reviewLayout);
        final SliderLayout slider = (SliderLayout) layout.findViewById(R.id.slider);
//        TextView couponTv = (TextView) layout.findViewById(R.id.couponTv);
        final TextView noDataTv = (TextView) layout.findViewById(R.id.noDataTv);
//        buyNowBtn = (TextView) layout.findViewById(R.id.buyNowBtn);
        final RecyclerView couponsList = (RecyclerView) layout.findViewById(R.id.couponsList);
        couponsList.setLayoutManager(new LinearLayoutManager(mContext));


        final TextView numOfOffersTv = (TextView) layout.findViewById(R.id.numOfOffersTv);
        final TextView numOfOffersTv1 = (TextView) layout.findViewById(R.id.numOfOffersTv1);
        final TextView nameTv = (TextView) layout.findViewById(R.id.nameTv);
        final TextView outletTv = (TextView) layout.findViewById(R.id.outletTv);
        final TextView moreOutletsTv = (TextView) layout.findViewById(R.id.moreOutletsTv);
        final TextView reviewTv = (TextView) layout.findViewById(R.id.reviewTv);
        final TextView totalReviewTv = (TextView) layout.findViewById(R.id.totalReviewTv);
        TextView timingsTv = (TextView) layout.findViewById(R.id.timingsTv);
        TextView statusTv = (TextView) layout.findViewById(R.id.statusTv);
        ImageView backIv = (ImageView) layout.findViewById(R.id.backIv);
        final ImageView imageIv = (ImageView) layout.findViewById(R.id.imageIv);
        ImageView typeIv = (ImageView) layout.findViewById(R.id.vegIv);

        String userId = Config.getSharedPreferences(mContext, "userId");
        String versionId = Config.getSharedPreferences(mContext, "versionId");
        String paymentId = Config.getSharedPreferences(mContext, "paymentId");
        String latitude = Config.getSharedPreferences(mContext, "latitude");
        String  longitude = Config.getSharedPreferences(mContext, "longitude");
        String merchantId = data.get(position).getMerchantId();

        if (data.get(position).getOutlets().size() == 1) {
            outlet = data.get(position).getOutlets().get(0).getAreaName();
        } else {
            outlet = data.get(position).getOutlets().get(0).getAreaName() + ", + "
                    + (data.get(position).getOutlets().size() - 1) + " More";
        }

        if (Config.isConnectedToInternet(mContext)) {
//            pd = new ProgressDialog(mContext);
//            pd.setMessage("Loading...");
//            pd.setCancelable(false);
//            pd.show();

            slider.removeAllSliders();
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Log.e("In SwipeViewPager", "Params : " + userId + " , " + versionId + " , " + paymentId + " n " + merchantId);
            Call<Merchant> call = apiInterface.getMerchantDetails(userId, versionId, paymentId, merchantId, latitude, longitude,Config.getSharedPreferences(mContext,"cityID"));
            call.enqueue(new Callback<Merchant>() {
                @Override
                public void onResponse(Call<Merchant> call, Response<Merchant> response) {
//                    pd.dismiss();
                    m = response.body();
                    if (m != null) {
                        mainLayout.setVisibility(View.VISIBLE);
                        noDataTv.setVisibility(View.GONE);
//                        getSupportActionBar().setTitle(m.getCompanyName());

                        coupons = m.getCoupons();
                        if (coupons != null && !coupons.isEmpty()) {
                            adapter = new MerchantDetailsAdapter(mContext, coupons, "Merchant Details", m.getCompanyName(), new Gson().toJson(m));
                            couponsList.setAdapter(adapter);

//                            url_maps = new HashMap<String, String>();
                            for (int i = 0; i < coupons.size(); i++) {
                                url_maps.put(i + "", coupons.get(i).getCouponImage());
//                                    if (coupons.get(i).getIsUsed().equals("0")) {
//                                        ++unUsedCoupons;
//                                    }
                            }

                            for (String name : url_maps.keySet()) {
                                DefaultSliderView textSliderView = new DefaultSliderView(mContext);
                                // initialize a SliderLayout
                                textSliderView
                                        .description(name)
                                        .image(url_maps.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.Fit)
                                        .setOnSliderClickListener((TestActivity) mContext);

                                //add your extra information
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle()
                                        .putString("extra", name);

                                slider.addSlider(textSliderView);
                            }

                            if (coupons.size() == 1) {
                                slider.stopAutoCycle();
                            }

                            reviewTv.setText(m.getAverageRatings() + "");
                            String totalReview = "";
                            if (m.getTotalRating().equals("0")) {
                                totalReview = m.getTotalRating() + " Review";
                            } else {
                                totalReview = m.getTotalRating() + " Reviews";
                            }
                            totalReviewTv.setText(totalReview);
                            numOfOffersTv.setText(coupons.size() + " Offers");
                            numOfOffersTv1.setText(coupons.size() + " Premium Offers");
                            nameTv.setText(m.getCompanyName());

                            charPos = outlet.indexOf("+");
                            Log.e("In MerchantDetailsAct", "Char + Pos : " + charPos);

                            if (charPos != -1) {
                                outletTv.setText(outlet.substring(0, charPos));
                                moreOutletsTv.setText(outlet.substring(charPos));
                            } else {
                                outletTv.setText(outlet);
//                                moreOutletsTv.setText("+ 0 More");
                                moreOutletsTv.setVisibility(View.GONE);
                            }
                            Glide.with(mContext).load(m.getCompanyLogo())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .error(R.mipmap.ic_launcher)
                                    .into(imageIv);

                        } else {
                            mainLayout.setVisibility(View.GONE);
                            noDataTv.setVisibility(View.VISIBLE);
                        }
                    } else {
//                        Toast.makeText(mContext, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
                        Config.showDialog(mContext, Config.FailureMsg);
                    }
                }

                @Override
                public void onFailure(Call<Merchant> call, Throwable t) {
//                    pd.dismiss();
//                    Toast.makeText(mContext, "Some Error Occurred. Please Try Again.", Toast.LENGTH_LONG).show();
                    Config.showDialog(mContext, Config.FailureMsg);
                }
            });
        } else {
            Config.showAlertForInternet((TestActivity) mContext);
        }

        slider.setPresetTransformer(SliderLayout.Transformer.Default);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
//        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(4000);

        slider.addOnPageChangeListener((TestActivity) mContext);

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TestActivity) mContext).onBackPressed();
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

        moreOutletsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOutletDialog();
            }
        });

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        ModelObject customPagerEnum = ModelObject.values()[position];
//        return mContext.getString(customPagerEnum.getTitleResId());
//    }

    public void showOutletDialog() {
        final DialogPlus outletDialog = DialogPlus.newDialog(mContext)
                .setContentHolder(new ViewHolder(R.layout.dialog_outlet_address_layout_test))
                .setGravity(Gravity.CENTER)
                .create();
        outletDialog.show();

        View outletView = outletDialog.getHolderView();
        TextView titleTv = outletView.findViewById(R.id.titleTv);
        TextView numOfOutletsTv = outletView.findViewById(R.id.numOfOutletsTv);
        RecyclerView outletList = outletView.findViewById(R.id.outletList);
        outletList.setLayoutManager(new LinearLayoutManager(mContext));

        titleTv.setText(m.getCompanyName());
        if (m.getCoupons().get(0).getCouponAtAvailableOutlets().size() == 1) {
            numOfOutletsTv.setText("Total " + m.getCoupons().get(0).getCouponAtAvailableOutlets().size() + " Outlet");
        } else {
            numOfOutletsTv.setText("Total " + m.getCoupons().get(0).getCouponAtAvailableOutlets().size() + " Outlets");
        }
        AllOutletAdapter adapter = new AllOutletAdapter(mContext, m.getCoupons().get(0).getCouponAtAvailableOutlets(),
               new Gson().toJson(m));
        outletList.setAdapter(adapter);

    }


}