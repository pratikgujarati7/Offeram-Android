package com.offeram.couponbouquet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.offeram.couponbouquet.adapters.AllOfferAdapter;
import com.offeram.couponbouquet.adapters.SearchResultAdapter;
import com.offeram.couponbouquet.responses.GetAllOffers;
import com.offeram.couponbouquet.responses.GetSearchedResult;
import com.offeram.couponbouquet.responses.SearchByCoupon;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResults extends AppCompatActivity {

    Toolbar toolbar;
    TextView titleTv, noDataTv;
    RecyclerView resultList;
    String userId = "", versionId = "15", paymentId = "", latitude = "", longitude = "", searchText = "";
    ProgressDialog pd;
    SearchResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Intent intent = getIntent();
        if (intent.hasExtra("searchText")) {
            searchText = intent.getStringExtra("searchText");
        }
        Log.e("In SearchResults", "Search Text Val : " + searchText);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleTv = toolbar.findViewById(R.id.titleTv);
        noDataTv = findViewById(R.id.noDataTv);
        resultList = findViewById(R.id.resultList);
        resultList.setLayoutManager(new LinearLayoutManager(SearchResults.this));
        titleTv.setText("Search Results");

        if (searchText != null && !searchText.equalsIgnoreCase("")) {
            resultList.setVisibility(View.VISIBLE);
            noDataTv.setVisibility(View.GONE);
            userId = Config.getSharedPreferences(SearchResults.this, "userId");
//            versionId = Config.getSharedPreferences(SearchResults.this, "versionId");
            paymentId = Config.getSharedPreferences(SearchResults.this, "paymentId");
            latitude = Config.getSharedPreferences(SearchResults.this, "latitude");
            longitude = Config.getSharedPreferences(SearchResults.this, "longitude");

            if (Config.isConnectedToInternet(SearchResults.this)) {
                pd = new ProgressDialog(SearchResults.this);
                pd.setCancelable(false);
                pd.setMessage("Loading...");
                pd.show();
                RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
//            Log.e("In FrgmtAllOffers", "Params : UserId -> " + userId + ", PaymentId -> " + paymentId
//                    + " n VersionId : " + versionId);
                Log.e("In SearchResults", "Params : UserId -> " + userId + ", VersionId -> " + versionId
                        + ", PaymentId -> " + paymentId + ", Latitude -> " + latitude + " n Longitude -> " + longitude);
                Call<GetSearchedResult> call = apiInterface.searchByCouponOrCompany(userId, versionId, paymentId,
                        searchText, latitude, longitude, Config.getSharedPreferences(SearchResults.this, "cityID"));
                call.enqueue(new Callback<GetSearchedResult>() {
                    @Override
                    public void onResponse(Call<GetSearchedResult> call, Response<GetSearchedResult> response) {
                        pd.dismiss();
                        GetSearchedResult sr = response.body();
                        if (sr != null) {
                            if (sr.getSuccess() == 1) {
                                adapter = new SearchResultAdapter(SearchResults.this, sr.getSearchList());
                                resultList.setAdapter(adapter);
                            } else {
                                noDataTv.setVisibility(View.VISIBLE);
                                resultList.setVisibility(View.GONE);
                            }
                        } else {
                            Config.showDialog(SearchResults.this, Config.FailureMsg);
                        }
                    }

                    @Override
                    public void onFailure(Call<GetSearchedResult> call, Throwable t) {
                        pd.dismiss();
                        Log.e("In SearchResults", "OnFailure Msg : " + t.getMessage());
                        Config.showDialog(SearchResults.this, Config.FailureMsg);
                    }
                });
            } else {
                Config.showAlertForInternet(SearchResults.this);
            }
        } else {
            noDataTv.setVisibility(View.VISIBLE);
            resultList.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
