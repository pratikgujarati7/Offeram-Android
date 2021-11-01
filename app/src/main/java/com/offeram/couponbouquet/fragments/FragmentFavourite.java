package com.offeram.couponbouquet.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.offeram.couponbouquet.adapters.StarredListAdapter;
import com.offeram.couponbouquet.responses.GetAllFavorites;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentFavourite extends Fragment {
    private RecyclerView recyclerView;
    private CardView nodataCard;
    private ProgressDialog pd;
    StarredListAdapter adapter;
    String userId = "", versionId = "", paymentId = "", latitude = "", longitude = "";
    private TextView txt_explore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favourite, container, false);
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
            Log.e("In FrgmtStarred", "Params : UserId -> " + userId + ", VersionId -> " + versionId
                    + ", PaymentId -> " + paymentId + ", Latitude -> " + latitude + " n Longitude -> " + longitude);
            Call<GetAllFavorites> call = apiInterface.getAllFavorites(userId, versionId, paymentId, latitude, longitude, Config.getSharedPreferences(getActivity(), "cityID"));
            call.enqueue(new Callback<GetAllFavorites>() {
                @Override
                public void onResponse(Call<GetAllFavorites> call, Response<GetAllFavorites> response) {
                    pd.dismiss();
                    GetAllFavorites gf = response.body();
                    Log.e("In FgrmtStarred", "Response : " + new Gson().toJson(response));
                    if (gf!=null) {
                        if (gf.getSuccess() == 1) {
                            recyclerView.setVisibility(View.VISIBLE);
                            nodataCard.setVisibility(View.GONE);
                            if (gf.getStarredList().size() > 0) {
                                adapter = new StarredListAdapter(getActivity(), gf.getStarredList());
                                recyclerView.setAdapter(adapter);
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
                    }else {
                        Config.showDialog(getActivity(), Config.FailureMsg);
                    }
                }

                @Override
                public void onFailure(Call<GetAllFavorites> call, Throwable t) {
                    pd.dismiss();
                    Log.e("In FrgmtStarred", "OnFailure Msg : " + t.getMessage());
                    Config.showDialog(getActivity(), Config.FailureMsg);
                }
            });
        } else {
            Config.showAlertForInternet(getActivity());
        }

        return rootView;
    }

}
