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
import com.offeram.couponbouquet.UsedOffersActivity;
import com.offeram.couponbouquet.adapters.UsedOfferAdapter;
import com.offeram.couponbouquet.responses.AllUsedCoupons;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentUsedOffers extends Fragment {
    RecyclerView recyclerView;
    String userId = "", paymentId = "";
    UsedOfferAdapter adapter;
    ProgressDialog pd;
    private CardView nodataCard;
    private TextView txt_explore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_usedoffers, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerview);
        nodataCard = rootView.findViewById(R.id.nodataCard);
        txt_explore = rootView.findViewById(R.id.txt_explore);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        userId = Config.getSharedPreferences(getActivity(), "userId");
        paymentId = Config.getSharedPreferences(getActivity(), "paymentId");

        getUsedOffer();



        return rootView;
    }

    public void getUsedOffer() {
        if(Config.isConnectedToInternet(getActivity())){
            pd = new ProgressDialog(getActivity());
            pd.setCancelable(false);
            pd.setMessage("Loading...");
            pd.show();
            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
            Log.e("In UsedOffers", "Params : UserId -> " + userId + ", PaymentId -> " + paymentId);
            Call<AllUsedCoupons> call = apiInterface.getAllUsedCoupons(userId, paymentId,Config.getSharedPreferences(getActivity(),"cityID"));
            call.enqueue(new Callback<AllUsedCoupons>() {
                @Override
                public void onResponse(Call<AllUsedCoupons> call, Response<AllUsedCoupons> response) {
                    pd.dismiss();
                    AllUsedCoupons uc = response.body();
                    Log.e("In UsedOffers", "Response : " + new Gson().toJson(uc));
                    if(uc != null){
                        String balance=uc.getOfferam_coin_balance();
                        if(uc.getUserUsedCoupons().size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            nodataCard.setVisibility(View.GONE);
                            adapter = new UsedOfferAdapter(getActivity(), uc.getUserUsedCoupons(),balance,FragmentUsedOffers.this);
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

                    }

                }

                @Override
                public void onFailure(Call<AllUsedCoupons> call, Throwable t) {
                    pd.dismiss();
                    Log.e("In UsedOffersAct", "OnFailure Msg : " + t.getMessage());
                    Config.showDialog(getActivity(), Config.FailureMsg);
                }
            });

        } else {
            Config.showAlertForInternet(getActivity());
        }
    }
}
