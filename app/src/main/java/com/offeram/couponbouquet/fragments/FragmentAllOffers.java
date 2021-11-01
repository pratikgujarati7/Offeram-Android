package com.offeram.couponbouquet.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.offeram.couponbouquet.BuyNowActivity;
import com.offeram.couponbouquet.Config;
import com.offeram.couponbouquet.R;
import com.offeram.couponbouquet.RetroApiClient;
import com.offeram.couponbouquet.RetroApiInterface;
import com.offeram.couponbouquet.adapters.AllOfferAdapter;
import com.offeram.couponbouquet.models.AllOffer;
import com.offeram.couponbouquet.models.City;
import com.offeram.couponbouquet.responses.Common;
import com.offeram.couponbouquet.responses.GetAllOffers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentAllOffers extends Fragment {

    RecyclerView recyclerView;
    TextView soonTv;
    CardView nodataCard;
    FloatingActionButton fabBtn;
    AllOfferAdapter adapter;
    String type = "", userId = "", versionId = "", paymentId = "", latitude = "", longitude = "";
    List<AllOffer> offerList = new ArrayList<>();
    String selectedAreaStr = "";
    String[] areaIdArr;
    ProgressDialog pd;
    String add;
    Boolean isCallApi = true;
    String cityID, strcityName;
    String isFilter = "";

    public FragmentAllOffers() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_all_offers, container, false);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        if (bundle.containsKey("type")) {
            type = bundle.getString("type");
        }

        Log.e("In FrgmtAllOffers", "Type : " + type + " n SelectedArea : " + selectedAreaStr);
        soonTv = rootView.findViewById(R.id.soonTv);
        nodataCard = rootView.findViewById(R.id.nodataCard);
        fabBtn = rootView.findViewById(R.id.fabBtn);
        recyclerView = rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        userId = Config.getSharedPreferences(getActivity(), "userId");
        versionId = Config.getSharedPreferences(getActivity(), "versionId");
        paymentId = Config.getSharedPreferences(getActivity(), "paymentId");
        latitude = Config.getSharedPreferences(getActivity(), "latitude");
        longitude = Config.getSharedPreferences(getActivity(), "longitude");
        isFilter = Config.getSharedPreferences(getActivity(), "isFilterForCallApi");
        selectedAreaStr = Config.getSharedPreferences(getActivity(), "selectedAreaStr");

        if (Config.getSharedPreferences(getActivity(), "cityID") == null) {
            if (!latitude.equals("0") && !longitude.equals("0")) {
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
                    Address obj = addresses.get(0);
                    add = obj.getLocality();
                    Log.e("Address:", add);
                    Common cityjson = new Gson().fromJson(Config.getSharedPreferences(getActivity(), "splashResponse"), Common.class);

                    if (cityjson.getAllCity().size() > 0) {
                        for (int i = 0; i < cityjson.getAllCity().size(); i++) {
                            City city = cityjson.getAllCity().get(i);
                            strcityName = city.getCity_name();
                            if (strcityName.equals(add)) {
                                cityID = city.getCity_id();
                                Config.saveSharedPreferences(getActivity(), "cityID", cityID);
                            }
                        }
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        }

        if (paymentId != null && paymentId.equals("0")) {
            fabBtn.setVisibility(View.VISIBLE);
        } else {
            fabBtn.setVisibility(View.GONE);
        }

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BuyNowActivity.class);
                startActivity(intent);
            }
        });

        if (Config.isConnectedToInternet(getActivity())) {

            RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
//            Log.e("In FrgmtAllOffers", "Params : UserId -> " + userId + ", PaymentId -> " + paymentId
//                    + " n VersionId : " + versionId);
            Log.e("In FrgmtAllOffers", "Params : UserId -> " + userId + ", VersionId -> " + versionId
                    + ", PaymentId -> " + paymentId + ", Latitude -> " + latitude + " n Longitude -> " + longitude);
            Call<GetAllOffers> call = null;

            /*// Note :- Code before minimising loading
            if (selectedAreaStr != null && !selectedAreaStr.equalsIgnoreCase("")) {
                areaIdArr = selectedAreaStr.split(",");
                Log.e("In FrgmtAllOffers", "Area array length : " + areaIdArr.length);
                call = apiInterface.getAllOffersByArea(userId, paymentId, versionId, latitude, longitude, areaIdArr, Config.getSharedPreferences(getActivity(), "cityID"));
            } else {
                call = apiInterface.getAllOffers(userId, paymentId, versionId, latitude, longitude, Config.getSharedPreferences(getActivity(), "cityID"));
            }*/

            // Note :- For minimising loading while switching to different tabs and filtration for cities and areas
            if (isFilter != null && isFilter.equals("1")) {
                isCallApi = true;
            } else {
                if (Config.getSharedPreferences(getActivity(), "allMerchantsResponse") != null) {
                    isCallApi = false;
                } else {
                    isCallApi = true;
                }
            }
            if (selectedAreaStr != null && !selectedAreaStr.equalsIgnoreCase("")) {
                areaIdArr = selectedAreaStr.split(",");
                Log.e("In FrgmtAllOffers", "Area array length : " + areaIdArr.length);
                call = apiInterface.getAllOffersByArea(userId, paymentId, versionId, latitude, longitude, areaIdArr, Config.getSharedPreferences(getActivity(), "cityID"));
            } else {
                call = apiInterface.getAllOffers(userId, paymentId, versionId, latitude, longitude, Config.getSharedPreferences(getActivity(), "cityID"));
            }

            if (isCallApi) {
                pd = new ProgressDialog(getActivity());
                pd.setCancelable(false);
                pd.setMessage("Loading...");
                pd.show();

                call.enqueue(new Callback<GetAllOffers>() {
                    @Override
                    public void onResponse(Call<GetAllOffers> call, Response<GetAllOffers> response) {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        GetAllOffers go = response.body();
                        if (go != null) {
                            Config.saveSharedPreferences(getActivity(), "allMerchantsResponse", new Gson().toJson(go));
                            setDataToRecyclerView(go);
                        } else {
                            Config.showDialog(getActivity(), Config.FailureMsg);

                        }
                    }

                    @Override
                    public void onFailure(Call<GetAllOffers> call, Throwable t) {
                        pd.dismiss();
                        Log.e("In FrgmtAllOffers", "OnFailure Msg : " + t.getMessage());
                        Config.showDialog(getActivity(), Config.FailureMsg);
                    }
                });
            } else {
                GetAllOffers go = new Gson().fromJson(Config.getSharedPreferences(getActivity(), "allMerchantsResponse"), GetAllOffers.class);

                if (go != null)
                    setDataToRecyclerView(go);
            }
        } else {
            Config.showAlertForInternet(getActivity());
        }

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
//        Log.e("In FrgmtAllOffers", "OnResume");
    }

    public void setDataToRecyclerView(GetAllOffers go) {
//        Log.e("In FrgmtAllOffers", "SendData : " + new Gson().toJson(go));
        offerList.clear();
        if (type != null && !type.equalsIgnoreCase("")) {
            Log.e("In FrgmtAllOffers", "In If Type : " + type);
            recyclerView.setVisibility(View.VISIBLE);
            soonTv.setVisibility(View.GONE);
            nodataCard.setVisibility(View.GONE);
            if (type.equalsIgnoreCase("all")) {
//                Log.e("In FrgmtAllOffers", "In Inner If");
                if (go.getAllOffer().size() > 0) {
                    adapter = new AllOfferAdapter(getActivity(), go.getAllOffer(), new Gson().toJson(go), type);
                    recyclerView.setAdapter(adapter);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    nodataCard.setVisibility(View.VISIBLE);
//                    Config.showDialog(getActivity(), "No data available yet...");
                }
            } else if (type.equalsIgnoreCase("special offers")) {
//                Log.e("In FrgmtAllOffers", "In Else If");
                recyclerView.setVisibility(View.GONE);
                soonTv.setVisibility(View.VISIBLE);
            } else {
//                Log.e("In FrgmtAllOffers", "In Else");
                for (int i = 0; i < go.getAllOffer().size(); i++) {
                    if (go.getAllOffer().get(i).getCategoryId().equals(type)) {
                        offerList.add(go.getAllOffer().get(i));
                    }
                }
                if (offerList.size() > 0) {
                    adapter = new AllOfferAdapter(getActivity(), offerList, new Gson().toJson(go), type);
                    recyclerView.setAdapter(adapter);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    nodataCard.setVisibility(View.VISIBLE);
//                    Config.showDialog(getActivity(), "No data available yet...");
                }
            }
        } else {
            Config.showDialog(getActivity(), Config.FailureMsg);
        }
    }

}
