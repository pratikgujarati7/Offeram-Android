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
import com.offeram.couponbouquet.adapters.TransactionHistoryAdapter;
import com.offeram.couponbouquet.responses.GetAllFavorites;
import com.offeram.couponbouquet.responses.TransactionResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionHistory extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView titleTv;
    private ProgressDialog pd;
    private String userId;
    private TextView noDataTv;
    CardView nodataCard;
    private RecyclerView recyclerView;
    private TextView txt_coin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recyclerview);
        noDataTv = findViewById(R.id.noDataTv);
        txt_coin = findViewById(R.id.txt_coin);
        nodataCard = findViewById(R.id.nodataCard);
        recyclerView.setLayoutManager(new LinearLayoutManager(TransactionHistory.this));

        titleTv = (TextView) findViewById(R.id.titleTv);
        titleTv.setText("Offeram Coins");
        userId = Config.getSharedPreferences(TransactionHistory.this, "userId");
        getHistory();

    }

    private void getHistory() {
        if (Config.isConnectedToInternet(TransactionHistory.this)) {
            pd = new ProgressDialog(TransactionHistory.this);
            pd.setCancelable(false);
            pd.setMessage("Loading...");
            pd.show();
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Call<TransactionResponse> call = apiInterface.getTransactionHistory(userId);
            call.enqueue(new Callback<TransactionResponse>() {
                @Override
                public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                    pd.dismiss();
                    TransactionResponse gf = response.body();
                    Log.e("In FrgmtPinged", "Response : " + new Gson().toJson(response));
                    txt_coin.setText(gf.getOfferam_coin_balance());
                    if (gf.getTransactions().size()>0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        nodataCard.setVisibility(View.GONE);
                        if (gf.getTransactions().size() > 0) {
                            TransactionHistoryAdapter adapter = new TransactionHistoryAdapter(TransactionHistory.this, gf.getTransactions());
                            recyclerView.setAdapter(adapter);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                        }

                    } else {
                        recyclerView.setVisibility(View.GONE);
                        nodataCard.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<TransactionResponse> call, Throwable t) {
                    pd.dismiss();
                    Log.e("In FrgmtPinged", "OnFailure Msg : " + t.getMessage());
                    Config.showDialog(TransactionHistory.this, Config.FailureMsg);
                }
            });
        } else {
            Config.showAlertForInternet(TransactionHistory.this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
