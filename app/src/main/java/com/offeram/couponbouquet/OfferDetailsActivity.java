package com.offeram.couponbouquet;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.offeram.couponbouquet.adapters.AllOutletAdapter;
import com.offeram.couponbouquet.models.Coupon;
import com.offeram.couponbouquet.models.CouponOutlet;
import com.offeram.couponbouquet.models.Merchant;
import com.offeram.couponbouquet.responses.Common;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.ViewHolder;

import org.apmem.tools.layouts.FlowLayout;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//public class OfferDetailsActivity extends AppCompatActivity implements View.OnClickListener {
public class OfferDetailsActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener, View.OnClickListener {

    //    Toolbar toolbar;
//    ImageView couponImageIv;
//    LinearLayout backLayout, shareLayout;
//    FlowLayout outletLayout;
//    TextView couponNameTv, endDateTv, areaNameTv, TnCTv, redeemBtn, buyNowBtn;
//    String couponDetails, merchantId, searchText;
    Merchant m;
    Coupon c;
    String companyName, paymentId, couponDetails, merchantId, searchText;
    List<CouponOutlet> co;
    LayoutInflater inflater;

    // New Design Components
    ImageView imageIv, backIv, starredIv, pingedIv;
    TextView titleTv, titleTv1, validDateTv, idTv, nameTv, redeemTv, tncValueTv, redeemBtn,statusIdTv;
    FloatingActionButton fabBtn;
    FlowLayout outletLayout;
    LinearLayout starLayout, pingLayout;
    RelativeLayout ll_idTv;
    int position = 0;
    SliderLayout slider;
    HashMap<String, String> url_maps = new HashMap<>();
    private String from;
    String merchantDetails = "", isStarred = "0";
    private String isPing="";
    private String message="You cannot redeem this offer as the coupon is already used";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details_test);

        final Intent intent = getIntent();
        if (intent != null) {
            Gson gson = new Gson();
            m = gson.fromJson(intent.getStringExtra("merchantDetails"), Merchant.class);
            c = gson.fromJson(intent.getStringExtra("couponDetails"), Coupon.class);
            position = intent.getIntExtra("position", 0);
            from = intent.getStringExtra("from");

            if (intent.hasExtra("message")){
                message=intent.getStringExtra("message");
            }
//            companyName = intent.getStringExtra("companyName");
//            merchantId = intent.getStringExtra("merchantId");
//            if (intent.hasExtra("searchText")) {
//                searchText = intent.getStringExtra("searchText");
//            }
        }
        Log.e("In OfferDetails", "Company Name : " + companyName);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(companyName);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        backLayout = (LinearLayout) findViewById(R.id.backLayout);
//        shareLayout = (LinearLayout) findViewById(R.id.shareLayout);
//        couponImageIv = (ImageView) findViewById(R.id.couponImageIv);
//        couponNameTv = (TextView) findViewById(R.id.couponNameTv);
//        endDateTv = (TextView) findViewById(R.id.endDateTv);
////        areaNameTv = (TextView) findViewById(R.id.areaNameTv);
//        TnCTv = (TextView) findViewById(R.id.TnCTv);
//        redeemBtn = (TextView) findViewById(R.id.redeemBtn);
//        buyNowBtn = (TextView) findViewById(R.id.buyNowBtn);

        starLayout = (LinearLayout) findViewById(R.id.starLayout);
        pingLayout = (LinearLayout) findViewById(R.id.pingLayout);
        ll_idTv = (RelativeLayout) findViewById(R.id.ll_idTv);
        slider = (SliderLayout) findViewById(R.id.slider);
        outletLayout = (FlowLayout) findViewById(R.id.outletLayout);
        imageIv = (ImageView) findViewById(R.id.imageIv);
        starredIv = (ImageView) findViewById(R.id.starredIv);
        pingedIv = (ImageView) findViewById(R.id.pingedIv);
        titleTv = (TextView) findViewById(R.id.titleTv);
        titleTv1 = (TextView) findViewById(R.id.titleTv1);
        validDateTv = (TextView) findViewById(R.id.validDateTv);
        idTv = (TextView) findViewById(R.id.idTv);
        statusIdTv = (TextView) findViewById(R.id.statusIdTv);
        nameTv = (TextView) findViewById(R.id.nameTv);
        redeemTv = (TextView) findViewById(R.id.redeemTv);
        tncValueTv = (TextView) findViewById(R.id.tncValueTv);
        backIv = (ImageView) findViewById(R.id.backIv);
        redeemBtn = (TextView) findViewById(R.id.redeemBtn);
        fabBtn = (FloatingActionButton) findViewById(R.id.fabBtn);

//        Glide.with(OfferDetailsActivity.this).load(c.getCouponImage()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true).into(couponImageIv);
//        couponNameTv.setText(c.getCouponTitle());

        url_maps = new HashMap<String, String>();
        for (int i = 0; i < m.getCoupons().size(); i++) {
            url_maps.put(i + "", m.getCoupons().get(i).getCouponImage());
//            if (coupons.get(i).getIsUsed().equals("0")) {
//                ++unUsedCoupons;
//            }
        }
        if (c.getIsUsed().equals("0")) {
            starredIv.setImageResource(R.drawable.ic_star_selected);
            pingedIv.setImageResource(R.drawable.ic_ping_selected);
            if (c.getIsStarred() == 0) {
                starredIv.setImageResource(R.drawable.ic_star_unselected);
                pingedIv.setImageResource(R.drawable.ic_ping_selected);
            } else if (c.getIsStarred() == 1) {
                starredIv.setImageResource(R.drawable.ic_star_selected);
                pingedIv.setImageResource(R.drawable.ic_ping_selected);
            }
//            holder.idTv.setTextSize(18);
//            holder.idTv.setBackgroundColor(context.getResources().getColor(R.color.textViewBgColor));
        } else if (c.getIsUsed().equals("1")) {
            starredIv.setImageResource(R.drawable.ic_star_used);
            pingedIv.setImageResource(R.drawable.ic_ping_used);
//            holder.idTv.setBackgroundColor(context.getResources().getColor(R.color.lightGrey));
        }
        if (from.equals("merchantdetail")) {
            isPing="0";
            starLayout.setVisibility(View.VISIBLE);
            pingLayout.setVisibility(View.VISIBLE);
            starLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (c.getIsUsed().equals("0")) {
                        final DialogPlus dialog = DialogPlus.newDialog(OfferDetailsActivity.this)
                                .setContentHolder(new ViewHolder(R.layout.dialog_confirmation_layout))
                                .setContentHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
                                .setCancelable(false)
                                .setGravity(Gravity.CENTER)
                                .create();
                        dialog.show();

                        View errorView = dialog.getHolderView();
                        TextView titleTv = (TextView) errorView.findViewById(R.id.titleTv);
                        Button yesBtn = (Button) errorView.findViewById(R.id.yesBtn);
                        final Button noBtn = (Button) errorView.findViewById(R.id.noBtn);
                        String message = "";
                        if (c.getIsStarred() == 0) {
                            message = "Are you sure you want to add this coupon to your wishlist ? ";
                        } else if (c.getIsStarred() == 1) {
                            message = "Are you sure you want to remove this coupon from your wishlist ? ";
                        }
                        titleTv.setText(message);

                        yesBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (Config.isConnectedToInternet(OfferDetailsActivity.this)) {
                                    if (c.getIsStarred() == 0) {
                                        isStarred = "1";
                                    } else if (c.getIsStarred() == 1) {
                                        isStarred = "0";
                                    }
                                    RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
                                    Call<Common> call = apiInterface.addToFavorite(Config.getSharedPreferences(OfferDetailsActivity.this, "userId"),
                                            c.getCouponId(), isStarred);
                                    call.enqueue(new Callback<Common>() {
                                        @Override
                                        public void onResponse(Call<Common> call, Response<Common> response) {
                                            Common co = response.body();
                                            if (co.getSuccess() == 1) {
                                                c.setIsStarred(Integer.parseInt(isStarred));
                                                if (c.getIsStarred() == 0) {
                                                    starredIv.setImageResource(R.drawable.ic_star_unselected);
                                                } else if (c.getIsStarred() == 1) {
                                                    starredIv.setImageResource(R.drawable.ic_star_selected);
                                                }
                                            } else {
                                                Config.showDialog(OfferDetailsActivity.this, Config.FailureMsg);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Common> call, Throwable t) {
                                            Log.e("In MerchantDetailsAdptr", "OnFailure Msg : " + t.getMessage());
                                            Config.showDialog(OfferDetailsActivity.this, Config.FailureMsg);
                                        }
                                    });
                                } else {
                                    Config.showAlertForInternet((Activity) OfferDetailsActivity.this);
                                }
                            }
                        });

                        noBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                    }
                }
            });
            pingLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (c.getIsUsed().equals("0")) {
                        Intent intent = new Intent(OfferDetailsActivity.this, PingActivity.class);
                        intent.putExtra("couponId", c.getCouponId());
                        startActivity(intent);
                    }
                }
            });
            if (c.getIsUsed().equals("0")) {
                idTv.setText((position + 1) + "");
                ll_idTv.setBackground(getResources().getDrawable(R.drawable.bg_coupon_used));
                if (c.getIs_pinged().equals("1")){
                    statusIdTv.setVisibility(View.VISIBLE);
                    ll_idTv.setBackground(getResources().getDrawable(R.drawable.bg_coupon_unused));
                    statusIdTv.setText("Pinged");
                    starredIv.setImageResource(R.drawable.ic_star_used);
                    pingedIv.setImageResource(R.drawable.ic_ping_used);
                }
//            holder.idTv.setTextSize(18);
//            holder.idTv.setBackgroundColor(context.getResources().getColor(R.color.textViewBgColor));
            } else if (c.getIsUsed().equals("1")) {
                statusIdTv.setVisibility(View.VISIBLE);
                idTv.setText((position + 1) + "");
                ll_idTv.setBackground(getResources().getDrawable(R.drawable.bg_coupon_unused));
//            holder.idTv.setBackgroundColor(context.getResources().getColor(R.color.lightGrey));
                if (c.getIs_pinged().equals("1")){
                    statusIdTv.setText("Pinged");
                }else {
                    statusIdTv.setText("Used");
                }

            }
        } else if (from.equals("pinglist")) {

            isPing="1";
            starLayout.setVisibility(View.GONE);
            pingLayout.setVisibility(View.GONE);

            if (c.getIsUsed().equals("0")) {
                idTv.setText("1");
                ll_idTv.setBackground(getResources().getDrawable(R.drawable.bg_coupon_used));
                if (c.getIs_pinged().equals("1")){
                    statusIdTv.setVisibility(View.VISIBLE);
                    ll_idTv.setBackground(getResources().getDrawable(R.drawable.bg_coupon_unused));
                    statusIdTv.setText("Pinged");
                    starredIv.setImageResource(R.drawable.ic_star_used);
                    pingedIv.setImageResource(R.drawable.ic_ping_used);
                }

            } else if (c.getIsUsed().equals("1")) {
                statusIdTv.setVisibility(View.VISIBLE);
                idTv.setText("1");
                ll_idTv.setBackground(getResources().getDrawable(R.drawable.bg_coupon_unused));
                statusIdTv.setText("Used");
                if (c.getIs_pinged().equals("1")){
                    statusIdTv.setText("Pinged");
                }else {
                    statusIdTv.setText("Used");
                }
//            holder.idTv.setBackgroundColor(context.getResources().getColor(R.color.lightGrey));
            }

        }

        for (String name : url_maps.keySet()) {
            DefaultSliderView textSliderView = new DefaultSliderView(OfferDetailsActivity.this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(OfferDetailsActivity.this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            slider.addSlider(textSliderView);
        }

        if (m.getCoupons().size() == 1) {
            slider.stopAutoCycle();
        }

        slider.setPresetTransformer(SliderLayout.Transformer.Default);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
//        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(4000);

        slider.addOnPageChangeListener(this);

        try {
//            String tempDate = new SimpleDateFormat("dd MMM, yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(c.getEndDate()));
//            Log.e("In OfferDetails", "tempDate : " + tempDate);
//            endDateTv.setText(new SimpleDateFormat("dd MMM, yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(c.getEndDate())));
            validDateTv.setText("Valid Till : " + c.getEndDate());
        } catch (Exception e) {
            Log.e("In OfferDetails", "Excp : " + e.getMessage());
        }

//        TnCTv.setText(Html.fromHtml(c.getTermsConditions()));
        Glide.with(OfferDetailsActivity.this).load(m.getCompanyLogo())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .thumbnail(0.5f)
                .error(R.mipmap.ic_launcher)
                .into(imageIv);
        titleTv.setText(m.getCompanyName());
        titleTv1.setText(m.getCompanyName());
//        if (c.getIsUsed().equals("0")) {
//            idTv.setText((position + 1) + "");
//            idTv.setBackground(getResources().getDrawable(R.drawable.bg_coupon_used));
////            holder.idTv.setTextSize(18);
////            holder.idTv.setBackgroundColor(context.getResources().getColor(R.color.textViewBgColor));
//        } else if (c.getIsUsed().equals("1")) {
//            idTv.setText((position + 1) + "");
//            idTv.setBackground(getResources().getDrawable(R.drawable.bg_coupon_unused));
////            holder.idTv.setBackgroundColor(context.getResources().getColor(R.color.lightGrey));
//        }
        nameTv.setText(c.getCouponTitle());
        if (c.getNumOfRedeem() == 0) {
            redeemTv.setVisibility(View.INVISIBLE);
        } else {
            redeemTv.setVisibility(View.VISIBLE);
            redeemTv.setText(c.getNumOfRedeem() + " redeemed");
        }
        tncValueTv.setText(Html.fromHtml(c.getTermsConditions(), null, new HtmlTagHandler()));

//        if (Config.getSharedPreferences(OfferDetailsActivity.this, "isDemo").equals("1")) {
//            paymentId = Config.getSharedPreferences(OfferDetailsActivity.this, "demoPaymentId");
//        } else if (Config.getSharedPreferences(OfferDetailsActivity.this, "isDemo").equals("0")) {
//            paymentId = Config.getSharedPreferences(OfferDetailsActivity.this, "paymentId");
//        }
//        if (paymentId != null && paymentId.equals("0")) {
//            redeemBtn.setBackgroundColor(getResources().getColor(R.color.darkGreyColor));
//            redeemBtn.setVisibility(View.GONE);
//            buyNowBtn.setVisibility(View.VISIBLE);
//        } else {
//
//            redeemBtn.setVisibility(View.VISIBLE);
//            buyNowBtn.setVisibility(View.GONE);
//            if (c.getIsUsed().equals("1")) {
//                redeemBtn.setBackgroundColor(getResources().getColor(R.color.darkGreyColor));
//            } else {
//                redeemBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//            }
//        }

        paymentId = Config.getSharedPreferences(OfferDetailsActivity.this, "paymentId");
        if (paymentId != null && paymentId.equals("0")) {
            fabBtn.setVisibility(View.VISIBLE);
            redeemBtn.setVisibility(View.GONE);
        } else {
            fabBtn.setVisibility(View.GONE);
            redeemBtn.setVisibility(View.VISIBLE);
            if (c.getIsUsed().equals("1")) {
                redeemBtn.setBackgroundColor(getResources().getColor(R.color.lightGrey));
            } else {
                redeemBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                if (c.getIs_pinged().equals("1")){
                    redeemBtn.setBackgroundColor(getResources().getColor(R.color.lightGrey));
                }
            }
        }

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OfferDetailsActivity.this, BuyNowActivity.class);
                startActivity(intent);
            }
        });

        co = Config.sortListDistanceWise(c.getCouponAtAvailableOutlets());
        inflater = LayoutInflater.from(OfferDetailsActivity.this);
        for (int i = 0; i < co.size(); i++) {
            View childView = inflater.inflate(R.layout.layout_dynamic_outlet_textview, outletLayout, false);
            TextView outletNameTv = (TextView) childView.findViewById(R.id.outletNameTv);
            outletNameTv.setText(co.get(i).getAreaName());
            outletNameTv.setTag(co.get(i).getCouponOutletsId());
            outletNameTv.setOnClickListener(this);
            outletLayout.addView(childView);
//            TextView outletTv = new TextView(OfferDetailsActivity.this);
//            outletTv.setText(co.get(i).getAreaName());
//            outletTv.setTextColor(getResources().getColor(R.color.textColorBlue));
//            outletTv.setPadding(5, 0, 5, 0);
//            outletTv.setTag(co.get(i).getOutletId());
//            outletTv.setOnClickListener(this);
//            outletLayout.addView(outletTv);
        }

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        redeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (c.getIsUsed().equals("1")) {
                    final DialogPlus redeemDisableDialog = DialogPlus.newDialog(OfferDetailsActivity.this)
                            .setContentHolder(new ViewHolder(R.layout.dialog_ok_layout))
                            .setGravity(Gravity.CENTER)
                            .create();
                    redeemDisableDialog.show();

                    View redeemView = redeemDisableDialog.getHolderView();
                    TextView titleTv = (TextView) redeemView.findViewById(R.id.titleTv);
                    TextView messageTv = (TextView) redeemView.findViewById(R.id.messageTv);
                    Button okBtn = (Button) redeemView.findViewById(R.id.okBtn);
                    titleTv.setVisibility(View.GONE);
                    messageTv.setText("You cannot redeem this offer as the coupon is already used");
                    messageTv.setText(message);

                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            redeemDisableDialog.dismiss();
                        }
                    });
                } else {
//                    finish();
                    Intent intent = new Intent(OfferDetailsActivity.this, RedeemCouponActivity.class);
                    intent.putExtra("couponDetails", couponDetails);
                    intent.putExtra("companyName", m.getCompanyName());
                    intent.putExtra("couponId", c.getCouponId());
                    intent.putExtra("ping_id", c.getPing_id());
                    intent.putExtra("is_ping", c.getIs_pinged());
//                        intent.putExtra("merchantId", merchantId);
//                        if(searchText != null && !searchText.equalsIgnoreCase("")) {
//                            intent.putExtra("searchText", searchText);
//                        }
                    startActivity(intent);
                }
            }
        });

//        backLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//
//        shareLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String message = "Get \"" + c.getCouponTitle() + "\"" + " from " + "\"" + companyName + "\". "
//                        + "Download your Offeram Coupon Bouquet Now.";
//                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
//                startActivity(Intent.createChooser(sharingIntent, "Share using..."));
//            }
//        });

//        outletLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

//        redeemBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Config.getSharedPreferences(OfferDetailsActivity.this, "paymentId") != null &&
//                        Config.getSharedPreferences(OfferDetailsActivity.this, "paymentId").equals("0")) {
//                    final DialogPlus redeemDisableDialog = DialogPlus.newDialog(OfferDetailsActivity.this)
//                            .setContentHolder(new ViewHolder(R.layout.dialog_ok_layout))
//                            .setGravity(Gravity.CENTER)
//                            .create();
//                    redeemDisableDialog.show();
//
//                    View redeemView = redeemDisableDialog.getHolderView();
//                    TextView titleTv = (TextView) redeemView.findViewById(R.id.titleTv);
//                    TextView messageTv = (TextView) redeemView.findViewById(R.id.messageTv);
//                    Button okBtn = (Button) redeemView.findViewById(R.id.okBtn);
//                    titleTv.setVisibility(View.GONE);
//                    messageTv.setText("You cannot redeem this offer as you are using the application as view only mode");
//
//                    okBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            redeemDisableDialog.dismiss();
//                        }
//                    });
//                } else {
//                    /*  final DialogPlus redeemDialog = DialogPlus.newDialog(OfferDetailsActivity.this)
//                            .setContentHolder(new ViewHolder(R.layout.dialog_confirmation_layout))
//                            .setGravity(Gravity.CENTER)
//                            .create();
//                    redeemDialog.show();
//
//                    View redeemView = redeemDialog.getHolderView();
//                    TextView titleTv = (TextView) redeemView.findViewById(R.id.titleTv);
//                    Button yesBtn = (Button) redeemView.findViewById(R.id.yesBtn);
//                    Button noBtn = (Button) redeemView.findViewById(R.id.noBtn);
//                    titleTv.setText("Please select a method from following to redeem this coupon");
//                    yesBtn.setText("Enter PIN");
//                    noBtn.setText("Scan Code");
//
//                    yesBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            redeemDialog.dismiss();
//                            Intent intent = new Intent(OfferDetailsActivity.this, EnterPinActivity.class);
//                            intent.putExtra("couponId", c.getCouponId());
//                            startActivity(intent);
//                        }
//                    });
//
//                    noBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            redeemDialog.dismiss(); // ZXing library to be implemented
//                            Intent intent = new Intent(OfferDetailsActivity.this, RedeemCouponActivity.class);
//                            intent.putExtra("couponId", c.getCouponId());
//                            startActivity(intent);
//                        }
//                    });  */
//                    if (c.getIsUsed().equals("1")) {
//                        final DialogPlus redeemDisableDialog = DialogPlus.newDialog(OfferDetailsActivity.this)
//                                .setContentHolder(new ViewHolder(R.layout.dialog_ok_layout))
//                                .setGravity(Gravity.CENTER)
//                                .create();
//                        redeemDisableDialog.show();
//
//                        View redeemView = redeemDisableDialog.getHolderView();
//                        TextView titleTv = (TextView) redeemView.findViewById(R.id.titleTv);
//                        TextView messageTv = (TextView) redeemView.findViewById(R.id.messageTv);
//                        Button okBtn = (Button) redeemView.findViewById(R.id.okBtn);
//                        titleTv.setVisibility(View.GONE);
//                        messageTv.setText("You cannot redeem this offer as the coupon is already used");
//
//                        okBtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                redeemDisableDialog.dismiss();
//                            }
//                        });
//                    } else {
//                        finish();
//                        Intent intent = new Intent(OfferDetailsActivity.this, RedeemCouponActivity.class);
//                        intent.putExtra("couponDetails", couponDetails);
//                        intent.putExtra("companyName", companyName);
//                        intent.putExtra("couponId", c.getCouponId());
////                        intent.putExtra("merchantId", merchantId);
////                        if(searchText != null && !searchText.equalsIgnoreCase("")) {
////                            intent.putExtra("searchText", searchText);
////                        }
//                        startActivity(intent);
//                    }
//                }
//
//            }
//        });
//
//        buyNowBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent(OfferDetailsActivity.this, ChangeVersionActivity.class);
//                startActivity(intent1);
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        Log.e("In OfferDetails", "In OnClick View GetTag Val : " + v.getTag().toString());
        for (int i = 0; i < co.size(); i++) {
            if (v.getTag().toString().equals(co.get(i).getCouponOutletsId())) {
                Log.e("In OfferDetails", "In OnClick In If View GetTag Val : " + co.get(i).getCouponOutletsId());
                final DialogPlus outletDialog = DialogPlus.newDialog(OfferDetailsActivity.this)
                        .setContentHolder(new ViewHolder(R.layout.dialog_outlet_address_layout_test))
                        .setGravity(Gravity.CENTER)
                        .create();
                outletDialog.show();

                View outletView = outletDialog.getHolderView();
                TextView titleTv = outletView.findViewById(R.id.titleTv);
                TextView numOfOutletsTv = outletView.findViewById(R.id.numOfOutletsTv);
                RecyclerView outletList = outletView.findViewById(R.id.outletList);
                outletList.setLayoutManager(new LinearLayoutManager(OfferDetailsActivity.this));

                titleTv.setText(m.getCompanyName());
                if (co.size() == 1) {
                    numOfOutletsTv.setText("Total " + co.size() + " Outlet");
                } else {
                    numOfOutletsTv.setText("Total " + co.size() + " Outlets");
                }
                AllOutletAdapter adapter = new AllOutletAdapter(OfferDetailsActivity.this, co, new Gson().toJson(m));
                outletList.setAdapter(adapter);

                /* View outletView = outletDialog.getHolderView();
                ImageView closeIv = (ImageView) outletView.findViewById(R.id.closeIv);
                TextView merchantNameTv = (TextView) outletView.findViewById(R.id.merchantNameTv);
                TextView areaNameTv = (TextView) outletView.findViewById(R.id.areaNameTv);
                TextView addressValueTv = (TextView) outletView.findViewById(R.id.addressValueTv);
                Button callBtn = (Button) outletView.findViewById(R.id.callBtn);

                merchantNameTv.setText(companyName);
                areaNameTv.setText(co.get(i).getAreaName());
                addressValueTv.setText(co.get(i).getAddress());
                final String phoneNumber = co.get(i).getPhoneNumber();

                closeIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        outletDialog.dismiss();
                    }
                });

                callBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        outletDialog.dismiss();
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + phoneNumber));
                        startActivity(callIntent);
                    }
                });  */

//                final Dialog outletDialog = new Dialog(OfferDetailsActivity.this);
//                outletDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                outletDialog.setContentView(R.layout.dialog_outlet_address_layout);
//                outletDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//                ImageView closeIv = (ImageView) outletDialog.findViewById(R.id.closeIv);
//                TextView merchantNameTv = (TextView) outletDialog.findViewById(R.id.merchantNameTv);
//                TextView areaNameTv = (TextView) outletDialog.findViewById(R.id.areaNameTv);
//                TextView addressValueTv = (TextView) outletDialog.findViewById(R.id.addressValueTv);
//                Button callBtn = (Button) outletDialog.findViewById(R.id.callBtn);
//
//                merchantNameTv.setText(companyName);
//                areaNameTv.setText(co.get(i).getAreaName());
//                addressValueTv.setText(co.get(i).getAddress());
//                final String phoneNumber = co.get(i).getPhoneNumber();
//
//                closeIv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.e("In OfferDetails", "In CloseBtn");
//                        outletDialog.dismiss();
//                    }
//                });
//
//                callBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        outletDialog.dismiss();
//                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
//                        callIntent.setData(Uri.parse("tel:" + phoneNumber));
//                        startActivity(callIntent);
//                    }
//                });
//
//                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85);
//                int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.55);
//                outletDialog.getWindow().setLayout(width, height);
//
//                outletDialog.show();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_redeem_coupon_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int itemId = item.getItemId();
//        if (itemId == R.id.action_share) {
//            String message = "Get \"" + c.getCouponTitle() + "\"" + " from " + "\"" + companyName + "\". "
//                    + Config.getSharedPreferences(OfferDetailsActivity.this, "couponMessage");
//            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//            sharingIntent.setType("text/plain");
//            sharingIntent.putExtra(Intent.EXTRA_TEXT, message);
//            startActivity(Intent.createChooser(sharingIntent, "Share using..."));
//        } else {
//            onBackPressed();
//        }
        return true;
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
