package com.offeram.couponbouquet.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.offeram.couponbouquet.adapters.PingedOffersAdapter;
import com.offeram.couponbouquet.responses.GetAllFavorites;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentPingOffers extends Fragment {
    RecyclerView recyclerView;
    CardView nodataCard;
    PingedOffersAdapter adapter;
    String userId = "", versionId = "", paymentId = "", latitude = "", longitude = "";
    ProgressDialog pd;
    private TextView txt_explore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pinged, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerview);
        nodataCard = rootView.findViewById(R.id.nodataCard);
        txt_explore = rootView.findViewById(R.id.txt_explore);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userId = Config.getSharedPreferences(getActivity(), "userId");
        versionId = Config.getSharedPreferences(getActivity(), "versionId");
        paymentId = Config.getSharedPreferences(getActivity(), "paymentId");
        latitude = Config.getSharedPreferences(getActivity(), "latitude");
        longitude = Config.getSharedPreferences(getActivity(), "longitude");

        if (Config.isConnectedToInternet(getActivity())) {
            pd = new ProgressDialog(getActivity());
            pd.setCancelable(false);
            pd.setMessage("Loading...");
            pd.show();
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Log.e("In FrgmtPinged", "Params : UserId -> " + userId + ", VersionId -> " + versionId
                    + ", PaymentId -> " + paymentId + ", Latitude -> " + latitude + " n Longitude -> " + longitude);
            Call<GetAllFavorites> call = apiInterface.my_pinged_offers(userId, versionId, paymentId, latitude, longitude,Config.getSharedPreferences(getActivity(),"cityID"));
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
                            adapter = new PingedOffersAdapter(getActivity(), gf.getPingedList(),"");
                            recyclerView.setAdapter(adapter);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            nodataCard.setVisibility(View.VISIBLE);
                            txt_explore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(getActivity(), HomeActivity.class);
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
                                Intent intent=new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        });
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

        return rootView;
    }
}
