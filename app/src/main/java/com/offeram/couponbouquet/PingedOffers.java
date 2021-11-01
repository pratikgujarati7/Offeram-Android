package com.offeram.couponbouquet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.offeram.couponbouquet.adapters.PingedListAdapter;
import com.offeram.couponbouquet.fragments.FragmentPinged;
import com.offeram.couponbouquet.responses.GetAllFavorites;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PingedOffers extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView titleTv;
    private RecyclerView recyclerView;
    private TextView txt_explore;
    CardView nodataCard;
    private String userId,versionId,paymentId,latitude,longitude;
    private ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pinged);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        titleTv = (TextView) findViewById(R.id.titleTv);
        titleTv.setText("Pinged Offers");
        recyclerView = findViewById(R.id.recyclerview);
        txt_explore = findViewById(R.id.txt_explore);
        nodataCard = findViewById(R.id.nodataCard);
        recyclerView.setLayoutManager(new LinearLayoutManager(PingedOffers.this));
        //userId ="11987";
        userId = Config.getSharedPreferences(PingedOffers.this, "userId");
        versionId = Config.getSharedPreferences(PingedOffers.this, "versionId");
        paymentId = Config.getSharedPreferences(PingedOffers.this, "paymentId");
        latitude = Config.getSharedPreferences(PingedOffers.this, "latitude");
        longitude = Config.getSharedPreferences(PingedOffers.this, "longitude");
        if (Config.isConnectedToInternet(PingedOffers.this)) {
            pd = new ProgressDialog(PingedOffers.this);
            pd.setCancelable(false);
            pd.setMessage("Loading...");
            pd.show();
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Log.e("In FrgmtPinged", "Params : UserId -> " + userId + ", VersionId -> " + versionId
                    + ", PaymentId -> " + paymentId + ", Latitude -> " + latitude + " n Longitude -> " + longitude);
            Call<GetAllFavorites> call = apiInterface.getAllPingedOffers(userId, versionId, paymentId, latitude, longitude,Config.getSharedPreferences(PingedOffers.this,"cityID"));
            call.enqueue(new Callback<GetAllFavorites>() {
                @Override
                public void onResponse(Call<GetAllFavorites> call, Response<GetAllFavorites> response) {
                    pd.dismiss();
                    GetAllFavorites gf = response.body();
                    Log.e("In FrgmtPinged", "Response : " + new Gson().toJson(response));
                    if (gf.getSuccess() == 1) {
                        recyclerView.setVisibility(View.VISIBLE);
                        nodataCard.setVisibility(View.GONE);
                        if (gf.getPingedList().size() > 0) {

                            PingedListAdapter adapter = new PingedListAdapter(PingedOffers.this, gf.getPingedList(),new Gson().toJson(gf));
                            recyclerView.setAdapter(adapter);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            nodataCard.setVisibility(View.VISIBLE);
                            txt_explore.setVisibility(View.VISIBLE);
                            txt_explore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(PingedOffers.this,HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }

                    } else {
                        recyclerView.setVisibility(View.GONE);
                        nodataCard.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<GetAllFavorites> call, Throwable t) {
                    pd.dismiss();
                    Log.e("In FrgmtPinged", "OnFailure Msg : " + t.getMessage());
                    Config.showDialog(PingedOffers.this, Config.FailureMsg);
                }
            });
        } else {
            Config.showAlertForInternet(PingedOffers.this);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
