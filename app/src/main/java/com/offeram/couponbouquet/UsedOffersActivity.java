package com.offeram.couponbouquet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.offeram.couponbouquet.adapters.UsedOfferAdapter;
import com.offeram.couponbouquet.models.AllOffer;
import com.offeram.couponbouquet.models.UsedOffer;
import com.offeram.couponbouquet.responses.AllUsedCoupons;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsedOffersActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView titleTv;
    RecyclerView recyclerView;
    String userId = "", paymentId = "";
    UsedOfferAdapter adapter;
    ProgressDialog pd;
    private CardView nodataCard;
    private TextView txt_explore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_used_offers);

        toolbar = findViewById(R.id.toolbar);
        titleTv = toolbar.findViewById(R.id.titleTv);
        titleTv.setText("Used Offers");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerview);
        nodataCard = findViewById(R.id.nodataCard);
        txt_explore = findViewById(R.id.txt_explore);
        recyclerView.setLayoutManager(new LinearLayoutManager(UsedOffersActivity.this));

        userId = Config.getSharedPreferences(UsedOffersActivity.this, "userId");
        paymentId = Config.getSharedPreferences(UsedOffersActivity.this, "paymentId");

        if(Config.isConnectedToInternet(UsedOffersActivity.this)){
            pd = new ProgressDialog(UsedOffersActivity.this);
            pd.setCancelable(false);
            pd.setMessage("Loading...");
            pd.show();
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Log.e("In UsedOffers", "Params : UserId -> " + userId + ", PaymentId -> " + paymentId);
            Call<AllUsedCoupons> call = apiInterface.getAllUsedCoupons(userId, paymentId,Config.getSharedPreferences(UsedOffersActivity.this,"cityID"));
            call.enqueue(new Callback<AllUsedCoupons>() {
                @Override
                public void onResponse(Call<AllUsedCoupons> call, Response<AllUsedCoupons> response) {
                    pd.dismiss();
                    AllUsedCoupons uc = response.body();
                    Log.e("In UsedOffers", "Response : " + new Gson().toJson(uc));
                    if(uc != null){

                        if(uc.getUserUsedCoupons().size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            nodataCard.setVisibility(View.GONE);
                            adapter = new UsedOfferAdapter(UsedOffersActivity.this, uc.getUserUsedCoupons(),uc.getOfferam_coin_balance());
                            recyclerView.setAdapter(adapter);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            nodataCard.setVisibility(View.VISIBLE);
                            txt_explore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(UsedOffersActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }

                    }

                }

                @Override
                public void onFailure(Call<AllUsedCoupons> call, Throwable t) {
                    pd.dismiss();
                    Log.e("In UsedOffersAct", "OnFailure Msg : " + t.getMessage());
                    Config.showDialog(UsedOffersActivity.this, Config.FailureMsg);
                }
            });

        } else {
            Config.showAlertForInternet(UsedOffersActivity.this);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

}
