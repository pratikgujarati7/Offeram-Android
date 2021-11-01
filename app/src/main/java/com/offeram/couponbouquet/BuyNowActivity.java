package com.offeram.couponbouquet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.offeram.couponbouquet.responses.Common;
import com.offeram.couponbouquet.responses.PromoDiscount;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyNowActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView titleTv, promoCodeTv, promoDescTv, promoCodeTv2, promoDescTv2, mrpTv, discountTv, totalPriceTv, applyBtn,
            payNowBtn, havePromoTv;
    LinearLayout applyPromoLayout, appliedLayout, descriptionLayout, discountLayout;
    TextInputLayout promoCodeInput;
    EditText promoCodeEt;
    String userId = "", versionId = "15", isApplied = "0", amount = "", splashResponse = "",dedutedAmount="";
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_now);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleTv = toolbar.findViewById(R.id.titleTv);
        titleTv.setText("Buy Now");

        applyPromoLayout = findViewById(R.id.applyPromoLayout);
        appliedLayout = findViewById(R.id.appliedLayout);
        descriptionLayout = findViewById(R.id.descriptionLayout);
        discountLayout = findViewById(R.id.discountLayout);
        promoCodeTv = findViewById(R.id.promoCodeTv);
        promoDescTv = findViewById(R.id.promoDescTv);
        promoCodeTv2 = findViewById(R.id.promoCodeTv2);
        promoDescTv2 = findViewById(R.id.promoDescTv2);
        mrpTv = findViewById(R.id.mrpTv);
        totalPriceTv = findViewById(R.id.totalPriceTv);
        havePromoTv = findViewById(R.id.havePromoTv);
        discountTv = findViewById(R.id.discountTv);
        applyBtn = findViewById(R.id.applyBtn);
        payNowBtn = findViewById(R.id.payNowBtn);
        promoCodeInput = findViewById(R.id.promoCodeInput);
        promoCodeEt = findViewById(R.id.promoCodeEt);

        userId = Config.getSharedPreferences(BuyNowActivity.this, "userId");
        splashResponse = Config.getSharedPreferences(BuyNowActivity.this, "splashResponse");
        final Common c = new Gson().fromJson(splashResponse, Common.class);



        applyPromoLayout.setVisibility(View.GONE);
        appliedLayout.setVisibility(View.GONE);
        discountLayout.setVisibility(View.GONE);
        havePromoTv.setVisibility(View.VISIBLE);
        payNowBtn.setText("Pay " + getString(R.string.rupee) + " " + c.getPurchasePrice() + " Now");
        mrpTv.setText(getString(R.string.rupee) + " " + c.getPurchasePrice());
        totalPriceTv.setText(getString(R.string.rupee) + " " + c.getPurchasePrice());

        String promo="Wow!!! You are just one step away from Dil Kholke Discounts.<br><br>Get <b>all the amazing offers</b> listed in the app for <b>all the cities</b> at just "+ getString(R.string.rupee) + " " + c.getPurchasePrice()  +" with 1 year validity.<br><br><b>Apply Promocode</b>";

        havePromoTv.setText(Html.fromHtml(promo));

        havePromoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                havePromoTv.setVisibility(View.GONE);
                applyPromoLayout.setVisibility(View.VISIBLE);
            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.hideSoftKeyboard(BuyNowActivity.this);
                Boolean isValid = true;
                isApplied = "1";
                promoCodeInput.setErrorEnabled(false);
                if(promoCodeEt.getText().toString().equals("")){
                    isValid = false;
                    promoCodeInput.setErrorEnabled(true);
                    promoCodeInput.setError("Must enter promo code");
                }

                if(isValid){
                    if(Config.isConnectedToInternet(BuyNowActivity.this)){
                        pd = new ProgressDialog(BuyNowActivity.this);
                        pd.setMessage("Loading...");
                        pd.setCancelable(false);
                        pd.show();

                        RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
                        Call<PromoDiscount> call = apiInterface.getPromoCodeDiscount(userId, versionId, promoCodeEt.getText().toString());
                        call.enqueue(new Callback<PromoDiscount>() {
                            @Override
                            public void onResponse(Call<PromoDiscount> call, Response<PromoDiscount> response) {
                                pd.dismiss();
                                PromoDiscount pd = response.body();
                                if(pd.getSuccess() == 1){
                                    appliedLayout.setVisibility(View.VISIBLE);
                                    discountLayout.setVisibility(View.VISIBLE);
                                    descriptionLayout.setVisibility(View.VISIBLE);
                                    promoCodeTv.setText("#" + promoCodeEt.getText().toString());
                                    promoCodeTv2.setText("#" + promoCodeEt.getText().toString());
                                    promoDescTv.setText(pd.getCodeDescription());
                                    promoDescTv2.setText(pd.getCodeDescription());

                                    if (pd.getDiscountType().equals("2")) {
                                        // When the coupon is redeemed or the discount is 100% -> discount type -> 2
                                        mrpTv.setText(getString(R.string.rupee) + " " + c.getPurchasePrice());
                                        discountTv.setText("- " + getString(R.string.rupee) + " " + c.getPurchasePrice());
                                        totalPriceTv.setText(getString(R.string.rupee));
                                        Config.saveSharedPreferences(BuyNowActivity.this, "isDemo", "0");
                                        Config.saveSharedPreferences(BuyNowActivity.this, "paymentId", pd.getPaymentId());
                                        Config.saveSharedPreferences(BuyNowActivity.this, "versionId", "15");
                                        Config.saveSharedPreferences(BuyNowActivity.this, "versionName", pd.getVersionName());
                                        Config.saveSharedPreferences(BuyNowActivity.this, "sideMenuText", pd.getSideMenuText());
                                        Log.e("In BuyNowActivity", "VersionId, VersionName, PaymentId, Side Menu Text : " + "0" + ", " + pd.getVersionName() + ", " + pd.getPaymentId() + ", " + pd.getSideMenuText());
                                        Intent intent = new Intent(BuyNowActivity.this, PaymentSuccess.class);
                                        intent.putExtra("from", "Redeem");
                                        startActivity(intent);

                                    } else {
                                        int discountType = 0;
                                        double discountInRs = 0.0, discountInPer = 0.0, deductedRs = 0.0;
                                        discountType = Integer.parseInt(pd.getDiscountType());
                                        if (discountType == 0)
                                            discountInRs = Double.parseDouble(pd.getDiscountValue());
                                        else
                                            discountInPer = Double.parseDouble(pd.getDiscountValue());

                                        double payableAmount = 0.0, paymentAmount = 0.0;
                                        paymentAmount = Double.parseDouble(c.getPurchasePrice());
                                        if (discountInRs == 0.0) {
//                                          calculate percentage of discount
                                            deductedRs = ((paymentAmount * discountInPer) / 100);
                                            payableAmount = paymentAmount - deductedRs;

                                        } else {
//                                          deduct rupees directly from payment amount
                                            deductedRs = discountInRs;
                                            payableAmount = paymentAmount - deductedRs;
                                        }

                                        mrpTv.setText(getString(R.string.rupee) + " " + c.getPurchasePrice());
                                        dedutedAmount=deductedRs+"";
                                        if(String.valueOf(deductedRs).contains(".0"))
                                            dedutedAmount = dedutedAmount.substring(0, dedutedAmount.length() - 2);
                                        discountTv.setText("- " + getString(R.string.rupee) + " " + dedutedAmount);


                                        amount = payableAmount + "";
                                        if(String.valueOf(payableAmount).contains(".0"))
                                            amount = amount.substring(0, amount.length() - 2);
                                        Log.e("In BuyNowActivity", "Payable Amt : " + amount);

                                        totalPriceTv.setText(getString(R.string.rupee) + " " + amount);

                                        payNowBtn.setText("Pay " + getString(R.string.rupee) + " " + amount + " Now");
//                                        if (payableAmount > 0) {
////                                          Integration of PayUMoney
//                                            Intent intent = new Intent(BuyNowActivity.this, PayTmActivity.class);
//                                            intent.putExtra("versionId", "15");
//                                            intent.putExtra("amount", payableAmount + "");
//                                            intent.putExtra("isApplied","1");
//                                            intent.putExtra("promotionalCode", "");
//                                            intent.putExtra("isApplied", "1");
//                                            BuyNowActivity.this.startActivity(intent);
//                                        }
                                    }
                                } else {
                                    final DialogPlus dialog = DialogPlus.newDialog(BuyNowActivity.this)
                                            .setContentHolder(new ViewHolder(R.layout.dialog_ok_layout))
                                            .setContentHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
                                            .setCancelable(false)
                                            .setGravity(Gravity.CENTER)
                                            .create();
                                    dialog.show();

                                    View errorView = dialog.getHolderView();
                                    TextView titleTv = (TextView) errorView.findViewById(R.id.titleTv);
                                    TextView messageTv = (TextView) errorView.findViewById(R.id.messageTv);
                                    Button okBtn = (Button) errorView.findViewById(R.id.okBtn);
                                    titleTv.setVisibility(View.GONE);
                                    messageTv.setText(pd.getMessage());
                                    okBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Call<PromoDiscount> call, Throwable t) {
                                pd.dismiss();
                                Log.e("In BuyNowActivity", "OnFailure Excp : " + t.getMessage());
                                Config.showDialog(BuyNowActivity.this, Config.FailureMsg);
                            }
                        });

                    } else {
                        Config.showAlertForInternet(BuyNowActivity.this);
                    }
                }
            }
        });

        payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isApplied.equals("1")){
                    Intent intent = new Intent(BuyNowActivity.this, PayTmActivity.class);
                    intent.putExtra("versionId", versionId);
                    intent.putExtra("amount", amount);
                    intent.putExtra("promotionalCode", promoCodeEt.getText().toString());
                    intent.putExtra("isApplied", isApplied);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(BuyNowActivity.this, PayTmActivity.class);
                    intent.putExtra("versionId", versionId);
                    intent.putExtra("amount", c.getPurchasePrice());
                    intent.putExtra("promotionalCode", "");
                    intent.putExtra("isApplied", isApplied);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

}
