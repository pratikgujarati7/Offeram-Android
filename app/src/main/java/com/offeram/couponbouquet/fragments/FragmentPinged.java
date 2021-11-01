package com.offeram.couponbouquet.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.offeram.couponbouquet.Config;
import com.offeram.couponbouquet.HomeActivity;
import com.offeram.couponbouquet.R;
import com.offeram.couponbouquet.RetroApiClient;
import com.offeram.couponbouquet.RetroApiInterface;
import com.offeram.couponbouquet.adapters.PingedListAdapter;
import com.offeram.couponbouquet.adapters.StarredListAdapter;
import com.offeram.couponbouquet.responses.GetAllFavorites;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentPinged extends Fragment {

    RecyclerView recyclerView;
    CardView nodataCard;
    PingedListAdapter adapter;
    String userId = "", versionId = "", paymentId = "", latitude = "", longitude = "";
    ProgressDialog pd;
    private TextView txt_explore, noDataTv;

    public FragmentPinged() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pinged, container, false);
        setHasOptionsMenu(true);

        recyclerView = rootView.findViewById(R.id.recyclerview);
        nodataCard = rootView.findViewById(R.id.nodataCard);
        txt_explore = rootView.findViewById(R.id.txt_explore);
        noDataTv = rootView.findViewById(R.id.noDataTv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //userId = "3009";  // Static Data For testing purpose
        userId = Config.getSharedPreferences(getActivity(), "userId");
        versionId = Config.getSharedPreferences(getActivity(), "versionId");
        paymentId = Config.getSharedPreferences(getActivity(), "paymentId");
        latitude = Config.getSharedPreferences(getActivity(), "latitude");
        longitude = Config.getSharedPreferences(getActivity(), "longitude");

        getPinged();


        return rootView;
    }

    public void getPinged() {
        if (Config.isConnectedToInternet(getActivity())) {
            pd = new ProgressDialog(getActivity());
            pd.setCancelable(false);
            pd.setMessage("Loading...");
            pd.show();
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Log.e("In FrgmtPinged", "Params : UserId -> " + userId + ", VersionId -> " + versionId
                    + ", PaymentId -> " + paymentId + ", Latitude -> " + latitude + " n Longitude -> " + longitude);
            Call<GetAllFavorites> call = apiInterface.getAllPingedOffers(userId, versionId, paymentId, latitude, longitude, Config.getSharedPreferences(getActivity(), "cityID"));
            call.enqueue(new Callback<GetAllFavorites>() {
                @Override
                public void onResponse(Call<GetAllFavorites> call, Response<GetAllFavorites> response) {
                    pd.dismiss();
                    GetAllFavorites gf = response.body();
                    Log.e("In FrgmtPinged", "Response : " + new Gson().toJson(response));
                    if (gf != null) {
                        if (gf.getSuccess() == 1) {
                            recyclerView.setVisibility(View.VISIBLE);
                            nodataCard.setVisibility(View.GONE);
                            if (gf.getPingedList().size() > 0) {
                                adapter = new PingedListAdapter(getActivity(), gf.getPingedList(), "", FragmentPinged.this);
                                recyclerView.setAdapter(adapter);
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                nodataCard.setVisibility(View.VISIBLE);
                                noDataTv.setText("Oops! You have not been pinged by anyone yet. Start sharing offers by Ping An Offer now!");
                                txt_explore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                });
                            }

                        } else {
                            recyclerView.setVisibility(View.GONE);
                            nodataCard.setVisibility(View.VISIBLE);
                            txt_explore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            });
                        }
                    } else {
                        Config.showDialog(getActivity(), Config.FailureMsg);
                    }
                }

                @Override
                public void onFailure(Call<GetAllFavorites> call, Throwable t) {
                    pd.dismiss();
                    Log.e("In FrgmtPinged", "OnFailure Msg : " + t.getMessage());
                    Config.showDialog(getActivity(), Config.FailureMsg);
                }
            });
        } else {
            Config.showAlertForInternet(getActivity());
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e("In FrgmtPinged", "In OnCreateOptions");
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getActivity().onBackPressed();
        return true;
    }

}
