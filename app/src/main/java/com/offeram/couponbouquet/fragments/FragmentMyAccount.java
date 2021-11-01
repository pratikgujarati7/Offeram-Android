package com.offeram.couponbouquet.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.offeram.couponbouquet.Config;
import com.offeram.couponbouquet.FriendsAndFamily;
import com.offeram.couponbouquet.OfferamCoins;
import com.offeram.couponbouquet.R;
import com.offeram.couponbouquet.RefferAndEarn;
import com.offeram.couponbouquet.RetroApiClient;
import com.offeram.couponbouquet.RetroApiInterface;
import com.offeram.couponbouquet.TransactionHistory;
import com.offeram.couponbouquet.UpdateProfile;
import com.offeram.couponbouquet.adapters.MyAccountListAdapter;
import com.offeram.couponbouquet.models.RedeemersModel;
import com.offeram.couponbouquet.responses.Common;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentMyAccount extends Fragment {

    Toolbar toolbar;
    TextView titleTv, nameTv, contactTv, validDateTv, txt_learnmore, txt_top_reedmer, txt_offeramcoin, txt_top_price, txt_top_name;
    ImageView editIv, img_profile, img_wallet;
    CircleImageView profileIv;
    RecyclerView recyclerView;
    MyAccountListAdapter adapter;
    //    int[] images = new int[]{R.drawable.ic_area_selected, R.drawable.fail, R.drawable.ic_bird, R.drawable.fail,
//            R.drawable.ic_area_selected, R.drawable.ic_location};

    //, R.drawable.pinged_offers, R.drawable.used_offers

    int[] images = new int[]{R.drawable.ic_refer_earn, R.drawable.ic_my_account_about_us,
            R.drawable.ic_terms_conditions, R.drawable.ic_rate_us, R.drawable.ic_customer_care,
            R.drawable.ic_terms_conditions, R.drawable.ic_terms_conditions, R.drawable.ic_terms_conditions, R.drawable.logout};
    String userDetails = "";
    ArrayList<String> list = new ArrayList<>();
    ProgressDialog pd;
    private RelativeLayout rel_coin, rel_family;
    private View view_coin, view_family;
    private RelativeLayout rel_offeramcoin, rel_offeramfriend;
    private String coin;


    public FragmentMyAccount() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_account, container, false);
        setHasOptionsMenu(true);

        toolbar = rootView.findViewById(R.id.toolbar);
        titleTv = toolbar.findViewById(R.id.titleTv);
        titleTv.setText("My Account");

        nameTv = rootView.findViewById(R.id.nameTv);
        contactTv = rootView.findViewById(R.id.contactTv);
        validDateTv = rootView.findViewById(R.id.validDateTv);
        txt_learnmore = rootView.findViewById(R.id.txt_learnmore);
        txt_top_reedmer = rootView.findViewById(R.id.txt_top_reedmer);
        txt_offeramcoin = rootView.findViewById(R.id.txt_offeramcoin);
        txt_top_name = rootView.findViewById(R.id.txt_top_name);
        txt_top_price = rootView.findViewById(R.id.txt_top_price);
        profileIv = rootView.findViewById(R.id.profileIv);
        img_profile = rootView.findViewById(R.id.img_profile);
        img_wallet = rootView.findViewById(R.id.img_wallet);
        rel_coin = rootView.findViewById(R.id.rel_coin);
        rel_family = rootView.findViewById(R.id.rel_family);
        view_coin = rootView.findViewById(R.id.view_coin);
        view_family = rootView.findViewById(R.id.view_family);
        editIv = rootView.findViewById(R.id.editIv);
        rel_offeramcoin = rootView.findViewById(R.id.rel_offeramcoin);
        rel_offeramfriend = rootView.findViewById(R.id.rel_offeramfriend);
        recyclerView = rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        editIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateProfile.class);
                intent.putExtra("userDetails", userDetails);
                startActivity(intent);
            }
        });

        rel_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_family.setVisibility(View.GONE);
                view_coin.setVisibility(View.VISIBLE);
                rel_offeramcoin.setVisibility(View.VISIBLE);
                rel_offeramfriend.setVisibility(View.GONE);
            }
        });
        rel_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_family.setVisibility(View.VISIBLE);
                view_coin.setVisibility(View.GONE);
                rel_offeramcoin.setVisibility(View.GONE);
                rel_offeramfriend.setVisibility(View.VISIBLE);
            }
        });
        txt_learnmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OfferamCoins.class);
                intent.putExtra("coin", coin);
                startActivity(intent);
            }
        });
        txt_top_reedmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FriendsAndFamily.class);
                intent.putExtra("userDetail", userDetails);
                startActivity(intent);
            }
        });

        img_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TransactionHistory.class);
                startActivity(intent);
            }
        });

        //TODO Display one record below friend and family

        return rootView;
    }

    public void getListData() {
//        list.add("Gift Cards");

        list.clear();
        list.add("Refer & Earn");
//        list.add("Pinged Offers");
//        list.add("Used Offers");
        list.add("About Us");
        list.add("Terms & Conditions");
        list.add("Rate Us");
        list.add("Customer Care");
        list.add("Privacy Policy");
        list.add("Disclaimer");
        list.add("Refund & Cancellation");
        list.add("Logout");
    }

    @Override
    public void onResume() {
        super.onResume();
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        String userId = Config.getSharedPreferences(getActivity(), "userId");
        Log.e("In FrgmtMyAccount", "Params : UserId -> " + userId);
        RetroApiInterface apiInterface = RetroApiClient.getClient().create(RetroApiInterface.class);
        Call<Common> call = apiInterface.getProfileDetails(userId);
        call.enqueue(new Callback<Common>() {
            @Override
            public void onResponse(Call<Common> call, Response<Common> response) {
                pd.dismiss();
                Common c = response.body();
                userDetails = new Gson().toJson(c);
                if (c != null) {
                    if (c.getSuccess() == 1) {
                        List<RedeemersModel> rm = c.getToptenredeemerslist();
                        for (int i = 0; i < rm.size(); i++) {
                            txt_top_name.setText(rm.get(0).getRedeemer_name());
                            txt_top_price.setText(rm.get(0).getRedeemed_amount());
                            Glide.with(getActivity())
                                    .load(rm.get(0).getRedeemer_profile_image_url())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .error(R.drawable.offeram_one)
                                    .into(img_profile);
                        }
                        coin = c.getUserofferamcoinbalance();
                        txt_offeramcoin.setText(c.getUserofferamcoinbalance() + " Earned");
                        Config.saveSharedPreferences(getActivity(), "referUrl", c.getReferal_url());
                        Config.saveSharedPreferences(getActivity(), "referCode", c.getReferal_code());
                        Config.saveSharedPreferences(getActivity(), "offeramCoin", c.getUserofferamcoinbalance());
                        if (c.getUserName() != null && !c.getUserName().equalsIgnoreCase("")) {
                            nameTv.setText(c.getUserName());
                        } else {
                            nameTv.setText("User");
                        }
                        if (c.getUserImage() != null && !c.getUserImage().equalsIgnoreCase("")) {
                            Glide.with(getActivity())
                                    .load(c.getUserImage())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .error(R.drawable.ic_launcher)
                                    .into(profileIv);
                        } else {
                            Glide.with(getActivity())
                                    .load(R.drawable.ic_launcher)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(profileIv);
                        }
                        contactTv.setText(c.getUserContact());
                        if (c.getValidDate() != null && !c.getValidDate().equalsIgnoreCase("")) {
                            validDateTv.setText("Valid Till : " + c.getValidDate());
                        } else {
                            validDateTv.setText("");
                        }

                        getListData();
                        adapter = new MyAccountListAdapter(getActivity(), list, images, new Gson().toJson(c));
                        recyclerView.setAdapter(adapter);

                    } else {
                        Config.showDialog(getActivity(), c.getMessage());
                    }
                } else {
                    Config.showDialog(getActivity(), Config.FailureMsg);
                }
            }

            @Override
            public void onFailure(Call<Common> call, Throwable t) {
                pd.dismiss();
                Log.e("In FrgmtMyAccount", "OnFailure Excp Msg : " + t.getMessage());
                Config.showDialog(getActivity(), Config.FailureMsg);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e("In FrgmtMyAccount", "In OnCreateOptions");
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
//        toolbar.inflateMenu(R.menu.menu_my_account);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getActivity().onBackPressed();
        return true;
    }

}
