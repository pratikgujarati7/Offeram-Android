package com.offeram.couponbouquet.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.offeram.couponbouquet.Config;
import com.offeram.couponbouquet.R;
import com.offeram.couponbouquet.RetroApiClient;
import com.offeram.couponbouquet.RetroApiInterface;
import com.offeram.couponbouquet.adapters.StarredListAdapter;
import com.offeram.couponbouquet.responses.GetAllFavorites;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentStarred extends Fragment {

    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView titleTv, noDataTv;
    StarredListAdapter adapter;
    String userId = "", versionId = "", paymentId = "", latitude = "", longitude = "";
    ProgressDialog pd;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter pagerAdapter;
    private int[] tabIcons = {
            R.drawable.favourites_tab,
            R.drawable.recieved_pings_tab,
            R.drawable.sent_pings_tab,
            R.drawable.used_offers_tab

    };
    private String[] navLabels = {
            "Favourites",
            "Received Ping",
            "Pinged Offers",
            "Used Offers"

    };
    private String from;

    public FragmentStarred() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_starred, container, false);
        setHasOptionsMenu(true);

        //Toolbar is set temporarily as pinged tab is hidden
        toolbar = rootView.findViewById(R.id.toolbar);
        titleTv = toolbar.findViewById(R.id.titleTv);
        titleTv.setText("Activities");

//        recyclerView = rootView.findViewById(R.id.recyclerview);
//        noDataTv = rootView.findViewById(R.id.noDataTv);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        userId = Config.getSharedPreferences(getActivity(), "userId");
        versionId = Config.getSharedPreferences(getActivity(), "versionId");
        paymentId = Config.getSharedPreferences(getActivity(), "paymentId");
        latitude = Config.getSharedPreferences(getActivity(), "latitude");
        longitude = Config.getSharedPreferences(getActivity(), "longitude");

        tabLayout = rootView.findViewById(R.id.tabLayout);
        viewPager = rootView.findViewById(R.id.viewPager);
        pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            from=bundle.getString("from");
            if (from.equals("pingedOffer")){
                TabLayout.Tab tab = tabLayout.getTabAt(1);
                tab.select();
            }
        }


        return rootView;
    }


    private void setupTabIcons() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            LinearLayout ll = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.layout_custom_tab, null);
            ImageView tabImageIv = ll.findViewById(R.id.tabImageIv);
            TextView tabTextTv = ll.findViewById(R.id.tabTextTv);
            tabTextTv.setText(navLabels[i]);
            tabImageIv.setVisibility(View.VISIBLE);
            tabImageIv.setImageResource(tabIcons[i]);
            tabLayout.getTabAt(i).setCustomView(ll);
        }
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Bundle bundle = null;
            switch (position) {
                case 0:
                    fragment = new FragmentFavourite();
                    return fragment;

                case 1:
                    fragment = new FragmentPinged();
                    return fragment;
                case 2:
                    fragment = new FragmentPingOffers();
                    return fragment;
                case 3:
                    fragment = new FragmentUsedOffers();
                    return fragment;

                default:
                    return null;
            }
        }
        @Override
        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "Favourites";
//                case 1:
//                    return "Used Offers";
//                case 2:
//                    return "Pinged Offers";
//                case 3:
//                    return "Received Pings";
//            }
            return null;
        }


        @Override
        public int getCount() {
            return 4;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e("In FrgmtStarred", "In OnCreateOptions");
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getActivity().onBackPressed();
        return true;
    }

}
